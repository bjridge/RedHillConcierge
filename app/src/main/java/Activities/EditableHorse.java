package Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import Application.MyApplication;
import DataControllers.DataFetcher;
import DataControllers.Horse;
import DataControllers.User;

public class EditableHorse extends AppCompatActivity implements View.OnClickListener {

    public int keyIndex;
    TextView nameInput;
    Spinner breedSpinner;
    Spinner colorSpinner;
    TextView grainAmountInput;
    Spinner grainTypeSpinner;
    Spinner haySpinner;
    Spinner sexSpinner;
    TextView stallInput;
    ImageButton exitButton;
    TextView ownerButton;
    ImageButton backStall;
    ImageButton nextStall;
    Button saveChanges;
    TextView stallNumber;
    TextView medication;
    TextView notes;
    TextView pictureURL;
    ImageView horsePicture;
    EditText permittedRiders;
    EditText hayAmount;
    EditText waterAmount;

    //checkboxes
    GridLayout gridInOut;
    List<CheckBox> night = new ArrayList<CheckBox>(7);
    List<CheckBox> day= new ArrayList<CheckBox>(7);

    MyApplication application;

    List<User> user;
    Horse horse;
    String userID;
    List<Horse> horseList;
    List<Horse> horses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__editable_horse);
        application = (MyApplication) getApplication();
        horses = application.getAllHorses();
        user = application.getAllUsers();
        initializeViewObjects();
        getIntentObjects();
        setButtonOnClickListeners();
        setupSpinnerValues();
        setInitialHorseValues();
        setStallButtons();
        checkPermissions();
    }
    private void initializeViewObjects(){
        gridInOut = (GridLayout) findViewById(R.id.grid_in_out);
        nameInput = (TextView) findViewById(R.id.horse_name_input);
        breedSpinner = (Spinner) findViewById(R.id.horse_breed_spinner);
        colorSpinner = (Spinner) findViewById(R.id.horse_color_spinner);
        grainAmountInput = (TextView) findViewById(R.id.horse_grain_amount_input);
        grainTypeSpinner = (Spinner) findViewById(R.id.horse_grain_type_spinner);
        haySpinner = (Spinner) findViewById(R.id.horse_hay_spinner);
        sexSpinner = (Spinner) findViewById(R.id.horse_sex_spinner);
        stallInput = (TextView) findViewById(R.id.horse_stall_input);
        exitButton = (ImageButton) findViewById(R.id.horse_exit_button);
        ownerButton = (TextView) findViewById(R.id.horse_owner_button);
        nextStall = (ImageButton) findViewById(R.id.next_button);
        backStall = (ImageButton) findViewById(R.id.back_button_clicked);
        saveChanges = (Button) findViewById(R.id.save_horse_button);
        stallNumber = (TextView) findViewById(R.id.stall_Number);
        medication = (TextView) findViewById(R.id.horse_medication);
        notes = (TextView) findViewById(R.id.horse_notes);
        pictureURL = (TextView) findViewById(R.id.horse_photo_input);
        horsePicture = (ImageView) findViewById(R.id.horse_image);
        permittedRiders = (EditText) findViewById(R.id.permitted_riders);
        waterAmount = (EditText) findViewById(R.id.water_amount);
        hayAmount = (EditText) findViewById(R.id.hay_amount);

        //check boxes
        day.add((CheckBox) findViewById(R.id.Day_Sun));
        night.add((CheckBox) findViewById(R.id.Night_Sun));
        day.add((CheckBox) findViewById(R.id.Day_Mon));
        night.add((CheckBox) findViewById(R.id.Night_Mon));
        day.add((CheckBox) findViewById(R.id.Day_Tues));
        night.add((CheckBox) findViewById(R.id.Night_Tues));
        day.add((CheckBox) findViewById(R.id.Day_Wed));
        night.add((CheckBox) findViewById(R.id.Night_Wed));
        day.add((CheckBox) findViewById(R.id.Day_Thu));
        night.add((CheckBox) findViewById(R.id.Night_Thu));
        day.add((CheckBox) findViewById(R.id.Day_Fri));
        night.add((CheckBox) findViewById(R.id.Night_Fri));
        day.add((CheckBox) findViewById(R.id.Day_Sat));
        night.add((CheckBox) findViewById(R.id.Night_Sat));
    }
    private void setStallButtons(){

        int index = 0;
        for(Horse listedHorse: horseList){
            if (horse.key().matches(listedHorse.key())) {
                keyIndex = index;
            }
            index++;
        }
        if(horseList.size()==1){
            nextStall.setVisibility(View.GONE);
            backStall.setVisibility(View.GONE);
        }else if(keyIndex==0){
            backStall.setVisibility(View.GONE);
            nextStall.setVisibility(View.VISIBLE);
        }else if(keyIndex==(horseList.size()-1)){
            nextStall.setVisibility(View.GONE);
            backStall.setVisibility(View.VISIBLE);
        }else{
            backStall.setVisibility(View.VISIBLE);
            nextStall.setVisibility(View.VISIBLE);
        }
    }
    private void setButtonOnClickListeners(){
        exitButton.setOnClickListener(this);
        ownerButton.setOnClickListener(this);
        nextStall.setOnClickListener(this);
        backStall.setOnClickListener(this);
        saveChanges.setOnClickListener(this);
    }
    private void getIntentObjects(){
        Intent intent = getIntent();
        horse = (Horse) intent.getSerializableExtra("horse");
        horseList = (List<Horse>) intent.getSerializableExtra("horseList");
    }
    private void setupSpinnerValues(){
        ArrayAdapter<String>[] adapters = buildAdapters();
        breedSpinner.setAdapter(adapters[0]);
        colorSpinner.setAdapter(adapters[1]);
        grainTypeSpinner.setAdapter(adapters[2]);
        haySpinner.setAdapter(adapters[3]);
        sexSpinner.setAdapter(adapters[4]);
    }
    private ArrayAdapter<String>[] buildAdapters(){
        ArrayAdapter<String>[] adapters = new ArrayAdapter[5];
        int resourceListIndex = 0;
        List<List<String>> resources = new ArrayList<List<String>>();
        resources.add(application.getBreedOptions());
        resources.add(application.getColorOptions());
        resources.add(application.getGrainOptions());
        resources.add(application.getHayOptions());
        resources.add(application.getSexOptions());
        for (List<String> strings: resources){
            //0 breeds, color, grain, hay, sex options
            String[] values = strings.toArray(new String[strings.size()]);
            ArrayAdapter<String> stringAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strings);
            stringAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapters[resourceListIndex] = stringAdapter;
            resourceListIndex++;
        }
        return adapters;
    }
    private void setInitialHorseValues(){
        String ownerName = "Owner >";

        for(int i = 0; i<user.size(); i++){
            if(user.get(i).key().toString().equals(horse.getOwner().toString())){
                ownerName = user.get(i).name() + " > ";
            }
        }
        ownerButton.setText(ownerName);
        nameInput.setText(horse.getName());
        grainAmountInput.setText(horse.getGrainAmount());
        stallInput.setText(horse.getStallNumber());
        setSpinnerValue(breedSpinner, horse.getBreed());
        setSpinnerValue(colorSpinner, horse.getColor());
        setSpinnerValue(grainTypeSpinner, horse.getGrainType());
        setSpinnerValue(haySpinner, horse.getHay());
        setSpinnerValue(haySpinner, "hay");
        String stallOutput="Stall " + horse.getStallNumber();
        setSpinnerValue(sexSpinner, horse.getSex());
        stallNumber.setText(stallOutput);
        notes.setText(horse.getNotes());
        medication.setText(horse.getMedicationInstructions());
        pictureURL.setText(horse.getPicture());
        setImage(pictureURL.toString());
        permittedRiders.setText(horse.getPermittedRiders());
        hayAmount.setText(horse.getHayAmount());
        waterAmount.setText(horse.getWaterAmount());

        //checkboxes
        String[] dayList = (horse.getInOutDay().toString().split(""));
        String[] nightList = (horse.getInOutNight().toString().split(""));

        for(int iDay= 0; iDay<day.size();iDay++){
            if(dayList[iDay+1].equals("1")){
                day.get(iDay).setChecked(true);
            }else{
                day.get(iDay).setChecked(false);
            }
        }

        for(int iNight=0; iNight<night.size();iNight++){
            if(nightList[iNight+1].equals("1")){
                night.get(iNight).setChecked(true);
            }else{
                night.get(iNight).setChecked(false);
            }
        }
    }
    private void setImage(String profilePictureURL){
        if(!horse.getPicture().equals(null)){
            Picasso.with(getApplicationContext()).load(horse.getPicture()).into(horsePicture);
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
    private void checkPermissions(){
        if(!userID.equals(horse.getOwner())){
            //make fields not editable
            nameInput.setEnabled(false);
            grainAmountInput.setEnabled(false);
            stallInput.setEnabled(false);
            breedSpinner.setEnabled(false);
            colorSpinner.setEnabled(false);
            grainTypeSpinner.setEnabled(false);
            haySpinner.setEnabled(false);
            sexSpinner.setEnabled(false);
            saveChanges.setVisibility(View.GONE);
            medication.setEnabled(false);
            notes.setEnabled(false);;
            pictureURL.setVisibility(View.GONE);
            gridInOut.setEnabled(false);
            waterAmount.setEnabled(false);
            hayAmount.setEnabled(false);
            for(int i = 0; i<day.size();i++){
                day.get(i).setEnabled(false);
                night.get(i).setEnabled(false);
            }
            permittedRiders.setEnabled(false);
        }else{
            //make fields not editable
            nameInput.setEnabled(false);
            grainAmountInput.setEnabled(true);
            stallInput.setEnabled(true);
            breedSpinner.setEnabled(true);
            colorSpinner.setEnabled(true);
            grainTypeSpinner.setEnabled(true);
            haySpinner.setEnabled(true);
            sexSpinner.setEnabled(true);
            saveChanges.setVisibility(View.VISIBLE);
            medication.setEnabled(true);
            notes.setEnabled(true);
            pictureURL.setVisibility(View.VISIBLE);
            gridInOut.setEnabled(true);
            waterAmount.setEnabled(true);
            hayAmount.setEnabled(true);
            for(int i = 0; i<day.size();i++){
                day.get(i).setEnabled(true);
                night.get(i).setEnabled(true);
            }
            permittedRiders.setEnabled(true);
        }
    }
    //save changes
    public void setSaveChanges(){
        if(thereAreNoEmptyFields()){
            setChanges();
        }
    }
    public void setChanges() {
        DataFetcher data = new DataFetcher();
        Horse newHorseData = getInputHorseValues();
        data.updateObject(newHorseData);
        horse = newHorseData;
        application.updateHorse(horse);

        showDialog("Saved Changes", "All changes to feed, supplements, & stall will take place next day. Please contact todays worker if changes need to be sooner.", false);
    }
    public Horse getInputHorseValues() {
        Horse newHorseData = new Horse(horse.key());

        newHorseData.setBreed(breedSpinner.getSelectedItem().toString());
        newHorseData.setColor(colorSpinner.getSelectedItem().toString());
        newHorseData.setGrainAmount(grainAmountInput.getText().toString());
        newHorseData.setGrainType(grainTypeSpinner.getSelectedItem().toString());
        newHorseData.setHay(haySpinner.getSelectedItem().toString());
        newHorseData.setName(nameInput.getText().toString());
        newHorseData.setSex(sexSpinner.getSelectedItem().toString());
        newHorseData.setNotes(notes.getText().toString());
        newHorseData.setOwner(userID.toString());
        newHorseData.setMedicationInstructions(medication.getText().toString());
        newHorseData.setStallNumber(stallInput.getText().toString());
        newHorseData.setPicture(pictureURL.getText().toString());
        newHorseData.setPermittedRiders(permittedRiders.getText().toString());
        newHorseData.setHayAmount(hayAmount.getText().toString());
        newHorseData.setWaterAmount(waterAmount.getText().toString());

        Calendar date = Calendar.getInstance();
        SimpleDateFormat dfN = new SimpleDateFormat("yyMdd");
        String todayNum = dfN.format(date.getTime());
        newHorseData.setLastRevisionDate(todayNum);

        Integer[] dayList = {0,0,0,0,0,0,0};
        Integer[] nightList = {0,0,0,0,0,0,0};
        for(int i= 0; i<day.size();i++){
            if(day.get(i).isChecked()) {
                dayList[i] = 1;
            }
        }

        for(int n= 0; n<night.size();n++) {
            if (night.get(n).isChecked()) {
                nightList[n] = 1;
            }
        }

        StringBuilder dayBuilder = new StringBuilder();
        StringBuilder nightBuilder = new StringBuilder();
        for (int i : dayList) {
            dayBuilder.append(i);
        }
        for(int i : nightList){
            nightBuilder.append(i);
        }
        newHorseData.setInOutDay(dayBuilder.toString());
        newHorseData.setInOutNight(nightBuilder.toString());

        return newHorseData;
    }
    private boolean thereAreNoEmptyFields(){
        String title = "Missing Information";
        String prefix = "Please complete the ";
        String body;
        String suffix = " field.";
        if(nameInput.getText().toString().matches("")){
            body = "Horse's Name";
            showDialog(title, prefix + body + suffix, false);
            return false;
        }else if(grainAmountInput.getText().toString().matches("")) {
            body = "Grain Amount";
            showDialog(title, prefix + body + suffix, false);
            return false;
        }else if(stallInput.getText().toString().matches("")) {
            body = "Horse's Name";
            showDialog(title, prefix + body + suffix, false);
            return false;
        }else{
            return true;
        }
    }
    private void showDialog(String title, String text, final boolean shouldExit){
        AlertDialog alertDialog = new AlertDialog.Builder(EditableHorse.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(text);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (shouldExit){
                            goBackToMainActivity();
                        }
                    }
                });
        alertDialog.show();
    }
    private void goBackToMainActivity(){
        Intent i = new Intent(getApplicationContext(), EditableHorse.class);
        Horse selectedHorse =horses.get(keyIndex);
        i.putExtra("userID", userID);
        i.putExtra("horse", selectedHorse);
        startActivity(i);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.save_horse_button:
                setSaveChanges();
                break;
            case R.id.back_button_clicked:
                Horse previousHorse = getNextHorse(false);
                horse = previousHorse;
                setStallButtons();
                checkPermissions();
                setInitialHorseValues();
                break;
            case R.id.next_button:
                horse = getNextHorse(true);
                setStallButtons();
                checkPermissions();
                setInitialHorseValues();
                break;
            case R.id.horse_owner_button:
                Intent i = new Intent(this, Profile.class);
                i.putExtra("user", application.getUser(horse.getOwner()));
                i.putExtra("contact", application.getContact(horse.getOwner()));
                startActivity(i);
                break;
            default:
                finish();
        }
    }
    private Horse getNextHorse(boolean next){
        int newHorseIndex = getCurrentHorseIndex();
        if (next){
            newHorseIndex++;
            return horseList.get(newHorseIndex);
        }
        newHorseIndex--;
        return horseList.get(newHorseIndex);
    }
    private int getCurrentHorseIndex(){
        int index = 0;
        for(Horse listedHorse: horseList){
            if (horse.key().matches(listedHorse.key())){
                return index;
            }
            index++;
        }
        return 0;
    }
}
