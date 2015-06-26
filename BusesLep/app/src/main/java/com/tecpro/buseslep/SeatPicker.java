package com.tecpro.buseslep;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by agustin on 26/06/15.
 */
public class SeatPicker extends Activity {

    public static final int Occupied = 1;
    public static final int Free = 2;
    public static final int Selected = 3;
    public static int[] seatStates = new int[50];

    private int roundtrip;
    private String cityfrom,cityto, arrdate1,arrhour1,arrdate2,arrhour2,cantTick, totalPriceGo, totalPriceGoRet ;
    private int seatsToSelect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.seat_picker);

        Bundle extras = getIntent().getExtras();

        roundtrip = extras.getInt("roundtrip");
        cityfrom = extras.getString("city_from");
        cityto = extras.getString("city_to");
        arrdate1 = extras.getString("arrival_date1");
        arrhour1 = extras.getString("arrival_hour1");
        arrdate2 = extras.getString("arrival_date2");
        arrhour2 = extras.getString("arrival_hour2");
        cantTick = extras.getString("cant_tickets");
        totalPriceGoRet = extras.getString("priceGoRet");
        totalPriceGo = extras.getString("priceGo");

        seatsToSelect = Integer.valueOf(cantTick);
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                ImageView imageView = (ImageView) v;


                if(seatStates[position] == Free && seatsToSelect > 0) {
                    imageView.setImageResource(R.drawable.selected_seat);
                    seatStates[position] = Selected;
                    seatsToSelect--;
                }
                else{
                    if(seatStates[position] == Selected){
                        imageView.setImageResource(R.drawable.free_seat);
                        seatStates[position] = Free;
                        seatsToSelect++;
                    }
                }

               // Toast.makeText(SeatPicker.this, "" + position,
                 //       Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void loadPurchaseDetails(View view) {
        if(seatsToSelect>0){
            Toast.makeText(SeatPicker.this, "Quedan " + seatsToSelect + " asientos por seleccionar",
                           Toast.LENGTH_SHORT).show();
            return;
        }

        Intent i =  new Intent(this, PurchaseDetails.class);
        i.putExtra("city_from",cityfrom);
        i.putExtra("city_to",cityto);
        i.putExtra("arrival_date1",arrdate1);
        i.putExtra("arrival_hour1",arrhour1);
        i.putExtra("arrival_date2",arrdate2);
        i.putExtra("arrival_hour2",arrhour2);
        i.putExtra("cant_tickets",cantTick);
        i.putExtra("roundtrip",Integer.valueOf(roundtrip));
        i.putExtra("priceGo", totalPriceGo);//precio ida
        i.putExtra("priceGoRet", totalPriceGoRet); //precio ida vuelta
        startActivity(i);
    }
}
