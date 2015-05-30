package com.tecpro.buseslep.search_scheludes.schedule;

import android.app.Activity;
import android.os.Bundle;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.tecpro.buseslep.AdaptatorLastSearch;
import com.tecpro.buseslep.R;

/**
 * Created by nico on 30/05/15.
 */
public class ScheduleSearch extends Activity {

    public BaseAdapter adaptador;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_search);
        adaptador = new AdaptatorSchedule(this);
        ListView listView = (ListView) findViewById(R.id.list_view_shedule);
        listView.setAdapter(adaptador);
        System.out.println("sss");



    }
}
