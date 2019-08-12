/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.Adapters.UnassignedUserForTeamLeaderAdapter;
import com.example.larisa.leavingpermissionapp.Model.User;
import com.example.larisa.leavingpermissionapp.R;
import com.example.larisa.leavingpermissionapp.Utils.FirebaseOps;
import com.example.larisa.leavingpermissionapp.Utils.FirebaseOpsListener;

import java.util.List;

/**
 *
 */
public class UnassignedUsersActivity extends AppCompatActivity implements FirebaseOpsListener {

    private static final String TAG = "UnassignedUsersActivity";

    // UI
    private RelativeLayout rL;
    private TextView chooseTeamMemberTV;
    private RecyclerView recyclerView;
    private UnassignedUserForTeamLeaderAdapter adapter;
    private Button addButton;
    private Button cancelButton;

    // Vars
    private List<User> userList;

    // Firebase
    private FirebaseOps firebaseOps;

    private void initUI() {
        rL = findViewById(R.id.unassignedUserRelativeLayout);

        chooseTeamMemberTV = findViewById(R.id.chooseTeamMemberTV);

        recyclerView = findViewById(R.id.unassignedUsersRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addButton = findViewById(R.id.addUsersToTeamButton);
        cancelButton = findViewById(R.id.cancelUsersToTeamButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void initFirebase() {
        firebaseOps = FirebaseOps.getInstance();
        firebaseOps.setListener(this);
    }

    private void setWindowSize(double widthRatio, double heightRatio) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int) (width * widthRatio), (int) (height * heightRatio));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unassigned_users);
        initUI();
        initFirebase();

        setWindowSize(0.83, 0.82);

        adapter = new UnassignedUserForTeamLeaderAdapter(this, firebaseOps.getUsers());
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onUsersCallback() {
        Log.d(TAG, "onUsersCallback: xxxxxxxxxxxx = " + firebaseOps.getUsers());
        adapter.setList(firebaseOps.getUsers());
    }

    @Override
    public void onRolesCallback() {
    }

}
