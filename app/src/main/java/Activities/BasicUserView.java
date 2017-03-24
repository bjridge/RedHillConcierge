package Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import java.util.ArrayList;
import java.util.List;

import Activities.Fragments.EventsTab;
import Activities.Fragments.HomeTab;
import Activities.Fragments.MyHorsesTab;
import Activities.Fragments.SearchTab;
import Activities.Fragments.TodayTab;
import DataControllers.DatabaseController;

public class BasicUserView extends AppCompatActivity implements View.OnClickListener {


    //toolbar resources
    private Toolbar toolbar;
    private TextView toolbarTitle;

    //tab navigation resources
    private TabLayout tabLayout;
    private ViewPager viewPager;

    FloatingActionButton administratorViewButton;

    DatabaseController controller;

    int[] drawables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__basic_user);

        initializeResources();
        configureTabNavigation();
        addTabMonitor();
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
                        title = "Today";
                        break;
                    case 2:
                        title="Search";
                        break;
                    case 3:
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
        administratorViewButton = (FloatingActionButton) findViewById(R.id.administrator_button);
        toolbarTitle.setText("Home");
        drawables = new int[5];
        administratorViewButton.setOnClickListener(this);
        drawables[0] = R.drawable.selector__home_tab_icon;
        drawables[1] = R.drawable.selector__today_tab_icon;
        drawables[2] = R.drawable.selector__search_tab_icon;
        drawables[3] = R.drawable.selector__events_tab_icon;
        drawables[4] = R.drawable.selector__my_horses_tab_icon;
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
        adapter.addFragment(new HomeTab(), "");
        adapter.addFragment(new TodayTab(), "");
        adapter.addFragment(new SearchTab(), "");
        adapter.addFragment(new EventsTab(), "");
        adapter.addFragment(new MyHorsesTab(), "");
        viewPager.setAdapter(adapter);

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


}
