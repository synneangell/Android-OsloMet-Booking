package com.example.s333957s331153mappe3;
import java.util.Date;

public class Reservasjon {
    public int ReservasjonsID;
    public int HusID;
    public int RomID;
    public String navn;
    public String dato;
    public String TidFra;
    public String TidTil;

    public Reservasjon(int reservasjonsID, int HusID, int RomID, String navn, String dato, String TidFra, String TidTil) {
        this.ReservasjonsID = reservasjonsID;
        this.HusID = HusID;
        this.RomID = RomID;
        this.navn = navn;
        this.dato = dato;
        this.TidFra = TidFra;
        this.TidTil = TidTil;
    }

    public int getReservasjonsID() {
        return ReservasjonsID;
    }

    public String getNavn() {
        return navn;
    }

    public String getDato() {
        return dato;
    }

    public String getKlokkeslettFra() {
        return TidFra;
    }

    public String getKlokkeslettTil() {
        return TidTil;
    }
}
