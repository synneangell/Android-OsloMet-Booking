package com.example.s333957s331153mappe3;
import android.content.Intent;
import android.graphics.Color;
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
import java.util.regex.Pattern;

public class RomAdministrerer extends AppCompatActivity {
    Spinner etasjer;
    EditText romNr, kapasitet, beskrivelse;
    Toolbar tb;
    Integer[] husEtasjer;
    int antallEtasjer;
    int husID;

    public static final Pattern KAPASITET = Pattern.compile("[0-9]{1,3}");
    public static final Pattern ROMNR = Pattern.compile("[0-9]{1,3}");
    public static final Pattern BESKRIVELSE = Pattern.compile("[a-zæøåA-ZÆØÅ0-9 ]{2,40}");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_romadministrator);
        etasjer = findViewById(R.id.spinnerEtasjer);
        romNr = findViewById(R.id.romNr);
        kapasitet = findViewById(R.id.kapasitet);
        beskrivelse = findViewById(R.id.beskrivelseRom);

        tb = findViewById(R.id.toolbarRom);
        tb.setTitle("\tOsloMet-Booking");
        tb.setLogo(R.mipmap.ic_launcher_round);
        tb.inflateMenu(R.menu.manu_rom);
        setActionBar(tb);

        tb.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RomAdministrerer.this, HusOversikt.class);
                startActivity(i);
            }
        });

        Intent intent = getIntent();
        husID = intent.getIntExtra("HusID", 0);
        antallEtasjer = intent.getIntExtra("Etasjer", 0);

        //----- Metoder for aa fylle spinner med etasjer -----//
        husEtasjer = new Integer[antallEtasjer];
        int etasjeNr = 1;
        for (int i = 0; i < husEtasjer.length; i++) {
            husEtasjer[i] = etasjeNr;
            etasjeNr++;
        }

        ArrayAdapter<Integer> etasjeAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, husEtasjer) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };
        etasjer.setAdapter(etasjeAdapter);
    }

    public void lagreRom(View v){
        SendJSON task = new SendJSON();
        if(!validerRomnr() | !validerKapasitet() | !validerBeskrivelse()) {
            Toast.makeText(RomAdministrerer.this, "Alle felt må være riktig fylt inn riktig", Toast.LENGTH_SHORT).show();
            return;
        } else {
            String urlString = ("http://student.cs.hioa.no/~s331153/romjsonin.php/?" +
                    "HusID=" + husID +
                    "&EtasjeNr=" +  etasjer.getSelectedItem() +
                    "&RomNr=" + romNr.getText().toString() +
                    "&Kapasitet=" + kapasitet.getText().toString() +
                    "&Beskrivelse=" + beskrivelse.getText().toString()).replaceAll(" ", "%20");
            task.execute(urlString);
            Toast.makeText(this, "Rom opprettet!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,
                    HusOversikt.class);
            startActivity(intent);
        }
    }

    //----- Metoder for validering -----//
    public boolean validerRomnr(){
        String romNrInput = romNr.getText().toString().trim();
        if(romNrInput.isEmpty()){
            romNr.setError("Romnr kan ikke være tomt");
            return false;
        } else if (!ROMNR.matcher(romNrInput).matches()){
            romNr.setError("Romnr må bestå av tall mellom 1 og 999");
            return false;
        } else {
            romNr.setError(null);
            return true;
        }
    }

    public boolean validerKapasitet(){
        String kapasitetInput = kapasitet.getText().toString().trim();
        if(kapasitetInput.isEmpty()){
            kapasitet.setError("Kapasitet kan ikke være tomt");
            return false;
        } else if (!KAPASITET.matcher(kapasitetInput).matches()){
            kapasitet.setError("Kapasitet må bestå av tall mellom 1 og 999");
            return false;
        } else {
            kapasitet.setError(null);
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
