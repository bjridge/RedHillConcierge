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
import Activities.Fragments.TodayTab;
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

    User user;
    Contact contact;
    List<Horse> horses;
    List<Permission> permissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__basic_user);



//  1) initialize objects; disable view
        initializeViewObjects();
        log("view objects initialized");
        initializeButtonActions();
        log("buttons initialized");
        toggleViewStatus(false);
        log("view disabled");

//  2) get the (partial) user and contact from the intent
        Intent i = getIntent();
        user = (User) i.getSerializableExtra("user");
        contact = (Contact) i.getSerializableExtra("contact");
        horses = (List<Horse>) i.getSerializableExtra("horses");
        log("intent objects initialized");

//  3) check to see if the user is complete
        if (!user.isComplete()){
//      a) if user is incomplete: fetch the user and then contact and then horses
            log("user is incomplete; just logged in");
            DataFetcher df = new DataFetcher();
            Task<DatabaseObject> getUserTask = df.getObject("user", user.key());
            getUserTask.addOnCompleteListener(new MyOnCompleteListener("tryToGetUser"));
        }else{
//      b) if user is complete: all is good, initialize as normal
            log("user is complete; already logged in");
            completeInitialization();
        }
    }
    private class MyOnCompleteListener implements OnCompleteListener {

        String purpose;

        public MyOnCompleteListener(String purpose){
            this.purpose = purpose;
        }

        @Override
        public void onComplete(@NonNull Task task){
            switch(purpose){
                case "tryToGetUser":
                    if (task.getResult() == null){
                        log("user is brand new; go to profile");
                        newUser();
                    }else{
                        log("user is not new; get user");
                        user = (User) task.getResult();
                        log("got user; getting contact;");
                        fetchContact();
                    }
                    break;
                case "getContact":
                    contact = (Contact) task.getResult();
                    log("got contact; getting horses");
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
                    horses.addAll(newHorses);
                    horses.addAll(newHorses);
                    log("got horses; completing initialization");
                    fetchPermissions();
                    break;
                case "getPermissions":
                    //by user ID or by horse ID?
                    log("about to get permissions");
                    List<DatabaseObject> permissionObjects  = (List<DatabaseObject>) task.getResult();
                    log("permissions: " + permissionObjects.size());
                    permissions = new ArrayList<Permission>();
                    for (DatabaseObject object: permissionObjects){
                        Permission permission = (Permission) object;
                        permissions.add(permission);
                    }
                    log("got permissions");
                    completeInitialization();
                    break;
                default:
                    break;
            }
        }
    }
    private void newUser(){
        navigateTo(Profile.class);
    }
    private void fetchContact(){
        DataFetcher df = new DataFetcher();
        Task<DatabaseObject> getContactTask = df.getObject("contact", user.key());
        OnCompleteListener<DatabaseObject> getContactTaskListener = new MyOnCompleteListener("getContact");
        getContactTask.addOnCompleteListener(getContactTaskListener);
    }
    private void fetchHorses(){
        DataFetcher df = new DataFetcher();
        Task<List<DatabaseObject>> getHorsesTask = df.getAll("horse");
        getHorsesTask.addOnCompleteListener(new MyOnCompleteListener("getHorses"));
    }
    private void fetchPermissions(){
        DataFetcher df = new DataFetcher();
        log("about to fetch permissions");
        Task<List<DatabaseObject>> getPermissionsTask = df.getAll("permission");
        log("got task");
        getPermissionsTask.addOnCompleteListener(new MyOnCompleteListener("getPermissions"));
    }
    private void completeInitialization(){
        initializeTabNavigation();
        initializeTabMonitor();
        configureAdministrativePrivelages();
        stopLoadingIcon();
        toggleViewStatus(true);
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
        drawables = new int[5];
        drawables[0] = R.drawable.selector__home_tab_icon;
        drawables[1] = R.drawable.selector__my_horses_tab_icon;
        drawables[2] = R.drawable.selector__search_tab_icon;
        drawables[3] = R.drawable.selector__today_tab_icon;
        drawables[4] = R.drawable.selector__events_tab_icon;
    }
    private void toggleViewStatus(boolean enabled){
        for (int i = 0; i < layout.getChildCount(); i++){
            View child = layout.getChildAt(i);
            child.setEnabled(enabled);
        }
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
        fragments = new MyFragment[5];
        fragments[0] = new HomeTab();
        fragments[1] = new MyHorsesTab();
        fragments[2] = new SearchTab();
        fragments[3] = new TodayTab();
        fragments[4] = new EventsTab();
        log("tabs built; adding user/contact");
        for (MyFragment frag: fragments){
            frag.setUser(user);
            frag.setContact(contact);
        }

        log("adding horses to horse tab");
        MyHorsesTab horseTab = (MyHorsesTab) fragments[1];
        horseTab.setHorses(horses);
        horseTab.setPermissions(permissions);
        log("added horses to horse tab");
        return fragments;
    }
    private void addTabIcons(){

        for (int tabNumber = 0; tabNumber < 5; tabNumber++){
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


    //first time loading:
        //users
        //contact
        //horses
        //permissions
        //resources




    private void navigateTo(Class destination){
        Context context = getApplicationContext();
        Intent i = new Intent(context, destination);
        i.putExtra("user", user);
        i.putExtra("contact", contact);
        startActivityForResult(i, 0);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == 0  && resultCode  == RESULT_OK) {

                User returnedUser = (User) data.getSerializableExtra("user");
                Contact returnedContact = (Contact) data.getSerializableExtra("contact");

                user = returnedUser;
                contact = returnedContact;
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