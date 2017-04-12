package Activities.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Activities.HorseList;
import Activities.Profile;
import DataControllers.Contact;
import DataControllers.Horse;
import DataControllers.User;

public class SearchTab extends MyFragment {

    //search dropdowns
    //breed
    //color
    //sex
    //stall number
    //name
    //owner name

    Spinner breedSpinner;
    Spinner colorSpinner;
    Spinner sexSpinner;
    Spinner stallSpinner;
    Spinner nameSpinner;
    Button searchButton;

    List<Horse> horses;

    public SearchTab() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab__search, container, false);
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //do shit for this fragment!
        horses = application.getAllHorses();
        initializeViewResources();
        setupSpinnerValues();
        setInitialValues();
        setButtonListener();
    }
    private void initializeViewResources(){
        breedSpinner = (Spinner) getView().findViewById(R.id.search_breed_spinner);
        colorSpinner = (Spinner) getView().findViewById(R.id.search_color_spinner);
        sexSpinner = (Spinner) getView().findViewById(R.id.search_sex_spinner);
        stallSpinner = (Spinner) getView().findViewById(R.id.search_stall_number_spinner);
        nameSpinner = (Spinner) getView().findViewById(R.id.search_name_spinner);
        searchButton = (Button) getView().findViewById(R.id.search_button);
    }
    private void setupSpinnerValues(){
        ArrayAdapter<String>[] adapters = buildAdapters();
        breedSpinner.setAdapter(adapters[0]);
        colorSpinner.setAdapter(adapters[1]);
        nameSpinner.setAdapter(adapters[2]);
        stallSpinner.setAdapter(adapters[3]);
        sexSpinner.setAdapter(adapters[4]);
    }
    private ArrayAdapter<String>[] buildAdapters(){
        ArrayAdapter<String>[] adapters = new ArrayAdapter[5];
        int resourceListIndex = 0;
        List<String> breedOptions = application.getBreedOptions();

        List<String> colorOptions = application.getColorOptions();
        List<String> sexOptions = application.getSexOptions();

        List<String> names = getAllHorseNames();

        List<String> stallNumbers = getStallNumbers();

        List<List<String>> options = new ArrayList<List<String>>();

        options.add(breedOptions);
        options.add(colorOptions);
        options.add(sexOptions);
        options.add(names);
        options.add(stallNumbers);



        for (List<String> eachList: options){
            eachList.add("Any");
            sortList(eachList);
            String[] values = eachList.toArray(new String[eachList.size()]);
            ArrayAdapter<String> stringAdapter = new ArrayAdapter<String>(getContext(), R.layout.custom_spinner_item_2, eachList);
            stringAdapter.setDropDownViewResource(R.layout.custom_spinner_item_2);
            adapters[resourceListIndex] = stringAdapter;
            resourceListIndex++;
        }
        return adapters;
    }
    private List<String> sortList(List<String> list){
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });
        return list;
    }
    private List<String> getAllHorseNames(){
        List<String> names = new ArrayList<String>();
        for(Horse horse: horses){
            names.add(horse.getName());
        }
        return names;
    }
    private List<String> getStallNumbers(){
        List<String> stalls = new ArrayList<String>();
        for (Horse horse: horses){
            stalls.add(horse.getStallNumber());
        }
        return stalls;
    }
    private void setInitialValues(){
        setSpinnerValue(breedSpinner, "Any");
        setSpinnerValue(colorSpinner, "Any");
        setSpinnerValue(sexSpinner, "Any");
        setSpinnerValue(stallSpinner, "Any");
        setSpinnerValue(nameSpinner, "Any");
    }
    private void setSpinnerValue(Spinner spinner, String input){
        int index = getIndex(spinner, input);
        spinner.setSelection(index);
    }
    private int getIndex(Spinner spinner, String value){
        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)){
                index = i;
                break;
            }
        }
        return index;
    }
    private void setButtonListener(){
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get spinner values
                //set search parameters
                //go to next list with results
                List<Horse> results = new ArrayList<Horse>();
                results.addAll(horses);
                String breedInput = getSelection(breedSpinner);
                String colorInput = getSelection(colorSpinner);
                String sexInput = getSelection(sexSpinner);
                String stallInput = getSelection(stallSpinner);
                String nameInput = getSelection(nameSpinner);


                if (!breedInput.matches("Any")){
                    //limit results by breed
                    List<Horse> newResults = new ArrayList<Horse>();
                    newResults.addAll(results);
                    for(Horse horse: results){
                        if (!horse.getBreed().matches(breedInput)){
                            newResults.remove(horse);
                        }
                    }
                    results = newResults;
                }
                if (!colorInput.matches("Any")){
                    List<Horse> newResults = new ArrayList<Horse>();
                    newResults.addAll(results);
                    for (Horse horse: results){
                        if (!horse.getColor().matches(colorInput)){
                            newResults.remove(horse);
                        }
                    }
                    results = newResults;

                }
                if (!sexInput.matches("Any")){
                    List<Horse> newResults = new ArrayList<Horse>();
                    newResults.addAll(results);
                    for (Horse horse: results){
                        if (!horse.getSex().matches(sexInput)){
                            newResults.remove(horse);
                        }
                    }
                    results = newResults;

                }
                if (!stallInput.matches("Any")){
                    List<Horse> newResults = new ArrayList<Horse>();
                    newResults.addAll(results);
                    for (Horse horse: results){
                        if (!horse.getStallNumber().matches(stallInput)){
                            newResults.remove(horse);
                        }
                    }
                    results = newResults;

                }
                if (!nameInput.matches("Any")){
                    List<Horse> newResults = new ArrayList<Horse>();
                    newResults.addAll(results);
                    for (Horse horse:results){
                        if (!horse.getName().matches(nameInput)){
                            newResults.remove(horse);
                        }
                    }
                    results = newResults;
                }
                goToSearchResults(results);
            }
        });
    }
    private String getSelection(Spinner spinner){
        return spinner.getSelectedItem().toString();
    }
    private void goToSearchResults(List<Horse> results){
        if (results.size() == 0){
            showDialog("No Results Found", "");
        }else{
            //go the next view with those necessary search results!
            Intent i = new Intent(getContext(), HorseList.class);
            i.putExtra("user", getUser());
            i.putExtra("horses", (Serializable) results);
            i.putExtra("permissions", (Serializable) getPermissions());
            startActivityForResult(i,  10);
        }
    }
    private void showDialog(String title, String text){
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(text);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
















}
