package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import java.io.Serializable;
import java.util.List;

import Activities.Fragments.HorseListAdapter;
import Application.MyApplication;
import DataControllers.Horse;
import DataControllers.Permission;
import DataControllers.User;

public class HorseList extends AppCompatActivity {
    List<Horse> horses;
    ListView displayedHorses;
    Horse selectedHorse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__horse_list);

        initializeViewObjects();
        //what we'll need from the intent:
            //list of horses to display
            //user who is logged in
            //list of permissions
        getResourcesFromIntent();
        showHorses();
    }
    private void initializeViewObjects(){
        displayedHorses = (ListView) findViewById(R.id.horse_list);
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
    private void showHorses(){
        //display them in the horse view!
        HorseListAdapter adapter = new HorseListAdapter(getApplicationContext(), horses);
        displayedHorses.setAdapter(adapter);
    }

    public class HorseTabActivity extends FragmentActivity {

    }

    public class HorseTabPagerAdapter extends FragmentStatePagerAdapter {
        public HorseTabPagerAdapter(FragmentManager fm){
            super(fm);
        }
        @Override
        public Fragment getItem(int i){
            Fragment fragment = new HorseTabFragment();
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
        public CharSequence getPageTitle(int position){
            return "Stall " + horses.get(position).getStallNumber();
        }
    }

    public static class HorseTabFragment extends Fragment {
        public static final String ARG_Object = "object";
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
            View rootView = inflater.inflate(R.layout.tab__horse_list, container, false);
            Bundle args =  getArguments();
            //populate information here!
            return rootView;
        }
    }

}


