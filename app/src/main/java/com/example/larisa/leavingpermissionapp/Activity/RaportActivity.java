package com.example.larisa.leavingpermissionapp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.R;

public class RaportActivity extends AppCompatActivity {

    private TextView date;
    private TextView Nume;
    private TextView Prenume;
    private TextView NrMat;
    private int  day;
    private int month;
    private int year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raport);
        date = findViewById(R.id.editTextDate);
        Nume = findViewById(R.id.textViewNume);
        Prenume = findViewById(R.id.textViewPrenume);
        NrMat = findViewById(R.id.textViewNrMat);

        day =  getIntent().getIntExtra("day",0);
        month = getIntent().getIntExtra("month",0);
        year = getIntent().getIntExtra("year", 0);
        date.setText(day + " "+ month + " " + year );

    }
}