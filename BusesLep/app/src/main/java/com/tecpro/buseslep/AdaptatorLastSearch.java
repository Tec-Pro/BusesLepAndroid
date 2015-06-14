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
    private final int StringMaxSize = 20;

    public AdaptatorLastSearch(Context contexto,  List<Map<String,Object>> searches) {
        //searches = Searches();
        this.searches = searches;

        inflador =(LayoutInflater)contexto
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public View getView(int posicion, View vistaReciclada,
                        ViewGroup padre) {
        Map<String,Object> s = searches.get(posicion);
        if (vistaReciclada == null) {
            vistaReciclada= inflador.inflate(R.layout.last_search_element, null);
        }
        departureLoc = (TextView) vistaReciclada.findViewById(R.id.departureLoc);


        arrDate = (TextView) vistaReciclada.findViewById(R.id.arrDate);
        depDate = (TextView) vistaReciclada.findViewById(R.id.depDate);
        String origin = (String)s.get("city_origin");
        if(origin.length()>StringMaxSize)
            origin = origin.substring(0, StringMaxSize);
        String destiny = (String)s.get("city_destiny");
        if(destiny.length()>StringMaxSize)
            destiny = destiny.substring(0,StringMaxSize);
        departureLoc.setText(origin +"  -  " + destiny);
        String dateGo = (String) s.get("date_go");
        depDate.setText(changeDateFormat(dateGo));
        String dateRet = (String) s.get("date_return");
        arrDate.setText(changeDateFormat(dateRet));


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

    private String changeDateFormat(String oldDate){
        String[] dateSplit;
        if(oldDate != null) {
            dateSplit = oldDate.split("-");
            if(dateSplit.length == 3)
                return  dateSplit[2] + "/" + dateSplit[1] + "/" + dateSplit[0];
        }
        return "";
    }
}
