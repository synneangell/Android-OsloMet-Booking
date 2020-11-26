package com.example.s333957s331153mappe3;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
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
    ArrayAdapter<String> romAdapter;
    Button slettRom, opprettReservasjon;

    List<Rom> alleRom;
    List<String> alleRomLV = new ArrayList<>();
    List<Integer> alleRomIndeksLV;
    Integer[] husEtasjer;

    SharedPreferences sp;

    String husInfoString;
    int slettetIndeks;
    int husIDValgt;

    Hus valgtHus = new Hus();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_husoversikt);
        lv = findViewById(R.id.lv);
        husInfo = findViewById(R.id.husInfo);
        etasjer = findViewById(R.id.etasjer);
        tb = findViewById(R.id.toolbarHusOversikt);
        tb = findViewById(R.id.toolbarHusOversikt);
        tb.setLogo(R.mipmap.ic_launcher_round);
        tb.inflateMenu(R.menu.manu_rom);
        setActionBar(tb);

        tb.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(HusOversikt.this, MapsActivity.class);
                startActivity(i);
            }
        });

        //----- Henter informasjon om valgt hus -----//
        sp = getApplicationContext().getSharedPreferences("Husoversikt", Context.MODE_PRIVATE);
        valgtHus.husID = sp.getInt("husID", 0);
        valgtHus.navn = sp.getString("navn", "Tomt");
        valgtHus.beskrivelse = sp.getString("beskrivelse", "Tomt");
        valgtHus.gateAdresse = sp.getString("gateadresse", "Tomt");
        valgtHus.latitude = Double.parseDouble(sp.getString("latitude", "Tomt"));
        valgtHus.longitude = Double.parseDouble(sp.getString("longitude", "Tomt"));
        valgtHus.etasjer = sp.getInt("etasjer", 0);

        husIDValgt = valgtHus.husID;

        husInfoString = "HusID: " + valgtHus.husID +
                "\nNavn: " + valgtHus.navn +
                "\nBeskrivelse: " + valgtHus.beskrivelse +
                "\nGateadresse: " + valgtHus.gateAdresse;
        husInfo.setText(husInfoString);

        husEtasjer = new Integer[valgtHus.etasjer];
        int etasjeNr = 1;
        for (int i = 0; i < husEtasjer.length; i++) {
            husEtasjer[i] = etasjeNr;
            etasjeNr++;
        }

        RomJSON task = new RomJSON();
        task.execute(new String[]{"http://student.cs.hioa.no/~s331153/romjsonout.php"});

    }

    public void klar(){
        hentRom();
    }

    public void hentRom(){
        //----- Fyller spinner med rom fra huset  -----//
        ArrayAdapter<Integer> etasjeAdapter = new ArrayAdapter<Integer>(HusOversikt.this, android.R.layout.simple_spinner_item, husEtasjer) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };
        etasjer.setAdapter(etasjeAdapter);

        etasjer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                String valgtEtasjeSpinner = etasjer.getItemAtPosition(arg2).toString();
                romAdapter = new ArrayAdapter<String>(HusOversikt.this, android.R.layout.simple_spinner_item, visRomListView(valgtEtasjeSpinner)) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView textView = view.findViewById(android.R.id.text1);
                        textView.setTextColor(Color.BLACK);
                        return view;
                    }
                };
                lv.setAdapter(romAdapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HusOversikt.this, RomAdministrerer.class);
                i.putExtra("HusID", husIDValgt);
                i.putExtra("Etasjer", valgtHus.etasjer);
                startActivity(i);
            }
        });

        //----- Funksjon for slett rom og opprett reservasjon -----//
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j){
                final Dialog dialog = new Dialog(HusOversikt.this);
                dialog.setContentView(R.layout.dialog);
                dialog.setCancelable(true);
                dialog.show();

                slettRom = dialog.findViewById(R.id.btnSlett);
                opprettReservasjon = dialog.findViewById(R.id.btnReservasjon);
                final int indeks = i;

                slettRom.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        AlertDialog.Builder builder = new AlertDialog.Builder(HusOversikt.this, R.style.AlertDialogStyle);
                        builder.setMessage(getResources().getString(R.string.slettRom));
                        builder.setPositiveButton(getResources().getString(R.string.ja), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int romID = alleRomIndeksLV.get(indeks);
                                SlettRomJSON task = new SlettRomJSON();
                                String url = "http://student.cs.hioa.no/~s331153/slettromjson.php/?RomID=" + Integer.toString(romID);
                                task.execute(url);
                                slettetIndeks = indeks;
                                slettRom();
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton(getResources().getString(R.string.nei), null);
                        builder.show();
                        dialog.dismiss();
                    }
                });

                opprettReservasjon.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view){
                        int romID = alleRomIndeksLV.get(indeks);
                        Intent intent = new Intent(HusOversikt.this, ReservasjonAdministrerer.class);
                        intent.putExtra("romID", romID);
                        intent.putExtra("husID", husIDValgt);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    //----- Oppdaterer listview ved sletting av rom -----//
    public void slettRom(){
        alleRomLV.remove(slettetIndeks);
        alleRom.remove(slettetIndeks);
        romAdapter.notifyDataSetChanged();
    }

    //----- Funksjonalitet for sletting av hus -----//
    public void slettHus(View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(HusOversikt.this, R.style.AlertDialogStyle);
        builder.setMessage(getResources().getString(R.string.slettHus));
        builder.setPositiveButton(getResources().getString(R.string.ja), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                SlettHusJSON task = new SlettHusJSON();
                String url = "http://student.cs.hioa.no/~s331153/sletthusjson.php/?HusID=" + Integer.toString(husIDValgt);
                task.execute(url);
                Intent j = new Intent(HusOversikt.this, MapsActivity.class);
                j.putExtra("slett", husIDValgt);
                startActivity(j);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.nei), null);
        builder.show();
    }

    //----- Fyller listview med rom -----//
    public List<String> visRomListView(String valgtEtasjeSpinner) {
        alleRomLV = new ArrayList<>();
        alleRomIndeksLV = new ArrayList<>();

        for (Rom etRom : alleRom) {
            if (etRom.getHusID() == husIDValgt) {
                if (Integer.toString(etRom.getEtasje()).equals(valgtEtasjeSpinner)) {
                    alleRomLV.add("\nRomnr: " + etRom.getRomNr() + "\nBeskrivelse: " + etRom.getBeskrivelse());
                    alleRomIndeksLV.add(etRom.getRomID());
                }
            }
        }
        return alleRomLV;
    }

    private class RomJSON extends AsyncTask<String, Void,String> {
        SharedPreferences sp;
        List<Rom> alleRomFraJson = new ArrayList<>();

        @Override
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
                        throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
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
                            Rom etRom = new Rom();
                            etRom.RomID = jsonobject.getInt("RomID");
                            etRom.HusID = jsonobject.getInt("HusID");
                            etRom.Etasje = jsonobject.getInt("EtasjeNr");
                            etRom.RomNr = jsonobject.getInt("RomNr");
                            etRom.Kapasitet = jsonobject.getInt("Kapasitet");
                            etRom.Beskrivelse = jsonobject.getString("Beskrivelse");
                            alleRomFraJson.add(etRom);
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
        }

        @Override
        protected void onPostExecute(String ss) {
            alleRom = alleRomFraJson;
            klar();
        }
    }
}





