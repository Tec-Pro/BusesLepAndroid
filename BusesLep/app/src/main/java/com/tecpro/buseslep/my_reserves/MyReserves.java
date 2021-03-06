package com.tecpro.buseslep.my_reserves;

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

import com.tecpro.buseslep.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by nico on 30/05/15.
 */
public class MyReserves extends Activity implements AdapterView.OnItemClickListener {

    public BaseAdapter adaptador;
    ListView listView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_reserves);
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
        adaptador = new AdaptatorMyReserve(this,(ArrayList<Map<String,Object>>)bundle.get("reserves"));
        listView = (ListView) findViewById(R.id.list_view_reserve);
        listView.setAdapter(adaptador);
        listView.setOnItemClickListener(this);

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,DialogReserve.class);
        ArrayList<Map<String, Object>> aux= new ArrayList<>();
        aux.add((Map<String, Object>) adaptador.getItem(position));
        intent.putExtra("reserve", aux);
        startActivity(intent);
    }


}
