package com.tecpro.buseslep.search_scheludes.schedule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tecpro.buseslep.R;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by agustin on 26/05/15.
 */
public class AdaptatorTickets extends BaseAdapter {
    private LayoutInflater inflador; // Crea Layouts a partir del XML
    private static List<String> tickets;
    private TextView number;

    public AdaptatorTickets(Context contexto) {
        tickets= new LinkedList<>();
        tickets.add("1");
        tickets.add("2");
        tickets.add("3");
        tickets.add("4");
        tickets.add("5");
        inflador =(LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int posicion, View vistaReciclada, ViewGroup padre) {
        String s = tickets.get(posicion);
        if (vistaReciclada == null) {
            vistaReciclada= inflador.inflate(R.layout.number_element, null);
        }
        number = (TextView) vistaReciclada.findViewById(R.id.txt_number_element);
        number.setText(s);
        return vistaReciclada;
    }
    public int getCount() {
        return tickets.size();
    }

    public Object getItem(int posicion) {
        return tickets.get(posicion);
    }

    public long getItemId(int posicion) {
        return posicion;
    }
}
