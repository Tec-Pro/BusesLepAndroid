package com.tecpro.buseslep.my_reserves;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tecpro.buseslep.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by agustin on 26/05/15.
 */
public class AdaptatorMyReserve extends BaseAdapter {
    private LayoutInflater inflador; // Crea Layouts a partir del XML
    private TextView destiny, date, hour,cant;
    public static ArrayList<Map<String, Object>> reserves;

    public AdaptatorMyReserve(Context contexto, ArrayList<Map<String, Object>> reserves) {
        this.reserves = reserves;
        inflador =(LayoutInflater)contexto
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    public View getView(int posicion, View vistaReciclada,ViewGroup padre) {
        Map<String, Object> p = reserves.get(posicion);
        if (vistaReciclada == null) {
            vistaReciclada = inflador.inflate(R.layout.reserve_element, null);
        }
        destiny = (TextView) vistaReciclada.findViewById(R.id.txt_destiny);
        hour = (TextView) vistaReciclada.findViewById(R.id.txt_hour);
        date = (TextView) vistaReciclada.findViewById(R.id.txt_date);
        cant = (TextView) vistaReciclada.findViewById(R.id.txt_cant);



        String dateString= (String) p.get("fecha_sale");
        String[] aux= dateString.split("/");
        dateString= aux[2]+"/"+aux[1]+"/"+aux[0];

        destiny.setText((String) p.get("lugar"));
        hour.setText((String)p.get("hora_sale"));
        date.setText(dateString);
        cant.setText((String) p.get("cantidad"));



        return vistaReciclada;
    }
    public int getCount() {
        return reserves.size();
    }
    public Object getItem(int posicion) {
        return reserves.get(posicion);
    }
    public long getItemId(int posicion) {
        return posicion;
    }

}
