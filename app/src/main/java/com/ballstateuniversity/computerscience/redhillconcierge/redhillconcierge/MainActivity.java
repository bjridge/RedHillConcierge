package com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.database.*;//FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //connect to database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        TextView editText = (TextView) findViewById(R.id.testOutput);
        editText.setText("did it work?");


    }


}
