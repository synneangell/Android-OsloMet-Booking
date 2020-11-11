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
import java.util.List;
import java.util.Locale;

public class HusAdministrerer extends AppCompatActivity {
    Spinner etasjer;
    TextView koordinater, gateadresse;
    EditText navn, beskrivelse;
    LatLng innKoordinater;
    Geocoder geocoder;
    List<Address> adresser;
    String adresse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_husadministrator);

        koordinater = (TextView) findViewById(R.id.koordinater);
        navn = (EditText) findViewById(R.id.navnBygning);
        beskrivelse = (EditText) findViewById(R.id.beskrivelseBygning);
        gateadresse = (TextView) findViewById(R.id.adresseBygning);
        etasjer = (Spinner) findViewById(R.id.spinnerEtasjer);

        Integer[] items = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        etasjer.setAdapter(adapter);

        innKoordinater = getIntent().getExtras().getParcelable("koordinater");
        koordinater.setText("Koordinater: "+innKoordinater.latitude+", "+innKoordinater.longitude);
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            adresser = geocoder.getFromLocation(innKoordinater.latitude, innKoordinater.latitude, 1);
        } catch (IOException e) {
            Log.d("TAG", "Fant ikke adresse til koordinater");
            e.printStackTrace();
        }
        adresse = adresser.get(0).getAddressLine(0);
        Log.d("Adresse",adresse);
        gateadresse.setText(adresse);


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


    }
