package ViewControllers;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeEditTexts();
        initializeButton();
    }

    private void initializeEditTexts(){
        firstNameInput = (EditText) findViewById(R.id.firstNameInput);
        lastNameInput = (EditText) findViewById(R.id.lastNameInput);
        typeInput = (EditText) findViewById(R.id.typeInput);
    }
    private void initializeButton(){
        addUserButton = (Button) findViewById(R.id.addUserButton);

        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tryAddNewUser();
                getAllUsers();

            }
        });
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




    private void getAllUsers() {
        DatabaseController dataController = new DatabaseController();
        final Task task = dataController.getAll("user");
        task.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                ArrayList<DatabaseObject> output = (ArrayList<DatabaseObject>) task.getResult();
                System.out.println(output.size() + " users found");
                // TODO: 2/12/2017 do somethign with output objects
            }
        });
    }
}
