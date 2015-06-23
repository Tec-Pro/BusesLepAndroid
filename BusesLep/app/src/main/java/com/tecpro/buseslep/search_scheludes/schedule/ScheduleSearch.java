package com.tecpro.buseslep.search_scheludes.schedule;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tecpro.buseslep.AdaptatorLastSearch;
import com.tecpro.buseslep.Dialog;
import com.tecpro.buseslep.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

/**
 * Created by nico on 30/05/15.
 */
public class ScheduleSearch extends Activity implements AdapterView.OnItemClickListener{

    public BaseAdapter adaptador;
    ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_search);
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
        Bundle bundle = getIntent().getExtras();
        String departCity= bundle.getString("departCity");
        String arrivCity = bundle.getString("arrivCity");
        String goOrReturn = bundle.getString("goOrReturn");
        String priceGo = bundle.getString("priceGo");
        String priceGoRet = bundle.getString("priceGoRet");
        TextView txtPriceGo = (TextView) findViewById(R.id.txt_description_price_go);
        DecimalFormat df = new DecimalFormat("0.00");
        txtPriceGo.setText("Ida $ "+df.format(Float.valueOf(priceGo)));
        TextView txtPriceGoRet = (TextView) findViewById(R.id.txt_description_price_go_ret);
        txtPriceGoRet.setText("Ida y vuelta $ "+df.format(Float.valueOf(priceGoRet)));
        TextView txtDescription= (TextView) findViewById(R.id.txt_description_schedule);
        TextView txtGoRet= (TextView) findViewById(R.id.txt_description_go_ret);
        txtGoRet.setText(goOrReturn);
        txtDescription.setText(departCity+" - "+arrivCity);
        adaptador = new AdaptatorSchedule(this,(ArrayList<Map<String,Object>>)bundle.get("schedules"));
        listView = (ListView) findViewById(R.id.list_view_shedule);
        listView.setAdapter(adaptador);
        listView.setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Schedule scheduleSelected=(Schedule)adaptador.getItem(position);
        resultIntent(scheduleSelected);
    }

    private void resultIntent(Schedule scheduleSelected) {
        if (scheduleSelected.getStatus().contains("viaje") || scheduleSelected.getStatus().contains("destino") || scheduleSelected.getStatus().contains("completo")){
            String message= "El servicio se encuentra " + scheduleSelected.getStatus() + ". \n Seleccione otro disponible por favor.";
            Intent intentDialog = new Intent(this, Dialog.class);
            intentDialog.putExtra("message",message);
            startActivity(intentDialog);
    }else {
            Intent intent = new Intent();
            intent.putExtra("arrivDate", scheduleSelected.getArrivDate());
            intent.putExtra("arrivTime", scheduleSelected.getArrivTime());
            intent.putExtra("departTime", scheduleSelected.getDepartTIme());
            intent.putExtra("departDate", scheduleSelected.getDepartDate());
            //intent.putExtra("estado",scheduleSelected.getStatus());
            intent.putExtra("codigo", scheduleSelected.getCode());
            intent.putExtra("idEmpresa", scheduleSelected.getIdEnterprise());
            intent.putExtra("id_destino", scheduleSelected.getIdDestino());

            setResult(RESULT_OK, intent);
            finish();
        }
    }

}
