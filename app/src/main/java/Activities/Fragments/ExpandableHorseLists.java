package Activities.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Activities.HorseTabs;
import Application.MyApplication;
import DataControllers.Horse;
import ListAdapters.MyHorsesExpandableListAdapter;

public class ExpandableHorseLists extends Fragment implements ExpandableListView.OnChildClickListener {

    List<Horse> horses;
    List<Horse> myHorses;
    List<List<Horse>> allHorseLists;

    MyApplication application;

    ExpandableListView horseLists;
    MyHorsesExpandableListAdapter adapter;


    public ExpandableHorseLists() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.tab__expandable_horse_lists, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        application = (MyApplication) getActivity().getApplication();
        initializeHorseLists();
        initializeAdapter();

        horseLists.setOnChildClickListener(this);
    }
    private void initializeHorseLists(){
        horses  = application.getAllHorses();
        myHorses = getMyHorses();
        allHorseLists = new ArrayList<List<Horse>>();
        allHorseLists.add(myHorses);
        allHorseLists.add(horses);
        for(List<Horse> horseList: allHorseLists){
            sortList(horseList);
        }
    }
    private void initializeAdapter(){
        horseLists = (ExpandableListView) getView().findViewById(R.id.my_horses_list);
        adapter = new MyHorsesExpandableListAdapter(getContext(), myHorses, horses);
        horseLists.setAdapter(adapter);
        horseLists.expandGroup(0);
        horseLists.expandGroup(1);
    }
    private List<Horse> sortList(List<Horse> list){
        Collections.sort(list, new Comparator<Horse>() {
            @Override
            public int compare(Horse o1, Horse o2) {
                return Integer.valueOf(Integer.parseInt(o1.getStallNumber())).compareTo(Integer.parseInt(o2.getStallNumber()));
            }
        });
        return list;
    }
    private List<Horse> getMyHorses(){
        String key = application.getUser().key();
        List<Horse> myHorses = new ArrayList<Horse>();
        for (Horse horse: horses){
            if (horse.getOwner().matches(key)){
                myHorses.add(horse);
            }
        }
        return myHorses;
    }



    @Override
    public void onResume(){
        super.onResume();
        //get lists again
        initializeHorseLists();
        initializeAdapter();
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Horse selectedHorse = allHorseLists.get(groupPosition).get(childPosition);
        Context context = getContext();

        ArrayList<Horse> selectedHorseList = (ArrayList<Horse>) allHorseLists.get(groupPosition);
        Intent i = new Intent(context, HorseTabs.class);

        i.putExtra("horses", selectedHorseList);
        i.putExtra("horse", selectedHorse);
        startActivity(i);

        return false;
    }
}


