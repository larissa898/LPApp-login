package com.example.larisa.leavingpermissionapp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.larisa.leavingpermissionapp.Adapters.RecycleViewAdapter;
import com.example.larisa.leavingpermissionapp.Database.Database;
import com.example.larisa.leavingpermissionapp.R;
import com.example.larisa.leavingpermissionapp.User;

import java.util.ArrayList;
import java.util.List;

public class ViewTeam extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecycleViewAdapter recycleViewAdapter;
    private List<User> usersList;
    private List <User> users;
    private Database db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_team);
        db = new Database(this);
        recyclerView = findViewById(R.id.recycleViewActivity);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        usersList = new ArrayList<>();
        users = new ArrayList<>();
        usersList = db.getAllItems();

        for(User u :usersList)
        {
            User user = new User();
            user.setUserNume(u.getUserNume());
            user.setÚserPrenume(u.getÚserPrenume());
            users.add(user);
        }

        recycleViewAdapter = new RecycleViewAdapter(this, users);
        recyclerView.setAdapter(recycleViewAdapter);
        recycleViewAdapter.notifyDataSetChanged();




    }
}