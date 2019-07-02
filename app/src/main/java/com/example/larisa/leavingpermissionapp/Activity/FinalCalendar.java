package com.example.larisa.leavingpermissionapp.Activity;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;

import com.example.larisa.leavingpermissionapp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.format.DayFormatter;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Calendar;
import java.util.Formatter;

public class FinalCalendar extends AppCompatActivity {
    private MaterialCalendarView calendarView;
    private CalendarDay var ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        var = CalendarDay.today();

        Log.d("The date of this calendar is of type", var.toString());



        setContentView(R.layout.activity_final_calendar);
        calendarView = findViewById(R.id.calendarView);
        calendarView.addDecorator(new DayViewDecorator() {
            @Override
            public boolean shouldDecorate(CalendarDay day) {

                return day.equals(var);
            }

            @Override
            public void decorate(DayViewFacade view) {
                view.addSpan(new DotSpan(Color.RED));

            }
        });

        //calendarView.setDateTextAppearance(R.style.AppTheme);


    }
}
