package com.tecpro.buseslep;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tecpro.buseslep.search_scheludes.SearchScheludes;
import com.tecpro.buseslep.utils.SecurePreferences;

/**
 * Created by jacinto on 6/1/15.
 */
public class Login extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
    }


    public void login(View v){
        String dni =  ((EditText) findViewById(R.id.textDNI)).getText().toString();
        String pass = ((EditText) findViewById(R.id.txtPass)).getText().toString();
        //mandar por json login
        //si se logeo correctamente en lep.
        Intent i;
        if (true) {
            Toast.makeText(getApplicationContext(), "Iniciando sesion", Toast.LENGTH_LONG).show();
            SecurePreferences preferences = new SecurePreferences(getApplication(), "my-preferences", "BusesLepCordoba", true);
            preferences.put("dni", dni);
            preferences.put("pass", pass);
            preferences.put("login", "true");
            Bundle bundle = getIntent().getExtras();
            if (bundle.getString("next").equals("main")) {
                i = new Intent(this, MainActivity.class);
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
            Toast.makeText(getApplicationContext(), "Ocurrio un error", Toast.LENGTH_LONG).show();
        }
    }

    public void launchSignin(View v){
        Intent i = new Intent(this, Singin.class);
        Bundle bundle = getIntent().getExtras();
        if (bundle.getString("next").equals("main")) {
            i.putExtra("next","main");
        } else {
            if (bundle.getString("next").equals("purchase")) {
                i.putExtra("next","purchase");
            } else {
                i.putExtra("next","reserve");
            }
            i.putExtra("city_from", bundle.getString("city_from"));
            i.putExtra("city_to", bundle.getString("city_to"));
            i.putExtra("arrival_date1", bundle.getString("arrival_date1"));
            i.putExtra("arrival_hour1", bundle.getString("arrival_hour1"));
            i.putExtra("arrival_date2", bundle.getString("arrival_date2"));
            i.putExtra("arrival_hour2", bundle.getString("arrival_hour2"));
            i.putExtra("cant_tickets", bundle.getString("cant_tickets"));
            i.putExtra("roundtrip", bundle.getInt("roundtrip"));
        }
        startActivity(i);
    }

}
