package com.example.s333957s331153mappe3;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HusAdministrerer extends AppCompatActivity {
    //Spinner etasjer;
    TextView txtKoordinater, test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_husadministrator);

        test = (TextView) findViewById(R.id.test);

/*        txtKoordinater = (TextView) findViewById(R.id.koordinater);
        etasjer = (Spinner) findViewById(R.id.spinnerEtasjer);

        Integer[] items = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        etasjer.setAdapter(adapter);

        LatLng koordinater = getIntent().getExtras().getParcelable("koordinater");
        txtKoordinater.setText(koordinater.toString());*/

        HusAdministrerer.HusGetJSON task = new HusAdministrerer.HusGetJSON();
        task.execute(new String[]{"http://student.cs.hioa.no/~s333975/Husjsonout.php"});
    }

    private class HusGetJSON extends AsyncTask<String, Void, String> {

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
                    System.out.println("Output from server .... \n");
                    while ((s = br.readLine()) != null) {
                        output = output + s;
                    }
                    conn.disconnect();
                    try {
                        JSONArray rommene = new JSONArray(output);
                        for (int i = 0; i < rommene.length(); i++) {
                            JSONObject jsonobject = rommene.getJSONObject(i);
                            int HusID = jsonobject.getInt("HusID");
                            String Beskrivelse = jsonobject.getString("Beskrivelse");
                            String Gateadresse = jsonobject.getString("Gateadresse");
                            String GpsKoordinater = jsonobject.getString("GpsKoordinater");
                            int Etasjer = jsonobject.getInt("Etasjer");

                            retur = HusID + Beskrivelse + Gateadresse + GpsKoordinater + Etasjer + "\n";
                        }
                        return retur;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    return retur;
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
