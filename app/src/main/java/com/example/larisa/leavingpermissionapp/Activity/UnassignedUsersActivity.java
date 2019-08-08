/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;

import com.example.larisa.leavingpermissionapp.Adapters.UnassignedUserForTeamLeaderAdapter;
import com.example.larisa.leavingpermissionapp.Model.User;
import com.example.larisa.leavingpermissionapp.R;
import com.example.larisa.leavingpermissionapp.Utils.FirebaseOps;

import java.util.List;

/**
 *
 */
public class UnassignedUsersActivity extends AppCompatActivity {

    private static final String TAG = "UnassignedUsersActivity";

    // UI
    private RecyclerView recyclerView;
    private UnassignedUserForTeamLeaderAdapter adapter;
    private Button addUsersToTeamButton;

    // Vars
    private List<User> userList;

    // Firebase
    private FirebaseOps firebaseOps;

    private void initUI() {
        recyclerView = findViewById(R.id.unassignedUsersRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addUsersToTeamButton = findViewById(R.id.addUsersToTeamButton);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unassigned_users);



    }


}
