package Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Application.MyApplication;
import DataControllers.DataFetcher;
import DataControllers.User;

public class Profile2 extends AppCompatActivity {
    GoogleApiClient mGoogleApiClient;
    FirebaseAuth mAuth;

    MyApplication application;
    User user;
    boolean isNewUser;

    ImageView image;
    FloatingActionButton callButton;
    FloatingActionButton saveButton;
    EditText nameInput;
    Spinner typeSpinner;
    EditText addressInput;
    EditText primaryPhoneInput;
    EditText secondaryPhoneInput;
    EditText imageInput;
    Button logOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__user_profile);
        application = (MyApplication) getApplication();

        initializeViewResources();
        initializeAdapters();

        getIntentValues();
        if (isNewUser){
            Toast.makeText(this, "new user", Toast.LENGTH_SHORT).show();
            showNewUserDialog();
            setNewUserValues();
            callButton.setVisibility(View.GONE);
            setupSaveFunction();
            logOutButton.setVisibility(View.GONE);
        }else{
            setUserValues();
            if (userIsNotAdministrator() && userDoesNotOwnProfile()){
                disableEditing();
                setupCallFunction();
                logOutButton.setVisibility(View.GONE);
            }else{
                setupSaveFunction();
                if (!userDoesNotOwnProfile()){
                    setupLogOutFunction();
                }
            }
        }
    }


    private void initializeViewResources(){
        image = (ImageView) findViewById(R.id.user_profile_image);
        callButton = (FloatingActionButton) findViewById(R.id.user_profile_call_button);
        saveButton = (FloatingActionButton) findViewById(R.id.user_profile_save_button);
        nameInput = (EditText) findViewById(R.id.user_profile_name_input);
        typeSpinner = (Spinner) findViewById(R.id.user_profile_user_type_spinner);
        addressInput = (EditText) findViewById(R.id.user_profile_street_address);
        primaryPhoneInput = (EditText) findViewById(R.id.user_profile_primary_phone_input);
        secondaryPhoneInput = (EditText) findViewById(R.id.user_profile_secondary_phone_input);
        imageInput = (EditText) findViewById(R.id.user_profile_image_input);
        logOutButton = (Button) findViewById(R.id.user_profile_log_out_button);
    }
    private void initializeAdapters(){
        String[] userTypeOptions = {"Basic User","Employee","Administrator"};
        ArrayAdapter<String> newAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.custom__spinner_white_small, userTypeOptions);
        newAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(newAdapter);
    }
    private void getIntentValues(){
        Intent i = getIntent();
        user = (User) i.getSerializableExtra("user");
        String isNewUserString = i.getStringExtra("isNewUser");
        if (isNewUserString == null){
            isNewUser = false;
        }else{
            isNewUser = true;
        }
    }
    private void showNewUserDialog(){
        String dialogTitle = "Welcome to Red Hill Concierge!";
        String dialogText = "It looks like this is your first time using Red Hill Concierge with this account.  We just need a few things from you so other users can contact you in the case of an emergency.";
        showDialog(dialogTitle, dialogText, false);
    }
    private void showDialog(String title, String text, final boolean shouldExit){
        AlertDialog alertDialog = new AlertDialog.Builder(Profile2.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(text);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (shouldExit){

                            exitActivity();
                        }else{

                        }
                    }
                });
        alertDialog.show();
    }
    private void setNewUserValues(){
        nameInput.setText(user.getName());
        int index = getIndex(typeSpinner, "Basic User");
        typeSpinner.setSelection(index);
        trySetImage();
        imageInput.setText(user.getImage());
        user.setType("Basic User");
        user.setSecondaryPhone("123");
        user.setPrimaryPhone("123");

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
    private void trySetImage(){
        try {
            Picasso.with(getApplicationContext()).load(user.getImage()).into(image);

        }catch (Exception e){
            Toast.makeText(this, "Failed to build photo", Toast.LENGTH_SHORT).show();
        }
    }
    private void setUserValues(){
        nameInput.setText(user.getName());
        int index = getIndex(typeSpinner, user.getType());
        typeSpinner.setSelection(index);
        trySetImage();
        addressInput.setText(user.getAddress());
        primaryPhoneInput.setText(user.getPrimaryPhone());
        secondaryPhoneInput.setText(user.getSecondaryPhone());
        imageInput.setText(user.getImage());
    }
    private boolean userIsNotAdministrator(){
        if (application.getUser().getType().matches("Administrator")){
            return false;
        }
        return true;
    }
    private boolean userDoesNotOwnProfile(){
        if (application.getUser().key().matches(user.key())){
            return false;
        }
        return true;
    }
    private void disableEditing(){
        image.setEnabled(false);
        nameInput.setEnabled(false);
        typeSpinner.setEnabled(false);
        addressInput.setEnabled(false);
    }
    private void setupCallFunction(){
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri number = Uri.parse("tel:" + user.getPrimaryPhone());
                Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                startActivity(callIntent);
            }
        });
    }
    private void setupSaveFunction(){
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trySaveUser();
            }
        });
    }

    private void trySaveUser(){
        if (noFieldsAreEmpty()){
            handleChanges();
        }
    }
    private boolean noFieldsAreEmpty(){
        if (nameInput.getText().toString().matches("")){
            alert("Please enter a name");
            return false;
        }
        if (addressInput.getText().toString().matches("")){
            alert("Please enter an address");
            return false;
        }
        if (primaryPhoneInput.getText().toString().matches("")){
            alert("Please enter a primary phone number");
            return false;
        }
        if (secondaryPhoneInput.getText().toString().matches("")){
            alert("Please enter a secondary phone number");
            return false;
        }
        if (imageInput.getText().toString().matches("")){
            alert("Please copy/paste a profile picture url");
            return false;
        }
        return true;
    }
    private void alert(String message){
        Toast.makeText(application, message, Toast.LENGTH_SHORT).show();
    }
    private User getInputUserValues(){
        User newUserValues = new User();
        newUserValues.setKey(user.key());
        newUserValues.setName(nameInput.getText().toString());
        newUserValues.setType(typeSpinner.getSelectedItem().toString());
        newUserValues.setAddress(addressInput.getText().toString());
        newUserValues.setPrimaryPhone(primaryPhoneInput.getText().toString());
        newUserValues.setSecondaryPhone(secondaryPhoneInput.getText().toString());
        newUserValues.setImage(imageInput.getText().toString());
        return newUserValues;
    }
    private void handleChanges(){
        User inputUserValues = getInputUserValues();
        if (!user.equals(inputUserValues)){
            resetRestrictedChanges(inputUserValues);
            setUserValues();
        }
        commitChanges(inputUserValues);
    }
    private void resetRestrictedChanges(User inputUserValues){
        if (user.getType() == null){
            alert("null type on input");
            return;
        }
        if (!user.getType().matches(inputUserValues.getType())){
            if (inputUserValues.getType() == null){
                Log.v("IMPORTANT5", "USER TYPE IS NULL");
            }
            if (userIsNotAdministrator()){
                showDialog("Admin Permission Required", "A request has been sent to an admin to update your user type.", false);
                inputUserValues.setType(user.getType());
            }
        }
        if (!user.getName().matches(inputUserValues.getName())){
            if (!isNewUser){
                if (userIsNotAdministrator()){
                    showDialog("Admin Permission Required", "A request has been sent to an admin to update your name.", false);
                    inputUserValues.setName(user.getName());
                }
            }
        }
    }
    private void setupLogOutFunction(){
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
                Intent i = new Intent(getApplicationContext(), Authentication.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
            }
        });
    }
    private void logout(){
        Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        mAuth.signOut();
    }
    private void commitChanges(User newUserValues){
        this.user = newUserValues;
        application.setUser(user);
        DataFetcher data = new DataFetcher();
        data.updateObject(newUserValues);
        if (isNewUser){
            showDialog("Thank You!", "You are officially registered with Red Hill Concierge!  Click 'okay' to continue to the app.", true);
        }else if (userIsNotAdministrator()){
            //exitActivity();
        }else{
            alert("User Information Saved");
        }
    }
    private void exitActivity(){
        Intent intent = getIntent();
        if (isNewUser){
            setResult(0, intent);
        }else{
            setResult(1, intent);
        }
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