package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;



import com.example.larisa.leavingpermissionapp.R;


public class RaportActivity extends AppCompatActivity {

    private Button Confirm;
    private Spinner From;
    private Spinner To;
    private TextView date;
    private TextView Nume;
    private TextView Prenume;
    private TextView NrMat;
    private Button CancelRaport;
    private TextView TotalOre;
    private int  day;
    private int month;
    private int year;
    String first="";
    String second="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raport);

        final String[] items = new String[]{"7:30", "8:00", "8:30", "9:00", "9:30",
                "10:00", "10:30", "11:00", "11:30","12:00", "12:30", "14:00",
                "14:30","15:00", "15:30", "16:00", "16:30","17:00", "17:30",
                "18:00", "18:30"};

        Confirm = findViewById(R.id.ConfirmButtonRaport);
        From = findViewById(R.id.spinnerFrom);
        To = findViewById(R.id.spinnerTo);
        TotalOre = findViewById(R.id.TotalTextView);
        date = findViewById(R.id.editTextDate);
        Nume = findViewById(R.id.textViewNume);
        Prenume = findViewById(R.id.textViewPrenume);
        NrMat = findViewById(R.id.textViewNrMat);
        CancelRaport = findViewById(R.id.buttonCancelRaport);

        day =  getIntent().getIntExtra("day",0);
        month = getIntent().getIntExtra("month",0);
        year = getIntent().getIntExtra("year", 0);



       Log.d("Date is", String.valueOf(day) + String.valueOf(month) + String.valueOf(year)) ;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);//set the spinners adapter to the previously created one.

        From.setAdapter(adapter);
        To.setAdapter(adapter);


        From.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final int fromPos = position;
                //final Object itemFrom = parent.getItemAtPosition(position);

                first = From.getSelectedItem().toString();
                To.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                       // for(int i=0; i<fromPos; i++){
                           // To[i].setEnabled(false);
                        //}

//                        To.setOnTouchListener(new View.OnTouchListener() {
//                            @Override
//                            public boolean onTouch(View v, MotionEvent event) {
//                                return false;
//                            }
//                        });
                        //Object itemTo = parent.getItemAtPosition(position);
                        second = To.getSelectedItem().toString();

                        String[] hourMinFrom = first.split(":");
                        String[] hourMinTo = second.split(":");

                        int hourResult=0;
                        int minResult=0;

                        if( Integer.valueOf(hourMinTo[1]) < Integer.valueOf(hourMinFrom[1])){
                             hourResult = Integer.valueOf(hourMinTo[0]) - Integer.valueOf(hourMinFrom[0]) - 1;
                             minResult = 30;
                           // Log.e("LARISSA", hourResult + ":" + minResult);
                        } else {
                             hourResult = Integer.valueOf(hourMinTo[0]) - Integer.valueOf(hourMinFrom[0]);
                             minResult = Integer.valueOf(hourMinTo[1]) - Integer.valueOf(hourMinFrom[1]);
                            //Log.e("LARISSA", hourResult + ":" + minResult);
                        }
                         if( hourResult > 3 ||( hourResult == 3 && minResult == 30) || (hourResult == 0 && minResult == 0) || (Integer.valueOf(hourMinFrom[0]) > Integer.valueOf(hourMinTo[0]))){

                             TotalOre.setText("Max 3 hours. Select again!");
                             Confirm.setEnabled(false);
                         } else {
                            TotalOre.setText(hourResult + " hours and " + minResult + " minutes");
                             Confirm.setEnabled(true);
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
                Intent intent = new Intent(RaportActivity.this, LeavingPermissionList.class);
                startActivity(intent);
            }
        });

        CancelRaport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}