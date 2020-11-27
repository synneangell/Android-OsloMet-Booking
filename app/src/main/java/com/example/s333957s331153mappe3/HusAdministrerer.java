package com.example.s333957s331153mappe3;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
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
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class HusAdministrerer extends AppCompatActivity {
    Spinner etasjer;
    TextView gateadresse;
    EditText navn, beskrivelse;
    LatLng innKoordinater;
    Toolbar tb;
    Geocoder geocoder;
    List<Address> addresses;

    public static final Pattern NAVN = Pattern.compile("[a-zæøåA-ZÆØÅ0-9 ]{2,20}");
    public static final Pattern BESKRIVELSE = Pattern.compile("[a-zæøåA-ZÆØÅ0-9 ]{2,40}");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_husadministrator);
        navn = findViewById(R.id.navnBygning);
        beskrivelse = findViewById(R.id.beskrivelseBygning);
        gateadresse = findViewById(R.id.adresseBygning);
        etasjer = findViewById(R.id.spinnerEtasjer);

        tb = findViewById(R.id.toolbarHus);
        tb.setTitle("\tOsloMet-Booking");
        tb.setLogo(R.mipmap.ic_launcher_round);
        tb.inflateMenu(R.menu.manu_rom);
        setActionBar(tb);
        tb.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HusAdministrerer.this, MapsActivity.class);
                startActivity(i);
            }
        });

        //----- Fyller spinner med valg for etasjer  -----//
        Integer[] items = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };
        etasjer.setAdapter(adapter);

        //----- Endrer koordinater til adresse -----//
        innKoordinater = getIntent().getExtras().getParcelable("koordinater");
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(innKoordinater.latitude, innKoordinater.longitude, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        gateadresse.setText(addresses.get(0).getAddressLine(0));
    }

    public void lagre (View v){
        SendJSON task = new SendJSON();
        if(!validerNavn() | !validerBeskrivelse()) {
            Toast.makeText(HusAdministrerer.this, "Alle felt må være riktig fylt inn riktig", Toast.LENGTH_SHORT).show();
            return;
        } else {
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
}
