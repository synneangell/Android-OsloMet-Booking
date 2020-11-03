package com.example.s333957s331153mappe3;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MapsActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {

    public static final String TAG = MapsActivity.class.getSimpleName();
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private GoogleMap mMap;
    ArrayList<LatLng> al = new ArrayList<>();
    LatLng oslo = new LatLng(59.918958, 10.755287);
    LatLng p35 = new LatLng(59.920503, 10.735504);
    LatLng p52 = new LatLng(59.922588, 10.732752);
    ArrayList<String> navn = new ArrayList<>();
    Boolean floatingButtonAapnet;
    TextView leggTilBygning;
    TextView leggTilRom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        leggTilBygning = (TextView) findViewById(R.id.txtLeggTilBygning);
        leggTilRom = (TextView) findViewById(R.id.txtLeggTilRom);

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

        al.add(oslo);
        al.add(p35);
        al.add(p52);
        navn.add("Oslo");
        navn.add("Pilestredet 35");
        navn.add("Pilestredet 52");

        /**---- OPPRETTER FLOATINGBUTTONS ----**/
        final FloatingActionButton fabLeggTilBygning = findViewById(R.id.fabLeggTilBygning);
        fabLeggTilBygning.hide();
        fabLeggTilBygning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, HusAdministrerer.class));
            }
        });

        final FloatingActionButton fabLeggTilRom = findViewById(R.id.fabLeggTilRom);
        fabLeggTilRom.hide();
        fabLeggTilRom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MapsActivity.this, RomAdministrerer.class));
            }
        });

        floatingButtonAapnet = false;

        FloatingActionButton fabLeggTil = findViewById(R.id.fabLeggTil);
        fabLeggTil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(floatingButtonAapnet == false){
                    fabLeggTilBygning.show();
                    fabLeggTilRom.show();
                    leggTilBygning.setText("Legg til bygning");
                    leggTilRom.setText("Legg til rom");
                    floatingButtonAapnet = true;
                }
                else {
                    fabLeggTilBygning.hide();
                    fabLeggTilRom.hide();
                    leggTilBygning.setText("");
                    leggTilRom.setText("");
                    floatingButtonAapnet = false;
                }
            }
        });
    }

    public void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title("Jeg er her!");
        mMap.addMarker(options);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
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

        for(int i = 0; i < al.size(); i++){
            for(int j = 0; j < navn.size(); j++){
                mMap.addMarker(new MarkerOptions().position(al.get(i)).title(String.valueOf(navn.get(j))));
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLng(al.get(i)));
        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                String markerTittel = marker.getTitle();
                Intent i = new Intent(MapsActivity.this, HusAdministrerer.class);
                i.putExtra("navn", markerTittel);
                startActivity(i);
                return false;
            }
        });
    }
}


