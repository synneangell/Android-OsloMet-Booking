package com.example.s333957s331153mappe3;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

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
        tb.setTitle("\tOsloMet-Booking");
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

    public void lagre(View v) {
        SendJSON task = new SendJSON();
        if (!validerNavn() | !validerBeskrivelse()) {
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

    public boolean validerNavn() {
        String navnInput = navn.getText().toString().trim();
        if (navnInput.isEmpty()) {
            navn.setError("Navn kan ikke være tomt");
            return false;
        } else if (!NAVN.matcher(navnInput).matches()) {
            navn.setError("Navnet må bestå av mellom 2 og 20 tegn");
            return false;
        } else {
            navn.setError(null);
            return true;
        }
    }

    public boolean validerBeskrivelse() {
        String beskrivelseInput = beskrivelse.getText().toString().trim();
        if (beskrivelseInput.isEmpty()) {
            beskrivelse.setError("Beskrivelse kan ikke være tomt");
            return false;
        } else if (!BESKRIVELSE.matcher(beskrivelseInput).matches()) {
            beskrivelse.setError("Beskrivelse må bestå av mellom 2 og 40 tegn");
            return false;
        } else {
            beskrivelse.setError(null);
            return true;
        }
    }
}
