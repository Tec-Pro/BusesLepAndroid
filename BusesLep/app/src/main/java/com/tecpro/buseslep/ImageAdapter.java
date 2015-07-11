package com.tecpro.buseslep;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
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
    private boolean driverAdded = false;

    public static final int Occupied =  R.drawable.occupied_seat;
    public static final int Free =  R.drawable.free_seat;
    public static final int Selected = R.drawable.selected_seat;
    public static final int None = R.drawable.none_seat;
    public static  final int Driver = R.drawable.driver_seat;

    public int color;

    public Integer[][] seatsArr = {
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
            {None,0}, {None,0}, {None,0}, {None,0}, {None,0},
            {None,0}, {None,0}, {None,0}, {None,0}, {None,0},
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
        if(seatsArr[59][0] != None && seatsArr[58][0] == None && seatsArr[54][0] == None && seatsArr[53][0] == None && seatsArr[48][0] == None){ //un temita
            seatsArr[58][0] = seatsArr[59][0];
            seatsArr[58][1] = seatsArr[59][1];
            seatsArr[59][0] = None;
            seatsArr[55][0] = Driver;
            driverAdded = true;
        }
        for(int i=0; i<seatsArr.length;i += 5){ //hago el espejo de la "matriz"
            int aux1;
            int aux2;
            int ind = i;
            for(int j = i+4; j > i+2; j-- ){
                aux1 =  seatsArr[j][0];
                aux2 = seatsArr[j][1];
                seatsArr[j][0] = seatsArr[ind][0];
                seatsArr[j][1] = seatsArr[ind][1];
                seatsArr[ind][0] = aux1;
                seatsArr[ind][1] = aux2;
                ind++;
            }
        }

        int noneCount = 0;
        while (seatsArr[noneCount][0] == None) { // cuenta la cantidad de lugares vacios de atras
            noneCount++;
        }
        noneCount = noneCount / 5;
        if (noneCount > 0){
            int i = 0;
            for (i = 0; i < seatsArr.length - noneCount * 5; i++) { //corre los asientos hacia atras, asi no queda nada en blanco
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
        for(int i = 0; i < seatsArr.length ; i += 5){ //se fija si la columna de la izq esta vacia
            noneCol = noneCol && seatsArr[i][0] == None;
            noneColCount++;
        }
        int numcols = 5;
        if(noneCol){
            Integer[][] auxArr = new Integer[seatsArr.length - noneColCount][2]; //muevo los asientos para sacar la columna vacia
            int colcount = 5;
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
            ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();
            layoutParams.width = convertDpToPixels(200,mContext); //this is in pixels
            gridView.setLayoutParams(layoutParams);
            numcols = 4;
        }
        if(!driverAdded) {
            boolean z = true;
            for (int i = seatsArr.length - 1; i > seatsArr.length - numcols - 1; i--) { //me fijo si la ultima fila es nula
                z = z && seatsArr[i][0] == None;
            }
            if (!z) {
                Integer[][] auxArr2 = new Integer[seatsArr.length + numcols][2]; //agrega una fila al ultimo
                for (int i = 0; i < seatsArr.length; i++) {
                    auxArr2[i][0] = seatsArr[i][0];
                    auxArr2[i][1] = seatsArr[i][1];
                }
                for (int i = seatsArr.length; i < auxArr2.length - 1; i++) {
                    auxArr2[i][0] = None;
                    auxArr2[i][1] = 0;
                }
                auxArr2[auxArr2.length - 1][0] = Driver; //agrego el conductor a la ultima fila
                auxArr2[auxArr2.length - 1][1] = 0;

                seatsArr = auxArr2.clone();
            } else {
                seatsArr[seatsArr.length - 1][0] = Driver;
                seatsArr[seatsArr.length - 1][1] = 0;
            }
        }



        Integer[][] aux= seatsArr.clone();
        int iAux=seatsArr.length-1;
        for(int i=0; i<seatsArr.length; i++){
            aux[iAux]=seatsArr[i];
            iAux--;
        }
        seatsArr= aux.clone();

    }

    public int getCount() {
        return seatsArr.length;
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
            imageView.setLayoutParams(new GridView.LayoutParams(convertDpToPixels(50,mContext), convertDpToPixels(50,mContext)));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(1, 1, 1, 1);

        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(seatsArr[position][0]);
        return imageView;
    }

    public static int convertDpToPixels(float dp, Context context){
        Resources resources = context.getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                resources.getDisplayMetrics()
        );
    }

}

