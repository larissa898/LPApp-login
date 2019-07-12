/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.Adapters.RecycleViewAdapterUser;
import com.example.larisa.leavingpermissionapp.Model.LP;
import com.example.larisa.leavingpermissionapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Button Confirm;
    private Spinner From;
    private Spinner To;
    private TextView date;
    private TextView Nume;
    private Button Beckraport;
    public int minnn;
    public int ora;
    private TextView TotalOre;
    private int  day;
    private int month;
    private int year;
    private String from1;
    private String to1;
    private String status = "neconfirmat";
    private Float total;
    String first="";
    String second="";
    private int minn;


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raport);

        final String[] items = new String[]{"7:30", "8:00", "8:30", "9:00", "9:30",
                "10:00", "10:30", "11:00", "11:30","12:00", "12:30", "13:00" ,"13:30",
                "14:00", "14:30","15:00", "15:30", "16:00", "16:30","17:00", "17:30",
                "18:00", "18:30", "19:00"};

        final String[] strMonths = {"January",
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


        Confirm = findViewById(R.id.ConfirmButtonRaportEdit);
        From = findViewById(R.id.spinnerFromEdit);
        To = findViewById(R.id.spinnerToEdit);
        TotalOre = findViewById(R.id.TotalTextViewEdit);
        date = findViewById(R.id.editTextDateEdit);
        Nume = findViewById(R.id.textViewNumeEdit);
        Beckraport = findViewById(R.id.buttonBackRaportEdit);
        day =  getIntent().getIntExtra("day",0);
        month = getIntent().getIntExtra("month",0);
        year = getIntent().getIntExtra("year", 0);
        total = getIntent().getFloatExtra("total",0);
        from1= getIntent().getStringExtra("from");
        to1 = getIntent().getStringExtra("to");

        //date.setText(day + " "+ strMonths[month] + " " + year );

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference dbReference;
        final DatabaseReference functionRef =  FirebaseDatabase.getInstance().getReference("Users");
        dbReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("LP").
                child(day + " " + strMonths[month] + " " + year);

        dbReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {

                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                      Log.d(("!!"), String.valueOf(snapshot));
                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }

        });


        final Query query =  functionRef.child(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {

                    String nume = dataSnapshot.child("fullName").getValue(String.class);

                   // Nume.setText(nume);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
               //
        // items);//set the spinners adapter to the previously created one.

//        From.setAdapter(adapter);
//
//        Confirm.setEnabled(false);
//
//        From.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                first = From.getSelectedItem().toString();
//
//
//                String[] hourMinFrom = first.split(":");
//
//                int FromMinutes  = Integer.valueOf(hourMinFrom[1]);
//                int FromHour = Integer.valueOf(hourMinFrom[0]);
//                List<String> ToList = new ArrayList<>();
//
//                if(     ((FromHour ==  8 ) && (FromMinutes == 0)) || ((FromHour ==  9) &&  (FromMinutes == 0)) ||
//                        ((FromHour ==  10) && (FromMinutes == 0)) || ((FromHour ==  11) && (FromMinutes == 0)) ||
//                        ((FromHour ==  12) && (FromMinutes == 0)) || ((FromHour ==  13) && (FromMinutes == 0)) ||
//                        ((FromHour ==  14) && (FromMinutes == 0)) || ((FromHour ==  15) && (FromMinutes == 0)) ||
//                        ((FromHour ==  16) && (FromMinutes == 0)) || ((FromHour ==  17) && (FromMinutes == 0)) ||
//                        ((FromHour ==  18) && (FromMinutes == 0)) || ((FromHour ==  19) && (FromMinutes == 0))) {
//
//                    for(int j=0; j<= 2*(20-FromHour); j=j+2)
//                    {
//                        ToList.add(FromHour   +":"+ 30 );
//                        ToList.add(FromHour +1 + ":" + "00");
//                        FromHour++;
//                    }
//
//                }else{
//                    for(int j=0; j<= 2*(18-FromHour); j=j+2)
//                    {
//                        ToList.add(FromHour + 1 + ":" + "00");
//                        ToList.add(FromHour + ":" + 30);
//                        FromHour++;
//                    }
//                }
//
//                ArrayAdapter<String> adapterTo = new ArrayAdapter<>(EditActivity.this,
//                        android.R.layout.simple_spinner_dropdown_item, ToList);
//                To.setAdapter(adapterTo);
//                To.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//
//                        String[] hourMinFrom = first.split(":");
//                        second = To.getSelectedItem().toString();
//
//                        String[] hourMinTo = second.split(":");
//                        int hourResult=0;
//                        int  minResult=0;
//
//
//                        if( Integer.valueOf(hourMinTo[1]) < Integer.valueOf(hourMinFrom[1])){
//                            hourResult = Integer.valueOf(hourMinTo[0]) - Integer.valueOf(hourMinFrom[0]) - 1;
//                            minResult = 30;
//
//                        } else {
//                            hourResult = Integer.valueOf(hourMinTo[0]) - Integer.valueOf(hourMinFrom[0]);
//                            minResult = Integer.valueOf(hourMinTo[1]) - Integer.valueOf(hourMinFrom[1]);
//
//                        }
//                        int plus =0;
//                        int minfin=0;
//                        if(total%10 ==3){
//                            TotalOre.setText("No!");
//                        }
//                        if(minResult ==30 && (total-total%10)==0.5){
//                            plus =1;
//                            minfin=0;
//
//                        }else if((minResult==30 && (total-total%10)==0) ||(minResult==0 && (total-total%10)==0.5) )
//                        {
//                            minfin=30;
//                        }else if((minResult==0 && (total-total%10)==0)) {
//                            minfin=0;
//                        }
//
//                        if( (((hourResult + (total%10)) >3) ||((((hourResult + (total%10)+ plus) ==3) && (minfin!=0)))))
//                        {
//                            TotalOre.setText("Select again!");
//                            Confirm.setEnabled(false);
//                        } else
//                        {
//                            TotalOre.setText(hourResult + " hours and " + minResult + " minutes");
//                            Confirm.setEnabled(true);
//                            //final int minutes = minResult;
//                            minnn=minResult;
//                            ora = hourResult;
//
//                        }
//
//
//                    }
//                    public void onNothingSelected(AdapterView<?> parent) {
//                    }
//
//                });
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });

