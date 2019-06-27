package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

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

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class RaportActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Button Confirm;
    private Spinner From;
    private Spinner To;
    private TextView date;
    private TextView Nume;
    private TextView Prenume;
    private Button Beckraport;
    public int minnn;
    public int ora;
    private TextView TotalOre;
    private int  day;
    private int month;
    private int year;
    String first="";
    String second="";


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


        Confirm = findViewById(R.id.ConfirmButtonRaport);
        From = findViewById(R.id.spinnerFrom);
        To = findViewById(R.id.spinnerTo);
        TotalOre = findViewById(R.id.TotalTextView);
        date = findViewById(R.id.editTextDate);
        Nume = findViewById(R.id.textViewNume);
        Prenume = findViewById(R.id.textViewPrenume);
        Beckraport = findViewById(R.id.buttonBackRaport);
        day =  getIntent().getIntExtra("day",0);
        month = getIntent().getIntExtra("month",0);
        year = getIntent().getIntExtra("year", 0);

        date.setText(day + " "+ strMonths[month] + " " + year );

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference functionRef =  FirebaseDatabase.getInstance().getReference("Users");

        final Query query =  functionRef.child(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    String prenume = dataSnapshot.child("prenume").getValue(String.class);
                    String nume = dataSnapshot.child("nume").getValue(String.class);
                    Prenume.setText(prenume);
                    Nume.setText(nume);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);//set the spinners adapter to the previously created one.

        From.setAdapter(adapter);

        Confirm.setEnabled(false);

        From.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                first = From.getSelectedItem().toString();
                

                String[] hourMinFrom = first.split(":");
                int FromMinutes  = Integer.valueOf(hourMinFrom[1]);
                int FromHour = Integer.valueOf(hourMinFrom[0]);
                List<String> ToList = new ArrayList<>();

                if(     ((FromHour ==  8 ) && (FromMinutes == 0)) || ((FromHour ==  9) &&  (FromMinutes == 0)) ||
                        ((FromHour ==  10) && (FromMinutes == 0)) || ((FromHour ==  11) && (FromMinutes == 0)) ||
                        ((FromHour ==  12)&&  (FromMinutes == 0)) || ((FromHour ==  13) && (FromMinutes == 0)) ||
                        ((FromHour ==  14) && (FromMinutes == 0)) || ((FromHour ==  15) && (FromMinutes == 0)) ||
                        ((FromHour ==  16) && (FromMinutes == 0)) || ((FromHour ==  17) && (FromMinutes == 0)) ||
                        ((FromHour ==  18)&& (FromMinutes == 0))  || ((FromHour ==  19)&& (FromMinutes == 0))) {

                    for(int j=0; j<= 2*(20-FromHour); j=j+2)
                    {
                        ToList.add(FromHour   +":"+ 30 );
                        ToList.add(FromHour +1 + ":" + "00");
                        FromHour++;
                    }

                }else{
                    for(int j=0; j<= 2*(18-FromHour); j=j+2)
                    {
                        ToList.add(FromHour + 1 + ":" + "00");
                        ToList.add(FromHour + ":" + 30);
                        FromHour++;
                    }
                }

                ArrayAdapter<String> adapterTo = new ArrayAdapter<>(RaportActivity.this, android.R.layout.simple_spinner_dropdown_item, ToList);
                To.setAdapter(adapterTo);
                To.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        String[] hourMinFrom = first.split(":");
                        second = To.getSelectedItem().toString();

                        String[] hourMinTo = second.split(":");
                        int hourResult=0;
                        int  minResult=0;


                        if( Integer.valueOf(hourMinTo[1]) < Integer.valueOf(hourMinFrom[1])){
                             hourResult = Integer.valueOf(hourMinTo[0]) - Integer.valueOf(hourMinFrom[0]) - 1;
                             minResult = 30;

                        } else {
                             hourResult = Integer.valueOf(hourMinTo[0]) - Integer.valueOf(hourMinFrom[0]);
                             minResult = Integer.valueOf(hourMinTo[1]) - Integer.valueOf(hourMinFrom[1]);

                        }
                         if( hourResult > 3 ||( hourResult == 3 && minResult == 30) || (first == "7:30" && second == "7:30") || (Integer.valueOf(hourMinFrom[0]) > Integer.valueOf(hourMinTo[0])
                                 || ((Integer.valueOf(hourMinFrom[0]) == Integer.valueOf(hourMinTo[0])) && (Integer.valueOf(hourMinFrom[1]) >= Integer.valueOf(hourMinTo[1]) )))){

                             TotalOre.setText("Select again!");
                             Confirm.setEnabled(false);
                         } else {
                            TotalOre.setText(hourResult + " hours and " + minResult + " minutes");
                             Confirm.setEnabled(true);
                             //final int minutes = minResult;
                             minnn=minResult;
                             ora = hourResult;

                         }

                    }
                    public void onNothingSelected(AdapterView<?> parent) {
                    }

                });

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Confirm.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())

                        {
                            Log.d("data", "exists");

                            Float total ;
                            if(minnn == 30){
                                total = Float.valueOf(String.valueOf(ora+ ".5") );


                            }
                            else
                            {
                                total = Float.valueOf(String.valueOf(ora) );
                            }


                            LP lp = new LP(date.getText().toString(),From.getSelectedItem().toString(),To.getSelectedItem().toString(),total );
                           // FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("LP");
                            //FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("LP").setValue(date.getText().toString());
                            FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("LP").child(date.getText().toString()).setValue(lp);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Intent intent = new Intent(RaportActivity.this, LeavingPermissionList.class);
                startActivity(intent);
            }
        });

        Beckraport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RaportActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

    }
}