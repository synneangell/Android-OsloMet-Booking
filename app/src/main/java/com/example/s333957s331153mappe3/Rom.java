package com.example.s333957s331153mappe3;

public class Rom {
    public int RomID;
    public int HusID;
    public int Etasje;
    public int RomNr;
    public int Kapasitet;
    public String Beskrivelse;


    public Rom() {

    }

    public Rom(int RomID, int HusID, int Etasje, int RomNr, int Kapasitet, String Beskrivelse) {
        this.RomID = RomID;
        this.HusID = HusID;
        this.Etasje = Etasje;
        this.RomNr = RomNr;
        this.Kapasitet = Kapasitet;
        this.Beskrivelse = Beskrivelse;
    }

    public int getRomID() {
        return RomID;
    }

    public int getHusID() {
        return HusID;
    }

    public int getEtasje() {
        return Etasje;
    }

    public int getRomNr() {
        return RomNr;
    }

    public int getKapasitet() {
        return Kapasitet;
    }

    public String getBeskrivelse() {
        return Beskrivelse;
    }

    public void setRomID(int RomID) {
        this.RomID = RomID;
    }

    public void setHusID(int HusID) {
        this.HusID = HusID;
    }

    public void setEtasje(int Etasje) {
        this.Etasje = Etasje;
    }

    public void setRomNr(int RomNr) {
        this.RomNr = RomNr;
    }

    public void setKapasitet(int Kapasitet) {
        this.Kapasitet = Kapasitet;
    }

    public void setBeskrivelse(String Beskrivelse) {
        this.Beskrivelse = Beskrivelse;
    }
}
