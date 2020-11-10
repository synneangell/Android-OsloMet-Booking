package com.example.s333957s331153mappe3;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HusAdministrerer extends AppCompatActivity {
    Spinner etasjer;
    TextView txtKoordinater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_husadministrator);

        txtKoordinater = (TextView) findViewById(R.id.koordinater);
        etasjer = (Spinner) findViewById(R.id.spinnerEtasjer);

        Integer[] items = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        etasjer.setAdapter(adapter);

        //LatLng koordinater = getIntent().getExtras().getParcelable("koordinater");
        //txtKoordinater.setText();

        getJSON task = new getJSON();
        task.execute(new String[]{"http://student.cs.hioa.no/~s331153/husjsonout.php"});
    }

        private class getJSON extends AsyncTask<String, Void,String> {
            JSONObject jsonObject;

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
                            JSONArray mat = new JSONArray(output); for (int i = 0; i < mat.length(); i++) {
                                JSONObject jsonobject = mat.getJSONObject(i);
                                int husID = jsonobject.getInt("HusID");
                                String beskrivelse = jsonobject.getString("Beskrivelse");
                                String gateadresse = jsonobject.getString("Gateadresse");
                                String gspKoordinater = jsonobject.getString("GspKoordinater");
                                int etasjer = jsonobject.getInt("Etasjer");
                                retur = retur + husID + "\n";
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
                txtKoordinater.setText(ss);
            }
        }
    }
