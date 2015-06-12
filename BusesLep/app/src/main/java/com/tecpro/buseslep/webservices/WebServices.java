package com.tecpro.buseslep.webservices;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Pair;
import android.widget.Toast;

import com.tecpro.buseslep.Dialog;
import com.tecpro.buseslep.search_scheludes.SearchScheludes;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by nico on 28/05/15.
 */
public class WebServices  {
    private static String NAMESPACE = "urn:LepWebServiceIntf-ILepWebService"; //figura claramente en el xml del webservice, ir mirando el binding
    private static String LocalidadesDesde = "LocalidadesDesde"; //nombre del metodo del ws, fijarse en el binding
    private static String LocalidadesHasta = "LocalidadesHasta"; //nombre del metodo del ws, fijarse en el binding
    private static String ListarHorarios = "ListarHorarios"; //nombre del metodo del ws, fijarse en el binding

    private static String VALIDATION_URI = "http://webservices.buseslep.com.ar:8080/WebServices/WebServiceLep.dll/soap/ILepWebService";//tiene que ser la uri que muestra el xml, por donde bindea
    private static SoapSerializationEnvelope envelope = null;
    private static SoapObject request = null;
    private static HttpTransportSE httpTransportSE = null;


    /**
     * obtengo todas las ciudaddes de origen desde el ws y retorno un par de dos cosas, una
     * es una lista de ciudad-id y la otra de los nombres solos,
     * @return
     */
    public static ArrayList<Map<String,Object>> getCities(Context context){
        String result;
        System.out.println("la concha de tu madre");
        ArrayList<Map<String,Object>> cities = new ArrayList<>();
        request = new SoapObject(NAMESPACE, LocalidadesDesde); //le digo que metodo voy a llamar
        request.addProperty("user","UsuarioLep"); //paso los parametros que pide el metodo
        request.addProperty("pass","Lep1234");
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //no se toda esta configuracion cual esta bien y cual mal
        envelope.enc = SoapSerializationEnvelope.ENC2003;
        envelope.setOutputSoapObject(request);
        httpTransportSE = new HttpTransportSE(VALIDATION_URI); //paso la uri donde transportaré
        System.out.println("la concha de tu madre 2");
        try {
            try{
            httpTransportSE.call(NAMESPACE + "#" + LocalidadesDesde, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
            }catch (Exception e){
                try {
                    httpTransportSE.call(NAMESPACE + "#" + LocalidadesDesde, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
                }catch (java.net.UnknownHostException unknown){
                    String message= "Ud. no posee conexión de internet; \n acceda a través de una red wi-fi o de su prestadora telefónica";
                    Intent intentDialog = new Intent(context, Dialog.class);
                    intentDialog.putExtra("message",message);
                    intentDialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentDialog);
                }
            }
            result= (String)envelope.getResponse();
            JSONArray json= new JSONObject(result).getJSONArray("Data");
            int i=0;
            while(i<json.length()){
                JSONObject jsonObject= json.getJSONObject(i);
                HashMap<String,Object> map= new HashMap<>();
                map.put("id",jsonObject.getInt("ID_Localidad"));
                map.put("name",jsonObject.getString("Localidad"));
                cities.add(map);
                i++;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return cities;
    }

    /**
     * obtengo todas las ciudaddes de destino desde el ws y retorno una lista de ciudades, el atributo que toma es el id de origen
     * @return
     */
    public static ArrayList<Map<String,Object>> getDestinationCities(Integer idOrigin, Context context){
        ArrayList<Map<String,Object>> cities = new ArrayList<>();
        if(idOrigin!=-1) {
            String result;
            request = new SoapObject(NAMESPACE, LocalidadesHasta); //le digo que metodo voy a llamar
            request.addProperty("user", "UsuarioLep"); //paso los parametros que pide el metodo
            request.addProperty("pass", "Lep1234");
            request.addProperty("IdLocalidadOrigen", idOrigin);
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //no se toda esta configuracion cual esta bien y cual mal
            envelope.enc = SoapSerializationEnvelope.ENC2003;
            envelope.setOutputSoapObject(request);
            httpTransportSE = new HttpTransportSE(VALIDATION_URI); //paso la uri donde transportaré
            try {
                try {
                httpTransportSE.call(NAMESPACE + "#" + LocalidadesHasta, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
                 }catch (Exception e){
                    try{
                    httpTransportSE.call(NAMESPACE + "#" + LocalidadesHasta, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
                    }catch (java.net.UnknownHostException unknown){
                        String message= "Ud. no posee conexión de internet; \n acceda a través de una red wi-fi o de su prestadora telefónica";
                        Intent intentDialog = new Intent(context, Dialog.class);
                        intentDialog.putExtra("message",message);
                        intentDialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentDialog);
                    }
                }
                result = (String) envelope.getResponse();
                JSONArray json= new JSONObject(result).getJSONArray("Data");
                int i=0;
                while(i<json.length()){
                    JSONObject jsonObject= json.getJSONObject(i);
                    HashMap<String,Object> map= new HashMap<>();
                    map.put("id",jsonObject.getInt("id_localidad_destino"));
                    map.put("name",jsonObject.getString("hasta"));
                    cities.add(map);
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cities;
    }

    public static ArrayList<Map<String,Object>> getSchedules(Integer idOrigin, Integer idDestiny, String date,Context context){
            ArrayList<Map<String,Object>> ret= new ArrayList<>();
            request = new SoapObject(NAMESPACE, ListarHorarios); //le digo que metodo voy a llamar
            request.addProperty("user", "UsuarioLep"); //paso los parametros que pide el metodo
            request.addProperty("pass", "Lep1234");
            request.addProperty("IdLocalidadOrigen", idOrigin);
            request.addProperty("IdLocalidadDestino", idDestiny);
            request.addProperty("Fecha", date);
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //no se toda esta configuracion cual esta bien y cual mal
            envelope.enc = SoapSerializationEnvelope.ENC2003;
            envelope.setOutputSoapObject(request);
            httpTransportSE = new HttpTransportSE(VALIDATION_URI); //paso la uri donde transportaré
            try {
                try{
                httpTransportSE.call(NAMESPACE + "#" + ListarHorarios, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
                }catch (Exception e){
                    try{
                    httpTransportSE.call(NAMESPACE + "#" + ListarHorarios, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
                    }catch (java.net.UnknownHostException unknown){
                        String message= "Ud. no posee conexión de internet; \n acceda a través de una red wi-fi o de su prestadora telefónica";
                        Intent intentDialog = new Intent(context, Dialog.class);
                        intentDialog.putExtra("message",message);
                        intentDialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentDialog);
                    }
                }
                String result = (String) envelope.getResponse();
                JSONArray json= new JSONObject(result).getJSONArray("Data");
                int i=0;
                while(i<json.length()){
                     Map<String, Object> map= new HashMap<>();
                    JSONObject jsonObject= json.getJSONObject(i);
                    map.put("estado",jsonObject.get("ServicioPrestado"));
                    map.put("fecha_llega",jsonObject.getString("FechaHoraLlegada").split(" ")[0].replace('-', '/'));
                    map.put("hora_llega",jsonObject.getString("FechaHoraLlegada").split(" ")[1].substring(0, 5));
                    map.put("fecha_sale",jsonObject.getString("fechahora").split(" ")[0].replace('-', '/'));
                    map.put("hora_sale", jsonObject.getString("fechahora").split(" ")[1].substring(0, 5));
                    map.put("codigo",jsonObject.getString("cod_horario"));
                    ret.add(map);
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        return ret;
    }

}
