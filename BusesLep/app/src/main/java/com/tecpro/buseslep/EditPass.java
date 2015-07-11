package com.tecpro.buseslep;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Pair;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.tecpro.buseslep.search_scheludes.SearchScheludes;
import com.tecpro.buseslep.utils.SecurePreferences;
import com.tecpro.buseslep.webservices.WebServices;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by jacinto on 6/16/15.
 */
public class EditPass extends Activity {
    private DrawerLayout drawerLayout;
    private ListView drawer;
    private ActionBarDrawerToggle toggle;
    private static final String[] opciones = {"Inicio"};
    private static AsyncCallerModificarContrasena asyncCallerModificarContrasena;
    private static String pass;
    private static String nuevapass;
    private static int dni;
    private static String email;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_pass);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        //actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.action_bar, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER
                )
        );
        actionBar.setDisplayShowTitleEnabled(false);
        loadMenuOptions();
    }
    private void loadMenuOptions(){
        // Rescatamos el Action Bar y activamos el boton Home
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        // Declarar e inicializar componentes para el Navigation Drawer
        drawer = (ListView) findViewById(R.id.options_edit_pass);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_pass);

        // Declarar adapter y eventos al hacer click
        drawer.setAdapter(new ArrayAdapter<String>(this, R.layout.element_menu, R.id.list_content, opciones));

        drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // Toast.makeText(SearchScheludes.this, "Pulsado: " + opciones[arg2], Toast.LENGTH_SHORT).show();
                switch (arg2) {
                    case 0:
                        finish();
                        break;
                }

                drawerLayout.closeDrawers();

            }
        });

        // Sombra del panel Navigation Drawer
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        // Integracion boton oficial
        toggle = new ActionBarDrawerToggle(
                this, // Activity
                drawerLayout, // Panel del Navigation Drawer
                R.drawable.ic_navigation_drawer, // Icono que va a utilizar
                R.string.options, // Descripcion al abrir el drawer
                R.string.app_name // Descripcion al cerrar el drawer
        ){
            public void onDrawerClosed(View view) {
                // Drawer cerrado
                getActionBar().setTitle(getResources().getString(R.string.app_name));
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                // Drawer abierto
                getActionBar().setTitle(R.string.options);
                invalidateOptionsMenu();
            }
        };

        drawerLayout.setDrawerListener(toggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Activamos el toggle con el icono
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    public void editPass(View v){
        nuevapass = ((EditText) findViewById(R.id.txtPass)).getText().toString();
        String passConfirm =  ((EditText) findViewById(R.id.txtPassConfirm)).getText().toString();
        if (nuevapass.isEmpty() || passConfirm.isEmpty()){
            Intent i= new Intent(this, Dialog.class);
            i.putExtra("message", "Por favor complete todos los campos");
            startActivity(i);
        } else {
            if (nuevapass.equals(passConfirm)) {
                loadEditPass();
            } else {
                Intent i = new Intent(this, Dialog.class);
                i.putExtra("message", "Las contraseñas no coinciden");
                startActivity(i);
            }
        }
    }

    private void loadEditPass(){
        SecurePreferences preferences = new SecurePreferences(getApplication(), "my-preferences", "BusesLepCordoba", true);
        dni = Integer.valueOf(preferences.getString("dni"));
        email = preferences.getString("email");
        pass = preferences.getString("pass");
        asyncCallerModificarContrasena= new AsyncCallerModificarContrasena(this);
        asyncCallerModificarContrasena.execute();
    }

    private class AsyncCallerModificarContrasena extends AsyncTask<String, Void, Pair<String,ArrayList<Map<String,Object>>> > {
        ProgressDialog pdLoading = new ProgressDialog(EditPass.this);
        Context context; //contexto para largar la activity aca adentro

        private AsyncCallerModificarContrasena(Context context) {
            this.context = context.getApplicationContext();
            pdLoading.setCancelable(false);

        }

        @Override
        protected Pair<String,ArrayList<Map<String,Object>>> doInBackground(String... params) {
            return new Pair("resultado", WebServices.CallModificarContraseña(dni, email, pass, nuevapass, getApplicationContext()));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setTitle("Por favor, espere.");
            pdLoading.setMessage("Cambiando contraseña");
            pdLoading.show();
        }


        @Override
        protected void onPostExecute(Pair<String,ArrayList<Map<String,Object>>> result) {
            if (result==null || result.second.isEmpty())
                Toast.makeText(getBaseContext(), "No se ha podido editadar la contraseña ", Toast.LENGTH_LONG).show();
                //this method will be running on UI <></>hread
            else{
                for (Map<String,Object> m: result.second){
                    if (m.containsKey("ret")){
                        if (((String) m.get("ret")).equals("1")){
                            Toast.makeText(getApplicationContext(), "Contraseña editada", Toast.LENGTH_LONG).show();
                            SecurePreferences preferences = new SecurePreferences(getApplication(), "my-preferences", "BusesLepCordoba", true);
                            preferences.put("pass", nuevapass);
                            finish();
                        } else {
                            Intent i= new Intent(EditPass.this, Dialog.class);
                            i.putExtra("message", "No se ha podido editadar la contraseña");
                            startActivity(i);
                        }
                    }
                }
            }
            pdLoading.dismiss();
        }
    }
}
