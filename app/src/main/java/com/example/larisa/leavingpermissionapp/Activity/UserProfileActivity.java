/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.larisa.leavingpermissionapp.Model.User;
import com.example.larisa.leavingpermissionapp.R;
import com.example.larisa.leavingpermissionapp.View.SignatureCanvasView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

public class UserProfileActivity extends AppCompatActivity {

    private static final String TAG = "UserProfileActivity";
    private final int SIGNATURE_REQUEST_CODE = 100;

    // UI
    private TextView userFullNameTV;
    private TextView userFunctieTV;
    private TextView userPhoneTV;
    private TextView userNrMatricolTV;
    private ImageView userSignatureIV;
    private Button addEditSignatureButton;
    private SignatureCanvasView signatureCanvasView;

    // Firebase
    private DatabaseReference usersRef;
    private StorageReference signatureRef;

    // Vars
    private String userId;
    private boolean isSignatureSet;
    private String signaturePath = "";



    private void checkSignatureExists() {

        getSupportActionBar().setTitle("Leaving Permission App");
        signatureRef.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(userSignatureIV);
                        addEditSignatureButton.setText("Edit Signature");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                    }
                });

    }

    private void initUI() {
        userFullNameTV = findViewById(R.id.userFullNameTV);
        userFunctieTV = findViewById(R.id.userFunctieTV);
        userPhoneTV = findViewById(R.id.userPhoneTV);
        userNrMatricolTV = findViewById(R.id.userNrMatricolTV);
        userSignatureIV = findViewById(R.id.userSignatureIV);
        addEditSignatureButton = findViewById(R.id.addEditSignatureButton);

        addEditSignatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, SignatureActivity.class);
                startActivityForResult(intent, SIGNATURE_REQUEST_CODE);
            }
        });




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

        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        signatureRef = FirebaseStorage.getInstance().getReference().child("signatures").child(userId);

        checkSignatureExists();
        initUI();
        getUserData();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SIGNATURE_REQUEST_CODE) {

            if(resultCode == Activity.RESULT_OK){
                signaturePath = data.getStringExtra("path");
                Toast.makeText(this, "signaturePath = " + signaturePath, Toast.LENGTH_SHORT).show();

                saveToFirebase(signaturePath);


            }


        }

    }

    private void saveToFirebase(String signaturePath) {

        Uri file = Uri.fromFile(new File(signaturePath));

        UploadTask uploadTask = signatureRef.putFile(file);
        Task<Uri> urlTask = uploadTask
                .continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()) {
                            throw task.getException();
                        }

                        return signatureRef.getDownloadUrl();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();
                            Log.d(TAG, "onComplete: URI: " + downloadUri.toString());
                            new File(signaturePath).delete();

                            Picasso.get().load(downloadUri).into(userSignatureIV);

                        }
                    }
                });


    }
}
