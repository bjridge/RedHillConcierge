package Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Button;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import Activities.Fragments.MyHorsesTab;
import Application.MyApplication;
import DataControllers.DataFetcher;
import DataControllers.Horse;
import DataControllers.User;
import DataControllers.Change;

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
    TextView stall;
    ImageButton backStall;
    ImageButton nextStall;
    Button saveChanges;
    //TextView medication;
    //TextView notes;
    //TextView stallInstuction;
    //TextView pictureURL;
    //ImageView horsePicture;

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
        stall = (TextView) findViewById(R.id.horse_stall_input);
        nextStall = (ImageButton) findViewById(R.id.next_button);
        backStall = (ImageButton) findViewById(R.id.back_button_clicked);
        saveChanges = (Button) findViewById(R.id.save_horse_button);
        //medication = (TextView) findViewById(R.id.horse_medication);
        //notes = (TextView) findViewById(R.id.horse_notes);
        //stallInstuction = (TextView) findViewById(R.id.horse_stall_instruction);
        //pictureURL = (TextView) findViewById(R.id.horse_photo_input);
        //horsePicture = (ImageView) findViewById(R.id.horse_image);
    }
    private void setStallButtons(){
        keyIndex = horseList.indexOf(horse.key());
        if(keyIndex==0){
            backStall.setVisibility(View.GONE);
        }else if(keyIndex==(horseList.size()-1)){
            nextStall.setVisibility(View.GONE);
        }else if(horseList.size()==1){
            nextStall.setVisibility(View.GONE);
            backStall.setVisibility(View.GONE);
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
        stall.setText(stallOutput);
        setSpinnerValue(sexSpinner, horse.getSex());
        //notes.setText(horse.getNotes());
        //medication.setText(horse.getMedicationInstructions());
        //stallInstuction.setText(horse.getStallInstructions());
        //setImage(pictureURL.toString());
    }

    private void setImage(String profilePictureURL){
        if (!profilePictureURL.matches("")){
          //  Picasso.with(getApplicationContext()).load(horse.getPicture()).into(horsePicture);
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
            nameInput.setKeyListener(null);
            grainAmountInput.setKeyListener(null);
            stallInput.setKeyListener(null);
            breedSpinner.setOnKeyListener(null);
            colorSpinner.setOnKeyListener(null);
            grainTypeSpinner.setOnKeyListener(null);
            haySpinner.setOnKeyListener(null);
            sexSpinner.setOnKeyListener(null);
            saveChanges.setVisibility(View.GONE);
//            medication.setKeyListener(null);
//            notes.setKeyListener(null);
//            stallInstuction.setKeyListener(null);
//            pictureURL.setVisibility(View.GONE);
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
        newHorseData.setNotes("testing");
        newHorseData.setOwner(userID.toString());
        newHorseData.setMedicationInstructions("nothing");
        newHorseData.setStallInstructions("null");
        newHorseData.setStallNumber(stallInput.getText().toString());
        //newHorseData.setPicture(pictureURL.getText().toString());

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
        Intent i;
        Horse selectedHorse;
        switch(v.getId()){
            case R.id.save_horse_button:
                setSaveChanges();
                break;
            case R.id.back_button_clicked:
                //get current horse
                //get previous horse
                //update horse values

                break;
            case R.id.next_button:
                //get current horse
                //get next horse
                //update horse values
                break;
            case R.id.horse_owner_button:
                i = new Intent(this, Profile.class);
                i.putExtra("user", application.getUser(horse.getOwner()));
                i.putExtra("contact", application.getContact(horse.getOwner()));
                startActivity(i);
                break;
            default:
                finish();
        }
    }
}
