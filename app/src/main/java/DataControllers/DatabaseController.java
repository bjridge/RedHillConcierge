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

//data controller
public class DatabaseController {

    public void addObject(DatabaseObject object) {


        System.out.println("add object was called");
        setID(object);
        System.out.println("about to add object");
    }
    private void setID(final DatabaseObject object){
        final DatabaseObject newObject = object;
        String objectType = getObjectType(object);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference reference = database.getReference("counts/" + objectType);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int value = dataSnapshot.getValue(int.class);
                newObject.setID(value + 1);
                System.out.println("object id set to:" + newObject.getID());
                add(newObject);
                incrementID(object);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void add(DatabaseObject object){

        String objectType = getObjectType(object);
        int objectID =  object.id;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference(objectType + "/" + objectID);
        reference.setValue(object);
    }

    private String getObjectType(DatabaseObject object){
        String objectType = object.getClass().toString();
        String removableString = "class DataControllers.";
        String newString = objectType.replaceAll(removableString, "");
        return newString.toLowerCase();
    }

    private void incrementID(DatabaseObject object){
        String objectType = getObjectType(object);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("counts/" + objectType);
        reference.setValue(object.getID());
    }

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
        //returns back up the chain to the UI
        return outputTask.getTask();
    }


    private ValueEventListener buildListener(TaskCompletionSource<ArrayList<DatabaseObject>> task){
        final TaskCompletionSource<ArrayList<DatabaseObject>> outputTask = task;
        ValueEventListener newListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<DatabaseObject> objects = new ArrayList<>();
                String dataType = dataSnapshot.getKey();
                //build a list of objects from the datasnapshot children
                    //how to get class type?
                outputTask.setResult(objects);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        return newListener;
    }
    private ArrayList<DatabaseObject> getUsersFromDataSnapshot(DataSnapshot dataSnapshot){
        ArrayList<DatabaseObject> users = new ArrayList<>();
        for (DataSnapshot userSnapshot: dataSnapshot.getChildren()){
            User newUser = userSnapshot.getValue(User.class);
            users.add(newUser);
        }
        return users;
    }
    private ArrayList<DatabaseObject> getHorsesFromDataSnapshot(DataSnapshot dataSnapshot){
        ArrayList<DatabaseObject> horses = new ArrayList<>();
        for (DataSnapshot horseSnapshot: dataSnapshot.getChildren()){
            Horse newUser = horseSnapshot.getValue(Horse.class);
            horses.add(newUser);
        }
        return horses;
    }




}
