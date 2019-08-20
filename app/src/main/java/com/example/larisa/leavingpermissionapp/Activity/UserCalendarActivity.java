package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.MainActivity;
import com.example.larisa.leavingpermissionapp.Model.LeavingPermission;
import com.example.larisa.leavingpermissionapp.Model.User;
import com.example.larisa.leavingpermissionapp.R;
import com.example.larisa.leavingpermissionapp.Utils.CurrentUserManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

public class UserCalendarActivity extends AppCompatActivity implements CurrentUserManager.CurrentUserManagerListener {

    private static final String TAG = "UserCalendarActivity";

    private Button openButton;
    private MaterialCalendarView calendarView;

    private DatabaseReference mDatabase;
    public String Current;
    private String actualM;
    private ImageView userProfileIV;

    private int mYear, mMonth, mDayOfMonth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CurrentUserManager(this).retrieveCurrentUserLeavingPermissionList(FirebaseAuth.getInstance().getUid());
        setContentView(R.layout.activity_calendar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendarView = findViewById(R.id.calendarViewID);
        openButton = findViewById(R.id.openButton);
        mDatabase = FirebaseDatabase.getInstance().getReference();

    }

    //TODO: DELETE THIS ONCE DONE --- initOldCalendar contains the code for handling date clicks for the old type of calendar
    private void initOldCalendar() {

//        String[] strMonths = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

//        Calendar calendar = Calendar.getInstance();
//        int currentDay = calendar.get(Calendar.DATE);
//        int currentMonth = calendar.get(Calendar.MONTH);
//        int currentYear = calendar.get(Calendar.YEAR);

//        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//
//            @SuppressLint("SimpleDateFormat")
//            @Override
//            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
//                @SuppressLint("DefaultLocale")
//                String dateString = String.format("%d-%d-%d", year, month, dayOfMonth + 3);
//
//                Date date = null;
//                try {
//                    date = new SimpleDateFormat("yyyy-M-d").parse(dateString);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                final String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
//                view.getFirstDayOfWeek();
//                final long pressTime = System.currentTimeMillis();
//
//                // If double click...
//                if (pressTime - lastPressTime <= DOUBLE_PRESS_INTERVAL) {
//                    //If it is weekend you can not add LPs
//                    //Otherwise double click will send you the next activity <<Leaving Permission List>>
//                    if (dayOfWeek.equals("Sunday") || (dayOfWeek.equals("Saturday"))) {
//                        Toast.makeText(getApplicationContext(), "It's weekend, choose a working day", Toast.LENGTH_SHORT).show();
//
//                    } else {
//                        mHasDoubleClicked = true;
//                        Intent intent = new Intent(UserCalendarActivity.this, UserLPList.class);
//                        //Calculates the month according to the month number
//                        actualM = strMonths[month];
//                        intent.putExtra("day", dayOfMonth);
//                        intent.putExtra("month", month);
//                        intent.putExtra("year", year);
//                        intent.putExtra("currentDay", currentDay);
//                        intent.putExtra("currentMonth", currentMonth);
//                        intent.putExtra("currentYear", currentYear);
//                        intent.putExtra("monthActual", actualM);
//                        Current = (currentDay + " " + currentMonth + " " + currentYear);
//                        startActivity(intent);
//                    }
//                } else {     // If not double click....
//                    mHasDoubleClicked = false;
//                    @SuppressLint("HandlerLeak") final Handler myHandler = new Handler() {
//                        public void handleMessage(Message m) {
//                            if (!mHasDoubleClicked && (dayOfWeek.equals("Sunday")
//                                    || (dayOfWeek.equals("Saturday")))) {
//                                Toast.makeText(getApplicationContext()
//                                        , "It's weekend, choose a working day", Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(getApplicationContext()
//                                        , "Press Double-Click to make a request or select Open Button",
//                                        Toast.LENGTH_SHORT).show();
//                                mYear = year;
//                                mDayOfMonth = dayOfMonth;
//                                mMonth = month;
//                            }
//                        }
//                    };
//                    Message m = new Message();
//                    myHandler.sendMessageDelayed(m, DOUBLE_PRESS_INTERVAL);
//                }
//
//                lastPressTime = pressTime;
//            }
//        });
//
//
//        openButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(UserCalendarActivity.this, UserLPList.class);
//                actualM = strMonths[mMonth];
//                intent.putExtra("day", mDayOfMonth);
//                intent.putExtra("month", mMonth);
//                intent.putExtra("year", mYear);
//                intent.putExtra("currentDay", currentDay);
//                intent.putExtra("currentMonth", currentMonth);
//                intent.putExtra("currentYear", currentYear);
//                intent.putExtra("monthActual", actualM);
//                Current = (currentDay + " " + currentMonth + " " + currentYear);
//                startActivity(intent);
//            }
//        });
    }

    private void initMaterialCalendarView(List<LeavingPermission> leavingPermissions) {
        final List<CalendarDay> eventDays = new ArrayList<>();

        boolean flag[] = new boolean[100000];
        for (final LeavingPermission leavingPermission : leavingPermissions) {
            Log.d(TAG, "initMaterialCalendarView: " + leavingPermission);
            if (leavingPermission.getStatus().equals("neconfirmat")) {
                String dateFormat = leavingPermission.getData();
                final CalendarDay date = dayConverter(dateFormat);
                calendarView.addDecorator(new DayViewDecorator() {
                    @Override
                    public boolean shouldDecorate(CalendarDay day) {
                        if (date.equals(day)) {
                            eventDays.add(day);
                        }
                        return date.equals(day);
                    }

                    @Override
                    public void decorate(DayViewFacade view) {
                        view.setBackgroundDrawable(getResources().getDrawable(R.drawable.redcircle));
                        int index = date.hashCode() % 100000;
                        flag[index] = true;
                    }
                });
            } else {
                String dateFormat = leavingPermission.getData();
                final CalendarDay date = dayConverter(dateFormat);
                calendarView.addDecorator(new DayViewDecorator() {
                    public boolean shouldDecorate(CalendarDay day) {
                        if (date.equals(day)) {
                            eventDays.add(day);
                        }
                        return date.equals(day);
                    }

                    public void decorate(DayViewFacade view) {
                        int index = date.hashCode() % 100000;
                        if (!flag[index]) {
                            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.greencircle));
                        }
                    }
                });
            }

            //TODO: handle on long click

//            calendarView.setOnDateLongClickListener(new OnDateLongClickListener() {
//                @Override
//                public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {
//
//                    Intent intent = new Intent(UserCalendarActivity.this, UserLPList.class);
//                    actualM = strMonths[mMonth];
//                    intent.putExtra("day", mDayOfMonth);
//                    intent.putExtra("month", mMonth);
//                    intent.putExtra("year", mYear);
//                    intent.putExtra("currentDay", currentDay);
//                    intent.putExtra("currentMonth", currentMonth);
//                    intent.putExtra("currentYear", currentYear);
//                    intent.putExtra("monthActual", actualM);
//                    Current = (currentDay + " " + currentMonth + " " + currentYear);
//                    startActivity(intent);
//                }
//
//            });
        }
    }

    CalendarDay dayConverter(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        CalendarDay newDate = null;
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
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }


    @Override
    public void onCurrentUserRetrieved(User user) {
    }

    @Override
    public void onLeavingPermissionListRetrieved(List<LeavingPermission> leavingPermissionList) {
        Log.d(TAG, "onCurrentUserRetrieved: " + leavingPermissionList);
        initMaterialCalendarView(leavingPermissionList);
    }
}