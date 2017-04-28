package ListAdapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

import java.util.List;

import Model.Objects.Horse;

public class HorseListAdapter extends BaseAdapter {

    Context context;
    List<Horse> horses;
    private static LayoutInflater inflater = null;

    public HorseListAdapter(Context context, List<Horse> data) {
        Log.v("important", "horse list size: " + data.size());
        this.context = context;
        this.horses = data;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return horses.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return horses.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final Horse horse = (Horse) getItem(position);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.custom__list_item__horse_list, null);
        }
        TextView horseName = (TextView) convertView.findViewById(R.id.horse_name);
        TextView horseStall = (TextView) convertView.findViewById(R.id.horse_stall);
        TextView horseDescription = (TextView) convertView.findViewById(R.id.horse_description);

        horseName.setText(horse.getName());
        horseStall.setText("Stall " + horse.getStallNumber());
        horseDescription.setText(horse.getColor() + " " + horse.getBreed());

        return convertView;
    }
}