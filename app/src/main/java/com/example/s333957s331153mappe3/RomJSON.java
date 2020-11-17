package com.example.s333957s331153mappe3;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class RomJSON extends AsyncTask<String, Void,String> {
    SharedPreferences sp;

        @Override
        protected String doInBackground(String... urls) {
            String retur = "";
            String s = "";
            String output = "";
            for (String url : urls) {

                try {
                    URL urlen = new URL(urls[0]);
                    HttpURLConnection conn = (HttpURLConnection)
                            urlen.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Accept", "application/json");

                    if (conn.getResponseCode() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                    }
                    BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                    System.out.println("Output from Server .... \n");
                    while ((s = br.readLine()) != null) {
                        output = output + s;
                    }
                    conn.disconnect();

                    try {
                        JSONArray mat = new JSONArray(output);
                        for (int i = 0; i < mat.length(); i++) {
                            JSONObject jsonobject = mat.getJSONObject(i);
                            int romID = jsonobject.getInt("RomID");
                            int husID = jsonobject.getInt("HusID");
                            int etasjeNr = jsonobject.getInt("EtasjeNr");
                            int romNr = jsonobject.getInt("RomNr");
                            int kapasitet = jsonobject.getInt("Kapasitet");
                            String beskrivelse = jsonobject.getString("Beskrivelse");
                            retur = retur + romID+";" + husID+";" + etasjeNr+";" + romNr+";" + kapasitet+";" + beskrivelse+";";
                        }
                        return retur;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return retur;
                } catch (Exception e) {
                    return "Noe gikk feil";
                }
            }
            return retur;
        }

    @Override
    protected void onPostExecute(String ss) {
        Context applicationContext = HusOversikt.getContextOfApplication();
        sp = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("alleRom",ss);
        editor.apply();
    }
}



