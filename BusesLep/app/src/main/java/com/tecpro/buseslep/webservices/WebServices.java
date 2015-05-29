package com.tecpro.buseslep.webservices;

import android.os.StrictMode;
import android.util.Pair;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.LinkedList;
import java.util.TreeMap;

/**
 * Created by nico on 28/05/15.
 */
public class WebServices  {
    private static String NAMESPACE = "urn:LepWebServiceIntf-ILepWebService"; //figura claramente en el xml del webservice, ir mirando el binding
    private static String LocalidadesDesde = "LocalidadesDesde"; //nombre del metodo del ws, fijarse en el binding
    private static String LocalidadesHasta = "LocalidadesHasta"; //nombre del metodo del ws, fijarse en el binding
    private static String ListarHorarios = "ListarHorarios"; //nombre del metodo del ws, fijarse en el binding

    private static String VALIDATION_URI = "http://webservices.buseslep.com.ar/WebServices/WebServiceLep.dll/soap/ILepWebService";//tiene que ser la uri que muestra el xml, por donde bindea
    private static SoapSerializationEnvelope envelope = null;
    private static SoapObject request = null;
    private static HttpTransportSE httpTransportSE = null;


    /**
     * obtengo todas las ciudaddes de origen desde el ws y retorno un par de dos cosas, una
     * es una lista de ciudad-id y la otra de los nombres solos,
     * @return
     */
    public static Pair<TreeMap<String, Integer>, LinkedList<String>> getCities(){
        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); //pongo esto para correrlo en en onCreate, sino va con un zyncTask
        //StrictMode.setThreadPolicy(policy);
        String result;
        String[] results;
        TreeMap<String, Integer> CitiesAndId = new TreeMap<String, Integer>();
        LinkedList<String> cities = new LinkedList<>();
        cities.add("Ciudad de origen");
        request = new SoapObject(NAMESPACE, LocalidadesDesde); //le digo que metodo voy a llamar
        request.addProperty("user","UsuarioLep"); //paso los parametros que pide el metodo
        request.addProperty("pass","Lep1234");
        envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //no se toda esta configuracion cual esta bien y cual mal
        //envelope.dotNet = true; //pero anda asi
        //envelope.xsd = SoapSerializationEnvelope.XSD;
        //envelope.setAddAdornments(false);
        envelope.enc = SoapSerializationEnvelope.ENC2003;
        envelope.setOutputSoapObject(request);
        httpTransportSE = new HttpTransportSE(VALIDATION_URI); //paso la uri donde transportaré
        try {
            httpTransportSE.call(NAMESPACE + "#" + LocalidadesDesde, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
            //System.out.println((String) envelope.getResponse());
            result= (String)envelope.getResponse();
            int start= result.indexOf("<rs:data>")+9;
            int finish = result.indexOf("</rs:data>");
            result = result.substring(start, finish);//busco la info que sirve que esta en <data> info </data>
            result=result.replaceAll("<z:row ", "");//elimino la primer parte de cada renglon que es siemre asi
            result=result.replaceAll("' ", ";"); //separo los atributos por un ;
            result=result.replaceAll("='", "="); //elimino las ' así tengo clave=valor
            result=result.replaceAll("Ã±", "ñ"); //pongo la ñ
            results=result.split("'/>");//elimino la ultima parte de cada renglon, el resultado ahora es clave=valor;clave2=valor2...;clave3=valor3
            for (int i=0; i<results.length-2; i++) { //elimino el ultimo elemento porque es basura de algun split
                String[] aux= results[i].split(";"); //obtengo las claves, en este caso son 3, pero solo uso 2
                String name=aux[1].split("=")[1];
                int id=Integer.valueOf(aux[0].split("=")[1]);
                CitiesAndId.put(name, id);
                cities.add(name);
            }
        }

        catch(Exception e){
            e.printStackTrace();
        }
        return new Pair(CitiesAndId,cities);
    }

    /**
     * obtengo todas las ciudaddes de destino desde el ws y retorno una lista de ciudades, el atributo que toma es el id de origen
     * @return
     */
    public static LinkedList<String> getDestinationCities(Integer idOrigin){
        LinkedList<String> cities = new LinkedList<>();
        cities.add("Ciudad de destino");
        if(idOrigin!=-1) {
            //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); //pongo esto para correrlo en en onCreate, sino va con un zyncTask
            //StrictMode.setThreadPolicy(policy);
            String result;
            String[] results;
            request = new SoapObject(NAMESPACE, LocalidadesHasta); //le digo que metodo voy a llamar
            request.addProperty("user", "UsuarioLep"); //paso los parametros que pide el metodo
            request.addProperty("pass", "Lep1234");
            request.addProperty("IdLocalidadOrigen", idOrigin);
            envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); //no se toda esta configuracion cual esta bien y cual mal
            //envelope.dotNet = true; //pero anda asi
            //envelope.xsd = SoapSerializationEnvelope.XSD;
            //envelope.setAddAdornments(false);
            envelope.enc = SoapSerializationEnvelope.ENC2003;
            envelope.setOutputSoapObject(request);
            httpTransportSE = new HttpTransportSE(VALIDATION_URI); //paso la uri donde transportaré
            try {
                httpTransportSE.call(NAMESPACE + "#" + LocalidadesHasta, envelope); //llamo al metodo, aca se puede cambiar soap_action por la concatenacion para hacerlo mas general
                result = (String) envelope.getResponse();
                int start = result.indexOf("<rs:data>") + 9;
                int finish = result.indexOf("</rs:data>");
                result = result.substring(start, finish);//busco la info que sirve que esta en <data> info </data>
                result = result.replaceAll("<z:row ", "");//elimino la primer parte de cada renglon que es siemre asi
                result = result.replaceAll("' ", ";"); //separo los atributos por un ;
                result = result.replaceAll("='", "="); //elimino las ' así tengo clave=valor
                result = result.replaceAll("Ã±", "ñ"); //pongo la ñ
                results = result.split("'/>");//elimino la ultima parte de cada renglon, el resultado ahora es clave=valor;clave2=valor2...;clave3=valor3
                for (int i = 0; i < results.length - 2; i++) { //elimino el ultimo elemento porque es basura de algun split
                    String[] aux = results[i].split(";"); //obtengo las claves, en este caso son 3, pero solo uso 2
                    String name = aux[3].split("=")[1];
                    cities.add(name);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cities;
    }



}
