package com.tecpro.buseslep.my_reserves;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tecpro.buseslep.Dialog;
import com.tecpro.buseslep.PurchaseDetails;
import com.tecpro.buseslep.R;
import com.tecpro.buseslep.SeatPicker;
import com.tecpro.buseslep.utils.PreferencesUsing;
import com.tecpro.buseslep.webservices.WebServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class DialogReserve extends Activity {

    Map<String,Object> reserve;
    private int idSell;
    private PreferencesUsing preferences;
    private String butacasIda = "", butacasVuelta = "";

    private String lugarIda;
    private String FechaHoraReservaIda;
    private String fecha_saleIda;
    private String hora_saleIda;
    private Integer cantidad;
    private int Id_EmpresaIda;
    private Integer Id_DestinoIda;
    private Integer IdLocalidadHastaIda;
    private Integer IdLocalidadDesdeIda;
    private int Cod_HorarioIda;


    private String fecha_saleVuelta="";
    private String hora_saleVuelta="";
    private String Id_EmpresaVuelta="";
    private String Id_DestinoVuelta="";
    private String Cod_HorarioVuelta="-1";
    private static String priceGo;
    private static String priceGoRet;
    private static AsyncCallerReserve asyncCallerReserve;
    private static AsyncCallerCancelReserve asyncCallerCancelReserve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_reserve);
        this.setFinishOnTouchOutside(false);
        //Grab the window of the dialog, and change the width
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        Window window = this.getWindow();
        lp.copyFrom(window.getAttributes());
