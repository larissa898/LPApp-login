package com.example.larisa.leavingpermissionapp;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.larisa.leavingpermissionapp.Activity.CalendarActivity;
import com.example.larisa.leavingpermissionapp.Activity.FinalCalendar;
import com.example.larisa.leavingpermissionapp.Activity.RegisterActivity;
import com.example.larisa.leavingpermissionapp.Activity.ViewTeam;
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
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.apache.poi.hssf.record.DBCellRecord;

import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // UI
    private Button login;
    private Button cancel;
    private EditText userNM;
    private EditText password;
    private TextView register;

    // Firebase
    private FirebaseAuth firebaseAuth;

    private void initFirebase() {
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void init() {
        login = findViewById(R.id.button);
        userNM = findViewById(R.id.editTextNM);
        cancel = findViewById(R.id.button2);
        password = findViewById(R.id.editText);
        register = findViewById(R.id.registerButton);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFirebase();
        init();

        if (isUserLoggedIn()) {

            DatabaseReference databaseReference =
                    FirebaseDatabase.getInstance().getReference("Users").child(firebaseAuth.getCurrentUser().getUid());
           databaseReference.addValueEventListener(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   if(dataSnapshot.exists())
                   {
                       String functie = dataSnapshot.child("functie").getValue(String.class);
                       Log.d("jgnhfbd",functie);
                       if(functie.equals("Team Leader"))
                       {
                           Intent intent = new Intent(MainActivity.this, ViewTeam.class);
                           startActivity(intent);
                           finish();
                       }
                       else
                       {
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


        }

                ActivityManager m = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE );
        List<ActivityManager.RunningTaskInfo> runningTaskInfoList =  m.getRunningTasks(10);
        Iterator<ActivityManager.RunningTaskInfo> itr = runningTaskInfoList.iterator();
        while(itr.hasNext())
        {
            ActivityManager.RunningTaskInfo runningTaskInfo = (ActivityManager.RunningTaskInfo)itr.next();
            int id = runningTaskInfo.id;
            CharSequence desc= runningTaskInfo.description;
            String topActivity = runningTaskInfo.topActivity.getShortClassName();
            int numOfActivities = runningTaskInfo.numActivities;
            Log.d("jbjnfd",String.valueOf(numOfActivities));
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
                                        if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {
                                            Log.d(TAG, "onComplete: isEmailVerified = " + FirebaseAuth.getInstance().getCurrentUser().isEmailVerified());
                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            createUser(user.getUid());
                                            redirectUser(user.getUid());

                                        }
                                        else
                                        {
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
                Intent registerIntent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivityForResult(registerIntent, 111);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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

                    SharedPreferences sharedPref = getSharedPreferences("LPAppSharedPreferences", Context.MODE_PRIVATE);
                    String registerFullName = sharedPref.getString("registerFullName", "");
                    final String registerFunction = sharedPref.getString("registerFunction", "");
                    String registerNumber = sharedPref.getString("registerNumber", "");
                    String registerPhone = sharedPref.getString("registerPhone", "");
                    User registerUser = new User(registerFullName, registerFunction, registerPhone, registerNumber);

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

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        Query queryCurrentUser = usersRef.child(userId);

        // addListenerForSingleValueEvent will be triggered once with the value of the data at the location.
        queryCurrentUser.addListenerForSingleValueEvent(new ValueEventListener() {
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 111) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Please check your email to activate your account", Toast.LENGTH_SHORT).show();
            }



        }
    }
}



