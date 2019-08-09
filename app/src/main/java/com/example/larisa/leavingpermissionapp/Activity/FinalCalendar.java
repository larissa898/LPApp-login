/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.larisa.leavingpermissionapp.Model.LeavingPermission;
import com.example.larisa.leavingpermissionapp.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FinalCalendar extends AppCompatActivity {
    private MaterialCalendarView calendarView;
    private List<LeavingPermission> sendLeavingPermission = new ArrayList<>();
    private Button backToTeam;
   public CalendarDay newDate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
//

        getSupportActionBar().setTitle("Leaving Permission App");


       final List<LeavingPermission> leavingPermissions = (List<LeavingPermission>) intent.getSerializableExtra("Lps");
       setContentView(R.layout.activity_final_calendar);
        calendarView = findViewById(R.id.calendarView);
        final List<CalendarDay> EventDays = new ArrayList<>();
        backToTeam=findViewById(R.id.backtoTeamButton);



  for (final LeavingPermission leavingPermission : leavingPermissions)
  {

      if(leavingPermission.getStatus().equals("neconfirmat"))
      {  String dateFormat = leavingPermission.getData();
          final CalendarDay newDate = dayConverter(dateFormat);
          Log.d("Avfdvgsfs",newDate.toString());
          calendarView.addDecorator(new DayViewDecorator() {
              @Override
              public boolean shouldDecorate(CalendarDay day) {
                  if(newDate.equals(day))
                  {
                      EventDays.add(day);
                  }
                  return newDate.equals(day);
              }

              @Override
              public void decorate(DayViewFacade view) {

                  view.addSpan(new DotSpan(Color.RED));



              }
          });
      }
  else {
       String dateFormat = leavingPermission.getData();
       final CalendarDay newDate = dayConverter(dateFormat);
       calendarView.addDecorator(new DayViewDecorator() {
           public boolean shouldDecorate(CalendarDay day) {
               if(newDate.equals(day))
                {
                    EventDays.add(day);
                }                return newDate.equals(day);
             }

             public void decorate(DayViewFacade view) {
                 view.setBackgroundDrawable(getResources().getDrawable(R.drawable.greencircle2));

            }
         });
    }






      calendarView.setOnDateLongClickListener(new OnDateLongClickListener() {
          @Override
          public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {
              if(EventDays.contains(date))
              {
                  Intent intent = new Intent(FinalCalendar.this, LPCalendarList.class);
                  for(LeavingPermission leavingPermission : leavingPermissions)
                  {
                      if(dayConverter(leavingPermission.getData()).equals(date))
                      {
                          sendLeavingPermission.add(leavingPermission);
                      }
                  }
                  intent.putExtra("TodayLP", (Serializable) sendLeavingPermission);
//                  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                  startActivity(intent);
//                  finish();
                  sendLeavingPermission.clear();



              }


          }
      });

  }


backToTeam.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

       Intent intent = new Intent(FinalCalendar.this, ViewTeamActivity.class);
       intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
       startActivity(intent);
//finish();


    }
});




    }

    CalendarDay dayConverter (String date)
    { SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
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
    public void onBackPressed()
    {
        finish();
        Intent intent = new Intent(this, ViewTeamActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

    }

}
