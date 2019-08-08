/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Utils;

import com.example.larisa.leavingpermissionapp.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class FirebaseOps {
    private static final String TAG = "FirebaseOps";
    private static FirebaseOps instance = null;

    private String userId;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private StorageReference signatureRef;

    private List<User> users;

    public static FirebaseOps getInstance() {
        if (instance == null) {
            instance = new FirebaseOps();
        }
        return instance;
    }


    private FirebaseOps() {
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        readData();
    }


    private void readData() {
        users = new ArrayList<>();
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        users.add(issue.getValue(User.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }


    public List<User> getUsersByRole(String role) {

        List<User> usersByRole = users.stream()
                .filter(user -> user.getFunctie().equals(role))
                .collect(Collectors.toList());
        return usersByRole;
    }
}
