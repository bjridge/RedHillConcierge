package DataControllers;


import android.provider.ContactsContract;

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
                object.setKey(value + 1);
                writeToDatabase(object);
                incrementID(object);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
    private void writeToDatabase(DatabaseObject object){
        String objectType = getObjectType(object);
        int objectID =  object.key();
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
        reference.setValue(object.key());
    }




    //gets all of any object one time
    public Task<ArrayList<DatabaseObject>> getAll(final String objectType){
        DatabaseReference reference = db.getReference(objectType);
        final TaskCompletionSource<ArrayList<DatabaseObject>> output = new TaskCompletionSource<>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot table) {
                switch(objectType){
                    case "user":
                        output.setResult(buildUsers(table));
                        break;
                    case "horse":
                        output.setResult(buildHorses(table));
                        break;
                    default:
                        System.out.println("non type found");
                        break;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return output.getTask();
    }



    private ArrayList<DatabaseObject> buildUsers(DataSnapshot table){
        ArrayList<DatabaseObject> newObjects = new ArrayList<DatabaseObject>();

        for (DataSnapshot child: table.getChildren()){
            User newUser = child.getValue(User.class);
            newUser.setKey(Integer.parseInt(child.getKey()));
            newObjects.add(newUser);
        }
        return newObjects;
    }
    private ArrayList<DatabaseObject> buildHorses(DataSnapshot table){
        ArrayList<DatabaseObject> horses = new ArrayList<>();
        for (DataSnapshot horseSnapshot: table.getChildren()){
            Horse newHorse = horseSnapshot.getValue(Horse.class);
            newHorse.setKey(Integer.parseInt(horseSnapshot.getKey()));
            horses.add(newHorse);
        }
        return horses;
    }
}
