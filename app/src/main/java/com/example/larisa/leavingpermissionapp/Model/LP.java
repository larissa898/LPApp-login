/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Model;

import java.io.Serializable;

public class LP implements Serializable {


    private String id;
    private String from;
    private String to;
    private Float total;
    private String status;
    private String data;
    private String nume;
    private User user;

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }




    public LP(String id,String nume, String from, String to, Float total, String status) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.total = total;
        this.status = status;
        this.nume = nume;
    }


    public LP(String id,String from, String to, Float total, String status, String nume, String data) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.total = total;
        this.status = status;
        this.nume = nume;
        this.data = data;
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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LP() {
    }
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}

