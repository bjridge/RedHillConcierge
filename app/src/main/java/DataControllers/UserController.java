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
public class UserController {

    public void addObject(DatabaseObject object) {

        final DatabaseObject newObject = object;
        String objectType = newObject.getClass().toString();
        System.out.println("this is a " + objectType);
//
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference reference = database.getReference("counts/" + objectType);
//        int lastID;
//        reference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                int value = dataSnapshot.getValue(int.class);
//                testUser.id = value;
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });


    }


















    public void addObject(String objectType, DatabaseObject object){

    }




    public User buildTestUser(){
        User u = new User();
        u.setFirstName("kelly");
        u.setLastName("test one last name");
        u.setType("standard");
        return u;
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
                switch (dataType){
                    case "user":
                        objects = getUsersFromDataSnapshot(dataSnapshot);
                        break;
                    case "horse":
                        objects = getHorsesFromDataSnapshot(dataSnapshot);
                        break;
                    default:
                        //do something with jobs/tasks
                        // TODO: 2/9/2017  add task/job objects
                        break;
                }
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
