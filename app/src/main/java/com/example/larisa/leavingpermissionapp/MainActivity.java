/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.larisa.leavingpermissionapp.Activity.UserCalendarActivity;
import com.example.larisa.leavingpermissionapp.Activity.RegisterActivity;
import com.example.larisa.leavingpermissionapp.Activity.TeamLeaderViewTeam;
import com.example.larisa.leavingpermissionapp.Model.User;
import com.example.larisa.leavingpermissionapp.Utils.CurrentUserManager;
import com.example.larisa.leavingpermissionapp.Utils.FirebaseOps;
import com.example.larisa.leavingpermissionapp.Utils.FirebaseOpsListener;
import com.example.larisa.leavingpermissionapp.Utils.Validator;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.larisa.leavingpermissionapp.Activity.RegisterActivity.FIRST_NAME;
import static com.example.larisa.leavingpermissionapp.Activity.RegisterActivity.LAST_NAME;
import static com.example.larisa.leavingpermissionapp.Activity.RegisterActivity.PHONE_NO;
import static com.example.larisa.leavingpermissionapp.Activity.RegisterActivity.REGISTRATION_NO;
import static com.example.larisa.leavingpermissionapp.Activity.RegisterActivity.ROLE;
import static com.example.larisa.leavingpermissionapp.Activity.RegisterActivity.SHARED_PREFERENCES;

public class MainActivity extends AppCompatActivity implements FirebaseOpsListener, CurrentUserManager.CurrentUserManagerListener {

    private final int REGISTER_REQUEST_CODE = 111;
    private static final String TAG = "MainActivity";
    private FirebaseOps firebaseOps;
    private CurrentUserManager currentUserManager;
    private Intent openActivityForRoleIntent = null;

    // UI
    private Button loginButton;
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView registerButton;
    private ProgressBar progressBar;

    // Firebase
    private FirebaseAuth firebaseAuth;
    private User user;

    private void initFirebase() {
        firebaseOps = FirebaseOps.getInstance();
        firebaseOps.setListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserManager = new CurrentUserManager(this);

    }

    private void initUI() {
        loginButton = findViewById(R.id.loginButton);
        emailEditText = findViewById(R.id.userNameET);
        passwordEditText = findViewById(R.id.passwordET);
        setOnEditorActionListenerForEditText(passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        progressBar = findViewById(R.id.loginProgressBar);
        progressBar.setVisibility(View.GONE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                if (Validator.validateNonEmptyField(emailEditText) && Validator.validateNonEmptyField(passwordEditText)) {
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    //check that the user has validated the email
                                    if (task.isSuccessful()) {
                                        FirebaseUser firebaseUser = firebaseOps.getCurrentFirebaseUser();
                                        if (firebaseUser.isEmailVerified()) {
                                            createUserObjectInDatabase(firebaseUser.getUid());

                                        } else {
                                            Toast.makeText(MainActivity.this, "Please verify your email first", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    emailEditText.setText("");
                                    passwordEditText.setText("");

                                }
                            });
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivityForResult(registerIntent, REGISTER_REQUEST_CODE);
            }
        });
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFirebase();
        initUI();
        getSupportActionBar().hide();
        if (firebaseOps.isUserLoggedIn()) {
            toggleProgressBar();
            currentUserManager.retrieveCurrentUserObj(firebaseOps.getCurrentFirebaseUser().getUid());
        }
    }

    public void createUserObjectInDatabase(String userId) {
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        Query queryCurrentUser = usersRef.child(userId);
        queryCurrentUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);

                    User userToBeRegistered = new User();
                    userToBeRegistered.setId(userId);
                    userToBeRegistered.setLastName(sharedPreferences.getString(LAST_NAME, ""));
                    userToBeRegistered.setFirstName(sharedPreferences.getString(FIRST_NAME, ""));
                    userToBeRegistered.setRegistrationNumber(sharedPreferences.getString(REGISTRATION_NO, ""));
                    userToBeRegistered.setPhoneNumber(sharedPreferences.getString(PHONE_NO, ""));
                    userToBeRegistered.setRole(sharedPreferences.getString(ROLE, ""));

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(firebaseOps.getCurrentFirebaseUser().getUid())
                            .setValue(userToBeRegistered);
                }
                currentUserManager.retrieveCurrentUserObj(firebaseOps.getCurrentFirebaseUser().getUid());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public void openActivityForRole(String role) {
       if (role.equals("Team Leader")) {
            Log.d("Query", "This is a team leader");
            openActivityForRoleIntent = new Intent(MainActivity.this, TeamLeaderViewTeam.class);
            startActivity(openActivityForRoleIntent);
            finish();
        } else {
            openActivityForRoleIntent = new Intent(MainActivity.this, UserCalendarActivity.class);
            startActivity(openActivityForRoleIntent);
            finish();
        }
    }

    public void toggleProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        emailEditText.setVisibility(View.GONE);
        passwordEditText.setVisibility(View.GONE);
        loginButton.setVisibility(View.GONE);
        registerButton.setVisibility(View.GONE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REGISTER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Please check your email to activate your account", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onUsersCallback() {

    }

    @Override
    public void onRolesCallback() {

    }

    @Override
    public void onCurrentUserRetrieved(User user) {

        if (openActivityForRoleIntent == null) {
            toggleProgressBar();
            openActivityForRole(user.getRole());
        }

    }
}



