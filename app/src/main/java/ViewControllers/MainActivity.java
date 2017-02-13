package ViewControllers;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.lang.reflect.Array;
import java.util.ArrayList;

import DataControllers.Contact;
import DataControllers.DatabaseController;
import DataControllers.DatabaseObject;
import DataControllers.Horse;
import DataControllers.User;

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
                testMethod();
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
        controller.addNewObject(newUser);
    }



    private void testMethod() {
        System.out.println("clicked");
        final Task<ArrayList<DatabaseObject>> task = controller.getAll("horse");
        task.addOnSuccessListener(new OnSuccessListener<ArrayList<DatabaseObject>>() {
            @Override
            public void onSuccess(ArrayList<DatabaseObject> databaseObjects) {
                ArrayList<DatabaseObject> objects = (ArrayList<DatabaseObject>) task.getResult();
                Horse user = (Horse) objects.get(4);
                firstNameInput.setText(user.key() + "");
                lastNameInput.setText(user.getName());
            }
        });
    }
}
