package com.tecpro.buseslep.search_scheludes;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.text.InputType;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tecpro.buseslep.R;
import com.tecpro.buseslep.search_scheludes.schedule.ScheduleSearch;
import com.tecpro.buseslep.search_scheludes.schedule.SummarySchedules;
import com.tecpro.buseslep.webservices.WebServices;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class SearchScheludes extends Activity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerGo;
    private Spinner spinnerDestiny;
    private TreeMap<String, Integer>CitiesAndId; //nombre e id de las ciudades de origen, la clave es el nombre para facilitar las cosas
    private List<String> departureCities; //los nombres de ciudades de origen
    private List<String> destinationCities; //los nombres de ciudades de origen
    private Spinner spinnerTickets;
    private CheckBox chkRoundTrip;
    //UI References
    private TextView fromDateEtxt;
    private TextView toDateEtxt;
    //dia mes y año de ida
    private int dayGo=-1;
    private int monthGo=-1;
    private int yearGo=-1;
    //dia mes y año de vuelta
    private int dayReturn=-1;
    private int monthReturn=-1;
    private int yearReturn=-1;

    //datos para la busqueda
    private Integer idOrigin=-1; //id de origen
    private Integer idDestiny=-1; //id de destino
    private Integer numberOfTickets=1;
    private String dateGo; //string para la fecha de ida en formato 20150605
    private String dateReturn; //string para la fecha de vuelta en formato 20150605
    private AsyncCallerCities asyncCallerCities;
    private AsyncCallerSchedules asyncCallerSchedules;

    private ArrayList<Map<String,Object>> schedules; //lista con todos los horarios, la misma la uso para ida y  para vuelta
    private String codeGo; //tengo el codigo del horario para la reserva
    private String codeReturn; //el codigo del horario apra la reserva pero de la vuelta
    //datos para la ida
    private String departTimeGo;
    private String departDateGo;
    private String arrivTimeGo;
    private String arrivDateGo;
    //datos para la vuelta
    private String departTimeReturn;
    private String departDateReturn;
    private String arrivTimeReturn;
    private String arrivDateReturn;
    private String price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_scheludes);
        findViewsById();
        loadOrigin();
        Calendar cal = Calendar.getInstance();
        dayGo = cal.get(Calendar.DAY_OF_MONTH);
        monthGo = cal.get(Calendar.MONTH)+1;
        yearGo = cal.get(Calendar.YEAR);
        dayReturn = cal.get(Calendar.DAY_OF_MONTH);
        monthReturn = cal.get(Calendar.MONTH)+1;
        yearReturn = cal.get(Calendar.YEAR);
        loadSpinnerTickets();
    }

    private void loadOrigin(){
        asyncCallerCities= new AsyncCallerCities();
        asyncCallerCities.execute("getCities");
    }

    private void loadDestiny(){
        asyncCallerCities= new AsyncCallerCities();
        asyncCallerCities.execute("getDestinationCities");
    }

    private void loadSpinnerTickets(){
        LinkedList<String> numberTickets = new LinkedList<>();
        numberTickets.add("Cantidad de pasajes");
        numberTickets.add("1");
        numberTickets.add("2");
        numberTickets.add("3");
        numberTickets.add("4");
        numberTickets.add("5");
        numberTickets.add("6");
        numberTickets.add("7");
        numberTickets.add("8");
        numberTickets.add("9");
        numberTickets.add("10");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SearchScheludes.this, android.R.layout.simple_spinner_item, numberTickets);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTickets.setAdapter(adapter);
    }

    /**
     * busco los txtView de fecha y los seteo
     */
    private void findViewsById() {
        fromDateEtxt = (TextView) findViewById(R.id.txt_date_from);
        toDateEtxt = (TextView) findViewById(R.id.txt_date_to);
        spinnerGo = (Spinner) findViewById(R.id.spinner_go);
        spinnerGo.setOnItemSelectedListener(this);
        spinnerDestiny = (Spinner) findViewById(R.id.spinner_destiny);
        spinnerDestiny.setOnItemSelectedListener(this);
        spinnerTickets = (Spinner) findViewById(R.id.spinner_number_tickets);
        spinnerTickets.setOnItemSelectedListener(this);
        chkRoundTrip = (CheckBox) findViewById(R.id.chk_round_trip);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
      //  getMenuInflater().inflate(R.menu.menu_search_scheludes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_update_cities) {
            loadOrigin();
            idOrigin=-1;
            loadDestiny();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
                if(dayGo<10)
                    auxDayRet= "0"+dayReturn;//le agrego un cero adelate
                dateReturn=yearReturn+""+auxMonthRet+""+auxDayRet;// es un entero casteado
                break;
            case 3://retorno de seleccion de horario ida
                codeGo= data.getStringExtra("codigo");
                departDateGo = data.getStringExtra("departDate");
                departTimeGo = data.getStringExtra("departTime");
                arrivDateGo = data.getStringExtra("arrivDate");
                arrivTimeGo = data.getStringExtra("arrivTime");
                //debo corroborar si es ida y vuelta, en caso de ser ida y vuelta debo largar la gui para elegir retorno
                if (chkRoundTrip.isChecked()) {
                    asyncCallerSchedules = new AsyncCallerSchedules(this);
                    asyncCallerSchedules.execute("return");
                }else{
                    launchBuyReserve(false);
                }
                break;
            case 4: //retorno de la seleccion de horario vuelta
                codeReturn= data.getStringExtra("codigo");
                codeGo= data.getStringExtra("codigo");
                departDateReturn = data.getStringExtra("departDate");
                departTimeReturn = data.getStringExtra("departTime");
                arrivDateReturn = data.getStringExtra("arrivDate");
                arrivTimeReturn = data.getStringExtra("arrivTime");
                launchBuyReserve(true);
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
        i.putExtra("Origin",(String)spinnerGo.getSelectedItem() );
        i.putExtra("Destiny",(String)spinnerDestiny.getSelectedItem() );
        i.putExtra("codeGo",codeGo );
        if(goReturn){
            i.putExtra("departTimeReturn",departTimeReturn );
            i.putExtra("departDateReturn",departDateReturn );
            i.putExtra("arrivDateReturn",arrivDateReturn );
            i.putExtra("arrivTimeReturn",arrivTimeReturn);
            i.putExtra("codeReturn",codeReturn );
        }
        startActivity(i);


    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Aquí se codifica la lógica que se ejecutará al seleccionar un elemento del Spinner.
        if (parent.getId()==  R.id.spinner_go){
            String nameCity=spinnerGo.getItemAtPosition(position).toString();
            if(nameCity!=null) {
                idOrigin = CitiesAndId.get(nameCity); //seteo el id de origen
                if(idOrigin==null)
                    idOrigin=-1;
                loadDestiny();
            }
        }
        if (parent.getId()==  R.id.spinner_destiny){
            String nameCity=spinnerDestiny.getItemAtPosition(position).toString();
            if(nameCity!=null) {
                idDestiny = CitiesAndId.get(nameCity); //seteo el id de destino
                if(idDestiny==null)
                    idDestiny=-1;
            }
        }
        if (parent.getId()==  R.id.spinner_number_tickets){
            if(position>0)
                numberOfTickets=Integer.valueOf((String)spinnerTickets.getItemAtPosition(position));
            else
                numberOfTickets=-1;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
            if(fromDateEtxt.getText().charAt(0)=='F'||
                    !dateIsValid(currentDay,currentMonth,currentYear,dayReturn,monthReturn,yearReturn)||
                    !dateIsValid(dayGo,monthGo,yearGo,dayReturn,monthReturn,yearReturn)) { //valido la fecha de regreso
                err=true;
                error=error.concat(" fecha de regreso incorrecta \n");
            }
        }
        //la forma crota de ver si elegi una fecha, me fijo si hay una F de 'F'echa
        if(fromDateEtxt.getText().charAt(0)=='F'||!dateIsValid(currentDay,currentMonth,currentYear,dayGo,monthGo,yearGo)) { //valido la fecha de regreso y que haya algo
            err=true;
            error=error.concat(" fecha de ida incorrecta \n");
        }
        if(!err) {
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
                    Pair<TreeMap<String, Integer>, LinkedList<String>> cities=WebServices.getCities();
                    departureCities=cities.second;
                    CitiesAndId=cities.first;
                    return new Pair("getCities",departureCities);
                case "getDestinationCities" :
                    destinationCities= WebServices.getDestinationCities(idOrigin);
                    return new Pair("getDestinationCities",destinationCities);
                case "getSchedules" :
                    schedules= WebServices.getSchedules(idOrigin, idDestiny, dateGo);
                    return new Pair("getDestinationCities",destinationCities);
            }
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here


            return null;
        }

        @Override
        protected void onPostExecute(Pair<String,List<String>> result) {
            if (result==null)
                Toast.makeText(getBaseContext(), "No se han encontrado horarios ciudades", Toast.LENGTH_SHORT).show();
            //this method will be running on UI thread
            else{
                switch (result.first){
                    case "getCities":
                        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(SearchScheludes.this, android.R.layout.simple_spinner_item, departureCities);
                        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerGo.setAdapter(adaptador);
                        break;
                    case "getDestinationCities":
                        ArrayAdapter<String> adapterDestiny = new ArrayAdapter<String>(SearchScheludes.this, android.R.layout.simple_spinner_item, destinationCities);
                        adapterDestiny.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerDestiny.setAdapter(adapterDestiny);
                        break;
                }
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
            if(params[0]=="go")
                return new Pair(params[0],WebServices.getSchedules(idOrigin, idDestiny, dateGo));
            else
                return new Pair(params[0],WebServices.getSchedules(idDestiny,idOrigin, dateReturn));
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
                int codeResult=-1;
                switch (result.first){
                    case "go":
                        codeResult=3;
                        i.putExtra("departCity",(String)spinnerGo.getSelectedItem());
                        i.putExtra("arrivCity",(String)spinnerDestiny.getSelectedItem());
                        i.putExtra("goOrReturn","ida");
                        break;
                    case "return":
                        codeResult=4;
                        i.putExtra("departCity",(String)spinnerDestiny.getSelectedItem());
                        i.putExtra("arrivCity",(String)spinnerGo.getSelectedItem());
                        i.putExtra("goOrReturn","vuelta");
                        break;
                }
                startActivityForResult(i,codeResult);
                }
            pdLoading.dismiss();
        }
        }



}
