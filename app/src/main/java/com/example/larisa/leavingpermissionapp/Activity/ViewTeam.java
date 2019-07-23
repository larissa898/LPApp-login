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
import com.example.larisa.leavingpermissionapp.Model.LP;
import com.example.larisa.leavingpermissionapp.R;
import com.example.larisa.leavingpermissionapp.Model.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ViewTeam extends AppCompatActivity implements Serializable {

    private RecyclerView recyclerView;
    private RecycleViewAdapter recycleViewAdapter;
    private List<User> usersList;
    private Button confirmButton;
    private TextView welcomText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_team);


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
                usersList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        User user = snapshot.getValue(User.class);
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


                final Intent intent = new Intent(ViewTeam.this, FinalCalendar.class);
                final List<LP> LPlist = new ArrayList<>();

                DatabaseReference dbReference =  FirebaseDatabase.getInstance().getReference("Users");
                final int[] i = {0};

                for (final User u : recycleViewAdapter.checkedUsers) {


                    dbReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    if (snapshot.child("fullName").getValue().equals(u.getFullName())) {
                                        for (DataSnapshot snapshot1 : snapshot.child("LP").getChildren()) {
                                            for (DataSnapshot snapshot2 : snapshot1.getChildren()) {
                                                // Log.d("Date is", String.valueOf(snapshot1.getKey()));
                                                LP lp = snapshot2.getValue(LP.class);
                                                //Log.d("user has been absent since", lp.getFrom());
                                                String date = snapshot1.getKey();
                                                lp.setData(date);
                                                LPlist.add(lp);


                                            }


                                        }
                                    }

                                }


                            }
                            //needs modifying

                            i[0]++;
                            if(i[0] == recycleViewAdapter.checkedUsers.size())
                            {
                                intent.putExtra("Lps", (Serializable) LPlist);
                                startActivity(intent);
                            }


                        }


                        @Override

                        public void onCancelled(@NonNull DatabaseError databaseError) {


                        }

                    });



                }



                Log.d("AAbCDS", String.valueOf(LPlist.size()));
                dbReference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                     LPlist.clear();
                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



            }


        });


    }

}