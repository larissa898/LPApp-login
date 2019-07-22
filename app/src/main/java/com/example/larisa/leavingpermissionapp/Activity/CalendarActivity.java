package com.example.larisa.leavingpermissionapp.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.larisa.leavingpermissionapp.Adapters.RecycleViewAdapterUser;
import com.example.larisa.leavingpermissionapp.Database.Database;
import com.example.larisa.leavingpermissionapp.Model.LP;
import com.example.larisa.leavingpermissionapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    private static final long DOUBLE_PRESS_INTERVAL = 250; // in millis
    private long lastPressTime;
    private Button CancelCalendar;
    private CalendarView calendarView;
    private boolean mHasDoubleClicked = false;
    private TextView Angajat;
    int actualDay;
    int actualMonth;
    int actualYear;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    public String Current;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        final String[] strMonths = {"January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"};

        Calendar cal = Calendar.getInstance();

        actualDay = cal.get(Calendar.DATE);
        actualMonth = cal.get(Calendar.MONTH);
        actualYear = cal.get(Calendar.YEAR);
        CancelCalendar = findViewById(R.id.CancelButtonCalendar);
        calendarView = findViewById(R.id.calendarViewID);
        Angajat = findViewById(R.id.NumeAngajatCalendar);
        recyclerView = findViewById(R.id.recyclerViewUser);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference functionRef =  FirebaseDatabase.getInstance().getReference("Users");

        Query query =  functionRef.child(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    String nume = dataSnapshot.child("fullName").getValue(String.class);
                    Angajat.setText("Buna " +  nume + "!" );

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange( CalendarView view, int year, int month, int dayOfMonth) {
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
                            Toast.makeText(getApplicationContext(), "It's weekend, choose a working day"
                                    , Toast.LENGTH_SHORT).show();

                        }else{

                            mHasDoubleClicked = true;
                            Intent intent = new Intent(CalendarActivity.this
                                    , LeavingPermissionList.class);
                            intent.putExtra("day", dayOfMonth);
                            intent.putExtra("month", month);
                            intent.putExtra("year", year);
                            intent.putExtra("actualDay", actualDay);
                            intent.putExtra("actualMonth", actualMonth);
                            intent.putExtra("actualYear", actualYear);
                            Current = (actualDay + " " + actualMonth+ " "+actualYear);
                            startActivity(intent);
                            onRestart();

                        }
                    }
                    else {     // If not double click....
                        mHasDoubleClicked = false;
                        @SuppressLint("HandlerLeak") final Handler myHandler = new Handler() {
                            public void handleMessage(Message m) {
                                if (!mHasDoubleClicked && (dayOfWeek.equals("Sunday")
                                        || (dayOfWeek.equals("Saturday")))) {
                                    Toast.makeText(getApplicationContext()
                                            , "It's weekend, choose a working day" , Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(getApplicationContext()
                                            , "Press Double-Click to make a request", Toast.LENGTH_SHORT).show();
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
    @Override
    protected void onRestart() {
        super.onRestart();

    }

}
