package com.tecpro.buseslep;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by agustin on 26/06/15.
 */
public class ImageAdapter extends BaseAdapter {
    private Context mContext;
    private  ArrayList<Map<String,Object>> seats;

    public static final int Occupied =  R.drawable.occupied_seat;
    public static final int Free =  R.drawable.free_seat;
    public static final int Selected = R.drawable.selected_seat;
    public static final int None = R.drawable.none_seat;

    public int color;

    public Integer[][] seatsArr = { {None,0}, {None,0}, {None,0}, {None,0}, {None,0},
            {None,0}, {None,0}, {None,0}, {None,0}, {None,0},
            {None,0}, {None,0}, {None,0}, {None,0}, {None,0},
            {None,0}, {None,0}, {None,0}, {None,0}, {None,0},
            {None,0}, {None,0}, {None,0}, {None,0}, {None,0},
            {None,0}, {None,0}, {None,0}, {None,0}, {None,0},
            {None,0}, {None,0}, {None,0}, {None,0}, {None,0},
            {None,0}, {None,0}, {None,0}, {None,0}, {None,0},
            {None,0}, {None,0}, {None,0}, {None,0}, {None,0},
            {None,0}, {None,0}, {None,0}, {None,0}, {None,0},
            {None,0}, {None,0}, {None,0}, {None,0}, {None,0},
            {None,0}, {None,0}, {None,0}, {None,0}, {None,0}
    };

    public ImageAdapter(Context c, ArrayList<Map<String,Object>> s ) {
        mContext = c;
        seats = s;
        for (int i = 0; i < seats.size(); i++) {
            int col = Integer.valueOf((String) seats.get(i).get("Columna"));
            int row = Integer.valueOf((String) seats.get(i).get("Fila"));
            int ocu = Integer.valueOf((String) seats.get(i).get("Ocupado"));
            int num = Integer.valueOf((String) seats.get(i).get("NumButaca"));
            if (ocu == 0)
                seatsArr[5 * (col - 1) + (row - 1)][0] = Free;
            else
                seatsArr[5 * (col - 1) + (row - 1)][0] = Occupied;
            seatsArr[5 * (col - 1) + (row - 1)][1] = num;
        }
        int noneCount = 0;
        while (seatsArr[noneCount][0] == None) { // cuenta la cantidad de lugares vacios de atras
            noneCount++;
        }
        noneCount = noneCount / 5;
        if (noneCount > 0){
            int i = 0;
            for (i = 0; i < seatsArr.length - noneCount * 5; i++) { //corre los acientos hacia atras, asi no queda nada en blanco
                seatsArr[i][0] = seatsArr[noneCount * 5 + i][0];
                seatsArr[i][1] = seatsArr[noneCount * 5 + i][1];
                seatsArr[noneCount * 5 + i][0] = None;
            }
            Integer[][] auxArr = new Integer[i][2];
            for(int j = 0; j < auxArr.length; j++){ //achico el arreglo para sacarle los lugares de adelante que quedaron vacios
                auxArr[j][0] = seatsArr[j][0];
                auxArr[j][1] = seatsArr[j][1];
            }
            seatsArr = auxArr.clone();
         }
    }

    public int getCount() {
        return seatsArr.length; //harcodeado por ahora
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(seatsArr[position][0]);
        return imageView;
    }


}

