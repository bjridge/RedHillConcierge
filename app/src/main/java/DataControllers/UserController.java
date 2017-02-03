package DataControllers;

import android.provider.ContactsContract;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserController {

    ArrayList<DatabaseObject> output;

    //gets all users
    public void getAllUsers(ArrayList<DatabaseObject> outputArrayList){
        this.output = outputArrayList;
        System.out.println("output was set");
        //get database
        FirebaseDatabase completeDatabase = getCompleteDatabase();
        //get user table
        DatabaseReference userTable = getUserTable(completeDatabase);
        //get user table values
            //set this as a listener to a reference to get the values
        ValueEventListener allUsersListener = buildValueEventListenerForDatabaseReference(userTable);
        userTable.addValueEventListener(allUsersListener);
    }
    private FirebaseDatabase getCompleteDatabase(){
        FirebaseDatabase completeDatabase = FirebaseDatabase.getInstance();
        return completeDatabase;
    }
    private DatabaseReference getUserTable(FirebaseDatabase completeDatabase){
        DatabaseReference userTable = completeDatabase.getReference("user");
        return userTable;
    }

    private ValueEventListener buildValueEventListenerForDatabaseReference(DatabaseReference table){
        ValueEventListener dataBuilder = new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                //assume it is always users
                ArrayList<DatabaseObject> newUsers = buildDatabaseObjectsFromSnapshot(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError){
            }
        };
        return dataBuilder;
    }
    private ArrayList<DatabaseObject> buildDatabaseObjectsFromSnapshot(DataSnapshot dataSnapshot){
        ArrayList<DatabaseObject> newObjects = new ArrayList<DatabaseObject>();

        String objectType = dataSnapshot.getKey();

        if (objectType == "user"){
            //build users
            Iterable<DataSnapshot> userSnapshots = dataSnapshot.getChildren();
            for (DataSnapshot userSnapshot: userSnapshots){
                User newUser = userSnapshot.getValue(User.class);
                newObjects.add(newUser);
                //test System.out.println("new " + newUser.getFirstName());
            }
        }else if (objectType == "horse"){
            //build horse
        }

        //build other types later

        return newObjects;
    }







}
