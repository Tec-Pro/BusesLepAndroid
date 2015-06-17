package com.tecpro.buseslep.search_scheludes.schedule;

import android.app.ActionBar;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tecpro.buseslep.LastSearches;
import com.tecpro.buseslep.Login;
import com.tecpro.buseslep.PurchaseDetails;
import com.tecpro.buseslep.R;
import com.tecpro.buseslep.ReserveDetails;
import com.tecpro.buseslep.search_scheludes.ChooseDate;
import com.tecpro.buseslep.search_scheludes.SearchScheludes;
import com.tecpro.buseslep.utils.SecurePreferences;
import com.tecpro.buseslep.webservices.WebServices;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class SummarySchedules extends Activity {

    private AsyncCallerSchedules asyncCallerSchedules;

    //textview para mostrar lo de la ida
    private TextView departTimeGo;
    private TextView departDateGo;
    private TextView arrivTimeGo;
    private TextView arrivtDateGo;
    private static String priceGo;
    private static String priceGoRet;

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
    private String bundleIdEnterpriseGo;
    private String bundleIdEnterpriseRet;
    private String bundlePriceGo;
    private String bundlePriceGoRet;
    private String bundleNumberTickets;

    private String bundleCityOrigin;
    private String bundleCityDestiny;
    private int idCityOrigin;
    private int idCityDestiny;

    //menu
    private DrawerLayout drawerLayout;
    private ListView drawer;
    private ActionBarDrawerToggle toggle;
    private static final String[] opciones = {"Inicio","Últimas búsquedas"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_schedules);
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

        bundleIdEnterpriseGo= bundle.getString("idEnterpriseGo");
        bundleIdEnterpriseRet= bundle.getString("idEnterpriseRet", "-1");

        idCityOrigin= bundle.getInt("codeCityOrigin");
        idCityDestiny= bundle.getInt("codeCityDestiny");

        codeGo= bundle.getString("codeGo", "-1");
        codeReturn= bundle.getString("codeReturn", "-1");

        bundleDepartTimeGo = bundle.getString("departTimeGo","");
        bundleDepartDateGo = bundle.getString("departDateGo","");
        bundleDepartTimeRet = bundle.getString("departTimeReturn","");
        bundleDepartDateRet = bundle.getString("departDateReturn","");
        bundleCityOrigin=bundle.getString("Origin","");
        bundleCityDestiny=bundle.getString("Destiny","");
        bundleNumberTickets = bundle.getString("numberTickets","");

        departTimeGo.setText(bundle.getString("departTimeGo",""));
        departDateGo.setText(bundle.getString("departDateGo",""));
        arrivTimeGo.setText(bundle.getString("arrivTimeGo", ""));
        arrivtDateGo.setText(bundle.getString("arrivDateGo",""));

        departTimeReturn.setText(bundle.getString("departTimeReturn", ""));
        departDateReturn.setText(bundle.getString("departDateReturn", ""));
        arrivTimeReturn.setText(bundle.getString("arrivTimeReturn", ""));
        arrivtDateReturn.setText(bundle.getString("arrivDateReturn", ""));
        descriptionReturn.setText(bundle.getString("Destiny","")+" - "+ bundle.getString("Origin",""));

        bundlePriceGo= bundle.getString("priceGo");
        bundlePriceGoRet= bundle.getString("priceGoRet");
        if(codeReturn=="-1") {
            descriptionReturn.setText("");
            ((TextView) findViewById(R.id.txt_flecha)).setVisibility(View.INVISIBLE);
            price.setText("$ " + Integer.valueOf(bundlePriceGo)*Integer.valueOf(bundleNumberTickets));

        }
        else{
            price.setText("$ " + Integer.valueOf(bundlePriceGoRet)*Integer.valueOf(bundleNumberTickets));
        }

        numberTickets.setText(bundleNumberTickets);
        descriptionGo.setText(bundle.getString("Origin","")+" - "+ bundle.getString("Destiny",""));
        price.setText("$ "+bundle.getString("priceGo",""));


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
        drawer.setAdapter(new ArrayAdapter<String>(this,R.layout.element_menu, R.id.list_content, opciones));

        drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                switch (arg2){
                    case 0://presione recargar ciudades
                        Intent i = new Intent(SummarySchedules.this, SearchScheludes.class);
                        startActivity(i);
                        break;
                    case 1:
                        loadLastSearches();
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

    private void loadLastSearches(){
        Intent i =  new Intent(this, LastSearches.class);
        startActivity(i);
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
        i.putExtra("cant_tickets",bundleNumberTickets);
        i.putExtra("roundtrip",Integer.valueOf(codeReturn));
        i.putExtra("IDEmpresaIda",Integer.valueOf(bundleIdEnterpriseGo));
        i.putExtra("IDEmpresaVuelta",Integer.valueOf(bundleIdEnterpriseRet));
        i.putExtra("CodHorarioIda", codeGo);
        i.putExtra("CodHorarioVuelta", codeReturn);
        i.putExtra("IDDestinoIda", idCityOrigin);
        i.putExtra("IDDestinoVuelta", idCityDestiny);
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
        i.putExtra("cant_tickets",bundleNumberTickets);
        i.putExtra("roundtrip",Integer.valueOf(codeReturn));
        i.putExtra("IDEmpresaIda",Integer.valueOf(bundleIdEnterpriseGo));
        i.putExtra("IDEmpresaVuelta",Integer.valueOf(bundleIdEnterpriseRet));
        i.putExtra("CodHorarioIda", codeGo);
        i.putExtra("CodHorarioVuelta", codeReturn);
        i.putExtra("IDDestinoIda", idCityOrigin);
        i.putExtra("IDDestinoVuelta", idCityDestiny);
        startActivity(i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case 1:
                bundleNumberTickets =data.getStringExtra("number");
                numberTickets.setText(bundleNumberTickets);
                if(codeReturn=="-1")
                    price.setText("$ " + Integer.valueOf(bundlePriceGo)*Integer.valueOf(bundleNumberTickets));
                else
                    price.setText("$ " + Integer.valueOf(bundlePriceGoRet)*Integer.valueOf(bundleNumberTickets));
                break;
            case 2://retorno de seleccion de horario ida
                codeGo= data.getStringExtra("codigo");
                departTimeGo.setText(data.getStringExtra("departTime"));
                departDateGo.setText(data.getStringExtra("departDate"));
                arrivTimeGo.setText(data.getStringExtra("arrivTime"));
                arrivtDateGo.setText(data.getStringExtra("arrivDate"));
                bundleDepartTimeGo =data.getStringExtra("departTime");
                bundleDepartDateGo =data.getStringExtra("departDate");

                break;
            case 3: //retorno de la seleccion de horario vuelta
                codeReturn= data.getStringExtra("codigo");
                departDateReturn.setText(data.getStringExtra("departDate"));
                departTimeReturn.setText(data.getStringExtra("departTime"));
                arrivtDateReturn.setText(data.getStringExtra("arrivDate"));
                arrivTimeReturn.setText(data.getStringExtra("arrivTime"));
                bundleDepartTimeRet =data.getStringExtra("departTime");
                bundleDepartDateRet =data.getStringExtra("departDate");
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
    private class AsyncCallerSchedules extends AsyncTask<String, Void, Pair<String,ArrayList<Map<String,Object>>> > {
        ProgressDialog pdLoading = new ProgressDialog(SummarySchedules.this);
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
                Map<String,String> priceMap= WebServices.getPrice(idCityOrigin, idCityDestiny, getApplicationContext());
                priceGo=  priceMap.get("priceGo");
                priceGoRet=  priceMap.get("priceGoRet");
                return new Pair(params[0], WebServices.getSchedules(idCityOrigin, idCityDestiny, dateGo, getApplicationContext()));
            }
            else {
                String[] aux = bundleDepartDateRet.split("/");
                String dateReturn= aux[2]+aux[1]+aux[0];
                return new Pair(params[0], WebServices.getSchedules(idCityDestiny, idCityOrigin, dateReturn, getApplicationContext()));
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
                i.putExtra("priceGo",priceGo);
                i.putExtra("priceGoRet",priceGoRet);
                int codeResult=-1;
                switch (result.first){
                    case "go":
                        codeResult=2;
                        i.putExtra("departCity",bundleCityOrigin);
                        i.putExtra("arrivCity",bundleCityDestiny);
                        i.putExtra("goOrReturn","Ida");
                        break;
                    case "return":
                        codeResult=3;
                        i.putExtra("departCity",bundleCityDestiny);
                        i.putExtra("arrivCity",bundleCityOrigin);
                        i.putExtra("goOrReturn","Vuelta");
                        break;
                }
                startActivityForResult(i,codeResult);
            }
            pdLoading.dismiss();
        }
    }

    public void clickChangeDateGo(View v){
        asyncCallerSchedules= new AsyncCallerSchedules(this);
        asyncCallerSchedules.execute("go");
    }

    public void clickChangeDateRet(View v){
        if(codeReturn!="-1") {
            asyncCallerSchedules = new AsyncCallerSchedules(this);
            asyncCallerSchedules.execute("return");
        }
    }

}
