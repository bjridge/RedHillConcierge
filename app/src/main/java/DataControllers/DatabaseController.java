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

public class DatabaseController {

    FirebaseDatabase db;

    public DatabaseController(){
        db = FirebaseDatabase.getInstance();
    }

    public void addObject(final DatabaseObject object) {
        String objectType = getObjectType(object);
        DatabaseReference reference = db.getReference("counts/" + objectType);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int value = dataSnapshot.getValue(int.class);
                object.setID(value + 1);
                writeToDatabase(object);
                incrementID(object);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void writeToDatabase(DatabaseObject object){
        String objectType = getObjectType(object);
        int objectID =  object.id;
        DatabaseReference reference = db.getReference(objectType + "/" + objectID);
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
        DatabaseReference reference = db.getReference("counts/" + objectType);
        reference.setValue(object.getID());
    }




    //gets all of any object
    public Task<ArrayList<DatabaseObject>> getAll(String objectType){
        TaskCompletionSource<ArrayList<DatabaseObject>> resultsLocation = new TaskCompletionSource<ArrayList<DatabaseObject>>();
        DatabaseReference reference = db.getReference(objectType);
        reference.addListenerForSingleValueEvent(buildListener(resultsLocation));
        return resultsLocation.getTask();
    }

    private ValueEventListener buildListener(final TaskCompletionSource<ArrayList<DatabaseObject>> task){
        ValueEventListener newListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot table) {
                ArrayList<DatabaseObject> objects = new ArrayList<>();
                String objectType = table.getKey();
                switch (objectType){
                    case "user":
                        buildUsers(table);
                        break;
                    case "horse":
                        buildHorses(table);
                        break;
                    default:
                        return;
                }
                task.setResult(objects);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        return newListener;
    }






    private ArrayList<DatabaseObject> buildUsers(DataSnapshot table){
        ArrayList<DatabaseObject> users = new ArrayList<>();
        for (DataSnapshot userSnapshot: table.getChildren()){
            User newUser = userSnapshot.getValue(User.class);
            users.add(newUser);
        }
        return users;
    }
    private ArrayList<DatabaseObject> buildHorses(DataSnapshot table){
        ArrayList<DatabaseObject> horses = new ArrayList<>();
        for (DataSnapshot horseSnapshot: table.getChildren()){
            Horse newHorse = horseSnapshot.getValue(Horse.class);
            horses.add(newHorse);
        }
        return horses;
    }




}
