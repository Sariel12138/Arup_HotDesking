package com.example.arup_hotdesking.controller;

import java.util.ArrayList;

public class BookinRecords {

    private String email, deskTitle,bookingRange;

    public BookinRecords() {
    }

    public BookinRecords(String email, String deskTitle, String bookingRange) {
        this.email = email;
        this.deskTitle = deskTitle;
        this.bookingRange = bookingRange;
    }
    public BookinRecords(String email, String deskTitle) {
        this.email = email;
        this.deskTitle = deskTitle;
    }

    public String getDeskTitle() {
        return deskTitle;
    }

    public void setDeskTitle(String deskTitle) {
        this.deskTitle = deskTitle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getBookingRange() {
        return bookingRange;
    }

    public void setBookingRange(String bookingRange) {
        this.bookingRange = bookingRange;
    }
}
