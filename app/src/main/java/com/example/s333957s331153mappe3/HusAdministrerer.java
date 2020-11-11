package com.example.s333957s331153mappe3;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HusAdministrerer extends AppCompatActivity {
    Spinner etasjer;
    TextView koordinater, gateadresse, test;
    EditText navn, beskrivelse;
    LatLng innKoordinater;
    Geocoder geocoder;
    List<Address> adresser;
    String adresse;
    List<Hus> alleHus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_husadministrator);

        koordinater = (TextView) findViewById(R.id.koordinater);
        test = (TextView) findViewById(R.id.test);
        navn = (EditText) findViewById(R.id.navnBygning);
        beskrivelse = (EditText) findViewById(R.id.beskrivelseBygning);
        gateadresse = (TextView) findViewById(R.id.adresseBygning);
        etasjer = (Spinner) findViewById(R.id.spinnerEtasjer);

        Integer[] items = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        etasjer.setAdapter(adapter);

        innKoordinater = getIntent().getExtras().getParcelable("koordinater");
        koordinater.setText(innKoordinater.latitude+", "+innKoordinater.longitude);
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            adresser = geocoder.getFromLocation(innKoordinater.latitude, innKoordinater.longitude, 1);
        } catch (IOException e) {
            Toast.makeText(this, "Ikke gyldig adresse funnet", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "Fant ikke adresse til koordinater");

        }
        adresse = adresser.get(0).getAddressLine(0);
        Log.d("Adresse",adresse);
        gateadresse.setText(adresse);


        HusAsyncTask task = new HusAsyncTask();
        task.execute("http://student.cs.hioa.no/~s331153/husjsonout.php");

    }

    public void lagre (View v){

        AlleAsyncTask task = new AlleAsyncTask();
        //Må ha en if-setning som validerer alle feltene som sendes med her!!
        String urlString = ("http://student.cs.hioa.no/~s331153/husjsonin.php/?" +
                "Navn=" + navn.getText().toString() +
                "&Beskrivelse=" + beskrivelse.getText().toString() +
                "&Gateadresse=" + gateadresse.getText().toString() +
                "&Latitude=" + innKoordinater.latitude +
                "&Longitude=" + innKoordinater.longitude +
                "&Etasjer=" + etasjer.getSelectedItem() )
                .replaceAll(" ", "%20");
        task.execute(urlString);
        Toast.makeText(this, "Bygning opprettet!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);

    }

    private class HusAsyncTask extends AsyncTask<String, Void,String> {
        JSONObject jsonObject;
        List<Hus> alleHus = new ArrayList<>();

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
                        for (int i = 0; i < mat.length(); i++) {
                            JSONObject jsonobject = mat.getJSONObject(i);
                            int husID = jsonobject.getInt("HusID");
                            String navn = jsonobject.getString("Navn");
                            String beskrivelse = jsonobject.getString("Beskrivelse");
                            String gateadresse = jsonobject.getString("Gateadresse");
                            Double latitude = jsonobject.getDouble("Latitude");
                            Double longitude = jsonobject.getDouble("Longitude");
                            int etasjer = jsonobject.getInt("Etasjer");
                            Hus etHus = new Hus(navn, beskrivelse, gateadresse, latitude, longitude, etasjer);
                            alleHus.add(etHus);
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
            test.setText(alleHus.get(0).beskrivelse);

        }

    }

    }



