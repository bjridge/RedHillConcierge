package Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import DataControllers.Contact;
import DataControllers.DataFetcher;
import DataControllers.DatabaseObject;
import DataControllers.Horse;
import DataControllers.Permission;
import DataControllers.User;

import static android.view.View.GONE;

public class BasicUserView extends AppCompatActivity implements View.OnClickListener {

    private boolean isLoaded = false;
    private boolean userLoaded = false;
    private boolean contactLoaded = false;

    //toolbar resources
    private Toolbar toolbar;
    private TextView toolbarTitle;

    //tab navigation resources
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ProgressBar loadingIcon;

    ImageButton administratorButton;
    ImageButton profileButton;
    ImageButton cameraButton;

    CoordinatorLayout layout;

    DataFetcher controller;

    int[] drawables;

    MyFragment[] fragments;

    MyTask listener;

    User user;
    Contact contact;
    List<Horse> horses;
    List<Permission> permissions;
    List<List<String>> resources;

    DataFetcher data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__basic_user);
        log("new main activity");
        data = new DataFetcher();

        checkForNewUser();

        //if that user does not get anywhere, do all of this stuff!

    }
    private void checkForNewUser(){
        getUserAndContactFromIntent();
        if (userJustLoggedIn()){
            fetchUser();
        }else{
            listener = new MyTask();
            fetchHorses();
        }
    }
    private void getUserAndContactFromIntent(){
        Intent i = getIntent();
        user = (User) i.getSerializableExtra("user");
        contact = (Contact) i.getSerializableExtra("contact");
    }
    private boolean userJustLoggedIn(){
        boolean userIsIncomplete = user.getType().matches("incomplete");
        return userIsIncomplete;
    }
    private void fetchUser(){
        Task<DatabaseObject> getUserTask = data.getObject("user", user.key());
        listener = new MyTask().forUser();
        getUserTask.addOnCompleteListener(listener);
    }
    private class MyTask implements OnCompleteListener {

        String purpose;

        private MyTask(){
        }

        public MyTask forUser(){
            purpose = "tryToGetUser";
            return this;
        }
        public MyTask forContact(){
            purpose = "getContact";
            return this;
        }
        public MyTask forHorses(){
            purpose = "getHorses";
            return this;
        }
        private MyTask forPermissions(){
            purpose = "getPermissions";
            return this;
        }
        private MyTask forResources(){
            log("created forResources properly");

            purpose = "getResources";
            return this;
        }

        @Override
        public void onComplete(@NonNull Task task){
            switch(purpose){
                case "tryToGetUser":
                    if (task.getResult() == null){
                        sendNewUserToProfile();
                    }else{
                        user = (User) task.getResult();
                        log("just got the user" + user.getLastName() + user.getFirstName() + user.key());
                        fetchContact();
                    }
                    break;
                case "getContact":
                    contact = (Contact) task.getResult();
                    log("just got the contact" + contact.getName());

                    fetchHorses();
                    break;
                case "getHorses":
                    List<DatabaseObject> objects  = (List<DatabaseObject>) task.getResult();
                    List<Horse> newHorses = new ArrayList<Horse>();
                    for (DatabaseObject object: objects){
                        Horse horse = (Horse) object;
                        newHorses.add(horse);
                    }
                    horses = newHorses;
                    fetchPermissions();
                    break;
                case "getPermissions":
                    List<DatabaseObject> permissionObjects  = (List<DatabaseObject>) task.getResult();
                    permissions = new ArrayList<Permission>();
                    for (DatabaseObject object: permissionObjects){
                        Permission permission = (Permission) object;
                        permissions.add(permission);
                    }
                    fetchResources();
                    break;
                case "getResources":
                    log("got to teh resources");
                    //get the resources here
                    List<List<String>> returnedResources = (List<List<String>>) task.getResult();
                    resources = returnedResources;
                    completeInitialization();
                    break;
                default:
                    break;
            }
        }
    }
    private void sendNewUserToProfile(){
        Intent intent = new Intent(getApplicationContext(), Profile.class);
        user.setType("new user");
        intent.putExtra("user", user);
        intent.putExtra("contact", contact);
        startActivity(intent);
        finish();
    }

    private void fetchContact(){
        Task<DatabaseObject> getContactTask = data.getObject("contact", user.key());
        getContactTask.addOnCompleteListener(listener.forContact());
    }
    private void fetchHorses(){
        Task<List<DatabaseObject>> getHorsesTask = data.getAll("horse");
        getHorsesTask.addOnCompleteListener(listener.forHorses());
    }
    private void fetchPermissions(){
        Task<List<DatabaseObject>> getPermissionsTask = data.getAll("permission");
        getPermissionsTask.addOnCompleteListener(listener.forPermissions());
    }
    private void fetchResources(){
        Task<List<List<String>>> getResourcesTask = data.getResources();
        getResourcesTask.addOnCompleteListener(listener.forResources());
        log("set the listener");
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
        i.putExtra("user", user);
        i.putExtra("contact", contact);
        startActivityForResult(i, 0);
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
        log("building tabs");
        fragments = new MyFragment[4];
        fragments[0] = new HomeTab();
        fragments[1] = new MyHorsesTab();
        fragments[2] = new SearchTab();
        fragments[3] = new EventsTab();
        log("tabs built; adding user/contact");
        for (MyFragment frag: fragments){
            frag.setUser(user);
            frag.setContact(contact);
            frag.setResources(resources);
        }


        log("adding horses to horse tab");
        MyHorsesTab horseTab = (MyHorsesTab) fragments[1];
        horseTab.setHorses(horses);
        horseTab.setPermissions(permissions);
        log("added horses to horse tab");
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
        if (!user.getType().matches("Administrator")){
            administratorButton.setVisibility(GONE);
        }
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

                    user = returnedUser;
                    contact = returnedContact;
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
        intent.putExtra("user", user);
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