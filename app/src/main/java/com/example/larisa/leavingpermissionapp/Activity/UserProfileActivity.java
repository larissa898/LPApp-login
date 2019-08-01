/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.larisa.leavingpermissionapp.View.SignatureCanvasView;

public class UserProfileActivity extends AppCompatActivity {

    SignatureCanvasView signatureCanvasView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signatureCanvasView = new SignatureCanvasView(this);
        setContentView(signatureCanvasView);
    }
}
