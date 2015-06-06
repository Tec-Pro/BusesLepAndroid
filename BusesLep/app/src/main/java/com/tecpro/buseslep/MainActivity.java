package com.tecpro.buseslep;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.BaseAdapter;

import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.tecpro.buseslep.batabase.DataBaseHelper;
import com.tecpro.buseslep.search_scheludes.SearchScheludes;
import com.tecpro.buseslep.search_scheludes.schedule.ScheduleSearch;
import com.tecpro.buseslep.search_scheludes.schedule.SummarySchedules;
import com.tecpro.buseslep.utils.SecurePreferences;
import com.tecpro.buseslep.webservices.WebServices;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity implements OnItemClickListener {
    //datos para la busqueda cuando clickeas en un horario
    private static Integer idOrigin=-1; //id de origen
    private static Integer idDestiny=-1; //id de destino
    private static String cityOrigin;
    private static String cityDestiny;
    private static Integer numberOfTickets=1;
    private static String dateGo; //string para la fecha de ida en formato 20150605
    private static String dateReturn; //string para la fecha de vuelta en formato 20150605
    private static int isRoundtrip;
    private static AsyncCallerSchedules asyncCallerSchedules;
    private static ArrayList<Map<String,Object>> schedules; //lista con todos los horarios, la misma la uso para ida y  para vuelta
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
    private String codeGoSchedule; //tengo el codigo del horario para la reserva
    private String codeReturnSchedule; //el codigo del horario apra la reserva pero de la vuelta
    private DataBaseHelper dbh;
    private List<Map<String,Object>> searches;
    ListView listView; //lista de busquedas recientes

    public BaseAdapter adaptador;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbh = new DataBaseHelper(this);
        dbh.deleteOldsSearches();
        searches = dbh.getSearches();
        adaptador = new AdaptatorLastSearch(this,searches);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adaptador);
        listView.setOnItemClickListener(this);
        SecurePreferences preferences = new SecurePreferences(getApplication(), "my-preferences", "BusesLepCordoba", true);
        if (preferences.getString("login") != null ) {
            if (preferences.getString("login").equals("true")) {
                findViewById(R.id.btnLogin).setVisibility(View.INVISIBLE);
                findViewById(R.id.btnRegister).setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onResume(){ //actualiza la listview cada vez que regresa de otra activity
        super.onResume();
        searches = dbh.getSearches();
        adaptador = new AdaptatorLastSearch(this,searches);
        listView.setAdapter(adaptador);
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

        if (id == R.id.action_logout) {
            SecurePreferences preferences = new SecurePreferences(getApplication(), "my-preferences", "BusesLepCordoba", true);
            preferences.put("login", "false");
            findViewById(R.id.btnLogin).setVisibility(View.VISIBLE);
            findViewById(R.id.btnRegister).setVisibility(View.VISIBLE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        loadSearch(searches.get(position));
    }

    public void launchSearchSchedules(View v){
        Intent i =  new Intent(this, SearchScheludes.class);
        startActivity(i);
    }

    public void launchLogin(View v){
        Intent i =  new Intent(this, Login.class);
        i.putExtra("next","main");
        startActivity(i);
    }

    public void launchRegister(View v){
        Intent i =  new Intent(this, Singin.class);
        i.putExtra("next","main");
        startActivity(i);
    }

    /**
     * Dado un map pasado por parametro que es el que retorna la base de datos, cargo los horarios
     * con todos los datos y largo la ventana de resumen de compra
     * @param search
     */
    private void loadSearch(Map<String,Object>search){
        cityOrigin = (String)search.get("city_origin");
        cityDestiny= (String)search.get("city_destiny");
        idOrigin= (int)search.get("code_city_origin");
        idDestiny= (int)search.get("code_city_destiny");
        dateGo=((String)search.get("date_go")).replaceAll("-","");
        isRoundtrip= (int)search.get("is_roundtrip");

        if(isRoundtrip == 1) //solo si es ida y vuelta pongo la fecha de regreso
            dateReturn=((String)search.get("date_return")).replaceAll("-","");
        numberOfTickets=(int)search.get("number_tickets");
        asyncCallerSchedules= new AsyncCallerSchedules(this);
        asyncCallerSchedules.execute("go");
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
        if(goReturn){
            i.putExtra("departTimeReturn",departTimeReturn );
            i.putExtra("departDateReturn",departDateReturn );
            i.putExtra("arrivDateReturn",arrivDateReturn );
            i.putExtra("arrivTimeReturn",arrivTimeReturn);
            i.putExtra("codeReturn",codeReturnSchedule );
        }
        startActivity(i);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {return;}
        switch (requestCode){
            case 3://retorno de seleccion de horario ida
            codeGoSchedule= data.getStringExtra("codigo");
            departDateGo = data.getStringExtra("departDate");
            departTimeGo = data.getStringExtra("departTime");
            arrivDateGo = data.getStringExtra("arrivDate");
            arrivTimeGo = data.getStringExtra("arrivTime");
            //debo corroborar si es ida y vuelta, en caso de ser ida y vuelta debo largar la gui para elegir retorno
            if (isRoundtrip==1) {
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
    launchBuyReserve(true);
    break;
}
}
    /**
     * el primer atributo que es String, son los nombres de los metodos que quiero llamar, lo hardcodeo con 1 solo atributo que es el nombre
     * del metodo así lo corro
     */
    private class AsyncCallerSchedules extends AsyncTask<String, Void, Pair<String,ArrayList<Map<String,Object>>> > {
        ProgressDialog pdLoading = new ProgressDialog(MainActivity.this);
        Context context; //contexto para largar la activity aca adentro

        private AsyncCallerSchedules(Context context) {
            this.context = context.getApplicationContext();
            pdLoading.setCancelable(false);

        }

        @Override
        protected Pair<String,ArrayList<Map<String,Object>>> doInBackground(String... params) {
            if(params[0]=="go")
                return new Pair(params[0], WebServices.getSchedules(idOrigin, idDestiny, dateGo));
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
                        i.putExtra("departCity",cityOrigin);
                        i.putExtra("arrivCity",cityDestiny);
                        i.putExtra("goOrReturn","ida");
                        break;
                    case "return":
                        codeResult=4;
                        i.putExtra("departCity",cityDestiny);
                        i.putExtra("arrivCity",cityOrigin);
                        i.putExtra("goOrReturn","vuelta");
                        break;
                }
                startActivityForResult(i,codeResult);
            }
            pdLoading.dismiss();
        }
    }
}
