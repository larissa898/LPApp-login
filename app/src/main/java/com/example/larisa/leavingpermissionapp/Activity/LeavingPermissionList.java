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

public class LeavingPermissionList extends AppCompatActivity {

    // UI
    public Button editButton;
    private Button AddButton;
    private TextView CurrentDay;
    private LeavePermissionForUserAdapter recycleViewAdapter;
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

        CurrentDay = findViewById(R.id.textViewDayCurrent);
        AddButton = findViewById(R.id.buttonAddList);
        TotalOreZi = findViewById(R.id.totalResult);
        editButton = findViewById(R.id.editButton);
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

        day = getIntent().getIntExtra("day", 0);
        actualDay = getIntent().getIntExtra("actualDay", 0);
        actualMonth = getIntent().getIntExtra("actualMonth", 0);
        actualYear = getIntent().getIntExtra("actualYear", 0);
        month = getIntent().getIntExtra("month", 0);
        year = getIntent().getIntExtra("year", 0);
        monthActual = getIntent().getStringExtra("monthActual");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        CurrentDay.setText(day + " " + monthActual + " " + year);

        final DatabaseReference dbReference;
        dbReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("LeavingPermission").
                child(day + " " + monthActual + " " + year);

        //update with Firebase
        dbReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                leavingPermissionList.clear();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                leavingPermissionList.clear();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                leavingPermissionList.clear();
                if (leavingPermissionList.size() == 0) {
                    Intent intent = new Intent(LeavingPermissionList.this, LeavingPermissionList.class);
                    intent.putExtra("day", day);
                    intent.putExtra("total", total);
                    intent.putExtra("month", month);
                    intent.putExtra("year", year);
                    intent.putExtra("monthActual", monthActual);
                    startActivity(intent);
                    finish();
                }

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //total H+M and LPList for this day from Firebase
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    float sum = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        LeavingPermission leavingPermission = snapshot.getValue(LeavingPermission.class);
                        leavingPermissionList.add(leavingPermission);
                        Log.i(LeavingPermissionList.class.getSimpleName(), "List Size: " + leavingPermissionList.size());
                        String h = snapshot.child("total").getValue().toString();
                        sum = sum + Float.parseFloat(h);
                        total = sum;
                    }
                    TotalOreZi.setText(String.valueOf(total));
                    if (total == 3.0) {
                        AddButton.setEnabled(false);
                    }
                    Current = String.valueOf(CurrentDay);
                    recycleViewAdapter = new LeavePermissionForUserAdapter(LeavingPermissionList.this, leavingPermissionList, day, month,
                            year, monthActual);
                    recyclerView.setAdapter(recycleViewAdapter);
                    recycleViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });

        //AddButton
        if ((day < actualDay && month < actualMonth && year < actualYear) || (month < actualMonth) || (year < actualYear) || (day < actualDay && month == actualMonth)) {
            AddButton.setEnabled(false);
        } else {
            AddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Flag = "add";
                    Intent intent = new Intent(LeavingPermissionList.this, RaportActivity.class);
                    intent.putExtra("Flag", Flag);
                    intent.putExtra("day", day);
                    intent.putExtra("total", total);
                    intent.putExtra("month", month);
                    intent.putExtra("year", year);
                    intent.putExtra("monthActual", monthActual);
                    Log.d("luna", String.valueOf(month));
                    startActivity(intent);
                    finish();
                }
            });
        }
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
