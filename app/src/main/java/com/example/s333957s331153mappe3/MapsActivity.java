package com.example.s333957s331153mappe3;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
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

public class MapsActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {

    public static final String TAG = MapsActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private GoogleMap mMap;
    LatLng pilestredet = new LatLng(59.923889, 10.731474);
    LatLng nyBygning;
    List<Hus> alleHus;
    Geocoder geocoder;
    List<Address> adresser;
    private List<Marker> husMarkers = new ArrayList<>();
    public static Context contextOfApplication;
    SharedPreferences sp;
    String husInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        mGoogleApiClient.connect();

        HusJSON task = new HusJSON();
        task.execute(new String[]{"http://student.cs.hioa.no/~s331153/husjsonout.php"});
        contextOfApplication = this.getApplicationContext();
        sp = PreferenceManager.getDefaultSharedPreferences(getContextOfApplication());
        husInfo = sp.getString("alleHus", "Tomt");
        Log.d("I maps", husInfo);

        alleHus = new ArrayList<>();

        String[] tempArray;
        String semikolon = ";";
        tempArray = husInfo.split(semikolon);

        for (int i = 0; i < tempArray.length; i+=7){
            Hus etHus = new Hus();
            etHus.husID = Integer.parseInt(tempArray[i]);
            etHus.navn = tempArray[i+1];
            etHus.beskrivelse = tempArray[i+2];
            etHus.gateAdresse = tempArray[i+3];
            etHus.latitude = Double.parseDouble(tempArray[i+4]);
            etHus.longitude = Double.parseDouble(tempArray[i+5]);
            etHus.etasjer = Integer.parseInt(tempArray[i+6]);
            alleHus.add(etHus);
        }

        Log.d("Allehus size ",Integer.toString(alleHus.size()));

    }

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }

    public void handleNewLocation(Location location) {
        CameraUpdate startPosisjon = CameraUpdateFactory.newLatLngZoom(pilestredet, 15);
        mMap.animateCamera(startPosisjon);

        /*
        for(Hus etHus : alleHus){
            Log.d("Alle hus", etHus.navn);
            Double latitude = etHus.getLatitude();
            Double longitude = etHus.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            float zoomSize = 15.0f;
            mMap.addMarker(new MarkerOptions().position(latLng).title(etHus.getNavn()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomSize));
        }*/
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Location services connected.");

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
        }
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            return;
        }

        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            handleNewLocation(location);

        } else {
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Location services suspended. Please reconnect.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

/*        for(Hus etHus : alleHus){
            Double latitude = etHus.getLatitude();
            Double longitude = etHus.getLongitude();
            LatLng latLng = new LatLng(latitude, longitude);
            float zoomSize = 15.0f;
            mMap.addMarker(new MarkerOptions().position(latLng).title(etHus.getHusID()+","+etHus.getNavn()));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomSize));
        }*/

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
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
                if(adresser == null){
                    Toast.makeText(MapsActivity.this, "Ikke gyldig adresse funnet", Toast.LENGTH_LONG);
                    Log.d("TAG", "Fant ikke adresse til koordinater");
                }
                else {
                    markerOptions.title("Ny bygning?");
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                    mMap.addMarker(markerOptions);
                }

            }
        });


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String markerTittel = marker.getTitle();
                if(markerTittel.equals("Ny bygning?")) {
                    Intent i = new Intent(MapsActivity.this, HusAdministrerer.class);
                    i.putExtra("koordinater", nyBygning);
                    startActivity(i);
                    return false;
                }
                else {
                    //String[] tempArray;
                    //String komma = ",";
                    //tempArray = markerTittel.split(komma);
                    //int husID = Integer.parseInt(tempArray[0]);
                    //SharedPreferences.Editor editor = sp.edit();
                    //editor.putString("alleHus", husInfo);
                    //editor.putInt("husID",husID);
                    //editor.apply();
                    Intent i = new Intent(MapsActivity.this, HusOversikt.class);
                    startActivity(i);
                    return false;
                }
            }
        });
    }


}