/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.larisa.leavingpermissionapp.Fragments.RegisterCredentialsFragment;
import com.example.larisa.leavingpermissionapp.Fragments.RegisterCredentialsFragmentListener;
import com.example.larisa.leavingpermissionapp.Fragments.RegisterDetailsFragment;
import com.example.larisa.leavingpermissionapp.Fragments.RegisterDetailsFragmentListener;
import com.example.larisa.leavingpermissionapp.R;
import com.example.larisa.leavingpermissionapp.Utils.FirebaseOps;

public class RegisterActivity extends AppCompatActivity
        implements RegisterCredentialsFragmentListener,
        RegisterDetailsFragmentListener {

    private static final String TAG = "RegisterActivity";
    public static final String SHARED_PREFERENCES = "sharedPrefs";
    public static final String LAST_NAME = "lastName";
    public static final String FIRST_NAME = "firstName";
    public static final String REGISTRATION_NO = "registrationNumber";
    public static final String PHONE_NO = "phoneNumber";
    public static final String ROLE = "role";
    
    private RegisterCredentialsFragment credentialsFragment;
    private RegisterDetailsFragment detailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        credentialsFragment = new RegisterCredentialsFragment();
        detailsFragment = new RegisterDetailsFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.registrationPage, credentialsFragment)
                .commit();
    }

    @Override
    public void onCredentialsSent(CharSequence email, CharSequence password) {

        FirebaseOps.getInstance().createUser(email.toString(), password.toString());
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.registrationPage, detailsFragment)
                .commit();
    }

    @Override
    public void onDetailsSent(CharSequence lastName, CharSequence firstName, CharSequence registrationNumber, CharSequence phoneNumber, CharSequence role) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(LAST_NAME, lastName.toString());
        editor.putString(FIRST_NAME, firstName.toString());
        editor.putString(REGISTRATION_NO, registrationNumber.toString());
        editor.putString(PHONE_NO, phoneNumber.toString());
        editor.putString(ROLE, role.toString());
        editor.apply();

        setResult(RESULT_OK);
        finish();
    }
}
