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

    private String totalPrice;
    private float t;
    private int idSell;

    protected List<String> mSupportedPaymentTypes = new ArrayList<String>(){{
        add("credit_card");
        add("debit_card");
        add("prepaid_card");
    }};

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.purchase_details);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(false);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.action_bar, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER
                )
        );
        Bundle extras = getIntent().getExtras();
        idSell = extras.getInt("idVenta");
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
        TextView dephour1 = (TextView)findViewById(R.id.depHour1);
        TextView cantTickets1 = (TextView)findViewById(R.id.cantTickets1);

        destiny1.setText(cityfrom + " - " + cityto);
       // departure1.setText(arrdate1 + "  " + arrhour1);
        departure1.setText(arrdate1);
        dephour1.setText(arrhour1);
        cantTickets1.setText(cantTick);

        TextView departure2 = (TextView)findViewById(R.id.departure2);
        TextView destiny2 = (TextView)findViewById(R.id.destiny2);
        TextView cantTickets2 = (TextView)findViewById(R.id.cantTickets2);
        TextView dephour2 = (TextView)findViewById(R.id.depHour2);

        destiny2.setText(cityto + " - " + cityfrom);
       // departure2.setText(arrdate2 + "  " + arrhour2);
        departure2.setText(arrdate2);
        dephour2.setText(arrhour2);
        cantTickets2.setText(cantTick);
        TextView total = (TextView)findViewById(R.id.totalPrice);
        totalPrice = extras.getString("priceGoRet");
        if(roundtrip == -1) {  //si es ida
            findViewById(R.id.backtrip).setVisibility(View.GONE);
            totalPrice = extras.getString("priceGo");

        }
        else
            totalPrice = extras.getString("priceGoRet");
        t = Float.valueOf(totalPrice);
        t *= Integer.valueOf(cantTick);
        total.setText( String.format("%.2f", t));
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

        if (requestCode == PaymentUtils.ADVANCED_VAULT_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {

                Long issuerId = (data.getStringExtra("issuerId") != null)
                        ? Long.parseLong(data.getStringExtra("issuerId")) : null;

                // Create payment
                PaymentUtils.createPayment(this, data.getStringExtra("token"),
                        Integer.parseInt(data.getStringExtra("installments")),
                        issuerId, JsonUtil.getInstance().fromJson(data.getStringExtra("paymentMethod"), PaymentMethod.class), null);

            } else {

                if ((data != null) && (data.getStringExtra("apiException") != null)) {
                    Toast.makeText(getApplicationContext(), data.getStringExtra("apiException"), Toast.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == MercadoPago.CONGRATS_REQUEST_CODE) {

            LayoutUtil.showRegularLayout(this);
        }
    }


    public void submitForm(View view) {

        // Call final vault activity, le paso los pagos soportados, pero es al pedo pasarlo aća
        PaymentUtils.startAdvancedVaultActivity(this, new BigDecimal(t), mSupportedPaymentTypes);
    }

}

