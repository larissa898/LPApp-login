package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.Adapters.RecycleViewAdapterLP;
import com.example.larisa.leavingpermissionapp.Model.LP;
import com.example.larisa.leavingpermissionapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class LPCalendarList extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private RecycleViewAdapterLP recycleViewAdapterLP;
    private List<LP> lpList;
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

        lpList = new ArrayList<>();
        Intent intent = getIntent();
        List<LP> lpList= (List<LP>) intent.getSerializableExtra("TodayLP");

        if(lpList != null)
        {
            listDate.setText(lpList.get(0).getData());
        }

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
            case R.id.finishedReviewing:
//
                DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users");
                for ( final Integer key : recycleViewAdapterLP.modifiedLP.keySet()) {

                  final  LP lp = recycleViewAdapterLP.modifiedLP.get(key);
                  dbReference.addValueEventListener(new ValueEventListener() {
                       @Override
                       public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                           if (dataSnapshot.exists()) {
                               for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                      if (snapshot.child("fullName").getValue(String.class).equals(lp.getNume())) {
                                        for (DataSnapshot snapshot1 : snapshot.child("LP").getChildren()) {
                                          int i = 0;
                                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                if (i == key && snapshot1.getKey().equals(lp.getData())) {
                                                    if(lp.getStatus().equals("confirmat"))
                                                    {
                                                        snapshot2.child("status").getRef().setValue("confirmat");
                                                    }
                                                    else
                                                        snapshot2.child("status").getRef().setValue("refuzat");

                                                   break;
                                              }
                                                else
                                                    {

                                                    i++;
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
