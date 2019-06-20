package com.example.larisa.leavingpermissionapp.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

import com.example.larisa.leavingpermissionapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    private static final long DOUBLE_PRESS_INTERVAL = 250; // in millis
    private long lastPressTime;
    private Button CancelCalendar;
    private CalendarView calendarView;
    private boolean mHasDoubleClicked = false;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        CancelCalendar = findViewById(R.id.CancelButtonCalendar);
        calendarView = findViewById(R.id.calendarViewID);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange( CalendarView view, int year, int month, int dayOfMonth) {
                Log.d("luna", String.valueOf(month));
                String dateString = String.format("%d-%d-%d", year, month, dayOfMonth+3);
                Date date = null;
                try {
                    date = new SimpleDateFormat("yyyy-M-d").parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                final String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
                view.getFirstDayOfWeek();
                final long pressTime = System.currentTimeMillis();

                // If double click...
                if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {
                    if ( dayOfWeek.equals("Sunday") || (dayOfWeek.equals("Saturday"))){
                        Toast.makeText(getApplicationContext(), "Weekend" , Toast.LENGTH_SHORT).show();
                    }else{

                        mHasDoubleClicked = true;
                        Intent intent = new Intent(CalendarActivity.this, LeavingPermissionList.class);
                        intent.putExtra("day", dayOfMonth);
                        intent.putExtra("month", month);
                        intent.putExtra("year", year);
                        Log.d("luna", String.valueOf(month));
                        startActivity(intent);
                    }
                }
                else {     // If not double click....
                    mHasDoubleClicked = false;
                    @SuppressLint("HandlerLeak") final Handler myHandler = new Handler() {
                        public void handleMessage(Message m) {
                            if (!mHasDoubleClicked && (dayOfWeek.equals("Sunday") || (dayOfWeek.equals("Saturday")))) {
                                Toast.makeText(getApplicationContext(), "Weekend" , Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(getApplicationContext(), "Press Double-Click to register", Toast.LENGTH_SHORT).show();
                            }
                        }
                    };
                    Message m = new Message();
                    myHandler.sendMessageDelayed(m,DOUBLE_PRESS_INTERVAL);
                }
                lastPressTime = pressTime;
            }
        });

        CancelCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}