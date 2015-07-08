package com.tecpro.buseslep;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
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


    private ArrayList<Map<String,Object>> seats = new ArrayList<>();

    private static AsyncCallerSeatPicker asyncCallerSeatPicker;
    private static AsyncCallerSelectSeat asyncCallerSelectSeat;

    private int p;
    private int roundtrip;
    String idDestinyGo, idDestinyRet;
    private String cantTick;
    int idEmpresaIda, idEmpresaVuelta, codHorarioIda, codHorarioVuelta, idCityOrigin,idCityDestiny;
    private int seatsToSelect;
    private Integer seatNum;
    private int idSell, isGo, isSelection;
    private List<Integer> butacas = new ArrayList<>();
    private ImageView imageView;

    GridView gridview;
    ImageAdapter imgAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seat_picker);
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
        isGo = extras.getInt("isGo");
        /*roundtrip = extras.getInt("roundtrip");
        cityfrom = extras.getString("city_from");
        cityto = extras.getString("city_to");
        arrdate1 = extras.getString("arrival_date1");
        arrhour1 = extras.getString("arrival_hour1");
        arrdate2 = extras.getString("arrival_date2");
        arrhour2 = extras.getString("arrival_hour2");*/
        cantTick = extras.getString("cant_tickets");
       /* totalPriceGoRet = extras.getString("priceGoRet");
        totalPriceGo = extras.getString("priceGo");*/

        idCityOrigin = extras.getInt("IDDestinoIda");
        idCityDestiny = extras.getInt("IDDestinoVuelta");
        idEmpresaIda = extras.getInt("IDEmpresaIda");
        codHorarioIda = extras.getInt("CodHorarioIda");
        idDestinyGo = extras.getString("id_destino_ida");
        idSell = extras.getInt("idVenta");
        seatsToSelect = Integer.valueOf(cantTick);
        TextView txt = (TextView)findViewById(R.id.seat_picker_txt);
        if(isGo == 1)
            txt.setText("Seleccione butacas para ida");
        else
            txt.setText("Seleccione butacas para vuelta");
        loadSeats();


        gridview = (GridView) findViewById(R.id.gridview);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                imageView = (ImageView) v;
                p = position;
                seatNum = imgAdapter.seatsArr[position][1];

                if(imgAdapter.seatsArr[position][0] != ImageAdapter.None) {
                    if (imgAdapter.seatsArr[position][0] == ImageAdapter.Free && seatsToSelect > 0) {
                      //  imageView.setImageResource(R.drawable.selected_seat);
                       // imgAdapter.seatsArr[position][0] = ImageAdapter.Selected;
                        isSelection = 1;
                        //butacas.add(seatNum);
                        //seatsToSelect--;
                        selectSeat();
                    } else {
                        if (imgAdapter.seatsArr[position][0] == ImageAdapter.Selected) {
                          //  imageView.setImageResource(R.drawable.free_seat);
                           // imgAdapter.seatsArr[position][0] = ImageAdapter.Free;
                            isSelection = 0;
                           // butacas.remove(seatNum);
                            //seatsToSelect++;
                            selectSeat();
                        }
                    }
                }
                //Toast.makeText(SeatPicker.this, "numero" + imgAdapter.seatsArr[position][1],
                  //      Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void selectSeat(){

        asyncCallerSelectSeat= new AsyncCallerSelectSeat(this);
        asyncCallerSelectSeat.execute();
    }

    private void loadSeats(){
        asyncCallerSeatPicker= new AsyncCallerSeatPicker(this);
        asyncCallerSeatPicker.execute();
    }

    private class AsyncCallerSelectSeat extends AsyncTask<String, Void, Pair<String,List<String>> > {

        ProgressDialog pdLoading = new ProgressDialog(SeatPicker.this);
        Context context;

        private AsyncCallerSelectSeat(Context context){
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
            String resultCode = "";

            resultCode = WebServices.callSeleccionarButaca(seatNum,idSell,isGo,isSelection,getApplicationContext());
           /* Log.i("DETE","num asiento: " + String.valueOf(seatNum));
            Log.i("DETE","id venta " +String.valueOf(idSell));
            Log.i("DETE","es ida " +String.valueOf(isGo));
            Log.i("DETE","es seleccion " + String.valueOf(isSelection));*/
            //Log.i("RES",resultCode.toString());
            ArrayList<String>ret =new ArrayList<String>();
            ret.add(resultCode);
            if(resultCode != null && ((isSelection == 1 && resultCode.equals("1")) || (isSelection==0 && resultCode.equals("0"))))
                return new Pair("res", ret);
            else
            if(resultCode.equals("-1") || resultCode.equals("-2"))
                return new Pair("res", ret);
            else
                return null;
        }

        @Override
        protected void onPostExecute(Pair<String,List<String>> result) {
            if (result == null) {
                imageView.setImageResource(R.drawable.occupied_seat);
                imgAdapter.seatsArr[p][0] = ImageAdapter.Occupied;
            }
            else{
                if(!result.second.get(0).equals("-1") && !result.second.get(0).equals("-2")) {
                    if (imgAdapter.seatsArr[p][0] != ImageAdapter.None && imgAdapter.seatsArr[p][0] != ImageAdapter.Driver) {
                        if (imgAdapter.seatsArr[p][0] == ImageAdapter.Free && seatsToSelect > 0) {
                            imageView.setImageResource(R.drawable.selected_seat);
                            imgAdapter.seatsArr[p][0] = ImageAdapter.Selected;
                            isSelection = 1;
                            butacas.add(seatNum);
                            seatsToSelect--;

                        } else {
                            if (imgAdapter.seatsArr[p][0] == ImageAdapter.Selected) {
                                imageView.setImageResource(R.drawable.free_seat);
                                imgAdapter.seatsArr[p][0] = ImageAdapter.Free;
                                isSelection = 0;
                                butacas.remove(seatNum);
                                seatsToSelect++;

                            }
                        }
                    }
                }else
                {
                    if(result.second.get(0).equals("-1"))
                        Toast.makeText(getApplicationContext(),"Butaca ocupada",Toast.LENGTH_SHORT);
                    else
                        Toast.makeText(getApplicationContext(),"Ya eligió todas sus butacas",Toast.LENGTH_SHORT).show();
                }
            }
            pdLoading.dismiss();
        }
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


            int idDes= 0;

            if(idDestinyGo != null && !idDestinyGo.isEmpty())
                idDes = Integer.valueOf(idDestinyGo);
            //if(idDestinyRet != null && !idDestinyRet.isEmpty())
              //  idDesRet = Integer.valueOf(idDestinyRet);
            seats = WebServices.callEstadoButacasPlantaHorario(idEmpresaIda,idDes,codHorarioIda,idCityOrigin, idCityDestiny,getApplicationContext());

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
                imgAdapter = new ImageAdapter(context,seats,gridview);
                gridview.setAdapter(imgAdapter);
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
        Intent intent = new Intent();
       /* Intent i =  new Intent(this, PurchaseDetails.class);
        i.putExtra("city_from",cityfrom);
        i.putExtra("city_to",cityto);
        i.putExtra("arrival_date1",arrdate1);
        i.putExtra("arrival_hour1",arrhour1);
        i.putExtra("arrival_date2",arrdate2);
        i.putExtra("arrival_hour2",arrhour2);
        i.putExtra("cant_tickets",cantTick);
        i.putExtra("roundtrip",Integer.valueOf(roundtrip));
        i.putExtra("priceGo", totalPriceGo);//precio ida
        i.putExtra("priceGoRet", totalPriceGoRet); //precio ida vuelta*/
        intent.putExtra("butacas",butacas.toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
