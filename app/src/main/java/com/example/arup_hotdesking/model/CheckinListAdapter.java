package com.example.arup_hotdesking.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.arup_hotdesking.R;

import java.util.ArrayList;

class ViewHolder{
    TextView attempt;
    TextView datetime;
    TextView seatname;
    TextView user;
}

public class CheckinListAdapter extends ArrayAdapter<CheckinRecords> {

    private Context mcontext;
    private int mresource;
    private int lastPosition=-1;

    public CheckinListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<CheckinRecords> objects) {
        super(context, resource, objects);
        this.mresource= resource;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String Attempt = getItem(position).getAttempt();
        String DateTime = getItem(position).getDateTime();
        String SeatName = getItem(position).getSeatName();
        String User = getItem(position).getUser();

        CheckinRecords checkinRecords = new CheckinRecords(Attempt, DateTime, SeatName, User);
        final View result;
        ViewHolder holder;

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(mcontext);
            convertView = inflater.inflate(mresource, parent, false);
            holder = new ViewHolder();
            result = convertView;

            holder.attempt = (TextView) convertView.findViewById(R.id.tv_attempt);
            holder.datetime = (TextView) convertView.findViewById(R.id.tv_date);
            holder.seatname = (TextView) convertView.findViewById(R.id.tv_seatname);
            holder.user = (TextView) convertView.findViewById(R.id.tv_email);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
            result= convertView;
        }
            Animation animation = AnimationUtils.loadAnimation(mcontext, (position > lastPosition) ? R.anim.load_down_anim : R.anim.load_up_anim);
            result.startAnimation(animation);
            lastPosition = position;

            holder.attempt.setText(checkinRecords.getAttempt());
            holder.datetime.setText(checkinRecords.getDateTime());
            holder.seatname.setText(checkinRecords.getSeatName());
            holder.user.setText(checkinRecords.getUser());

            return convertView;
    }
}
