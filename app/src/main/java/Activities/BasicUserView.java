package Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import Activities.Fragments.EventsTab;
import Activities.Fragments.HomeTab;
import Activities.Fragments.MyFragment;
import Activities.Fragments.MyHorsesTab;
import Activities.Fragments.SearchTab;
import Application.MyApplication;
import DataControllers.Contact;
import DataControllers.DataFetcher;
import DataControllers.DatabaseObject;
import DataControllers.Horse;
import DataControllers.Permission;
import DataControllers.User;

import static android.view.View.GONE;

public class BasicUserView extends AppCompatActivity implements View.OnClickListener {

//view resources
    private CoordinatorLayout layout;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ProgressBar loadingIcon;
    private ImageButton administratorButton;
    private ImageButton profileButton;
    private ImageButton cameraButton;

    int[] drawables;
    MyFragment[] fragments;
    MyTask listener;

//logic resources
    MyApplication application;
    DataFetcher data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__basic_user);

        initializeResources();
        loadData();
    }
    private void initializeResources(){
        application = (MyApplication) this.getApplication();
        data = new DataFetcher();
    }
    private void loadData(){
        loadAllHorses();
        loadAllPermissions();
        loadAllResources();
        loadAllContacts();
        loadAllUsers();
    }

    private void loadAllHorses(){
        Task<List<DatabaseObject>> getHorsesTask = data.getAll("horse");
        MyTask fetchHorsesListener = new MyTask("getAllHorses");
        getHorsesTask.addOnCompleteListener(fetchHorsesListener);
    }
    private void loadAllPermissions(){
        Task<List<DatabaseObject>> getPermissionsTask = data.getAll("permission");
        MyTask fetchPermissionsListener = new MyTask("getAllPermissions");
        getPermissionsTask.addOnCompleteListener(fetchPermissionsListener);
    }
    private void loadAllResources(){
        Task<List<List<String>>> getResourcesTask = data.getResources();
        MyTask fetchResourcesListener = new MyTask("getAllResources");
        getResourcesTask.addOnCompleteListener(fetchResourcesListener);
    }
    private void loadAllContacts(){
        Task<List<DatabaseObject>> getContactsTask = data.getAll("contact");
        MyTask fetchAllContactsTask = new MyTask("getAllContacts");
        getContactsTask.addOnCompleteListener(fetchAllContactsTask);
    }
    private void loadAllUsers(){
        Task<List<DatabaseObject>> getUsersTask = data.getAll("user");
        MyTask fetchAllUsersTask = new MyTask("getAllUsers");
        getUsersTask.addOnCompleteListener(fetchAllUsersTask);
    }
    private class MyTask implements OnCompleteListener {
        String purpose;
        private MyTask(String type){
            purpose = type;
        }
        @Override
        public void onComplete(@NonNull Task task){
            log("task completed: " + purpose);
            switch(purpose){
                case "getAllHorses":
                    getAllHorsesFromTask(task.getResult());
                    break;
                case "getAllPermissions":
                    getAllPermissionsFromTask(task.getResult());
                    break;
                case "getAllResources":
                    log("get all resources completed");
                    getAllResourcesFromTask(task.getResult());
                    break;
                case "getAllContacts":
                    getAllContactsFromTask(task.getResult());
                    break;
                case "getAllUsers":
                    log("called get all users");
                    getAllUsersFromTask(task.getResult());
                    break;
                default:
                    break;
            }
            checkForCompletion();
        }
    }
    private void getAllHorsesFromTask(Object result){
        List<DatabaseObject> allHorseObjects  = (List<DatabaseObject>) result;
        List<Horse> newHorses = new ArrayList<Horse>();
        for (DatabaseObject horseObject: allHorseObjects){
            Horse horse = (Horse) horseObject;
            newHorses.add(horse);
        }
        application.setAllHorses(newHorses);
    }
    private void getAllPermissionsFromTask(Object result){
        List<DatabaseObject> permissionObjects  = (List<DatabaseObject>) result;
        List<Permission> permissions = new ArrayList<Permission>();
        for (DatabaseObject permissionObject: permissionObjects){
            Permission permission = (Permission) permissionObject;
            permissions.add(permission);
        }
        application.setAllPermissions(permissions);
    }
    private void getAllResourcesFromTask(Object result){
        List<List<String>> returnedResources = (List<List<String>>) result;
        log("about to set all resources");
        application.setResources(returnedResources);
    }
    private void getAllContactsFromTask(Object result){
        List<DatabaseObject> contactObjects = (List<DatabaseObject>) result;
        List<Contact> contacts = new ArrayList<Contact>();
        for (DatabaseObject contactObject: contactObjects){
            Contact contact = (Contact) contactObject;
            contacts.add(contact);
        }
        application.setAllContacts(contacts);
    }
    private void getAllUsersFromTask(Object result){
        log("about to get users from results");
        List<DatabaseObject> userObjects = (List<DatabaseObject>) result;
        log("cast to results");
        List<User> users = new ArrayList<User>();
        for (DatabaseObject userObject: userObjects){
            User user = (User) userObject;
            users.add(user);
        }
       application.setAllUsers(users);
    }

    private void checkForCompletion(){
        if (application.loadingIsComplete()){
            log("completed loading");
            Contact contact = application.getContact(application.getUser().key());
            if (contact == null){
                //user does not exist; go to profile!
                sendNewUserToProfile();
            }else{
                application.setContact(contact);
                log("contact set");
                User user = application.getUser(contact.key());
                log("user gotten");
                application.setUser(user);
                completeInitialization();
            }
        }
    }


    private void sendNewUserToProfile(){
        Intent intent = new Intent(getApplicationContext(), Profile.class);
        intent.putExtra("user", application.getUser());
        startActivityForResult(intent, 0);
    }


    private void completeInitialization(){
        initializeViewObjects();
        initializeButtonActions();
        initializeTabNavigation();
        initializeTabMonitor();
        configureAdministrativePrivelages();
        stopLoadingIcon();
    }


    private void navigateTo(Class destination){
        Context context = getApplicationContext();
        Intent i = new Intent(context, destination);
        startActivityForResult(i, 1);
    }

    private void initializeViewObjects(){
        layout = (CoordinatorLayout) findViewById(R.id.layout);
        loadingIcon = (ProgressBar) findViewById(R.id.loading_icon);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        administratorButton = (ImageButton) findViewById(R.id.administrator_button);
        cameraButton = (ImageButton) findViewById(R.id.camera_button);
        profileButton = (ImageButton) findViewById(R.id.profile_button);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbarTitle = (TextView) findViewById(R.id.main_toolbar_title);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        drawables = new int[4];
        drawables[0] = R.drawable.selector__home_tab_icon;
        drawables[1] = R.drawable.selector__my_horses_tab_icon;
        drawables[2] = R.drawable.selector__search_tab_icon;
        drawables[3] = R.drawable.selector__events_tab_icon;
    }
    private void initializeButtonActions(){
        cameraButton.setOnClickListener(this);
        profileButton.setOnClickListener(this);
        administratorButton.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.administrator_button:
               // navigateTo(AdministratorView.class);
                break;
            case R.id.profile_button:
                navigateTo(Profile.class);
                break;
            case R.id.camera_button:
                //go to camera view
                break;
            default:
                //do nothing
                break;
        }
        if (v.getId() == R.id.administrator_button){
            switchToAdministratorView();
        }
    }
    private void initializeTabNavigation(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        initializeViewPager();
        tabLayout.setupWithViewPager(viewPager);
        addTabIcons();
    }

    private void initializeViewPager(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Fragment[] fragments = initializeFragments();
        for (Fragment tab: fragments){
            adapter.addFragment(tab, "");
        }
        viewPager.setAdapter(adapter);
    }
    private Fragment[] initializeFragments(){
        fragments = new MyFragment[4];
        fragments[0] = new HomeTab();
        fragments[1] = new MyHorsesTab();
        fragments[2] = new SearchTab();
        fragments[3] = new EventsTab();
        return fragments;
    }
    private void addTabIcons(){

        for (int tabNumber = 0; tabNumber < 4; tabNumber++){
            View homeTab = getLayoutInflater().inflate(R.layout.custom_tab_item, null);

            homeTab.findViewById(R.id.icon).setBackgroundResource(drawables[tabNumber]);
            tabLayout.getTabAt(tabNumber).setCustomView(homeTab);
        }
    }
    private void initializeTabMonitor(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //get the tab
                String title = "";
                int selectedIndex = tab.getPosition();
                switch (selectedIndex){
                    case 0:
                        title = "Home";
                        break;
                    case 1:
                        title="My Horses";
                        break;
                    case 2:
                        title = "Search";
                        break;
                    case 3:
                        title = "Today";
                        break;
                    default:
                        title = "Calendar";
                        break;
                }
                toolbarTitle.setText(title);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                Fragment testHome = new HomeTab();

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
    private void stopLoadingIcon(){
        loadingIcon.setVisibility(View.GONE);
    }






    private void configureAdministrativePrivelages(){
//        if (!user.getType().matches("Administrator")){
//            administratorButton.setVisibility(GONE);
//        }
    }

    private void disableAdminPrivelages(){
        administratorButton.setVisibility(GONE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

                if (resultCode == 0){
                    //profile view returning
                    User returnedUser = (User) data.getSerializableExtra("user");
                    Contact returnedContact = (Contact) data.getSerializableExtra("contact");

//                    user = returnedUser;
//                    contact = returnedContact;
                }else if (resultCode == 1){
                    //horse detail view returning

                }


        } catch (Exception ex) {
            Toast.makeText(BasicUserView.this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

    }




    private void switchToAdministratorView(){
        Context context = getApplicationContext();
        Intent intent = new Intent(context, AdministratorView.class);
        startActivityForResult(intent, 1);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
            viewPager.setOffscreenPageLimit(4);

        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    private void log(String message){
        Log.v("IMPORTANT", message);
    }
}