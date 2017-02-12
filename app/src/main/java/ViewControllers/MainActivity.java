package ViewControllers;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import DataControllers.DatabaseObject;
import DataControllers.Horse;
import DataControllers.User;
import DataControllers.DatabaseController;

import com.google.android.gms.tasks.OnCompleteListener;
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
                tryAddNewUser();
            }
        });
    }

    private void tryAddNewUser(){
        if (firstNameInput.getText().equals("") || lastNameInput.getText().equals("") || typeInput.getText().equals("")){
            firstNameInput.setText("must complete all fields");
            return;
        }
        System.out.println("about to try to add");
        //addNewUser();
        testAddHorse();
    }

    private void testAddHorse(){
        Horse h = new Horse();
        DatabaseController controller = new DatabaseController();
        controller.addObject(h);
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

        //getLastUserID();

    }












    private void getAUser(){
        FirebaseDatabase allData = FirebaseDatabase.getInstance();
        DatabaseReference userData = allData.getReference("user/0");
        userData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User newUser = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void addAUser(){
        FirebaseDatabase allData = FirebaseDatabase.getInstance();
        DatabaseReference userData = allData.getReference("user/0");
    }











    private void doTheThing(){
        DatabaseController testController = new DatabaseController();
        Task<ArrayList<DatabaseObject>> getAllUsersTask = testController.getAll("user");

        getAllUsersTask.addOnCompleteListener(new OnCompleteListener<ArrayList<DatabaseObject>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<DatabaseObject>> task) {
                ArrayList<DatabaseObject> users = task.getResult();
                System.out.println("it is done" + users.size());
                //listView.setInput(users)
            }
        });
        System.out.println("end users: " );
    }
    private OnCompleteListener<ArrayList<DatabaseObject>> buildTheThing(){
        return
                new OnCompleteListener<ArrayList<DatabaseObject>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<DatabaseObject>> task) {
                ArrayList<DatabaseObject> users = task.getResult();
                System.out.println("it is done" + users.size());
                //listView.setInput(users)
            }
        };
    }
}
