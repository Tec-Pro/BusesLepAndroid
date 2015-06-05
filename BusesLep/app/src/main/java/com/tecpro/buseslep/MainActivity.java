package com.tecpro.buseslep;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.BaseAdapter;

import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.tecpro.buseslep.search_scheludes.SearchScheludes;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends Activity implements OnItemClickListener {

    public BaseAdapter adaptador;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        adaptador = new AdaptatorLastSearch(this);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adaptador);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void launchSearchSchedules(View v){
        Intent i =  new Intent(this, SearchScheludes.class);
        startActivity(i);
    }

    public void launchLogin(View v){
        Intent i =  new Intent(this, Login.class);
        startActivity(i);
    }

    public void launchRegister(View v){
        Intent i =  new Intent(this, Singin.class);
        startActivity(i);
    }
}
