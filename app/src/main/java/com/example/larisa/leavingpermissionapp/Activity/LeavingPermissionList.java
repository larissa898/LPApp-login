package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.Adapters.RecycleViewAdapter;
import com.example.larisa.leavingpermissionapp.Adapters.RecycleViewAdapterUser;
import com.example.larisa.leavingpermissionapp.Model.LP;
import com.example.larisa.leavingpermissionapp.Model.User;
import com.example.larisa.leavingpermissionapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LeavingPermissionList extends AppCompatActivity {

    private Button CancelList;
    private Button AddButton;
    private TextView CurrentDay;
    public String Current;
    private int  day;
    private int month;
    private int year;
    private int actualDay;
    private int actualMonth;
    private RecycleViewAdapterUser recycleViewAdapter;
    private RecyclerView recyclerView;
    private List<LP> LpList;
    private int actualYear;
    private Float total;
    private TextView TotalOreZi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaving_permission_list);
         String[] strMonths = {"January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"};



        recyclerView = findViewById(R.id.recyclerViewUser);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        LpList = new ArrayList<>();

        CurrentDay = findViewById(R.id.textViewDayCurrent);
        AddButton = findViewById(R.id.buttonAddList);
        CancelList = findViewById(R.id.buttonCancelList);
        TotalOreZi = findViewById(R.id.totalResult);

        day =  getIntent().getIntExtra("day",0);
        actualDay =  getIntent().getIntExtra("actualDay",0);
        actualMonth =  getIntent().getIntExtra("actualMonth",0);
        actualYear =  getIntent().getIntExtra("actualYear",0);
        month = getIntent().getIntExtra("month",0);
        year = getIntent().getIntExtra("year", 0);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        CurrentDay.setText(day + " "+ strMonths[month] + " " + year);

        DatabaseReference dbReference;
        dbReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("LP").
                child(day + " " + strMonths[month] + " " + year);

        dbReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())

                {
                    float sum = 0;
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                            final LP lp  = snapshot.getValue(LP.class);
                            LpList.add(lp);
                            String h = snapshot.child("total").getValue().toString();
                            sum = sum + Float.parseFloat(h);
                            total=sum;


                    }
                    TotalOreZi.setText(String.valueOf(total));


                    Current = String.valueOf(CurrentDay);
                    recycleViewAdapter = new RecycleViewAdapterUser(LeavingPermissionList.this, LpList, day, month, year);
                    recyclerView.setAdapter(recycleViewAdapter);
                    recycleViewAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        if((day < actualDay  &&  month < actualMonth && year < actualYear) || (month < actualMonth ) || (year < actualYear) || (day < actualDay) ){
            AddButton.setEnabled(false);

        }else{
            AddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LeavingPermissionList.this, RaportActivity.class);
                    intent.putExtra("day", day);
                    intent.putExtra("total", total);
                    intent.putExtra("month", month);
                    intent.putExtra("year", year);
                    Log.d("luna", String.valueOf(month));
                    startActivity(intent);
                }
            });
        }

        CancelList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LeavingPermissionList.this, CalendarActivity.class);
                startActivity(intent);
            }
        });
    }


}
