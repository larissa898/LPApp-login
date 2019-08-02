package com.example.larisa.leavingpermissionapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.larisa.leavingpermissionapp.Activity.CalendarActivity;
import com.example.larisa.leavingpermissionapp.Activity.RegisterActivity;
import com.example.larisa.leavingpermissionapp.Activity.ViewTeam;
import com.example.larisa.leavingpermissionapp.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
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


public class MainActivity extends AppCompatActivity {
    private Button login;
    private Button cancel;
    private EditText userNM;
    private EditText password;
    private TextView register;


    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mStateListener;
    private TextView textView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        if (isUserLoggedIn()) {
            finish();
        }


        login = findViewById(R.id.button);

        userNM = findViewById(R.id.editTextNM);
        cancel = findViewById(R.id.button2);
        password = findViewById(R.id.editText);
        register = findViewById(R.id.registerButton);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("message");
        databaseReference.setValue("Hello there");


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                Log.d("Message", "Reading from the database" + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Message", "Failed to read from the database");
            }
        });


        mStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d("User", "is signed in");

                } else
                    Log.d("User", "is signed out");


            }
        };


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userNM.getText().toString();
                String pwd = password.getText().toString();
                if (email.equals("") || pwd.equals("")) {
                    Toast.makeText(MainActivity.this, "Please enter your credentials", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.signInWithEmailAndPassword(email, pwd)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    //check that the user has validated the email
                                    if (task.isSuccessful() && FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        createUser(user.getUid());
                                        redirectUser(user.getUid());

//                                            }
                                    } else {
//                                        if (!FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
//                                            Toast.makeText(MainActivity.this, "Please verify your email address", Toast.LENGTH_LONG).show();
//                                        } else {
                                            Toast.makeText(MainActivity.this, "Wrong Credentials", Toast.LENGTH_LONG).show();
                                            userNM.setText("");
                                            password.setText("");
//                                        }
                                    }


                                }


                            });
                }


            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }


    public boolean isUserLoggedIn() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
            startActivity(intent);
            return true;
        } else {
            return false;
        }
    }


    public void createUser(String userId) {
        DatabaseReference functionRef = FirebaseDatabase.getInstance().getReference("Users");
        Query query = functionRef.child(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    SharedPreferences sharedPref = getSharedPreferences("LPAppSharedPreferences", Context.MODE_PRIVATE);
                    String registerFullName = sharedPref.getString("registerFullName", "");
                    final String registerFunction = sharedPref.getString("registerFunction", "");
                    String registerNumber = sharedPref.getString("registerNumber", "");
                    String registerPhone = sharedPref.getString("registerPhone", "");

                    User registerUser = new User(registerFullName, registerFunction,
                            registerPhone, registerNumber);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(registerUser).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {

                                        Toast.makeText(MainActivity.this, "User has been successfully created", Toast.LENGTH_SHORT).show();
                                        if (registerFunction.equals("Team Leader")) {
                                            Intent intent = new Intent(MainActivity.this, ViewTeam.class);
                                            startActivity(intent);
                                            userNM.setText("");
                                            password.setText("");
                                        } else {
                                            Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                                            startActivity(intent);
                                            userNM.setText("");
                                            password.setText("");
                                        }


                                    }
                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void redirectUser(String userId) {

        DatabaseReference functionRef = FirebaseDatabase.getInstance().getReference("Users");
        Query query = functionRef.child(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String functie = dataSnapshot.child("functie").getValue(String.class);

                    if (functie.equals("Team Leader")) {
                        Log.d("Query", "This is a team leader");
                        Intent intent = new Intent(MainActivity.this, ViewTeam.class);
                        startActivity(intent);
                        userNM.setText("");
                        password.setText("");
                    } else {
                        Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                        startActivity(intent);
                        userNM.setText("");
                        password.setText("");

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        Toast.makeText(MainActivity.this, "User has signed in", Toast.LENGTH_LONG).show();

    }


}



