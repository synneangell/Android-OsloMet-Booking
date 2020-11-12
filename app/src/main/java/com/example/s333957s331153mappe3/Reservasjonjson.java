package com.example.s333957s331153mappe3;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Reservasjonjson extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getJSON task = new getJSON();
        task.execute(new String[]{"http://student.cs.hioa.no/~s331153/reservasjonjsonout.php"});
    }

    private class getJSON extends AsyncTask<String, Void,String> {
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
                            int reservasjonID = jsonobject.getInt("ReservasjonID");
                            int romID = jsonobject.getInt("RomID");
                            int husID = jsonobject.getInt("HusID");
                            String navn = jsonobject.getString("Navn");
                            String dato = jsonobject.getString("Dato");
                            String tidFra = jsonobject.getString("TidFra");
                            String tidTil = jsonobject.getString("TidTil");
                            Reservasjon enReservasjon = new Reservasjon(reservasjonID, romID, husID, navn, dato, tidFra, tidTil);
                            alleReservasjoner.add(enReservasjon);
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
    }
}
