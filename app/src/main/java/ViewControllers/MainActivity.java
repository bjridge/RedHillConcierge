package ViewControllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;
import DataControllers.User;
import com.google.firebase.database.*;//FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import android.widget.TextView;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        //how do we separate this task?

        final TextView testOutput = (TextView) findViewById(R.id.testOutput);
    //does the thing
        //get database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference users = database.getReference("user");
        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String lastUserID = "";
                Iterable<DataSnapshot> users = dataSnapshot.getChildren();
                for (DataSnapshot user: users){
                    lastUserID = (String) user.getKey();
                }
                testOutput.setText(lastUserID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private User buildTestUser(){
        User u = new User();
        u.setFirstName("test5");
        u.setLastName("test6");
        u.setType("test employee 2");
        return u;
    }



}
