package com.example.arup_hotdesking.model;

import android.util.Log;

import com.haibin.calendarview.Calendar;

import java.util.List;

public class BookingRecord {
    private String deskID;
    private List<Calendar> bookingRange;
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
        return getFrom_dateCalendar().getYear();
    }

    public String getFrom_DateString(){
        if(bookingRange==null) return null;
        StringBuilder from_date = new StringBuilder();
        Calendar calendar = getFrom_dateCalendar();
        from_date.append(calendar.getDay()).append("/")
                .append(calendar.getMonth());

        return from_date.toString();
    }

    public String getTo_DateString(){
        if(bookingRange==null) return null;
        StringBuilder from_date = new StringBuilder();
        Calendar calendar = getTo_dateCalendar();
        from_date.append(calendar.getDay()).append("/")
                .append(calendar.getMonth());

        return from_date.toString();
    }

    public Calendar getFrom_dateCalendar(){
        return bookingRange!=null?bookingRange.get(0):null;
    }

    public Calendar getTo_dateCalendar(){
//        Calendar calendar = new Calendar();
//        String[] s = to_date.split("/");
//        int day = Integer.parseInt(s[0]);
//        int month = Integer.parseInt(s[1]);
//        int year = Integer.parseInt(s[2]);
//        calendar.setMonth(month);
//        calendar.setDay(day);
//        calendar.setYear(year);

        return bookingRange!=null?bookingRange.get(bookingRange.size()-1):null;
    }
}
