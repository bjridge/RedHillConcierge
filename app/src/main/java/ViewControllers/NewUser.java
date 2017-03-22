package ViewControllers;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import DataControllers.Contact;
import DataControllers.User;


public class NewUser extends AppCompatActivity {

    TextView firstNameInput;
    TextView lastNameInput;
    Spinner userTypeInput;
    TextView primaryPhoneInput;
    TextView secondaryPhoneInput;
    TextView streetAddressInput;
    TextView zipInput;
    Spinner stateInput;
    Button completeButton;
    ImageView profilePicture;



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
        String id = i.getStringExtra("id");
        String name = i.getStringExtra("name");
        String picture = i.getStringExtra("pictureURL");

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
        //Picasso.with(getApplicationContext()).load(pictureAddress).into(imageView);
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
        completeButton = (Button) findViewById(R.id.completeButton);

        ArrayAdapter<CharSequence> userTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.user_types, android.R.layout.simple_dropdown_item_1line);
        userTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        userTypeInput.setAdapter(userTypeAdapter);

        ArrayAdapter<CharSequence> stateAdapter = ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_dropdown_item_1line);
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stateInput.setAdapter(stateAdapter);

        completeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //verify all fields are complete
                //transition to main app

            }
        });
    }

    private void showFirstTimeDialog(){
        AlertDialog alertDialog = new AlertDialog.Builder(NewUser.this).create();
        alertDialog.setTitle("Welcome to Red Hill Concierge!");
        alertDialog.setMessage("It looks like this is your first time using Red Hill Concierge with this account.  We just need a few things from you so other users can contact you in the case of an emergency.");
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


}
