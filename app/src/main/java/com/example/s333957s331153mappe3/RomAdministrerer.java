package com.example.s333957s331153mappe3;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RomAdministrerer extends AppCompatActivity {
    Spinner etasje;
    EditText romNr, kapasitet, beskrivelse;
    TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_romadministrator);
        etasje = (Spinner) findViewById(R.id.spinnerEtasjer);
        romNr = (EditText) findViewById(R.id.romNr);
        kapasitet = (EditText) findViewById(R.id.kapasitet);
        beskrivelse = (EditText) findViewById(R.id.beskrivelseRom);
        test = (TextView) findViewById(R.id.test);

        RomGetJSON task = new RomGetJSON();
        task.execute(new String[]{"http://student.cs.hioa.no/~s333975/Romjsonout.php"});
    }

    public void regRom(View v){

    }

    class RomGetJSON extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String retur = "";
            String s = "";
            String output = "";
            for (String url : urls) {
                try {
                    URL urlen = new URL(urls[0]);
                    HttpURLConnection conn = (HttpURLConnection)
                            urlen.openConnection(); conn.setRequestMethod("GET");
                    conn.setRequestProperty("Accept", "application/json");
                    if (conn.getResponseCode() != 200) {
                        throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                    }

                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    System.out.println("Output from server .... \n");
                    while ((s = br.readLine()) != null) {
                        output = output + s;
                    }
                    conn.disconnect();
                    try {
                        JSONArray rom = new JSONArray(output);
                        for (int i = 0; i < rom.length(); i++) {
                            JSONObject jsonobject = rom.getJSONObject(i);
                            int RomID = jsonobject.getInt("RomID");
                            String Beskrivelse = jsonobject.getString("Beskrivelse");
                            int HusID = jsonobject.getInt("HusID");
                            int Etasje = jsonobject.getInt("Etasje");
                            int Romnr = jsonobject.getInt("RomNr");
                            int Kapasitet = jsonobject.getInt("Kapasitet");

                            retur = RomID + HusID + Etasje + Romnr + Kapasitet + Beskrivelse + "\n";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    return "Noe gikk galt";
                }
            }
            return retur;
        }

        @Override
        protected void onPostExecute(String ss) {
            test.setText(ss);
        }
    }
}