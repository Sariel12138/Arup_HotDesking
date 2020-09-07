package com.example.arup_hotdesking.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arup_hotdesking.R;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
    List<BookingRecord> bookingRecords = new ArrayList<>();

    public void setBookingRecords(List<BookingRecord> bookingRecords) {
        this.bookingRecords = bookingRecords;
//        this.bookingRecords.add(new BookingRecord(1,"28/08","29/08","abab"));
//        this.bookingRecords.add(new BookingRecord(2,"30/08","31/08","abab"));
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_normal,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BookingRecord record = bookingRecords.get(position);
        holder.number.setText(String.valueOf(position+1));
        holder.from_date.setText(record.getFrom_date());
        holder.to_date.setText(record.getTo_date());
    }

    @Override
    public int getItemCount() {
        return bookingRecords.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView number,from_date,to_date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.numberText);
            from_date = itemView.findViewById(R.id.from_date);
            to_date = itemView.findViewById(R.id.to_date);
        }
    }
}
