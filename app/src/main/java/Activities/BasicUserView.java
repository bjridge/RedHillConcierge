package Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;

import Activities.Fragments.EventsTab;
import Activities.Fragments.HomeTab;
import Activities.Fragments.LoadingTab;
import Activities.Fragments.MyFragment;
import Activities.Fragments.MyHorsesTab;
import Activities.Fragments.SearchTab;
import Activities.Fragments.TodayTab;
import DataControllers.Contact;
import DataControllers.DataFetcher;
import DataControllers.DatabaseObject;
import DataControllers.User;

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

    ImageButton administratorButton;
    ImageButton profileButton;
    ImageButton cameraButton;

    DataFetcher controller;

    int[] drawables;

    MyFragment[] fragments;

    User user;
    Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__basic_user);

        setupUserObject();

        setupViewObjects();
        setupOnClickListeners();

        configureTabNavigation();
        addTabMonitor();

    }
    private void setupUserObject(){
        Intent i = getIntent();
        user = (User) i.getSerializableExtra("user");
    }
    private void setupLoadingPage(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new LoadingTab(), "");
        viewPager.setAdapter(adapter);
    }
    private void setupViewObjects() {
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
    private void setupOnClickListeners(){
        cameraButton.setOnClickListener(this);
        profileButton.setOnClickListener(this);
        administratorButton.setOnClickListener(this);
    }
    private void fetchUser(){
        Intent i = getIntent();
        String id = i.getStringExtra("id");
        DataFetcher dc = new DataFetcher();
        Task<DatabaseObject> getUserTask = dc.getObject("user", id);
        getUserTask.addOnCompleteListener(new OnCompleteListener<DatabaseObject>() {
            @Override
            public void onComplete(@NonNull Task<DatabaseObject> task) {
                user = (User) task.getResult();
                if (!user.getType().matches("Administrator")){
                    disableAdminPrivelages();
                    //configureTabNavigation();
                }
            }
        });
        Task<DatabaseObject> getContactTask = dc.getObject("contact", id);
        getContactTask.addOnCompleteListener(new OnCompleteListener<DatabaseObject>() {
            @Override
            public void onComplete(@NonNull Task<DatabaseObject> task) {
                contact = (Contact) task.getResult();
            }
        });
    }



    private void addTabMonitor(){
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




    private void disableAdminPrivelages(){
        administratorButton.setVisibility(View.GONE);
    }



    private void configureTabNavigation(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
        addTabIcons();
    }

    private void setupViewPager(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Fragment[] fragments = buildLoadingFragments();
        for (Fragment tab: fragments){
            adapter.addFragment(tab, "");
        }
        viewPager.setAdapter(adapter);

    }

    private Fragment[] buildLoadingFragments(){
        fragments = new MyFragment[5];
        fragments[0] = new HomeTab();
        fragments[1] = new MyHorsesTab();
        fragments[2] = new SearchTab();
        fragments[3] = new TodayTab();
        fragments[4] = new EventsTab();
        return fragments;
    }

    private Fragment[] buildFragments(){
        fragments = new MyFragment[5];
        fragments[0] = new HomeTab();
        fragments[1] = new MyHorsesTab();
        fragments[2] = new SearchTab();
        fragments[3] = new TodayTab();
        fragments[4] = new EventsTab();

        for (MyFragment frag: fragments){
            frag.setUser(user);
            frag.setContact(contact);
        }
        return fragments;
    }

    private void addTabIcons(){

        for (int tabNumber = 0; tabNumber < 5; tabNumber++){
            View homeTab = getLayoutInflater().inflate(R.layout.custom_tab_item, null);

            homeTab.findViewById(R.id.icon).setBackgroundResource(drawables[tabNumber]);
            tabLayout.getTabAt(tabNumber).setCustomView(homeTab);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.administrator_button:
                navigateTo(AdministratorView.class);
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

    private void navigateTo(Class destination){
        Context context = getApplicationContext();
        Intent i = new Intent(context, destination);
        i.putExtra("user", user);
        i.putExtra("isNewUser", false);
        startActivityForResult(i, 0);
    }




    private void switchToAdministratorView(){
        Context context = getApplicationContext();
        Intent intent = new Intent(context, AdministratorView.class);
        startActivity(intent);

    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
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





}
