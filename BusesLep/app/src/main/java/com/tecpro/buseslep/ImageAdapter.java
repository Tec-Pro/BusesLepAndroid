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

    private Integer[] seatsArr = { None, None, None, None, None,
                                    None, None, None, None, None,
                                    None, None, None, None, None,
                                    None, None, None, None, None,
                                    None, None, None, None, None,
                                    None, None, None, None, None,
                                    None, None, None, None, None,
                                    None, None, None, None, None,
                                    None, None, None, None, None,
                                    None, None, None, None, None,
                                    None, None, None, None, None,
                                    None, None, None, None, None
    };

    public ImageAdapter(Context c, ArrayList<Map<String,Object>> s ) {
        mContext = c;
        seats = s;
        for(int i=0; i< seats.size(); i++){
            int col = Integer.valueOf((String)seats.get(i).get("Columna"));
            int row = Integer.valueOf((String)seats.get(i).get("Fila"));
            int ocu = Integer.valueOf((String)seats.get(i).get("Ocupado"));
            if(ocu == 0)
                seatsArr[5*(col-1) + (row-1)] = Free;
            else
                seatsArr[5*(col-1) + (row-1)] = Occupied;
        }
    }

    public int getCount() {
        return 60; //harcodeado por ahora
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

        SeatPicker.seatStates[position] = seatsArr[position];

        imageView.setImageResource(seatsArr[position]);
        return imageView;
    }

    // references to our images

}

