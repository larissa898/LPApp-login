/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.larisa.leavingpermissionapp.MainActivity;
import com.example.larisa.leavingpermissionapp.Model.LeavingPermission;
import com.example.larisa.leavingpermissionapp.Model.User;
import com.example.larisa.leavingpermissionapp.R;
import com.google.firebase.auth.FirebaseAuth;
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

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Activity with calendar showing LeavingPermission requests for users selected by Team Leader in {@link ViewTeamActivity}
 * <br>
 * LeavingPermission neconfirmat = RED DOT
 * <br>
 * LeavingPermission confirmat = GREEN CIRCLE
 * <br>
 * Long-clicking a date with a {@link LeavingPermission} object inside it will open {@link TeamLeaderUserLPList} Activity
 */
public class TeamLeaderCalendar extends AppCompatActivity {

    private static final String TAG = "TeamLeaderCalendar";

    private MaterialCalendarView calendarView;
    private List<LeavingPermission> sendLeavingPermission = new ArrayList<>();
    public CalendarDay newDate;
    List<LeavingPermission> leavingPermissionList = new ArrayList<>();
    List<CalendarDay> eventDays = new ArrayList<>();


    public void addLPForUser(String userId, LPListListener listener) {

        DatabaseReference LPRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("LeavingPermission");

        LPRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {
                        for (DataSnapshot LPSnapshot : dateSnapshot.getChildren()) {

                            LeavingPermission LP = LPSnapshot.getValue(LeavingPermission.class);
                            Log.d(TAG, "addLPForUser: found LP with id = " + LP.getId());
                            leavingPermissionList.add(LP);

                        }
                    }
                }
                Log.d(TAG, "addLPForUser outside of if: lp_list = " + leavingPermissionList);
                LPRef.removeEventListener(this);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: dbError = " + databaseError.getMessage() + "\n" + databaseError.getDetails());
            }
        });


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<User> checkedUsersList = (List<User>) getIntent().getSerializableExtra("checkedUsers");

        setContentView(R.layout.activity_final_calendar);
        calendarView = findViewById(R.id.calendarView);

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                leavingPermissionList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                        User dbUser = userSnapshot.getValue(User.class);
                        checkedUsersList.stream()
                                .filter(user -> dbUser.getId().equals(user.getId()))
                                .findFirst()
                                .ifPresent(user -> {

                                    for (DataSnapshot ds : userSnapshot.child("LeavingPermission").getChildren()) {
                                        for (DataSnapshot dss : ds.getChildren()) {
                                            LeavingPermission lp = dss.getValue(LeavingPermission.class);
                                            leavingPermissionList.add(lp);
                                        }
                                    }
                                    decorateCalendar();
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: dbError = " + databaseError.getMessage() + "\n" + databaseError.getDetails());
            }
        });


        calendarView.setOnDateLongClickListener(new OnDateLongClickListener() {
            @Override
            public void onDateLongClick(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date) {
                if (eventDays.contains(date)) {
                    Intent intent = new Intent(TeamLeaderCalendar.this, TeamLeaderUserLPList.class);
                    for (LeavingPermission leavingPermission : leavingPermissionList) {
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

    public void decorateCalendar(){
        boolean flag[] = {false};
        Log.d(TAG, "decorateCalendar: " + leavingPermissionList);

        for (LeavingPermission leavingPermission : leavingPermissionList) {

            if (leavingPermission.getStatus().equals("neconfirmat")) {
                String dateFormat = leavingPermission.getData();
                CalendarDay date = dayConverter(dateFormat);
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
                        flag[date.describeContents()] = true;
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
                        if (!flag[date.describeContents()]) {
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
                startActivity(new Intent(TeamLeaderCalendar.this, UserProfileActivity.class));
                return true;

            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(TeamLeaderCalendar.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    private interface LPListListener {
        void onCallBack();
    }


}
