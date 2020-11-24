package com.example.s333957s331153mappe3;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
    String stringAlleRom;
    String valgtEtasje;
    Hus valgtHus;
    public static Context context;
    int slettetIndeks;
    List<String> alleRomLV = new ArrayList<>();
    ArrayAdapter<String> romAdapter;
    List<Integer> alleRomIndeksLV;
    Button slettRom, opprettReservasjon;


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

        context = getApplicationContext();

        RomJSON task = new RomJSON();
        task.execute(new String[]{"http://student.cs.hioa.no/~s331153/romjsonout.php"});
        sp = PreferenceManager.getDefaultSharedPreferences(MapsActivity.getContext());
        stringAlleHus = sp.getString("alleHus", "Får ikke hentet data");
        husIDValgt = sp.getInt("husID", 0);
        Log.d("Alle hus i husoversikt", stringAlleHus);

        stringAlleRom = sp.getString("alleRom", "Får ikke hentet rom");
        Log.d("Alle rom i husoversikt", stringAlleRom);

        alleHus = new ArrayList<>();

        String[] tempArray;
        String semikolon = ";";
        tempArray = stringAlleHus.split(semikolon);
        for (int i = 0; i < tempArray.length; i += 7) {
            Hus etHus = new Hus();
            etHus.husID = Integer.parseInt(tempArray[i]);
            etHus.navn = tempArray[i + 1];
            etHus.beskrivelse = tempArray[i + 2];
            etHus.gateAdresse = tempArray[i + 3];
            etHus.latitude = Double.parseDouble(tempArray[i + 4]);
            etHus.longitude = Double.parseDouble(tempArray[i + 5]);
            etHus.etasjer = Integer.parseInt(tempArray[i + 6]);
            alleHus.add(etHus);
        }

        for (Hus etHus : alleHus) {
            if (etHus.husID == husIDValgt) {
                valgtHus = etHus;
            }
        }

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

        alleRom = new ArrayList<>();

        String[] tempArray2;
        tempArray2 = stringAlleRom.split(semikolon);
        if(tempArray2.length > 2) {
            for (int i = 0; i < tempArray2.length; i += 6) {
                Rom etRom = new Rom();
                etRom.setRomID(Integer.parseInt(tempArray2[i]));
                etRom.setHusID(Integer.parseInt(tempArray2[i + 1]));
                etRom.setEtasje(Integer.parseInt(tempArray2[i + 2]));
                etRom.setRomNr(Integer.parseInt(tempArray2[i + 3]));
                etRom.setKapasitet(Integer.parseInt(tempArray2[i + 4]));
                etRom.setBeskrivelse(tempArray2[i + 5]);
                alleRom.add(etRom);
            }
        }

        Log.d("Alle rom size", Integer.toString(alleRom.size()));

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
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
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
                                        Log.d("Romid", String.valueOf(romID));
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
                                //Her må det også bli sendt med valgt hus og rom
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

    public void slettRom(){
        alleRomLV.remove(slettetIndeks);
        alleRom.remove(slettetIndeks);
        romAdapter.notifyDataSetChanged();
    }

    public static Context getContext() {
        return context;
    }

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
}





