package com.example.s333957s331153mappe3;

public class Hus {
    public int HusID;
    public String beskrivelse;
    public String gateAdresse;
    public String gpsKoordinater;
    public int antallEtasjer;

    public Hus (int HudID, String beskrivelse, String gateAdresse, String gpsKoordinater, int antallEtasjer){
        this.HusID = HudID;
        this.beskrivelse = beskrivelse;
        this.gateAdresse = gateAdresse;
        this.gpsKoordinater = gpsKoordinater;
        this.antallEtasjer = antallEtasjer;
    }

    public int getHusID(){
        return HusID;
    }

    public String getBeskrivelse(){
        return beskrivelse;
    }

    public String getGateAdresse(){
        return gateAdresse;
    }

    public String getGpsKoordinater(){
        return gpsKoordinater;
    }

    public int getAntallEtasjer(){
        return antallEtasjer;
    }
}
