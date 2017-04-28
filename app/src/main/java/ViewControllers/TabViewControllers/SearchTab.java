package ViewControllers.TabViewControllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
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

import ViewControllers.HorseDetailedViewTabs;
import Model.MyApplication;
import Model.Objects.Horse;
import Model.Objects.User;
import ListAdapters.HorseListAdapter;

public class SearchTab extends Fragment implements AdapterView.OnItemSelectedListener {

    MyApplication application;

    Spinner breedSpinner;
    Spinner colorSpinner;
    Spinner sexSpinner;
    Spinner stallSpinner;
    Spinner nameSpinner;
    Spinner ownerSpinner;
    Button searchButton;

    List<Horse> horses;
    List<User> owners;

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
        application = (MyApplication) getActivity().getApplication();
        initializeViewResources();
        initializeDataResources();
        initializeSpinnerValues();
        setInitialSpinnerValuesToAny();
        initializeSearchListener();
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
    private void initializeDataResources(){
        horses = application.getAllHorses();
        owners = application.getAllUsers();
    }
    private void initializeSpinnerValues(){
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
        List<List<String>> allOptions = buildAllOptions();
        setAdapterOptions(adapters, allOptions);
        return adapters;
    }
    private List<List<String>> buildAllOptions(){
        List<String> breedOptions = application.getLimitedBreedOptions();
        List<String> colorOptions = application.getLimitedColorOptions();
        List<String> sexOptions = application.getLimitedSexOptions();
        List<String> names = application.getLimitedHorseNameOptions();
        List<String> stallNumbers = application.getLimitedStalls();
        List<String> ownerNames = application.getLimitedOwners();
        List<List<String>> options = new ArrayList<List<String>>();
        options.add(breedOptions);
        options.add(colorOptions);
        options.add(sexOptions);
        options.add(stallNumbers);
        options.add(names);
        options.add(ownerNames);
        sortAdapterOptions(options);
        sortStalls(options.get(3));
        addAnyValueToEachAdapter(options);
        return options;
    }
    private void addAnyValueToEachAdapter(List<List<String>> options){
        for (List<String> eachList: options){
            eachList.add("Any");
        }
    }
    private void sortAdapterOptions(List<List<String>> options){
        for (List<String> strings: options){
            sortStrings(strings);
        }
    }
    private List<String> sortStrings(List<String> list){
        Collections.sort(list, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareToIgnoreCase(o2);
            }
        });
        return list;
    }
    private void sortStalls(List<String> stalls){
        Collections.sort(stalls, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int itemA = Integer.parseInt(o1);
                int itemB = Integer.parseInt(o2);
                if (Integer.valueOf(o1) < Integer.valueOf(o2)){
                    return -1;
                }
                return 1;
            }
        });
    }

    private void setAdapterOptions(ArrayAdapter<String>[] adapters, List<List<String>> options){
        int adapterIndex = 0;
        for(List<String> eachList: options){
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), R.layout.custom__spinner_item__white, eachList);
            adapter.setDropDownViewResource(R.layout.custom__spinner_item__black);
            adapters[adapterIndex] = adapter;
            adapterIndex++;
        }
    }



    private void setInitialSpinnerValuesToAny(){
        setSpinnerValue(breedSpinner, "Any");
        setSpinnerValue(colorSpinner, "Any");
        setSpinnerValue(sexSpinner, "Any");
        setSpinnerValue(stallSpinner, "Any");
        setSpinnerValue(nameSpinner, "Any");
        setSpinnerValue(ownerSpinner, "Any");
    }
    private void setSpinnerValue(Spinner spinner, String input){
        int index = getSpinnerOptionIndex(spinner, input);
        spinner.setSelection(index);
    }
    private int getSpinnerOptionIndex(Spinner spinner, String value){
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)){
                index = i;
                break;
            }
        }
        return index;
    }
    private void initializeSearchListener(){
        nameSpinner.setOnItemSelectedListener(this);
        sexSpinner.setOnItemSelectedListener(this);
        stallSpinner.setOnItemSelectedListener(this);
        breedSpinner.setOnItemSelectedListener(this);
        colorSpinner.setOnItemSelectedListener(this);
        ownerSpinner.setOnItemSelectedListener(this);
    }
    private void getResults(){
        List<Horse> results = new ArrayList<Horse>();
        results.addAll(horses);
        String breedInput = getSelection(breedSpinner);
        String colorInput = getSelection(colorSpinner);
        String sexInput = getSelection(sexSpinner);
        String stallInput = getSelection(stallSpinner);
        String nameInput = getSelection(nameSpinner);
        String ownerInput = getSelection(ownerSpinner);

        if (!breedInput.matches("Any")){
            results = filterHorseList(results, "breed", breedInput);
        }
        if (!colorInput.matches("Any")){
            results = filterHorseList(results, "color", colorInput);
        }
        if (!sexInput.matches("Any")){
            results = filterHorseList(results, "sex", sexInput);
        }
        if (!stallInput.matches("Any")){
            results = filterHorseList(results, "stall", stallInput);
        }
        if (!nameInput.matches("Any")){
            results = filterHorseList(results, "name", nameInput);
        }

        if (!ownerInput.matches("Any")){
            results = filterHorseList(results, "owner", ownerInput);
        }
        goToSearchResults(results);
    }
    private List<Horse> filterHorseList(List<Horse> results, String field, String expectedValue){
        List<Horse> newResults = new ArrayList<Horse>();
        for(Horse horse: results){
            String currentHorseValue;
            switch (field){
                case "breed":
                    currentHorseValue = horse.getBreed();
                    break;
                case "color":
                    currentHorseValue = horse.getColor();
                    break;
                case "sex":
                    currentHorseValue = horse.getSex();
                    break;
                case "stall":
                    currentHorseValue = horse.getStallNumber();
                    break;
                case "name":
                    currentHorseValue = horse.getName();
                    break;
                default:
                    User user = application.getUser(horse.getOwner());
                    currentHorseValue = user.getName();
                    break;
            }
            if (currentHorseValue.matches(expectedValue)){
                newResults.add(horse);
            }
        }
        return newResults;
    }
    private String getSelection(Spinner spinner){
        return spinner.getSelectedItem().toString();
    }
    private void goToSearchResults(List<Horse> results){
        if (results.size() == 0){
            results = new ArrayList<Horse>();
            displayResults(results);
        }else{
            Collections.sort(results, new Comparator<Horse>() {
                @Override
                public int compare(Horse o1, Horse o2) {
                    return o1.compareTo(o2);
                }
            });

            displayResults(results);
        }
    }
    private void displayResults(List<Horse> results){
        HorseListAdapter adapter = new HorseListAdapter(getActivity().getApplicationContext(), results);
        displayedHorses.setAdapter(adapter);
        displayedHorses.setEmptyView(getView().findViewById(R.id.empty));
        addOnClickListener(results);
    }

    private void addOnClickListener(final List<Horse> results){
        displayedHorses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Horse horse = results.get(position);
                ArrayList<Horse> selectedHorseList = (ArrayList<Horse>) results;
                Intent i = new Intent(getContext().getApplicationContext(), HorseDetailedViewTabs.class);
                i.putExtra("horses", selectedHorseList);
                i.putExtra("horse", horse);
                startActivity(i);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        getResults();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onResume(){
        super.onResume();
        initializeDataResources();
        initializeSpinnerValues();
        setInitialSpinnerValuesToAny();
        initializeSearchListener();
    }
}
