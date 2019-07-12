package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.example.larisa.leavingpermissionapp.Adapters.RecycleViewAdapterLP;
import com.example.larisa.leavingpermissionapp.Model.LP;
import com.example.larisa.leavingpermissionapp.R;

import java.util.ArrayList;
import java.util.List;

public class LPCalendarList extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private RecycleViewAdapterLP recycleViewAdapterLP;
    private List<LP> lpList;
    private Button backToCalendar;
    private Button doneConfirming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lpcalendar_list);


        recyclerView = findViewById(R.id.recycleViewLP);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        lpList = new ArrayList<>();
        Intent intent = getIntent();
        List<LP> lpList= (List<LP>) intent.getSerializableExtra("TodayLP");


        recycleViewAdapterLP = new RecycleViewAdapterLP(LPCalendarList.this, lpList);

        recyclerView.setAdapter(recycleViewAdapterLP);
        recycleViewAdapterLP.notifyDataSetChanged();

        backToCalendar = findViewById(R.id.backToCalendar);
        doneConfirming =findViewById(R.id.finishedReviewing);

        backToCalendar.setOnClickListener(this);
        doneConfirming.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.backToCalendar:
                finish();
                break;
            case R.id.finishedReviewing:
                finish();
                break;


        }
}
}
