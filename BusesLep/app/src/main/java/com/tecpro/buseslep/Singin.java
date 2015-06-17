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
import android.widget.Spinner;
import android.widget.Toast;

import com.tecpro.buseslep.search_scheludes.SearchScheludes;
import com.tecpro.buseslep.utils.SecurePreferences;
import com.tecpro.buseslep.webservices.WebServices;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by jacinto on 6/4/15.
 */
public class Singin extends Activity {
    private DrawerLayout drawerLayout;
    private ListView drawer;
    private ActionBarDrawerToggle toggle;
    private static final String[] opciones = {"Inicio"};
    private static int user;
    private static String userS;
    private static String pass;
    private static String nombre;
    private static String ape;
    private static String email;
    private static AsyncCallerLogin asyncCallerLogin;
    private static AsyncCallerSignin asyncCallerSignin;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.singin);
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
        drawer = (ListView) findViewById(R.id.options_signin);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_signin);

        // Declarar adapter y eventos al hacer click
        drawer.setAdapter(new ArrayAdapter<String>(this, R.layout.element_menu, R.id.list_content, opciones));

        drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // Toast.makeText(SearchScheludes.this, "Pulsado: " + opciones[arg2], Toast.LENGTH_SHORT).show();
                switch (arg2) {
                    case 0://presione recargar ciudades
                        Intent i = new Intent(Singin.this, SearchScheludes.class);
                        startActivity(i);
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

    public void singin(View v){
        String name =  ((EditText) findViewById(R.id.txtName)).getText().toString();
        String surname =  ((EditText) findViewById(R.id.txtSurname)).getText().toString();
        String email =  ((EditText) findViewById(R.id.txtEmail)).getText().toString();
        String dni =  ((EditText) findViewById(R.id.textDNI)).getText().toString();
        String pass = ((EditText) findViewById(R.id.txtPass)).getText().toString();
        String passConfirm =  ((EditText) findViewById(R.id.txtPassConfirm)).getText().toString();
        if (pass.equals(passConfirm)) {
            SecurePreferences preferences = new SecurePreferences(getApplication(), "my-preferences", "BusesLepCordoba", true);
            preferences.put("dni", dni);
            preferences.put("pass", pass);
            preferences.put("apellido", surname);
            preferences.put("nombre", name);
            preferences.put("email", email);
            preferences.put("login", "false");
            loadSingin(Integer.valueOf(dni), pass, name, surname, email);
        } else {
            Toast.makeText(getApplicationContext(), "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
        }
    }

    private void loadSingin(int u, String p, String n, String a, String e){
        user = u;
        pass = p;
        nombre = n;
        ape = a;
        email = e;
        asyncCallerSignin= new AsyncCallerSignin(this);
        asyncCallerSignin.execute();
    }

    private class AsyncCallerSignin extends AsyncTask<String, Void, Pair<String,ArrayList<Map<String,Object>>> > {
        ProgressDialog pdLoading = new ProgressDialog(Singin.this);
        Context context; //contexto para largar la activity aca adentro

        private AsyncCallerSignin(Context context) {
            this.context = context.getApplicationContext();
            pdLoading.setCancelable(false);

        }

        @Override
        protected Pair<String,ArrayList<Map<String,Object>>> doInBackground(String... params) {
            return new Pair("resultado", WebServices. CallRegistrarUsuario(user, pass, nombre, ape, email, getApplicationContext()));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setTitle("Por favor, espere.");
            pdLoading.setMessage("Creando Cuenta");
            pdLoading.show();
        }


        @Override
        protected void onPostExecute(Pair<String,ArrayList<Map<String,Object>>> result) {
            if (result==null || result.second.isEmpty())
                Toast.makeText(getBaseContext(), "No se ha podido crear la cuenta ", Toast.LENGTH_SHORT).show();
                //this method will be running on UI <></>hread
            else{
                for (Map<String,Object> m: result.second){
                    if (m.containsKey("ret")){
                        if (((String) m.get("ret")).equals("-1")){
                            Toast.makeText(getBaseContext(), "No se ha podido crear la cuenta ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Cuenta creada", Toast.LENGTH_LONG).show();
                            try {
                                wait(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            loadLogin(String.valueOf(user), pass);
                        }
                    }
                }
            pdLoading.dismiss();
        }
    }
    }

    private void loadLogin(String u, String p){
        userS = u;
        pass = p;
        asyncCallerLogin= new AsyncCallerLogin(this);
        asyncCallerLogin.execute();
    }

    private class AsyncCallerLogin extends AsyncTask<String, Void, Pair<String,ArrayList<Map<String,Object>>> > {
        ProgressDialog pdLoading = new ProgressDialog(Singin.this);
        Context context; //contexto para largar la activity aca adentro

        private AsyncCallerLogin(Context context) {
            this.context = context.getApplicationContext();
            pdLoading.setCancelable(false);

        }

        @Override
        protected Pair<String,ArrayList<Map<String,Object>>> doInBackground(String... params) {
            return new Pair("resultado", WebServices.callLogin(userS, pass, getApplicationContext()));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setTitle("Por favor, espere.");
            pdLoading.setMessage("Iniciando sesion");
            pdLoading.show();
        }


        @Override
        protected void onPostExecute(Pair<String,ArrayList<Map<String,Object>>> result) {
            if (result==null || result.second.isEmpty())
                Toast.makeText(getBaseContext(), "No se ha podido iniciar sesion ", Toast.LENGTH_SHORT).show();
                //this method will be running on UI <></>hread
            else{
                Toast.makeText(getApplicationContext(), "Sesion iniciada", Toast.LENGTH_SHORT).show();
                SecurePreferences preferences = new SecurePreferences(getApplication(), "my-preferences", "BusesLepCordoba", true);
                preferences.put("login", "true");
            }
            Intent i;
            Bundle bundle = getIntent().getExtras();
            if (bundle.getString("next").equals("main")) {
                i = new Intent(Singin.this, SearchScheludes.class);
            } else {
                if (bundle.getString("next").equals("purchase")) {
                    i = new Intent(Singin.this, PurchaseDetails.class);
                } else {
                    i = new Intent(Singin.this, ReserveDetails.class);
                }
                i.putExtra("city_from",bundle.getString("city_from"));
                i.putExtra("city_to",bundle.getString("city_to"));
                i.putExtra("arrival_date1",bundle.getString("arrival_date1"));
                i.putExtra("arrival_hour1",bundle.getString("arrival_hour1"));
                i.putExtra("arrival_date2",bundle.getString("arrival_date2"));
                i.putExtra("arrival_hour2",bundle.getString("arrival_hour2"));
                i.putExtra("cant_tickets", bundle.getString("cant_tickets"));
                i.putExtra("roundtrip", bundle.getInt("roundtrip"));
            }
            startActivity(i);
            pdLoading.dismiss();
        }
    }

}
