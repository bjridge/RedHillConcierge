package Activities.Fragments;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import Activities.EditableHorse;
import DataControllers.Contact;
import DataControllers.DataFetcher;
import DataControllers.Horse;
import DataControllers.HorseAdapter;
import DataControllers.Permission;
import DataControllers.User;

public class MyHorsesTab extends MyFragment implements ExpandableListView.OnChildClickListener {

    List<Horse> horses;
    List<Horse> myHorses;
    List<Horse> sharedHorses;
    List<Permission> permissions;
    List<List<Horse>> allHorseLists;

    ExpandableListView horseLists;
    MyHorsesExpandableListAdapter adapter;


    public MyHorsesTab() {
        // Required empty public constructor
        myHorses = new ArrayList<Horse>();
        sharedHorses = new ArrayList<Horse>();
    }
    public void setHorses(List<Horse>  horses){
        this.horses = horses;
    }
    public void setPermissions(List<Permission> permissions){
        this.permissions = permissions;

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

        sortMyHorses();
        sortSharedHorses();
        allHorseLists = new ArrayList<List<Horse>>();
        allHorseLists.add(myHorses);
        allHorseLists.add(sharedHorses);
        allHorseLists.add(horses);

        log("about to initialize horse list adapter");
        horseLists = (ExpandableListView) getView().findViewById(R.id.my_horses_list);
        adapter = new MyHorsesExpandableListAdapter(getContext(), myHorses, sharedHorses, horses);
        log("initialized adapter; setting adapter");
        horseLists.setAdapter(adapter);
        log("set adapter");

        horseLists.setOnChildClickListener(this);

        //how to get all horses, and only display certain ones?
        //load them into the application during the first stage

        //what horses are yours?




    }
    private void sortMyHorses(){
        for (Horse horse: horses) {
            if (horse.getOwner().matches(super.getUser().key())) {
                myHorses.add(horse);
            }
        }
    }
    private void sortSharedHorses(){
        for (Permission permission: permissions){
            log("user: " + permission.getUser());
            if (permission.getUser().matches(super.getUser().key())){
                log("found a permission");
                Horse sharedHorse = findHorse(permission.getHorse());
                if (sharedHorse != null){
                    sharedHorses.add(sharedHorse);
                }
            }
        }
    }
    private Horse findHorse(String key){
        for (Horse horse: horses){
            if (horse.key().matches(key)){
                log("found the hourse");
                return horse;
            }
        }
        log("didnt find the horse");
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
        i.putExtra("user", super.getUser());
        startActivityForResult(i, 0);







        return false;
    }
}


