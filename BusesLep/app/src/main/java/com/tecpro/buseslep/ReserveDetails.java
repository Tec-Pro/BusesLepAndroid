package com.tecpro.buseslep;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * Created by agustin on 06/06/15.
 */
public class ReserveDetails extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserve_details);
        Bundle extras = getIntent().getExtras();
        boolean roundtrip = extras.getBoolean("roundtrip");
        String cityfrom1 = extras.getString("city_from1");
        String cityto1 = extras.getString("city_to1");
        String arrdate1 = extras.getString("arrival_date1");
        String arrhour1 = extras.getString("arrival_hour1");
        String cantTick1 = extras.getString("cant_tickets1");

        TextView destiny1 = (TextView)findViewById(R.id.destiny1);
        TextView departure1 = (TextView)findViewById(R.id.departure1);
        TextView cantTickets1 = (TextView)findViewById(R.id.cantTickets1);

        destiny1.setText(cityfrom1 + " - " + cityto1);
        departure1.setText(arrdate1 + "  " + arrhour1);
        cantTickets1.setText(cantTick1);

        if(roundtrip) { //si es ida y vuelta leo y seteo los otros datos
            String cityfrom2 = extras.getString("city_from2");
            String cityto2 = extras.getString("city_to2");
            String arrdate2 = extras.getString("arrival_date2");
            String arrhour2 = extras.getString("arrival_hour2");
            String cantTick2 = extras.getString("cant_tickets2");

            TextView departure2 = (TextView)findViewById(R.id.departure2);
            TextView destiny2 = (TextView)findViewById(R.id.destiny2);
            TextView cantTickets2 = (TextView)findViewById(R.id.cantTickets2);

            destiny1.setText(cityfrom2 + " - " + cityto2);
            departure1.setText(arrdate2 + "  " + arrhour2);
            cantTickets1.setText(cantTick2);
        }
        else{
            findViewById(R.id.backtrip).setVisibility(View.GONE);
        }
    }

}
