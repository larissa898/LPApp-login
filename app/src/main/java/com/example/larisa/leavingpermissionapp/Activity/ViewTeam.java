package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Intent;
import android.hardware.usb.UsbRequest;
import android.provider.ContactsContract;
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
import com.example.larisa.leavingpermissionapp.Model.LP;
import com.example.larisa.leavingpermissionapp.R;
import com.example.larisa.leavingpermissionapp.Model.User;
import com.example.larisa.leavingpermissionapp.TestCalendar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
                final HashMap<String,LP> Userlp = new HashMap<>();

                DatabaseReference dbReference;

                for (final User u : recycleViewAdapter.checkedUsers) {

                    dbReference = FirebaseDatabase.getInstance().getReference("Users");
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
                                                Userlp.put(u.getFullName()+ " "+ snapshot1,lp);





                                            }


                                        }
                                    }

                                }



                            }
                            //needs modifying

                          Log.d("List has", String.valueOf(Userlp.size()));
                            intent.putExtra("Lps",Userlp);
                            startActivity(intent);


                        }


                        @Override

                        public void onCancelled(@NonNull DatabaseError databaseError) {


                        }

                    });


                }





            }



        });



    }
}