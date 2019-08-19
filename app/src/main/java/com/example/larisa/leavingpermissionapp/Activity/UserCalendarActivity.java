package com.example.larisa.leavingpermissionapp.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.larisa.leavingpermissionapp.MainActivity;
import com.example.larisa.leavingpermissionapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UserCalendarActivity extends AppCompatActivity {

    private static final String TAG = "UserCalendarActivity";

    private static final long DOUBLE_PRESS_INTERVAL = 250; // in millis
    private long lastPressTime;
    private Button OpenDay;
    private CalendarView calendarView;
    private boolean mHasDoubleClicked = false;
    private TextView Angajat;

    private DatabaseReference mDatabase;
    private RecyclerView recyclerView;
    public String Current;
    private String actualM;
    private ImageView userProfileIV;

    private int mYear, mMonth, mDayOfMonth;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        getSupportActionBar().setTitle("Leaving Permission App");

        //months of the year
        final String[] strMonths = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DATE);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);

        calendarView = findViewById(R.id.calendarViewID);
        OpenDay = findViewById(R.id.OpenDay);
        recyclerView = findViewById(R.id.recyclerViewUser);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @SuppressLint("SimpleDateFormat")
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                @SuppressLint("DefaultLocale")
                String dateString = String.format("%d-%d-%d", year, month, dayOfMonth + 3);

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
                    //If it is weekend you can not add LPs
                    //Otherwise double click will send you the next activity <<Leaving Permission List>>
                    if (dayOfWeek.equals("Sunday") || (dayOfWeek.equals("Saturday"))) {
                        Toast.makeText(getApplicationContext(), "It's weekend, choose a working day", Toast.LENGTH_SHORT).show();

                    } else {
                        mHasDoubleClicked = true;
                        Intent intent = new Intent(UserCalendarActivity.this, UserLeavingPermissionList.class);
                        //Calculates the month according to the month number
                        actualM = strMonths[month];
                        intent.putExtra("day", dayOfMonth);
                        intent.putExtra("month", month);
                        intent.putExtra("year", year);
                        intent.putExtra("currentDay", currentDay);
                        intent.putExtra("currentMonth", currentMonth);
                        intent.putExtra("currentYear", currentYear);
                        intent.putExtra("monthActual", actualM);
                        Current = (currentDay + " " + currentMonth + " " + currentYear);
                        startActivity(intent);
                    }
                } else {     // If not double click....
                    mHasDoubleClicked = false;
                    @SuppressLint("HandlerLeak") final Handler myHandler = new Handler() {
                        public void handleMessage(Message m) {
                            if (!mHasDoubleClicked && (dayOfWeek.equals("Sunday")
                                    || (dayOfWeek.equals("Saturday")))) {
                                Toast.makeText(getApplicationContext()
                                        , "It's weekend, choose a working day", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext()
                                        , "Press Double-Click to make a request or select Open Button",
                                        Toast.LENGTH_SHORT).show();
                                mYear = year;
                                mDayOfMonth = dayOfMonth;
                                mMonth = month;
                            }
                        }
                    };
                    Message m = new Message();
                    myHandler.sendMessageDelayed(m, DOUBLE_PRESS_INTERVAL);
                }

                lastPressTime = pressTime;
            }
        });


        OpenDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserCalendarActivity.this, UserLeavingPermissionList.class);
                actualM = strMonths[mMonth];
                intent.putExtra("day", mDayOfMonth);
                intent.putExtra("month", mMonth);
                intent.putExtra("year", mYear);
                intent.putExtra("currentDay", currentDay);
                intent.putExtra("currentMonth", currentMonth);
                intent.putExtra("currentYear", currentYear);
                intent.putExtra("monthActual", actualM);
                Current = (currentDay + " " + currentMonth + " " + currentYear);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_userProfile:
                startActivity(new Intent(UserCalendarActivity.this, UserProfileActivity.class));
                return true;

            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(UserCalendarActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;

        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}