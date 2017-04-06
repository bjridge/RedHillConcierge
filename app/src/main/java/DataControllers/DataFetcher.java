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


    private FirebaseDatabase db;

    public DataFetcher(){
        db = FirebaseDatabase.getInstance();
    }









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
        Class tableClass = DatabaseObject.class;
        try {
            tableClass = Class.forName("DataControllers." + className);
        } catch (ClassNotFoundException e){}
            for (DataSnapshot objectData: table.getChildren()){
                DatabaseObject newObject = (DatabaseObject) objectData.getValue(tableClass);
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

    public Task<List<List<String>>> getResources(){
        log("about to try to get resources");
        DatabaseReference ref = db.getReference("resorces");
        final TaskCompletionSource<List<List<String>>> taskOutput = new TaskCompletionSource<>();
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int resourceIndex = 0;
                List<List<String>> resourceLists = new ArrayList<List<String>>();
                log(dataSnapshot.getChildrenCount() + " objects");
                for (DataSnapshot resourceCollection: dataSnapshot.getChildren()){
                    List<String> resources = new ArrayList<String>();
                    for (DataSnapshot eachResource: resourceCollection.getChildren()){
                        String resource = (String) eachResource.getValue(String.class);
                        resources.add(resource);
                    }
                    resourceLists.add(resources);
                    resourceIndex++;
                }
                taskOutput.setResult(resourceLists);
                log("lists: " + resourceLists.size());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return taskOutput.getTask();
    }







    private void log(String message){
        Log.v("TESTING", message);
    }
}
