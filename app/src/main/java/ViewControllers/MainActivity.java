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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    TextView testOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("do something");

        testOutput = (TextView) findViewById(R.id.testOutput);
        getAUser();
        }

    private void getAUser(){
        FirebaseDatabase allData = FirebaseDatabase.getInstance();
        DatabaseReference userData = allData.getReference("user/0");
        userData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User newUser = dataSnapshot.getValue(User.class);
                testOutput.setText("new user: " + newUser.getFirstName());
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
}