//This makes the dialog take up the full width
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
        Bundle bundle= getIntent().getExtras();
        reserve=((ArrayList<Map<String,Object>>)bundle.get("reserve")).get(0);
        preferences = new PreferencesUsing(this);
        preferences.init();
        lugarIda= (String)((Map)reserve.get("ida")).get("lugar");
        FechaHoraReservaIda= (String)((Map)reserve.get("ida")).get("FechaHoraReserva");
        fecha_saleIda= (String)((Map)reserve.get("ida")).get("fecha_sale");
        hora_saleIda= (String)((Map)reserve.get("ida")).get("hora_sale");

        if(reserve.containsKey("vuelta")) {
            fecha_saleVuelta = (String) ((Map) reserve.get("vuelta")).get("fecha_sale");
            hora_saleVuelta = (String) ((Map) reserve.get("vuelta")).get("hora_sale");
        }

    }

    public void clickBuyReserve(View view){
        reserveAndLoadSeatPicker();
    }

    public void clickCancelReserve(View view){
        cancelReserve();
    }

    private void reserveAndLoadSeatPicker(){
        asyncCallerReserve= new AsyncCallerReserve(this);
        asyncCallerReserve.execute();
    }

    private void cancelReserve(){
        asyncCallerCancelReserve= new AsyncCallerCancelReserve(this);
        asyncCallerCancelReserve.execute();
    }

        private void loadSeatPicker(boolean isGo){
        Intent i =  new Intent(this, SeatPicker.class);
        int resultCode = 4;
        i.putExtra("cant_tickets", cantidad.toString());
        if(isGo){
            i.putExtra("isGo",1);
            i.putExtra("IDEmpresaIda",Integer.valueOf(Id_EmpresaIda));
            i.putExtra("CodHorarioIda",Integer.valueOf(Cod_HorarioIda ));
            i.putExtra("IDDestinoIda",Integer.valueOf(IdLocalidadDesdeIda));
            i.putExtra("IDDestinoVuelta",Integer.valueOf(IdLocalidadHastaIda));
            i.putExtra("id_destino_ida", Id_DestinoIda.toString());
            i.putExtra("idVenta", idSell);
        }
        else{
            i.putExtra("isGo",0);
            i.putExtra("IDEmpresaIda",Integer.valueOf(Id_EmpresaVuelta));
            i.putExtra("CodHorarioIda",Integer.valueOf(Cod_HorarioVuelta) );
            i.putExtra("IDDestinoIda",Integer.valueOf(IdLocalidadHastaIda));
            i.putExtra("IDDestinoVuelta",Integer.valueOf(IdLocalidadDesdeIda));
            i.putExtra("id_destino_ida", Id_DestinoVuelta);
            i.putExtra("idVenta", idSell);
            resultCode = 5;
        }
        startActivityForResult(i, resultCode);
    }


        private class AsyncCallerReserve extends AsyncTask<String, Void, String > {
        ProgressDialog pdLoading = new ProgressDialog(DialogReserve.this);
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
        protected String doInBackground(String... params) {
            String dni = preferences.getDni();
            Map<String, Object> map = WebServices.callPasarReservasaPrepago(dni,FechaHoraReservaIda,getApplicationContext());
            idSell= (Integer)map.get("Id_Venta");
            cantidad=(Integer)map.get("cantidad");
            Id_EmpresaIda= (Integer)map.get("Id_Empresa");
            Id_DestinoIda=(Integer)map.get("Id_Destino");
            IdLocalidadHastaIda=(Integer)map.get("ID_Localidad_Destino");
            IdLocalidadDesdeIda=(Integer) map.get("ID_Localidad_Origen");
            Cod_HorarioIda=(Integer)map.get("Cod_Horario");
            priceGo=(String)map.get("importe");
            if(map.containsKey("vuelta")){
                Map<String, Object> mapVuelta= (Map)map.get("vuelta");
                Id_EmpresaVuelta= mapVuelta.get("Id_Empresa").toString();
                Id_DestinoVuelta=mapVuelta.get("Id_Destino").toString();
                Cod_HorarioVuelta=mapVuelta.get("Cod_Horario").toString();
                priceGoRet=(String)mapVuelta.get("importe");
            }
            if(idSell<=0)
                return null;
            return String.valueOf(idSell) ;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result== null ) {
                Intent i= new Intent(DialogReserve.this, Dialog.class);
                i.putExtra("message", "No se ha podido reservar");
                startActivity(i);
                //this method will be running on UI thread
            }
            else{
                loadSeatPicker(true);
            }
            pdLoading.dismiss();
        }
    }


      @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        switch (requestCode) {

            case 4: //retorno de la seleccion de butacas ida
                butacasIda = data.getStringExtra("butacas");

                if(Cod_HorarioVuelta == "-1")
                    launchBuy();
                else
                    loadSeatPicker(false);
                break;
            case 5:
                butacasVuelta = data.getStringExtra("butacas");

                launchBuy();
                break;
        }
    }

       private void launchBuy(){
        Intent i =  new Intent(this, PurchaseDetails.class);
        i.putExtra("city_from",lugarIda.split(" a ")[0]);
        i.putExtra("city_to",lugarIda.split(" a ")[1]);
        i.putExtra("arrival_date1",fecha_saleIda);
        i.putExtra("arrival_hour1",hora_saleIda);
        i.putExtra("arrival_date2",fecha_saleVuelta);
        i.putExtra("arrival_hour2",hora_saleVuelta);
        i.putExtra("cant_tickets",cantidad.toString());
        i.putExtra("roundtrip",Integer.valueOf(Cod_HorarioVuelta));
        i.putExtra("IDEmpresaIda",Id_EmpresaIda);
        i.putExtra("IDEmpresaVuelta",Id_EmpresaVuelta);
        i.putExtra("CodHorarioIda", Integer.valueOf(Cod_HorarioIda));
        i.putExtra("CodHorarioVuelta", Integer.valueOf(Cod_HorarioVuelta));
        i.putExtra("IDDestinoIda", Integer.valueOf(IdLocalidadDesdeIda));
        i.putExtra("IDDestinoVuelta", Integer.valueOf(IdLocalidadHastaIda));
        i.putExtra("priceGo", priceGo);//precio ida
        i.putExtra("priceGoRet", priceGoRet); //precio ida vuelta
        i.putExtra("id_destino_ida", Id_DestinoIda);
        i.putExtra("id_destino_vuelta", Id_DestinoVuelta);
        i.putExtra("idVenta", idSell);
        i.putExtra("butacasIda",butacasIda);
        i.putExtra("butacasVuelta", butacasVuelta);
        startActivity(i);
    }



    private class AsyncCallerCancelReserve extends AsyncTask<String, Void, String > {
        ProgressDialog pdLoading = new ProgressDialog(DialogReserve.this);
        Context context;

        private AsyncCallerCancelReserve(Context context){
            this.context = context.getApplicationContext();
            pdLoading.setCancelable(true);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setTitle("Por favor, espere.");
            pdLoading.setMessage("Cancelando reserva");
            pdLoading.show();
        }
        @Override
        protected String doInBackground(String... params) {
            String dni = preferences.getDni();
            String result = WebServices.callAnularReservas(dni, FechaHoraReservaIda, getApplicationContext());
            return result ;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result== null || result!= "1") {
                Intent i= new Intent(DialogReserve.this, Dialog.class);
                i.putExtra("message", "No se ha podido anular la reserva");
                startActivity(i);
                //this method will be running on UI thread
            }
            else{
                Intent i= new Intent(DialogReserve.this, Dialog.class);
                i.putExtra("message", "Se ha anulado la reserva correctamente");
                startActivity(i);
                finish();
            }
            pdLoading.dismiss();
        }
    }
}
