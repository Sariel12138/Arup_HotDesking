package com.example.arup_hotdesking.model;

import android.util.Log;

import com.haibin.calendarview.Calendar;

public class BookingRecord {
    private int id;
    private String from_date;
    private String to_date;
    private String email;

    public BookingRecord(){};

    public BookingRecord(int id, String from_date, String to_date, String email) {
        this.id = id;
        this.from_date = from_date;
        this.to_date = to_date;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getFrom_date() {
        return from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public String getEmail() {
        return email;
    }

    public Calendar getFrom_dateCalendar(){
        Calendar calendar = new Calendar();
        String[] s = from_date.split("/");
        int day = Integer.parseInt(s[0]);
        int month = Integer.parseInt(s[1]);
        int year = Integer.parseInt(s[2]);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setYear(year);

        return calendar;
    }

    public Calendar getTo_dateCalendar(){
        Calendar calendar = new Calendar();
        String[] s = to_date.split("/");
        int day = Integer.parseInt(s[0]);
        int month = Integer.parseInt(s[1]);
        int year = Integer.parseInt(s[2]);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setYear(year);

        return calendar;
    }
}
