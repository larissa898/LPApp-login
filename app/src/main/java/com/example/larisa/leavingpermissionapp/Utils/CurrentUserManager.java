/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Utils;

import android.support.annotation.NonNull;

import com.example.larisa.leavingpermissionapp.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CurrentUserManager {

    public static User currentUser = null;
    public FirebaseAuth firebaseAuth;
    private CurrentUserManagerListener listener;

    public CurrentUserManager(CurrentUserManagerListener currentUserManagerListener) {
        firebaseAuth = FirebaseAuth.getInstance();
        listener = currentUserManagerListener;
    }

    public void retrieveCurrentUserObj(String id) {


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(id);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    currentUser = dataSnapshot.getValue(User.class);
                    listener.onCurrentUserRetrieved(currentUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public interface CurrentUserManagerListener {

        void onCurrentUserRetrieved(User user);

    }

}
