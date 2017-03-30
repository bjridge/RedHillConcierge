package DataControllers;



import android.provider.ContactsContract;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class DataFetcher {

    //test pushing from kelly's new laptop!
    //tested receiving from brad's vr laptop!
    //now testing pushing back so kelly's lender can pull!



    private FirebaseDatabase db;

    public DataFetcher(){
        db = FirebaseDatabase.getInstance();
    }








    // TODO: 3/21/2017 horse keys must be integers, but users are now string id's
//    public void addNewObject(final DatabaseObject object) {
//        String objectType = getObjectType(object);
//        DatabaseReference objectCountReference = db.getReference("counts/" + objectType);
//        objectCountReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                int value = dataSnapshot.getValue(int.class);
//                //object.setKey(value + 1);
//                writeToDatabase(object);
//                incrementCounter(object);
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {}
//        });
//    }
    private void writeToDatabase(DatabaseObject object){
        String objectType = getObjectType(object);
        String objectID =  object.key();
        DatabaseReference reference = db.getReference("objects/" + objectType + "/" + objectID);
        reference.setValue(object);
        return;
    }
    private String getObjectType(DatabaseObject object){
        String objectType = object.getClass().toString();
        String removableString = "class DataControllers.";
        String newString = objectType.replaceAll(removableString, "");
        return newString.toLowerCase();
    }
    private void incrementCounter(DatabaseObject object){
        String objectType = getObjectType(object);
        DatabaseReference reference = db.getReference("counts/" + objectType);
        reference.setValue(object.key());
    }


    //gets all of any object one time
    public Task<List<DatabaseObject>> getAll(final String objectType){
        log("getting all ");
        DatabaseReference reference = db.getReference("objects/" + objectType);
        final TaskCompletionSource<List<DatabaseObject>> output = new TaskCompletionSource<>();
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot table) {
                output.setResult(buildObjects(table));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        return output.getTask();
    }
    private List<DatabaseObject> buildObjects(DataSnapshot table){
        List<DatabaseObject> newObjects = new ArrayList<>();
        String objectType = table.getKey();
        String className = objectType.substring(0, 1).toUpperCase() + objectType.substring(1);
        log("name of class: " + className);
        Class tableClass = DatabaseObject.class;
        try {
            tableClass = Class.forName("DataControllers." + className);
            log("real class name: " + tableClass);
        } catch (ClassNotFoundException e){}
            for (DataSnapshot objectData: table.getChildren()){
                log("got a data snapshot; creating it now");
                DatabaseObject newObject = (DatabaseObject) objectData.getValue(tableClass);
                log("created object from data snapshot");
                newObject.setKey(objectData.getKey());
                newObjects.add(newObject);
            }
        return newObjects;
    }

    public void updateObject(DatabaseObject object){
        writeToDatabase(object);
    }






    public Task<DatabaseObject> getObject(final String objectType, String key){
        DatabaseReference objectReference = db.getReference("objects/" + objectType + "/" + key);
        final TaskCompletionSource<DatabaseObject> taskOutput = new TaskCompletionSource<>();
        objectReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {

                    String className = objectType.substring(0, 1).toUpperCase() + objectType.substring(1);
                    DatabaseObject object = (DatabaseObject) dataSnapshot.getValue(Class.forName("DataControllers." + className));
                    if (object != null){
                        object.setKey(dataSnapshot.getKey());
                    }else{
                        object = null;
                    }
                    taskOutput.setResult(object);


                }catch (ClassNotFoundException e){

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
        return taskOutput.getTask();
    }


    private void log(String message){
        Log.v("IMPORTANT", message);
    }
}
