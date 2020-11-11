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

    public Reservasjon(int HusID, int RomID, String navn, String dato, String TidFra, String TidTil) {
        this.HusID = HusID;
        this.RomID = RomID;
        this.navn = navn;
        this.dato = dato;
        this.TidFra = TidFra;
        this.TidTil = TidTil;
    }

    public int getHusID() {
        return HusID;
    }
    public int RomID() {
        return RomID;
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

    public void setHusID(int husID) {
        HusID = husID;
    }

    public void setRomID(int romID) {
        RomID = romID;
    }

    public void setReservasjonsID(int reservasjonsID) {
        ReservasjonsID = reservasjonsID;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public void setTidFra(String tidFra) {
        TidFra = tidFra;
    }

    public void setTidTil(String tidTil) {
        TidTil = tidTil;
    }
}
