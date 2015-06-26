package com.tecpro.buseslep.utils;

import android.app.Activity;
import android.content.Intent;
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
import com.tecpro.buseslep.mercadopago.AdvancedVaultActivity;


import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class PaymentUtils {

    public static final int ADVANCED_VAULT_REQUEST_CODE = 11;

    // * Merchant public key
    public static final String MERCHANT_PUBLIC_KEY = "TEST-2e5d7d95-7cb8-48d3-8bd6-cfde1bc34254";
    // DUMMY_MERCHANT_PUBLIC_KEY_AR = "444a9ef5-8a6b-429f-abdf-587639155d88";


    // * Merchant server vars
    public static final String MERCHANT_BASE_URL = "https://www.mercadopago.com";
    public static final String MERCHANT_CREATE_PAYMENT_URI = "/checkout/examples/doPayment";

    // * Merchant access token
    public static final String DUMMY_MERCHANT_ACCESS_TOKEN = "mla-cards-data";
    // DUMMY_MERCHANT_ACCESS_TOKEN_AR = "mla-cards-data";

    // * Payment item
    public static final String DUMMY_ITEM_ID = "id1";
    public static final Integer DUMMY_ITEM_QUANTITY = 1;
    public static final BigDecimal DUMMY_ITEM_UNIT_PRICE = new BigDecimal("100");

    public static void startAdvancedVaultActivity(Activity activity , BigDecimal amount, List<String> supportedPaymentTypes) {

        Intent advVaultIntent = new Intent(activity, AdvancedVaultActivity.class);
        advVaultIntent.putExtra("merchantPublicKey", MERCHANT_PUBLIC_KEY);
        advVaultIntent.putExtra("merchantBaseUrl", MERCHANT_BASE_URL);
        advVaultIntent.putExtra("merchantAccessToken", MERCHANT_CREATE_PAYMENT_URI);
        advVaultIntent.putExtra("amount", amount.toString());
        putListExtra(advVaultIntent, "supportedPaymentTypes", supportedPaymentTypes);
        activity.startActivityForResult(advVaultIntent, ADVANCED_VAULT_REQUEST_CODE);
    }



    public static void createPayment(final Activity activity, String token, Integer installments, Long cardIssuerId, final PaymentMethod paymentMethod, Discount discount) {

        if (paymentMethod != null) {

            LayoutUtil.showProgressLayout(activity);

            // Set item
            Item item = new Item(DUMMY_ITEM_ID, DUMMY_ITEM_QUANTITY,
                    DUMMY_ITEM_UNIT_PRICE);

            // Set payment method id
            String paymentMethodId = paymentMethod.getId();

            // Set campaign id
            Long campaignId = (discount != null) ? discount.getId() : null;

            // Set merchant payment
            MerchantPayment payment = new MerchantPayment(item, installments, cardIssuerId,
                    token, paymentMethodId, campaignId, DUMMY_MERCHANT_ACCESS_TOKEN);
            System.out.println("token "+token);

            //TESTINGGGGGGGGGGGGGGGGGG
            Gson mGson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).serializeNulls().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").create();
            Payment test=mGson.fromJson("{\"id\":28473,\"date_created\":\"2015-06-25T21:22:17.974-04:00\",\"date_approved\":\"2015-06-25T21:22:17.873-04:00\",\"date_last_updated\":\"2015-06-25T21:22:17.873-04:00\",\"money_release_date\":null,\"operation_type\":\"regular_payment\",\"issuer_id\":\"338\",\"payment_method_id\":\"master\",\"payment_type_id\":\"credit_card\",\"status\":\"approved\",\"status_detail\":\"accredited\",\"currency_id\":\"ARS\",\"description\":\"Title of what you are paying for\",\"live_mode\":false,\"sponsor_id\":null,\"collector_id\":185927909,\"payer\":{\"type\":\"guest\",\"id\":null,\"email\":\"test_user_19653727@testuser.com\",\"identification\":{\"type\":\"DNI\",\"number\":\"1111111\"}},\"metadata\":{},\"order\":{},\"external_reference\":null,\"transaction_amount\":150,\"transaction_amount_refunded\":0,\"coupon_amount\":0,\"differential_pricing_id\":null,\"transaction_details\":{\"net_received_amount\":141.01,\"total_paid_amount\":174.89,\"overpaid_amount\":0,\"external_resource_url\":null,\"installment_amount\":58.3,\"financial_institution\":null,\"payment_method_reference_id\":null},\"fee_details\":[{\"type\":\"financing_fee\",\"fee_payer\":\"payer\",\"amount\":24.89},{\"type\":\"mercadopago_fee\",\"fee_payer\":\"collector\",\"amount\":8.99}],\"captured\":true,\"binary_mode\":false,\"call_for_authorize_id\":null,\"statement_descriptor\":\"WWW.MERCADOPAGO.COM\",\"installments\":3,\"card\":{\"id\":null,\"first_six_digits\":\"503175\",\"last_four_digits\":\"0604\",\"expiration_month\":6,\"expiration_year\":2016,\"date_created\":\"2015-06-25T21:22:17.848-04:00\",\"date_last_updated\":\"2015-06-25T21:22:17.569-04:00\",\"cardholder\":{\"name\":\"APRO\",\"identification\":{\"number\":\"32666666\",\"type\":\"DNI\"}}},\"notification_url\":null,\"refunds\":[]}",Payment.class);

            new MercadoPago.StartActivityBuilder()
                    .setActivity(activity)
                    .setPayment(test)
                    .setPaymentMethod(paymentMethod)
                    .startCongratsActivity();
            //FIN DE TESTING


            // Create payment
            /*MerchantServer.createPayment(activity, MERCHANT_BASE_URL, MERCHANT_CREATE_PAYMENT_URI, payment, new Callback<Payment>() {
                @Override
                public void success(Payment payment, Response response) {

                    new MercadoPago.StartActivityBuilder()
                            .setActivity(activity)
                            .setPayment(payment)
                            .setPaymentMethod(paymentMethod)
                            .startCongratsActivity() ;
                }

                @Override
                public void failure(RetrofitError error) {

                    LayoutUtil.showRegularLayout(activity);
                    Toast.makeText(activity, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            });*/
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
}
