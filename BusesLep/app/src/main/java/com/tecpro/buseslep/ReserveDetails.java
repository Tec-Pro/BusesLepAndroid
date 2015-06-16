package com.tecpro.buseslep;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.tecpro.buseslep.webservices.WebServices;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agustin on 06/06/15.
 */
public class ReserveDetails extends Activity {

    private static AsyncCallerReserve asyncCallerReserve;

    int roundtrip;
    String cityfrom;
    String cityto;
    String arrdate1;
    String arrhour1;
    String arrdate2;
    String arrhour2;
    String cantTick;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve_details);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(false);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.action_bar, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER
                )
        );
        Bundle extras = getIntent().getExtras();
        roundtrip = extras.getInt("roundtrip");
        cityfrom = extras.getString("city_from");
        cityto = extras.getString("city_to");
        arrdate1 = extras.getString("arrival_date1");
        arrhour1 = extras.getString("arrival_hour1");
        arrdate2 = extras.getString("arrival_date2");
        arrhour2 = extras.getString("arrival_hour2");
        cantTick = extras.getString("cant_tickets");

        TextView destiny1 = (TextView)findViewById(R.id.destiny1);
        TextView departure1 = (TextView)findViewById(R.id.departure1);
        TextView cantTickets1 = (TextView)findViewById(R.id.cantTickets1);

        destiny1.setText(cityfrom + " - " + cityto);
        departure1.setText(arrdate1 + "  " + arrhour1);
        cantTickets1.setText(cantTick);

        TextView departure2 = (TextView)findViewById(R.id.departure2);
        TextView destiny2 = (TextView)findViewById(R.id.destiny2);
        TextView cantTickets2 = (TextView)findViewById(R.id.cantTickets2);

        destiny2.setText(cityto + " - " + cityfrom);
        departure2.setText(arrdate2 + "  " + arrhour2);
        cantTickets2.setText(cantTick);

        if(roundtrip == -1)  //si es ida y vuelta leo y seteo los otros datos
            findViewById(R.id.backtrip).setVisibility(View.GONE);
    }

    public void reserve(View view) {
        asyncCallerReserve= new AsyncCallerReserve(this);
        asyncCallerReserve.execute();
    }

    private class AsyncCallerReserve extends AsyncTask<String, Void, Pair<String,List<String>> > {
        ProgressDialog pdLoading = new ProgressDialog(ReserveDetails.this);
        Context context;

        private AsyncCallerReserve(Context context){
            this.context = context.getApplicationContext();
            pdLoading.setCancelable(true);
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
        protected Pair<String, List<String>> doInBackground(String... params) {
            String resultCode = WebServices.CallAgregarReserva(null,null,null,1,1,1,1,1,1,1,1,1,1,1,1,1,getApplicationContext());//harcode or die
            if(resultCode.equals("Error de Autenticacion"))
                return null;
            return new Pair("resultado",  new ArrayList<String>().add(resultCode) );
        }

        @Override
        protected void onPostExecute(Pair<String,List<String>> result) {
            if (result== null )
                Toast.makeText(getBaseContext(), "Error de Autenticacion", Toast.LENGTH_SHORT).show();
                //this method will be running on UI thread
            else{
                Toast.makeText(getBaseContext(), "Reserva realizada con exito \n Le enviamos un mail con los detalles", Toast.LENGTH_SHORT).show();
            }
            pdLoading.dismiss();
        }
    }
}
