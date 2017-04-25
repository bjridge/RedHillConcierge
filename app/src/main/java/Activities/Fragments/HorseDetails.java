package Activities.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import Activities.HorseTabs;
import Application.MyApplication;
import DataControllers.Horse;
import DataControllers.User;
import ListAdapters.HorseListAdapter;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;

public class HorseDetails extends Fragment {

    MyApplication application;

    Horse horse;

    View v;

    ImageView image;
    TextView nameInput;
    TextView imageInput;
    FloatingActionButton saveButton;
    Spinner breedSpinner;
    Spinner colorSpinner;
    Spinner sexSpinner;
    TextView grainAmountInput;
    Spinner grainTypeSpinner;
    Spinner hayTypeSpinner;
    TextView medicalInput;
    TextView stallInput;
    TextView otherInput;
    TextView riderInput;
    TextView ownerButton;
    List<CheckBox> day;
    List<CheckBox> night;

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
        v = inflater.inflate(R.layout.tab__horse_details, container, false);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        application = (MyApplication) getActivity().getApplication();
        initializeViewObjects();
        initializeSpinnerOptions();
        initializeAllFieldValues();
        disableFieldsIfUserDoesNotHavePriveleges();
        Log.v("ONE", "LOADED TEH FRAGMENTS");
        getActivity().getWindow().setSoftInputMode(SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }
    private void initializeViewObjects(){
        image = (ImageView) v.findViewById(R.id.horse_detail_picture);
        nameInput = (TextView) v.findViewById(R.id.horse_detail_name);
        imageInput = (TextView) v.findViewById(R.id.horse_detail_picture_url);
        saveButton = (FloatingActionButton) v.findViewById(R.id.horse_detail_save_button);
        breedSpinner = (Spinner) v.findViewById(R.id.horse_detail_breed_spinner);
        colorSpinner = (Spinner) v.findViewById(R.id.horse_detail_color_spinner);
        sexSpinner = (Spinner) v.findViewById(R.id.horse_detail_sex_spinner);
        grainAmountInput = (TextView) v.findViewById(R.id.horse_detail_grain_amount);
        grainTypeSpinner = (Spinner) v.findViewById(R.id.horse_detail_grain_type_spinner);
        hayTypeSpinner = (Spinner) v.findViewById(R.id.horse_detail_hay_spinner);
        medicalInput = (TextView) v.findViewById(R.id.horse_detail_medical);
        stallInput = (TextView) v.findViewById(R.id.horse_detail_stall_number);
        otherInput = (TextView) v.findViewById(R.id.horse_detail_notes);
        riderInput = (TextView) v.findViewById(R.id.horse_detail_rider);
        ownerButton = (TextView) v.findViewById(R.id.horse_detail_owner_button);
        day = new ArrayList<CheckBox>(7);
        night = new ArrayList<CheckBox>(7);
        day.add((CheckBox) v.findViewById(R.id.Day_Sun));
        night.add((CheckBox) v.findViewById(R.id.Night_Sun));
        day.add((CheckBox) v.findViewById(R.id.Day_Mon));
        night.add((CheckBox) v.findViewById(R.id.Night_Mon));
        day.add((CheckBox) v.findViewById(R.id.Day_Tues));
        night.add((CheckBox) v.findViewById(R.id.Night_Tues));
        day.add((CheckBox) v.findViewById(R.id.Day_Wed));
        night.add((CheckBox) v.findViewById(R.id.Night_Wed));
        day.add((CheckBox) v.findViewById(R.id.Day_Thu));
        night.add((CheckBox) v.findViewById(R.id.Night_Thu));
        day.add((CheckBox) v.findViewById(R.id.Day_Fri));
        night.add((CheckBox) v.findViewById(R.id.Night_Fri));
        day.add((CheckBox) v.findViewById(R.id.Day_Sat));
        night.add((CheckBox) v.findViewById(R.id.Night_Sat));
    }
    private void initializeSpinnerOptions(){
        List<ArrayAdapter<String>> adapters = getAdaptersForAllSpinners();
        setSpinnerAdapters(adapters);
    }
    private List<ArrayAdapter<String>> getAdaptersForAllSpinners(){
        List<List<String>> allOptions = getOptionsForAllFields();
        List<ArrayAdapter<String>> adapters = new ArrayList<>(5);
        for (List<String> options: allOptions){
            ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, options);
            newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapters.add(newAdapter);
        }
        return adapters;
    }
    private List<List<String>> getOptionsForAllFields(){
        List<List<String>> resources = new ArrayList<List<String>>();
        resources.add(application.getBreedOptions());
        resources.add(application.getColorOptions());
        resources.add(application.getGrainOptions());
        resources.add(application.getHayOptions());
        resources.add(application.getSexOptions());
        return resources;
    }
    private void setSpinnerAdapters(List<ArrayAdapter<String>> adapters){
        breedSpinner.setAdapter(adapters.get(0));
        colorSpinner.setAdapter(adapters.get(1));
        sexSpinner.setAdapter(adapters.get(2));
        grainTypeSpinner.setAdapter(adapters.get(3));
        hayTypeSpinner.setAdapter(adapters.get(4));
    }
    private void initializeAllFieldValues(){
        if(!horse.getPicture().equals(null)){
            try {
                Picasso.with(getActivity().getApplicationContext()).load(horse.getPicture()).into(image);
            }catch (Exception e){}
            }
        setSpinnerValue(breedSpinner, horse.getBreed());
        setSpinnerValue(colorSpinner, horse.getColor());
        setSpinnerValue(grainTypeSpinner, horse.getGrainType());
        setSpinnerValue(hayTypeSpinner, horse.getHay());
        setSpinnerValue(sexSpinner, horse.getSex());
        nameInput.setText(horse.getName());
        imageInput.setText(horse.getPicture());
        grainAmountInput.setText(horse.getGrainAmount());
        medicalInput.setText(horse.getMedicationInstructions());
        stallInput.setText(horse.getStallNumber());
        otherInput.setText(horse.getNotes());
        riderInput.setText(horse.getPermittedRiders());
        User owner = application.getUser(horse.getOwner());
        ownerButton.setText(owner.getFirstName() + " " + owner.getLastName());
        //check boxes
        int index = 0;
        for (char status: horse.getInOutDay().toCharArray()){
            if (status == '1'){
                day.get(index).setChecked(true);
            }
            index++;
        }
        index = 0;
        for (char status: horse.getInOutNight().toCharArray()){
            if (status == '1'){
                night.get(index).setChecked(true);
            }
            index++;
        }
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

    private void disableFieldsIfUserDoesNotHavePriveleges(){
        if (userIsOwnerOfHorse() || userIsAdministrator() || userIsEmployee()){
            //leave everything enabled
        }else{
            disableEditableFields();
        }
    }
    private boolean userIsOwnerOfHorse(){
        String userKey = application.getUser().key();
        String ownerKey = horse.getOwner();
        if (userKey.matches(ownerKey)){
            return true;
        }
        return false;
    }
    private boolean userIsAdministrator(){
        if (application.getUser().getType().matches("Administrator")){
            return true;
        }
        return false;
    }
    private boolean userIsEmployee(){
        if (application.getUser().getType().matches("Employee")){
            return true;
        }
        return false;
    }
    private void disableEditableFields(){
        nameInput.setEnabled(false);
        imageInput.setEnabled(false);
        saveButton.setVisibility(v.GONE);
        breedSpinner.setEnabled(false);
        colorSpinner.setEnabled(false);
        sexSpinner.setEnabled(false);
        grainAmountInput.setEnabled(false);
        grainTypeSpinner.setEnabled(false);
        hayTypeSpinner.setEnabled(false);
        medicalInput.setEnabled(false);
        stallInput.setEnabled(false);
        otherInput.setEnabled(false);
        riderInput.setEnabled(false);
        for (CheckBox cb: day){
            cb.setEnabled(false);
        }
        for (CheckBox cb: night){
            cb.setEnabled(false);
        }

    }
}
