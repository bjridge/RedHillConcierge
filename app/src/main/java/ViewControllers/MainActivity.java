package ViewControllers;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import DataControllers.DatabaseObject;
import DataControllers.User;
import DataControllers.UserController;

import com.google.firebase.database.*;//FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("do something");

    }

    private void doTheThing(){
        System.out.println("did the thing");
        final TextView testOutput = (TextView) findViewById(R.id.testOutput);
        ArrayList<DatabaseObject> userObjectsOutput = new ArrayList<DatabaseObject>();
        if (userObjectsOutput.size() == 0){
            testOutput.setText("nothing yet");
        }
        System.out.println("array size:");
        UserController userController = new UserController();
        userController.getAllUsers(userObjectsOutput);
        System.out.println("array size:");

        //userObjectsOutput should have been updated by now
        if (userObjectsOutput.size() == 0){
            testOutput.setText("shits zero, yo");
        }else{
            testOutput.setText(userObjectsOutput.get(0).getName());
        }
    }

    private User buildTestUser(){
        User u = new User();
        u.setFirstName("test5");
        u.setLastName("test6");
        u.setType("test employee 2");
        return u;
    }



}
