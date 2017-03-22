package ViewControllers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import DataControllers.Contact;
import DataControllers.DatabaseController;
import DataControllers.DatabaseObject;

public class PersonalInfo extends AppCompatActivity {
    TextView ownerName;
    EditText ownerPhone;
    EditText ownerStreet;
    EditText ownerCity;
    EditText ownerState;
    EditText emergencyName;
    EditText emergencyPhone;
    EditText emergencyStreet;
    EditText emergencyCity;
    EditText emergencyState;
    EditText vetName;
    EditText vetPhone;
    EditText vetStreet;
    EditText vetCity;
    EditText vetState;
    Button save;
    ImageButton search;
    ImageButton feed ;
    ImageButton cal;
    ImageButton back;
    ImageButton person;

    DatabaseController controller=new DatabaseController();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_info_layout);
        initializeEditableFields();
    }

    private void initializeEditableFields(){
        ownerName = (TextView) findViewById(R.id.lblOwnerName);
        ownerPhone = (EditText) findViewById(R.id.txtOwnerPhone);
        ownerStreet = (EditText) findViewById(R.id.txtOwnerStreet);
        ownerCity = (EditText) findViewById(R.id.txtOwnerCity);
        ownerState = (EditText) findViewById(R.id.txtOwnerState);
        emergencyName = (EditText) findViewById(R.id.txtEmergencyName);
        emergencyPhone = (EditText) findViewById(R.id.txtEmergencyPhone);
        //emergencyStreet = (EditText) findViewById(R.id.txtEmergencyStreet);
        emergencyCity = (EditText) findViewById(R.id.txtEmergencyCity);
        //emergencyState = (EditText) findViewById(R.id.txtEmergencyStreet);
        vetName = (EditText) findViewById(R.id.txtVetName);
        vetPhone = (EditText) findViewById(R.id.txtVetPhone);
        vetStreet = (EditText) findViewById(R.id.txtVetStreet);
        vetCity = (EditText) findViewById(R.id.txtVetCity);
        vetState = (EditText) findViewById(R.id.txtVetState);

        Task<ArrayList<DatabaseObject>> getContactInfo = controller.getAll("contact");
        getContactInfo.addOnCompleteListener(new OnCompleteListener<ArrayList<DatabaseObject>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<DatabaseObject>> task) {
                ArrayList<DatabaseObject> objects = task.getResult();
                Contact contactInfo = (Contact) objects.get(0);
                ownerName.setText(contactInfo.getName());
            }
        });
    }
}
