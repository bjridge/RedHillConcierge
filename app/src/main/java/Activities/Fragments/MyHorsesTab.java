package Activities.Fragments;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.ConsoleHandler;

import Activities.EditableHorse;
import Application.MyApplication;
import DataControllers.Contact;
import DataControllers.DataFetcher;
import DataControllers.Horse;
import DataControllers.HorseAdapter;
import DataControllers.Permission;

public class MyHorsesTab extends MyFragment implements ExpandableListView.OnChildClickListener {

    List<Horse> horses;
    List<Horse> myHorses;
    List<Horse> sharedHorses;
    List<Permission> permissions;
    List<List<Horse>> allHorseLists;

    MyApplication application;

    ExpandableListView horseLists;
    MyHorsesExpandableListAdapter adapter;


    public MyHorsesTab() {
        horses=new ArrayList<Horse>();
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
        this.application = super.application;
        horses  = application.getAllHorses();
        myHorses = filterMyHorses();
        sharedHorses = filterSharedHorses();
        allHorseLists = new ArrayList<List<Horse>>();
        allHorseLists.add(myHorses);
        allHorseLists.add(sharedHorses);
        allHorseLists.add(horses);

        for(List<Horse> horseList: allHorseLists){
            sortList(horseList);
        }


        horseLists = (ExpandableListView) getView().findViewById(R.id.my_horses_list);
        adapter = new MyHorsesExpandableListAdapter(getContext(), myHorses, sharedHorses, horses);
        horseLists.setAdapter(adapter);

        horseLists.setOnChildClickListener(this);
    }
    private List<Horse> sortList(List<Horse> list){
        Collections.sort(list, new Comparator<Horse>() {
            @Override
            public int compare(Horse o1, Horse o2) {
                return o1.getStallNumber().compareToIgnoreCase(o2.getStallNumber());
            }
        });
        return list;
    }
    private List<Horse> filterMyHorses(){
        String key = application.getUser().key();
        List<Horse> myHorses = new ArrayList<Horse>();
        for (Horse horse: horses){
            if (horse.getOwner().matches(key)){
                myHorses.add(horse);
            }
        }
        return myHorses;
    }
    private List<Horse> filterSharedHorses(){
        //get all permissions
        String key = application.getUser().key();
        List<Horse> permissions = new ArrayList<Horse>();
        for (Permission permission: application.getAllPermissions()){
            if (permission.getUser().matches(key)){
                Horse horse = findHorse(permission.getHorse());
                permissions.add(horse);
            }
        }
        return permissions;
    }


    private Horse findHorse(String key){
        for (Horse horse: horses){
            if (horse.key().matches(key)){
                return horse;
            }
        }
        return null;
    }
    private void log(String message){
        Log.v("IMPORTANT", message);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        Horse selectedHorse = allHorseLists.get(groupPosition).get(childPosition);
        Context context = getContext();
        Intent i = new Intent(context, EditableHorse.class);
        i.putExtra("horse", selectedHorse);
        startActivityForResult(i, 0);

        return false;
    }
}


