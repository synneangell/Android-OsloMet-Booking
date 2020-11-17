package com.example.s333957s331153mappe3;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
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
    SharedPreferences sp;
    String stringAlleReservasjoner;
    List<Reservasjon> alleReservasjoner;
    List<String> ledigeTider;
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
        tb.setTitle("\tReserver rom");
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

        dato.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                visLedigeTider();
            }
        });

        ReservasjonJSON task = new ReservasjonJSON();
        task.execute("http://student.cs.hioa.no/~s331153/reservasjonjsonout.php");
        sp = PreferenceManager.getDefaultSharedPreferences(this);
        stringAlleReservasjoner = sp.getString("alleReservasjoner", "Får ikke hentet reservasjon");
        alleReservasjoner = new ArrayList<>();

        String semikolon = ";";
        String[] tempArray2;
        tempArray2 = stringAlleReservasjoner.split(semikolon);
        for (int i = 0; i < tempArray2.length; i+=6){
            Reservasjon enReservasjon = new Reservasjon();
            enReservasjon.reservasjonsID = Integer.parseInt(tempArray2[i]);
            enReservasjon.romID = Integer.parseInt(tempArray2[i + 1]);
            enReservasjon.husID = Integer.parseInt(tempArray2[i + 2]);
            enReservasjon.navn = tempArray2[i + 3];
            enReservasjon.dato = tempArray2[i + 4];
            enReservasjon.tid = tempArray2[i + 5];
            alleReservasjoner.add(enReservasjon);
        }
    }

    public List<String> visLedigeTider(){
        for(Reservasjon enReservasjon : alleReservasjoner){
            if(enReservasjon.getDato().equals(dato)){
                    for(String ledigTid : tider){
                        if(!enReservasjon.getTid().equals(tid)){
                            ledigeTider.add(ledigTid);
                        }
                    }
                } else {
                for(String tid : tider){
                    ledigeTider.add(tid);
                }
            }
        }
        settAdapter();
        return ledigeTider;
    }

    public void settAdapter(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, visLedigeTider()){
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

    public void lagreRes (View v) throws ParseException {
        LagreReservasjonJSON task = new LagreReservasjonJSON();
        if(!validerNavn() | !validerDato()) {
            Toast.makeText(ReservasjonAdministrerer.this, "Alle felt må være riktig fylt inn riktig", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            String urlString = ("http://student.cs.hioa.no/~s331153/reservasjonjsonin.php/?" +
                    "Navn=" + navn.getText().toString() +
                    "&Dato=" + dato.getText().toString() +
                    "&Tid=" + tid.getSelectedItem()).replaceAll(" ", "%20");
            task.execute(urlString);
            Toast.makeText(this, "Reservasjon opprettet!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        }
    }

    public boolean validerNavn(){
        String navnInput = navn.getText().toString().trim();
        if(navnInput.isEmpty()){
            navn.setError("Navn kan ikke være tomt");
            return false;
        } else if (!NAVN.matcher(navnInput).matches()){
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

        if(datoInput.isEmpty()) {
            dato.setError("Dato må være valgt eller skrevet inn");
            return false;
        }
        else {
            try {
                Date d2 = sdformat.parse(datoInput);
                if (d1.compareTo(d2) > 0) {
                    dato.setError("Dato kan ikke være tilbake i tid");
                    return false;
                } else {
                    dato.setError(null);
                    return true;
                }
            }
            catch(ParseException e){
                dato.setError("Dato må være i format DD-MM-YYYY");
                return false;
            }
        }
    }

    public void dato(View v){
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.AlertDialogStyle,
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
}
