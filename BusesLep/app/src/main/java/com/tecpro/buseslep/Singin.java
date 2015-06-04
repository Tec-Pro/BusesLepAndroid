package com.tecpro.buseslep;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.tecpro.buseslep.utils.SecurePreferences;

/**
 * Created by jacinto on 6/4/15.
 */
public class Singin extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singin);
    }

    public void singin(View v){
        String name =  ((EditText) findViewById(R.id.txtName)).getText().toString();
        String surname =  ((EditText) findViewById(R.id.txtSurname)).getText().toString();
        String email =  ((EditText) findViewById(R.id.txtEmail)).getText().toString();
        String dni =  ((EditText) findViewById(R.id.textDNI)).getText().toString();
        String pass = ((EditText) findViewById(R.id.txtPass)).getText().toString();
        String passConfirm =  ((EditText) findViewById(R.id.txtPassConfirm)).getText().toString();
        if (pass.equals(passConfirm)) {
            //mandar json
            if (true) {
                Toast.makeText(getApplicationContext(), "Cuenta creada exitosamente", Toast.LENGTH_LONG).show();
                SecurePreferences preferences = new SecurePreferences(getApplication(), "my-preferences", "BusesLepCordoba", true);
                preferences.put("dni", dni);
                preferences.put("pass", pass);
            /*   Bundle bundle = getIntent().getExtras();
                if (bundle.getString("next").equals("main")) {
                    Intent i = new Intent(this, MainActivity.class);
                } else {
                    if (bundle.getString("next").equals("purchase")) {
                        //  Intent i = new Intent(this, purchase.class);
                    } else {
                        //  Intent i = new Intent(this, reserve.class);
                    }
                }*/
                Intent i = new Intent(this, MainActivity.class); //borrar esta linea cuando este todo
                startActivity(i);
            } else {         //si no se logea
                Toast.makeText(getApplicationContext(), "Ocurrio un error, intente luego nuevamente", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
        }
    }
}
