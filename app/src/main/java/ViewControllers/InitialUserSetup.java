package ViewControllers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;
import com.squareup.picasso.Picasso;

import DataControllers.Contact;
import DataControllers.DatabaseController;
import DataControllers.User;


public class InitialUserSetup extends AppCompatActivity {

    TextView firstNameInput;
    TextView lastNameInput;
    Spinner userTypeInput;
    TextView primaryPhoneInput;
    TextView secondaryPhoneInput;
    TextView streetAddressInput;
    TextView cityInput;
    TextView zipInput;
    TextView photoInput;
    Spinner stateInput;
    FloatingActionButton completeButton;
    ImageView profilePicture;

    String id;
    String photoBackup;


    // TODO: 3/22/2017 onChange for the user type, tell them they have to be approved but will start out as basic user!

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_user_login);

        setupViewObjects();
        setInitialValues();
        showFirstTimeDialog();


    }

    private void setInitialValues(){
        Intent i = getIntent();
        id = i.getStringExtra("id");
        String name = i.getStringExtra("name");
        String picture = i.getStringExtra("pictureURL");
        photoInput.setText(picture);
        photoBackup = picture;

        User newUser = new User(id);
        String[] names = name.split(" ");
        String firstName = names[0];
        String lastName = names[1];
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);

        firstNameInput.setText(firstName);
        lastNameInput.setText(lastName);
        setPicture(picture);
    }

    private void setPicture(String pictureAddress){
        Picasso.with(getApplicationContext()).load(pictureAddress).into(profilePicture);
    }

    private void setupViewObjects(){

        profilePicture = (ImageView) findViewById(R.id.profilePicture);
        firstNameInput = (TextView) findViewById(R.id.firstNameInput);
        lastNameInput = (TextView) findViewById(R.id.lastNameInput);
        userTypeInput = (Spinner) findViewById(R.id.userTypeInput);
        primaryPhoneInput = (TextView) findViewById(R.id.primaryPhoneInput);
        secondaryPhoneInput = (TextView) findViewById(R.id.secondaryPhoneInput);
        streetAddressInput = (TextView) findViewById(R.id.streetAddressInput);
        zipInput = (TextView) findViewById(R.id.zipInput);
        stateInput = (Spinner) findViewById(R.id.stateInput);
        completeButton = (FloatingActionButton) findViewById(R.id.completeButton);
        photoInput = (TextView) findViewById(R.id.photoInput);
        cityInput = (TextView) findViewById(R.id.cityInput);

        ArrayAdapter<CharSequence> userTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.user_types, android.R.layout.simple_dropdown_item_1line);
        userTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeInput.setAdapter(userTypeAdapter);

        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(this, R.array.states, R.layout.spinner_item);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateInput.setAdapter(stateAdapter);



        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verify all fields are complete
                //create new user/contact objects for firebase
                testFieldsAndContinue();
            }
        });
    }

    private void testFieldsAndContinue(){
        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();
        String userType = userTypeInput.getSelectedItem().toString();
        String photo = photoInput.getText().toString();
        String primaryPhone = primaryPhoneInput.getText().toString();
        String secondaryPhone = secondaryPhoneInput.getText().toString();
        String streetAddress = streetAddressInput.getText().toString();
        String zip = zipInput.getText().toString();
        String state = stateInput.getSelectedItem().toString();
        String city = cityInput.getText().toString();


        String title = "Incomplete Information";
        String prefix = "Please complete the ";
        String text = "";
        String suffix = " field.";

        if ( firstName.matches("")){
            text = "First Name";
            showDialog(title, prefix + text + suffix);
            return;
        }else if(lastName.matches("")){
            text = "Last Name";
            showDialog(title, prefix + text + suffix);
            return;
        }else if(userType.matches("")){
            text = "User Type";
            showDialog(title, prefix + text + suffix);
            return;
        }else if(primaryPhone.matches("")){
            text = "Primary Phone";
            showDialog(title, prefix + text + suffix);
            return;
        }else if(secondaryPhone.matches("")){
            text= "Secondary Phone";
            showDialog(title, prefix + text + suffix);
            return;
        }else if(streetAddress.matches("")) {
            text = "Street Address";
            showDialog(title, prefix + text + suffix);
            return;
        }else if(city.matches("")) {
            text = "City";
            showDialog(title, prefix + text + suffix);
            return;
        }else if(zip.matches("")){
            text = "Zip Code";
            showDialog(title, prefix + text + suffix);
            return;
        }else if (state.matches("")){
            text = "State";
            showDialog(title, prefix + text + suffix);
            return;
        }else{

            User user = new User(id);
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setType("Basic User");

            Contact contact = new Contact(id);
            contact.setName(firstName + " " + lastName);
            contact.setPrimaryPhone(primaryPhone);
            contact.setSecondaryPhone(secondaryPhone);
            contact.setStreetAddress(streetAddress);
            contact.setState(state);
            contact.setCity(city);
            contact.setZip(zip);
            if (photo.matches("")){
                photo = photoBackup;
            }
            contact.setPhoto(photo);

            buildNewUser(user, contact);

            goToMainActivity(user);
        }
    }

    private void goToMainActivity(User user){
        Context context = getApplicationContext();
        Intent i = new Intent(context, MainActivity.class);
        i.putExtra("id", user.key());
        startActivity(i);
    }


    private void buildNewUser(User user, Contact contact){
        DatabaseController dc = new DatabaseController();
        dc.updateObject(user);
        dc.updateObject(contact);
    }





    private void showFirstTimeDialog(){

        String title = "Welcome to Red Hill Concierge!";
        String text = "It looks like this is your first time using Red Hill Concierge with this account.  We just need a few things from you so other users can contact you in the case of an emergency.";

        showDialog(title, text);
    }

    private void showDialog(String title, String text){
        AlertDialog alertDialog = new AlertDialog.Builder(InitialUserSetup.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(text);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


}
