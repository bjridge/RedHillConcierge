package ViewControllers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import DataControllers.DatabaseController;
import DataControllers.DatabaseObject;
import DataControllers.Horse;
import DataControllers.User;

public class HorseData extends AppCompatActivity{
    TextView lblOwnerName;
    DatabaseController controller=new DatabaseController();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.horse_layout);
        initializeEditableFields();
    }

    private void initializeEditableFields() {
        lblOwnerName = (TextView) findViewById(R.id.lblOwnerName);
        Task<ArrayList<DatabaseObject>> getAllHorse = controller.getAll("horse");

        getAllHorse.addOnCompleteListener(new OnCompleteListener<ArrayList<DatabaseObject>>() {
            @Override
            public void onComplete(@NonNull Task<ArrayList<DatabaseObject>> task) {
                ArrayList<DatabaseObject> objects = task.getResult();
                lblOwnerName.setText("testing");
                Horse horse = (Horse) objects.get(1);
            }
        });
    }
}
