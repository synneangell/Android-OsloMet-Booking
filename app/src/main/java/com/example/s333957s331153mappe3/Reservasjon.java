package com.example.s333957s331153mappe3;
import java.util.Date;

public class Reservasjon {
    public int reservasjonsID;
    public int romID;
    public int husID;
    public String navn;
    public String dato;
    public String tid;

    public Reservasjon(int reservasjonsID, int husID, int romID, String navn, String dato, String tid) {
        this.reservasjonsID = reservasjonsID;
        this.romID = romID;
        this.husID = husID;
        this.navn = navn;
        this.dato = dato;
        this.tid = tid;
    }

    public Reservasjon(){}

    public int getReservasjonsID() {
        return reservasjonsID;
    }

    public void setReservasjonsID(int reservasjonsID) {
        this.reservasjonsID = reservasjonsID;
    }

    public int getRomID() {
        return romID;
    }

    public void setRomID(int romID) {
        this.romID = romID;
    }

    public int getHusID() {
        return husID;
    }

    public void setHusID(int husID) {
        this.husID = husID;
    }

    public String getNavn() {
        return navn;
    }

    public void setNavn(String navn) {
        this.navn = navn;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }
}
