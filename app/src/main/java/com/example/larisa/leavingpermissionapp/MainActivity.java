package com.example.larisa.leavingpermissionapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button login;
    private Button cancel;
    private EditText userNM;
    private EditText password;
    private Button register;


    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        //db.insertDB(user);


        login = findViewById(R.id.button);
        cancel =  findViewById(R.id.button2);
        userNM =  findViewById(R.id.editTextNM);
        password=  findViewById(R.id.editText);
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
                Log.d("Message", "Failed to read from the database" );
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        mStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null)
                {
                    Log.d("User" , "is signed in");

                }
                else
                    Log.d("User" , "is signed out");


            }
        };


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = userNM.getText().toString();
                String pwd = password.getText().toString();
                if(email.equals("") || pwd.equals(""))
                {
                    Toast.makeText(MainActivity.this, "Please enter your credentials", Toast.LENGTH_SHORT).show();
                }
                else
                {


//Authenticate with fire base

                firebaseAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                               String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                               FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


                                //check that the user has validated the email
                                if(task.isSuccessful() && user.isEmailVerified())


                                {
                                    //check if the user is a team leader or not


                                    DatabaseReference functionRef =  FirebaseDatabase.getInstance().getReference("Users");
                                    Query query =  functionRef.child(userId);
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists())
                                            {   String functie = dataSnapshot.child("functie").getValue(String.class);

                                                if(functie.equals("Team Leader"))
                                                {
                                                    Log.d("Query", "This is a team leader");
                                                    Intent intent = new Intent(MainActivity.this, ViewTeam.class);
                                                    startActivity(intent);
                                                    userNM.setText("");
                                                    password.setText("");
                                                }
                                                else

                                                {
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
                                else
                                    {
                                        if(!user.isEmailVerified())
                                        {
                                            Toast.makeText(MainActivity.this,"Please verify your email address",Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(MainActivity.this,"Wrong Credentials",Toast.LENGTH_LONG).show();
                                            userNM.setText("");
                                            password.setText("");
                                        }

                                }

                }


            });
                }
        }});
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
}
