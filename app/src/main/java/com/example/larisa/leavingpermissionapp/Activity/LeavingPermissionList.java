package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.R;

public class LeavingPermissionList extends AppCompatActivity {

    private Button CancelList;
    private Button AddButton;
    private TextView CurrentDay;
    private int  day;
    private int month;
    private int year;
    private int actualDay;
    private int actualMonth;
    private int actualYear;

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

        CurrentDay = findViewById(R.id.textViewDayCurrent);
        AddButton = findViewById(R.id.buttonAddList);
        CancelList = findViewById(R.id.buttonCancelList);

        day =  getIntent().getIntExtra("day",0);
        actualDay =  getIntent().getIntExtra("actualDay",0);
        actualMonth =  getIntent().getIntExtra("actualMonth",0);
        actualYear =  getIntent().getIntExtra("actualYear",0);
        month = getIntent().getIntExtra("month",0);
        year = getIntent().getIntExtra("year", 0);

        CurrentDay.setText(day + " "+ strMonths[month] + " " + year);

        if(day < actualDay ||  month < actualMonth || year < actualYear){
            AddButton.setEnabled(false);

        }else{
            AddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LeavingPermissionList.this, RaportActivity.class);
                    intent.putExtra("day", day);
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
