/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class UserProfileAdminViewActivity extends AppCompatActivity {

    private static final String TAG = "UserProfileActivity";
    private final int SIGNATURE_REQUEST_CODE = 100;

    // UI
    private EditText userFullNameTV;
    private EditText userFunctieTV;
    private EditText userPhoneTV;
    private EditText userNrMatricolTV;
    private EditText userTeamLeaderTV;
    private ImageView userSignatureIV;
    private Button addEditSignatureButton;
    private SignatureCanvasView signatureCanvasView;
    private Button editSaveUserButton;

    // Firebase
    private DatabaseReference usersRef;
    private StorageReference signatureRef;

    // Vars
    private String userId;
    private boolean isSignatureSet;
    private String signaturePath = "";

    private boolean inEditMode = false;


    private void checkSignatureExists() {

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
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
        userFullNameTV = findViewById(R.id.adminUserFullNameTV);
        userFunctieTV = findViewById(R.id.adminUserFunctieTV);
        userPhoneTV = findViewById(R.id.adminUserPhoneTV);
        userNrMatricolTV = findViewById(R.id.adminUserNrMatricolTV);
        userTeamLeaderTV = findViewById(R.id.admin_team_leader_user_profile_TV);
        editSaveUserButton = findViewById(R.id.adminEditUser);

        disableEditing(userFullNameTV);
        disableEditing(userFunctieTV);
        disableEditing(userPhoneTV);
        disableEditing(userNrMatricolTV);
        disableEditing(userTeamLeaderTV);
        setOnEditorActionListenerForEditText(userFullNameTV);
        setOnEditorActionListenerForEditText(userFunctieTV);
        setOnEditorActionListenerForEditText(userPhoneTV);
        setOnEditorActionListenerForEditText(userNrMatricolTV);
        setOnEditorActionListenerForEditText(userTeamLeaderTV);


        editSaveUserButton.setOnClickListener(view -> {
            if (!inEditMode) {
                enableEditing(userFullNameTV);
                enableEditing(userFunctieTV);
                enableEditing(userPhoneTV);
                enableEditing(userNrMatricolTV);
                enableEditing(userTeamLeaderTV);
                inEditMode = true;
                editSaveUserButton.setText("Save");
                userNrMatricolTV.requestFocus();
            } else {
                disableEditing(userFullNameTV);
                disableEditing(userFunctieTV);
                disableEditing(userPhoneTV);
                disableEditing(userNrMatricolTV);
                disableEditing(userTeamLeaderTV);
                inEditMode = false;
                changeValue("fullName", userFullNameTV.getText().toString());
                changeValue("functie", userFunctieTV.getText().toString());
                changeValue("telefon", userPhoneTV.getText().toString());
                changeValue("nrMatricol", userNrMatricolTV.getText().toString());
                //                    changeValue("fullName",userTeamLeaderTV.getText().toString());
                editSaveUserButton.setText("Edit User");


            }
        });
    }

    public void setOnEditorActionListenerForEditText(EditText editText) {
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event.getAction() == KeyEvent.ACTION_DOWN
                    || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard();
            }
            return false;
        });

    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void enableEditing(EditText editText) {
        editText.setKeyListener((KeyListener) editText.getTag());
    }

    private void disableEditing(EditText editText) {
        editText.setTag(editText.getKeyListener());
        editText.setKeyListener(null);
    }

    private void changeValue(String field, String value) {
        usersRef.child(userId).child(field).setValue(value);
    }

    private void getUserData() {

        Query query = usersRef.child(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    User user = dataSnapshot.getValue(User.class);
                    Log.d(TAG, "onDataChange: user: " + user);

                    userFullNameTV.setText(user.getLastName() + " " + user.getFirstName());
                    userFunctieTV.setText(user.getRole());
                    userNrMatricolTV.setText(user.getRegistrationNumber());
                    userPhoneTV.setText(user.getPhoneNumber());
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
        setContentView(R.layout.activity_user_profile_admin_view);

        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        signatureRef = FirebaseStorage.getInstance().getReference().child("signatures").child(userId);

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
        if (requestCode == SIGNATURE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
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
                        if (!task.isSuccessful()) {
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
