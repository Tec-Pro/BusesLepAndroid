package com.tecpro.buseslep.search_scheludes.schedule;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.tecpro.buseslep.AdaptatorLastSearch;
import com.tecpro.buseslep.R;

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
        Bundle bundle = getIntent().getExtras();
        String departCity= bundle.getString("departCity");
        String arrivCity = bundle.getString("arrivCity");
        String goOrReturn = bundle.getString("goOrReturn");
        TextView txtDescription= (TextView) findViewById(R.id.txt_description_schedule);
        txtDescription.setText(departCity+" - "+arrivCity+"    "+ goOrReturn);
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

    private void resultIntent(Schedule scheduleSelected){
        Intent intent= new Intent();
        intent.putExtra("arrivDate",scheduleSelected.getArrivDate());
        intent.putExtra("arrivTime",scheduleSelected.getArrivTime());
        intent.putExtra("departTime",scheduleSelected.getDepartTIme());
        intent.putExtra("departDate",scheduleSelected.getDepartDate());
        //intent.putExtra("estado",scheduleSelected.getStatus());
        intent.putExtra("codigo",scheduleSelected.getCode());
        setResult(RESULT_OK, intent);
        finish();
    }

}
