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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservasjonliste);
        lv = findViewById(R.id.reservasjoner);
        tb = findViewById(R.id.toolbarReservasjon);
        tb.setLogo(R.mipmap.ic_launcher_round);
        tb.inflateMenu(R.menu.manu_rom);
        setActionBar(tb);

        husIDValgt = sp.getInt("husID", 0);

        Reservasjonjson task = new Reservasjonjson();
        task.execute("http://student.cs.hioa.no/~s331153/reservasjonjsonout.php");
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        stringAlleReservasjoner = sp.getString("alleReservasjoner", "Får ikke hentet rom");

        alleReservasjoner = new ArrayList<>();

        String semikolon = ";";
        String[] tempArray2;
        tempArray2 = stringAlleReservasjoner.split(semikolon);
        for (int i = 0; i < tempArray2.length; i+=7){
            Reservasjon enReservasjon = new Reservasjon();
            enReservasjon.setReservasjonsID(Integer.parseInt(tempArray2[i]));
            enReservasjon.setRomID(Integer.parseInt(tempArray2[i+1]));
            enReservasjon.setHusID(Integer.parseInt(tempArray2[i+2]));
            enReservasjon.setNavn(tempArray2[i+3]);
            enReservasjon.setDato(tempArray2[i+4]);
            enReservasjon.setTidFra(tempArray2[i+5]);
            enReservasjon.setTidTil(tempArray2[i+6]);
            alleReservasjoner.add(enReservasjon);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, visReservasjonListView()){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long j){
                AlertDialog.Builder builder = new AlertDialog.Builder(ReservasjonListe.this, R.style.AlertDialogStyle);
                builder.setMessage(getResources().getString(R.string.slettReservasjon));
                builder.setPositiveButton(getResources().getString(R.string.ja), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //sette inn kode for slett her
                        Intent intent = new Intent(ReservasjonListe.this, MapsActivity.class);
                        startActivity(intent);
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.nei), null);
                builder.show();
            }
        });
    }

    public List<String> visReservasjonListView(){
        List<String> alleReservasjonerLV = new ArrayList<>();

        for(Reservasjon enReservasjon : alleReservasjoner){
            if(enReservasjon.getHusID() == husIDValgt){
                    alleReservasjonerLV.add("\nHusID: "+enReservasjon.getHusID()+
                            "\nNavn: "+enReservasjon.getNavn()+"\nDato: "+enReservasjon.getDato()+
                            "\nTidFra: "+enReservasjon.getTidFra()+"\nTidTil: "+enReservasjon.getTidTil());
                }
            }
        return alleReservasjonerLV;
    }
}
