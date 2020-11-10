package com.example.s333957s331153mappe3;

public class Hus {
    public int HusID;
    public String Beskrivelse;
    public String Gateadresse;
    public String GpsKoordinater;
    public int Etasjer;

    public Hus(int HudID, String Beskrivelse, String Gateadresse, String GpsKoordinater, int Etasjer){
        this.HusID = HudID;
        this.Beskrivelse = Beskrivelse;
        this.Gateadresse = Gateadresse;
        this.Etasjer = Etasjer;
        this.GpsKoordinater = GpsKoordinater;
    }

    public int getHusID(){
        return HusID;
    }
    public String getBeskrivelse(){
        return Beskrivelse;
    }
    public String getGateadresse(){
        return Gateadresse;
    }
    public String getGpsKoordinater(){
        return GpsKoordinater;
    }
    public int getEtasjer(){
        return Etasjer;
    }
}
