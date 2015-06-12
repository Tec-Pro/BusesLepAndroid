package com.tecpro.buseslep;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

    private DrawerLayout drawerLayout;
    private ListView drawer;
    private ActionBarDrawerToggle toggle;
    private static final String[] opciones = {"Inicio"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        loadMenuOptions();
    }
    private void loadMenuOptions(){
        // Rescatamos el Action Bar y activamos el boton Home
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        // Declarar e inicializar componentes para el Navigation Drawer
        drawer = (ListView) findViewById(R.id.options_login);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_login);

        // Declarar adapter y eventos al hacer click
        drawer.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, opciones));

        drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // Toast.makeText(SearchScheludes.this, "Pulsado: " + opciones[arg2], Toast.LENGTH_SHORT).show();
                switch (arg2){
                    case 0://presione recargar ciudades
                        Intent i = new Intent(Login.this, SearchScheludes.class);
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
