package com.example.s333957s331153mappe3;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

class AlleAsyncTask extends AsyncTask<String, Void,String> {
    JSONObject jsonObject;
    List<Hus> alleHus = new ArrayList<>();
    List<Rom> alleRom = new ArrayList<>();
    List<Reservasjon> alleReservasjoner = new ArrayList<>();

    @Override
    protected String doInBackground(String... urls) {
        String retur = "";
        String s = "";
        String output = "";
        for (String url : urls) {

            try {
                URL urlen = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection)
                        urlen.openConnection(); conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
                }
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                System.out.println("Output from Server .... \n");
                while ((s = br.readLine()) != null) {
                    output = output + s;
                }
                conn.disconnect();

                try {
                    JSONArray mat = new JSONArray(output);
                    if (urls[0].equals("http://student.cs.hioa.no/~s331153/husjsonout.php")) {
                        for (int i = 0; i < mat.length(); i++) {
                            JSONObject jsonobject = mat.getJSONObject(i);
                            int husID = jsonobject.getInt("HusID");
                            String navn = jsonobject.getString("Navn");
                            String beskrivelse = jsonobject.getString("Beskrivelse");
                            String gateadresse = jsonobject.getString("Gateadresse");
                            Double latitude = jsonobject.getDouble("Latitude");
                            Double longitude = jsonobject.getDouble("Longitude");
                            int etasjer = jsonobject.getInt("Etasjer");
                            retur = retur + navn + "\n";
                            Hus etHus = new Hus(navn, beskrivelse, gateadresse, latitude, longitude, etasjer);
                            alleHus.add(etHus);
                        }
                    }
                    else if (urls[0].equals("http://student.cs.hioa.no/~s331153/romjsonout.php")) {
                            for (int i = 0; i < mat.length(); i++) {
                                JSONObject jsonobject = mat.getJSONObject(i);
                                int husID = jsonobject.getInt("HusID");
                                int etasje = jsonobject.getInt("Etasje");
                                int romNr = jsonobject.getInt("RomNr");
                                int kapasitet = jsonobject.getInt("Kapasitet");
                                String beskrivelse = jsonobject.getString("Beskrivelse");
                                retur = retur + romNr + "\n";
                                Rom etRom = new Rom(husID, etasje, romNr, kapasitet, beskrivelse);
                                alleRom.add(etRom);
                            }
                    }
                    else if (urls[0].equals("http://student.cs.hioa.no/~s331153/reservasjonjsonout.php")) {
                        for (int i = 0; i < mat.length(); i++) {
                            JSONObject jsonobject = mat.getJSONObject(i);
                            //Kode for å legge reservasjoner i liste
                        }
                    }
                    return retur;
                } catch (JSONException e) {
                    e.printStackTrace(); }
                return retur;
            } catch (Exception e) {
                return "Noe gikk feil"; }
        }
        return retur;
    }
    @Override
    protected void onPostExecute(String ss) {
        //txtKoordinater.setText(ss);
        //Må her legge husene inn i kartet?
    }

    public List<Hus> getAlleHus() {
        return alleHus;
    }

    public List<Rom> getAlleRom() {
        return alleRom;
    }

    public List<Reservasjon> getAlleReservasjoner() {
        return alleReservasjoner;
    }


}
