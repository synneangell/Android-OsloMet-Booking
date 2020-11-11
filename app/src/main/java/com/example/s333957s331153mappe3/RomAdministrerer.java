package com.example.s333957s331153mappe3;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class RomAdministrerer extends AppCompatActivity {
    Spinner etasjer;
    EditText romNr, kapasitet, beskrivelse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_romadministrator);
        etasjer = (Spinner) findViewById(R.id.spinnerEtasjer);
        romNr = (EditText) findViewById(R.id.romNr);
        kapasitet = (EditText) findViewById(R.id.kapasitet);
        beskrivelse = (EditText) findViewById(R.id.beskrivelseRom);

        Integer[] items = new Integer[]{1,2,3,4,5,6,7,8,9,10};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, items);
        etasjer.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.manu_rom);
        setActionBar(toolbar);
        toolbar.setTitle("Legg til rom");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.arrow));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RomAdministrerer.this, HusAdministrerer.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.manu_rom, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.imgLagre:
                lagre();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void lagre(){
        AlleAsyncTask task = new AlleAsyncTask();
        String urlString = ("http://student.cs.hioa.no/~s331153/romjsonin.php/?" +
                "Etasje=" +  etasjer.getSelectedItem() +
                "&RomNr=" + romNr.getText().toString() +
                "&Kapasitet=" + kapasitet.getText().toString() +
                "&Beskrivelse=" + beskrivelse.getText().toString()).replaceAll(" ", "%20");
        task.execute(urlString);
        Toast.makeText(this, "Rom opprettet!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HusAdministrerer.class);
        startActivity(intent);
    }
}
