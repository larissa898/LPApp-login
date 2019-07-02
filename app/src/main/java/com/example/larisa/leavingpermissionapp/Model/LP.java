package com.example.larisa.leavingpermissionapp.Model;

public class LP {

    private String from;
    private String to;
    private Float total;
    private String status;


    public LP( String from, String status , String to, Float total) {

        this.from = from;
        this.to = to;
        this.total = total;
        this.status = status;
    }


    public  String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public  String getTo() {
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
