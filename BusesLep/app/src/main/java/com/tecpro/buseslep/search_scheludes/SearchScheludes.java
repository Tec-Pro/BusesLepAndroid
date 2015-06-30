package com.tecpro.buseslep.search_scheludes;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.support.v7.internal.widget.AdapterViewCompat;
import android.text.InputType;
import android.util.Pair;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tecpro.buseslep.Dialog;
import com.tecpro.buseslep.EditPass;
import com.tecpro.buseslep.EditProfile;
import com.tecpro.buseslep.LastSearches;
import com.tecpro.buseslep.Login;
import com.tecpro.buseslep.R;
import com.tecpro.buseslep.batabase.DataBaseHelper;
import com.tecpro.buseslep.my_purchases.MyPurchases;
import com.tecpro.buseslep.my_reserves.MyReserves;
import com.tecpro.buseslep.search_scheludes.schedule.ChooseNumberTickets;
import com.tecpro.buseslep.search_scheludes.schedule.ScheduleSearch;
import com.tecpro.buseslep.search_scheludes.schedule.SummarySchedules;
import com.tecpro.buseslep.search_scheludes.select_city.AdaptatorCity;
import com.tecpro.buseslep.search_scheludes.select_city.ChooseCity;
import com.tecpro.buseslep.utils.PreferencesUsing;
import com.tecpro.buseslep.webservices.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class SearchScheludes extends Activity  {

    private static TextView txtViewCityGo;
    private static TextView txtViewCityDestiny;
    private ArrayList<Map<String,Object>> departureCities; //los nombres de ciudades de origen
    private ArrayList<Map<String,Object>> destinationCities; //los nombres de ciudades de origen
    private static TextView txtNumerTickets;
    private static CheckBox chkRoundTrip;
    //UI References
    private static TextView fromDateEtxt;
    private static TextView toDateEtxt;
    //dia mes y año de ida
    private int dayGo=-1;
    private int monthGo=-1;
    private int yearGo=-1;
    //dia mes y año de vuelta
    private int dayReturn=-1;
    private int monthReturn=-1;
    private int yearReturn=-1;

    //datos para la busqueda
    private static Integer idOrigin=-1; //id de origen
    private static Integer idDestiny=-1; //id de destino
    private static String cityOrigin;
    private static String cityDestiny;
    private static Integer numberOfTickets=-1;
    private static String dateGo; //string para la fecha de ida en formato 20150605
    private static String dateReturn; //string para la fecha de vuelta en formato 20150605
    private AsyncCallerCities asyncCallerCities;
    private AsyncCallerSchedules asyncCallerSchedules;
    private AsyncCallerMisReservas asyncCallerMisReservas;
    private AsyncCallerMisCompras asyncCallerMisCompras;

    private static String priceGo;
    private static String priceGoRet;

    private ArrayList<Map<String,Object>> schedules; //lista con todos los horarios, la misma la uso para ida y  para vuelta
    private String codeGoSchedule; //tengo el codigo del horario para la reserva
    private String codeReturnSchedule; //el codigo del horario apra la reserva pero de la vuelta
    private String codeEnterpriseGo;
    private String codeEnterpriseRet;
    //datos para la ida
    private String departTimeGo;
    private String departDateGo;
    private String arrivTimeGo;
    private String arrivDateGo;
    private String idDestinoIda;
    private String idDestinoVuelta;
    //datos para la vuelta
    private String departTimeReturn;
    private String departDateReturn;
    private String arrivTimeReturn;
    private String arrivDateReturn;
    private DataBaseHelper dbh; //databasehelper para la db

    private PreferencesUsing preferences;
    private static String dniLogged=null;

    //menu
    private DrawerLayout drawerLayout;
    private ListView drawer;
    private ActionBarDrawerToggle toggle;
    private static final String[] opciones = {"Recargar ciudades", "Últimas Búsquedas","Editar Perfil","Cambiar Contraseña" ,"Mis Reservas","Mis Compras", "Cerrar Sesion"};
    private static final String[] optionsNotSesion= {"Recargar ciudades", "Últimas Búsquedas"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_scheludes);
        preferences= new PreferencesUsing(this);
        preferences.init();
        if(preferences.isOnline()){
            findViewById(R.id.btnLogin).setVisibility(View.GONE);
            dniLogged= preferences.getDni();
        }else{
            dniLogged = null;
        }

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
        findViewsById();
        loadOrigin();
        Calendar cal = Calendar.getInstance();
        dayGo = cal.get(Calendar.DAY_OF_MONTH);
        monthGo = cal.get(Calendar.MONTH)+1;
        yearGo = cal.get(Calendar.YEAR);
        dayReturn = cal.get(Calendar.DAY_OF_MONTH);
        monthReturn = cal.get(Calendar.MONTH)+1;
        yearReturn = cal.get(Calendar.YEAR);
        dbh= new DataBaseHelper(this);
        clickGoes(null);
    }

    private void loadMenuOptions(){
        // Rescatamos el Action Bar y activamos el boton Home
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        // Declarar e inicializar componentes para el Navigation Drawer
        drawer = (ListView) findViewById(R.id.options_search_schedules);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_search_schedules);

        // Declarar adapter y eventos al hacer click
        if(preferences.isOnline())
            drawer.setAdapter(new ArrayAdapter<String>(this,R.layout.element_menu, R.id.list_content, opciones));
        else
            drawer.setAdapter(new ArrayAdapter<String>(this, R.layout.element_menu, R.id.list_content, optionsNotSesion));
        drawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                if(preferences.isOnline()) { //si el usuario tiene una sesion
                    switch (arg2) {
                        case 0://presione recargar ciudades
                            loadOrigin();
                            idOrigin = -1;
                            loadDestiny();
                            break;
                        case 1:
                            loadLastSearches();
                            break;
                        case 2:
                            editProfile();
                            break;
                        case 3:
                            editPass();
                            break;
                        case 4:
                            asyncCallerMisReservas= new AsyncCallerMisReservas(getApplicationContext());
                            asyncCallerMisReservas.execute();
                            break;
                        case 5:
                            asyncCallerMisCompras= new AsyncCallerMisCompras(getApplicationContext());
                            asyncCallerMisCompras.execute();
                            break;
                        case 6:
                            preferences.logout();
                            findViewById(R.id.btnLogin).setVisibility(View.VISIBLE);
                            dniLogged=null;
                            break;
                    }
                }
                else{
                    switch (arg2) {
                        case 0://presione recargar ciudades
                            loadOrigin();
                            idOrigin = -1;
                            loadDestiny();
                            break;
                        case 1:
                            loadLastSearches();
                            break;
                    }
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
                //getActionBar().setTitle(getResources().getString(R.string.app_name));
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View drawerView) {
                // Drawer abierto
                //getActionBar().setTitle(R.string.options);
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

    private void loadLastSearches(){
        Intent i =  new Intent(this, LastSearches.class);
        startActivity(i);
    }

    private void editProfile(){
        Intent i =  new Intent(this, EditProfile.class);
        startActivity(i);
    }

    private void editPass(){
        Intent i =  new Intent(this, EditPass.class);
        startActivity(i);
    }

    private void loadOrigin(){
        asyncCallerCities= new AsyncCallerCities();
        asyncCallerCities.execute("getCities");
    }

    private void loadDestiny(){
        asyncCallerCities= new AsyncCallerCities();
        asyncCallerCities.execute("getDestinationCities");
    }




    /**
     * busco los txtView de fecha y los seteo
     */
    private  void findViewsById() {
        fromDateEtxt = (TextView) findViewById(R.id.txt_date_from);
        toDateEtxt = (TextView) findViewById(R.id.txt_date_to);
        txtViewCityGo = (TextView) findViewById(R.id.txt_city_origin);
        txtViewCityDestiny = (TextView) findViewById(R.id.txt_city_destiny);
        txtNumerTickets = (TextView) findViewById(R.id.txt_number_tickets);
        chkRoundTrip = (CheckBox) findViewById(R.id.chk_round_trip);

    }


    public void launchLogin(View v){
        Intent i =  new Intent(this, Login.class);
        i.putExtra("next", "main");
        startActivity(i);
    }


    public void clickDateGo(View v){
        int requestCode=1;
        String descriptionDate="Selección fecha de ida";
        Intent intent = new Intent(this, ChooseDate.class);//lanzo actividad de elegir fecha dependiendo de si es ida o vuelta
        intent.putExtra("day",dayGo);
        intent.putExtra("month", monthGo - 1);
        intent.putExtra("year",yearGo);
        intent.putExtra("description", descriptionDate);
        startActivityForResult(intent, requestCode);
    }


    public void clickDateReturn(View v){
        int requestCode=2;
        String descriptionDate="Selección fecha de vuelta";
        Intent intent = new Intent(this, ChooseDate.class);//lanzo actividad de elegir fecha dependiendo de si es ida o vuelta
        intent.putExtra("day",dayReturn);
        intent.putExtra("month",monthReturn-1);
        intent.putExtra("year",yearReturn);
        intent.putExtra("description", descriptionDate);
        startActivityForResult(intent, requestCode);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        switch (requestCode){
            case 1://retorno de seleccion de ida
                dayGo= data.getIntExtra("day", 1);
                monthGo= data.getIntExtra("month",1);
                yearGo= data.getIntExtra("year",2015);
                fromDateEtxt.setText(dayGo+"/"+monthGo+"/"+yearGo);
                String auxMonth=String.valueOf(monthGo);
                String auxDay=String.valueOf(dayGo);
                if(monthGo<10)
                    auxMonth= "0"+monthGo;//le agrego un cero adelate
                if(dayGo<10)
                    auxDay= "0"+dayGo;//le agrego un cero adelate
                dateGo=yearGo+""+auxMonth+""+auxDay;// es un entero casteado
                break;
            case 2: //retorno de seleccion de vuelta
                dayReturn= data.getIntExtra("day", 1);
                monthReturn= data.getIntExtra("month",1);
                yearReturn= data.getIntExtra("year",2015);
                toDateEtxt.setText(dayReturn+"/"+monthReturn+"/"+yearReturn);
                String auxMonthRet=String.valueOf(monthReturn);
                String auxDayRet=String.valueOf(dayReturn);
                if(monthReturn<10)
                    auxMonthRet= "0"+monthReturn;//le agrego un cero adelate
                if(dayReturn<10)
                    auxDayRet= "0"+dayReturn;//le agrego un cero adelate
                dateReturn=yearReturn+""+auxMonthRet+""+auxDayRet;// es un entero casteado
                break;
            case 3://retorno de seleccion de horario ida
                codeGoSchedule= data.getStringExtra("codigo");
                departDateGo = data.getStringExtra("departDate");
                departTimeGo = data.getStringExtra("departTime");
                arrivDateGo = data.getStringExtra("arrivDate");
                arrivTimeGo = data.getStringExtra("arrivTime");
                codeEnterpriseGo = data.getStringExtra("idEmpresa");
                idDestinoIda= data.getStringExtra("id_destino");
                //debo corroborar si es ida y vuelta, en caso de ser ida y vuelta debo largar la gui para elegir retorno
                if (chkRoundTrip.isChecked()) {
                    asyncCallerSchedules = new AsyncCallerSchedules(this);
                    asyncCallerSchedules.execute("return");
                }else{
                    launchBuyReserve(false);
                }
                break;
            case 4: //retorno de la seleccion de horario vuelta
                codeReturnSchedule= data.getStringExtra("codigo");
                departDateReturn = data.getStringExtra("departDate");
                departTimeReturn = data.getStringExtra("departTime");
                arrivDateReturn = data.getStringExtra("arrivDate");
                arrivTimeReturn = data.getStringExtra("arrivTime");
                codeEnterpriseRet = data.getStringExtra("idEmpresa");
                idDestinoVuelta= data.getStringExtra("id_destino");

                launchBuyReserve(true);
                break;
            case 5: //retorno de elegir el numero de pasajeros
                numberOfTickets = Integer.valueOf(data.getStringExtra("number"));
                txtNumerTickets.setText(String.valueOf(numberOfTickets));
                break;
            case 6: //retorno de elegir ciudad de origen
                idOrigin= data.getIntExtra("id", -1);
                cityOrigin = data.getStringExtra("name");
                txtViewCityGo.setText(cityOrigin);
                loadDestiny();
                break;
            case 7: //retorno de elegir ciudad de destino
                idDestiny= data.getIntExtra("id", -1);
                cityDestiny = data.getStringExtra("name");
                txtViewCityDestiny.setText(cityDestiny);
                break;

        }
    }

    /**
     * true si es ida y vuelta, lanza la vista de resumen
     * @param goReturn
     */
    private void launchBuyReserve(boolean goReturn){
        Intent i= new Intent(this, SummarySchedules.class);
        i.putExtra("departTimeGo",departTimeGo );
        i.putExtra("departDateGo",departDateGo );
        i.putExtra("arrivDateGo",arrivDateGo );
        i.putExtra("arrivTimeGo",arrivTimeGo );
        i.putExtra("numberTickets",numberOfTickets.toString() );
        i.putExtra("Origin",cityOrigin );
        i.putExtra("Destiny",cityDestiny );
        i.putExtra("codeGo",codeGoSchedule );
        i.putExtra("codeCityOrigin", idOrigin);
        i.putExtra("codeCityDestiny",idDestiny);
        i.putExtra("idEnterpriseGo", codeEnterpriseGo);
        i.putExtra("priceGo",priceGo);
        i.putExtra("priceGoRet",priceGoRet);
        i.putExtra("id_destino_ida", idDestinoIda);
        if(goReturn){
            i.putExtra("departTimeReturn",departTimeReturn );
            i.putExtra("departDateReturn",departDateReturn );
            i.putExtra("arrivDateReturn",arrivDateReturn );
            i.putExtra("arrivTimeReturn",arrivTimeReturn);
            i.putExtra("codeReturn",codeReturnSchedule );
            i.putExtra("idEnterpriseRet", codeEnterpriseRet);
            i.putExtra("id_destino_vuelta", idDestinoVuelta);
        }
        System.out.println(idDestinoIda);
        startActivity(i);


    }


    /**
     * cada vez que haga click en el box de ida y vuelta debe poner invisible el field de vuelta
     * @param v
     */
    public void clickGoes(View v){
        LinearLayout linearLayoutReturn = (LinearLayout) findViewById(R.id.linear_layout_to);// obtengo el linear layout que tiene la fecha de regreso
        if(chkRoundTrip.isChecked()) {
            linearLayoutReturn.setVisibility(View.VISIBLE); //seteo visible si esta seleccionado ida y vuelta
        }else{
            linearLayoutReturn.setVisibility(View.GONE); //la hago desaparecer si no esta seleccionado ida y vuelta

        }
    }

    public void clickSearch(View v){
        String error="";
        boolean err=false;
        Calendar cal = Calendar.getInstance();
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        int currentMonth = cal.get(Calendar.MONTH) + 1;
        int currentYear = cal.get(Calendar.YEAR);
        if(numberOfTickets==-1){
            error= error.concat(" seleccione cantidad de pasajes \n");
            err=true;//ocurrio un error
        }
        if(idOrigin==-1){
           error= error.concat(" seleccione ciudad de origen \n");
           err=true;//ocurrio un error
        }
        if(idDestiny==-1){
            error= error.concat(" seleccione ciudad de destino \n");
            err=true;//ocurrio un error
        }
        if (chkRoundTrip.isChecked()){
            if(toDateEtxt.getText().length()==0||
                    !dateIsValid(currentDay,currentMonth,currentYear,dayReturn,monthReturn,yearReturn)||
                    !dateIsValid(dayGo,monthGo,yearGo,dayReturn,monthReturn,yearReturn)) { //valido la fecha de regreso
                err=true;
                error=error.concat(" fecha de regreso incorrecta \n");
            }
        }
        //la forma crota de ver si elegi una fecha, me fijo si hay una F de 'F'echa
        if(fromDateEtxt.getText().length()==0||!dateIsValid(currentDay,currentMonth,currentYear,dayGo,monthGo,yearGo)) { //valido la fecha de regreso y que haya algo
            err=true;
            error=error.concat(" fecha de ida incorrecta \n");
        }
        if(!err) {
            //guardo la busqueda realizada en la base de datos
            String auxMonth=String.valueOf(monthGo);
            String auxDay=String.valueOf(dayGo);
            if(monthGo<10)
                 auxMonth= "0"+monthGo;//le agrego un cero adelate
            if(dayGo<10)
                 auxDay= "0"+dayGo;//le agrego un cero adelate
            dateGo=yearGo+""+auxMonth+""+auxDay;// es un entero casteado
            String auxDateReturn= null;
            boolean isRoundtrip= chkRoundTrip.isChecked();
            if(isRoundtrip){
                String auxMonthRet=String.valueOf(monthReturn);
                String auxDayRet=String.valueOf(dayReturn);
                if(monthReturn<10)
                    auxMonthRet= "0"+monthReturn;//le agrego un cero adelate
                if(dayReturn<10)
                    auxDayRet= "0"+dayReturn;//le agrego un cero adelate
                auxDateReturn= yearReturn+"-"+auxMonthRet+"-"+auxDayRet;
            }
            dbh.insert(cityOrigin,cityDestiny,idOrigin,idDestiny,yearGo+"-"+auxMonth+"-"+auxDay,auxDateReturn,numberOfTickets,isRoundtrip);
            asyncCallerSchedules= new AsyncCallerSchedules(this);
            asyncCallerSchedules.execute("go");

        }
        else
            Toast.makeText(getApplicationContext(),error,Toast.LENGTH_SHORT).show();
    }


    /**
     * retorna true si la fecha de hoy es menor que la fecha pasada por parametro
     * @return
     * @throws ParseException
     */
    private boolean dateIsValid(int day1,int month1,int year1, int day2, int month2, int year2) {
        /**Obtenemos las fechas enviadas en el formato a comparar*/
        try {

            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaDate1 = formateador.parse(day1 + "/" + month1 + "/" + year1);
            Date fechaDate2 = formateador.parse(day2 + "/" + month2 + "/" + year2);
            return fechaDate1.before(fechaDate2) ||fechaDate1.equals(fechaDate2);
        } catch (ParseException e) {
        }
        return false;
    }


    //con esto puedo mostrar un mensaje de carga

    /**
     * el primer atributo que es String, son los nombres de los metodos que quiero llamar, lo hardcodeo con 1 solo atributo que es el nombre
     * del metodo así lo corro
     */
    private class AsyncCallerCities extends AsyncTask<String, Void, Pair<String,List<String>> > {
        ProgressDialog pdLoading = new ProgressDialog(SearchScheludes.this);

        private AsyncCallerCities(){
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
        protected Pair<String,List<String>> doInBackground(String... params) {
            //dependiendo de que le paso por parametro, me fijo que hago
            switch (params[0]){
                case "getCities" :
                    departureCities=WebServices.getCities(getApplicationContext());
                    return new Pair("getCities",departureCities);
                case "getDestinationCities" :
                    destinationCities= WebServices.getDestinationCities(idOrigin, getApplicationContext());
                    return new Pair("getDestinationCities",destinationCities);
            }
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here


            return null;
        }

        @Override
        protected void onPostExecute(Pair<String,List<String>> result) {
            if(result.first == "getCities"){
                txtViewCityDestiny.setText("");
                txtViewCityGo.setText("");
            }
            if(result.first == "getDestinationCities"){
                txtViewCityDestiny.setText("");
            }
            pdLoading.dismiss();
        }

    }


    /**
     * el primer atributo que es String, son los nombres de los metodos que quiero llamar, lo hardcodeo con 1 solo atributo que es el nombre
     * del metodo así lo corro
     */
    private class AsyncCallerSchedules extends AsyncTask<String, Void, Pair<String,ArrayList<Map<String,Object>>> > {
        ProgressDialog pdLoading = new ProgressDialog(SearchScheludes.this);
        Context context; //contexto para largar la activity aca adentro

        private AsyncCallerSchedules(Context context) {
            this.context = context.getApplicationContext();
            pdLoading.setCancelable(false);

        }

        @Override
        protected Pair<String,ArrayList<Map<String,Object>>> doInBackground(String... params) {
            if(params[0]=="go") {
                Map<String,String> priceMap= WebServices.getPrice(idOrigin, idDestiny, getApplicationContext());
                priceGo=  priceMap.get("priceGo");
                priceGoRet=  priceMap.get("priceGoRet");
                return new Pair(params[0], WebServices.getSchedules(idOrigin, idDestiny, dateGo, dniLogged, getApplicationContext()));
            }else
                return new Pair(params[0],WebServices.getSchedules(idDestiny,idOrigin, dateReturn, dniLogged, getApplicationContext()));
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
                schedules= result.second;
                Intent i = new Intent(context, ScheduleSearch.class);
                i.putExtra("schedules", schedules);
                i.putExtra("priceGo",priceGo);
                i.putExtra("priceGoRet",priceGoRet);
                int codeResult=-1;
                switch (result.first){
                    case "go":
                        codeResult=3;
                        i.putExtra("departCity",cityOrigin);
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

    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void clickNumberTickets(View v){
        int requestCode=5;
        Intent intent = new Intent(this, ChooseNumberTickets.class);//lanzo actividad de elegir fecha dependiendo de si es ida o vuelta
        startActivityForResult(intent, requestCode);
    }

    public void clickCityOrigin(View v){
        int requestCode=6;
        Intent intent = new Intent(this, ChooseCity.class);//lanzo actividad de elegir fecha dependiendo de si es ida o vuelta
        intent.putExtra("cities",departureCities);
        startActivityForResult(intent, requestCode);
    }

    public void clickCityDestiny(View v){
        if(destinationCities==null || destinationCities.size()==0){
            Intent i= new Intent(this, Dialog.class);
            i.putExtra("message", "Primero seleccione una ciudad de destino");
            startActivity(i);
        }else {
            int requestCode = 7;
            Intent intent = new Intent(this, ChooseCity.class);//lanzo actividad de elegir fecha dependiendo de si es ida o vuelta
            intent.putExtra("cities", destinationCities);
            startActivityForResult(intent, requestCode);
        }
    }

    @Override
    public void onResume() { //actualiza la listview cada vez que regresa de otra activity
        super.onResume();
        if(preferences.isOnline()){
            findViewById(R.id.btnLogin).setVisibility(View.GONE);
            dniLogged= preferences.getDni();
        }else{
            dniLogged = null;
        }
        clickGoes(null);
        loadMenuOptions();
    }


    /**
     * el primer atributo que es String, son los nombres de los metodos que quiero llamar, lo hardcodeo con 1 solo atributo que es el nombre
     * del metodo así lo corro
     */
    private class AsyncCallerMisReservas extends AsyncTask<String, Void, ArrayList<Map<String,Object>> > {
        ProgressDialog pdLoading = new ProgressDialog(SearchScheludes.this);
        Context context; //contexto para largar la activity aca adentro

        private AsyncCallerMisReservas(Context context) {
            this.context = context.getApplicationContext();
            pdLoading.setCancelable(false);

        }

        @Override
        protected ArrayList<Map<String,Object>> doInBackground(String... params) {

                return WebServices.callListarMisReservas(dniLogged, getApplicationContext());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setTitle("Por favor, espere.");
            pdLoading.setMessage("Obteniendo reservas");
            pdLoading.show();
        }


        @Override
        protected void onPostExecute(ArrayList<Map<String,Object>> result) {
            if (result==null || result.isEmpty())
                Toast.makeText(getBaseContext(), "No se han encontrado reservas ", Toast.LENGTH_SHORT).show();
                //this method will be running on UI thread
            else{
                Intent i= new Intent(context, MyReserves.class);
                i.putExtra("reserves",result);
                startActivity(i);
            }
            pdLoading.dismiss();
        }
    }


    /**
     * el primer atributo que es String, son los nombres de los metodos que quiero llamar, lo hardcodeo con 1 solo atributo que es el nombre
     * del metodo así lo corro
     */
    private class AsyncCallerMisCompras extends AsyncTask<String, Void, ArrayList<Map<String,Object>> > {
        ProgressDialog pdLoading = new ProgressDialog(SearchScheludes.this);
        Context context; //contexto para largar la activity aca adentro

        private AsyncCallerMisCompras(Context context) {
            this.context = context.getApplicationContext();
            pdLoading.setCancelable(false);

        }

        @Override
        protected ArrayList<Map<String,Object>> doInBackground(String... params) {

            return WebServices.callListarMisCompras(dniLogged, getApplicationContext());
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setTitle("Por favor, espere.");
            pdLoading.setMessage("Obteniendo compras");
            pdLoading.show();
        }


        @Override
        protected void onPostExecute(ArrayList<Map<String,Object>> result) {
            if (result==null || result.isEmpty())
                Toast.makeText(getBaseContext(), "No se han encontrado compras ", Toast.LENGTH_SHORT).show();
                //this method will be running on UI thread
            else{
                Intent i= new Intent(context, MyPurchases.class);
                i.putExtra("purchases",result);
                startActivity(i);
            }
            pdLoading.dismiss();
        }
    }
}
