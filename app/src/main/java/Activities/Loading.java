package Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import DataControllers.DataFetcher;
import DataControllers.DatabaseObject;
import DataControllers.User;

public class Loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab__loading);

        //start task regarding login authentication with Red Hill Concierge
        Intent i = getIntent();
        final String id = i.getStringExtra("id");
        final String[] name = i.getStringExtra("name").split(" ");
        final String pictureURL = i.getStringExtra("pictureURL");
        Log.v("IMPORTANT", "url:" + pictureURL);
        DataFetcher df = new DataFetcher();
        Task<DatabaseObject> userFetchTask = df.getObject("user", id);
        userFetchTask.addOnCompleteListener(new OnCompleteListener<DatabaseObject>() {
            @Override
            public void onComplete(@NonNull Task<DatabaseObject> task) {
                if (task.getResult() != null){
                    User user = (User) task.getResult();
                    Intent i = new Intent(getApplicationContext(), BasicUserView.class);
                    i.putExtra("user", user);
                    startActivity(i);
                }else{
                    String firstName = name[0];
                    String lastName = name[1];
                    User newUser = new User(id);
                    newUser.setFirstName(firstName);
                    newUser.setLastName(lastName);
                    newUser.setType("Basic User");
                    Intent i = new Intent(getApplicationContext(), Profile.class);
                    i.putExtra("user", newUser);
                    i.putExtra("isNewUser", true);
                    i.putExtra("pictureURL", pictureURL);
                    startActivity(i);

                    DataFetcher df = new DataFetcher();
                    df.updateObject(newUser);
                    finish();
                }
            }
        });
    }








}
