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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.Adapters.LeavePermissionForUserAdapter;
import com.example.larisa.leavingpermissionapp.Model.LeavingPermission;
import com.example.larisa.leavingpermissionapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Will open {@link RaportActivity} with the corresponding Flag.
 * <br>
 * Flag can be "add" or "edit".
 */
public class UserLeavingPermissionList extends AppCompatActivity {

    private static final String TAG = "UserLeavingPermissionLi";

    // UI
    private Button AddBtn;
    private TextView currentDayTV;
    private LeavePermissionForUserAdapter adapter;
    private RecyclerView recyclerView;
    private TextView TotalOreZi;

    // Vars
    public String Current;
    private String monthActual;
    private int day, month, year, actualDay, actualMonth, actualYear;
    private List<LeavingPermission> leavingPermissionList;
    private Float total;


    private void initUI() {
        recyclerView = findViewById(R.id.recyclerViewUser);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        currentDayTV = findViewById(R.id.textViewDayCurrent);
        AddBtn = findViewById(R.id.buttonAddList);
        TotalOreZi = findViewById(R.id.totalResult);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        setContentView(R.layout.activity_leaving_permission_list);

        initUI();

        leavingPermissionList = new ArrayList<>();

        actualDay = getIntent().getIntExtra("currentDayTV", 0);
        actualMonth = getIntent().getIntExtra("currentMonth", 0);
        actualYear = getIntent().getIntExtra("currentYear", 0);

        day = getIntent().getIntExtra("day", 0);
        month = getIntent().getIntExtra("month", 0);
        year = getIntent().getIntExtra("year", 0);
        monthActual = getIntent().getStringExtra("monthActual");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        currentDayTV.setText(day + " " + monthActual + " " + year);

        DatabaseReference LPSelectedDateRef = FirebaseDatabase.getInstance().getReference("Users")
                .child(user.getUid()).child("LeavingPermission")
                .child(day + " " + monthActual + " " + year);


        //total H+M and LPList for this day from Firebase
        LPSelectedDateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    leavingPermissionList.clear();
                    float sum = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        LeavingPermission leavingPermission = snapshot.getValue(LeavingPermission.class);
                        leavingPermissionList.add(leavingPermission);

                        String h = snapshot.child("total").getValue().toString();
                        sum = sum + Float.parseFloat(h);
                        total = sum;
                    }

                    TotalOreZi.setText(String.valueOf(total));
                    if (total == 3.0) {
                        AddBtn.setEnabled(false);
                    }

                    Current = String.valueOf(currentDayTV);
                    adapter = new LeavePermissionForUserAdapter(UserLeavingPermissionList.this, leavingPermissionList, day, month, year, monthActual);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                else {
                    leavingPermissionList.clear();
                    adapter = new LeavePermissionForUserAdapter(UserLeavingPermissionList.this, leavingPermissionList, day, month, year, monthActual);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: databaseError = " + databaseError.getDetails());
            }

        });


            AddBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Flag = "add";
                    Intent intent = new Intent(UserLeavingPermissionList.this, RaportActivity.class);
                    intent.putExtra("Flag", Flag);
                    intent.putExtra("day", day);
                    intent.putExtra("total", total);
                    intent.putExtra("month", month);
                    intent.putExtra("year", year);
                    intent.putExtra("monthActual", monthActual);
                    Log.d("luna", String.valueOf(month));
                    startActivity(intent);
                }
            });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


}
