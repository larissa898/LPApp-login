package com.example.larisa.leavingpermissionapp.Model;

public class User {
    private int matricol;
    private String userNume;
    private String úserPrenume;
    private String functie;
    private String parola;

    public User(int matricol, String userNume, String úserPrenume, String functie, String parola) {
        this.matricol = matricol;
        this.userNume = userNume;
        this.úserPrenume = úserPrenume;
        this.functie = functie;
        this.parola = parola;
    }


    public User() {
    }

    public static void logOut() {
    }

    public int getMatricol() {
        return matricol;
    }

    public void setMatricol(int matricol) {
        this.matricol = matricol;
    }

    public  String getUserNume() {
        return userNume;
    }

    public void setUserNume(String userNume) {
        this.userNume = userNume;
    }

    public String getÚserPrenume() {
        return úserPrenume;
    }

    public void setÚserPrenume(String úserPrenume) {
        this.úserPrenume = úserPrenume;
    }

    public String getFunctie() {
        return functie;
    }

    public void setFunctie(String functie) {
        this.functie = functie;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }
}
