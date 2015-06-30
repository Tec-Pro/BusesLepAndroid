package com.tecpro.buseslep.my_purchases;

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

import com.tecpro.buseslep.Dialog;
import com.tecpro.buseslep.R;
import com.tecpro.buseslep.search_scheludes.schedule.AdaptatorSchedule;
import com.tecpro.buseslep.search_scheludes.schedule.Schedule;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by nico on 30/05/15.
 */
public class MyPurchases extends Activity {

    public BaseAdapter adaptador;
    ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_purchases);
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
        adaptador = new AdaptatorMyPurchase(this,(ArrayList<Map<String,Object>>)bundle.get("purchases"));
        listView = (ListView) findViewById(R.id.list_view_purchase);
        listView.setAdapter(adaptador);
    }





}
