package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import DataControllers.Contact;
import DataControllers.Horse;
import DataControllers.User;

public class EditableHorse extends AppCompatActivity implements View.OnClickListener {

    TextView nameInput;
    Spinner breedSpinner;
    Spinner colorSpinner;
    TextView grainAmountInput;
    Spinner grainTypeSpinner;
    CheckBox hayInput;
    Spinner sexSpinner;
    TextView stallInput;
    ImageButton exitButton;

    Horse horse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__editable_horse);

        initializeViewObjects();
        getHorseFromIntent();
        setInitialHorseValues();
        setButtonOnClickListeners();



    }
    private void initializeViewObjects(){
        nameInput = (TextView) findViewById(R.id.horse_name_input);
        breedSpinner = (Spinner) findViewById(R.id.horse_breed_spinner);
        colorSpinner = (Spinner) findViewById(R.id.horse_color_spinner);
        grainAmountInput = (TextView) findViewById(R.id.horse_grain_amount_input);
        grainTypeSpinner = (Spinner) findViewById(R.id.horse_grain_type_spinner);
        hayInput = (CheckBox) findViewById(R.id.horse_hay_checkbox);
        sexSpinner = (Spinner) findViewById(R.id.horse_sex_spinner);
        stallInput = (TextView) findViewById(R.id.horse_stall_input);
        exitButton = (ImageButton) findViewById(R.id.horse_exit_button);
    }
    private void setButtonOnClickListeners(){
        exitButton.setOnClickListener(this);
    }
    private void getHorseFromIntent(){
        Intent intent = getIntent();
        horse = (Horse) intent.getSerializableExtra("horse");
    }
    private void setInitialHorseValues(){
        nameInput.setText(horse.getName());
        grainAmountInput.setText(horse.getGrainAmount());
        hayInput.setText(horse.isHay());
        stallInput.setText(horse.getStallNumber());
    }

    @Override
    public void onClick(View v) {
        setResult(1);
        finish();
    }
}
