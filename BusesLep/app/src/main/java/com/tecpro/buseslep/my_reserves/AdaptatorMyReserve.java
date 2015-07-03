package com.tecpro.buseslep.my_reserves;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tecpro.buseslep.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by agustin on 26/05/15.
 */
public class AdaptatorMyReserve extends BaseAdapter {
    private LayoutInflater inflador; // Crea Layouts a partir del XML
    private TextView destinyGo, dateGo, hourGo,cant,destinyRet, dateRet, hourRet;
    public static ArrayList<Map<String, Object>> reserves;

    public AdaptatorMyReserve(Context contexto, ArrayList<Map<String, Object>> reserves) {
        this.reserves = reserves;
        inflador =(LayoutInflater)contexto
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }



    public View getView(int posicion, View vistaReciclada,ViewGroup padre) {
        Map<String, Object> p = (Map<String, Object>)reserves.get(posicion).get("ida");

        if (vistaReciclada == null) {
            vistaReciclada = inflador.inflate(R.layout.reserve_element, null);
        }
        destinyGo = (TextView) vistaReciclada.findViewById(R.id.txt_destiny_go);
        hourGo = (TextView) vistaReciclada.findViewById(R.id.txt_hour_go);
        dateGo = (TextView) vistaReciclada.findViewById(R.id.txt_date_go);
        cant = (TextView) vistaReciclada.findViewById(R.id.txt_cant);



        String dateString= (String) p.get("fecha_sale");
        String[] aux= dateString.split("/");
        dateString= aux[2]+"/"+aux[1]+"/"+aux[0];

        destinyGo.setText((String) p.get("lugar"));
        hourGo.setText((String)p.get("hora_sale"));
        dateGo.setText(dateString);
        cant.setText((String) p.get("cantidad"));

        if(reserves.get(posicion).containsKey("vuelta")){
            Map<String, Object> ret = (Map<String, Object>)reserves.get(posicion).get("vuelta");
            destinyRet = (TextView) vistaReciclada.findViewById(R.id.txt_destiny_ret);
            hourRet = (TextView) vistaReciclada.findViewById(R.id.txt_hour_ret);
            dateRet = (TextView) vistaReciclada.findViewById(R.id.txt_date_ret);
            String dateStringRet= (String) ret.get("fecha_sale");
            String[] auxRet= dateStringRet.split("/");
            dateStringRet= auxRet[2]+"/"+auxRet[1]+"/"+auxRet[0];

            destinyRet.setText((String) ret.get("lugar"));
            hourRet.setText((String)ret.get("hora_sale"));
            dateRet.setText(dateStringRet);
            ((LinearLayout)  vistaReciclada.findViewById(R.id.layout_ret)).setVisibility(View.VISIBLE);

        }
        else
            ((LinearLayout)  vistaReciclada.findViewById(R.id.layout_ret)).setVisibility(View.GONE);

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
