package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Intent;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.Adapters.RecycleViewAdapter;
import com.example.larisa.leavingpermissionapp.Database.Database;
import com.example.larisa.leavingpermissionapp.MainActivity;
import com.example.larisa.leavingpermissionapp.R;
import com.example.larisa.leavingpermissionapp.Model.User;
import com.example.larisa.leavingpermissionapp.TestCalendar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewTeam extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecycleViewAdapter recycleViewAdapter;
    private List<User> usersList;
    private List <User> users;
    private Database db;
    private Button confirmButton;
    private TextView welcomText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_team);
        db = new Database(this);

        recyclerView = findViewById(R.id.recycleViewActivity);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        confirmButton = findViewById(R.id.confirmButton);
        welcomText = findViewById(R.id.welcomeText);

        usersList = new ArrayList<>();

        DatabaseReference dbReference;
        dbReference = FirebaseDatabase.getInstance().getReference("Users");
        dbReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())

                {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        User user  = snapshot.getValue(User.class);
                        usersList.add(user);
                    }




                    recycleViewAdapter = new RecycleViewAdapter(ViewTeam.this, usersList);
                    recyclerView.setAdapter(recycleViewAdapter);
                    recycleViewAdapter.notifyDataSetChanged();

                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent = new Intent(ViewTeam.this, TestCalendar.class);
             startActivity(intent);


            }
        });





        /*usersList = db.getAllItems();*/







    }



}