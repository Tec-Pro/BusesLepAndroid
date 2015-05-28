package com.tecpro.buseslep.search_scheludes;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.text.InputType;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

public class SearchScheludes extends Activity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerGo;
    private Spinner spinnerDestiny;
    private TreeMap<String, Integer> nameIdHometowns; //nombre e id de las ciudades de origen, la clave es el nombre para facilitar las cosas
    private List<String> nameHometowns; //los nombres
    private NumberPicker pickTickets;
    private CheckBox chkRoundTrip;
    //UI References
    private TextView fromDateEtxt;
    private TextView toDateEtxt;
    //dia mes y año de ida
    private int dayGo;
    private int monthGo;
    private int yearGo;
    //dia mes y año de vuelta
    private int dayReturn;
    private int monthReturn;
    private int yearReturn;

    //datos para la busqueda
    private Integer idOrigin;
    private Integer idDestiny;
    private int numberOfTickets;
    private Integer dateGo;
    private Integer dateReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_scheludes);
        findViewsById();
        loadOrigin();
        spinnerGo.setOnItemSelectedListener(this);
        Calendar cal = Calendar.getInstance();
        dayGo = cal.get(Calendar.DAY_OF_MONTH);
        monthGo = cal.get(Calendar.MONTH)+1;
        yearGo = cal.get(Calendar.YEAR);
        dayReturn = cal.get(Calendar.DAY_OF_MONTH);
        monthReturn = cal.get(Calendar.MONTH)+1;
        yearReturn = cal.get(Calendar.YEAR);
    }

    private void loadOrigin(){
        // Inicializamos la variable.
        nameIdHometowns = new TreeMap<String, Integer>();
        nameHometowns = new LinkedList<>();
        nameIdHometowns.put("Río Cuarto", 1);
        nameIdHometowns.put("Córdoba", 2);
        nameIdHometowns.put("Villa María", 3);
        nameIdHometowns.put("Santa Rosa de Calamuchita", 4);
        nameHometowns.add("Ciudad de Origen");
        nameHometowns.add("Río Cuarto");
        nameHometowns.add("Córdoba");
        nameHometowns.add("Villa María");
        nameHometowns.add("Santa Rosa de Calamuchita");
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nameHometowns);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGo.setAdapter(adaptador);
    }

    private void loadDestiny(int idOrigin){

    }

    /**
     * busco los txtView de fecha y los seteo
     */
    private void findViewsById() {
        fromDateEtxt = (TextView) findViewById(R.id.txt_date_from);
        fromDateEtxt.requestFocus();
        toDateEtxt = (TextView) findViewById(R.id.txt_date_to);
        spinnerGo = (Spinner) findViewById(R.id.spinner_go);
        spinnerGo = (Spinner) this.findViewById(R.id.spinner_go);
        spinnerDestiny = (Spinner) findViewById(R.id.spinner_destiny);
        spinnerDestiny = (Spinner) this.findViewById(R.id.spinner_destiny);
        pickTickets = (NumberPicker) findViewById(R.id.pick_tickets);
        pickTickets.setMaxValue(10);
        pickTickets.setMinValue(1);
        chkRoundTrip = (CheckBox) findViewById(R.id.chk_round_trip);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_scheludes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void clickDate(View v){
        int requestCode=-1;
        String descriptionDate="Selección fecha de ";
        Intent intent = new Intent(this, ChooseDate.class);//lanzo actividad de elegir fecha dependiendo de si es ida o vuelta
        if (v== fromDateEtxt){
            requestCode=1;
            descriptionDate=descriptionDate.concat("ida");
            intent.putExtra("day",dayGo);
            intent.putExtra("month",monthGo);
            intent.putExtra("year",yearGo);
        }
        else {
            requestCode = 2;
            descriptionDate=descriptionDate.concat("vuelta");
            intent.putExtra("day",dayReturn);
            intent.putExtra("month",monthReturn);
            intent.putExtra("year",yearReturn);
        }
        intent.putExtra("description", descriptionDate);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        switch (requestCode){
            case 1:
                dayGo= data.getIntExtra("day", 1);
                monthGo= data.getIntExtra("month",1);
                yearGo= data.getIntExtra("year",2015);
                fromDateEtxt.setText(dayGo+"/"+monthGo+"/"+yearGo);
                dateGo= Integer.valueOf(dayGo+""+monthGo+""+yearGo);// es un entero casteado
                break;
            case 2:
                dayReturn= data.getIntExtra("day", 1);
                monthReturn= data.getIntExtra("month",1);
                yearReturn= data.getIntExtra("year",2015);
                toDateEtxt.setText(dayReturn+"/"+monthReturn+"/"+yearReturn);
                dateReturn= Integer.valueOf(dayReturn+""+monthReturn+""+yearReturn);// es un entero casteado

                break;
        }
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Aquí se codifica la lógica que se ejecutará al seleccionar un elemento del Spinner.
        if (parent.getId()==  R.id.spinner_go){
            String nameCity=spinnerGo.getItemAtPosition(position).toString();
            if(nameCity!=null) {
                Toast.makeText(getApplicationContext(), " id " + nameIdHometowns.get(nameCity), Toast.LENGTH_LONG).show();
                idOrigin = nameIdHometowns.get(nameCity); //seteo el id de origen
            }
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
       if(idOrigin==null){
           error= error.concat(" seleccione ciudad de origen \n");
           err=true;//ocurrio un error
       }
        if(idDestiny==null){
            error= error.concat(" seleccione ciudad de destino \n");
            err=true;//ocurrio un error
        }
        if (chkRoundTrip.isChecked()){
            if(!dateIsValid(dayReturn,monthReturn,yearReturn)) { //valido la fecha de regreso
                err=true;
                error=error.concat(" fecha de regreso incorrecta \n");
            }
        }
        if(!dateIsValid(dayGo,monthGo,yearGo)) { //valido la fecha de regreso
            err=true;
            error=error.concat(" fecha de ida incorrecta \n");
        }
        if(!err)
            Toast.makeText(getApplicationContext(),"datos perfectos",Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(),error,Toast.LENGTH_LONG).show();
    }


    /**
     * retorna true si la fecha de hoy es menor que la fecha pasada por parametro
     * @param day
     * @param month
     * @param year
     * @return
     * @throws ParseException
     */
    private boolean dateIsValid(int day, int month, int year) {
        /**Obtenemos las fechas enviadas en el formato a comparar*/
        try {
            Calendar cal = Calendar.getInstance();
            int currentDay = cal.get(Calendar.DAY_OF_MONTH);
            int currentMonth = cal.get(Calendar.MONTH) + 1;
            int currentYear = cal.get(Calendar.YEAR);
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaDate1 = formateador.parse(currentDay + "/" + currentMonth + "/" + currentYear);
            Date fechaDate2 = formateador.parse(day + "/" + month + "/" + year);
            if (fechaDate1.before(fechaDate2)) {
                System.out.println("La Fecha 1 es menor ");
            }
            return fechaDate1.before(fechaDate2) ||fechaDate1.equals(fechaDate2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
