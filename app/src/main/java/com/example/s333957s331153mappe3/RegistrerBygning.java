package com.example.s333957s331153mappe3;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrerBygning extends AppCompatActivity {
    TextView markerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reghus);
        markerText = findViewById(R.id.marker);
        String tittel = getIntent().getStringExtra("navn");
        markerText.setText(tittel);

    }
}
