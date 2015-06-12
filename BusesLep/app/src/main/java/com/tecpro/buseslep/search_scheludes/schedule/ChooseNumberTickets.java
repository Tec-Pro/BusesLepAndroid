package com.tecpro.buseslep.search_scheludes.schedule;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.tecpro.buseslep.R;

import java.util.LinkedList;

public class ChooseNumberTickets extends Activity implements AdapterView.OnItemClickListener {

    private ListView tickets;
    private AdaptatorTickets adaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_number_tickets);


        tickets= (ListView) findViewById(R.id.listview_tickets);
        adaptador = new AdaptatorTickets(this);
        tickets.setAdapter(adaptador);
        tickets.setOnItemClickListener(this);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("number", (String)tickets.getItemAtPosition(position));
        setResult(RESULT_OK, intent);
        finish();
    }
}
