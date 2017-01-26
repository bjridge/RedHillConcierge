package ViewControllers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;
import DataControllers.User;
import com.google.firebase.database.*;//FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final TextView testOutput = (TextView) findViewById(R.id.testOutput);
        testOutput.setText("about to try to fetch data");

        //get database reference
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        testOutput.setText("made it past reference");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User outputUser = dataSnapshot.child("user/0").getValue(User.class);
                testOutput.setText(outputUser.getFirstName());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });








    }


}
