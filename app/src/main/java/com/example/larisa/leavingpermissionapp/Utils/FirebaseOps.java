/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Utils;

import android.support.annotation.NonNull;

import com.example.larisa.leavingpermissionapp.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;
    private StorageReference signatureRef;

    private List<User> usersList;
    private User user;

    public static FirebaseOps getInstance() {
        if (instance == null) {
            instance = new FirebaseOps();
        }
        return instance;
    }

    private FirebaseOps() {
        mAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        readData();
    }



    private void readData() {
        usersList = new ArrayList<>();
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usersList.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        usersList.add(issue.getValue(User.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        trackCurrentUser();

    }

    public String getCurrentUserUid() {
        return mAuth.getCurrentUser().getUid();
    }

    public DatabaseReference getCurrentUserRef() {
        return getUsersRef().child(getCurrentUserUid());
    }

    public FirebaseUser getCurrentFirebaseUser() {
        return mAuth.getCurrentUser();
    }


    public List<User> getUsersByRole(String role) {

        List<User> usersByRole = usersList.stream()
                .filter(user -> user.getFunctie().equals(role))
                .collect(Collectors.toList());
        return usersByRole;
    }


    private void trackCurrentUser() {

        // if getCurrentFirebaseUser() == null, then we are in the login / register page
        if (getCurrentFirebaseUser() != null) {
            String currentUserUid = getCurrentFirebaseUser().getUid();
            DatabaseReference currentUserRef = usersRef.child(currentUserUid);

            currentUserRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        user = dataSnapshot.getValue(User.class);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    /**
     * The current User model corresponding to the logged in {@link FirebaseUser}
     * @return a User object, or null if no {@link FirebaseUser} is currently logged in
     */
    public User getCurrentUser() {
        return user;

    }

    public DatabaseReference getUsersRef() {
        return usersRef;
    }














}
