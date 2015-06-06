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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_number_tickets);
        tickets= (ListView) findViewById(R.id.listview_tickets);
        loadSpinnerTickets();
        tickets.setOnItemClickListener(this);
    }


    private void loadSpinnerTickets(){
        LinkedList<String> numberTickets = new LinkedList<>();
        numberTickets.add("1 pasajeros");
        numberTickets.add("2 pasajeros");
        numberTickets.add("3 pasajeros");
        numberTickets.add("4 pasajeros");
        numberTickets.add("5 pasajeros");
        numberTickets.add("6 pasajeros");
        numberTickets.add("7 pasajeros");
        numberTickets.add("8 pasajeros");
        numberTickets.add("9 pasajeros");
        numberTickets.add("10 pasajeros");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ChooseNumberTickets.this, android.R.layout.simple_list_item_1, numberTickets);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        tickets.setAdapter(adapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent();
        intent.putExtra("number", position+1);
        setResult(RESULT_OK, intent);
        finish();
    }
}
