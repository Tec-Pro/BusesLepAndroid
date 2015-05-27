package com.tecpro.buseslep.search_scheludes;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tecpro.buseslep.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

public class SearchScheludes extends Activity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerGo;
    private Spinner spinnerDestiny;
    private TreeMap<Integer, String> nameIdHometowns; //nombre e id de las ciudades de origen
    private List<String> nameHometowns;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    private NumberPicker pickTickets;
    //UI References
    private TextView fromDateEtxt;
    private TextView toDateEtxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_scheludes);
        findViewsById();
        loadOrigin();
        spinnerGo.setOnItemSelectedListener(this);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
    }

    private void loadOrigin(){
        // Inicializamos la variable.
        nameIdHometowns = new TreeMap<Integer, String>();
        nameHometowns = new LinkedList<>();
        nameIdHometowns.put(1, "Río Cuarto");
        nameIdHometowns.put(2, "Córdoba");
        nameIdHometowns.put(3, "Villa María");
        nameIdHometowns.put(4, "Santa Rosa de Calamuchita");
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
        spinnerGo = (Spinner) findViewById(R.id.spinner_go);
        spinnerGo = (Spinner) this.findViewById(R.id.spinner_go);
        // Inicializamos la variable.
        nameIdHometowns = new TreeMap<Integer, String>();
        nameHometowns = new LinkedList<>();
        nameIdHometowns.put(1, "Río Cuarto");
        nameIdHometowns.put(2, "Córdoba");
        nameIdHometowns.put(3, "Villa María");
        nameIdHometowns.put(4, "Santa Rosa de Calamuchita");
        nameHometowns.add("Ciudad de Origen");
        nameHometowns.add("Río Cuarto");
        nameHometowns.add("Córdoba");
        nameHometowns.add("Villa María");
        nameHometowns.add("Santa Rosa de Calamuchita");
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nameHometowns);
        adaptador.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGo.setAdapter(adaptador);
        setDateTimeField();
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
    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                fromDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                toDateEtxt.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
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


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // Aquí se codifica la lógica que se ejecutará al seleccionar un elemento del Spinner.
        if (parent.getId()==  R.id.spinner_go){
            Toast.makeText(getApplicationContext(), spinnerGo.getItemAtPosition(position).toString(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onClickDate(View v) {
        if(v == findViewById(R.id.txt_date_to)) {
         //   fromDatePickerDialog.show();
        } else if(v == findViewById(R.id.txt_date_from)) {
         //   toDatePickerDialog.show();
        }
    }
}
