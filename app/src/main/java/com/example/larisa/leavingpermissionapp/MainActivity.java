/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.larisa.leavingpermissionapp.Activity.AdminActivity;
import com.example.larisa.leavingpermissionapp.Activity.CalendarActivity;
import com.example.larisa.leavingpermissionapp.Utils.FirebaseOps;
import com.example.larisa.leavingpermissionapp.Activity.RegisterViewPagerActivity;
import com.example.larisa.leavingpermissionapp.Activity.ViewTeamActivity;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    // UI
    private Button login;
    private EditText userNM;
    private EditText password;
    private TextView register;

    // Firebase
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    private void init() {
        login = findViewById(R.id.loginButton);
        userNM = findViewById(R.id.userNameET);
        password = findViewById(R.id.passwordET);
        register = findViewById(R.id.registerButton);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Leaving Permission App");

        initFirebase();
        init();

        if (isUserLoggedIn()) {

            redirectUser(user.getUid());
            finish();
        }


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userNM.getText().toString();
                String pwd = password.getText().toString();
                if (email.equals("") || pwd.equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter both your credentials", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.signInWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    //check that the user has validated the email
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        if (user.isEmailVerified()) {
                                            Log.d(TAG, "onComplete: isEmailVerified = " + FirebaseAuth.getInstance().getCurrentUser().isEmailVerified());
                                            createUser(user.getUid());
                                            redirectUser(user.getUid());

                                        } else {
                                            Log.d(TAG, "onComplete: isEmailVerified = " + FirebaseAuth.getInstance().getCurrentUser().isEmailVerified());
                                            Toast.makeText(MainActivity.this, "Please verify your Email first", Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                }

                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(MainActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                    userNM.setText("");
                                    password.setText("");

                                }
                            });
                }


            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(MainActivity.this, RegisterViewPagerActivity.class);

                startActivityForResult(registerIntent, 111);
            }
        });


    }


    /**
     * @return true if user hasn't logged out from last session, false otherwise
     */
    public boolean isUserLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }


    public void createUser(String userId) {
        DatabaseReference functionRef = FirebaseDatabase.getInstance().getReference("Users");
        Query query = functionRef.child(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    User userToBeRegistered = new User();
                    String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
                    try {
                        FileInputStream fileInputStream = new FileInputStream(new File(dirPath, "user.txt"));
                        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
                        userToBeRegistered = (User) objectInputStream.readObject();
                        objectInputStream.close();
                        fileInputStream.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }

                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(userToBeRegistered);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void redirectUser(String userId) {

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        Query queryCurrentUser = usersRef.child(userId);

        // addListenerForSingleValueEvent will be triggered once with the value of the data at the location.
        queryCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.d(TAG, "onDataChange: datasnpashot.exists = " + dataSnapshot.exists());
                    String functie = dataSnapshot.child("functie").getValue(String.class);
                    if (functie.equals("admin")) {
                        Intent intent = new Intent(MainActivity.this, AdminActivity.class);
                        startActivity(intent);
                        finish();
                    } else if (functie.equals("Team Leader")) {
                        Log.d("Query", "This is a team leader");
                        Intent intent = new Intent(MainActivity.this, ViewTeamActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.d(TAG, "onDataChange: 1");
                        Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(MainActivity.this, "User has signed in", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 111) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Please check your email to activate your account", Toast.LENGTH_SHORT).show();
            }
        }
    }
}



