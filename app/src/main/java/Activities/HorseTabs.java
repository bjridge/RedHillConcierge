package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import java.util.List;

import Activities.Fragments.HorseDetails;
import ListAdapters.HorseListAdapter;
import Application.MyApplication;
import DataControllers.Horse;

public class HorseTabs extends AppCompatActivity {


    ViewPager horseTabs;
    TabLayout tabs;
    HorseTabPagerAdapter tabAdapter;

    List<Horse> horses;
    Horse selectedHorse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__horse_tabs);

        getResourcesFromIntent();
        initializeViewObjects();

    }
    private void initializeViewObjects(){
        horseTabs = (ViewPager) findViewById(R.id.horse_tabs_view_pager);
        tabs = (TabLayout) findViewById(R.id.horse_tabs);

        tabAdapter =
                new HorseTabPagerAdapter(getSupportFragmentManager());
        horseTabs.setAdapter(tabAdapter);
        tabs.setupWithViewPager(horseTabs);

    }
    private void getResourcesFromIntent(){
        MyApplication application = (MyApplication) getApplication();
        Intent i = getIntent();
        horses = (List<Horse>) i.getSerializableExtra("horses");
        if(horses == null){
            horses = application.getAllHorses();
        }
        selectedHorse = (Horse) i.getSerializableExtra("horse");
        if (selectedHorse==null){
            selectedHorse = horses.get(0);
        }
    }



    public class HorseTabPagerAdapter extends FragmentStatePagerAdapter {
        public HorseTabPagerAdapter(FragmentManager fm){
            super(fm);
        }
        @Override
        public Fragment getItem(int i){
            Fragment fragment = new HorseDetails().forHorse(horses.get(i));
            Bundle args = new Bundle();
            args.putSerializable("horse", horses.get(i));
            fragment.setArguments(args);
            return fragment;
        }
        @Override
        public int getCount(){
            return horses.size();
        }
        @Override
        public String getPageTitle(int position){
            return "Stall " + horses.get(position).getStallNumber();
        }

    }
}


