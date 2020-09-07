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
        this.from_dateString = setFromDateString();
        this.to_dateString = setToDateString();
        this.year = setYear();
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
        return year;
    }

    public String getFrom_date(){
        return from_dateString;
    }

    public String getTo_date(){
        return to_dateString;
    }

    private String setFromDateString(){
        if(bookingRange==null) return null;
        StringBuilder from_date = new StringBuilder();
        Calendar calendar = bookingRange.get(0);
        from_date.append(calendar.getDay()).append("/")
                .append(calendar.getMonth());

        return from_date.toString();
    }

    private String setToDateString(){
        if(bookingRange==null) return null;
        StringBuilder from_date = new StringBuilder();
        Calendar calendar = bookingRange.get(bookingRange.size()-1);
        from_date.append(calendar.getDay()).append("/")
                .append(calendar.getMonth());

        return from_date.toString();
    }

    private int setYear(){
        if(bookingRange==null) return 0;
        return bookingRange.get(0).getYear();
    }
}
