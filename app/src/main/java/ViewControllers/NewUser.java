package ViewControllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import DataControllers.Contact;
import DataControllers.User;

public class NewUser extends AppCompatActivity {

    TextView firstNameInput;
    TextView lastNameInput;
    TextView emailInput;
    TextView phoneNumberInput;
    TextView streetAddressInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_user);

        Intent i = getIntent();
        String id = i.getStringExtra("id");
        String name = i.getStringExtra("name");
        String picture = i.getStringExtra("pictureURL");
        String email = i.getStringExtra("email");

        User newUser = new User(id);
        String[] names = name.split(" ");
        String firstName = names[0];
        String lastName = names[1];
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);

        Contact newContact = new Contact(id);


        firstNameInput = (TextView) findViewById(R.id.firstNameInput);
        lastNameInput = (TextView) findViewById(R.id.lastNameInput);
        emailInput = (TextView) findViewById(R.id.emailInput);
        phoneNumberInput = (TextView) findViewById(R.id.phoneNumberInput);
        streetAddressInput = (TextView) findViewById(R.id.streetAddressInput);

        firstNameInput.setText(newUser.getFirstName());
        lastNameInput.setText(newUser.getLastName());
    }
}
