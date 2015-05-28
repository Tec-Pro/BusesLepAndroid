package com.tecpro.buseslep.search_scheludes;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;

import com.tecpro.buseslep.R;

public class ChooseDate extends Activity {

    private DatePicker datePicker;
    private int day;
    private int month;
    private int year;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_date);
        Bundle bundle = getIntent().getExtras();
        ((TextView)findViewById(R.id.txtDescriptionDate)).setText(bundle.getString("description"));
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        day=bundle.getInt("day");
        month=bundle.getInt("month");
        year=bundle.getInt("year");
        datePicker.updateDate(year,month-1,day);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_choose_date, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void clickBtnAccept(View view){
        Intent intent = new Intent();
        intent.putExtra("day",datePicker.getDayOfMonth());
        intent.putExtra("month",datePicker.getMonth()+1);
        intent.putExtra("year",datePicker.getYear());
        setResult(RESULT_OK, intent);
        finish();
    }
}
