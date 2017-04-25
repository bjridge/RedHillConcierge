package Activities.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import java.util.ArrayList;

import Activities.HorseTabs;
import DataControllers.Horse;
import ListAdapters.HorseListAdapter;

public class HorseDetails extends Fragment {

    Horse horse;

    public HorseDetails(){}
    public HorseDetails forHorse(Horse horse){
        this.horse = horse;
        return this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.tab__horse_details, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        initializeViewObjects();
    }
    private void initializeViewObjects(){

    }

}
