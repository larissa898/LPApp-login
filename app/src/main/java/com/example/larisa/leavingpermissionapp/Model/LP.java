package com.example.larisa.leavingpermissionapp.Model;

import java.util.Date;

public class LP {
    private String currentDate;
    private String from;
    private String to;
    private Float total;


    public LP(String currentDate, String from, String to, Float total) {
        this.currentDate = currentDate;
        this.from = from;
        this.to = to;
        this.total = total;
    }



    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }



    public LP() {
    }






}
