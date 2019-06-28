package com.example.larisa.leavingpermissionapp;

import android.content.Context;
import android.drm.DrmStore;
import android.graphics.Color;
import android.icu.text.TimeZoneFormat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.EventLog;
import android.widget.CalendarView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TestCalendar extends AppCompatActivity {
    CompactCalendarView compactCalendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_calendar);
        compactCalendarView = findViewById(R.id.compactcalendar_view);
        compactCalendarView.setLocale(Calendar.getInstance().getTimeZone(),Locale.FRANCE);
       // compactCalendarView.setEventIndicatorStyle(R.style.ThemeOverlay_MaterialComponents_Dark);
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);
        Long time = Calendar.getInstance().getTimeInMillis();
        Event evt = new Event(Color.WHITE, Long.valueOf(Calendar.getInstance().getTimeInMillis()),"Omg");
        compactCalendarView.addEvent(evt);

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            Context context = getApplicationContext();
            @Override
            public void onDayClick(Date dateClicked) {

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {

            }
        });
    }
}
