package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import java.util.ArrayList;
import java.util.List;

import Application.MyApplication;
import DataControllers.Horse;

public class EditableHorse extends AppCompatActivity implements View.OnClickListener {

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

    MyApplication application;
    Horse horse;


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

        initializeViewObjects();
        getIntentObjects();
        setButtonOnClickListeners();
        setupSpinnerValues();

        setInitialHorseValues();



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
    }
    private void setButtonOnClickListeners(){
        exitButton.setOnClickListener(this);
        ownerButton.setOnClickListener(this);
    }
    private void getIntentObjects(){
        Intent intent = getIntent();
        horse = (Horse) intent.getSerializableExtra("horse");
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
        nameInput.setText(horse.getName());
        grainAmountInput.setText(horse.getGrainAmount());
        stallInput.setText(horse.getStallNumber());
        setSpinnerValue(breedSpinner, horse.getBreed());
        setSpinnerValue(colorSpinner, horse.getColor());
        setSpinnerValue(grainTypeSpinner, horse.getGrainType());
        setSpinnerValue(haySpinner, horse.getHay());
        setSpinnerValue(haySpinner, "hay");
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


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.horse_owner_button){
            Intent i = new Intent(this, Profile.class);
            i.putExtra("user", horse.getOwner());
            startActivityForResult(i, 2);
        }else{
            setResult(1);
            finish();
        }
    }
}
