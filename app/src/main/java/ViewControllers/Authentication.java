package ViewControllers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import DataControllers.DatabaseController;
import DataControllers.DatabaseObject;
import DataControllers.User;

public class Authentication extends AppCompatActivity {

    //view objects
    TextView firebaseOutput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authentication);

        setupViewObjects();

        testFirebase();

    }

    private void testFirebase(){
        DatabaseController dc = new DatabaseController();
        Task<ArrayList<DatabaseObject>> getAllUsersTask = dc.getAll("user");
        getAllUsersTask.addOnCompleteListener(new OnCompleteListener<ArrayList<DatabaseObject>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<DatabaseObject>> task) {
                ArrayList<DatabaseObject> objects = task.getResult();
                for (DatabaseObject object: objects){
                    User user = (User) object;
                    firebaseOutput.setText("last user: " + user.getFirstName());
                }
            }
        });
    }

    private void setupViewObjects(){
        firebaseOutput = (TextView) findViewById(R.id.firebaseOutput);
    }
}
