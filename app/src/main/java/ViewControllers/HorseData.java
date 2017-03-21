package ViewControllers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import DataControllers.DatabaseController;
import DataControllers.DatabaseObject;
import DataControllers.Horse;


public class HorseData extends AppCompatActivity {
    TextView spnHorseName;
    TextView lblOwnerName;
    EditText txtStall;
    EditText txtYear;
    Spinner spnColor;
    Spinner spnBreed;
    Spinner spnGender;
    Spinner spnGrainType;
    EditText txtGrainAmount;
    Spinner spnHay;
    EditText txtHayAmount;
    EditText txtMedical;
    Spinner spnVet;
    EditText txtBehavoir;
    EditText txtRiders;
    EditText txtNotes;
    ImageButton btnSearch;
    ImageButton btnFeed ;
    ImageButton btnCal;
    ImageButton btnBack;
    ImageButton btnPerson;

    DatabaseController controller=new DatabaseController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.horse_layout);
        initializeEditableFields();
    }

    private void initializeEditableFields() {
        lblOwnerName = (TextView) findViewById(R.id.lblOwnerName);
        spnHorseName =(TextView) findViewById(R.id.spnHorseName);
        txtStall = (EditText) findViewById(R.id.txtStall);
        txtYear = (EditText) findViewById(R.id.txtYear);
        spnColor = (Spinner) findViewById(R.id.spnColor);
        spnBreed = (Spinner) findViewById(R.id.spnBreed);
        spnGender = (Spinner) findViewById(R.id.spnGender);
        spnGrainType = (Spinner) findViewById(R.id.spnGrainType);
        txtGrainAmount = (EditText) findViewById(R.id.txtGranAmount);
        spnHay = (Spinner) findViewById(R.id.spnHay);
        txtHayAmount = (EditText) findViewById(R.id.txtHayAmount);
        txtMedical = (EditText) findViewById(R.id.txtMedical);
        spnVet = (Spinner) findViewById(R.id.spnVet);
        txtBehavoir = (EditText) findViewById(R.id.txtBehavoir);
        txtRiders=(EditText) findViewById(R.id.txtRiders);
        txtNotes =(EditText) findViewById(R.id.txtNotes);

        /*btnHome = (ImageButton) findViewById(R.id.home_icon); 
        btnSearch = (ImageButton) findViewById(R.id.search_icon); 
        btnFeed = (ImageButton) findViewById(R.id.feed_icon); 
        btnCal = (ImageButton) findViewById(R.id.col_icon); 
        btnBack = (ImageButton) findViewById(R.id.backIcon); 
        btnPerson = (ImageButton) findViewById(R.id.personIcon);*/

        Task<ArrayList<DatabaseObject>> getAllHorse = controller.getAll("horse");

        /*getAllHorse.addOnCompleteListener(new OnCompleteListener<ArrayList<DatabaseObject>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<DatabaseObject>> task) {
                ArrayList<DatabaseObject> objects = task.getResult();

                //TODO: Change get(#) to horse id that is passed in
                //TODO: make sure that if vicki changes the grain or hay type it will not offect the auto selected
                Horse horse = (Horse) objects.get(1);
                lblOwnerName.setText(horse.getName());
                //spnHorseName.setText(horse.getName());
                //txtStall.setText(horse.getStallInstructions());
                //txtYear.setText(horse.getYear());
                //spnBreed.setSelection(((ArrayAdapter)spnBreed.getAdapter()).getPosition(horse.getBreed()));
                //spnColor.setSelection(((ArrayAdapter)spnColor.getAdapter()).getPosition(horse.getColor()));
                //spnGrainType.setSelection(((ArrayAdapter)spnGrainType.getAdapter()).getPosition(horse.getGrainType()));
                //txtGrainAmount.setText(horse.getGrainAmount());
                //spnHay.setSelection(((ArrayAdapter)spnHay.getAdapter()).getPosition(horse.getHay()));
                //txtMedical.setText(horse.getMedicationInstructions());
                //txtNotes.setText(horse.getNotes());
                //spnGender.setSelection(((ArrayAdapter)spnGender.getAdapter()).getPosition(horse.getSex()));

            }
        });*/
    }
}
