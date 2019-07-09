package com.example.larisa.leavingpermissionapp.Model;

import java.io.Serializable;
import java.util.Date;

public class LP implements Serializable {

    public String name;
    public String from;
    public String to;
    public Float total;
    public String status;


    public LP( String from, String to, Float total, String status) {

        this.from = from;
        this.to = to;
        this.total = total;
        this.status = status;
    }


    public LP(String name, String from, String to, Float total, String status) {
        this.name = name;
        this.from = from;
        this.to = to;
        this.total = total;
        this.status = status;
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


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
