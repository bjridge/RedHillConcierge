package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import java.util.List;

import Activities.Fragments.HorseDetails;
import Application.MyApplication;
import DataControllers.Horse;

public class HorseTabs extends AppCompatActivity {


    ViewPager horseTabs;
    TabLayout tabs;
    HorseTabPagerAdapter tabAdapter;
    ImageButton backButton;

    List<Horse> horses;
    Horse selectedHorse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__horse_tabs);

        getResourcesFromIntent();
        initializeViewObjects();
        initializeTabs();
        selectHorse();

    }
    private void initializeViewObjects(){
        horseTabs = (ViewPager) findViewById(R.id.horse_tabs_view_pager);
        tabs = (TabLayout) findViewById(R.id.horse_tabs);
        tabs.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabAdapter =
                new HorseTabPagerAdapter(getSupportFragmentManager());
        horseTabs.setAdapter(tabAdapter);
        tabs.setupWithViewPager(horseTabs);
        backButton = (ImageButton) findViewById(R.id.horse_tabs_back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
    private void initializeTabs(){
        final int white = getResources().getColor(R.color.white);
        final int accent = getResources().getColor(R.color.accent);
        for (int tabIndex = 0; tabIndex < horses.size(); tabIndex++){
            View homeTab = getLayoutInflater().inflate(R.layout.custom__horse_tab_item, null);
            TextView nameInput = (TextView) homeTab.findViewById(R.id.horse_tab_name);
            TextView stallInput = (TextView) homeTab.findViewById(R.id.horse_tab_stall);
            nameInput.setText(horses.get(tabIndex).getName());
            stallInput.setText("Stall " + horses.get(tabIndex).getStallNumber());
            nameInput.setTextColor(white);
            stallInput.setTextColor(white);
            tabs.getTabAt(tabIndex).setCustomView(homeTab);
        }
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                TextView name = (TextView) view.findViewById(R.id.horse_tab_name);
                TextView stall = (TextView) view.findViewById(R.id.horse_tab_stall);
                name.setTextColor(accent);
                stall.setTextColor(accent);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                TextView name = (TextView) view.findViewById(R.id.horse_tab_name);
                TextView stall = (TextView) view.findViewById(R.id.horse_tab_stall);
                name.setTextColor(white);
                stall.setTextColor(white);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void selectHorse(){
        int selectedHorseIndex = findIndexOfSelectedHorse();

        TabLayout.Tab tab = tabs.getTabAt(selectedHorseIndex);
        tab.select();
    }
    private int findIndexOfSelectedHorse(){
        int index = 0;
        for (Horse horse: horses){
            if (selectedHorse.key().matches(horse.key())){
                return index;
            }
            index++;
        }
        return index;
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


