package com.example.arup_hotdesking.model;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arup_hotdesking.R;
import com.example.arup_hotdesking.controller.MainActivity;
import com.haibin.calendarview.Calendar;

import java.util.ArrayList;
import java.util.List;

public class MyBookingAdapter extends RecyclerView.Adapter<MyBookingAdapter.MyViewHolder>{

    List<BookingRecord> bookingRecords = new ArrayList<>();
    UserViewModel userViewModel;
    int recordIndex = 0;
    int bookDateIndex = 0;

    public void setBookingRecords(List<BookingRecord> bookingRecords, UserViewModel userViewModel) {
        this.bookingRecords = bookingRecords;
        this.userViewModel = userViewModel;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View itemView = layoutInflater.inflate(R.layout.cell_user_booking, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        BookingRecord bookingRecord = bookingRecords.get(recordIndex);
        Calendar bookDate = bookingRecord.getBookingRange().get(bookDateIndex);

        StringBuilder bookdaysToString = new StringBuilder();

            bookdaysToString.append(bookDate.getDay()).append("/")
                            .append(bookDate.getMonth()).append("/")
                            .append(bookDate.getYear());

        holder.number.setText(bookDateIndex==0?String.valueOf(recordIndex):null);
        holder.deskNo.setText(bookingRecord.getDeskTitle());
        holder.myBooking.setText(bookdaysToString);
        holder.deleteBtn.setOnClickListener(new DeleteClickListener(bookingRecord,bookDate));

        if(bookDateIndex < bookingRecord.getBookingRange().size()-1) bookDateIndex++;
        else if(recordIndex < bookingRecords.size()-1){
            bookDateIndex=0;
            recordIndex++;
        }
    }


    @Override
    public int getItemCount() {
        int itemCount = 0;
        for(int i=0;i<bookingRecords.size();i++){
            List<Calendar> bookDates = bookingRecords.get(i).getBookingRange();
            for(int j=0;j<bookDates.size();j++){
                itemCount++;
            }
        }
        return itemCount;
    }

    private class DeleteClickListener implements View.OnClickListener{
        private BookingRecord bookingRecord;
        private Calendar bookDate;

        public DeleteClickListener(BookingRecord bookingRecord,Calendar bookDate){
            this.bookingRecord = bookingRecord;
            this.bookDate = bookDate;
        }

        @Override
        public void onClick(View view) {
            userViewModel.deleteBooking(bookingRecord,bookDate);
        }
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView number, myBooking, deskNo;
        Button deleteBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.textView9);
            myBooking = itemView.findViewById(R.id.textView10);
            deskNo = itemView.findViewById(R.id.textView12);
            deleteBtn = itemView.findViewById(R.id.btn_delete_booking);
        }
    }
}
