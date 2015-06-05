package com.tecpro.buseslep.batabase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by nico on 05/06/15.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(Context context) {
        super(context, "buseslep", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE searches(_id INTEGER PRIMARY KEY AUTOINCREMENT, city_origin TEXT NOT NULL, city_destiny TEXT NOT NULL, code_city_origin INTEGER NOT NULL, code_city_destiny INTEGER NOT NULL, date_go DATE NOT NULL, date_return DATE , number_tickets INTEGER NOT NULL, is_roundtrip INTEGER NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnte, int versionNue) {

    }

    /**
     * inserta un nuevo registro de busqueda, los campos date deben ser del estilo "2015-05-06, es decir YYYY-MM-DD"
     * @param city_origin
     * @param city_destiny
     * @param code_city_origin
     * @param code_city_destiny
     * @param date_go
     * @param date_return
     * @param number_tickets
     * @param is_roundtrip
     * @return
     */
    public long insert ( String city_origin,String city_destiny, Integer code_city_origin, Integer code_city_destiny, String date_go, String date_return, Integer number_tickets, boolean is_roundtrip){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues register = new ContentValues();
        register.put("city_origin", city_origin);
        register.put("city_destiny", city_destiny);
        register.put("code_city_origin", code_city_origin);
        register.put("code_city_destiny", code_city_destiny);
        register.put("date_go", date_go);
        register.put("date_return", date_return);
        register.put("number_tickets", number_tickets);
        register.put("is_roundtrip", is_roundtrip);
        long i = db.insert("searches",null,register);
        db.close();
        return i;
    }

    /**
     * retorno una lista de map,el map tiene:
     * city_origin, city_destiny, code_city_origin, code_city_destiny, date_go, date_return, number_tickets, is_roundtrip
     * date_return solo está presente SI Y SOLO SI is_roundtrip == 1, caso contrario no existe ese registro
     * @return
     */
    public List<Map<String,Object>> getSearches(){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql=  "SELECT city_origin, city_destiny, code_city_origin, code_city_destiny, date_go, date_return, number_tickets, is_roundtrip FROM searches;" ;
        Cursor c = db.rawQuery(sql,null);
        List<Map<String,Object>> ret= new LinkedList<>();
        //me aseguro que haya un elemento
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                Map<String,Object> map= new HashMap<>();
                map.put("city_origin", c.getString(0));
                map.put("city_destiny",c.getString(1));
                map.put("code_city_origin",c.getInt(2));
                map.put("code_city_destiny",c.getInt(3));
                map.put("date_go",c.getString(4));
                if(c.getInt(7) == 1) //solo si es ida y vuelta pongo la fecha de regreso
                    map.put("date_return",c.getString(5));
                map.put("number_tickets",c.getInt(6));
                map.put("is_roundtrip",c.getInt(7));
                ret.add(map);
            } while(c.moveToNext());
        }
        db.close();
        return ret;
    }

    /**
     * borra TODAS las busquedas realizadas
     */
    public void delete(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("searches",null,null);
        db.close();
    }

    /**
     * borra TODAS las busquedas realizadas pero que son menor a la fecha de hoy
     */
    public void deleteOldsSearches(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(Calendar.getInstance().getTime());
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("searches"," date_go < '"+date+"'",null);
        db.close();
    }
}