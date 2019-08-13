/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import android.widget.ImageView;

import com.example.larisa.leavingpermissionapp.Adapters.UsersForTeamLeaderAdapter;
import com.example.larisa.leavingpermissionapp.MainActivity;
import com.example.larisa.leavingpermissionapp.Model.LeavingPermission;
import com.example.larisa.leavingpermissionapp.Model.User;
import com.example.larisa.leavingpermissionapp.R;
import com.example.larisa.leavingpermissionapp.Utils.FirebaseOps;
import com.example.larisa.leavingpermissionapp.Utils.FirebaseOpsListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ViewTeamActivity extends AppCompatActivity implements Serializable, FirebaseOpsListener {

    private static final String TAG = "ViewTeamActivity";

    // UI
    private RecyclerView recyclerView;
    private UsersForTeamLeaderAdapter usersForTeamLeaderAdapter;
    private Button confirmButton;
    private ImageView unassignedUserIV;

    // Vars
    private List<User> usersList;
    private User currentUser;

    // Firebase
    FirebaseOps firebaseOps;


    public void initUI() {
        recyclerView = findViewById(R.id.recycleViewActivity);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        confirmButton = findViewById(R.id.confirmButton);
        unassignedUserIV = findViewById(R.id.unassignedUserIV);


        //TODO: finish this part after adding/finishing the UnassignedUsersActivity code.

        startBlinkingAnimation();

        unassignedUserIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ViewTeamActivity.this, UnassignedUsersActivity.class));

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
        getSupportActionBar().setTitle("Leaving Permission App");
        usersList = new ArrayList<>();


        DatabaseReference currentUserRef = firebaseOps.getUsersRef();
        currentUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);


                        // if user in list has Team Leader nrMatricol the same as the currently logged in user
                        if (user.getTeamLeader() != null && user.getTeamLeader().equals(firebaseOps.getCurrentUser().getRegistrationNumber()))
                            usersList.add(user);

                    }

                    usersForTeamLeaderAdapter = new UsersForTeamLeaderAdapter(ViewTeamActivity.this, usersList);
                    recyclerView.setAdapter(usersForTeamLeaderAdapter);
                    usersForTeamLeaderAdapter.notifyDataSetChanged();


                }


            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        confirmButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(ViewTeamActivity.this, FinalCalendar.class);
                final List<LeavingPermission> leavingPermissionList = new ArrayList<>();

                final DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users");
                final int[] i = {0};
                i[0] = 0;

                for (final User u : usersForTeamLeaderAdapter.checkedUsers) {

                    i[0]++;

                    dbReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    if (snapshot.child("lastName").getValue().equals(u.getLastName())
                                            && snapshot.child("firstName").getValue().equals(u.getFirstName())) {
                                        for (DataSnapshot snapshot1 : snapshot.child("LeavingPermission").getChildren()) {
                                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                LeavingPermission leavingPermission = snapshot2.getValue(LeavingPermission.class);

                                                String date = snapshot1.getKey();
                                                String lastName = snapshot.child("lastName").getValue(String.class);
                                                String firstName = snapshot.child("firstName").getValue(String.class);

                                                String role = snapshot.child("role").getValue(String.class);
                                                String phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                                                String registrationNumber = snapshot.child("registrationNumber").getValue(String.class);

                                                User user = new User(lastName, firstName, role, phoneNumber, registrationNumber);

                                                leavingPermission.setUser(user);

                                                leavingPermission.setData(date);
                                                leavingPermissionList.add(leavingPermission);

                                            }
                                        }
                                    }
                                }
                            }

                            intent.putExtra("Lps", (Serializable) leavingPermissionList);
                            startActivity(intent);

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                    });

                }


                Log.d(TAG, String.valueOf(leavingPermissionList.size()));
                dbReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                        leavingPermissionList.clear();

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }


        });

    }


    private void startBlinkingAnimation() {
        Animation animation = new AlphaAnimation(1, 0);
        animation.setDuration(1000);
        animation.setInterpolator(new LinearInterpolator());
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        unassignedUserIV.setAnimation(animation);
    }

    private void stopBlinkingAnimation() {
        unassignedUserIV.setAnimation(null);
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
                startActivity(new Intent(ViewTeamActivity.this, UserProfileActivity.class));
                return true;

            case R.id.menu_logout:
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ViewTeamActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return true;
    }


    @Override
    public void onUsersCallback() {

    }

    @Override
    public void onRolesCallback() {

    }
}


