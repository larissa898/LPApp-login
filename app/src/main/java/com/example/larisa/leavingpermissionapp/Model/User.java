package com.example.larisa.leavingpermissionapp.Model;

import java.util.List;

public class User {

    private String nume;
    private String prenume;
    private String functie;

    private List<LP> lps;


    public User(String nume, String prenume, String functie) {
        this.nume = nume;
        this.prenume = prenume;
        this.functie = functie;
    }

    public User(String nume, String prenume, String functie, List<LP> lps) {

        this.nume = nume;
        this.functie = functie;
        this.prenume = prenume;
        this.lps = lps;
    }





    public User() {
    }

    public static void logOut() {
    }



    public  String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getPrenume() {
        return prenume;
    }

    public void setPrenume(String prenume) {
        this.prenume = prenume;
    }

    public String getFunctie() {
        return functie;
    }

    public void setFunctie(String functie) {
        this.functie = functie;
    }

    public List<LP> getLps() {
        return lps;
    }

    public void setLps(List<LP> lps) {
        this.lps = lps;
    }
}
