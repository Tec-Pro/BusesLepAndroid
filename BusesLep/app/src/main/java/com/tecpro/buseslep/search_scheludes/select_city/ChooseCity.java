package com.tecpro.buseslep.search_scheludes.select_city;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.tecpro.buseslep.R;
import com.tecpro.buseslep.search_scheludes.schedule.AdaptatorSchedule;
import com.tecpro.buseslep.search_scheludes.schedule.AdaptatorTickets;

import java.util.ArrayList;
import java.util.Map;

public class ChooseCity extends Activity implements AdapterView.OnItemClickListener {

    private ListView cities;
    private AdaptatorCity adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_city);
        cities= (ListView) findViewById(R.id.listview_cities);
        Bundle bundle = getIntent().getExtras();
        adaptador = new AdaptatorCity(this,(ArrayList<Map<String,Object>>)bundle.get("cities"));
        cities.setAdapter(adaptador);
        cities.setOnItemClickListener(this);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        Map<String,Object> city= (Map<String,Object>)adaptador.getItem(position);
        intent.putExtra("id", (Integer)city.get("id"));
        intent.putExtra("name",(String) city.get("name"));
        setResult(RESULT_OK, intent);
        finish();
    }
}
