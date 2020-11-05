package com.example.s333957s331153mappe3;

public class Rom {
    public int RomID;
    public int HusID;
    public int etasjeNr;
    public int romNr;
    public int kapasitet;
    public String beskrivelse;

    public Rom(int romID, int husID, int etasjeNr, int romNr, int kapasitet, String beskrivelse) {
        this.RomID = romID;
        this.HusID = husID;
        this.etasjeNr = etasjeNr;
        this.romNr = romNr;
        this.kapasitet = kapasitet;
        this.beskrivelse = beskrivelse;
    }

    public int getRomID() {
        return RomID;
    }

    public int getHusID(){return HusID;}

    public int getEtasjeNr() {
        return etasjeNr;
    }

    public int getRomNr() {
        return romNr;
    }

    public int getKapasitet() {
        return kapasitet;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }
}
