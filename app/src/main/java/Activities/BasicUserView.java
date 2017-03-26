package Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
import Activities.Fragments.MyFragment;
import Activities.Fragments.MyHorsesTab;
import Activities.Fragments.SearchTab;
import DataControllers.Contact;
import DataControllers.DatabaseController;
import DataControllers.DatabaseObject;
import DataControllers.User;

public class BasicUserView extends AppCompatActivity implements View.OnClickListener {


    //toolbar resources
    private Toolbar toolbar;
    private TextView toolbarTitle;

    //tab navigation resources
    private TabLayout tabLayout;
    private ViewPager viewPager;

    ImageButton administratorButton;
    ImageButton settingsButton;
    ImageButton cameraButton;

    DatabaseController controller;

    int[] drawables;

    User user;
    Contact contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__basic_user);

        initializeResources();
        configureTabNavigation();
        addTabMonitor();

        checkPermissions();






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
                        title="Search";
                        break;
                    case 2:
                        title = "Events";
                        break;
                    default:
                        title = "My Horses";
                        break;
                }
                toolbarTitle.setText(title);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    private void initializeResources() {
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbarTitle = (TextView) findViewById(R.id.main_toolbar_title);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        administratorButton = (ImageButton) findViewById(R.id.administrator_button);
        settingsButton = (ImageButton) findViewById(R.id.settings_button);
        cameraButton = (ImageButton) findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
        administratorButton.setOnClickListener(this);
        toolbarTitle.setText("Home");
        drawables = new int[4];
        administratorButton.setOnClickListener(this);
        drawables[0] = R.drawable.selector__home_tab_icon;
        drawables[1] = R.drawable.selector__search_tab_icon;
        drawables[2] = R.drawable.selector__events_tab_icon;
        drawables[3] = R.drawable.selector__my_horses_tab_icon;

        getUser();
    }
    private void getUser(){
        Intent i = getIntent();
        String id = i.getStringExtra("id");
        DatabaseController dc = new DatabaseController();
        Task<DatabaseObject> getUserTask = dc.getObject("user", id);
        getUserTask.addOnCompleteListener(new OnCompleteListener<DatabaseObject>() {
            @Override
            public void onComplete(@NonNull Task<DatabaseObject> task) {
                user = (User) task.getResult();
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
    private void configureTabNavigation(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
        addTabIcons();
    }
    private void setupViewPager(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Fragment[] fragments = buildFragments();
        for (Fragment tab: fragments){
            adapter.addFragment(tab, "");
        }
        viewPager.setAdapter(adapter);

    }

    private Fragment[] buildFragments(){
        MyFragment[] fragments = new MyFragment[4];
        fragments[0] = new HomeTab();
        fragments[1] = new SearchTab();
        fragments[2] = new EventsTab();
        fragments[3] = new MyHorsesTab();
        for (MyFragment frag: fragments){
            frag.setUser(user);
            frag.setContact(contact);
        }
        return fragments;
    }


    private void addTabIcons(){

        for (int tabNumber = 0; tabNumber < 4; tabNumber++){
            View homeTab = getLayoutInflater().inflate(R.layout.custom_tab_item, null);

            homeTab.findViewById(R.id.icon).setBackgroundResource(drawables[tabNumber]);
            tabLayout.getTabAt(tabNumber).setCustomView(homeTab);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.administrator_button){
            switchToAdministratorView();
        }
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

    private void checkPermissions(){
        DatabaseController dc = new DatabaseController();
        //dc.getObject(id);
    }


}
