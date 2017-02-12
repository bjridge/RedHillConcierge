package ViewControllers;

import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import DataControllers.DatabaseObject;
import DataControllers.User;
import DataControllers.UserController;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;//FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

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
                firstNameInput.setText("button was clicked");
            }
        });
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
        final TextView testOutput = (TextView) findViewById(R.id.testOutput);
        UserController testController = new UserController();
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
