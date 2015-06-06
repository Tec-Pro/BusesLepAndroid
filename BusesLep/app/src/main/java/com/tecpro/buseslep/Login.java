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
        if (true) {
            Toast.makeText(getApplicationContext(), "Iniciando sesion", Toast.LENGTH_LONG).show();
            SecurePreferences preferences = new SecurePreferences(getApplication(), "my-preferences", "BusesLepCordoba", true);
            preferences.put("dni", dni);
            preferences.put("pass", pass);
            preferences.put("login", "true");
          /*  Bundle bundle = getIntent().getExtras();
            if (bundle.getString("next").equals("main")) {
                Intent i = new Intent(this, MainActivity.class);
            } else {
                if (bundle.getString("next").equals("purchase")) {
                    //  Intent i = new Intent(this, purchase.class);
                } else {
                    //  Intent i = new Intent(this, reserve.class);
                }
            }*/
            Intent i = new Intent(this, MainActivity.class); //borrar esta linea
            startActivity(i);
        } else {         //si no se logea
            Toast.makeText(getApplicationContext(), "Ocurrio un error", Toast.LENGTH_LONG).show();
        }
    }

    public void launchSignin(View v){
        Intent i = new Intent(this, Singin.class);
        startActivity(i);
    }

}
