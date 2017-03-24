package Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

public class AdministratorView extends AppCompatActivity{

    Toolbar toolbar;
    TextView toolbarTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__administrator);

        toolbar = (Toolbar) findViewById(R.id.administrator_toolbar);
        toolbarTitle = (TextView) findViewById(R.id.administrator_toolbar_title);

        toolbarTitle.setText("Administrative View");

    }
}
