package com.example.s333957s331153mappe3;

public class Hus {
    public int husID;
    public String navn;
    public String beskrivelse;
    public String gateAdresse;
    public Double latitude;
    public Double longitude;
    public int etasjer;

    public Hus(){ }

    public Hus(String navn, String beskrivelse, String gateAdresse, Double latitude, Double longitude, int etasjer){
        this.navn = navn;
        this.beskrivelse = beskrivelse;
        this.gateAdresse = gateAdresse;
        this.etasjer = etasjer;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public Hus(int husID, String navn, String beskrivelse, String gateAdresse, Double latitude, Double longitude, int etasjer){
        this.husID = husID;
        this.navn = navn;
        this.beskrivelse = beskrivelse;
        this.gateAdresse = gateAdresse;
        this.etasjer = etasjer;
        this.latitude = latitude;
        this.longitude = longitude;

    }

    public int getHusID(){
        return husID;
    }

    public String getNavn(){
        return navn;
    }

    public String getBeskrivelse(){
        return beskrivelse;
    }

    public String getGateAdresse(){
        return gateAdresse;
    }

    public Double getLatitude(){
        return latitude;
    }

    public Double getLongitude(){
        return longitude;
    }

    public int getEtasjer(){
        return etasjer;
    }

    public void setHusID(int husID) {
        this.husID = husID;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public void setGateAdresse(String gateAdresse) {
        this.gateAdresse = gateAdresse;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double latitude) {
        this.longitude = longitude;
    }

    public void setEtasjer(int etasjer) {
        this.etasjer = etasjer;
    }
}