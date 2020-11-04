package com.example.s333957s331153mappe3;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

public class RomAdministrerer extends AppCompatActivity {
    Spinner bygning, etasje;
    EditText romNr, kapasitet, beskrivelse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_husadministrator);
        bygning = (Spinner) findViewById(R.id.spinnerBygning);
        etasje = (Spinner) findViewById(R.id.spinnerEtasjer);
        romNr = (EditText) findViewById(R.id.romNr);
        kapasitet = (EditText) findViewById(R.id.kapasitet);
        beskrivelse = (EditText) findViewById(R.id.beskrivelseRom);

        getJSON task = new getJSON();
        task.execute(new String[]{"http://student.cs.hioa.no/~s333975/Romjsonout.php"});

    }
}
