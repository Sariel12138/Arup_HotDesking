package com.example.arup_hotdesking.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arup_hotdesking.R;
import com.haibin.calendarview.Calendar;

import java.util.ArrayList;
import java.util.List;

public class MyBookingAdapter extends RecyclerView.Adapter<MyBookingAdapter.MyViewHolder>{

    List<BookingRecord> bookingRecords = new ArrayList<>();

    public void setBookingRecords(List<BookingRecord> bookingRecords) {
        this.bookingRecords = bookingRecords;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_user_booking, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BookingRecord bookingRecord = bookingRecords.get(position);
        List<Calendar> bookdays = bookingRecord.getBookingRange();
        StringBuilder bookdaysToString = new StringBuilder();

        for (Calendar calendar: bookdays) {
            bookdaysToString.append(calendar.getDay()).append("/")
                            .append(calendar.getMonth()).append("/")
                            .append(calendar.getYear()).append("\n");
        }

        holder.number.setText(String.valueOf(position+1));
        holder.deskNo.setText(bookingRecord.getDeskTitle());
        holder.myBooking.setText(bookdaysToString);
    }

    @Override
    public int getItemCount() {
        return bookingRecords.size();
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView number, myBooking, deskNo;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.textView9);
            myBooking = itemView.findViewById(R.id.textView10);
            deskNo = itemView.findViewById(R.id.textView12);
        }
    }
}
