package Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import DataControllers.Contact;
import DataControllers.DatabaseController;
import DataControllers.DatabaseObject;
import DataControllers.User;


public class Profile extends AppCompatActivity implements View.OnClickListener {

    private ImageButton exitButton;
    private ImageView profilePicture;
    private ImageView profilePictureBackground;
    private Spinner userTypeSpinner;
    private Button logOffButton;
    private EditText firstNameInput;
    private EditText lastNameInput;
    private EditText phoneOneInput;
    private EditText phoneTwoInput;
    private EditText streetAddressInput;
    private EditText cityInput;
    private Spinner stateSpinner;
    private EditText zipCodeInput;

    private User user;
    private Contact contact;

    GoogleApiClient mGoogleApiClient;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__profile);
        initializeViewResources();
        setupSpinners();
        setupButton();
        getUser();

    }
    private void initializeViewResources(){
        exitButton = (ImageButton) findViewById(R.id.profile_exit_button);
        profilePicture = (ImageView) findViewById(R.id.profile_image);
        profilePictureBackground = (ImageView) findViewById(R.id.profile_image_background);
        userTypeSpinner = (Spinner) findViewById(R.id.profile_user_type_spinner);
        logOffButton = (Button) findViewById(R.id.profile_log_out_button);
        firstNameInput = (EditText) findViewById(R.id.profile_first_name_input);
        lastNameInput = (EditText) findViewById(R.id.profile_last_name_input);
        phoneOneInput = (EditText) findViewById(R.id.profile_phone_one_input);
        phoneTwoInput = (EditText) findViewById(R.id.profile_phone_two_input);
        streetAddressInput = (EditText) findViewById(R.id.profile_street_address_input);
        cityInput = (EditText) findViewById(R.id.profile_city_input);
        stateSpinner = (Spinner) findViewById(R.id.profile_state_spinner);
        zipCodeInput = (EditText) findViewById(R.id.profile_zip_code_input);

        Intent i = getIntent();
        String id = i.getStringExtra("id");
        user = new User(id);
    }
    private void setupSpinners(){
        ArrayAdapter<CharSequence> userTypeAdapter = ArrayAdapter.createFromResource(this,

                R.array.user_types, R.layout.custom_spinner_item);
        userTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeSpinner.setAdapter(userTypeAdapter);


        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_dropdown_item_1line);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateSpinner.setAdapter(stateAdapter);
    }
    private void setupButton(){
        exitButton.setOnClickListener(this);
        logOffButton.setOnClickListener(this);
    }
    private void getUser(){
        DatabaseController dc = new DatabaseController();
        dc.getObject("user", user.key()).addOnCompleteListener(new OnCompleteListener<DatabaseObject>() {
            @Override
            public void onComplete(@NonNull Task<DatabaseObject> task) {
                user = (User) task.getResult();
            }
        });
        dc.getObject("contact", user.key()).addOnCompleteListener(new OnCompleteListener<DatabaseObject>() {
            @Override
            public void onComplete(@NonNull Task<DatabaseObject> task) {
                contact = (Contact) task.getResult();
                setInitialValues();
            }
        });
    }
    private void setInitialValues(){
        firstNameInput.setText(user.getFirstName());
        lastNameInput.setText(user.getLastName());
        phoneOneInput.setText(contact.getPrimaryPhone());
        phoneTwoInput.setText(contact.getSecondaryPhone());
        streetAddressInput.setText(contact.getStreetAddress());
        cityInput.setText(contact.getCity());
        zipCodeInput.setText(contact.getZip());
        int stateIndex = getIndex(stateSpinner, contact.getState());
        stateSpinner.setSelection(stateIndex);
        int userTypeIndex = getIndex(userTypeSpinner, user.getType());
        userTypeSpinner.setSelection(userTypeIndex);
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
        switch(v.getId()){
            case R.id.profile_log_out_button:
                logOut();
                break;
            default:
                exitProfileView();
                break;
        }
    }
    private void logOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // ...
                        Toast.makeText(getApplicationContext(),"Logged Out",Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(getApplicationContext(), Authentication.class);
                        startActivity(i);
                    }
                });
        mAuth.signOut();
    }
    private void exitProfileView(){
        Context context = getApplicationContext();
        Intent i = new Intent(Profile.this, BasicUserView.class);
        i.putExtra("id", user.key());
        startActivity(i);
        finish();
    }




    @Override
    protected void onStart() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        mGoogleApiClient.connect();

        mAuth = FirebaseAuth.getInstance();

        super.onStart();
    }

}
