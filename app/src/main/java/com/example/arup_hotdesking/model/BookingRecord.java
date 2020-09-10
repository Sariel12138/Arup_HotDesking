package com.example.arup_hotdesking.model;

import android.util.Log;

import com.haibin.calendarview.Calendar;

import java.util.List;

public class BookingRecord {
    private String deskID;
    private List<Calendar> bookingRange;
    private String email;
    private  String deskTitle;

    public BookingRecord(){};

    public BookingRecord(String deskID, List<Calendar> bookingRange, String email, String deskTitle) {
        this.deskID = deskID;
        this.bookingRange = bookingRange;
        this.email = email;
        this.deskTitle = deskTitle;
    }

    public List<Calendar> getBookingRange() {
        return bookingRange;
    }

    public String getDeskID() {
        return deskID;
    }

    public String getDeskTitle() {
        return deskTitle;
    }

    public String getEmail() {
        return email;
    }



    public int year(){
        if(bookingRange==null) return 0;
        return bookingRange.get(0).getYear();
    }

    public String from_DateString(){
        if(bookingRange==null) return null;
        StringBuilder from_date = new StringBuilder();
        Calendar calendar = bookingRange.get(0);
        from_date.append(calendar.getDay()).append("/")
                .append(calendar.getMonth());

        return from_date.toString();
    }

    public String to_DateString(){
        if(bookingRange==null) return null;
        StringBuilder from_date = new StringBuilder();
        Calendar calendar = bookingRange.get(bookingRange.size()-1);
        from_date.append(calendar.getDay()).append("/")
                .append(calendar.getMonth());

        return from_date.toString();
    }
}
