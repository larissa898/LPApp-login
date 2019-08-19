/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.larisa.leavingpermissionapp.Model.LeavingPermission;
import com.example.larisa.leavingpermissionapp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *  Activity with calendar showing LeavingPermission requests for users selected by Team Leader in {@link ViewTeamActivity}
 *  <br>
 *  LeavingPermission neconfirmat = RED DOT
 *  <br>
 *  LeavingPermission confirmat = GREEN CIRCLE
 *  <br>
 *  Long-clicking a date with a {@link LeavingPermission} object inside it will open {@link TeamLeaderUserLPList} Activity
 */
public class TeamLeaderCalendar extends AppCompatActivity {

    private static final String TAG = "TeamLeaderCalendar";
    
    private MaterialCalendarView calendarView;
    private List<LeavingPermission> sendLeavingPermission = new ArrayList<>();
    public CalendarDay newDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();

        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final List<LeavingPermission> leavingPermissions = (List<LeavingPermission>) intent.getSerializableExtra("Lps");
        setContentView(R.layout.activity_final_calendar);
        calendarView = findViewById(R.id.calendarView);
        final List<CalendarDay> EventDays = new ArrayList<>();

        boolean flag[]={false};
        for (final LeavingPermission leavingPermission : leavingPermissions) {

            if (leavingPermission.getStatus().equals("neconfirmat")) {
                String dateFormat = leavingPermission.getData();
                final CalendarDay date = dayConverter(dateFormat);
                calendarView.addDecorator(new DayViewDecorator() {
                    @Override
                    public boolean shouldDecorate(CalendarDay day) {
                        if (date.equals(day)) {
                            EventDays.add(day);
                        }
                        return date.equals(day);
                    }

                    @Override
                    public void decorate(DayViewFacade view) {
                        view.setBackgroundDrawable(getResources().getDrawable(R.drawable.redcircle));
                        flag[date.describeContents()] = true;
                    }
                });
            } else {
                String dateFormat = leavingPermission.getData();
                final CalendarDay date = dayConverter(dateFormat);
                calendarView.addDecorator(new DayViewDecorator() {
                    public boolean shouldDecorate(CalendarDay day) {
                        if (date.equals(day)) {
                            EventDays.add(day);
                        }
                        return date.equals(day);
                    }

                    public void decorate(DayViewFacade view) {
                        if(!flag[date.describeContents()]) {
                            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.greencircle));
                        }
                    }
                });
            }


            calendarView.setOnDateLongClickListener(new OnDateLongClickListener() {
                @Override
                public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {
                    if (EventDays.contains(date)) {
                        Intent intent = new Intent(TeamLeaderCalendar.this, TeamLeaderUserLPList.class);
                        for (LeavingPermission leavingPermission : leavingPermissions) {
                            if (dayConverter(leavingPermission.getData()).equals(date)) {
                                sendLeavingPermission.add(leavingPermission);
                            }
                        }
                        intent.putExtra("TodayLP", (Serializable) sendLeavingPermission);
                        startActivity(intent);
                        sendLeavingPermission.clear();
                    }
                }
            });
        }


    }

    CalendarDay dayConverter(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String[] convertedDate = new String[0];
        try {
            convertedDate = (sdf2.format(sdf.parse(date))).split("-");
            newDate = CalendarDay.from(Integer.valueOf(convertedDate[0]), Integer.valueOf(convertedDate[1]), Integer.valueOf(convertedDate[2]));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return newDate;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
