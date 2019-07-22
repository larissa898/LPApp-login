package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;

import com.example.larisa.leavingpermissionapp.Model.LP;
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
    private List<LP> sendLP = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent intent = getIntent();
       final List<LP> lps= (List<LP>) intent.getSerializableExtra("Lps");
       setContentView(R.layout.activity_final_calendar);
        calendarView = findViewById(R.id.calendarView);
        final List<CalendarDay> EventDays = new ArrayList<>();



  for (final LP lp : lps)
  {
      if(lp.getStatus().equals("neconfirmat"))
      {  String dateFormat = lp.getData();
          final CalendarDay newDate = dayConverter(dateFormat);
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
      calendarView.setOnDateLongClickListener(new OnDateLongClickListener() {
          @Override
          public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {
              if(EventDays.contains(date))
              {
                  Intent intent = new Intent(FinalCalendar.this, LPCalendarList.class);
                  for(LP lp :lps)
                  {
                      if(dayConverter(lp.getData()).equals(date))
                      {
                          sendLP.add(lp);
                      }
                  }
                  intent.putExtra("TodayLP", (Serializable)sendLP);
                  startActivity(intent);
                  sendLP.clear();
              }
          }
      });

  }
    }
    CalendarDay dayConverter (String date)
    { SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String[] convertedDate = new String[0];
        try {
            convertedDate = (sdf2.format(sdf.parse(date))).split("-");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        CalendarDay newDate = CalendarDay.from(Integer.valueOf(convertedDate[0]), Integer.valueOf(convertedDate[1]), Integer.valueOf(convertedDate[2]));
        return newDate;

    }
}
