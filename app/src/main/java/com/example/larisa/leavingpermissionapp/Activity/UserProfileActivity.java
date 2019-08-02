/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.larisa.leavingpermissionapp.Model.User;
import com.example.larisa.leavingpermissionapp.R;
import com.example.larisa.leavingpermissionapp.View.SignatureCanvasView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = "UserProfileActivity";

    // UI
    private TextView userFullNameTV;
    private TextView userFunctieTV;
    private TextView userPhoneTV;
    private TextView userNrMatricolTV;
    private ImageView userSignatureIV;
    SignatureCanvasView signatureCanvasView;

    // Firebase
    private DatabaseReference usersRef;

    // Vars
    String userId;


    private void initUI() {
        userFullNameTV = findViewById(R.id.userFullNameTV);
        userFunctieTV = findViewById(R.id.userFunctieTV);
        userPhoneTV = findViewById(R.id.userPhoneTV);
        userNrMatricolTV = findViewById(R.id.userNrMatricolTV);
        userSignatureIV = findViewById(R.id.userSignatureIV);

        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

    }

    private void getUserData() {
        Query query = usersRef.child(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    Log.d(TAG, "onDataChange: user: " + user);

                    userFullNameTV.setText(user.getFullName());
                    userFunctieTV.setText(user.getFunctie());
                    userNrMatricolTV.setText(user.getNrMatricol());
                    userPhoneTV.setText(user.getTelefon());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "onCancelled: " + databaseError.getMessage());
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        initUI();
        getUserData();

    }




}
