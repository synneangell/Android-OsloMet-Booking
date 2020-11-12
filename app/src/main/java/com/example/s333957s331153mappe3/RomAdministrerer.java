package com.example.s333957s331153mappe3;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RomAdministrerer extends AppCompatActivity {
    Spinner etasjer;
    EditText romNr, kapasitet, beskrivelse;
    Toolbar tb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_romadministrator);
        etasjer = findViewById(R.id.spinnerEtasjer);
        romNr = findViewById(R.id.romNr);
        kapasitet = findViewById(R.id.kapasitet);
        beskrivelse = findViewById(R.id.beskrivelseRom);
        tb = findViewById(R.id.toolbarRom);
        tb.setLogo(R.mipmap.ic_launcher_round);
        tb.inflateMenu(R.menu.manu_rom);
        setActionBar(tb);

        RomAdministrerer.RomJSON task = new RomAdministrerer.RomJSON();
        task.execute("http://student.cs.hioa.no/~s331153/romjsonout.php");

        int husid = getSharedPreferences("HusOversikt", MODE_PRIVATE).getInt("HusID", MODE_PRIVATE);
        int etasjerHus = getSharedPreferences("HusOversikt", MODE_PRIVATE).getInt("Etasjer", MODE_PRIVATE);
        Log.d("Hus id er ", Integer.toString(husid));
        Log.d("Etasjer i hus ", Integer.toString(etasjerHus));

        Integer[] husEtasjer = new Integer[etasjerHus];
        int etasjeNr = 1;
        for(int i = 0; i < husEtasjer.length; i++){
            husEtasjer[i] = etasjeNr;
            etasjeNr++;
        }

        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, husEtasjer){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };
        etasjer.setAdapter(adapter);
    }

    public void lagreRom(View v){
        int husid = getSharedPreferences("HusOversikt", MODE_PRIVATE).getInt("HusID", MODE_PRIVATE);

        RomJSON task = new RomJSON();
        String urlString = ("http://student.cs.hioa.no/~s331153/romjsonin.php/?" +
                "HusID=" + husid +
                "&Etasje=" +  etasjer.getSelectedItem() +
                "&RomNr=" + romNr.getText().toString() +
                "&Kapasitet=" + kapasitet.getText().toString() +
                "&Beskrivelse=" + beskrivelse.getText().toString()).replaceAll(" ", "%20");
        task.execute(urlString);
        Toast.makeText(this, "Rom opprettet!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, HusAdministrerer.class);
        startActivity(intent);
    }

    private class RomJSON extends AsyncTask<String, Void,String> {
        List<Rom> alleRom = new ArrayList<>();

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
                            int romID = jsonobject.getInt("RomID");
                            int husID = jsonobject.getInt("HusID");
                            int etasjeNr = jsonobject.getInt("EtasjeNr");
                            int romNr = jsonobject.getInt("RomNr");
                            int kapasitet = jsonobject.getInt("Kapasitet");
                            String beskrivelse = jsonobject.getString("Beskrivelse");
                            Rom etRom = new Rom(romID, husID, etasjeNr, romNr, kapasitet, beskrivelse);
                            alleRom.add(etRom);
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
    }
}
