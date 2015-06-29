package com.tecpro.buseslep;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
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
    private GridView gridView;

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

    public ImageAdapter(Context c, ArrayList<Map<String,Object>> s , GridView grid) {
        mContext = c;
        seats = s;
        gridView = grid;
        for (int i = 0; i < seats.size(); i++) {
            int col = Integer.valueOf((String) seats.get(i).get("Columna"));
            int row = Integer.valueOf((String) seats.get(i).get("Fila"));
            int ocu = Integer.valueOf((String) seats.get(i).get("Ocupado"));
            int num = Integer.valueOf((String) seats.get(i).get("NumButaca"));
            int index = 5 * (col - 1) + (row - 1);
            if(index > 59){
                index = 59;
            }
            if (ocu == 0)
                seatsArr[index][0] = Free;
            else
                seatsArr[index][0] = Occupied;
            seatsArr[index][1] = num;
        }
        if(seatsArr[59][0] != None && seatsArr[58][0] == None && seatsArr[54][0] == None && seatsArr[53][0] == None && seatsArr[48][0] == None){
            seatsArr[48][0] = seatsArr[59][0];
            seatsArr[48][1] = seatsArr[59][1];
            seatsArr[59][0] = None;
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
        boolean n = true;
        for(int i = seatsArr.length-1; i> seatsArr.length - 6; i--){ //me fijo si la ultima fila es nula
            n = n &&seatsArr[i][0] == None;
        }
        if(n){
            Integer[][] auxArr = new Integer[seatsArr.length - 5][2]; //le saco la ultima fila
            for(int i = 0 ; i< seatsArr.length - 5; i++){
                auxArr[i][0] = seatsArr[i][0];
                auxArr[i][1] = seatsArr[i][1];

            }
            seatsArr = auxArr.clone();
        }

        boolean noneCol = true;
        int noneColCount = 0;
        for(int i = 4; i < seatsArr.length ; i += 5){ //se fija si la columna de la derecha esta vacia
            noneCol = noneCol && seatsArr[i][0] == None;
            noneColCount++;
        }

        if(noneCol){
            Integer[][] auxArr = new Integer[seatsArr.length - noneColCount][2]; //muevo los asientos para sacar la columna vacia
            int colcount = 1;
            int i2 = 0;
            for(int i = 0; i< seatsArr.length; i++){
                if(colcount == 5)
                    colcount = 1;
                else {
                    auxArr[i2][0] = seatsArr[i][0];
                    auxArr[i2][1] = seatsArr[i][1];
                    i2++;
                    colcount++;
                }
            }
            seatsArr = auxArr.clone();
            gridView.setNumColumns(4);
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

