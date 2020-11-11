package com.example.s333957s331153mappe3;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HusOversikt extends AppCompatActivity {
    ListView lv;
    Toolbar tb;
    Spinner etasjer;
    FloatingActionButton fab;
    List<Rom> alleRom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_husoversikt);
        lv = findViewById(R.id.lv);
        etasjer = findViewById(R.id.etasjer);
        tb = findViewById(R.id.toolbar);
        tb.setLogo(R.mipmap.ic_launcher_round);
        tb.inflateMenu(R.menu.manu_rom);
        setActionBar(tb);
        tb.setTitle("Hus");

        Integer[] items = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };
        etasjer.setAdapter(adapter);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HusOversikt.this, RomAdministrerer.class);
                startActivity(i);
            }
        });

        lv.setAdapter(adapter);

/*        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j){
                int indeks = i;
                AlertDialog.Builder builder = new AlertDialog.Builder(HusOversikt.this, R.style.AlertDialogStyle);
                builder.setMessage(getResources().getString(R.string.slettKontakt));
                builder.setPositiveButton(getResources().getString(R.string.ja), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        long MId = sp.getLong("MId", 0);
                        List<Long> deltakere = db.finnMoteDeltakelse(MId);
                        long KId = deltakere.get(indeks);
                        db.slettMoteDeltakelse(MId, KId);
                        Intent intent = new Intent(Aktivitet_MoteDeltagelse.this, Aktivitet_MoteDeltagelse.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.nei), null);
                builder.show();
            }
        });*/

/*        AlleAsyncTask romGetJSON = new AlleAsyncTask();
        romGetJSON.execute("http://student.cs.hioa.no/~s331153/romjsonout.php");
        alleRom = romGetJSON.getAlleRom();
        ArrayAdapter adapter2 = new ArrayAdapter(this, R.layout.activity_husoversikt, alleRom);
        lv.setAdapter(adapter2);*/
    }
}

