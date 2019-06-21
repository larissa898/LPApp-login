package com.example.larisa.leavingpermissionapp.Model;

public class User {
    private String email;
    private String parola;
    private String userNume;
    private String úserPrenume;
    private String functie;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User(String email, String userNume, String úserPrenume, String functie, String parola) {
        this.email = email;
        this.userNume = userNume;
        this.úserPrenume = úserPrenume;
        this.functie = functie;
        this.parola = parola;
    }


    public User() {
    }

    public static void logOut() {
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
