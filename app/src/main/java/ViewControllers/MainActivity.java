package ViewControllers;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import DataControllers.Contact;
import DataControllers.DatabaseObject;
import DataControllers.Horse;
import DataControllers.User;
import DataControllers.DatabaseController;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;//FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText firstNameInput;
    EditText lastNameInput;
    EditText typeInput;
    Button addUserButton;

    DatabaseController controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeEditTexts();
        initializeButton();

        controller = new DatabaseController();
    }

    private void initializeEditTexts(){
        firstNameInput = (EditText) findViewById(R.id.firstNameInput);
        firstNameInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firstNameInput.setText("");
            }
        });
        lastNameInput = (EditText) findViewById(R.id.lastNameInput);
        lastNameInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lastNameInput.setText("");
            }
        });
        typeInput = (EditText) findViewById(R.id.typeInput);
        typeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                typeInput.setText("");
            }
        });
    }
    private void initializeButton(){
        addUserButton = (Button) findViewById(R.id.addUserButton);

        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAllUsers();
            }
        });
    }
    private void updateUser(){
        User newUser = new User();
        newUser.setFirstName("Bradley");
        newUser.setLastName("Ridge");
        newUser.setType("Lead Developer");
        newUser.setKey(1);
        controller.updateObject(newUser);
    }

    private void tryAddNewUser(){
        if (firstNameInput.getText().equals("") || lastNameInput.getText().equals("") || typeInput.getText().equals("")){
            firstNameInput.setText("must complete all fields");
            return;
        }
        addNewUser();
    }
    private void addNewUser(){
        String firstName = firstNameInput.getText().toString();
        String lastName = lastNameInput.getText().toString();
        String type = typeInput.getText().toString();

        User newUser = new User();
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setType(type);

        DatabaseController controller = new DatabaseController();
        controller.addObject(newUser);
    }


    private ArrayList<DatabaseObject> output;

    private void getAllUsers() {
        final Task task = controller.getAll("user");
        task.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                output = (ArrayList<DatabaseObject>) task.getResult();

                //do things here!

                for (DatabaseObject object: output){
                    User user = (User) object;
                    Contact contact = new Contact();
                    contact.setName(user.getFirstName() + " " + user.getLastName());
                    contact.setKey(user.key());
                    controller.updateObject(contact);
                }





            }
        });
    }
}
