package com.example.arup_hotdesking.controller;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

class ViewHolderB{
    TextView email;
    TextView deskTitle;
    TextView collect;
}
public class BookinListAdapter extends ArrayAdapter<BookinRecords> {

    private Context mcontext;
    private  int mresource;
    private int lastPosition=-1;

    public BookinListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<BookinRecords> objects) {
        super(context, resource, objects);
        this.mresource= resource;
        this.mcontext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String email = getItem(position).getEmail();
        String deskTitle = getItem(position).getDeskTitle();
        String bookingRange = getItem(position).getBookingRange();

        BookinRecords bookinRecords = new BookinRecords(email, deskTitle, bookingRange);
        final View result;
        ViewHolderB holder;

        if (convertView == null) {

            LayoutInflater inflater = LayoutInflater.from(mcontext);
            convertView = inflater.inflate(mresource, parent, false);
            holder = new ViewHolderB();
            result = convertView;

            holder.email = (TextView) convertView.findViewById(R.id.tv_email);
            holder.deskTitle = (TextView) convertView.findViewById(R.id.tv_desk);
            holder.collect = (TextView) convertView.findViewById(R.id.tv_collect);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolderB) convertView.getTag();
            result= convertView;
        }
        Animation animation = AnimationUtils.loadAnimation(mcontext, (position > lastPosition) ? R.anim.book_loaddown_anim : R.anim.book_loadup_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.email.setText(bookinRecords.getEmail());
        holder.deskTitle.setText(bookinRecords.getDeskTitle());
        holder.collect.setText(bookinRecords.getBookingRange());

        return convertView;
    }
}
