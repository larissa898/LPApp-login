package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;

import com.example.larisa.leavingpermissionapp.Model.LP;
import com.example.larisa.leavingpermissionapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateLongClickListener;
import com.prolificinteractive.materialcalendarview.format.DayFormatter;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.security.Key;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FinalCalendar extends AppCompatActivity {
    private MaterialCalendarView calendarView;
    private CalendarDay var ;
    private List<String > date;
    private HashMap <CalendarDay,List<LP>> dates;
    final HashMap<CalendarDay,List<LP>> formattedDates = new HashMap<>();


    private static final String KEY_LPS = "KEY_LPS";

    public static void start(Context context, ArrayList<String> ids) {
        Intent starter = new Intent(context, FinalCalendar.class);
        starter.putExtra(KEY_LPS, ids);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        var = CalendarDay.today();


        DatabaseReference dbReference;
        Intent intent = getIntent();
        final ArrayList<String> ids = (ArrayList<String>) intent.getSerializableExtra(KEY_LPS);
        final List<LP>lps = new ArrayList<>();


        setContentView(R.layout.activity_final_calendar);
        calendarView = findViewById(R.id.calendarView);

        Log.d("Passed ids are", ids.get(0));
        for(int i = 0; i<ids.size();i++)
        {

            dbReference = FirebaseDatabase.getInstance().getReference("Users");
            final int finalI = i;
            dbReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists() )
                    {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            Log.d("Snapshot is", snapshot.toString());
                            if (snapshot.getKey().equals(ids.get(finalI))) {

                                        for (DataSnapshot snapshot1 : snapshot.child("LP").getChildren()) {
                                          for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                              LP lp = snapshot2.getValue(LP.class);
                                              String dateFormat = snapshot1.getKey();
                                              SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                                              SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                                              String[] convertedDate = new String[0];
                                              try {
                                                  convertedDate = (sdf2.format(sdf.parse(dateFormat))).split("-");


                                              } catch (ParseException e) {
                                                  e.printStackTrace();
                                              }
                                              final CalendarDay newDate = CalendarDay.from(Integer.valueOf(convertedDate[0]), Integer.valueOf(convertedDate[1]), Integer.valueOf(convertedDate[2]));
                                              Log.d("Formatted date is", newDate.toString());

                                              calendarView.addDecorator(new DayViewDecorator() {
                                                  @Override
                                                  public boolean shouldDecorate(CalendarDay day) {

                                                      return newDate.equals(day);
                                                  }

                                                  @Override
                                                  public void decorate(DayViewFacade view) {
                                                      view.addSpan(new DotSpan(Color.RED));

                                                  }



//                                          }
                                          });
                                        }
                                     }

                             }









                }}}

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });






























    }
}
}
