package com.example.s333957s331153mappe3;
import java.util.Date;

public class Reservasjon {
    public int ReservasjonsID;
    public String navn;
    public Date dato;
    public String klokkeslettFra;
    public String klokkeslettTil;

    public Reservasjon(int reservasjonsID, String navn, Date dato, String klokkeslettFra, String klokkeslettTil) {
        this.ReservasjonsID = reservasjonsID;
        this.navn = navn;
        this.dato = dato;
        this.klokkeslettFra = klokkeslettFra;
        this.klokkeslettTil = klokkeslettTil;
    }

    public int getReservasjonsID() {
        return ReservasjonsID;
    }

    public String getNavn() {
        return navn;
    }

    public Date getDato() {
        return dato;
    }

    public String getKlokkeslettFra() {
        return klokkeslettFra;
    }

    public String getKlokkeslettTil() {
        return klokkeslettTil;
    }
}
