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
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.larisa.leavingpermissionapp.MainActivity;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {

    private static final long DOUBLE_PRESS_INTERVAL = 250; // in millis
    private long lastPressTime;
    private Button CancelCalendar;
    private Button OpenDay;
    private CalendarView calendarView;
    private boolean mHasDoubleClicked = false;
    private TextView Angajat;
    int actualDay;
    int actualMonth;
    int actualYear;
    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    public String Current;
    private String actualM;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        //months of the year
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

        Calendar calendar = Calendar.getInstance();

        actualDay = calendar.get(Calendar.DATE);
        actualMonth = calendar.get(Calendar.MONTH);
        actualYear = calendar.get(Calendar.YEAR);
        CancelCalendar = findViewById(R.id.CancelButtonCalendar);
        calendarView = findViewById(R.id.calendarViewID);
        Angajat = findViewById(R.id.NumeAngajatCalendar);
        OpenDay = findViewById(R.id.OpenDay);
        recyclerView = findViewById(R.id.recyclerViewUser);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference functionRef =  FirebaseDatabase.getInstance().getReference("Users");

        //Set the welcome message with the user name
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

        //
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
                        //If it is weekend you can not add LPsadd
                        //Otherwise double click will send you the next activity <<Leaving Permission List>>
                        if ( dayOfWeek.equals("Sunday") || (dayOfWeek.equals("Saturday"))){
                            Toast.makeText(getApplicationContext(), "It's weekend, choose a working day"
                                    , Toast.LENGTH_SHORT).show();

                        }
                        else{

                            mHasDoubleClicked = true;
                            Intent intent = new Intent(CalendarActivity.this
                                    , LeavingPermissionList.class);

                            //Calculates the month according to the month number
                            actualM=strMonths[month];
                            intent.putExtra("day", dayOfMonth);
                            intent.putExtra("month", month);
                            intent.putExtra("year", year);
                            intent.putExtra("actualDay", actualDay);
                            intent.putExtra("actualMonth", actualMonth);
                            intent.putExtra("actualYear", actualYear);
                            intent.putExtra("monthActual", actualM);
                            Current = (actualDay + " " + actualMonth+ " "+actualYear);
                            startActivity(intent);
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
                                            , "Press Double-Click to make a request or select Open Button",
                                            Toast.LENGTH_SHORT).show();
                                    OpenDay.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(CalendarActivity.this
                                                    , LeavingPermissionList.class);
                                            actualM=strMonths[month];
                                            intent.putExtra("day", dayOfMonth);
                                            intent.putExtra("month", month);
                                            intent.putExtra("year", year);
                                            intent.putExtra("actualDay", actualDay);
                                            intent.putExtra("actualMonth", actualMonth);
                                            intent.putExtra("actualYear", actualYear);
                                            intent.putExtra("monthActual", actualM);
                                            Current = (actualDay + " " + actualMonth+ " "+actualYear);
                                            startActivity(intent);
                                        }
                                    });
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
                Intent intent = new Intent(CalendarActivity.this
                        , MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected void onRestart() {
        super.onRestart();

    }

}
