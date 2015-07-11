package com.tecpro.buseslep.webservices;

import android.content.Context;
import android.content.Intent;
import android.os.StrictMode;
import android.util.Log;
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
    private static String ObtenerTarifaTramo = "ObtenerTarifaTramo"; //obtengo precio
    private static String LoginWSFunction = "login"; //funcion para loguearce
    private static String ModificarContrasena = "ModificarContraseña";
    private static String RegistrarUsuario = "RegistrarUsuario";
    private static String RecuperarContrasena = "RecuperarContrasena";
    private static String EditarPerfil = "EditarPerfilCliente";
    private static String AgregarReserva = "AgregarReserva";
    private static String ListarMisReserva = "ListarMisReserva";
    private static String ListarMisCompras = "ListarMisCompras";
    private static String EstadoButacasPlantaHorario = "EstadoButacasPlantaHorario";
    private static String SeleccionarButaca = "SeleccionarButaca";
    private static String AnularReservas = "AnularReservas";
    private static String PasarReservasaPrepago = "PasarReservasaPrepago";
    private static String EliminarButacaSeleccionada = "EliminarButacaSeleccionada";

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
        ArrayList<Map<String,Object>> cities = new ArrayList<>();
        request = new SoapObject(NAMESPACE, LocalidadesDesde); //le digo que metodo voy a llamar
        request.addProperty("userWS","UsuarioLep"); //paso los parametros que pide el metodo
        request.addProperty("passWS","Lep1234");
        request.addProperty("id_plataforma",1);
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //no se toda esta configuracion cual esta bien y cual mal
        envelope.enc = SoapSerializationEnvelope.ENC2003;
        envelope.setOutputSoapObject(request);
        httpTransportSE = new HttpTransportSE(VALIDATION_URI); //paso la uri donde transportaré
        try {
            try{
            httpTransportSE.call(NAMESPACE + "#" + LocalidadesDesde, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
            }catch (Exception e){
                try {
                    httpTransportSE.call(NAMESPACE + "#" + LocalidadesDesde, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
                }catch (java.net.UnknownHostException | java.net.SocketTimeoutException ex){
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
            request.addProperty("userWS","UsuarioLep"); //paso los parametros que pide el metodo
            request.addProperty("passWS","Lep1234");
            request.addProperty("IdLocalidadOrigen", idOrigin);
            request.addProperty("id_plataforma",1);
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
                    }catch (java.net.UnknownHostException | java.net.SocketTimeoutException ex){
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

    public static ArrayList<Map<String,Object>> getSchedules(Integer idOrigin, Integer idDestiny, String date,String dni,Context context){
            ArrayList<Map<String,Object>> ret= new ArrayList<>();
            request = new SoapObject(NAMESPACE, ListarHorarios); //le digo que metodo voy a llamar
            request.addProperty("userWS","UsuarioLep"); //paso los parametros que pide el metodo
            request.addProperty("passWS","Lep1234");
            request.addProperty("IdLocalidadOrigen", idOrigin);
            request.addProperty("IdLocalidadDestino", idDestiny);
            request.addProperty("Fecha", date);
            request.addProperty("id_plataforma",1);
        if(!(dni== null)){
            request.addProperty("DNI",Integer.valueOf(dni));
        }
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
                    }catch (java.net.UnknownHostException | java.net.SocketTimeoutException ex){
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
                    //System.out.println(jsonObject);
                    map.put("estado", jsonObject.get("ServicioPrestado"));
                    map.put("fecha_llega",jsonObject.getString("FechaHoraLlegada").split(" ")[0].replace('-', '/'));
                    map.put("hora_llega",jsonObject.getString("FechaHoraLlegada").split(" ")[1].substring(0, 5));
                    map.put("fecha_sale",jsonObject.getString("fechahora").split(" ")[0].replace('-', '/'));
                    map.put("hora_sale", jsonObject.getString("fechahora").split(" ")[1].substring(0, 5));
                    map.put("codigo",jsonObject.getString("cod_horario"));
                    map.put("Id_Empresa",jsonObject.getString("Id_Empresa"));
                    map.put("id_destino",jsonObject.getString("id_destino"));
                    ret.add(map);
                    i++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        return ret;
    }

    /**
     * obtengo el precio
     * @return
     */
    public static Map<String,String> getPrice(Integer idOrigin, Integer idDestiny,Context context){
        String result;
        Map<String,String> price = new HashMap<String,String>();
        request = new SoapObject(NAMESPACE, ObtenerTarifaTramo); //le digo que metodo voy a llamar
        request.addProperty("userWS","UsuarioLep"); //paso los parametros que pide el metodo
        request.addProperty("passWS","Lep1234");
        request.addProperty("ID_LocalidadOrigen", idOrigin);
        request.addProperty("ID_LocalidadDestino", idDestiny);
        request.addProperty("id_Plataforma", 1);
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //no se toda esta configuracion cual esta bien y cual mal
        envelope.enc = SoapSerializationEnvelope.ENC2003;
        envelope.setOutputSoapObject(request);
        httpTransportSE = new HttpTransportSE(VALIDATION_URI); //paso la uri donde transportaré
        try {
            try{
                httpTransportSE.call(NAMESPACE + "#" + ObtenerTarifaTramo, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
            }catch (Exception e){
                try {
                    httpTransportSE.call(NAMESPACE + "#" + ObtenerTarifaTramo, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
                }catch (java.net.UnknownHostException | java.net.SocketTimeoutException ex){
                    String message= "Ud. no posee conexión de internet; \n acceda a través de una red wi-fi o de su prestadora telefónica";
                    Intent intentDialog = new Intent(context, Dialog.class);
                    intentDialog.putExtra("message",message);
                    intentDialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentDialog);
                }
            }
            result= (String)envelope.getResponse();
            String[] auxPrice = result.split(" - ");
            String priceGo= auxPrice[0].split(":")[1];
            String priceGoRet= auxPrice[1].split(":")[1];
            price.put("priceGo",priceGo);
            price.put("priceGoRet",priceGoRet);


        }
        catch(Exception e){
            e.printStackTrace();
        }
        return price;
    }

    public static ArrayList<Map<String,Object>> callLogin(String user, String pass,Context context){
        String result;
        ArrayList<Map<String,Object>> cities = new ArrayList<>();
        request = new SoapObject(NAMESPACE, LoginWSFunction); //le digo que metodo voy a llamar
        request.addProperty("userWS","UsuarioLep"); //paso los parametros que pide el metodo
        request.addProperty("passWS","Lep1234");
        request.addProperty("DNI", user);
        request.addProperty("Pass", pass);
        request.addProperty("id_plataforma", 1);
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //no se toda esta configuracion cual esta bien y cual mal
        envelope.enc = SoapSerializationEnvelope.ENC2003;
        envelope.setOutputSoapObject(request);
        httpTransportSE = new HttpTransportSE(VALIDATION_URI); //paso la uri donde transportaré
        try {
            try{
                httpTransportSE.call(NAMESPACE + "#" + LoginWSFunction, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
            }catch (Exception e){
                try {
                    httpTransportSE.call(NAMESPACE + "#" + LoginWSFunction, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
                }catch (java.net.UnknownHostException | java.net.SocketTimeoutException ex){
                    String message= "Ud. no posee conexión de internet; \n acceda a través de una red wi-fi o de su prestadora telefónica";
                    Intent intentDialog = new Intent(context, Dialog.class);
                    intentDialog.putExtra("message",message);
                    intentDialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentDialog);
                }
            }
            result= String.valueOf(envelope.getResponse());
            JSONArray json= new JSONObject(result).getJSONArray("Data");
            int i=0;
            while(i<json.length()){
                JSONObject jsonObject= json.getJSONObject(i);
                HashMap<String,Object> map= new HashMap<>();
                map.put("DNI",jsonObject.getInt("DNI"));
                map.put("Apellido",jsonObject.getString("Apellido"));
                map.put("Nombre",jsonObject.getString("Nombre"));
                map.put("Email",jsonObject.getString("Email"));
                cities.add(map);
                i++;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return cities;
    }

    public static ArrayList<Map<String,Object>> CallModificarContraseña(int dni, String email, String pas, String nuevapass,Context context){
        String result;
        ArrayList<Map<String,Object>> cities = new ArrayList<>();
        request = new SoapObject(NAMESPACE, ModificarContrasena); //le digo que metodo voy a llamar
        request.addProperty("userWS","UsuarioLep"); //paso los parametros que pide el metodo
        request.addProperty("passWS","Lep1234");
        request.addProperty("DNI", dni);
        request.addProperty("Email", email);
        request.addProperty("Pas", pas);
        request.addProperty("NuevaPass", nuevapass);
        request.addProperty("id_Plataforma", 1);
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //no se toda esta configuracion cual esta bien y cual mal
        envelope.enc = SoapSerializationEnvelope.ENC2003;
        envelope.setOutputSoapObject(request);
        httpTransportSE = new HttpTransportSE(VALIDATION_URI); //paso la uri donde transportaré
        try {
            try{
                httpTransportSE.call(NAMESPACE + "#" + ModificarContrasena, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
            }catch (Exception e){
                try {
                    httpTransportSE.call(NAMESPACE + "#" + ModificarContrasena, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
                }catch (java.net.UnknownHostException | java.net.SocketTimeoutException ex){
                    String message= "Ud. no posee conexión de internet; \n acceda a través de una red wi-fi o de su prestadora telefónica";
                    Intent intentDialog = new Intent(context, Dialog.class);
                    intentDialog.putExtra("message",message);
                    intentDialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentDialog);
                }
            }
            result= String.valueOf(envelope.getResponse());
            if (result.equals("-1")){
                HashMap<String,Object> map= new HashMap<>();
                map.put("ret","-1");
                cities.add(map);
            } else {
                if (result.equals("1")) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("ret", "1");
                    cities.add(map);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return cities;
    }

    public static ArrayList<Map<String,Object>> CallRegistrarUsuario(int dni, String pass, String nombre, String ape, String email, Context context){
        String result;
        ArrayList<Map<String,Object>> cities = new ArrayList<>();
        request = new SoapObject(NAMESPACE, RegistrarUsuario); //le digo que metodo voy a llamar
        request.addProperty("userWS","UsuarioLep"); //paso los parametros que pide el metodo
        request.addProperty("passWS","Lep1234");
        request.addProperty("PDni", dni);
        request.addProperty("pass", pass);
        request.addProperty("Nombre", nombre);
        request.addProperty("Apellido", ape);
        request.addProperty("Email", email);
        request.addProperty("id_Plataforma", 1);
        //string 	RegistrarUsuario( int PDni, string pass, string Nombre, string Apellido, string Email, int id_Plataforma)
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //no se toda esta configuracion cual esta bien y cual mal
        envelope.enc = SoapSerializationEnvelope.ENC2003;
        envelope.setOutputSoapObject(request);
        httpTransportSE = new HttpTransportSE(VALIDATION_URI); //paso la uri donde transportaré
        try {
            try{
                httpTransportSE.call(NAMESPACE + "#" + RegistrarUsuario, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
            }catch (Exception e){
                try {
                    httpTransportSE.call(NAMESPACE + "#" + RegistrarUsuario, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
                }catch (java.net.UnknownHostException | java.net.SocketTimeoutException ex){
                    String message= "Ud. no posee conexión de internet; \n acceda a través de una red wi-fi o de su prestadora telefónica";
                    Intent intentDialog = new Intent(context, Dialog.class);
                    intentDialog.putExtra("message",message);
                    intentDialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentDialog);
                }
            }
            result= String.valueOf(envelope.getResponse());
            if (result.equals("-1")){
                HashMap<String,Object> map= new HashMap<>();
                map.put("ret","-1");
                cities.add(map);
            } else {
                if (result.equals("1")) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("ret", "1");
                    cities.add(map);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return cities;
    }

    public static ArrayList<Map<String,Object>> CallRecuperarContrasena(int dni, String email, Context context){
        String result;
        ArrayList<Map<String,Object>> cities = new ArrayList<>();
        request = new SoapObject(NAMESPACE, RecuperarContrasena); //le digo que metodo voy a llamar
        request.addProperty("userWS","UsuarioLep"); //paso los parametros que pide el metodo
        request.addProperty("passWS","Lep1234");
        request.addProperty("Dni", dni);
        request.addProperty("Email", email);
        request.addProperty("id_Plataforma", 1);
        //string 	RegistrarUsuario( int PDni, string pass, string Nombre, string Apellido, string Email, int id_Plataforma)
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //no se toda esta configuracion cual esta bien y cual mal
        envelope.enc = SoapSerializationEnvelope.ENC2003;
        envelope.setOutputSoapObject(request);
        httpTransportSE = new HttpTransportSE(VALIDATION_URI); //paso la uri donde transportaré
        try {
            try{
                httpTransportSE.call(NAMESPACE + "#" + RecuperarContrasena, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
            }catch (Exception e){
                try {
                    httpTransportSE.call(NAMESPACE + "#" + RecuperarContrasena, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
                }catch (java.net.UnknownHostException | java.net.SocketTimeoutException ex){
                    String message= "Ud. no posee conexión de internet; \n acceda a través de una red wi-fi o de su prestadora telefónica";
                    Intent intentDialog = new Intent(context, Dialog.class);
                    intentDialog.putExtra("message",message);
                    intentDialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentDialog);
                }
            }
            result= String.valueOf(envelope.getResponse());
            System.out.println(result + "sssssssssssssssssss");
            if (result.equals("-1")){
                HashMap<String,Object> map= new HashMap<>();
                map.put("ret","-1");
                cities.add(map);
            } else {
                if (result.equals("1")) {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("ret", "1");
                    cities.add(map);
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return cities;
    }

   public static ArrayList<Map<String,Object>> CallEditarPerfil(int dni, String nombre, String ape, String email, Context context) {
        String result;
        ArrayList<Map<String, Object>> cities = new ArrayList<>();
        request = new SoapObject(NAMESPACE, EditarPerfil); //le digo que metodo voy a llamar
        request.addProperty("userWS", "UsuarioLep"); //paso los parametros que pide el metodo
        request.addProperty("passWS", "Lep1234");
        request.addProperty("DNI", dni);
        request.addProperty("Nombre", nombre);
        request.addProperty("Apellido", ape);
        request.addProperty("Email", email);
        request.addProperty("id_Plataforma", 1);
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //no se toda esta configuracion cual esta bien y cual mal
        envelope.enc = SoapSerializationEnvelope.ENC2003;
        envelope.setOutputSoapObject(request);
        httpTransportSE = new HttpTransportSE(VALIDATION_URI); //paso la uri donde transportaré
        try {
            try {
                httpTransportSE.call(NAMESPACE + "#" + EditarPerfil, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
            } catch (Exception e) {
                try {
                    httpTransportSE.call(NAMESPACE + "#" + EditarPerfil, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
                }catch (java.net.UnknownHostException | java.net.SocketTimeoutException ex){
                    String message = "Ud. no posee conexión de internet; \n acceda a través de una red wi-fi o de su prestadora telefónica";
                    Intent intentDialog = new Intent(context, Dialog.class);
                    intentDialog.putExtra("message", message);
                    intentDialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentDialog);
                }
            }
            result= String.valueOf(envelope.getResponse());
            System.out.println("ssssss "+result+" daaaaaaaaaaaaaaaaa");
            if (result.equals("-1")){
                HashMap<String,Object> map= new HashMap<>();
                map.put("ret","-1");
                cities.add(map);
            } else {

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("ret", "1");
                    cities.add(map);

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return cities;
    }


    public static String CallAgregarReserva(boolean isRoundTrip, String dni, int IDEmpresaIda, int IDDestinoIda, int CodHorarioIda,int IdLocalidadDesdeIda, int IdlocalidadHastaIda, int CantidadIda,int IDEmpresaVuelta, int IDDestinoVuelta,int CodHorarioVuelta, int IdLocalidadDesdeVuelta, int IdlocalidadHastaVuelta, int CantidadVuelta, int EsCompra, Context context){
        String result = "";

       // ArrayList<Map<String,Object>> resultCode = new ArrayList<>();
        request = new SoapObject(NAMESPACE, AgregarReserva); //le digo que metodo voy a llamar
        request.addProperty("userWS","UsuarioLep"); //paso los parametros que pide el metodo
        request.addProperty("passWS","Lep1234");
        request.addProperty("Dni", dni);
        request.addProperty("IDEmpresaIda", IDEmpresaIda);
        request.addProperty("IDDestinoIda", IDDestinoIda);
        request.addProperty("CodHorarioIda", CodHorarioIda);
        request.addProperty("IdLocalidadDesdeIda", IdLocalidadDesdeIda);
        request.addProperty("IdlocalidadHastaIda", IdlocalidadHastaIda);
        request.addProperty("CantidadIda", CantidadIda);
        if(isRoundTrip) {
            request.addProperty("IDEmpresaVuelta", IDEmpresaVuelta);
            request.addProperty("IDDestinoVuelta", IDDestinoVuelta);
            request.addProperty("CodHorarioVuelta", CodHorarioVuelta);
            request.addProperty("IdLocalidadDesdeVuelta", IdLocalidadDesdeVuelta);
            request.addProperty("IdlocalidadHastaVuelta", IdlocalidadHastaVuelta);
            request.addProperty("CantidadVuelta", CantidadVuelta);
        }
        else{
            request.addProperty("IDEmpresaVuelta", 0);
            request.addProperty("IDDestinoVuelta", 0);
            request.addProperty("CodHorarioVuelta", 0);
            request.addProperty("IdLocalidadDesdeVuelta", 0);
            request.addProperty("IdlocalidadHastaVuelta", 0);
            request.addProperty("CantidadVuelta", 0);
        }
        request.addProperty("Id_Plataforma", 1);
        request.addProperty("EsCompra", EsCompra);

        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //no se toda esta configuracion cual esta bien y cual mal
        envelope.enc = SoapSerializationEnvelope.ENC2003;
        envelope.setOutputSoapObject(request);
        httpTransportSE = new HttpTransportSE(VALIDATION_URI); //paso la uri donde transportaré
        try {
            try{
                httpTransportSE.call(NAMESPACE + "#" + AgregarReserva, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
            }catch (Exception e){
                try {
                    httpTransportSE.call(NAMESPACE + "#" + AgregarReserva, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
                }catch (java.net.UnknownHostException | java.net.SocketTimeoutException ex){
                    String message= "Ud. no posee conexión de internet; \n acceda a través de una red wi-fi o de su prestadora telefónica";
                    Intent intentDialog = new Intent(context, Dialog.class);
                    intentDialog.putExtra("message",message);
                    intentDialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentDialog);
                }
            }
            result= String.valueOf(envelope.getResponse());;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<Map<String,Object>> callEstadoButacasPlantaHorario (int IdEmpresa, int IdDestino, int CodHorario, int IdLocalidadDesde, int IdLocalidadHasta,Context context){
        String result;
        ArrayList<Map<String,Object>> ret= new ArrayList<>();

        request = new SoapObject(NAMESPACE, EstadoButacasPlantaHorario); //le digo que metodo voy a llamar
        request.addProperty("userWS","UsuarioLep"); //paso los parametros que pide el metodo
        request.addProperty("passWS","Lep1234");
        request.addProperty("IdEmpresa",IdEmpresa);
        request.addProperty("IdDestino",IdDestino);
        request.addProperty("CodHorario", CodHorario);
        request.addProperty("IdLocalidadDesde",IdLocalidadDesde);
        request.addProperty("IdLocalidadHasta",IdLocalidadHasta);
        request.addProperty("id_Plataforma",1);
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //no se toda esta configuracion cual esta bien y cual mal
        envelope.enc = SoapSerializationEnvelope.ENC2003;
        envelope.setOutputSoapObject(request);
        httpTransportSE = new HttpTransportSE(VALIDATION_URI); //paso la uri donde transportaré
        try {
            try{
                httpTransportSE.call(NAMESPACE + "#" + EstadoButacasPlantaHorario, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
            }catch (Exception e){
                try {
                    httpTransportSE.call(NAMESPACE + "#" + EstadoButacasPlantaHorario, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
                }catch (java.net.UnknownHostException | java.net.SocketTimeoutException ex){
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
                Map<String, Object> map= new HashMap<>();
                JSONObject jsonObject= json.getJSONObject(i);
                map.put("Columna", jsonObject.getString("Columna"));
                map.put("NumButaca",jsonObject.getString("NumButaca"));
                map.put("Fila",jsonObject.getString("Fila"));
                map.put("Ocupado",jsonObject.getString("Ocupado"));
                ret.add(map);
                i++;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return ret;
    }



    public static String callSeleccionarButaca(int NroButaca, int IDVenta, int EsIda, int  EsSeleccion,Context context){
        String result = "";
        request = new SoapObject(NAMESPACE, SeleccionarButaca); //le digo que metodo voy a llamar
        request.addProperty("userWS","UsuarioLep"); //paso los parametros que pide el metodo
        request.addProperty("passWS","Lep1234");
        request.addProperty("NroButaca",NroButaca);
        request.addProperty("IDVenta",IDVenta);
        request.addProperty("EsIda", EsIda);
        request.addProperty("EsSeleccion",EsSeleccion);
        request.addProperty("id_Plataforma",1);
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //no se toda esta configuracion cual esta bien y cual mal
        envelope.enc = SoapSerializationEnvelope.ENC2003;
        envelope.setOutputSoapObject(request);
        httpTransportSE = new HttpTransportSE(VALIDATION_URI); //paso la uri donde transportaré
        try {
            try{
                httpTransportSE.call(NAMESPACE + "#" + SeleccionarButaca, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
            }catch (Exception e){
                try {
                    httpTransportSE.call(NAMESPACE + "#" + SeleccionarButaca, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
                }catch (java.net.UnknownHostException | java.net.SocketTimeoutException ex){
                    String message= "Ud. no posee conexión de internet; \n acceda a través de una red wi-fi o de su prestadora telefónica";
                    Intent intentDialog = new Intent(context, Dialog.class);
                    intentDialog.putExtra("message",message);
                    intentDialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentDialog);
                }
            }
            result= (String)envelope.getResponse();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<Map<String,Object>> callListarMisReservas(String Dni,Context context){
        String result;
        ArrayList<Map<String,Object>> ret= new ArrayList<>();

        request = new SoapObject(NAMESPACE, ListarMisReserva); //le digo que metodo voy a llamar
        request.addProperty("userWS","UsuarioLep"); //paso los parametros que pide el metodo
        request.addProperty("passWS","Lep1234");
        request.addProperty("Dni", Dni);
        request.addProperty("id_Plataforma", 1);
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //no se toda esta configuracion cual esta bien y cual mal
        envelope.enc = SoapSerializationEnvelope.ENC2003;
        envelope.setOutputSoapObject(request);
        httpTransportSE = new HttpTransportSE(VALIDATION_URI); //paso la uri donde transportaré
        try {
            try{
                httpTransportSE.call(NAMESPACE + "#" + ListarMisReserva, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
            }catch (Exception e){
                try {
                    httpTransportSE.call(NAMESPACE + "#" + ListarMisReserva, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
                }catch (java.net.UnknownHostException | java.net.SocketTimeoutException ex){
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
                Map<String, Object> map= new HashMap<>();
                JSONObject jsonObject= json.getJSONObject(i);
                //System.out.println(jsonObject);
                map.put("lugar", jsonObject.get("Destino"));
                map.put("FechaHoraReserva",jsonObject.getString("FechaHoraReserva"));
                String fecha_sale=jsonObject.getString("Salida").split(" ")[0].replace('-', '/');
                String[] auxString= fecha_sale.split("/");
                fecha_sale= auxString[2]+"/"+auxString[1]+"/"+auxString[0];
                map.put("fecha_sale",fecha_sale);
                map.put("hora_sale", jsonObject.getString("Salida").split(" ")[1].substring(0, 5));
                map.put("cantidad",jsonObject.getString("cantidad"));
                map.put("Id_Empresa",jsonObject.getString("Id_Empresa"));
                map.put("Id_Destino",jsonObject.getString("Id_Destino"));
                map.put("IdLocalidadHasta",jsonObject.getString("IdLocalidadHasta"));
                map.put("IdLocalidadDesde",jsonObject.getString("IdLocalidadDesde"));
                map.put("Cod_Horario", jsonObject.getString("Cod_Horario"));
                Map<String, Object> mapAux= new HashMap<>();
                if(ret.size()>0){
                    String hora1;
                    if((ret.get(ret.size()-1).containsKey("Ida")))
                        hora1=(String)((Map)ret.get(ret.size()-1).get("Ida")).get("FechaHoraReserva");
                    else
                        hora1=(String)((Map)ret.get(ret.size()-1).get("Vuelta")).get("FechaHoraReserva");
                    String hora2= jsonObject.getString("FechaHoraReserva");
                    if(hora1.equals(hora2)) {
                        mapAux = (Map) ret.get(ret.size() - 1);
                        ret.remove(ret.size() - 1);
                        mapAux.put(jsonObject.getString("Sentido"), map);
                    }else
                        mapAux.put(jsonObject.getString("Sentido"),map);
                }
                else {
                    mapAux.put(jsonObject.getString("Sentido"), map);
                }

                ret.add(mapAux);
                i++;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        System.out.println(ret);
        return ret;
    }

    public static Map<String,Object> realizarCobroMercadoPago(String  DatosCompra,Context context){
        String result;
        Map<String,Object> ret= new HashMap<>();
        /**
         * NOMBRES DE ATRIBUTOS PARA WEBSERVICE DE MERCADOPAGO
         * CAMBIA EL NAMESPACE Y TODO LO REFERIDO
         */
        String namespaceMP ="urn:WSCobroMercadoPagoIntf-IWSCobroMercadoPago";
        String RealizarCobroMercadoPago = "RealizarCobroMercadoPago";
        String validationUri = "http://webservices.buseslep.com.ar:8080/WebServices/WSCobroMercadoPago.dll/soap/ILepWebService";//tiene que ser la uri que muestra el xml, por donde bindea

        request = new SoapObject(namespaceMP, RealizarCobroMercadoPago); //le digo que metodo voy a llamar
        request.addProperty("UserCobro","54GFDG2224785486DG"); //paso los parametros que pide el metodo
        request.addProperty("PassCobro","15eQiDeCtCaDmS2506");
        request.addProperty("DatosCompra", DatosCompra);
        request.addProperty("id_Plataforma", 1);
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //no se toda esta configuracion cual esta bien y cual mal
        envelope.enc = SoapSerializationEnvelope.ENC2003;
        envelope.setOutputSoapObject(request);
        httpTransportSE = new HttpTransportSE(validationUri); //paso la uri donde transportaré
        try {
            try{
                httpTransportSE.call(namespaceMP + "#" + RealizarCobroMercadoPago, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
            }catch (Exception e){
                try {
                    httpTransportSE.call(namespaceMP + "#" + RealizarCobroMercadoPago, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
                }catch (java.net.UnknownHostException | java.net.SocketTimeoutException ex){
                    System.err.println(ex);
                    String message= "Ud. no posee conexión de internet; \n acceda a través de una red wi-fi o de su prestadora telefónica";
                    Intent intentDialog = new Intent(context, Dialog.class);
                    intentDialog.putExtra("message",message);
                    intentDialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentDialog);
                }
            }
            result= (String)envelope.getResponse();
            System.out.println(result);
            JSONObject json= new JSONObject(result);
            if(result.contains("Cod_Impresion")){
                String codImpresion=result.split("Cod_Impresion\":\"")[1];
                codImpresion =codImpresion.substring(0,codImpresion.length()-2);
                ret.put("codImpresion",codImpresion);

            }else
                ret.put("codImpresion","");
            ret.put("datosCompra",json);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return ret;
    }

    public static ArrayList<Map<String,Object>> callListarMisCompras(String Dni,Context context){
        String result;
        ArrayList<Map<String,Object>> ret= new ArrayList<>();

        request = new SoapObject(NAMESPACE, ListarMisCompras); //le digo que metodo voy a llamar
        request.addProperty("userWS","UsuarioLep"); //paso los parametros que pide el metodo
        request.addProperty("passWS","Lep1234");
        request.addProperty("Dni", Dni);
        request.addProperty("id_Plataforma", 1);
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //no se toda esta configuracion cual esta bien y cual mal
        envelope.enc = SoapSerializationEnvelope.ENC2003;
        envelope.setOutputSoapObject(request);
        httpTransportSE = new HttpTransportSE(VALIDATION_URI); //paso la uri donde transportaré
        try {
            try{
                httpTransportSE.call(NAMESPACE + "#" + ListarMisCompras, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
            }catch (Exception e){
                try {
                    httpTransportSE.call(NAMESPACE + "#" + ListarMisCompras, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
                }catch (java.net.UnknownHostException | java.net.SocketTimeoutException ex){
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
                Map<String, Object> map= new HashMap<>();
                JSONObject jsonObject= json.getJSONObject(i);
                map.put("lugar", jsonObject.get("Destino"));
                map.put("fecha_sale",jsonObject.getString("Salida").split(" ")[0].replace('-', '/'));
                map.put("hora_sale", jsonObject.getString("Salida").split(" ")[1].substring(0, 5));
                map.put("cantidad",jsonObject.getString("cantidad"));
                map.put("CodImpresion",jsonObject.getString("CodImpresion"));
                ret.add(map);
                i++;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return ret;
    }


    public static  Map<String, Object> callPasarReservasaPrepago(String dni, String FechaHoraReserva, Context context){
        String result = "";
        Integer idSell=-1;
        Float importe=new Float(0);
        Map<String, Object> map = new HashMap<>();
        request = new SoapObject(NAMESPACE, PasarReservasaPrepago); //le digo que metodo voy a llamar
        request.addProperty("userWS","UsuarioLep"); //paso los parametros que pide el metodo
        request.addProperty("passWS","Lep1234");
        request.addProperty("Dni", dni);
        request.addProperty("FechaHoraReserva", FechaHoraReserva);
        request.addProperty("Id_Plataforma", 1);

        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //no se toda esta configuracion cual esta bien y cual mal
        envelope.enc = SoapSerializationEnvelope.ENC2003;
        envelope.setOutputSoapObject(request);
        httpTransportSE = new HttpTransportSE(VALIDATION_URI); //paso la uri donde transportaré
        try {
            try{
                httpTransportSE.call(NAMESPACE + "#" + PasarReservasaPrepago, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
            }catch (Exception e){
                try {
                    httpTransportSE.call(NAMESPACE + "#" + PasarReservasaPrepago, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
                }catch (java.net.UnknownHostException | java.net.SocketTimeoutException ex){
                    String message= "Ud. no posee conexión de internet; \n acceda a través de una red wi-fi o de su prestadora telefónica";
                    Intent intentDialog = new Intent(context, Dialog.class);
                    intentDialog.putExtra("message",message);
                    intentDialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentDialog);
                }
            }
            result= String.valueOf(envelope.getResponse());
            JSONArray json= new JSONObject(result).getJSONArray("Data");
            if(json.length()>0) {
                JSONObject jsonObject = json.getJSONObject(0);
                map.put("Id_Venta", jsonObject.getInt("Id_Venta"));
                map.put("Id_Empresa", jsonObject.getInt("Id_Empresa"));
                map.put("ID_Localidad_Destino", jsonObject.getInt("ID_Localidad_Destino"));
                map.put("Id_Destino", jsonObject.getInt("Id_Destino"));
                map.put("Importe_ida", jsonObject.getString("Importe"));
                importe=Float.valueOf(jsonObject.getString("Importe"));
                map.put("cantidad", jsonObject.getInt("cantidad"));
                map.put("ID_Localidad_Origen", jsonObject.getInt("ID_Localidad_Origen"));
                map.put("Cod_Horario", jsonObject.getInt("Cod_Horario"));
            }
            if(json.length()==2){
                Map<String, Object> mapVuelta = new HashMap<>();
                JSONObject jsonObject = json.getJSONObject(1);
                mapVuelta.put("Id_Empresa",jsonObject.getInt("Id_Empresa"));
                mapVuelta.put("ID_Localidad_Destino",jsonObject.getInt("ID_Localidad_Destino"));
                mapVuelta.put("Id_Destino",jsonObject.getInt("Id_Destino"));
                mapVuelta.put("Importe_vuelta", String.valueOf(Float.valueOf(jsonObject.getString("Importe"))+importe));
                mapVuelta.put("ID_Localidad_Origen",jsonObject.getInt("ID_Localidad_Origen"));
                mapVuelta.put("Cod_Horario",jsonObject.getInt("Cod_Horario"));
                map.put("vuelta",mapVuelta);

            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return map;
    }


    public static  String callAnularReservas(String dni, String FechaHoraReserva, Context context){
        String result = "";
        request = new SoapObject(NAMESPACE, AnularReservas); //le digo que metodo voy a llamar
        request.addProperty("userWS","UsuarioLep"); //paso los parametros que pide el metodo
        request.addProperty("passWS","Lep1234");
        request.addProperty("Dni", dni);
        request.addProperty("FechaHoraReserva", FechaHoraReserva);
        request.addProperty("Id_Plataforma", 1);

        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //no se toda esta configuracion cual esta bien y cual mal
        envelope.enc = SoapSerializationEnvelope.ENC2003;
        envelope.setOutputSoapObject(request);
        httpTransportSE = new HttpTransportSE(VALIDATION_URI); //paso la uri donde transportaré
        try {
            try {
                httpTransportSE.call(NAMESPACE + "#" + AnularReservas, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
            } catch (Exception e) {
                try {
                    httpTransportSE.call(NAMESPACE + "#" + AnularReservas, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
                } catch (java.net.UnknownHostException | java.net.SocketTimeoutException ex) {
                    String message = "Ud. no posee conexión de internet; \n acceda a través de una red wi-fi o de su prestadora telefónica";
                    Intent intentDialog = new Intent(context, Dialog.class);
                    intentDialog.putExtra("message", message);
                    intentDialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentDialog);
                }
            }
            result = String.valueOf(envelope.getResponse());
        }catch(Exception e){
                e.printStackTrace();
            }
        return result;
    }

    public static  String callEliminarButacaSeleccionada(Integer idVenta,Context context){
        String result = "";
        request = new SoapObject(NAMESPACE, EliminarButacaSeleccionada); //le digo que metodo voy a llamar
        request.addProperty("userWS","UsuarioLep"); //paso los parametros que pide el metodo
        request.addProperty("passWS","Lep1234");
        request.addProperty("idVenta", idVenta);
        request.addProperty("Id_Plataforma", 1);

        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //no se toda esta configuracion cual esta bien y cual mal
        envelope.enc = SoapSerializationEnvelope.ENC2003;
        envelope.setOutputSoapObject(request);
        httpTransportSE = new HttpTransportSE(VALIDATION_URI); //paso la uri donde transportaré
        try {
            try {
                httpTransportSE.call(NAMESPACE + "#" + EliminarButacaSeleccionada, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
            } catch (Exception e) {
                try {
                    httpTransportSE.call(NAMESPACE + "#" + EliminarButacaSeleccionada, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
                } catch (java.net.UnknownHostException | java.net.SocketTimeoutException ex) {
                    String message = "Ud. no posee conexión de internet; \n acceda a través de una red wi-fi o de su prestadora telefónica";
                    Intent intentDialog = new Intent(context, Dialog.class);
                    intentDialog.putExtra("message", message);
                    intentDialog.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intentDialog);
                }
            }
            result = String.valueOf(envelope.getResponse());
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
}
