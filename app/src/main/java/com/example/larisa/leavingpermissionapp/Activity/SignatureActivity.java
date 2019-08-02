/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.larisa.leavingpermissionapp.R;
import com.example.larisa.leavingpermissionapp.View.SignatureCanvasView;

public class SignatureActivity extends AppCompatActivity {

    private SignatureCanvasView signatureCanvasView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_canvas);
        signatureCanvasView = findViewById(R.id.signatureCanvas);
    }

    public void clear(View view){
        signatureCanvasView.clear();
    }
}
