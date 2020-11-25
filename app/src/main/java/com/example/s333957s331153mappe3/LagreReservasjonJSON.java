package com.example.s333957s331153mappe3;

import android.os.AsyncTask;
import android.util.Log;
import java.net.HttpURLConnection;
import java.net.URL;

public class LagreReservasjonJSON extends AsyncTask<String, Void,String> {
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

                retur = "Vellykket lagring!";
                conn.disconnect();

            } catch (Exception e) {
                return "Noe gikk feil";
            }
        }
        return retur;
    }

    @Override
    protected void onPostExecute(String ss) {
        Log.d("Vellykket lagring", ss);

    }
}
