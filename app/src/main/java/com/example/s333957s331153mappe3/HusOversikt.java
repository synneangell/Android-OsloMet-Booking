package com.example.s333957s331153mappe3;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HusOversikt extends AppCompatActivity {
    ListView lv;
    Toolbar tb;
    Spinner etasjer;
    TextView husInfo;
    FloatingActionButton fab;
    List<Rom> alleRom;
    List<Hus> alleHus = new ArrayList<>();

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

        Integer[] items = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };
        etasjer.setAdapter(adapter);

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

        /*        AlleAsyncTask romGetJSON = new AlleAsyncTask();
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

                Button slettRom, opprettReservasjon, avbryt;
                slettRom = dialog.findViewById(R.id.btnRom);
                opprettReservasjon = dialog.findViewById(R.id.btnReservasjon);
                //avbryt = findViewById(R.id.avbryt);

                opprettReservasjon.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        //Her må det også bli sendt med valgt hus og rom
                        dialog.dismiss();
                    }
                });

                slettRom.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        //Her skal det komme inn kode for å slette rom
                        Intent intent = new Intent(HusOversikt.this, ReservasjonAdministrerer.class);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
            }
        });

        AlleAsyncTask task = new AlleAsyncTask();
        task.execute("http://student.cs.hioa.no/~s331153/husjsonout.php");
        alleHus = task.getAlleHus();
        husInfo.setText("Antall registrerte hus er : "+Integer.toString(alleHus.size()));
    }

/*    @Override
    protected String doInBackground(String... urls) {
        String retur = "";
        String s = "";
        String output = "";
        for (String url : urls) {
            try {
                URL urlen = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection)
                        urlen.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Accept", "application/json");
                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP error code : + conn.getResponseCode()");
                }
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                System.out.println("Output from Server .... \n");
                while ((s = br.readLine()) != null) {
                    output = output + s;
                }
                conn.disconnect();
                try {
                    JSONArray mat = new JSONArray(output);
                    for (int i = 0; i < mat.length(); i++) {
                        JSONObject jsonobject = mat.getJSONObject(i);
                        String Beskrivelse = jsonobject.getString("Beskrivelse");
                        retur = retur + Beskrivelse + "\n";
                    }
                    return retur;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return retur;
            } catch (Exception e) {
                return "Noe gikk feil";
            }
        }
        return retur;
    }*/

}

