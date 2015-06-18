package com.tecpro.buseslep.utils;

import android.content.Context;

/**
 * Created by jacinto on 6/16/15.
 */
public class PreferencesUsing {

    private static SecurePreferences preferences;

    /*
     *PASARLE COMO PARAMETRO getApplication()
     *@param getApplication()
     */
    public PreferencesUsing(Context c){
        preferences = new SecurePreferences(c, "my-preferences", "BusesLepCordoba", true);
    }


    public void init(){
        if (preferences.getString("login") == null) {
            preferences.put("dni", "-");
            preferences.put("pass", "-");
            preferences.put("apellido", "-");
            preferences.put("nombre", "-");
            preferences.put("email", "-");
            preferences.put("login", "false");
        }
    }


    public String getDni(){
        return preferences.getString("dni");
    }

    public String getNombre(){
        return preferences.getString("nombre");
    }

    public String getApellido(){
        return preferences.getString("apellido");
    }

    public String getEmail(){
        return preferences.getString("email");
    }

    public String getPass(){
        return preferences.getString("pass");
    }

    public boolean isOnline() {
       if (preferences.getString("login").equals("true")) {
           return true;
       }
       return false;
    }

    public void logout(){
        preferences.put("login", "false");
    }


}
