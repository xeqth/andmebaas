package com.example.user.andmebaas;



public class UserProfileData {
    public String eesNimi;
    public String pereNimi;
    public String epost;

    public UserProfileData(String eesNimi, String pereNimi, String epost) {
        this.eesNimi = eesNimi;
        this.pereNimi = pereNimi;
        this.epost = epost;
    }

    public UserProfileData(){

    }
    public String getEesNimi(){
        return eesNimi;
    }
    public void getEesNimi(String eesNimi){
        this.eesNimi = eesNimi;
    }
    public String getPereNimi(){
        return pereNimi;
    }
    public void getPereNimi(String pereNimi){
        this.pereNimi = pereNimi;
    }
    public String getEpost(){
        return epost;
    }
    public void getEpost(String epost){
        this.epost = epost;

    }


}