package Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.List;

import Activities.Fragments.EventsTab;
import Activities.Fragments.HomeTab;
import Activities.Fragments.HorseAdminTab;
import Activities.Fragments.HorseListTab;
import Activities.Fragments.MyFragment;
import Activities.Fragments.MyHorsesTab;
import Activities.Fragments.SearchTab;
import Activities.Fragments.TodayTab;
import Application.MyApplication;
import DataControllers.User;

public class AdministratorView extends AppCompatActivity implements View.OnClickListener{

    Toolbar toolbar;
    TextView toolbarTitle;
    ImageButton adminBasicUserButton;

    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int[] drawables;
    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__administrator);

        Intent i = getIntent();
        user = (User) i.getSerializableExtra("user");

        toolbar = (Toolbar) findViewById(R.id.administrator_toolbar);
        toolbarTitle = (TextView) findViewById(R.id.administrator_toolbar_title);
        adminBasicUserButton = (ImageButton) findViewById(R.id.administrator_basic_user_button);

        tabLayout = (TabLayout) findViewById(R.id.admin_tabs);
        viewPager = (ViewPager) findViewById(R.id.admin_view_pager);

        drawables = new int[3];
        drawables[0] = R.drawable.selector__admin_notifications_tab_icon;
        drawables[1] = R.drawable.selector__admin_notifications_tab_icon;
        drawables[2] = R.drawable.selector__admin_notifications_tab_icon;


        adminBasicUserButton.setOnClickListener(this);

        toolbarTitle.setText("Administrative View");



        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
        addTabIcons();
        addTabMonitor();

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
        MyApplication ap = (MyApplication) getApplication();
        MyFragment[] fragments = new MyFragment[3];
        fragments[0] = new HorseListTab().withHorses(ap.getAllHorses());
        fragments[1] = new MyHorsesTab();
        fragments[2] = new SearchTab();
        return fragments;
    }
    private void addTabIcons(){

        for (int tabNumber = 0; tabNumber < 3; tabNumber++){
            View homeTab = getLayoutInflater().inflate(R.layout.custom_tab_item, null);

            homeTab.findViewById(R.id.icon).setBackgroundResource(drawables[tabNumber]);
            tabLayout.getTabAt(tabNumber).setCustomView(homeTab);
        }
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








    @Override
    public void onClick(View v) {
        //means you're going back!
            //go back to basic user view, and share the user!
            Context context = getApplicationContext();
            Intent returnIntent = new Intent(AdministratorView.this, BasicUserView.class);
            returnIntent.putExtra("user", user);
            setResult(Activity.RESULT_OK, returnIntent);
            startActivity(returnIntent);

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
