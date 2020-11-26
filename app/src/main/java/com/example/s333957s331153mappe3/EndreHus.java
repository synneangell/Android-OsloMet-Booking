package com.example.s333957s331153mappe3;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.model.LatLng;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class EndreHus extends AppCompatActivity {
    EditText navn, beskrivelse;
    Toolbar tb;
    String innNavn, innBeskrivelse;
    int husID;

    public static final Pattern NAVN = Pattern.compile("[a-zæøåA-ZÆØÅ0-9 ]{2,20}");
    public static final Pattern BESKRIVELSE = Pattern.compile("[a-zæøåA-ZÆØÅ0-9 ]{2,40}");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_endrehus);
        navn = findViewById(R.id.navnBygning);
        beskrivelse = findViewById(R.id.beskrivelseBygning);

        tb = findViewById(R.id.toolbarHus);
        tb.setTitle("\tEndre bygg");
        tb.setLogo(R.mipmap.ic_launcher_round);
        tb.inflateMenu(R.menu.manu_rom);
        setActionBar(tb);
        tb.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EndreHus.this, HusOversikt.class);
                startActivity(i);
            }
        });

        Intent i = getIntent();
        husID = i.getIntExtra("husID", 0);
        innNavn = i.getStringExtra("navn");
        innBeskrivelse = i.getStringExtra("beskrivelse");

        navn.setText(innNavn);
        beskrivelse.setText(innBeskrivelse);

    }

    public void lagre (View v){
      EndreHusJSON task = new EndreHusJSON();
        if(!validerNavn() | !validerBeskrivelse()) {
            Toast.makeText(EndreHus.this, "Alle felt må være riktig fylt inn riktig", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String urlString = ("http://student.cs.hioa.no/~s331153/endrehusjson.php/?" +
                    "HusID=" + husID +
                    "&Navn=" + navn.getText().toString() +
                    "&Beskrivelse=" + beskrivelse.getText().toString())
                    .replaceAll(" ", "%20");
            task.execute(urlString);
            Toast.makeText(this, "Bygning endret!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }
    }

    public boolean validerNavn(){
        String navnInput = navn.getText().toString().trim();
        if(navnInput.isEmpty()){
            navn.setError("Navn kan ikke være tomt");
            return false;
        } else if (!NAVN.matcher(navnInput).matches()){
            navn.setError("Navnet må bestå av mellom 2 og 20 tegn");
            return false;
        } else {
            navn.setError(null);
            return true;
        }
    }

    public boolean validerBeskrivelse(){
        String beskrivelseInput = beskrivelse.getText().toString().trim();
        if(beskrivelseInput.isEmpty()){
            beskrivelse.setError("Beskrivelse kan ikke være tomt");
            return false;
        } else if (!BESKRIVELSE.matcher(beskrivelseInput).matches()){
            beskrivelse.setError("Beskrivelse må bestå av mellom 2 og 40 tegn");
            return false;
        } else {
            beskrivelse.setError(null);
            return true;
        }
    }

    private class EndreHusJSON extends AsyncTask<String, Void,String> {
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

                    retur = "Vellykket oppdatering av hus!";
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
}
