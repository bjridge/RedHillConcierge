package Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import Application.MyApplication;
import DataControllers.Change;
import DataControllers.Contact;
import DataControllers.DataFetcher;
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
    private TextView pictureLabel;

    private MyApplication application;
    private User user;
    private Contact contact;
    private boolean userIsNew;

    GoogleApiClient mGoogleApiClient;
    FirebaseAuth mAuth;

    private int returnCode = 0;
    //0: viewed/edited your own profile
    //1: viewed someone else's profile


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__profile);
        application = (MyApplication) getApplication();

        getIntentObjects();
        initializeViewResources();
        setupSpinners();
        setupButton();

        contact = application.getContact(user.key());
        if (contact == null){
            //new user
            userIsNew = true;
            contact = application.getContact();
            showNewUserDialog();
            setupProfileForNewUser();
        }else{
            userIsNew = false;
            setupProfileForExistingUser();
        }
        checkUser();
    }
    private void getIntentObjects(){
        Intent i = getIntent();
        user = (User) i.getSerializableExtra("user");
        log("got to profile");

    }
    private void showNewUserDialog(){
        String dialogTitle = "Welcome to Red Hill Concierge!";
        String dialogText = "It looks like this is your first time using Red Hill Concierge with this account.  We just need a few things from you so other users can contact you in the case of an emergency.";
        showDialog(dialogTitle, dialogText, false);
    }
    private void showDialog(String title, String text, final boolean shouldExit){
        AlertDialog alertDialog = new AlertDialog.Builder(Profile.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(text);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (shouldExit){
                            goToMainActivity();
                        }
                    }
                });
        alertDialog.show();
    }
    private void setupProfileForNewUser(){
        setNewUserValues();
        setInitialUserValues();
        setInitialContactValuesForNewUser();
    }
    private void setNewUserValues(){
        user.setType("Basic User");
    }
    private void setInitialUserValues(){
        firstNameInput.setText(user.getFirstName());
        lastNameInput.setText(user.getLastName());
        int userTypeIndex = getIndex(userTypeSpinner, user.getType());
        userTypeSpinner.setSelection(userTypeIndex);
    }
    private void setInitialContactValuesForNewUser(){
        setImage(contact.getPhoto());
        pictureInput.setText(contact.getPhoto());
    }


    private void setupProfileForExistingUser(){
        setInitialUserValues();
        setInitialContactValues();
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
        pictureLabel = (TextView) findViewById(R.id.lblPicture);

    }
    private void checkUser(){
        String loggedInUser = application.getUser().key();

        if(!loggedInUser.equals(user.key()) ){
            logOffButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.GONE);
            pictureInput.setVisibility(View.GONE);
            pictureLabel.setVisibility(View.GONE);
            userTypeSpinner.setEnabled(false);
            firstNameInput.setEnabled(false);
            lastNameInput.setEnabled(false);
            phoneOneInput.setEnabled(false);
            phoneTwoInput.setEnabled(false);
            streetAddressInput.setEnabled(false);
            cityInput.setEnabled(false);
            stateSpinner.setEnabled(false);
            zipCodeInput.setEnabled(false);
        }else{
            logOffButton.setVisibility(View.VISIBLE);
            nextButton.setVisibility(View.VISIBLE);
            pictureInput.setVisibility(View.VISIBLE);
            pictureLabel.setVisibility(View.VISIBLE);
            userTypeSpinner.setEnabled(true);
            firstNameInput.setEnabled(false);
            lastNameInput.setEnabled(false);
            phoneOneInput.setEnabled(true);
            phoneTwoInput.setEnabled(true);
            streetAddressInput.setEnabled(true);
            cityInput.setEnabled(true);
            stateSpinner.setEnabled(true);
            zipCodeInput.setEnabled(true);
        }
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
        setImage(contact.getPhoto());
    }
    private void setImage(String profilePictureURL){
        if (!profilePictureURL.matches("")){
            Picasso.with(getApplicationContext()).load(contact.getPhoto()).into(profilePicture);
        }
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
                goTo(Authentication.class);
                break;
            case R.id.back_button_clicked:
                finish();
                break;
            default:
                if (thereAreNoEmptyFields()){
                    updateUserAndContact();
                }else{
                    goToMainActivity();
                }
                break;
        }
    }
    private void logOut(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        mAuth.signOut();
    }
    private void goTo(Class nextView){
        Intent i = new Intent(getApplicationContext(), nextView);
        i.putExtra("user", user);
        i.putExtra("contact", contact);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(i);
        finish();
    }
    private boolean thereAreNoEmptyFields(){
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
    private void updateUserAndContact() {
        DataFetcher data = new DataFetcher();
        boolean restrictedChangeOccured = false;
        if (userValuesChanged() || userIsNew){
            User newUserValues = getInputUserValues();
            restrictedChangeOccured = checkForRestrictedChanges(newUserValues);
            newUserValues.setType(user.getType());
            data.updateObject(newUserValues);
            user = newUserValues;
            application.updateUser(user);
        }
        if (contactValuesChanged() || userIsNew){
            Contact newContactValues = getInputContactValues();
            data.updateObject(newContactValues);
            contact = newContactValues;
            application.updateContact(contact);
        }
        if (!restrictedChangeOccured){
            goToMainActivity();
        }
    }

    private boolean userValuesChanged(){
        User originalUserValues = user;
        User newUserValues = getInputUserValues();
        if (originalUserValues.equals(newUserValues)){
            return false;
        }
        return true;
    }
    private User getInputUserValues(){
        User newUserValues = new User();

        String typeValue = userTypeSpinner.getSelectedItem().toString();
        String firstNameValue = firstNameInput.getText().toString();
        String lastNameValue = lastNameInput.getText().toString();

        newUserValues.setKey(user.key());
        newUserValues.setType(typeValue);
        newUserValues.setFirstName(firstNameValue);
        newUserValues.setLastName(lastNameValue);

        return newUserValues;
    }
    private boolean contactValuesChanged(){
        Contact originalContactValues = contact;
        Contact inputContactValues = getInputContactValues();
        if (originalContactValues.equals(inputContactValues)){
            return false;
        }
        return true;
    }
    private Contact getInputContactValues(){
        Contact newContactValues = new Contact(contact.key());

        String name = firstNameInput.getText().toString() + " " + lastNameInput.getText().toString();
        String picture = pictureInput.getText().toString();
        String primaryPhone = phoneOneInput.getText().toString();
        String secondaryPhone = phoneTwoInput.getText().toString();
        String address = streetAddressInput.getText().toString();
        String city = cityInput.getText().toString();
        String state = stateSpinner.getSelectedItem().toString();
        String zip = zipCodeInput.getText().toString();

        newContactValues.setName(name);
        newContactValues.setPhoto(picture);
        newContactValues.setPrimaryPhone(primaryPhone);
        newContactValues.setSecondaryPhone(secondaryPhone);
        newContactValues.setStreetAddress(address);
        newContactValues.setCity(city);
        newContactValues.setState(state);
        newContactValues.setZip(zip);

        return newContactValues;
    }
    private boolean checkForRestrictedChanges(User newUserValues) {
        if (!user.getType().matches(newUserValues.getType())) {
            buildChange(user, newUserValues);
            String title = "Change Requires Permission";
            String body = "A request to change your user type from " + user.getType() + " to " + newUserValues.getType() + " has been sent.";
            showDialog(title, body, true);
            return true;
        }
        return false;
    }


    private void buildChange(User oldUser, User newUser){
        Change change = new Change();
        change.setKey(user.key() + "-user-userType");
        change.setOldValue(user.getType());
        change.setNewValue(newUser.getType());
        change.setObjectKey(user.key());
        DataFetcher dc = new DataFetcher();
        dc.updateObject(change);
    }
    private void goToMainActivity(){
        if (userIsNew){
            goTo(BasicUserView.class);
        }else{
            goBackToMainActivity();
        }
    }
    private void goBackToMainActivity(){
        Intent intent = getIntent();
        intent.putExtra("user", user);
        intent.putExtra("contact", contact);
        Log.v("important", "about to end profile activity");
        setResult(0, intent);
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

    private void log(String text){
        Log.v("IMPORTANT", text);
    }

}
