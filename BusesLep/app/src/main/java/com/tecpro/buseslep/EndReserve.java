package com.tecpro.buseslep;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.tecpro.buseslep.search_scheludes.SearchScheludes;

/**
 * Created by nico on 27/06/15.
 */
public class EndReserve extends Activity {

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_reserve);
 /*       ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowCustomEnabled(false);
        getActionBar().setIcon(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(getLayoutInflater().inflate(R.layout.action_bar, null),
                new ActionBar.LayoutParams(
                        ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.MATCH_PARENT,
                        Gravity.CENTER
                )
        );*/
        String email= getIntent().getExtras().getString("email","");
        TextView txtView =((TextView) findViewById(R.id.txtEmail));
        txtView.setText(email);
        this.setFinishOnTouchOutside(false);
    }

    public void clickFinish(View v){
        Intent i= new Intent(this,SearchScheludes.class);
        finish();
        startActivity(i);

    }
    @Override
    public void onBackPressed() {
        Intent i= new Intent(this,SearchScheludes.class);
        finish();
        startActivity(i);
    }
}
