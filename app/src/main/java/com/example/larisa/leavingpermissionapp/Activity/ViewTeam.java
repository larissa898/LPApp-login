package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Intent;
import android.renderscript.Sampler;
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
import android.widget.Toast;

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
    private List<User> selectedUsers;


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
        selectedUsers = new ArrayList<>();


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
//        selectedUsers = recycleViewAdapter.checkedUsers;
//        if(selectedUsers.size() == 0)
//        {
//            confirmButton.setEnabled(false);
//        }
//        else
//        {
//            confirmButton.setEnabled(true);
//        }



        confirmButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                final Intent intent = new Intent(ViewTeam.this, FinalCalendar.class);
                final List<LP> LPlist = new ArrayList<>();

                final DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users");
                final int[] i = {0};
                i[0] = 0;






                    intent.putExtra("Lps", (Serializable) selectedUsers);
                    startActivity(intent);






            }
        });
    }
}