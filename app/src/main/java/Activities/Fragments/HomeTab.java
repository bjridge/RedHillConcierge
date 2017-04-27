package Activities.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import Application.MyApplication;

public class HomeTab extends Fragment {

    MyApplication application;

    TextView nameOutput;
    TextView typeOutput;
    TextView statement;

    public HomeTab() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.tab__home, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        application = (MyApplication)  getActivity().getApplication();
        nameOutput = (TextView) getView().findViewById(R.id.home_name);
        typeOutput = (TextView) getView().findViewById(R.id.home_user_type);
        statement = (TextView) getView().findViewById(R.id.statement);
        Log.v("important", "about to get application");
        nameOutput.setText(application.getUser().getName());
        typeOutput.setText(application.getUser().getType());
        String statementString = "A great horse will change your life. The truly special ones define it.. \n\n We ride to fly, to feel, to breathe, to bond, to relax, to get away, to feel strong, to love, WE RIDE TO LIVE! \n\n There" +
                "'" + "s nothing quite so special as the bond between a girl, her dog and her horse \n\n A barn is a sanctuary in an unsettled world, a sheltered place where life’s true priorities are clear.  When you take a step back, it" +
                "’" + "s not just about horse" + "’" + "s – it" +
                "’" +"s about love, life and learning \n\n Every day is a good day when you ride \n\n I work hard so my horse can have nicer stuff then me.";
        statement.setText(statementString);

    }



















}
