package com.example.arup_hotdesking.model;

public class ReleaseBookingRecords {

    private String email, deskTitle,date;

    public ReleaseBookingRecords(String email, String deskTitle, String date) {
        this.email = email;
        this.deskTitle = deskTitle;
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeskTitle() {
        return deskTitle;
    }

    public void setDeskTitle(String deskTitle) {
        this.deskTitle = deskTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
