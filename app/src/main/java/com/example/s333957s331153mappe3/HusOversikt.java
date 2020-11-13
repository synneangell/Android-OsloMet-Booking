package com.example.s333957s331153mappe3;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
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
    List<Hus> alleHus;
    String stringAlleHus;
    int husIDValgt;
    SharedPreferences sp;
    Integer[] husEtasjer;
    String husInfoString;
    Hus valgtHus;


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

        sp = PreferenceManager.getDefaultSharedPreferences(MapsActivity.getContextOfApplication());
        stringAlleHus = sp.getString("alleHus", "Får ikke hentet data");
        husIDValgt = sp.getInt("husID", 0);
        Log.d("Alle hus i husoversikt", stringAlleHus);
        Log.d("HusID valgt", Integer.toString(husIDValgt));

        alleHus = new ArrayList<>();

        String[] tempArray;
        String semikolon = ";";
        tempArray = stringAlleHus.split(semikolon);
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

        for(Hus etHus : alleHus){
            if(etHus.husID == husIDValgt){
                valgtHus = etHus;
            }
        }

        husInfoString = "HusID: "+valgtHus.husID +
                "\nNavn: "+valgtHus.navn +
                "\nBeskrivelse: "+valgtHus.beskrivelse +
                "\nGateadresse: "+valgtHus.gateAdresse;
        husInfo.setText(husInfoString);

        husEtasjer = new Integer[valgtHus.etasjer];
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
        etasjer.setAdapter(etasjeAdapter);

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

    private class HusAsyncTask extends AsyncTask<String, Void,String> {
        List<Hus> alleHus = new ArrayList<>();

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
                    conn.setRequestMethod("GET");
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
                            int husID = jsonobject.getInt("HusID");
                            String navn = jsonobject.getString("Navn");
                            String beskrivelse = jsonobject.getString("Beskrivelse");
                            String gateadresse = jsonobject.getString("Gateadresse");
                            Double latitude = jsonobject.getDouble("Latitude");
                            Double longitude = jsonobject.getDouble("Longitude");
                            int etasjer = jsonobject.getInt("Etasjer");
                            Hus etHus = new Hus(navn, beskrivelse, gateadresse, latitude, longitude, etasjer);
                            alleHus.add(etHus);
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
            husInfo.setText(
                    "Hus ID: " + alleHus.get(0).husID + "\n" +
                            "Navn: " + alleHus.get(0).navn + "\n" +
                            "Beskrivelse: " + alleHus.get(0).beskrivelse + "\n" +
                            "Adresse: " + alleHus.get(0).gateAdresse + "\n" +
                            "Etasjer: " + alleHus.get(0).etasjer);

/*            Integer[] husEtasjer = new Integer[]{};

            int etasjeHus = alleHus.get(0).etasjer;
            for(int i = 1; i <= etasjeHus; i++){
                husEtasjer = new Integer[i];
            }*/

            Integer[] husEtasjer = new Integer[alleHus.get(0).etasjer];
            int etasjeNr = 1;
            for(int i = 0; i < husEtasjer.length; i++){
                husEtasjer[i] = etasjeNr;
                etasjeNr++;
            }

            ArrayAdapter<Integer> adapter2 = new ArrayAdapter<Integer>(HusOversikt.this, android.R.layout.simple_spinner_item, husEtasjer){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView textView = view.findViewById(android.R.id.text1);
                    textView.setTextColor(Color.BLACK);
                    return view;
                }
            };
            etasjer.setAdapter(adapter2);
        }
    }
}

