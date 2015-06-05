package com.tecpro.buseslep.search_scheludes;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.tecpro.buseslep.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ChooseDate extends FragmentActivity {

    private CaldroidFragment caldroidFragment;
    private int day;
    private int month;
    private int year;
    private Date dateSelected;
    final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
    private TextView txtSelectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_date);
        Bundle bundle = getIntent().getExtras();
        ((TextView)findViewById(R.id.txtDescriptionDate)).setText(bundle.getString("description"));
        day=bundle.getInt("day");
        month=bundle.getInt("month");
        year=bundle.getInt("year");
        txtSelectedDate = (TextView)findViewById(R.id.txt_select_date);
        Calendar cal= new GregorianCalendar(year,month,day);
        dateSelected= cal.getTime();
        txtSelectedDate.setText("Fecha elegida: "+formatter.format(dateSelected));
        // Setup caldroid fragment
        // **** If you want normal CaldroidFragment, use below line ****
        caldroidFragment = new CaldroidFragment();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                txtSelectedDate.setText("Fecha elegida: "+formatter.format(date));
                dateSelected= date;

            }

            @Override
            public void onChangeMonth(int month, int year) {
//                String text = "month: " + month + " year: " + year;
//                Toast.makeText(getApplicationContext(), text,
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickDate(Date date, View view) {
//                Toast.makeText(getApplicationContext(),
//                        "Long click " + formatter.format(date),
//                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCaldroidViewCreated() {
//             (caldroidFragment.getLeftArrowButton() != null) {
//                    Toast.makeText(getApplicationContext(),
//                            "Caldroid view is created", Toast.LENGTH_SHORT)
//                            .show();
//                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);

        Bundle args = new Bundle();
        cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);
        caldroidFragment.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
       t.commit();

    }

/*    @Override
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
    */

    public void clickBtnAccept(View view){
        Intent intent = new Intent();
        Calendar cal= new GregorianCalendar();
        cal.setTime(dateSelected);
        intent.putExtra("day",cal.get(Calendar.DATE));
        intent.putExtra("month",cal.get(Calendar.MONTH)+1);
        intent.putExtra("year",cal.get(Calendar.YEAR));
        setResult(RESULT_OK, intent);
        finish();
    }



}
