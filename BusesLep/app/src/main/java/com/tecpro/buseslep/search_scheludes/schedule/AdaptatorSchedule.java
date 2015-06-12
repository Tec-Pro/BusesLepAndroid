package com.tecpro.buseslep.search_scheludes.schedule;

import android.content.Context;
import android.graphics.Color;
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
import java.util.Map;

/**
 * Created by agustin on 26/05/15.
 */
public class AdaptatorSchedule extends BaseAdapter {
    private LayoutInflater inflador; // Crea Layouts a partir del XML
    private TextView departDate, departTIme, arrivDate, arrivTime, status;
    public static List<Map> schedules;

    public AdaptatorSchedule(Context contexto, ArrayList<Map<String,Object>> schedules) {
        this.schedules = Schedules(schedules);
        inflador =(LayoutInflater)contexto
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * transformo una lista de map a una lista de Schedule
     * @param schedules
     * @return
     */
    static List Schedules( ArrayList<Map<String,Object>> schedules){
        ArrayList result = new ArrayList();
        for (int i=0; i<schedules.size();i++){
            String dateGo= (String)schedules.get(i).get("fecha_sale");
            String[] aux= dateGo.split("/");
            dateGo= aux[2]+"/"+aux[1]+"/"+aux[0];
            String dateRet= (String)schedules.get(i).get("fecha_llega");
            String[] auxRet= dateRet.split("/");
            if (auxRet.length==3)
                dateRet= auxRet[2]+"/"+auxRet[1]+"/"+auxRet[0];
            Schedule s = new Schedule( dateGo,  (String)schedules.get(i).get("hora_sale"), dateRet,(String)schedules.get(i).get("estado"), (String)schedules.get(i).get("hora_llega"), (String)schedules.get(i).get("codigo"));
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
        if(!(s.getStatus().contains("viaje")||s.getStatus().contains("destino") ||s.getStatus().contains("completo")) )//si esta en viaje o destino lo pongo en rojo
            status.setTextColor(Color.BLUE);
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
