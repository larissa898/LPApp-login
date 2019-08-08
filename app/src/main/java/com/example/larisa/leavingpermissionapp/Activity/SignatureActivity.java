/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.larisa.leavingpermissionapp.R;
import com.example.larisa.leavingpermissionapp.Utils.ExternalStoragePermission;
import com.example.larisa.leavingpermissionapp.View.SignatureCanvasView;

public class SignatureActivity extends AppCompatActivity {


    // UI
    private SignatureCanvasView signatureCanvasView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_canvas);

        signatureCanvasView = findViewById(R.id.signatureCanvas);
    }

    public void clear(View view) {
        signatureCanvasView.clear();
    }

    public void save(View view) {
        if (ExternalStoragePermission.verifyStoragePermissions(this)) {
            signatureCanvasView.saveLocal(this);
        }
    }

    public void onSuccesfulImageSave(String path) {

        Intent returnIntent = new Intent();
        returnIntent.putExtra("path", path);
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            signatureCanvasView.saveLocal(this);
        }
    }

}
