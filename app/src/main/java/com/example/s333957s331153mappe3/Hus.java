package com.example.s333957s331153mappe3;

public class Hus {
    public int husID;
    public String navn;
    public String beskrivelse;
    public String gateAdresse;
    public String koordinater;
    public int etasjer;

    public Hus(String navn, String beskrivelse, String gateAdresse, String koordinater, int etasjer){
        this.navn = navn;
        this.beskrivelse = beskrivelse;
        this.gateAdresse = gateAdresse;
        this.etasjer = etasjer;
        this.koordinater = koordinater;
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

    public String getKoordinater(){
        return koordinater;
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

    public void setKoordinater(String koordinater) {
        this.koordinater = koordinater;
    }

    public void setEtasjer(int etasjer) {
        this.etasjer = etasjer;
    }
}
