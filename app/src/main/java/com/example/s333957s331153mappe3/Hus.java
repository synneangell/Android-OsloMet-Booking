package com.example.s333957s331153mappe3;

public class Hus {
    public int HusID;
    public String Navn;
    public String beskrivelse;
    public String gateAdresse;
    public String Koordinater;
    public int Etasjer;

    public Hus(int HudID, String Navn, String beskrivelse, String gateAdresse, String gpsKoordinater, int Etasjer){
        this.HusID = HudID;
        this.Navn = Navn;
        this.beskrivelse = beskrivelse;
        this.gateAdresse = gateAdresse;
        this.Etasjer = Etasjer;
        this.Koordinater = gpsKoordinater;
    }

    public int getHusID(){
        return HusID;
    }

    public String getNavn(){
        return Navn;
    }

    public String getBeskrivelse(){
        return beskrivelse;
    }

    public String getGateAdresse(){
        return gateAdresse;
    }

    public String getKoordinater(){
        return Koordinater;
    }

    public int getEtasjer(){
        return Etasjer;
    }
}