//        Confirm.setOnClickListener(new View.OnClickListener() {
//
//            DateFormat df = new SimpleDateFormat("HH:mm:ss");
//            String time = df.format(Calendar.getInstance().getTime());
//
//            @Override
//            public void onClick(View v) {
//                query.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        if(dataSnapshot.exists())
//
//                        {
//
//                            String nume =dataSnapshot.child("fullName").getValue(String.class);
//                            Log.d("data", "exists");
//
//                            Float total ;
//                            if(minnn == 30){
//                                total = Float.valueOf(String.valueOf(ora+ ".5") );
//
//
//                            }
//                            else
//                            {
//                                total = Float.valueOf(String.valueOf(ora) );
//                            }
//
//
//                            LP lp = new LP(nume,From.getSelectedItem().toString() ,To.getSelectedItem().toString(),total,status);
//                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("LP").child(date.getText().toString()).child(time).setValue(lp);
//
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
//                Intent intent = new Intent(EditActivity.this, LeavingPermissionList.class);
//                intent.putExtra("day", day);
//                intent.putExtra("month", month);
//                intent.putExtra("year", year);
//                startActivity(intent);
//            }
//        });

//        Beckraport.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(EditActivity.this, LeavingPermissionList.class);
//                intent.putExtra("day", day);
//                intent.putExtra("month", month);
//                intent.putExtra("year", year);
//                startActivity(intent);
//            }
//        });

    }

}
