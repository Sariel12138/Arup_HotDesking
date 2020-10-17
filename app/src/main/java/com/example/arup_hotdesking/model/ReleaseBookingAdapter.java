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

class ViewHolderC{
    TextView email;
    TextView deskTitle;
    TextView date;
}

public class ReleaseBookingAdapter extends ArrayAdapter<ReleaseBookingRecords> {

    private Context mcontext;
    private  int mresource;
    private int lastPosition=-1;


    public ReleaseBookingAdapter(@NonNull Context context, int resource, @NonNull ArrayList<ReleaseBookingRecords> objects) {
        super(context, resource, objects);
        this.mcontext = context;
        this.mresource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String email = getItem(position).getEmail();
        String deskTitle = getItem(position).getDeskTitle();
        String date = getItem(position).getDate();

        ReleaseBookingRecords releaseBookingRecords= new ReleaseBookingRecords(email, deskTitle, date);
        final View result;
        ViewHolderC holder;

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(mcontext);
            convertView = inflater.inflate(mresource, parent, false);
            holder = new ViewHolderC();
            result = convertView;

            holder.email = (TextView) convertView.findViewById(R.id.tv_emailC);
            holder.deskTitle = (TextView) convertView.findViewById(R.id.tv_deskC);
            holder.date = (TextView) convertView.findViewById(R.id.tv_dateC);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolderC) convertView.getTag();
            result= convertView;
        }
        Animation animation = AnimationUtils.loadAnimation(mcontext, (position > lastPosition) ? R.anim.book_loaddown_anim : R.anim.book_loadup_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.email.setText(releaseBookingRecords.getEmail());
        holder.deskTitle.setText(releaseBookingRecords.getDeskTitle());
        holder.date.setText(releaseBookingRecords.getDate());

        return convertView;
    }
}
