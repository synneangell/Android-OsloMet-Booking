package com.example.s333957s331153mappe3;
import android.content.Context;
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

public class RomAdministrerer extends AppCompatActivity {
    public static Context context;
    Spinner etasjer;
    EditText romNr, kapasitet, beskrivelse;
    Toolbar tb;
    Integer[] husEtasjer;
    int antallEtasjer;
    int husID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_romadministrator);
        etasjer = findViewById(R.id.spinnerEtasjer);
        romNr = findViewById(R.id.romNr);
        kapasitet = findViewById(R.id.kapasitet);
        beskrivelse = findViewById(R.id.beskrivelseRom);

        tb = findViewById(R.id.toolbarRom);
        tb.setTitle("\tRegistrer nytt rom");
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

        context = getContext();

        Intent intent = getIntent();
        husID = intent.getIntExtra("HusID", 0);
        antallEtasjer = intent.getIntExtra("Etasjer", 0);

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

    public static Context getContext(){
        return context;
    }

    public void lagreRom(View v){
        LagreRomJSON task = new LagreRomJSON();
        String urlString = ("http://student.cs.hioa.no/~s331153/romjsonin.php/?" +
                "HusID=" + husID +
                "&EtasjeNr=" +  etasjer.getSelectedItem() +
                "&RomNr=" + romNr.getText().toString() +
                "&Kapasitet=" + kapasitet.getText().toString() +
                "&Beskrivelse=" + beskrivelse.getText().toString()).replaceAll(" ", "%20");
        task.execute(urlString);
        Toast.makeText(this, "Rom opprettet!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,
                MapsActivity.class);
        startActivity(intent);
    }
}
