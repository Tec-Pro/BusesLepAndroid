package com.tecpro.buseslep;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.tecpro.buseslep.batabase.DataBaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by agustin on 26/05/15.
 */
public class AdaptatorLastSearch extends BaseAdapter {
    private LayoutInflater inflador; // Crea Layouts a partir del XML
    private TextView arrivalLoc, departureLoc, depDate, arrDate;
    private RatingBar valoracion;
    //public static List searches;
    private DataBaseHelper dbh;
    private List<Map<String,Object>> searches;

    public AdaptatorLastSearch(Context contexto, DataBaseHelper dbh) {
        //searches = Searches();
        this.dbh = dbh;

        dbh.deleteOldsSearches();
     //   dbh.insert("Rio Cuarto", "Cordoba", 22, 23, "15-7-2015", "16-7-2015", 1, true);
       // dbh.insert("Rio Cuarto", "Cordoba2", 24, 25, "14-7-2015", "20-7-2015", 1, true);
        searches = dbh.getSearches();

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
        Map<String,Object> s = searches.get(posicion);
        if (vistaReciclada == null) {
            vistaReciclada= inflador.inflate(R.layout.last_search_element, null);
        }
        departureLoc = (TextView) vistaReciclada.findViewById(R.id.departureLoc);
        arrivalLoc = (TextView) vistaReciclada.findViewById(R.id.arrivalLoc);

        arrDate = (TextView) vistaReciclada.findViewById(R.id.arrDate);
        depDate = (TextView) vistaReciclada.findViewById(R.id.depDate);

        departureLoc.setText((String)s.get("city_origin"));
        arrivalLoc.setText((String)s.get("city_destiny"));
        depDate.setText((String) s.get("date_go"));
        arrDate.setText((String)s.get("date_return"));


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
