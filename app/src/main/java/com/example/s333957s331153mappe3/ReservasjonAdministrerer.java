package com.example.s333957s331153mappe3;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ReservasjonAdministrerer extends AppCompatActivity {
    EditText navn, dato, startTid, sluttTid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_romadministrator);

        navn = (EditText) findViewById(R.id.txtNavn);
        dato = (EditText) findViewById(R.id.txtDato);
        startTid = (EditText) findViewById(R.id.txtStartTid);
        sluttTid = (EditText) findViewById(R.id.txtSluttTid);
    }

    public void lagreRes (View v){
        AlleAsyncTask task = new AlleAsyncTask();
        String urlString = ("http://student.cs.hioa.no/~s331153/reservasjonjsonin.php/?" +
                "Navn=" + navn.getText().toString() +
                "&Dato=" + dato.getText().toString() +
                "&TidFra=" + startTid.getText().toString() +
                "&TidTil=" + sluttTid.getText().toString()).replaceAll(" ", "%20");
        task.execute(urlString);
        Toast.makeText(this, "Reservasjon opprettet!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MapsActivity.class);
        startActivity(intent);
    }
}
