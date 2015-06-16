package com.tecpro.buseslep;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mercadopago.core.MercadoPago;
import com.mercadopago.model.PaymentMethod;
import com.mercadopago.util.JsonUtil;
import com.mercadopago.util.LayoutUtil;
import com.tecpro.buseslep.utils.PaymentUtils;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by agustin on 06/06/15.
 */
public class PurchaseDetails extends Activity {

    protected List<String> mSupportedPaymentTypes = new ArrayList<String>(){{
        add("credit_card");
        add("debit_card");
        add("prepaid_card");
    }};

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_details);

        Bundle extras = getIntent().getExtras();

        int roundtrip = extras.getInt("roundtrip");
        String cityfrom = extras.getString("city_from");
        String cityto = extras.getString("city_to");
        String arrdate1 = extras.getString("arrival_date1");
        String arrhour1 = extras.getString("arrival_hour1");
        String arrdate2 = extras.getString("arrival_date2");
        String arrhour2 = extras.getString("arrival_hour2");
        String cantTick = extras.getString("cant_tickets");

        TextView destiny1 = (TextView)findViewById(R.id.destiny1);
        TextView departure1 = (TextView)findViewById(R.id.departure1);
        TextView cantTickets1 = (TextView)findViewById(R.id.cantTickets1);

        destiny1.setText(cityfrom + " - " + cityto);
        departure1.setText(arrdate1 + "  " + arrhour1);
        cantTickets1.setText(cantTick);

        TextView departure2 = (TextView)findViewById(R.id.departure2);
        TextView destiny2 = (TextView)findViewById(R.id.destiny2);
        TextView cantTickets2 = (TextView)findViewById(R.id.cantTickets2);

        destiny2.setText(cityto + " - " + cityfrom);
        departure2.setText(arrdate2 + "  " + arrhour2);
        cantTickets2.setText(cantTick);

        if(roundtrip == -1)  //si es ida y vuelta leo y seteo los otros datos
            findViewById(R.id.backtrip).setVisibility(View.GONE);



        /*TextView seatNum1 = (TextView)findViewById(R.id.seatNum1);
        TextView seatNum2 = (TextView)findViewById(R.id.seatNum2);
        TextView departurePlace1 = (TextView)findViewById(R.id.departurePlace1);
        TextView departurePlace2 = (TextView)findViewById(R.id.departurePlace2);
        TextView platform1 = (TextView)findViewById(R.id.platform1);
        TextView platform2 = (TextView)findViewById(R.id.platform2);
        TextView total = (TextView)findViewById(R.id.totalPrice);*/


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MercadoPago.PAYMENT_METHODS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // Set payment method
                PaymentMethod paymentMethod = JsonUtil.getInstance().fromJson(data.getStringExtra("paymentMethod"), PaymentMethod.class);

                // Call new card activity
                PaymentUtils.startCardActivity(this, PaymentUtils.DUMMY_MERCHANT_PUBLIC_KEY, paymentMethod);
            } else {

                if ((data != null) && (data.getStringExtra("apiException") != null)) {
                    Toast.makeText(getApplicationContext(), data.getStringExtra("apiException"), Toast.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == PaymentUtils.CARD_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                // Create payment
                PaymentUtils.createPayment(this, data.getStringExtra("token"),
                        1, null, JsonUtil.getInstance().fromJson(data.getStringExtra("paymentMethod"), PaymentMethod.class), null);

            } else {

                if (data != null) {
                    if (data.getStringExtra("apiException") != null) {

                        Toast.makeText(getApplicationContext(), data.getStringExtra("apiException"), Toast.LENGTH_LONG).show();

                    } else if (data.getBooleanExtra("backButtonPressed", false)) {

                        new MercadoPago.StartActivityBuilder()
                                .setActivity(this)
                                .setPublicKey(PaymentUtils.DUMMY_MERCHANT_PUBLIC_KEY)
                                .setSupportedPaymentTypes(mSupportedPaymentTypes)
                                .startPaymentMethodsActivity();
                    }
                }
            }
        } else if (requestCode == MercadoPago.CONGRATS_REQUEST_CODE) {

            LayoutUtil.showRegularLayout(this);
        }
    }

    public void submitForm(View view) {

        new MercadoPago.StartActivityBuilder()
                .setActivity(this)
                .setPublicKey(PaymentUtils.DUMMY_MERCHANT_PUBLIC_KEY)
                .setSupportedPaymentTypes(mSupportedPaymentTypes)
                .startPaymentMethodsActivity();
    }

}

