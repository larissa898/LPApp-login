/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.example.larisa.leavingpermissionapp.Model.LeavingPermission;
import com.example.larisa.leavingpermissionapp.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CurrentUserManager {
    private static final String TAG = "CurrentUserManager";
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

    public void retrieveCurrentUserLeavingPermissionList(String id) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(id).child("LeavingPermission");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<LeavingPermission> leavingPermissionList = new ArrayList<>();
                if (dataSnapshot.exists()) {

                    for (DataSnapshot lpDates : dataSnapshot.getChildren()) {
                        for (DataSnapshot lpIDs : lpDates.getChildren()) {
                            LeavingPermission lp = lpIDs.getValue(LeavingPermission.class);
                            lp.setData(lpDates.getKey());
                            leavingPermissionList.add(lp);
                        }
                    }
                    listener.onLeavingPermissionListRetrieved(leavingPermissionList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public interface CurrentUserManagerListener {

        void onCurrentUserRetrieved(User user);

        void onLeavingPermissionListRetrieved(List<LeavingPermission> leavingPermissionList);

    }

}
