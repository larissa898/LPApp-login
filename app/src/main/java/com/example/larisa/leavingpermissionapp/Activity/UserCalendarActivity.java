package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UserCalendarActivity extends AppCompatActivity implements CurrentUserManager.CurrentUserManagerListener {

    private static final String TAG = "UserCalendarActivity";
    public String Current;
    String[] strMonths = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private Button openButton;
    private MaterialCalendarView calendarView;
    private DatabaseReference mDatabase;
    private String actualM;
    private ImageView userProfileIV;
    private int mYear, mMonth, mDayOfMonth;
    private String role;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CurrentUserManager currentUserManager = new CurrentUserManager(this);
        currentUserManager.retrieveCurrentUserObj(FirebaseAuth.getInstance().getUid());
        currentUserManager.retrieveUserLeavingPermissionList(FirebaseAuth.getInstance().getUid());
        setContentView(R.layout.activity_calendar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        calendarView = findViewById(R.id.calendarViewID);
        mDatabase = FirebaseDatabase.getInstance().getReference();


        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DATE);
        int currentMonth = calendar.get(Calendar.MONTH);
        int currentYear = calendar.get(Calendar.YEAR);


        calendarView.setOnDateLongClickListener(new OnDateLongClickListener() {
            @Override
            public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {

                calendarView.setSelectedDate(date);
                Log.d(TAG, "onDateLongClick: xxxxx " + calendarView.getSelectedDate());


                Intent intent = new Intent(UserCalendarActivity.this, UserLPList.class);
                actualM = strMonths[mMonth];

                intent.putExtra("day", calendarView.getSelectedDate().getDay());
                intent.putExtra("month", calendarView.getSelectedDate().getMonth());
                intent.putExtra("year", calendarView.getSelectedDate().getYear());
                intent.putExtra("currentDay", currentDay);
                intent.putExtra("currentMonth", currentMonth);
                intent.putExtra("currentYear", currentYear);
                intent.putExtra("monthActual", actualM);
                startActivity(intent);
            }
        });

    }

    private void initMaterialCalendarView(List<LeavingPermission> leavingPermissions) {
        final List<CalendarDay> eventDays = new ArrayList<>();
        calendarView.removeDecorators();

        boolean[] isRefusedorUnconfirmed = new boolean[100000];
        for (final LeavingPermission leavingPermission : leavingPermissions) {
            if (!leavingPermission.getStatus().equals("confirmat")) {
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
                        isRefusedorUnconfirmed[index] = true;
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
                        if (!isRefusedorUnconfirmed[index]) {
                            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.greencircle));
                        }
                    }
                });
            }
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
        role = user.getRole();
        invalidateOptionsMenu();
    }

    @Override
    public void onLeavingPermissionListRetrieved(List<LeavingPermission> leavingPermissionList) {
        Log.d(TAG, "onCurrentUserRetrieved: " + leavingPermissionList);
        initMaterialCalendarView(leavingPermissionList);
    }
}