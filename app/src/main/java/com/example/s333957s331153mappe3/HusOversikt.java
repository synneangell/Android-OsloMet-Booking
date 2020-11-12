package com.example.s333957s331153mappe3;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class HusOversikt extends AppCompatActivity {
    ListView lv;
    Toolbar tb;
    Spinner etasjer;
    TextView husInfo;
    FloatingActionButton fab;
    List<Hus> alleHus;
    List<Rom> alleRom;
    public static Context contextOfApplication;
    SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_husoversikt);
        lv = findViewById(R.id.lv);
        husInfo = findViewById(R.id.husInfo);
        etasjer = findViewById(R.id.etasjer);
        tb = findViewById(R.id.toolbarHusOversikt);
        tb.setLogo(R.mipmap.ic_launcher_round);
        tb.inflateMenu(R.menu.manu_rom);
        setActionBar(tb);
        tb.setTitle("Hus");

        HusJSON task = new HusJSON();
        task.execute("http://student.cs.hioa.no/~s331153/husjsonout.php");
        sp = PreferenceManager.getDefaultSharedPreferences(MapsActivity.getContextOfApplication());

        //int husIDValgt = sp.getInt("husID", 0);
        String allHusInfo = sp.getString("alleHus","Tomt");
        String allRomInfo = sp.getString("alleRom","Tomt");
/*
        String[] tempArray;
        String semikolon = ";";
        tempArray = allHusInfo.split(semikolon);

        for (int i = 0; i < tempArray.length; i+=7){
            Hus etHus = new Hus();
            etHus.husID = Integer.parseInt(tempArray[i]);
            etHus.navn = tempArray[i+1];
            etHus.beskrivelse = tempArray[i+2];
            etHus.gateAdresse = tempArray[i+3];
            etHus.latitude = Double.parseDouble(tempArray[i+4]);
            etHus.longitude = Double.parseDouble(tempArray[i+5]);
            etHus.etasjer = Integer.parseInt(tempArray[i+6]);
            alleHus.add(etHus);
        }

        Log.d("Hus ID", Integer.toString(husIDValgt));

        String husInfoString = "HusID: "+alleHus.get(husIDValgt).husID +
        "/n Navn: "+alleHus.get(husIDValgt).navn +
        "/n Beskrivelse: "+alleHus.get(husIDValgt).beskrivelse +
        "/n Gateadresse: "+alleHus.get(husIDValgt).gateAdresse;

        husInfo.setText(husInfoString);

        Integer[] husEtasjer = new Integer[alleHus.get(husIDValgt).etasjer];
        int etasjeNr = 1;
        for(int i = 0; i < husEtasjer.length; i++){
            husEtasjer[i] = etasjeNr;
            etasjeNr++;
        }

        ArrayAdapter<Integer> etasjeAdapter = new ArrayAdapter<Integer>(HusOversikt.this, android.R.layout.simple_spinner_item, husEtasjer){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };
        etasjer.setAdapter(etasjeAdapter);*/


        String[] rom = new String[]{"Rom 1", "Rom 2", "Rom 3", "Rom 4", "Rom 5", "Rom 6", "Rom 7", "Rom 8"};
        ArrayAdapter<String> romAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, rom){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };
        lv.setAdapter(romAdapter);

        /* AlleAsyncTask romGetJSON = new AlleAsyncTask();
        romGetJSON.execute("http://student.cs.hioa.no/~s331153/romjsonout.php");
        alleRom = romGetJSON.getAlleRom();
        ArrayAdapter adapter2 = new ArrayAdapter(this, R.layout.activity_husoversikt, alleRom);
        lv.setAdapter(adapter2);*/

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HusOversikt.this, RomAdministrerer.class);
                startActivity(i);
            }
        });


       lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j){
                final Dialog dialog = new Dialog(HusOversikt.this);
                dialog.setContentView(R.layout.dialog);
                dialog.setCancelable(true);
                dialog.show();

                Button slettRom, opprettReservasjon;
                slettRom = dialog.findViewById(R.id.btnSlett);
                opprettReservasjon = dialog.findViewById(R.id.btnReservasjon);

                slettRom.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        //Her skal det komme inn kode for å slette rom
                        dialog.dismiss();
                    }
                });

                opprettReservasjon.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        //Her må det også bli sendt med valgt hus og rom
                        Intent intent = new Intent(HusOversikt.this, ReservasjonAdministrerer.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    public static Context getContextOfApplication(){
        return contextOfApplication;
    }



}

