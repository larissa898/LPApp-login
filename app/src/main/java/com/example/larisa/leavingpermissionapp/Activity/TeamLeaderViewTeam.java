/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.example.larisa.leavingpermissionapp.Adapters.UsersForTeamLeaderAdapter;
import com.example.larisa.leavingpermissionapp.MainActivity;
import com.example.larisa.leavingpermissionapp.Model.User;
import com.example.larisa.leavingpermissionapp.R;
import com.example.larisa.leavingpermissionapp.Utils.CurrentUserManager;
import com.example.larisa.leavingpermissionapp.Utils.FirebaseOps;
import com.example.larisa.leavingpermissionapp.Utils.FirebaseOpsListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TeamLeaderViewTeam extends AppCompatActivity implements Serializable, FirebaseOpsListener {

    private static final String TAG = "TeamLeaderViewTeam";

    // UI
    private RecyclerView recyclerView;
    private UsersForTeamLeaderAdapter adapter;

    private CheckBox checkAllCB;

    private Button selectButton;
    private ImageView unassignedUserIV;

    // Vars
    private List<User> usersList;
    private User currentUser;

    // Firebase
    FirebaseOps firebaseOps;

    public CheckBox getCheckAllCB() {
        return checkAllCB;
    }

    public void initUI() {
        recyclerView = findViewById(R.id.recycleViewActivity);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        selectButton = findViewById(R.id.select_button);
        checkAllCB = findViewById(R.id.selectAllCheckbox);
        unassignedUserIV = findViewById(R.id.unassignedUserIV);
        unassignedUserIV.setVisibility(View.GONE);

        checkAllCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Log.d(TAG, "onCheckedChanged: b = true");
                    adapter.selectAll();
                } else {
                    Log.d(TAG, "onCheckedChanged: b = false");
                    adapter.unselectall();
                }
            }
        });

        unassignedUserIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TeamLeaderViewTeam.this, TeamLeaderUnassignedUsersList.class));
            }
        });

    }

    public void initFirebase() {
        firebaseOps = FirebaseOps.getInstance();
        firebaseOps.setListener(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_team);

        initUI();
        initFirebase();
        getSupportActionBar().setTitle("");
        usersList = new ArrayList<>();


        // Fill recyclerView with team of currently logged in TeamLeader
        DatabaseReference usersReff = firebaseOps.getAllUsersRef();
        usersReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);

                        // if user in list has Team Leader fullName the same as the currently logged in user
                        if (user.getTeamLeader() != null && user.getTeamLeader().equals(CurrentUserManager.currentUser.getFullName()))
                            usersList.add(user);

                    }

                    adapter = new UsersForTeamLeaderAdapter(TeamLeaderViewTeam.this, usersList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!adapter.checkedUsers.isEmpty()) {
                    Intent teamLeaderIntent = new Intent(TeamLeaderViewTeam.this, TeamLeaderCalendar.class);
                    teamLeaderIntent.putExtra("checkedUsers", (Serializable) adapter.checkedUsers);
                    startActivity(teamLeaderIntent);
                }

            }
        });
        checkAllCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


    private void startBlinkingAnimation() {
        unassignedUserIV.setVisibility(View.VISIBLE);
        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        unassignedUserIV.setAnimation(animation);
    }

    private void stopBlinkingAnimation() {
        unassignedUserIV.setAnimation(null);
        unassignedUserIV.setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile_myreq_logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_userProfile:
                startActivity(new Intent(TeamLeaderViewTeam.this, UserProfileActivity.class));
                return true;
            case R.id.menu_toggle_requests:
                startActivity(new Intent(TeamLeaderViewTeam.this, UserCalendarActivity.class));
                return true;
            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(TeamLeaderViewTeam.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return true;
    }


    @Override
    public void onUsersCallback() {
        for (User user : firebaseOps.getUsers()) {
            if (user.getTeamLeader() == null && !user.getRole().equals("Team Leader")) {
                Log.d(TAG, "onUsersCallback: found unassigned user.");
                startBlinkingAnimation();
                return;
            }
        }
        Log.d(TAG, "onUsersCallback: no unassigned user found");
        stopBlinkingAnimation();
    }

    @Override
    public void onRolesCallback() {

    }

    /**
     * Set firebaseOps listener back to this, when returning from {@link TeamLeaderUnassignedUsersList}
     */
    @Override
    protected void onResume() {
        firebaseOps = FirebaseOps.getInstance();
        firebaseOps.setListener(this);
        onUsersCallback();

        super.onResume();
    }
}


