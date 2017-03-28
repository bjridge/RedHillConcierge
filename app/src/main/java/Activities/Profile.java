package Activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.squareup.picasso.Picasso;

import DataControllers.Change;
import DataControllers.Contact;
import DataControllers.DataFetcher;
import DataControllers.DatabaseObject;
import DataControllers.User;


public class Profile extends AppCompatActivity implements View.OnClickListener {

    private ImageButton exitButton;
    private FloatingActionButton nextButton;
    private ImageView profilePicture;
    private ImageView profilePictureBackground;
    private EditText pictureInput;
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

        Intent i = getIntent();
        user = (User) i.getSerializableExtra("user");
        Boolean isNewUser = i.getBooleanExtra("isNewUser", true);
        initializeViewResources();
        setupSpinners();
        setupButton();

        if (isNewUser){
            setupFirstTimeProfile(i.getStringExtra("pictureURL"));
        }else{
            setInitialUserValues();
            fetchContact(user.key());
        }
    }
    private void setupFirstTimeProfile(String pictureURL){
        showFirstTimeDialog();
        contact = new Contact(user.key());
        contact.setPhoto(pictureURL);
        setInitialUserValues();
        setInitialContactValues();
    }
    private void showFirstTimeDialog(){
        String dialogTitle = "Welcome to Red Hill Concierge!";
        String dialogText = "It looks like this is your first time using Red Hill Concierge with this account.  We just need a few things from you so other users can contact you in the case of an emergency.";
        showDialog(dialogTitle, dialogText, false);
    }

    private void fetchContact(String id){
        DataFetcher df = new DataFetcher();
        Task<DatabaseObject> fetchUserTask = df.getObject("contact", id);
        fetchUserTask.addOnCompleteListener(new OnCompleteListener<DatabaseObject>() {
            @Override
            public void onComplete(@NonNull Task<DatabaseObject> task) {
                contact = (Contact) task.getResult();
                setInitialContactValues();
            }
        });
    }
    private void initializeViewResources(){
        exitButton = (ImageButton) findViewById(R.id.profile_exit_button);
        nextButton = (FloatingActionButton) findViewById(R.id.profile_save_button);
        profilePicture = (ImageView) findViewById(R.id.profile_image);
        profilePictureBackground = (ImageView) findViewById(R.id.profile_image_background);
        pictureInput = (EditText) findViewById(R.id.profile_photo_input);
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
        nextButton.setOnClickListener(this);
    }
    private void setInitialContactValues(){
        phoneOneInput.setText(contact.getPrimaryPhone());
        phoneTwoInput.setText(contact.getSecondaryPhone());
        streetAddressInput.setText(contact.getStreetAddress());
        cityInput.setText(contact.getCity());
        zipCodeInput.setText(contact.getZip());
        int stateIndex = getIndex(stateSpinner, contact.getState());
        stateSpinner.setSelection(stateIndex);
        pictureInput.setText(contact.getPhoto());
        String profilePictureURL = contact.getPhoto();
        if (!profilePictureURL.matches("")){
            Picasso.with(getApplicationContext()).load(contact.getPhoto()).into(profilePicture);
        }
    }
    private void setInitialUserValues(){
        firstNameInput.setText(user.getFirstName());
        lastNameInput.setText(user.getLastName());
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
                if (noEmptyFields()){
                    handleChanges();
                }
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
        Intent returnIntent = new Intent(Profile.this, BasicUserView.class);
        returnIntent.putExtra("user", user);
        setResult(Activity.RESULT_OK, returnIntent);
        startActivity(returnIntent);
        finish();
    }
    private boolean noEmptyFields(){
        String title = "Missing Information";
        String prefix = "Please complete the ";
        String body;
        String suffix = " field.";
        if (pictureInput.getText().toString().matches("")){
            body = "Picture URL";
            showDialog(title, prefix + body + suffix, false);
            return false;
        }if (firstNameInput.getText().toString().matches("")){
            body = "First Name";
            showDialog(title, prefix + body + suffix, false);
            return false;
        }if (lastNameInput.getText().toString().matches("")){
            body = "Last Name";
            showDialog(title, prefix + body + suffix, false);
            return false;
        }if (phoneOneInput.getText().toString().matches("")){
            body = "Phone One";
            showDialog(title, prefix + body + suffix, false);
            return false;
        }if (phoneTwoInput.getText().toString().matches("")){
            body = "Phone Two";
            showDialog(title, prefix + body + suffix, false);
            return false;
        }if (streetAddressInput.getText().toString().matches("")){
            body = "Street Address";
            showDialog(title, prefix + body + suffix, false);
            return false;
        }if (cityInput.getText().toString().matches("")){
            body = "City";
            showDialog(title, prefix + body + suffix, false);
            return false;
        }if (zipCodeInput.getText().toString().matches("")){
            body = "Zip Code";
            showDialog(title, prefix + body + suffix, false);
            return false;
        }
        return true;
    }
    private void handleChanges() {

        String picture = pictureInput.getText().toString();
        String userType = userTypeSpinner.getSelectedItem().toString();
        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();
        String primaryPhone = phoneOneInput.getText().toString();
        String secondaryPhone = phoneTwoInput.getText().toString();
        String address = streetAddressInput.getText().toString();
        String city = cityInput.getText().toString();
        String state = stateSpinner.getSelectedItem().toString();
        String zip = zipCodeInput.getText().toString();

        User userInfo = new User(user.key());
        userInfo.setType(userType);
        userInfo.setFirstName(firstName);
        userInfo.setLastName(lastName);
        Contact contactInfo = new Contact(user.key());
        contactInfo.setKey(contact.key());
        contactInfo.setName(firstName + " " + lastName);
        contactInfo.setPhoto(picture);
        contactInfo.setPrimaryPhone(primaryPhone);
        contactInfo.setSecondaryPhone(secondaryPhone);
        contactInfo.setStreetAddress(address);
        contactInfo.setCity(city);
        contactInfo.setState(state);
        contactInfo.setZip(zip);
        DataFetcher dc = new DataFetcher();

        if (!contact.equals(contactInfo)) {
            dc.updateObject(contactInfo);
        }
        if (!user.equals(userInfo)){
            //then changes to user were made, create change
            if (!user.getType().matches(userInfo.getType())){
                buildChange(user, userInfo);
                String title = "Administrative Approval Required";
                String message = "A request has been sent to an administrator to change your user type from " + user.getType() +
                        " to " + userType + ".";
                showDialog(title, message, true);
                userInfo.setType(user.getType());
                dc.updateObject(userInfo);
                user = userInfo;
                return;
            }
            dc.updateObject(userInfo);
            user = userInfo;
        }
        exitProfileView();
    }
    private void showDialog(String title, String text, final boolean shouldExit){
        AlertDialog alertDialog = new AlertDialog.Builder(Profile.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(text);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        //exit the app
                        if (shouldExit){
                            exitProfileView();
                        }
                    }
                });
        alertDialog.show();
    }
    private void buildChange(User oldUser, User newUser){
        //requires approval
        Change change = new Change();
        change.setKey(user.key() + "-user-userType");
        change.setOldValue(user.getType());
        change.setNewValue(newUser.getType());
        change.setObjectKey(user.key());
        DataFetcher dc = new DataFetcher();
        dc.updateObject(change);
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
