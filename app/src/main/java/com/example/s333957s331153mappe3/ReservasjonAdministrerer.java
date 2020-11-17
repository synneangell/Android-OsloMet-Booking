package com.example.s333957s331153mappe3;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

public class ReservasjonAdministrerer extends AppCompatActivity {
    EditText navn, dato, startTid, sluttTid;
    Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservasjon);

        navn = findViewById(R.id.txtNavn);
        dato = findViewById(R.id.txtDato);
        startTid = findViewById(R.id.txtTid);
        sluttTid = findViewById(R.id.txtTid);

        tb = findViewById(R.id.toolbarReservasjon);
        tb.setTitle("\tReserver rom");
        tb.setLogo(R.mipmap.ic_launcher_round);
        tb.inflateMenu(R.menu.manu_rom);
        setActionBar(tb);

        tb.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReservasjonAdministrerer.this, HusOversikt.class);
                startActivity(i);
            }
        });
    }

    public void lagreRes (View v){
        ReservasjonJSON task = new ReservasjonJSON();
        String urlString = ("http://student.cs.hioa.no/~s331153/reservasjonjsonin.php/?" +
                "Navn=" + navn.getText().toString() +
                "&Dato=" + dato.getText().toString() +
                "&TidFra=" + startTid.getText().toString()).replaceAll(" ", "%20");
        task.execute(urlString);
        Toast.makeText(this, "Reservasjon opprettet!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
