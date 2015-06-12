package com.tecpro.buseslep.search_scheludes.schedule;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tecpro.buseslep.Login;
import com.tecpro.buseslep.PurchaseDetails;
import com.tecpro.buseslep.R;
import com.tecpro.buseslep.ReserveDetails;
import com.tecpro.buseslep.search_scheludes.ChooseDate;
import com.tecpro.buseslep.utils.SecurePreferences;
import com.tecpro.buseslep.webservices.WebServices;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class SummarySchedules extends Activity {

    //textview para mostrar lo de la ida
    private TextView departTimeGo;
    private TextView departDateGo;
    private TextView arrivTimeGo;
    private TextView arrivtDateGo;

    //textViews para mostrar la vuelta
    private TextView departTimeReturn;
    private TextView departDateReturn;
    private TextView arrivTimeReturn;
    private TextView arrivtDateReturn;

    //numero de tickets
    private TextView numberTickets;

    private TextView descriptionGo;
    private TextView descriptionReturn;
    private TextView price;

    private String codeGo;
    private String codeReturn;
    private String bundleDepartTimeGo;
    private String bundleDepartDateGo;
    private String bundleDepartTimeRet;
    private String bundleDepartDateRet ;
    private String bundleCityOrigin;
    private String bundleCityDestiny;

    //menu
    private DrawerLayout drawerLayout;
    private ListView drawer;
    private ActionBarDrawerToggle toggle;
    private static final String[] opciones = {"OPCION UNO","OPCION DOS", "OPCION 3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_schedules);
        loadMenuOptions();
        departTimeGo = (TextView) findViewById(R.id.txt_depart_time_go);
        departDateGo = (TextView) findViewById(R.id.txt_depart_date_go);
        arrivTimeGo = (TextView) findViewById(R.id.txt_arriv_time_go);
        arrivtDateGo = (TextView) findViewById(R.id.txt_arriv_date_go);

        departTimeReturn = (TextView) findViewById(R.id.txt_depart_time_return);
        departDateReturn = (TextView) findViewById(R.id.txt_depart_date_return);
        arrivTimeReturn = (TextView) findViewById(R.id.txt_arriv_time_return);
        arrivtDateReturn = (TextView) findViewById(R.id.txt_arriv_date_return);
        numberTickets = (TextView) findViewById(R.id.txt_number_tickets);
        descriptionGo = (TextView) findViewById(R.id.txt_description_go);
        descriptionReturn = (TextView) findViewById(R.id.txt_description_return);
        price= (TextView) findViewById(R.id.txt_price);
        Bundle bundle = getIntent().getExtras();

        codeGo= bundle.getString("codeGo", "-1");
        codeReturn= bundle.getString("codeReturn", "-1");

        bundleDepartTimeGo = bundle.getString("departTimeGo","");
        bundleDepartDateGo = bundle.getString("departDateGo","");
        bundleDepartTimeRet = bundle.getString("departTimeReturn","");
        bundleDepartDateRet = bundle.getString("departDateReturn","");
        bundleCityOrigin=bundle.getString("Origin","");
        bundleCityDestiny=bundle.getString("Destiny","");

        departTimeGo.setText(bundle.getString("departTimeGo",""));
        departDateGo.setText(bundle.getString("departDateGo",""));
        arrivTimeGo.setText(bundle.getString("arrivTimeGo", ""));
        arrivtDateGo.setText(bundle.getString("arrivDateGo",""));

        departTimeReturn.setText(bundle.getString("departTimeReturn", ""));
        departDateReturn.setText(bundle.getString("departDateReturn", ""));
        arrivTimeReturn.setText(bundle.getString("arrivTimeReturn", ""));
        arrivtDateReturn.setText(bundle.getString("arrivDateReturn", ""));
        descriptionReturn.setText(bundle.getString("Destiny","")+" - "+ bundle.getString("Origin",""));

        if(codeReturn=="-1") {
            descriptionReturn.setText("");
            ((TextView) findViewById(R.id.txt_flecha)).setVisibility(View.INVISIBLE);
        }

        numberTickets.setText(bundle.getString("numberTickets",""));
        descriptionGo.setText(bundle.getString("Origin","")+" - "+ bundle.getString("Destiny",""));
        price.setText("$ "+bundle.getString("price",""));


    }

    private void loadMenuOptions(){
        // Rescatamos el Action Bar y activamos el boton Home
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

        // Declarar e inicializar componentes para el Navigation Drawer
        drawer = (ListView) findViewById(R.id.options_summary_schedules);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_summary_schedules);

        // Declarar adapter y eventos al hacer click
        drawer.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, opciones));

        drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // Toast.makeText(SearchScheludes.this, "Pulsado: " + opciones[arg2], Toast.LENGTH_SHORT).show();


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

    /**
     * click en boton reserva
     * @param v
     */
    public void onClickReserve(View v){
        SecurePreferences preferences = new SecurePreferences(getApplication(), "my-preferences", "BusesLepCordoba", true);
        Intent i;
        if (preferences.getString("login") != null ) {
            if (preferences.getString("login").equals("true")) {
                i =  new Intent(this, ReserveDetails.class);
            } else {
                i =  new Intent(this, Login.class);
                i.putExtra("next","reserve");
            }
        } else {
            i =  new Intent(this, Login.class);
            i.putExtra("next","reserve");
        }
        i.putExtra("city_from",bundleCityOrigin);
        i.putExtra("city_to",bundleCityDestiny);
        i.putExtra("arrival_date1",bundleDepartDateGo);
        i.putExtra("arrival_hour1",bundleDepartTimeGo);
        i.putExtra("arrival_date2",bundleDepartDateRet);
        i.putExtra("arrival_hour2",bundleDepartTimeRet);
        i.putExtra("cant_tickets",numberTickets.getText());
        i.putExtra("roundtrip",Integer.valueOf(codeReturn));
        startActivity(i);

    }

    /**
     * click en boton compra
     * @param v
     */
    public void onClickBuy(View v){
        SecurePreferences preferences = new SecurePreferences(getApplication(), "my-preferences", "BusesLepCordoba", true);
        Intent i;
        if (preferences.getString("login") != null ) {
            if (preferences.getString("login").equals("true")) {
                i =  new Intent(this, PurchaseDetails.class);
            } else {
                i =  new Intent(this, Login.class);
                i.putExtra("next","purchase");
            }
        } else {
            i =  new Intent(this, Login.class);
            i.putExtra("next","purchase");
        }
        i.putExtra("city_from",bundleCityOrigin);
        i.putExtra("city_to",bundleCityDestiny);
        i.putExtra("arrival_date1",bundleDepartDateGo);
        i.putExtra("arrival_hour1",bundleDepartTimeGo);
        i.putExtra("arrival_date2",bundleDepartDateRet);
        i.putExtra("arrival_hour2",bundleDepartTimeRet);
        i.putExtra("cant_tickets",numberTickets.getText());
        i.putExtra("roundtrip",Integer.valueOf(codeReturn));

        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case 1:
                numberTickets.setText((data.getStringExtra("number")));
                break;
        }
    }

    public void clickNumberTicktes(View v){
        int requestCode=1;
        Intent intent = new Intent(this, ChooseNumberTickets.class);//lanzo actividad de elegir fecha dependiendo de si es ida o vuelta
        startActivityForResult(intent, requestCode);
    }



    /**
     * el primer atributo que es String, son los nombres de los metodos que quiero llamar, lo hardcodeo con 1 solo atributo que es el nombre
     * del metodo así lo corro
     */
 /*   private class AsyncCallerSchedules extends AsyncTask<String, Void, Pair<String,ArrayList<Map<String,Object>>> > {
        ProgressDialog pdLoading = new ProgressDialog(SearchScheludes.this);
        Context context; //contexto para largar la activity aca adentro

        private AsyncCallerSchedules(Context context) {
            this.context = context.getApplicationContext();
            pdLoading.setCancelable(false);

        }

        @Override
        protected Pair<String,ArrayList<Map<String,Object>>> doInBackground(String... params) {
            if(params[0]=="go") {
                String[] aux = bundleDepartDateGo.split("/");
                String dateGo= aux[2]+aux[1]+aux[0];
                return new Pair(params[0], WebServices.getSchedules(Integer.valueOf(codeGo), Integer.valueOf(codeReturn), dateGo, getApplicationContext()));
            }
            else {
                String[] aux = bundleDepartDateRet.split("/");
                String dateReturn= aux[2]+aux[1]+aux[0];
                return new Pair(params[0], WebServices.getSchedules(Integer.valueOf(codeReturn), Integer.valueOf(codeGo), dateReturn, getApplicationContext()));
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setTitle("Por favor, espere.");
            pdLoading.setMessage("Obteniendo horarios");
            pdLoading.show();
        }


        @Override
        protected void onPostExecute(Pair<String,ArrayList<Map<String,Object>>> result) {
            if (result==null || result.second.isEmpty())
                Toast.makeText(getBaseContext(), "No se han encontrado horarios ", Toast.LENGTH_SHORT).show();
                //this method will be running on UI thread
            else{
                ArrayList<Map<String,Object>> schedules= result.second;
                Intent i = new Intent(context, ScheduleSearch.class);
                i.putExtra("schedules", schedules);
                int codeResult=-1;
                switch (result.first){
                    case "go":
                        codeResult=3;
                        i.putExtra("departCity",Integer.valueOf(codeGo));
                        i.putExtra("arrivCity",cityDestiny);
                        i.putExtra("goOrReturn","Ida");
                        break;
                    case "return":
                        codeResult=4;
                        i.putExtra("departCity",cityDestiny);
                        i.putExtra("arrivCity",cityOrigin);
                        i.putExtra("goOrReturn","Vuelta");
                        break;
                }
                startActivityForResult(i,codeResult);
            }
            pdLoading.dismiss();
        }
    }
    */
}
