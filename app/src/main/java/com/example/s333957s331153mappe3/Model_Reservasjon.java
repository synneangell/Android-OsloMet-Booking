package com.example.s333957s331153mappe3;
import java.util.Date;

public class Model_Reservasjon {
    public int ReservasjonsID;
    public String navn;
    public String dato;
    public String TidFra;
    public String TidTil;

    public Model_Reservasjon(int reservasjonsID, String navn, String dato, String TidFra, String TidTil) {
        this.ReservasjonsID = reservasjonsID;
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
