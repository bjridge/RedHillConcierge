package ViewControllers;

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

import ViewControllers.TabViewControllers.CalendarTab;
import ViewControllers.TabViewControllers.HomeTab;
import ViewControllers.TabViewControllers.HorseListsTab;
import ViewControllers.TabViewControllers.SearchTab;
import ViewControllers.TabViewControllers.UserListTab;
import Model.MyApplication;
import Model.FirebaseDatabaseConnector;
import Model.Objects.FirebaseObject;
import Model.Objects.Horse;
import Model.Objects.User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

//view resources
    private CoordinatorLayout layout;
    private Toolbar toolbar;
    private TextView toolbarTitle;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ProgressBar loadingIcon;
    private ImageButton profileButton;

    int[] drawables;
    Fragment[] fragments;
    ViewPagerAdapter adapter;

//logic resources
    MyApplication application;
    FirebaseDatabaseConnector data;
    boolean isAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view__main_activity);
        initializeResources();
    }
    private void initializeResources(){
        application = (MyApplication) this.getApplication();
        data = new FirebaseDatabaseConnector();
        Intent i = getIntent();
        User partialUser = (User) i.getSerializableExtra("user");
        application.setUser(partialUser);
        loadUser();
    }

//load all data to the application
    private void loadUser(){
        Task<FirebaseObject> getUserTask = data.getObject("user", application.getUser().key());
        MyTask fetchUserListener = new MyTask("getUser");
        getUserTask.addOnCompleteListener(fetchUserListener);
    }
    private void loadData(){
        loadAllHorses();
        loadAllResources();
        loadAllUsers();
    }
    private void loadAllHorses(){
        Task<List<FirebaseObject>> getHorsesTask = data.getAllObjects("horse");
        MyTask fetchHorsesListener = new MyTask("getAllHorses");
        getHorsesTask.addOnCompleteListener(fetchHorsesListener);
    }
    private void loadAllResources(){
        Task<List<List<String>>> getResourcesTask = data.getResources();
        MyTask fetchResourcesListener = new MyTask("getAllResources");
        getResourcesTask.addOnCompleteListener(fetchResourcesListener);
    }
    private void loadAllUsers(){
        Task<List<FirebaseObject>> getUsersTask = data.getAllObjects("user");
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
                case "getUser":
                    tryGetUserFromTask(task.getResult());
                    break;
                case "getAllHorses":
                    getAllHorsesFromTask(task.getResult());
                    break;
                case "getAllResources":
                    getAllResourcesFromTask(task.getResult());
                    break;
                case "getAllUsers":
                    getAllUsersFromTask(task.getResult());
                    break;
                default:
                    break;
            }
            checkForCompletion();
        }
    }
    private void tryGetUserFromTask(Object result){
        User resultingUser = (User) result;
        if (resultingUser == null){
            Toast.makeText(application, "the user we got was null", Toast.LENGTH_SHORT).show();
            goToNewUserFlow();
        }else{
            application.setUser(resultingUser);
            if (resultingUser.getType().matches("Administrator")){
                isAdmin = true;
            }else{
                isAdmin = false;
            }
            loadData();
        }
    }
    private void goToNewUserFlow(){
        Intent i = new Intent(getApplicationContext(), UserDetailedView.class);
        i.putExtra("user", application.getUser());
        i.putExtra("isNewUser", "true");
        startActivityForResult(i, 0);
    }
    private void getAllHorsesFromTask(Object result){
        List<FirebaseObject> allHorseObjects  = (List<FirebaseObject>) result;
        List<Horse> newHorses = new ArrayList<Horse>();
        for (FirebaseObject horseObject: allHorseObjects){
            Horse horse = (Horse) horseObject;
            newHorses.add(horse);
        }
        application.setAllHorses(newHorses);
    }
    private void getAllResourcesFromTask(Object result){
        List<List<String>> returnedResources = (List<List<String>>) result;
        log("about to set all resources");
        application.setResources(returnedResources);
    }
    private void getAllUsersFromTask(Object result){
        log("about to get users from results");
        List<FirebaseObject> userObjects = (List<FirebaseObject>) result;
        log("cast to results");
        List<User> users = new ArrayList<User>();
        for (FirebaseObject userObject: userObjects){
            User user = (User) userObject;
            users.add(user);
        }
       application.setAllUsers(users);
    }
    private void checkForCompletion(){
        log("checking for completion");
        if (application.loadingIsComplete()){
            completeInitialization();
            log("completed loading");
        }
    }

//put data into the view
    private void completeInitialization(){
        initializeViewObjects();
        initializeButtonActions();
        initializeTabNavigation();
        initializeTabMonitor();
        stopLoadingIcon();
    }
    private void initializeViewObjects(){
        layout = (CoordinatorLayout) findViewById(R.id.layout);
        loadingIcon = (ProgressBar) findViewById(R.id.loading_icon);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        profileButton = (ImageButton) findViewById(R.id.profile_button);
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbarTitle = (TextView) findViewById(R.id.main_toolbar_title);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        drawables = new int[5];
        drawables[0] = R.drawable.selector__home_tab_icon;
        drawables[1] = R.drawable.selector__my_horses_tab_icon;
        drawables[2] = R.drawable.selector__search_tab_icon;
        drawables[3] = R.drawable.selector__events_tab_icon;
        drawables[4] = R.drawable.selector__users_tab_icon;
    }
    private void initializeButtonActions(){
        profileButton.setOnClickListener(this);
    }
    private void initializeTabNavigation(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        initializeViewPager();
        tabLayout.setupWithViewPager(viewPager);
        addTabIcons();
    }
    private void initializeViewPager(){
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Fragment[] fragments = initializeFragments();
        for (Fragment tab: fragments){
            adapter.addFragment(tab, "");
        }
        viewPager.setAdapter(adapter);
    }
    private Fragment[] initializeFragments(){
        int tabCount = isAdmin ? 5 : 4;
        fragments = new Fragment[tabCount];
        fragments[0] = new HomeTab();
        fragments[1] = new HorseListsTab();
        fragments[2] = new SearchTab();
        fragments[3] = new CalendarTab();
        if(isAdmin){
            fragments[4] = new UserListTab();
        }
        return fragments;
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
                        title = "Calendar";
                        break;
                    default:
                        title = "(Admin) Users View";
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
    private void addTabIcons(){
        int tabCount = isAdmin ? 5 : 4;
        for (int tabNumber = 0; tabNumber < tabCount; tabNumber++){
            View homeTab = getLayoutInflater().inflate(R.layout.custom__tab_item, null);
            homeTab.findViewById(R.id.icon).setBackgroundResource(drawables[tabNumber]);
            tabLayout.getTabAt(tabNumber).setCustomView(homeTab);
        }
    }
    private void stopLoadingIcon(){
        loadingIcon.setVisibility(View.GONE);
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

                if (resultCode == 0){
                    //new user created
                    loadData();
                }else if (resultCode == 1){
                    //old user returning

                }else if (resultCode == 2){
                }


        } catch (Exception ex) {
            Toast.makeText(MainActivity.this, ex.toString(),
                    Toast.LENGTH_SHORT).show();
        }

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


//setup actions and onClick events
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.profile_button:
                Context context = getApplicationContext();
                Intent i = new Intent(context, UserDetailedView.class);
                i.putExtra("user", application.getUser());
                startActivityForResult(i, 1);
                break;

            default:
                //do nothing
                break;
        }
    }
}