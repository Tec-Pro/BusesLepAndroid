package com.tecpro.buseslep;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tecpro.buseslep.search_scheludes.SearchScheludes;
import com.tecpro.buseslep.utils.SecurePreferences;
import com.tecpro.buseslep.webservices.WebServices;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by jacinto on 6/4/15.
 */
public class Singin extends Activity {
    private List<String> departureCities; //los nombres de ciudades
    private Spinner spinner;
    private TreeMap<String, Integer>CitiesAndId; //nombre e id de las ciudades de origen, la clave es el nombre para facilitar las cosas

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singin);
        spinner = (Spinner) findViewById(R.id.spinnerCities);
        AsyncCallerCities asyncCallerCities= new AsyncCallerCities();
        asyncCallerCities.execute("getCities");
    }

    public void singin(View v){
        String name =  ((EditText) findViewById(R.id.txtName)).getText().toString();
        String surname =  ((EditText) findViewById(R.id.txtSurname)).getText().toString();
        String email =  ((EditText) findViewById(R.id.txtEmail)).getText().toString();
        String dni =  ((EditText) findViewById(R.id.textDNI)).getText().toString();
        String pass = ((EditText) findViewById(R.id.txtPass)).getText().toString();
        String passConfirm =  ((EditText) findViewById(R.id.txtPassConfirm)).getText().toString();
        if (pass.equals(passConfirm)) {
            //mandar json(Para crear y despues para logear)
            Intent i;
            if (true) {
                Toast.makeText(getApplicationContext(), "Cuenta creada exitosamente", Toast.LENGTH_LONG).show();
                SecurePreferences preferences = new SecurePreferences(getApplication(), "my-preferences", "BusesLepCordoba", true);
                preferences.put("dni", dni);
                preferences.put("pass", pass);
                preferences.put("login", "true");
                Bundle bundle = getIntent().getExtras();
                if (bundle.getString("next").equals("main")) {
                    i = new Intent(this, SearchScheludes.class);
                } else {
                    if (bundle.getString("next").equals("purchase")) {
                        i = new Intent(this, PurchaseDetails.class);
                    } else {
                        i = new Intent(this, ReserveDetails.class);
                    }
                    i.putExtra("city_from",bundle.getString("city_from"));
                    i.putExtra("city_to",bundle.getString("city_to"));
                    i.putExtra("arrival_date1",bundle.getString("arrival_date1"));
                    i.putExtra("arrival_hour1",bundle.getString("arrival_hour1"));
                    i.putExtra("arrival_date2",bundle.getString("arrival_date2"));
                    i.putExtra("arrival_hour2",bundle.getString("arrival_hour2"));
                    i.putExtra("cant_tickets", bundle.getString("cant_tickets"));
                    i.putExtra("roundtrip",bundle.getInt("roundtrip"));
                }
                startActivity(i);
            } else {         //si no se logea
                Toast.makeText(getApplicationContext(), "Ocurrio un error, intente luego nuevamente", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * el primer atributo que es String, son los nombres de los metodos que quiero llamar, lo hardcodeo con 1 solo atributo que es el nombre
     * del metodo así lo corro
     */
    public class AsyncCallerCities extends AsyncTask<String, Void, Pair<String,List<String>> > {
        ProgressDialog pdLoading = new ProgressDialog(Singin.this);

        private AsyncCallerCities() {
            pdLoading.setCancelable(false);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setTitle("Por favor, espere.");
            pdLoading.setMessage("Obteniendo datos del servidor");
            pdLoading.show();
        }

        @Override
        //devuelvo la lista de ciudades que se obtuvo y el nombre para saber si es de origen o destino
        protected Pair<String, List<String>> doInBackground(String... params) {
            //dependiendo de que le paso por parametro, me fijo que hago
            switch (params[0]) {
                case "getCities":
                   // ArrayList cities = WebServices.getCities(getApplicationContext());
                   // departureCities = cities.second;
                   // CitiesAndId = cities.first;
                   // return new Pair("getCities", departureCities);
            }
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here


            return null;
        }

        @Override
        protected void onPostExecute(Pair<String, List<String>> result) {
            if (result == null)
                Toast.makeText(getBaseContext(), "No se han encontrado ciudades", Toast.LENGTH_SHORT).show();
                //this method will be running on UI thread
            else {
                switch (result.first) {
                    case "getCities":
                        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(Singin.this, android.R.layout.simple_spinner_item, departureCities);
                        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adaptador);
                        break;
                }
            }
            pdLoading.dismiss();
        }
    }
}
