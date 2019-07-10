package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.larisa.leavingpermissionapp.Adapters.RecycleViewAdapter;
import com.example.larisa.leavingpermissionapp.Adapters.RecycleViewAdapterLP;
import com.example.larisa.leavingpermissionapp.Model.LP;
import com.example.larisa.leavingpermissionapp.Model.User;
import com.example.larisa.leavingpermissionapp.R;

import java.util.ArrayList;
import java.util.List;

public class LPCalendarList extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecycleViewAdapterLP recycleViewAdapterLP;
    private List<LP> lpList;

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
    }
}
