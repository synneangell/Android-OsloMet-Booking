package com.example.s333957s331153mappe3;
import android.os.Bundle;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;

public class ReservasjonAdministrerer extends AppCompatActivity {
    TextView markerText;
    Date dato;
    Spinner tid;
    TextView navn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_romadministrator);
        markerText = findViewById(R.id.marker);
        String tittel = getIntent().getStringExtra("navn");
        markerText.setText(tittel);

    }
}
