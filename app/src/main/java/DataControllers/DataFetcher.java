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
    public Task<DatabaseObject[]> getAll(final String objectType){
        DatabaseReference reference = db.getReference("objects/" + objectType);
        final TaskCompletionSource<DatabaseObject[]> output = new TaskCompletionSource<>();
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
    private DatabaseObject[] buildObjects(DataSnapshot table){
        int objectCount = (int) table.getChildrenCount();
        Log.v("IMPORTANT", "count: " + objectCount);

        DatabaseObject[] newObjects = new DatabaseObject[objectCount];
        String objectType = table.getKey();
        String className = objectType.substring(0, 1).toUpperCase() + objectType.substring(1);
        Class tableClass = DatabaseObject.class;
        try {
            Log.v("IMPORTANT", "about to try and make the objects");

            tableClass = Class.forName("DataControllers." + className);
        } catch (ClassNotFoundException e){}
            int childNumber = 0;
            for (DataSnapshot objectData: table.getChildren()){
                Log.v("IMPORTANT", "about to try to create aN OBJECT OF CLASS: " + tableClass);

                Horse newObject = objectData.getValue(Horse.class);
                Log.v("IMPORTANT", "created a horse object ");

                newObject.setKey(objectData.getKey());
                newObjects[childNumber] = newObject;
                childNumber++;
            }
        Log.v("IMPORTANT", "made all of the objects");

        return newObjects;
    }










    private DatabaseObject[] buildHorses(DataSnapshot table){
        int count = (int) table.getChildrenCount();
        Horse[] horses = new Horse[count];
        int i = 0;
        for (DataSnapshot horseSnapshot: table.getChildren()){
            Horse newHorse = horseSnapshot.getValue(Horse.class);
            newHorse.setKey(horseSnapshot.getKey());
            horses[i] = newHorse;
            i++;
        }
        return horses;
    }
    private ArrayList<DatabaseObject> buildContacts(DataSnapshot table){
        ArrayList<DatabaseObject> horses = new ArrayList<>();
        for (DataSnapshot snapshot: table.getChildren()){
            Contact newContact = snapshot.getValue(Contact.class);
            newContact.setKey(snapshot.getKey());
            horses.add(newContact);
        }
        return horses;
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
                    Log.v("IMPORTANT", className);
                    DatabaseObject object = (DatabaseObject) dataSnapshot.getValue(Class.forName("DataControllers." + className));
                    if (object != null){
                        object.setKey(dataSnapshot.getKey());
                        Log.v("IMPORTANT", "object is not null");

                    }else{
                        object = null;

                        Log.v("IMPORTANT", "object is null");

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
}
