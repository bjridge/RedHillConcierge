package Activities.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import DataControllers.Contact;
import DataControllers.Horse;
import DataControllers.HorseAdapter;
import DataControllers.User;

public class MyHorsesTab extends MyFragment {

    ListView myHorsesList;
    ListView sharedHorsesList;





    public MyHorsesTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab__my_horses, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        myHorsesList = (ListView) getView().findViewById(R.id.my_horses_list);
        sharedHorsesList = (ListView) getView().findViewById(R.id.shared_horses_list);

        Horse testHorse = new Horse();
        testHorse.setKey("1");
        testHorse.setName("Test Horse");
        testHorse.setBreed("test breed");
        testHorse.setColor("test color");
        testHorse.setSex("test breed");
        testHorse.setStallNumber(4);

        Horse[] testHorses = new Horse[]{testHorse, testHorse, testHorse};

        HorseAdapter horseArrayAdapter = new HorseAdapter(getContext(),R.layout.custom_horse_list_item, testHorses );

        myHorsesList.setAdapter(horseArrayAdapter);

        //how to get all horses, and only display certain ones?
        //load them into the application during the first stage


    }
















}
