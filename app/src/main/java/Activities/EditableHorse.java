package Activities;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import Activities.Fragments.MyHorsesTab;
import Application.MyApplication;
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
    TextView stall;
    ImageButton backStall;
    ImageButton nextStall;

    MyApplication application;

    List<User> user;
    Horse horse;
    String userID;
    ArrayList<String> horseKeys;
    List<Horse> horses;


    //restricted changes - 24 hour change only; no permission
        //midication instructions
        //hay
        //grain
        //inOutDay
        //inOutNight

    //if emergency, contact today's employee

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
        stall = (TextView) findViewById(R.id.stall_on);
        nextStall = (ImageButton) findViewById(R.id.next_button);
        backStall = (ImageButton) findViewById(R.id.back_button);
    }
    private void setStallButtons(){
        keyIndex = horseKeys.indexOf(horse.key());
        if(keyIndex==0){
            backStall.setVisibility(View.GONE);
        }else if(keyIndex==(horseKeys.size()-1)){
            nextStall.setVisibility(View.GONE);
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
    }
    private void getIntentObjects(){
        Intent intent = getIntent();
        horse = (Horse) intent.getSerializableExtra("horse");
        userID = (String) intent.getSerializableExtra("userID");
        horseKeys= intent.getExtras().getStringArrayList("horseList");
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
        }
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch(v.getId()){
            case R.id.horse_exit_button:
                //needs fixed
//                i = new Intent(this, BasicUserView.class);
//                setResult(1,i);
//                finish();
            case R.id.next_button:
                i = new Intent(getApplicationContext(), EditableHorse.class);
                i.putStringArrayListExtra("horseList", horseKeys);
                Horse selectedHorse =horses.get(keyIndex+1);

                i.putExtra("userID", userID);
                i.putExtra("horse", selectedHorse);
                startActivity(i);
                break;
            case R.id.horse_owner_button:
                i = new Intent(this, Profile.class);
                i.putExtra("user", horse.getOwner());
                startActivity(i);
                break;
            default:
                setResult(1);
                finish();
        }
    }
}
