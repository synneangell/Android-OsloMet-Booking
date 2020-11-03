package com.example.s333957s331153mappe3;

public class Model_Rom {
    public int RomID;
    public int etasjeNr;
    public int romNr;
    public int kapasitet;
    public String beskrivelse;

    public Model_Rom(int romID, int etasjeNr, int romNr, int kapasitet, String beskrivelse) {
        this.RomID = romID;
        this.etasjeNr = etasjeNr;
        this.romNr = romNr;
        this.kapasitet = kapasitet;
        this.beskrivelse = beskrivelse;
    }

    public int getRomID() {
        return RomID;
    }

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
