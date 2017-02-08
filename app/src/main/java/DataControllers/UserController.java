package DataControllers;


import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UserController {

    // TODO: 2/8/2017 figure out how to send output from this class to a different class (in the UI)
    // TODO: 2/8/2017 get user by ID 
    // TODO: 2/8/2017 get user by name 
    // TODO: 2/8/2017 get user by horse (goes into ownership)

    //gets all of any object
    public Task<ArrayList<DatabaseObject>> getAll(String objectTypeRequested){
        Task<ArrayList<DatabaseObject>> fetchAllObjectsTask = fetchData(objectTypeRequested);
        return fetchAllObjectsTask;
    }
    private Task<ArrayList<DatabaseObject>> fetchData(String objectType){
        DatabaseReference dataLocation = getDataLocation(objectType);
        Task<ArrayList<DatabaseObject>> task = buildTask(dataLocation);
        return task;
    }
    private DatabaseReference getDataLocation(String objectType){
        FirebaseDatabase allData = FirebaseDatabase.getInstance();
        DatabaseReference objectData = allData.getReference(objectType);
        return objectData;
    }

    private Task<ArrayList<DatabaseObject>> buildTask(DatabaseReference reference){
        final TaskCompletionSource<ArrayList<DatabaseObject>> outputTask = new TaskCompletionSource<ArrayList<DatabaseObject>>();
        ValueEventListener newListener = buildListener(outputTask);
        reference.addValueEventListener(newListener);
        return outputTask.getTask();
    }
    private ArrayList<DatabaseObject> getUsersFromDataSnapshot(DataSnapshot dataSnapshot){
        ArrayList<DatabaseObject> users = new ArrayList<>();
        for (DataSnapshot userSnapshot: dataSnapshot.getChildren()){
            User newUser = userSnapshot.getValue(User.class);
            users.add(newUser);
        }
        return users;
    }

    private ValueEventListener buildListener(TaskCompletionSource<ArrayList<DatabaseObject>> task){
        final TaskCompletionSource<ArrayList<DatabaseObject>> outputTask = task;
        ValueEventListener newListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<DatabaseObject> objects = new ArrayList<>();
                if (dataSnapshot.getKey() == "user"){
                    objects = getUsersFromDataSnapshot(dataSnapshot);
                }
                outputTask.setResult(objects);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        return newListener;
    }




}
