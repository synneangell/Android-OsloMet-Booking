package com.example.s333957s331153mappe3;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class ReservasjonAdministrerer extends AppCompatActivity {
    EditText navn, dato;
    private int year, month, day;
    Button datePicker;
    Toolbar tb;
    Spinner tid;
    int valgtHusID, valgtRomID;
    List<Reservasjon> alleReservasjoner;
    String[] tider = new String[]{"08:00-09:00", "09:00-10:00", "10:00-11:00", "11:00-12:00", "12:00-13:00",
            "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00", "17:00-18:00", "18:00-19:00",
            "19:00-20:00", "20:00-21:00", "21:00-22:00", "22:00-23:00"};

    public static final Pattern NAVN = Pattern.compile("[a-zæøåA-ZÆØÅ0-9 ]{2,20}");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservasjon);
        navn = findViewById(R.id.txtNavn);
        dato = findViewById(R.id.dato);
        datePicker = findViewById(R.id.datePicker);
        tid = findViewById(R.id.spinnerTid);

        tb = findViewById(R.id.toolbarReservasjon);
        tb.setTitle("\tOsloMet-Booking");
        tb.setLogo(R.mipmap.ic_launcher_round);
        tb.inflateMenu(R.menu.manu_rom);
        setActionBar(tb);
        tb.setNavigationIcon(R.drawable.ic_baseline_arrow_back_24);
        tb.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReservasjonAdministrerer.this, HusOversikt.class);
                startActivity(i);
            }
        });

        Intent intent = getIntent();
        valgtHusID = intent.getIntExtra("husID", 0);
        valgtRomID = intent.getIntExtra("romID", 0);

        ReservasjonJSON task = new ReservasjonJSON();
        task.execute("http://student.cs.hioa.no/~s331153/reservasjonjsonout.php");
    }

    //----- Metoder som skjer i det task blir ferdig -----//
    public void klar() {
        hentReservasjoner();
    }

    public void hentReservasjoner() {
        settAdapter();
    }

    public void settAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tider) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = view.findViewById(android.R.id.text1);
                textView.setTextColor(Color.BLACK);
                return view;
            }
        };
        tid.setAdapter(adapter);
    }

    //----- Lagre funksjonalitet -----//
    public void lagreRes(View v) throws ParseException {
        SendJSON task = new SendJSON();
        if (!validerNavn() | !validerDato()) {
            Toast.makeText(ReservasjonAdministrerer.this, "Alle felt må være riktig fylt inn riktig", Toast.LENGTH_SHORT).show();
            return;
        } else {
            boolean tidOpptatt = false;
            for (Reservasjon enReservasjon : alleReservasjoner) {
                if (enReservasjon.getDato().equals(dato.getText().toString()) && enReservasjon.getTid().equals(tid.getSelectedItem().toString())) {
                    tidOpptatt = true;
                }
            }
            if (tidOpptatt == true) {
                Toast.makeText(ReservasjonAdministrerer.this, "Rommet er opptatt på dette tidspunktet. Velg en annen tid", Toast.LENGTH_SHORT).show();
                return;
            } else {
                String urlString = ("http://student.cs.hioa.no/~s331153/reservasjonjsonin.php/?" +
                        "RomID=" + valgtRomID +
                        "&HusID=" + valgtHusID +
                        "&Navn=" + navn.getText().toString() +
                        "&Dato=" + dato.getText().toString() +
                        "&Tid=" + tid.getSelectedItem()).replaceAll(" ", "%20");
                task.execute(urlString);
                Toast.makeText(this, "Reservasjon opprettet!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, MapsActivity.class);
                startActivity(intent);
            }

        }
    }

    //----- Metoder for validering -----//
    public boolean validerNavn() {
        String navnInput = navn.getText().toString().trim();
        if (navnInput.isEmpty()) {
            navn.setError("Navn kan ikke være tomt");
            return false;
        } else if (!NAVN.matcher(navnInput).matches()) {
            navn.setError("Navnet må bestå av mellom 2 og 20 tegn");
            return false;
        } else {
            navn.setError(null);
            return true;
        }
    }

    public boolean validerDato() throws ParseException {
        String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        SimpleDateFormat sdformat = new SimpleDateFormat("dd-MM-yyyy");
        Date d1 = sdformat.parse(currentDate);
        String datoInput = dato.getText().toString().trim();

        if (datoInput.isEmpty()) {
            dato.setError("Dato må være valgt eller skrevet inn");
            return false;
        } else {
            try {
                Date d2 = sdformat.parse(datoInput);
                if (d1.compareTo(d2) > 0) {
                    dato.setError("Dato kan ikke være tilbake i tid");
                    return false;
                } else if (d1.equals(d2)) {
                    String tidInput = tid.getSelectedItem().toString();
                    Date date = new Date();
                    DateFormat format = new SimpleDateFormat("HH:mm");
                    String tidNaa = format.format(date);
                    String[] tidArray = tidInput.split(":");
                    String[] tidNaaArray = tidNaa.split(":");
                    if ((Integer.parseInt(tidArray[0]) <= Integer.parseInt(tidNaaArray[0]))) {
                        dato.setError("Tidspunkt og dato må være frem i tid");
                        return false;
                    }
                } else {
                    dato.setError(null);
                    return true;
                }
            } catch (ParseException e) {
                dato.setError("Dato må være i format DD-MM-YYYY");
                return false;
            }
        }
        return true;
    }


    public void dato(View v){
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.datepicker,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        if(dayOfMonth <= 9 && monthOfYear <= 9){
                            String day = String.format("%02d" , dayOfMonth);
                            monthOfYear += 1;
                            String month = String.format("%02d" , monthOfYear);
                            dato.setText(day + "-" + month + "-" + year);
                        }
                        else if(dayOfMonth <= 9){
                            String day = String.format("%02d" , dayOfMonth);
                            dato.setText(day + "-" + (monthOfYear+1) + "-" + year);
                        }
                        else if(monthOfYear<= 9){
                            monthOfYear += 1;
                            String month = String.format("%02d" , monthOfYear);
                            dato.setText(dayOfMonth + "-" + month + "-" + year);
                        }
                        else{
                            dato.setText(dayOfMonth + "-" + (monthOfYear+1) + "-" + year);
                        }
                    }
                }, year, month, day);
        datePickerDialog.show();
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
