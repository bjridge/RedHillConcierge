package ViewControllers;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import java.util.ArrayList;
import java.util.List;

import DataControllers.DatabaseController;

public class MainActivity extends AppCompatActivity {


    //toolbar resources
    private Toolbar toolbar;
    private TextView toolbarTitle;

    //tab navigation resources
    private TabLayout tabLayout;
    private ViewPager viewPager;

    DatabaseController controller;

    int[] drawables;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        toolbarTitle.setText("Home");
        drawables = new int[5];
        drawables[0] = R.drawable.home_tab_icon_selector;
        drawables[1] = R.drawable.assignment_icon_selector;
        drawables[2] = R.drawable.search_tab_icon_selector;
        drawables[3] = R.drawable.calendar_icon_selector;
        drawables[4] = R.drawable.horse_icon_selector;
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
        adapter.addFragment(new HomeTabFragment(), "");
        adapter.addFragment(new TodayFragment(), "");
        adapter.addFragment(new SearchTabFragment(), "");
        adapter.addFragment(new EventsFragment(), "");
        adapter.addFragment(new MyHorsesFragment(), "");
        viewPager.setAdapter(adapter);

    }


    private void addTabIcons(){

        for (int tabNumber = 0; tabNumber < 5; tabNumber++){
            View homeTab = getLayoutInflater().inflate(R.layout.custom_tab, null);

            homeTab.findViewById(R.id.icon).setBackgroundResource(drawables[tabNumber]);
            tabLayout.getTabAt(tabNumber).setCustomView(homeTab);
        }
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
