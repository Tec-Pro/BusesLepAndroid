package com.tecpro.buseslep;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by agustin on 26/05/15.
 */
public class AdaptatorLastSearch extends BaseAdapter {
    private LayoutInflater inflador; // Crea Layouts a partir del XML
    private TextView nombre, direccion;
    private RatingBar valoracion;
    private List searches;

    public AdaptatorLastSearch(Context contexto) {
        searches = Searches();
        inflador =(LayoutInflater)contexto
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static List Searches(){
        ArrayList resultado = new ArrayList();
        for (int i=0; i<20; i++){
            Search s = new Search("RioCuarto"+i,"Cordoba"+i,i,i+2);
            resultado.add(s);
        }
        return resultado;
    }

    public View getView(int posicion, View vistaReciclada,
                        ViewGroup padre) {
        Search s = (Search)searches.get(posicion);
        if (vistaReciclada == null) {
            vistaReciclada= inflador.inflate(R.layout.last_search_element, null);
        }
        nombre = (TextView) vistaReciclada.findViewById(R.id.localities);
        direccion = (TextView) vistaReciclada.findViewById(R.id.dates);

        nombre.setText(s.getDeparture() + " - " +s.getArrival());
        direccion.setText(s.getDepartureDate() + " > " + s.getArrivalDate());


        //valoracion.setRating(s.getValoracion());
        return vistaReciclada;
    }
    public int getCount() {
        return searches.size();
    }
    public Object getItem(int posicion) {
        return searches.get(posicion);
    }
    public long getItemId(int posicion) {
        return posicion;
    }
}
