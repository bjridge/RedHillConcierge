package Activities.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.ListView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import Activities.EditableHorse;
import DataControllers.Horse;
import DataControllers.Permission;
import DataControllers.User;

public class SearchTab extends MyFragment {

    Spinner breedSpinner;
    Spinner colorSpinner;
    Spinner sexSpinner;
    Spinner stallSpinner;
    Spinner nameSpinner;
    Spinner ownerSpinner;
    Button searchButton;

    List<Horse> horses;
    List<User> owners;
    List<Permission> permissions;
    User user;

    ListView displayedHorses;

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
        owners = application.getAllUsers();
        initializeViewResources();
        setupSpinnerValues();
        setInitialValues();
        setButtonListener();
        //addOnClickListener();
    }
    private void initializeViewResources(){
        breedSpinner = (Spinner) getView().findViewById(R.id.search_breed_spinner);
        colorSpinner = (Spinner) getView().findViewById(R.id.search_color_spinner);
        sexSpinner = (Spinner) getView().findViewById(R.id.search_sex_spinner);
        stallSpinner = (Spinner) getView().findViewById(R.id.search_stall_number_spinner);
        nameSpinner = (Spinner) getView().findViewById(R.id.search_name_spinner);
        ownerSpinner = (Spinner) getView().findViewById(R.id.search_owner_name_spinner);
        searchButton = (Button) getView().findViewById(R.id.search_button);
        displayedHorses = (ListView) getView().findViewById((R.id.horse_list));
    }
    private void setupSpinnerValues(){
        ArrayAdapter<String>[] adapters = buildAdapters();
        breedSpinner.setAdapter(adapters[0]);
        colorSpinner.setAdapter(adapters[1]);
        sexSpinner.setAdapter(adapters[2]);
        stallSpinner.setAdapter(adapters[3]);
        nameSpinner.setAdapter(adapters[4]);
        ownerSpinner.setAdapter(adapters[5]);
    }
    private ArrayAdapter<String>[] buildAdapters(){
        ArrayAdapter<String>[] adapters = new ArrayAdapter[6];
        int resourceListIndex = 0;
        List<String> breedOptions = getBreedOptions();
        List<String> colorOptions = getColorOptions();
        List<String> sexOptions = getSexOptions();
        List<String> names = getAllHorseNames();
        List<String> stallNumbers = getStallNumbers();
        List<String> ownerNames = getOwnerNames();
        List<List<String>> options = new ArrayList<List<String>>();

        options.add(breedOptions);
        options.add(colorOptions);
        options.add(sexOptions);
        options.add(stallNumbers);
        options.add(names);
        options.add(ownerNames);

        for (List<String> eachList: options){
            eachList.add("<Any>");
            sortList(eachList);
            String[] values = eachList.toArray(new String[eachList.size()]);
            ArrayAdapter<String> stringAdapter = new ArrayAdapter<String>(getContext(), R.layout.custom_spinner_item_2, eachList);
            stringAdapter.setDropDownViewResource(R.layout.custom_spinner_item_2);
            adapters[resourceListIndex] = stringAdapter;
            resourceListIndex++;
        }
        return adapters;
    }
    private List<String> getColorOptions(){
        List<String> options = new ArrayList<>();
        for(Horse horse: horses){
            String color = horse.getColor();
            if (!options.contains(color)){
                options.add(color);
            }
        }
        return options;
    }
    private List<String> getSexOptions(){
        List<String> options = new ArrayList<>();
        for (Horse horse: horses){
            String sex = horse.getSex();
            if(!options.contains(sex)){
                options.add(sex);
            }
        }
        return options;
    }

    private List<String> getBreedOptions(){
        List<String> options = new ArrayList<>();
        for (Horse horse: horses){
            String breed = horse.getBreed();
            if (!options.contains(breed)){
                options.add(horse.getBreed());
            }
        }
        return options;
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
    private List<String> getOwnerNames(){
        List<String> ownerNames = new ArrayList<String>();
        for(User owner: owners){
            ownerNames.add(owner.getFirstName() + " " + owner.getLastName());
        }
        return ownerNames;
    }
    private void setInitialValues(){
        setSpinnerValue(breedSpinner, "<Any>");
        setSpinnerValue(colorSpinner, "<Any>");
        setSpinnerValue(sexSpinner, "<Any>");
        setSpinnerValue(stallSpinner, "<Any>");
        setSpinnerValue(nameSpinner, "<Any>");
        setSpinnerValue(ownerSpinner, "<Any>");
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
                String ownerInput = getSelection(ownerSpinner);

                if (!breedInput.matches("<Any>")){
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
                if (!colorInput.matches("<Any>")){
                    List<Horse> newResults = new ArrayList<Horse>();
                    newResults.addAll(results);
                    for (Horse horse: results){
                        if (!horse.getColor().matches(colorInput)){
                            newResults.remove(horse);
                        }
                    }
                    results = newResults;
                }
                if (!sexInput.matches("<Any>")){
                    List<Horse> newResults = new ArrayList<Horse>();
                    newResults.addAll(results);
                    for (Horse horse: results){
                        if (!horse.getSex().matches(sexInput)){
                            newResults.remove(horse);
                        }
                    }
                    results = newResults;
                }
                if (!stallInput.matches("<Any>")){
                    List<Horse> newResults = new ArrayList<Horse>();
                    newResults.addAll(results);
                    for (Horse horse: results){
                        if (!horse.getStallNumber().matches(stallInput)){
                            newResults.remove(horse);
                        }
                    }
                    results = newResults;
                }
                if (!nameInput.matches("<Any>")){
                    List<Horse> newResults = new ArrayList<Horse>();
                    newResults.addAll(results);
                    for (Horse horse:results){
                        if (!horse.getName().matches(nameInput)){
                            newResults.remove(horse);
                        }
                    }
                    results = newResults;
                }

                if (!ownerInput.matches("<Any>")){
                    List<User> ownerResult = new ArrayList<User>();
                    ownerResult.addAll(owners);
                    List<String> userID = new ArrayList<String>();
                    for(User user: ownerResult){
                        if(user.getFirstName().equals(ownerInput)){
                            userID.add(user.key());
                        }
                    }

                    List<Horse> newResults = new ArrayList<Horse>();
                    newResults.addAll(results);
                    for (Horse horse: results){
                        for(int i = 0; i<userID.size(); i++){
                            if (!horse.getOwner().matches(userID.get(i))) {
                                newResults.remove(horse);
                            }
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
            Collections.sort(results, new Comparator<Horse>() {
                @Override
                public int compare(Horse o1, Horse o2) {
                    return o1.compareTo(o2);
                }
            });
            user = (User) getUser();
            permissions = (List<Permission>) getPermissions();
            showHorses(results);
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

    private void showHorses(List<Horse> results){
        //display them in the horse view!
        HorseListAdapter adapter = new HorseListAdapter(getActivity().getApplicationContext(), results);
        displayedHorses.setAdapter(adapter);
        addOnClickListener(results);
    }

    private void addOnClickListener(final List<Horse> results){
        displayedHorses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //get the selected horse
                Horse horse = results.get(position);
                ArrayList<Horse> selectedHorseList = (ArrayList<Horse>) results;
                Log.e("onItemClick: ", Integer.toString(selectedHorseList.size()));
                Context context = getContext();
                //go to next view

                Intent i = new Intent(getContext().getApplicationContext(), EditableHorse.class);
                i.putExtra("horseList", selectedHorseList);
                i.putExtra("horse", horse);
                i.putExtra("user", application.getUser().key());
                //startActivityForResult(i, 0);
            }
        });
    }
}
