package com.example.s333957s331153mappe3;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
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

public class ReservasjonListe extends AppCompatActivity {
    List<Reservasjon> alleReservasjoner;
    List<String> alleReservasjonerLV = new ArrayList<>();
    List<Integer> alleReservasjonerIndeksLV;
    ListView lv;
    ArrayAdapter<String> adapter;
    Toolbar tb;
    int husIDValgt;
    int slettetIndeks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservasjonliste);
        lv = findViewById(R.id.reservasjoner);
        tb = findViewById(R.id.toolbarReservasjon);
        tb.setLogo(R.mipmap.ic_launcher_round);
        tb.inflateMenu(R.menu.manu_rom);
        setActionBar(tb);

        tb.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReservasjonListe.this, MapsActivity.class);
                startActivity(i);
            }
        });

        Intent intent = getIntent();
        husIDValgt = intent.getIntExtra("husID", 0);

        ReservasjonJSON task = new ReservasjonJSON();
        task.execute("http://student.cs.hioa.no/~s331153/reservasjonjsonout.php");

    }

    //----- Metoder som skjer naar task er klar -----//
    public void klar(){
        hentReservasjoner();
    }

    public void hentReservasjoner(){
        for(Reservasjon enReservasjon : alleReservasjoner){
            Log.d("Reservasjon", enReservasjon.navn);
        }

        adapter = lagAdapter();
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, View view, int i, long j) {
                final int indeks = i;
                AlertDialog.Builder builder = new AlertDialog.Builder(ReservasjonListe.this, R.style.AlertDialogStyle);
                builder.setMessage(getResources().getString(R.string.slettReservasjon));
                builder.setPositiveButton(getResources().getString(R.string.ja), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int reservasjonsID = alleReservasjonerIndeksLV.get(indeks);
                        SlettReservasjonJSON task = new SlettReservasjonJSON();
                        String url = "http://student.cs.hioa.no/~s331153/slettreservasjonjson.php/?ReservasjonID=" + Integer.toString(reservasjonsID);
                        task.execute(url);
                        slettetIndeks = indeks;
                        slettReservasjon();
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.nei), null);
                builder.show();

            }
        });
    }

    //----- Oppdaterer listview dersom resverasjon slettes -----//
    public void slettReservasjon(){
        alleReservasjoner.remove(slettetIndeks);
        alleReservasjonerLV.remove(slettetIndeks);
        adapter.notifyDataSetChanged();
    }

    //----- Metoder for aa fylle listview -----//

    public ArrayAdapter lagAdapter() {
        ArrayAdapter nyAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, visReservasjonListView()) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };
        return nyAdapter;
    }

    public List<String> visReservasjonListView() {
        alleReservasjonerLV = new ArrayList<>();
        alleReservasjonerIndeksLV = new ArrayList<>();
        for (Reservasjon enReservasjon : alleReservasjoner) {
            Log.d("Res size i metode", Integer.toString(alleReservasjoner.size()));
            if (enReservasjon.getHusID() == husIDValgt) {
                alleReservasjonerLV.add("\nReservasjonsID: " + enReservasjon.getReservasjonsID() +
                        "\nHusID: " + enReservasjon.getHusID() +
                        "\nRomID: " + enReservasjon.getRomID() +
                        "\nNavn: " + enReservasjon.getNavn() +
                        "\nDato: " + enReservasjon.getDato() +
                        "\nTid: " + enReservasjon.getTid());
                alleReservasjonerIndeksLV.add(enReservasjon.getReservasjonsID());
            }
        }
        return alleReservasjonerLV;
    }

    private class ReservasjonJSON extends AsyncTask<String, Void,String> {
        List <Reservasjon> reservasjonJSON = new ArrayList<>();

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
                        Log.d("output i resJSON", output);
                    }
                    conn.disconnect();

                    try {
                        JSONArray mat = new JSONArray(output);
                        for (int i = 0; i < mat.length(); i++) {
                            JSONObject jsonobject = mat.getJSONObject(i);
                            Reservasjon enReservasjon = new Reservasjon();
                            enReservasjon.reservasjonsID = jsonobject.getInt("ReservasjonID");
                            enReservasjon.romID = jsonobject.getInt("RomID");
                            enReservasjon.husID = jsonobject.getInt("HusID");
                            enReservasjon.navn = jsonobject.getString("Navn");
                            enReservasjon.dato = jsonobject.getString("Dato");
                            enReservasjon.tid = jsonobject.getString("Tid");
                            reservasjonJSON.add(enReservasjon);
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
            alleReservasjoner = reservasjonJSON;
            klar();

        }
    }
}