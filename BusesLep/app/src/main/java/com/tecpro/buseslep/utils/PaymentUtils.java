package com.tecpro.buseslep.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.mercadopago.core.MercadoPago;
import com.mercadopago.core.MerchantServer;
import com.mercadopago.model.Discount;
import com.mercadopago.model.Item;
import com.mercadopago.model.MerchantPayment;
import com.mercadopago.model.Payment;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.util.LayoutUtil;
import com.tecpro.buseslep.Dialog;
import com.tecpro.buseslep.mercadopago.AdvancedVaultActivity;
import com.tecpro.buseslep.webservices.WebServices;


import org.json.JSONObject;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PaymentUtils {

    private static  Activity activ;
    private static Integer idVenta;
    private static BigDecimal amount;
    private static Payment payment;
    public static String codImpresion;
    private static PaymentMethod paymentMethod;
    public static boolean exito=false;

    public static final int ADVANCED_VAULT_REQUEST_CODE = 11;

    // * Merchant public key
    public static final String MERCHANT_PUBLIC_KEY = "APP_USR-3f8dc194-8894-4d07-bb6c-b4a786a19c6c";
    // DUMMY_MERCHANT_PUBLIC_KEY_AR = "444a9ef5-8a6b-429f-abdf-587639155d88";


    // * Merchant access token
    // DUMMY_MERCHANT_ACCESS_TOKEN_AR = "mla-cards-data";



    public static void startAdvancedVaultActivity(Activity activity , BigDecimal amountParams, List<String> supportedPaymentTypes, Integer idSell) {
        activ = activity;
        idVenta=idSell;
        amount= amountParams;
        Intent advVaultIntent = new Intent(activity, AdvancedVaultActivity.class);
        advVaultIntent.putExtra("merchantPublicKey", MERCHANT_PUBLIC_KEY);
        advVaultIntent.putExtra("amount", amountParams.toString());
        putListExtra(advVaultIntent, "supportedPaymentTypes", supportedPaymentTypes);
        activity.startActivityForResult(advVaultIntent, ADVANCED_VAULT_REQUEST_CODE);
    }



    public static void createPayment( final Activity activity, String token, Integer installments, final PaymentMethod paymentMethodParam ) {
        activ=activity;
        if (paymentMethodParam != null) {
            paymentMethod =paymentMethodParam;
            LayoutUtil.showProgressLayout(activity);
            // Set payment method id
            String paymentMethodId = paymentMethodParam.getId();

            Gson mGson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).serializeNulls().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();

            PreferencesUsing preferences= new PreferencesUsing(activity);
            preferences.init();
            PayerTecProName payerName= new PayerTecProName(preferences.getNombre(),preferences.getApellido());
            AditionalInfo additionalInfo= new AditionalInfo(payerName);
            PaymentTecPro paymentTecPro = new PaymentTecPro("boletos","boleto:"+idVenta,installments,preferences.getEmail(),paymentMethodId,token,amount, additionalInfo);//pago a enviar al server
            String datosCompra = mGson.toJson(paymentTecPro); //lo convierto en json
            AsyncCallerCompraMP asyncCallerCompraMP= new AsyncCallerCompraMP(activity.getApplicationContext());
            asyncCallerCompraMP.execute(datosCompra);
        } else {

            Toast.makeText(activity, "Invalid payment method", Toast.LENGTH_LONG).show();
        }
    }

    private static void putListExtra(Intent intent, String listName, List<String> list) {

        if (list != null) {
            Gson gson = new Gson();
            Type listType = new TypeToken<List<String>>(){}.getType();
            intent.putExtra(listName, gson.toJson(list, listType));
        }
    }


    /**
     * asynctask para realizar una compra por mercadopago, llamo al servidor de la lep
     */
    private static class AsyncCallerCompraMP extends AsyncTask<String, Void, Map<String,Object>> {
        ProgressDialog pdLoading = new ProgressDialog(activ);
        Context context; //contexto para largar la activity aca adentro

        private AsyncCallerCompraMP(Context context) {
            this.context = context.getApplicationContext();
            pdLoading.setCancelable(false);

        }

        @Override
        protected Map<String,Object> doInBackground(String... params) {

            return WebServices.realizarCobroMercadoPago(params[0], activ);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //this method will be running on UI thread
            pdLoading.setTitle("Por favor, espere.");
            pdLoading.setMessage("Realizando compra");
            pdLoading.show();
        }


        @Override
        protected void onPostExecute(Map<String,Object> result) {
            System.out.println(result);
            if (result==null || result.isEmpty())
                Toast.makeText(activ.getBaseContext(), "Ocurrió un  error", Toast.LENGTH_SHORT).show();
                //this method will be running on UI thread
            else{
                Gson mGson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).serializeNulls().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
                payment = mGson.fromJson(((JSONObject)result.get("datosCompra")).toString(), Payment.class);
                codImpresion = (String)result.get("codImpresion");
                new MercadoPago.StartActivityBuilder()
                        .setActivity(activ)
                        .setPayment(payment)
                        .setPaymentMethod(paymentMethod)
                        .startCongratsActivity() ;
                String messageError= "ERROR";
                Intent i= new Intent(activ, Dialog.class);
                exito=false;
                switch (payment.getStatusDetail()){
                    case "accredited": //Pago aprobado
                        exito=true;
                        break;
                    case "pending_contingency": //Pago pendiente
                        messageError="ERROR: Pago pendiente";
                        i.putExtra("message", messageError);
                        activ.startActivity(i);
                        break;
                    case "cc_rejected_call_for_authorize": //Pago rechazado, llamar para autorizar.
                        messageError="ERROR: Pago rechazado, llamar para autorizar.";
                        i.putExtra("message", messageError);
                        activ.startActivity(i);
                        break;
                    case "cc_rejected_insufficient_amount": //Pago rechazado, saldo insuficiente.
                        messageError="ERROR: Pago rechazado, saldo insuficiente.";
                        i.putExtra("message", messageError);
                        activ.startActivity(i);
                        break;
                    case "cc_rejected_bad_filled_security_code": //Pago rechazado por código de seguridad.
                        messageError="ERROR: Pago rechazado por código de seguridad.";
                        i.putExtra("message", messageError);
                        activ.startActivity(i);
                        break;
                    case "cc_rejected_bad_filled_date": //Pago rechazado por fecha de expiración.
                        messageError="ERROR: Pago rechazado por fecha de expiración.";
                        i.putExtra("message", messageError);
                        activ.startActivity(i);
                        break;
                    case "cc_rejected_bad_filled_other": //Pago rechazado por error en el formulario
                        messageError="ERROR: Pago rechazado por error en el formulario";
                        i.putExtra("message", messageError);
                        activ.startActivity(i);
                        break;
                    default: //Pago rechazado
                        messageError="ERROR";
                        i.putExtra("message", messageError);
                        activ.startActivity(i);
                        break;
                }

            }
                pdLoading.dismiss();
        }
    }

}
