package com.example.arup_hotdesking.model;

import android.util.Log;

public class BookingRecord {
    private int id;
    private String from_date;
    private String to_date;
    private String email;

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
}
