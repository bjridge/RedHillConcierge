package Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
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
import java.util.Date;
import java.util.Locale;
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
    TextView stallInstuction;
    TextView lblStall;
    TextView pictureURL;
    ImageView horsePicture;

    //checkboxes
    GridLayout gridInOut;
    CheckBox sunDay;
    CheckBox sunNight;
    CheckBox monDay;
    CheckBox monNight;
    CheckBox tuesDay;
    CheckBox tuesNight;
    CheckBox wedDay;
    CheckBox wedNight;
    CheckBox thuDay;
    CheckBox thuNight;
    CheckBox friDay;
    CheckBox friNight;
    CheckBox satDay;
    CheckBox satNight;

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
        lblStall = (TextView) findViewById(R.id.lblStall);
        stallInstuction = (TextView) findViewById(R.id.horse_stall_instruction);
        pictureURL = (TextView) findViewById(R.id.horse_photo_input);
        horsePicture = (ImageView) findViewById(R.id.horse_image);

        //check boxes
        sunDay = (CheckBox) findViewById(R.id.Day_Sun);
        sunNight = (CheckBox) findViewById(R.id.Night_Sun);
        monDay = (CheckBox) findViewById(R.id.Day_Mon);
        monNight = (CheckBox) findViewById(R.id.Night_Mon);
        tuesDay = (CheckBox) findViewById(R.id.Day_Tues);
        tuesNight = (CheckBox) findViewById(R.id.Night_Tues);
        wedDay = (CheckBox) findViewById(R.id.Day_Wed);
        wedNight = (CheckBox) findViewById(R.id.Night_Wed);
        thuDay = (CheckBox) findViewById(R.id.Day_Thu);
        thuNight = (CheckBox) findViewById(R.id.Night_Thu);
        friDay = (CheckBox) findViewById(R.id.Day_Fri);
        friNight = (CheckBox) findViewById(R.id.Night_Fri);
        satDay = (CheckBox) findViewById(R.id.Day_Sat);
        satNight = (CheckBox) findViewById(R.id.Night_Sat);
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
        userID = intent.getStringExtra("userID");
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

        //checkboxes
        String[] day = (horse.getInOutDay().toString().split(""));
        String[] night = (horse.getInOutNight().toString().split(""));

        if(day[1].equals("1")){
            sunDay.setChecked(true);
        } else{
            sunDay.setChecked(false);
        }
        if(night[1].equals("1")){
            sunNight.setChecked(true);
        }else{
            sunNight.setChecked(false);
        }
        if(day[2].equals("1")){
            monDay.setChecked(true);
        } else{
            monDay.setChecked(false);
        }
        if(night[2].equals("1")){
            monNight.setChecked(true);
        }else{
            monNight.setChecked(false);
        }
        if(day[3].equals("1")){
            tuesDay.setChecked(true);
        } else{
            tuesDay.setChecked(false);
        }
        if(night[3].equals("1")){
            tuesNight.setChecked(true);
        } else{
            tuesNight.setChecked(false);
        }
        if(day[4].equals("1")){
            wedDay.setChecked(true);
        }else{
            wedDay.setChecked(false);
        }
        if(night[4].equals("1")){
            wedNight.setChecked(true);
        }else{
            wedNight.setChecked(false);
        }
        if(day[5].equals("1")){
            thuDay.setChecked(true);
        }else{
            thuDay.setChecked(false);
        }
        if(night[5].equals("1")){
            thuNight.setChecked(true);
        }else{
            thuNight.setChecked(false);
        }
        if(day[6].equals("1")){
            friDay.setChecked(true);
        }else{
            friDay.setChecked(false);
        }
        if(night[6].equals("1")){
            friNight.setChecked(true);
        }else {
            friNight.setChecked(false);
        }
        if(day[7].equals("1")){
            satDay.setChecked(true);
        }else{
            satDay.setChecked(false);
        }
        if(night[7].equals("1")){
            satNight.setChecked(true);
        }else{
            satNight.setChecked(false);
        }

        //todays stall needs
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String today = new SimpleDateFormat("EE", Locale.ENGLISH).format(date.getTime());
        String[] daysOfWeek = {"Sun", "Mon", "Tues", "Wed", "Thurs","Fri","Sat" };
        String dayInOut = "Out";
        String nightInOut = "Out";
        for(int i = 0; i<daysOfWeek.length;i++){
            if(daysOfWeek[i].equals(today)){
                if(day[i].equals("1")){
                    dayInOut="Inside";
                }
                if(night[i].equals("1")){
                    nightInOut="Inside";
                }
            }
        }
        lblStall.setText(today + " Stalled?");
        stallInstuction.setText("Day " + dayInOut + " Night " +nightInOut);

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
            stallInstuction.setEnabled(false);;
            pictureURL.setVisibility(View.GONE);
            gridInOut.setVisibility(View.GONE);
            stallInstuction.setVisibility(View.VISIBLE);
            lblStall.setVisibility(View.VISIBLE);
        }else{
            //make fields not editable
            nameInput.setEnabled(true);
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
            stallInstuction.setEnabled(true);;
            pictureURL.setVisibility(View.VISIBLE);
            gridInOut.setVisibility(View.VISIBLE);
            stallInstuction.setVisibility(View.GONE);
            lblStall.setVisibility(View.GONE);
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

        showDialog("Saved Changes", "All changes to feed, medication, & staying In/Out will take place next day. Please contact todays worker if need changed sooner.", false);
    }
    public Horse getInputHorseValues(){
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

        Integer[] day = {0,0,0,0,0,0,0};
        Integer[] night = {0,0,0,0,0,0,0};
        if(sunDay.isChecked()){
            day[0]=1;
        }
        if(monDay.isChecked()){
            day[1]=1;
        }
        if(tuesDay.isChecked()){
            day[2]=1;
        }
        if(wedDay.isChecked()){
            day[3]=1;
        }
        if(thuDay.isChecked()){
            day[4]=1;
        }
        if(friDay.isChecked()){
            day[5]=1;
        }
        if(satDay.isChecked()){
            day[6]=1;
        }

        if(sunNight.isChecked()){
            night[0]=1;
        }
        if(monNight.isChecked()){
            night[1]=1;
        }
        if(tuesNight.isChecked()){
            night[2]=1;
        }
        if(wedNight.isChecked()){
            night[3]=1;
        }
        if(thuNight.isChecked()){
            night[4]=1;
        }
        if(friNight.isChecked()){
            night[5]=1;
        }
        if(satNight.isChecked()){
            night[6]=1;
        }

        StringBuilder dayBuilder = new StringBuilder();
        StringBuilder nightBuilder = new StringBuilder();
        for (int i : day) {
            dayBuilder.append(i);
        }
        for(int i : night){
            nightBuilder.append(i);
        }
        Log.e( "getInputHorseValues: ", "testing" + nightBuilder.toString());
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
