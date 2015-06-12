package com.tecpro.buseslep.search_scheludes.select_city;

import android.content.Context;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tecpro.buseslep.R;
import com.tecpro.buseslep.search_scheludes.schedule.Schedule;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by agustin on 26/05/15.
 */
public class AdaptatorCity extends BaseAdapter {
    private LayoutInflater inflador; // Crea Layouts a partir del XML
    private static ArrayList<Map<String,Object>> cities;
    private TextView city;

    public AdaptatorCity(Context contexto, ArrayList<Map<String,Object>> cities) {
        this.cities = cities;
        inflador =(LayoutInflater)contexto
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    public View getView(int posicion, View vistaReciclada, ViewGroup padre) {
        String s = (String)cities.get(posicion).get("name");
        if (vistaReciclada == null) {
            vistaReciclada= inflador.inflate(R.layout.city_element, null);
        }
        city = (TextView) vistaReciclada.findViewById(R.id.txt_city_element);
        city.setText(s);
        return vistaReciclada;
    }
    public int getCount() {
        return cities.size();
    }

    public Object getItem(int posicion) {
        return cities.get(posicion);
    }

    public long getItemId(int posicion) {
        return posicion;
    }
}
