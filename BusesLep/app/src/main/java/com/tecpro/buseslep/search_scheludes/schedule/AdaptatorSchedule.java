package com.tecpro.buseslep.search_scheludes.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tecpro.buseslep.R;
import com.tecpro.buseslep.Search;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agustin on 26/05/15.
 */
public class AdaptatorSchedule extends BaseAdapter {
    private LayoutInflater inflador; // Crea Layouts a partir del XML
    private TextView departDate, departTIme, arrivDate, arrivTime, status;
    public static List schedules;

    public AdaptatorSchedule(Context contexto) {
        schedules = Schedules();
        inflador =(LayoutInflater)contexto
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static List Schedules(){
        ArrayList result = new ArrayList();
        for (int i=0; i<20; i++){
            Schedule s = new Schedule( "29/05/2015",  "17:30", "29/05/2015","en destino", "20:30");
            result.add(s);
        }
        return result;
    }

    public View getView(int posicion, View vistaReciclada,
                        ViewGroup padre) {
        Schedule s = (Schedule)schedules.get(posicion);
        if (vistaReciclada == null) {
            vistaReciclada= inflador.inflate(R.layout.schedule_element, null);
        }
        departDate = (TextView) vistaReciclada.findViewById(R.id.departure_date);
        departTIme = (TextView) vistaReciclada.findViewById(R.id.departure_time);
        arrivDate = (TextView) vistaReciclada.findViewById(R.id.arrival_date);
        arrivTime = (TextView) vistaReciclada.findViewById(R.id.arrival_time);
        status = (TextView) vistaReciclada.findViewById(R.id.status);

        departDate.setText(s.getDepartDate());
        departTIme.setText(s.getDepartTIme());
        arrivDate.setText(s.getArrivDate());
        arrivTime.setText(s.getArrivTime());
        status.setText(s.getStatus());

        return vistaReciclada;
    }
    public int getCount() {
        return schedules.size();
    }
    public Object getItem(int posicion) {
        return schedules.get(posicion);
    }
    public long getItemId(int posicion) {
        return posicion;
    }
}
