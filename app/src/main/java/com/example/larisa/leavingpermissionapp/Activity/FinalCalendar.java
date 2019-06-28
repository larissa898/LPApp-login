package com.example.larisa.leavingpermissionapp.Activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CalendarView;

import com.example.larisa.leavingpermissionapp.R;

import java.util.Calendar;

public class FinalCalendar extends AppCompatActivity {
    private CalendarView calendarView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_calendar);
        calendarView = findViewById(R.id.calendarView);
        //calendarView.setDateTextAppearance();


    }
}
