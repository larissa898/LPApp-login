/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.larisa.leavingpermissionapp.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
    private DatabaseReference rolesRef;

    private StorageReference signatureRef;

    private List<User> users;
    private User user;
    private List<String> roles;

    public static FirebaseOps getInstance() {
        if (instance == null) {
            instance = new FirebaseOps();
        }
        return instance;
    }

    private FirebaseOps() {
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        rolesRef = FirebaseDatabase.getInstance().getReference("Roles");
        mAuth = FirebaseAuth.getInstance();
        readUsers();
        readRoles();
        trackCurrentUser();
    }


    private void readUsers() {
        users = new ArrayList<>();
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                users.clear();
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

    private void readRoles() {
        roles = new ArrayList<>();
        rolesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        roles.add(issue.getValue(String.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



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

        List<User> usersByRole = users.stream()
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















    public List<String> getRoles(){
        return roles;
    }


    public void createUser(String registerEmail, String registerPassword){
        Log.d(TAG, "createUser: xxxxxx");
        mAuth.createUserWithEmailAndPassword(registerEmail, registerPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "onComplete: xxxxxx");
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            user.sendEmailVerification();
                            mAuth.signOut();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "onFailure: xxxxxx" + e.getLocalizedMessage());
            }
        });
    }
}
