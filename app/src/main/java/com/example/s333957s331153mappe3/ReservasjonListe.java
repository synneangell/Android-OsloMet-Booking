package com.example.s333957s331153mappe3;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class ReservasjonListe extends AppCompatActivity {
    ListView lv;
    SharedPreferences sp;
    List<Reservasjon> alleReservasjoner;
    String stringAlleReservasjoner;
    Toolbar tb;
    int husIDValgt;
    List<String> alleReservasjonerLV = new ArrayList<>();
    List<Integer> alleReservasjonerIndeksLV;
    ArrayAdapter<String> adapter;
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
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        stringAlleReservasjoner = sp.getString("alleReservasjoner", "Får ikke hentet reservasjon");
        alleReservasjoner = new ArrayList<>();

        String semikolon = ";";
        String[] tempArray2;
        tempArray2 = stringAlleReservasjoner.split(semikolon);
        for (int i = 0; i < tempArray2.length; i += 6) {
            Reservasjon enReservasjon = new Reservasjon();
            enReservasjon.reservasjonsID = Integer.parseInt(tempArray2[i]);
            enReservasjon.romID = Integer.parseInt(tempArray2[i + 1]);
            enReservasjon.husID = Integer.parseInt(tempArray2[i + 2]);
            enReservasjon.navn = tempArray2[i + 3];
            enReservasjon.dato = tempArray2[i + 4];
            enReservasjon.tid = tempArray2[i + 5];
            alleReservasjoner.add(enReservasjon);
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

    public void slettReservasjon(){
        alleReservasjoner.remove(slettetIndeks);
        alleReservasjonerLV.remove(slettetIndeks);
        adapter.notifyDataSetChanged();
    }

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
}