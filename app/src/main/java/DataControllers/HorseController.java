package DataControllers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by Bradley Ridge on 2/8/2017.
 */

public class HorseController {

    //get all horses
    public void getAllHorses(){
        FirebaseDatabase completeDatabase = FirebaseDatabase.getInstance();
        DatabaseReference horseDatabase = completeDatabase.getReference("horse");
        horseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //where to put the data

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void getAllHorses2(){

    }





}
