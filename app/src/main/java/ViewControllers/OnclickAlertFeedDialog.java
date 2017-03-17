package ViewControllers;

import android.app.Dialog;
import android.app.Activity;
import android.widget.Button;
import android.os.Bundle;
import android.view.Window;
import android.view.View;
import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

/**
 * Created by kellysmith on 3/17/17.
 */

public class OnclickAlertFeedDialog extends Dialog implements android.view.View.OnClickListener{
    public Activity c;
    public Dialog d;
    public Button confirm;

    public OnclickAlertFeedDialog(Activity a) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alert_feed_layout);
        confirm = (Button) findViewById(R.id.btn_confirm);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                c.finish();
                break;
            default:
                break;
        }
        dismiss();
    }
}
