package com.tecpro.buseslep.search_scheludes.schedule;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.tecpro.buseslep.R;

import org.w3c.dom.Text;

public class SummarySchedules extends Activity {

    //textview para mostrar lo de la ida
    private TextView departTimeGo;
    private TextView departDateGo;
    private TextView arrivTimeGo;
    private TextView arrivtDateGo;

    //textViews para mostrar la vuelta
    private TextView departTimeReturn;
    private TextView departDateReturn;
    private TextView arrivTimeReturn;
    private TextView arrivtDateReturn;

    //numero de tickets
    private TextView numberTickets;

    private TextView descriptionGo;
    private TextView descriptionReturn;
    private TextView price;

    private String codeGo;
    private String codeReturn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_schedules);
        departTimeGo = (TextView) findViewById(R.id.txt_depart_time_go);
        departDateGo = (TextView) findViewById(R.id.txt_depart_date_go);
        arrivTimeGo = (TextView) findViewById(R.id.txt_arriv_time_go);
        arrivtDateGo = (TextView) findViewById(R.id.txt_arriv_date_go);

        departTimeReturn = (TextView) findViewById(R.id.txt_depart_time_return);
        departDateReturn = (TextView) findViewById(R.id.txt_depart_date_return);
        arrivTimeReturn = (TextView) findViewById(R.id.txt_arriv_time_return);
        arrivtDateReturn = (TextView) findViewById(R.id.txt_arriv_date_return);
        numberTickets = (TextView) findViewById(R.id.txt_number_tickets);
        descriptionGo = (TextView) findViewById(R.id.txt_description_go);
        descriptionReturn = (TextView) findViewById(R.id.txt_description_return);
        price= (TextView) findViewById(R.id.txt_price);
        Bundle bundle = getIntent().getExtras();

        codeGo= bundle.getString("codeGo", "-1");
        codeReturn= bundle.getString("codeReturn", "-1");

        departTimeGo.setText(bundle.getString("departTimeGo",""));
        departDateGo.setText(bundle.getString("departDateGo",""));
        arrivTimeGo.setText(bundle.getString("arrivTimeGo", ""));
        arrivtDateGo.setText(bundle.getString("arrivDateGo",""));

        departTimeReturn.setText(bundle.getString("departTimeReturn", ""));
        departDateReturn.setText(bundle.getString("departDateReturn", ""));
        arrivTimeReturn.setText(bundle.getString("arrivTimeReturn", ""));
        arrivtDateReturn.setText(bundle.getString("arrivDateReturn", ""));
        descriptionReturn.setText(bundle.getString("Destiny","")+" - "+ bundle.getString("Origin",""));

        if(codeReturn=="-1") {
            descriptionReturn.setText("");
            ((TextView) findViewById(R.id.txt_flecha)).setVisibility(View.INVISIBLE);
        }

        numberTickets.setText(bundle.getString("numberTickets",""));
        descriptionGo.setText(bundle.getString("Origin","")+" - "+ bundle.getString("Destiny",""));
        price.setText("$ "+bundle.getString("price",""));


    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_summary_schedules, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

//        return super.onOptionsItemSelected(item);
//    }

    /**
     * click en boton reserva
     * @param v
     */
    public void onClickReserve(View v){
        //Intent i =  new Intent(this, Reserve.class);
        //startActivity(i);
    }

    /**
     * click en boton compra
     * @param v
     */
    public void onClickBuy(View v){
        //Intent i =  new Intent(this, Buy.class);
        //startActivity(i);
    }
}
