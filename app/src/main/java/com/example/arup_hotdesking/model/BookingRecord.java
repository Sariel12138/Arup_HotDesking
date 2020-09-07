package com.example.arup_hotdesking.model;

import android.util.Log;

import com.haibin.calendarview.Calendar;

import java.util.List;

public class BookingRecord {
    private String deskID;
    private List<Calendar> bookingRange;
    private String from_dateString;
    private String to_dateString;
    private int year;
    private String email;

    public BookingRecord(){};

    public BookingRecord(String deskID, List<Calendar> bookingRange, String email) {
        this.deskID = deskID;
        this.bookingRange = bookingRange;
        this.email = email;
    }

    public List<Calendar> getBookingRange() {
        return bookingRange;
    }

    public String getDeskID() {
        return deskID;
    }

    public String getEmail() {
        return email;
    }

    public int getYear(){
        if(bookingRange==null) return 0;
        return bookingRange.get(0).getYear();
    }

    public String getFrom_DateString(){
        if(bookingRange==null) return null;
        StringBuilder from_date = new StringBuilder();
        Calendar calendar = bookingRange.get(0);
        from_date.append(calendar.getDay()).append("/")
                .append(calendar.getMonth());

        return from_date.toString();
    }

    public String getTo_DateString(){
        if(bookingRange==null) return null;
        StringBuilder from_date = new StringBuilder();
        Calendar calendar = bookingRange.get(bookingRange.size()-1);
        from_date.append(calendar.getDay()).append("/")
                .append(calendar.getMonth());

        return from_date.toString();
    }
}
