/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.Adapters.LeavePermissionForTLAdapter;
import com.example.larisa.leavingpermissionapp.Model.LeavingPermission;
import com.example.larisa.leavingpermissionapp.R;
import com.example.larisa.leavingpermissionapp.Utils.FirebaseOps;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class LPCalendarList extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private LeavePermissionForTLAdapter leavePermissionForTLAdapter;
    private List<LeavingPermission> leavingPermissionList;
    private Button backToCalendar;
    private Button doneConfirming;
    private TextView listDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpcalendar_list);

        listDate = findViewById(R.id.lpListDate);
        recyclerView = findViewById(R.id.recycleViewLP);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        this.leavingPermissionList = new ArrayList<>();
        Intent intent = getIntent();
        List<LeavingPermission> leavingPermissionList = (List<LeavingPermission>) intent.getSerializableExtra("TodayLP");


        if (leavingPermissionList != null) {
            listDate.setText(leavingPermissionList.get(0).getData());
        }

        leavePermissionForTLAdapter = new LeavePermissionForTLAdapter(LPCalendarList.this, leavingPermissionList);

        recyclerView.setAdapter(leavePermissionForTLAdapter);
        leavePermissionForTLAdapter.notifyDataSetChanged();
        backToCalendar = findViewById(R.id.backToCalendar);
        doneConfirming = findViewById(R.id.finishedReviewing);


        backToCalendar.setOnClickListener(this);
        doneConfirming.setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.finishedReviewing:
//

                DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users");
                for (final String key : leavePermissionForTLAdapter.modifiedLP.keySet()) {
                    final LeavingPermission leavingPermission = leavePermissionForTLAdapter.modifiedLP.get(key);
                    dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                search:
                                {
                                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                        if (snapshot.child("fullName").getValue(String.class).equals(leavingPermission.getNume())) {
                                            for (DataSnapshot snapshot1 : snapshot.child("LeavingPermission").getChildren()) {
                                                if (snapshot1.getKey().equals(leavingPermission.getData())) {
                                                    for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
//
                                                        if (snapshot2.child("id").getValue().equals(key)) {
                                                            if (leavingPermission.getStatus().equals("confirmat")) {
//
                                                                snapshot2.child("status").getRef().setValue(
                                                                        "confirmat");

                                                            } else {
//
                                                                snapshot2.child("status").getRef().setValue("refuzat");
//
//
//
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }


                finish();


                break;
            case R.id.backToCalendar:
                finish();
                break;


        }
    }

}
