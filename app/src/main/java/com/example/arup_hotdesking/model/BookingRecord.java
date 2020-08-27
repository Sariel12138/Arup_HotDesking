package com.example.arup_hotdesking.model;

public class BookingRecord {
    private int id = 1;
    private String from_date = "28/08";
    private String to_date = "29/08";
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
