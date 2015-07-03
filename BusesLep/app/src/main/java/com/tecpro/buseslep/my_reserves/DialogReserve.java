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
    private String cantidadIda;
    private int Id_EmpresaIda;
    private String Id_DestinoIda;
    private String IdLocalidadHastaIda;
    private String IdLocalidadDesdeIda;
    private int Cod_HorarioIda;

    private String lugarVuelta="";
    private String FechaHoraReservaVuelta="";
    private String fecha_saleVuelta="";
    private String hora_saleVuelta="";
    private String cantidadVuelta="";
    private String Id_EmpresaVuelta="";
    private String Id_DestinoVuelta="";
    private String IdLocalidadHastaVuelta="";
    private String IdLocalidadDesdeVuelta="";
    private String Cod_HorarioVuelta="-1";
    private static String priceGo;
    private static String priceGoRet;
    private static AsyncCallerReserve asyncCallerReserve;

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
        cantidadIda= (String)((Map)reserve.get("ida")).get("cantidad");
        Id_EmpresaIda= Integer.valueOf((String)((Map)reserve.get("ida")).get("Id_Empresa"));
        Id_DestinoIda= (String)((Map)reserve.get("ida")).get("Id_Destino");
        IdLocalidadHastaIda= (String)((Map)reserve.get("ida")).get("IdLocalidadHasta");
        IdLocalidadDesdeIda= (String)((Map)reserve.get("ida")).get("IdLocalidadDesde");
        Cod_HorarioIda= Integer.valueOf((String)((Map)reserve.get("ida")).get("Cod_Horario"));
        if(reserve.containsKey("vuelta")) {
            lugarVuelta = (String) ((Map) reserve.get("vuelta")).get("lugar");
            FechaHoraReservaVuelta = (String) ((Map) reserve.get("vuelta")).get("FechaHoraReserva");
            fecha_saleVuelta = (String) ((Map) reserve.get("vuelta")).get("fecha_sale");
            hora_saleVuelta = (String) ((Map) reserve.get("vuelta")).get("hora_sale");
            cantidadVuelta = (String) ((Map) reserve.get("vuelta")).get("cantidad");
            Id_EmpresaVuelta = (String) ((Map) reserve.get("vuelta")).get("Id_Empresa");
            Id_DestinoVuelta = (String) ((Map) reserve.get("vuelta")).get("Id_Destino");
            IdLocalidadHastaVuelta = (String) ((Map) reserve.get("vuelta")).get("IdLocalidadHasta");
            IdLocalidadDesdeVuelta = (String) ((Map) reserve.get("vuelta")).get("IdLocalidadDesde");
            Cod_HorarioVuelta = (String) ((Map) reserve.get("vuelta")).get("Cod_Horario");
        }

    }

    public void clickBuyReserve(View view){
        reserveAndLoadSeatPicker();

    }

    private void reserveAndLoadSeatPicker(){
        asyncCallerReserve= new AsyncCallerReserve(this);
        asyncCallerReserve.execute();
    }

        private void loadSeatPicker(boolean isGo){
        Intent i =  new Intent(this, SeatPicker.class);
        int resultCode = 4;
        i.putExtra("cant_tickets", cantidadIda);
        if(isGo){
            i.putExtra("isGo",1);
            i.putExtra("IDEmpresaIda",Integer.valueOf(Id_EmpresaIda));
            i.putExtra("CodHorarioIda",Integer.valueOf(Cod_HorarioIda ));
            i.putExtra("IDDestinoIda",Integer.valueOf(IdLocalidadDesdeIda));
            i.putExtra("IDDestinoVuelta",Integer.valueOf(IdLocalidadHastaIda));
            i.putExtra("id_destino_ida", Id_DestinoIda);
            i.putExtra("idVenta", idSell);
        }
        else{
            i.putExtra("isGo",0);
            i.putExtra("IDEmpresaIda",Integer.valueOf(Id_EmpresaVuelta));
            i.putExtra("CodHorarioIda",Integer.valueOf(Cod_HorarioVuelta) );
            i.putExtra("IDDestinoIda",Integer.valueOf(IdLocalidadDesdeVuelta));
            i.putExtra("IDDestinoVuelta",Integer.valueOf(IdLocalidadHastaVuelta));
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
            String resultCode;
            resultCode = WebServices.callPasarReservasaPrepago(dni,FechaHoraReservaIda,getApplicationContext());
            Map<String,String> priceMap= WebServices.getPrice(Integer.valueOf(IdLocalidadDesdeIda), Integer.valueOf(IdLocalidadHastaIda), getApplicationContext());
            priceGo=  priceMap.get("priceGo");
            priceGoRet=  priceMap.get("priceGoRet");
            try{
                idSell = Integer.valueOf(resultCode);
            }
            catch (NumberFormatException e){
                idSell = -1;
            }
            if(idSell<=0)
                return null;
            return resultCode ;
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
        i.putExtra("cant_tickets",cantidadIda);
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
}
