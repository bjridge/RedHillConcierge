package Activities.Fragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.zip.DataFormatException;

import Activities.HorseTabs;
import Application.MyApplication;
import DataControllers.DataFetcher;
import DataControllers.Horse;
import DataControllers.User;
import ListAdapters.MyHorsesExpandableListAdapter;

public class ExpandableHorseLists extends Fragment implements ExpandableListView.OnChildClickListener, View.OnClickListener {

    List<Horse> horses;
    List<Horse> myHorses;
    List<List<Horse>> allHorseLists;

    MyApplication application;

    ExpandableListView horseLists;
    MyHorsesExpandableListAdapter adapter;


    FloatingActionButton addHorseButton;


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

        addHorseButton = (FloatingActionButton) getView().findViewById(R.id.add_horse_button);
        if (application.getUser().getType().matches("Administrator")){
            addHorseButton.setOnClickListener(this);
        }else{
            addHorseButton.setEnabled(false);
            addHorseButton.setVisibility(View.GONE);
        }
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

    @Override
    public void onClick(View v) {
       final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom__dialog_add_horse);
        dialog.setCancelable(true);

//        // set the custom dialog components - text, image and button
        final Spinner userSpinner = (Spinner) dialog.findViewById(R.id.dialog_spinner);
        final EditText nameInput = (EditText) dialog.findViewById(R.id.dialog_horse_name);
        final TextView cancelButton = (TextView) dialog.findViewById(R.id.dialog_cancel);
        final TextView addButton = (TextView) dialog.findViewById(R.id.dialog_add);
        List<String> allUserNames = application.getAllUserNames();
        ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(getActivity(), R.layout.custom_spinner_item, allUserNames);
        newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userSpinner.setAdapter(newAdapter);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nameInput.getText().toString().matches("")){
                    nameInput.requestFocus();
                    buildToast("Please enter a name");
                }else{
                    addNewHorse(nameInput.getText().toString(), userSpinner.getSelectedItem().toString());
                    dialog.dismiss();
                    dialog.hide();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
                dialog.hide();

            }
        });
        dialog.show();
    }
    private void addNewHorse(String name, String ownerName){
        String ownerID = application.getUserByName(ownerName);
        Horse newHorse = new Horse();
        newHorse.setOwner(ownerID);
        newHorse.setName(name);
        DataFetcher df = new DataFetcher();
        df.addHorse(newHorse);
        application.addHorse(newHorse);
        buildToast("Horse (" + name + ") has been added to Red Hill Concierge");
    }
    private void buildToast(String message){
        Toast newToast = Toast.makeText(getActivity(), message,
                Toast.LENGTH_LONG);
        TextView toast = (TextView) newToast.getView().findViewById(android.R.id.message);
        toast.setTextColor(getResources().getColor(R.color.accent));
        toast.setTextSize(25);
        newToast.show();
    }
}


