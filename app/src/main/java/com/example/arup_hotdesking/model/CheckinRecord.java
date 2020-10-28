package com.example.arup_hotdesking.model;

import java.sql.Time;

public class CheckinRecord {

    private String Attempt, DateTime, User, SeatName;

    public CheckinRecord(){

    }

    public CheckinRecord(String attempt, String dateTime, String user, String seatName){
        this.Attempt= attempt;
        this.DateTime=dateTime;
        this.User= user;
        this.SeatName= seatName;
    }

    public String getAttempt() {
        return Attempt;
    }

    public void setAttempt(String attempt) {
        Attempt = attempt;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }

    public String getSeatName() {
        return SeatName;
    }

    public void setSeatName(String seatName) {
        SeatName = seatName;
    }
}
