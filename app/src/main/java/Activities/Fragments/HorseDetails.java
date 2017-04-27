package Activities.Fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Application.MyApplication;
import DataControllers.Change;
import DataControllers.DataFetcher;
import DataControllers.Horse;
import DataControllers.User;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;

public class HorseDetails extends Fragment {

    MyApplication application;

    Horse horse;

    View v;

    ImageView image;
    TextView nameInput;
    Spinner ownerSpinner;
    TextView imageInput;
    ImageView callButton;
    FloatingActionButton saveButton;
    Spinner breedSpinner;
    Spinner colorSpinner;
    Spinner sexSpinner;
    TextView grainAmountInput;
    Spinner grainTypeSpinner;
    TextView waterAmountInput;
    Spinner hayTypeSpinner;
    TextView hayAmountInput;
    TextView medicalInput;
    TextView stallInput;
    TextView otherInput;
    TextView riderInput;
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
        initializeCallButton();
        getActivity().getWindow().setSoftInputMode(SOFT_INPUT_STATE_ALWAYS_HIDDEN);

    }
    private void initializeViewObjects(){
        image = (ImageView) v.findViewById(R.id.horse_detail_picture);
        nameInput = (TextView) v.findViewById(R.id.horse_detail_name);
        imageInput = (TextView) v.findViewById(R.id.horse_detail_picture_url);
        callButton = (ImageView) v.findViewById(R.id.horse_detail_call_owner);
        ownerSpinner = (Spinner) v.findViewById(R.id.horse_detail_owner_spinner);
        saveButton = (FloatingActionButton) v.findViewById(R.id.horse_detail_save_button);
        breedSpinner = (Spinner) v.findViewById(R.id.horse_detail_breed_spinner);
        colorSpinner = (Spinner) v.findViewById(R.id.horse_detail_color_spinner);
        sexSpinner = (Spinner) v.findViewById(R.id.horse_detail_sex_spinner);
        grainAmountInput = (TextView) v.findViewById(R.id.horse_detail_grain_amount);
        grainTypeSpinner = (Spinner) v.findViewById(R.id.horse_detail_grain_type_spinner);
        waterAmountInput = (TextView) v.findViewById(R.id.horse_detail_water_amount);
        hayTypeSpinner = (Spinner) v.findViewById(R.id.horse_detail_hay_spinner);
        hayAmountInput = (TextView) v.findViewById(R.id.horse_detail_hay_amount);
        medicalInput = (TextView) v.findViewById(R.id.horse_detail_medical);
        stallInput = (TextView) v.findViewById(R.id.horse_detail_stall_number);
        otherInput = (TextView) v.findViewById(R.id.horse_detail_notes);
        riderInput = (TextView) v.findViewById(R.id.horse_detail_rider);
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
        List<ArrayAdapter<String>> adapters = new ArrayList<>(6);
        for (List<String> options: allOptions){
            ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(getActivity(), R.layout.custom_spinner_item_2, options);
            newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapters.add(newAdapter);
        }
        ArrayAdapter<String> ownerSpinnerAdapter = new ArrayAdapter<String>(getActivity(), R.layout.custom_spinner_item, allOptions.get(5));
        ownerSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapters.set(5, ownerSpinnerAdapter);
        return adapters;
    }
    private List<List<String>> getOptionsForAllFields(){
        List<List<String>> resources = new ArrayList<List<String>>();
        resources.add(application.getBreedOptions());
        resources.add(application.getColorOptions());
        resources.add(application.getGrainOptions());
        resources.add(application.getHayOptions());
        resources.add(application.getSexOptions());
        resources.add(application.getAllUserNames());
        return resources;
    }
    private void setSpinnerAdapters(List<ArrayAdapter<String>> adapters){
        breedSpinner.setAdapter(adapters.get(0));
        colorSpinner.setAdapter(adapters.get(1));
        sexSpinner.setAdapter(adapters.get(4));
        grainTypeSpinner.setAdapter(adapters.get(2));
        hayTypeSpinner.setAdapter(adapters.get(3));
        ownerSpinner.setAdapter(adapters.get(5));
    }
    private void initializeAllFieldValues(){
        if(!horse.getPicture().equals(null)){
            try {
                Picasso.with(getActivity().getApplicationContext()).load(horse.getPicture()).into(image);
            }catch (Exception e){
            }
        }else{
        }
        setSpinnerValue(breedSpinner, horse.getBreed());
        setSpinnerValue(colorSpinner, horse.getColor());
        setSpinnerValue(grainTypeSpinner, horse.getGrainType());
        setSpinnerValue(hayTypeSpinner, horse.getHay());
        setSpinnerValue(sexSpinner, horse.getSex());
        User owner = application.getUser(horse.getOwner());
        String ownerName = owner.getName();
        setSpinnerValue(ownerSpinner, ownerName);
        nameInput.setText(horse.getName());

            imageInput.setText(horse.getPicture());

        grainAmountInput.setText(horse.getGrainAmount());
        medicalInput.setText(horse.getMedicationInstructions());
        stallInput.setText(horse.getStallNumber());
        otherInput.setText(horse.getNotes());
        riderInput.setText(horse.getPermittedRiders());
        waterAmountInput.setText(horse.getWaterAmount());
        hayAmountInput.setText(horse.getHayAmount());
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
            initializeSaveButtonAction();
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
        ownerSpinner.setEnabled(false);
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
        hayAmountInput.setEnabled(false);
        waterAmountInput.setEnabled(false);
        for (CheckBox cb: day){
            cb.setEnabled(false);
        }
        for (CheckBox cb: night){
            cb.setEnabled(false);
        }

    }
    private void initializeSaveButtonAction(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            saveHorse();
            }
        });
    }

    private void saveHorse(){
        Horse inputValues = getInputValues();
        //if any changes occured
        if (!inputValues.equals(horse)){
            if (restrictedChangeOccurred(inputValues)){
                if (!userIsAdministrator()){
                    showDialog("Warning!", "Some changes might take up to 24 hours to take effect.  Please call an employee in the case of an emergency.", false);
                }
                inputValues.setLastRevisionDate(application.getCurrentDateString());
            }
            if (ownerChangeOccurred(inputValues) && !userIsAdministrator()){
                showDialog("Admin Permission Required", "A request has been sent to an administrator to change the owner of this horse.", false);
                buildChange(inputValues.getOwner());
                inputValues.setOwner(horse.getOwner());
            }

            DataFetcher df = new DataFetcher();
            df.updateObject(inputValues);
            Toast.makeText(getActivity(), "Horse updated",
                    Toast.LENGTH_SHORT).show();
            horse = inputValues;
            initializeAllFieldValues();
            application.updateHorse(horse);
        }
    }
    private Horse getInputValues(){
        Horse h = new Horse();
        //non-changing values
        h.setKey(horse.key());
        h.setLastRevisionDate(horse.getLastRevisionDate());
        //non-critical values
        h.setBreed(breedSpinner.getSelectedItem().toString());
        h.setPicture(imageInput.getText().toString());
        h.setColor(colorSpinner.getSelectedItem().toString());
        h.setName(nameInput.getText().toString());
        h.setNotes(otherInput.getText().toString());
        h.setSex(sexSpinner.getSelectedItem().toString());
        h.setPermittedRiders(riderInput.getText().toString());
        //critical values
        h.setGrainAmount(grainAmountInput.getText().toString());
        h.setGrainType(grainTypeSpinner.getSelectedItem().toString());
        h.setHay(hayTypeSpinner.getSelectedItem().toString());
        h.setMedicationInstructions(medicalInput.getText().toString());
        h.setStallNumber(stallInput.getText().toString());

        String dayInOut = "";
        for (CheckBox cb: day){
            if (cb.isChecked()){
                dayInOut += "1";
            }else{
                dayInOut += "0";
            }
        }
        String nightInOut = "";
        for (CheckBox cb: night){
            if (cb.isChecked()){
                nightInOut += "1";
            }else{
                nightInOut += "0";
            }
        }
        h.setInOutDay(dayInOut);
        h.setInOutNight(nightInOut);
        h.setWaterAmount(waterAmountInput.getText().toString());
        h.setHayAmount(hayAmountInput.getText().toString());

        //administrative changes
        h.setOwner(application.getUserByName(ownerSpinner.getSelectedItem().toString()));
        return h;
    }
    private boolean restrictedChangeOccurred(Horse h){
        h.setGrainAmount(grainAmountInput.getText().toString());
        h.setGrainType(grainTypeSpinner.getSelectedItem().toString());
        h.setHay(hayTypeSpinner.getSelectedItem().toString());
        h.setMedicationInstructions(medicalInput.getText().toString());
        h.setStallNumber(stallInput.getText().toString());
        if (!h.getGrainAmount().matches(horse.getGrainAmount())){
            return true;
        }
        if (!h.getGrainType().matches(horse.getGrainType())){
            return true;
        }
        if (!h.getHay().matches(horse.getHay())){
            return true;
        }
        if (!h.getHayAmount().matches(horse.getHayAmount())){
            return true;
        }
        if (!h.getMedicationInstructions().matches(horse.getMedicationInstructions())){
            return true;
        }
        if (!h.getStallNumber().matches(horse.getStallNumber())){
            return true;
        }
        if (!h.getInOutDay().matches(horse.getInOutDay())){
            return true;
        }
        if (!h.getInOutNight().matches(horse.getInOutNight())){
            return true;
        }
        return false;
    }
    private boolean ownerChangeOccurred(Horse h){
        if (!h.getOwner().matches(horse.getOwner())){
            return true;
        }
        return false;
    }


    private void buildChange(String newOwner){
        Change change = new Change();
        change.setKey(application.getUser().key() + "-horse-owner");
        change.setOldValue(horse.getOwner());
        change.setNewValue(newOwner);
        change.setObjectKey(horse.key());
        DataFetcher dc = new DataFetcher();
        dc.updateObject(change);
    }
    private void showDialog(String title, String text, final boolean shouldExit){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(text);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (shouldExit){
                            //goToMainActivity();
                        }
                    }
                });
        alertDialog.show();
    }
    private void initializeCallButton(){
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);

                intent.setData(Uri.parse("tel:" + application.getUser().getPrimaryPhone()));
                getContext().startActivity(intent);
            }
        });
    }
}
