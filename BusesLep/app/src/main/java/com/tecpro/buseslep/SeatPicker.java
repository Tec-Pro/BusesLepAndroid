package com.tecpro.buseslep;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.tecpro.buseslep.search_scheludes.SearchScheludes;
import com.tecpro.buseslep.webservices.WebServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by agustin on 26/06/15.
 */
public class SeatPicker extends Activity {



    public static int[] seatStates = new int[60];
    private ArrayList<Map<String,Object>> seats = new ArrayList<>();

    private static AsyncCallerSeatPicker asyncCallerSeatPicker;

    private int roundtrip;
    String idDestinyGo, idDestinyRet;
    private String cityfrom,cityto, arrdate1,arrhour1,arrdate2,arrhour2,cantTick, totalPriceGo, totalPriceGoRet ;
    int idEmpresaIda, idEmpresaVuelta, codHorarioIda, codHorarioVuelta, idCityOrigin,idCityDestiny;
    private int seatsToSelect;
    GridView gridview;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seat_picker);

        Bundle extras = getIntent().getExtras();

        roundtrip = extras.getInt("roundtrip");
        cityfrom = extras.getString("city_from");
        cityto = extras.getString("city_to");
        arrdate1 = extras.getString("arrival_date1");
        arrhour1 = extras.getString("arrival_hour1");
        arrdate2 = extras.getString("arrival_date2");
        arrhour2 = extras.getString("arrival_hour2");
        cantTick = extras.getString("cant_tickets");
        totalPriceGoRet = extras.getString("priceGoRet");
        totalPriceGo = extras.getString("priceGo");

        idCityOrigin = extras.getInt("IDDestinoIda");
        idCityDestiny = extras.getInt("IDDestinoVuelta");
        idEmpresaIda = extras.getInt("IDEmpresaIda");
        idEmpresaVuelta = extras.getInt("IDEmpresaVuelta");
        codHorarioVuelta = extras.getInt("CodHorarioVuelta");
        codHorarioIda = extras.getInt("CodHorarioIda");
        idDestinyGo = extras.getString("id_destino_ida");
        idDestinyRet = extras.getString("id_destino_vuelta");

        seatsToSelect = Integer.valueOf(cantTick);
        loadSeats();


        gridview = (GridView) findViewById(R.id.gridview);


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                ImageView imageView = (ImageView) v;

                if(seatStates[position] != ImageAdapter.None) {
                    if (seatStates[position] == ImageAdapter.Free && seatsToSelect > 0) {
                        imageView.setImageResource(R.drawable.selected_seat);
                        seatStates[position] = ImageAdapter.Selected;
                        seatsToSelect--;
                    } else {
                        if (seatStates[position] == ImageAdapter.Selected) {
                            imageView.setImageResource(R.drawable.free_seat);
                            seatStates[position] = ImageAdapter.Free;
                            seatsToSelect++;
                        }
                    }
                }
               // Toast.makeText(SeatPicker.this, "" + position,
                 //       Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void loadSeats(){
        asyncCallerSeatPicker= new AsyncCallerSeatPicker(this);
        asyncCallerSeatPicker.execute();
    }

    private class AsyncCallerSeatPicker extends AsyncTask<String, Void, Pair<String,List<String>> > {

        ProgressDialog pdLoading = new ProgressDialog(SeatPicker.this);
        Context context;

        private AsyncCallerSeatPicker(Context context){
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

            boolean isround = false;
            int idDesGo = 0;
            int idDesRet = 0;
            if(idDestinyGo != null && !idDestinyGo.isEmpty())
                idDesGo = Integer.valueOf(idDestinyGo);
            //if(idDestinyRet != null && !idDestinyRet.isEmpty())
              //  idDesRet = Integer.valueOf(idDestinyRet);
            if(roundtrip  != -1)
                isround = true;
            seats = WebServices.callEstadoButacasPlantaHorario(idEmpresaIda,idDesGo,codHorarioIda,idCityOrigin, idCityDestiny,getApplicationContext());

            return new Pair("seats",  seats);
        }

        @Override
        protected void onPostExecute(Pair<String,List<String>> result) {
            if (result== null ) {
                Intent i= new Intent(SeatPicker.this, Dialog.class);
                i.putExtra("message", "Error al mostrar asientos");
                startActivity(i);
                //this method will be running on UI thread
            }
            else{
                gridview.setAdapter(new ImageAdapter(context,seats));
            }
            pdLoading.dismiss();
        }
    }

    public void loadPurchaseDetails(View view) {
        if(seatsToSelect>0){
            Toast.makeText(SeatPicker.this, "Quedan " + seatsToSelect + " asientos por seleccionar",
                           Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i =  new Intent(this, PurchaseDetails.class);
        i.putExtra("city_from",cityfrom);
        i.putExtra("city_to",cityto);
        i.putExtra("arrival_date1",arrdate1);
        i.putExtra("arrival_hour1",arrhour1);
        i.putExtra("arrival_date2",arrdate2);
        i.putExtra("arrival_hour2",arrhour2);
        i.putExtra("cant_tickets",cantTick);
        i.putExtra("roundtrip",Integer.valueOf(roundtrip));
        i.putExtra("priceGo", totalPriceGo);//precio ida
        i.putExtra("priceGoRet", totalPriceGoRet); //precio ida vuelta
        startActivity(i);
    }
}
