package com.example.s333957s331153mappe3;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HusAdministrerer extends AppCompatActivity {
    TextView test;
    EditText navnBygning, beskrivelseBygning, adresseBygning, etasjerBygning;
    String server_url = "http://student.cs.hioa.no/~s333975/Husjsonin.php";
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_husadministrator);

        navnBygning = (EditText) findViewById(R.id.navnBygning);
        beskrivelseBygning = (EditText) findViewById(R.id.beskrivelseBygning);
        adresseBygning = (EditText) findViewById(R.id.adresseBygning);
        etasjerBygning = (EditText) findViewById(R.id.etasjerBygning);
        test = (TextView) findViewById(R.id.test);
        builder = new AlertDialog.Builder(HusAdministrerer.this);

/*      txtKoordinater = (TextView) findViewById(R.id.koordinater);
        etasjer = (Spinner) findViewById(R.id.spinnerEtasjer);

        Integer[] items = new Integer[]{1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        etasjer.setAdapter(adapter);

        LatLng koordinater = getIntent().getExtras().getParcelable("koordinater");
        txtKoordinater.setText(koordinater.toString());*/

        HusAdministrerer.HusGetJSON task = new HusAdministrerer.HusGetJSON();
        task.execute(new String[]{"http://student.cs.hioa.no/~s331153/husjsonout.php"});
    }

/*    public void regBygning(View v){
        final String navn, beskrivelse, adresse, etasjer;
        navn = navnBygning.getText().toString();
        beskrivelse = beskrivelseBygning.getText().toString();
        adresse = adresseBygning.getText().toString();
        etasjer = etasjerBygning.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        builder.setTitle("Server response");
                        builder.setMessage("Response :"+response);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                navnBygning.setText("");
                                beskrivelseBygning.setText("");
                                adresseBygning.setText("");
                                etasjerBygning.setText("");
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();

                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Navn", navn);
                params.put("Beskrivelse", beskrivelse);
                params.put("Adresse", adresse);
                params.put("Etasjer", etasjer);
                return params;
            }
        };
    }*/

    /**
     *     private class HusGetJSON extends AsyncTask<String, Void, List<Hus>> {
     *        @Override
     *         protected List<Hus> doInBackground(String... urls) {
     *             List<Hus> husListe = new ArrayList<>();
     *             JSONArray hus = JsonParser.createJsonArray(urls[0]);
     */

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
