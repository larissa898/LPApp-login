package com.example.larisa.leavingpermissionapp.Model;

public class User {

    private String nume;
    private String prenume;
    private String functie;




    public User( String nume, String prenume, String functie) {

        this.nume = nume;
        this.functie = functie;
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


}
