package com.example.s333957s331153mappe3;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
import java.util.Map;

public class MapsActivity extends AppCompatActivity implements
 OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng pilestredet = new LatLng(59.923889, 10.731474);
    LatLng nyBygning;
    List<Hus> alleHus;
    String stringAlleHus;
    Toolbar tb;
    public static Context context;
    SharedPreferences sp;
    Geocoder geocoder;
    List<Address> adresser;
    private List<Marker> husMarkers = new ArrayList<>();
    Marker markerValgt;
    private boolean markerSatt;
    String navn, beskrivelse, gateadresse;
    int enHusID, etasjer;
    Double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        tb = findViewById(R.id.toolbarMaps);
        tb.setLogo(R.mipmap.ic_launcher_round);
        setActionBar(tb);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    public static Context getContext(){
        return context;
    }

    public void klar(){
        hentHus();
    }

    public void hentHus(){
        for(Hus etHus : alleHus){
            Log.d("Et hus sitt navn", etHus.navn);
            Double latitude = etHus.getLatitude();
            Double longitude = etHus.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            float zoomSize = 15.0f;
            husMarkers.add(mMap.addMarker(new MarkerOptions().position(latLng).title(etHus.getHusID() + ", "+etHus.getNavn())));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomSize));
        }
        for(Marker marker : husMarkers){
            Log.d("Marker", marker.getTitle());
        }

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                if(markerSatt == true) {
                    markerValgt.remove();
                    markerSatt = false;
                }
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);

                nyBygning = new LatLng(latLng.latitude, latLng.longitude);
                geocoder = new Geocoder(MapsActivity.this, Locale.getDefault());
                try {
                    adresser = geocoder.getFromLocation(nyBygning.latitude, nyBygning.longitude, 1);
                } catch (IOException e) {
                    Toast.makeText(MapsActivity.this, "Ikke gyldig adresse funnet", Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "Fant ikke adresse til koordinater");

                }
                if (adresser == null) {
                    Toast.makeText(MapsActivity.this, "Ikke gyldig adresse funnet", Toast.LENGTH_LONG);
                    Log.d("TAG", "Fant ikke adresse til koordinater");
                } else {
                    markerOptions.title("Ny bygning?");
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    markerValgt = mMap.addMarker(markerOptions);
                    markerSatt = true;
                }
            }

        });


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                final String markerTittel = marker.getTitle();
                if (markerTittel.equals("Ny bygning?")) {
                    Intent i = new Intent(MapsActivity.this, HusAdministrerer.class);
                    i.putExtra("koordinater", nyBygning);
                    startActivity(i);
                    return false;
                } else {
                    final Dialog dialog = new Dialog(MapsActivity.this);
                    dialog.setContentView(R.layout.dialog_maps);
                    dialog.setCancelable(true);
                    dialog.show();

                    Button seOversikt, seReservasjoner;
                    seOversikt = dialog.findViewById(R.id.btnHusOversikt);
                    seReservasjoner = dialog.findViewById(R.id.btnSeReservasjon);

                    seReservasjoner.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(MapsActivity.this, ReservasjonListe.class);
                            String[] tempArray;
                            String komma = ",";
                            tempArray = markerTittel.split(komma);
                            int husID = Integer.parseInt(tempArray[0]);
                            i.putExtra("husID", husID);
                            startActivity(i);
                            dialog.dismiss();
                        }
                    });

                    seOversikt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String[] tempArray;
                            String komma = ",";
                            tempArray = markerTittel.split(komma);
                            int husID = Integer.parseInt(tempArray[0]);
                            //sp = PreferenceManager.getDefaultSharedPreferences(MapsActivity.getContext());
                            //SharedPreferences.Editor editor = sp.edit();
                            sp = getSharedPreferences("Husoversikt", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            for(Hus etHus : alleHus){
                                if(etHus.husID == husID){
                                    enHusID = etHus.husID;
                                    navn = etHus.navn;
                                    beskrivelse = etHus.beskrivelse;
                                    gateadresse = etHus.gateAdresse;
                                    latitude = etHus.latitude;
                                    longitude = etHus.longitude;
                                    etasjer = etHus.etasjer;
                                }
                            }
                            editor.putInt("husID", enHusID);
                            editor.putString("navn", navn);
                            editor.putString("beskrivelse", beskrivelse);
                            editor.putString("gateadresse", gateadresse);
                            editor.putString("latitude", Double.toString(latitude));
                            editor.putString("longitude", Double.toString(longitude));
                            editor.putInt("etasjer", etasjer);
                            editor.commit();
                            markerValgt = marker;
                            Intent i = new Intent(MapsActivity.this, HusOversikt.class);
                            startActivity(i);
                            dialog.dismiss();
                        }
                    });
                    return false;
                }
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        HusJSON task = new HusJSON();
        task.execute(new String[]{"http://student.cs.hioa.no/~s331153/husjsonout.php"});

        Log.d("onMapReady", "Inne i onMapReady");

        mMap = googleMap;
        CameraUpdate startPosisjon = CameraUpdateFactory.newLatLngZoom(pilestredet, 15);
        mMap.animateCamera(startPosisjon);
        context = getApplicationContext();
        Log.d("Markører: ", String.valueOf(markerSatt));

    /*    Intent intent = getIntent();
        if(intent.hasExtra("navn")) {
            Hus nyttHus = new Hus();
            nyttHus.navn = intent.getStringExtra("navn");
            nyttHus.beskrivelse = intent.getStringExtra("beskrivelse");
            nyttHus.gateAdresse = intent.getStringExtra("gateadresse");
            nyttHus.latitude = intent.getDoubleExtra("latitude", 0);
            nyttHus.longitude = intent.getDoubleExtra("longitude", 0);
            nyttHus.etasjer = intent.getIntExtra("etasjer", 0);
            LatLng nyttHusLatLng = new LatLng(nyttHus.latitude, nyttHus.longitude);
            husMarkers.add(mMap.addMarker(new MarkerOptions().position(nyttHusLatLng).title(nyttHus.getHusID() + ", " + nyttHus.getNavn())));
        }*/
    }
    private class HusJSON extends AsyncTask<String, Void,String> {
        SharedPreferences sp;
        List <Hus> hus = new ArrayList<>();

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
                        Log.d("output i husjson", output);
                    }
                    conn.disconnect();

                    try {
                        JSONArray mat = new JSONArray(output);
                        for (int i = 0; i < mat.length(); i++) {
                            JSONObject jsonobject = mat.getJSONObject(i);
                            Hus etHus = new Hus();
                            etHus.husID = jsonobject.getInt("HusID");
                            etHus.navn = jsonobject.getString("Navn");
                            etHus.beskrivelse = jsonobject.getString("Beskrivelse");
                            etHus.gateAdresse = jsonobject.getString("Gateadresse");
                            etHus.latitude = jsonobject.getDouble("Latitude");
                            etHus.longitude = jsonobject.getDouble("Longitude");
                            etHus.etasjer = jsonobject.getInt("Etasjer");
                            //retur = retur + husID + ";" + navn + ";" +beskrivelse + ";" +gateadresse + ";" +latitude + ";" +longitude + ";" + etasjer +";";
                            hus.add(etHus);
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
            alleHus = hus;
            klar();
        }
    }

}

