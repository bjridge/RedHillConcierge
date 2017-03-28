package DataControllers;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

import DataControllers.Horse;
import android.widget.TextView;

import com.ballstateuniversity.computerscience.redhillconcierge.redhillconcierge.R;

public class HorseAdapter extends ArrayAdapter {

    Context context;
    int layoutResourceId;
    Horse[] horses;

    public HorseAdapter(Context context, int layoutResourceId, Horse[] horses) {
        super(context, layoutResourceId, horses);

        this.layoutResourceId = layoutResourceId;
        this.horses = horses;
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = convertView;
        HorseHolder holder = null;
        if (row == null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new HorseHolder();
            holder.nameInput = (TextView) row.findViewById(R.id.horse_name);
            holder.stallInput = (TextView) row.findViewById(R.id.horse_stall);
            holder.descriptionInput = (TextView) row.findViewById(R.id.horse_description);
            holder.sexInput = (TextView) row.findViewById(R.id.horse_sex);

            row.setTag(holder);
        }else{
            holder = (HorseHolder) row.getTag();
        }

        Horse horse = horses[position];
        holder.nameInput.setText(horse.getName());
        holder.stallInput.setText("Stall" + horse.getStallNumber());
        holder.descriptionInput.setText(horse.getColor() + " " + horse.getBreed());
        holder.sexInput.setText(horse.getSex());

        return row;
    }
    static class HorseHolder{
        TextView nameInput;
        TextView stallInput;
        TextView descriptionInput;
        TextView sexInput;
    }


}
