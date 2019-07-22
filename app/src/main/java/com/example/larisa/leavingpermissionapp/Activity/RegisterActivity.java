package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.larisa.leavingpermissionapp.MainActivity;
import com.example.larisa.leavingpermissionapp.Model.LP;
import com.example.larisa.leavingpermissionapp.Model.User;
import com.example.larisa.leavingpermissionapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private EditText lastName;
    private EditText firstName;
    private EditText function;
    private  Button  confirmButton;
    private Button cancelRegistration;
    private List <LP> LPs;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = findViewById(R.id.registerEmail);
        password = findViewById(R.id.registerPassword);
        confirmPassword = findViewById(R.id.confirmPassword);
        lastName = findViewById(R.id.registerLastName);
        firstName = findViewById(R.id.registerFirstName);
        function = findViewById(R.id.registerFunction);
        confirmButton = findViewById(R.id.confirmRegister);
        cancelRegistration = findViewById(R.id.registerCancel);
        mAuth = FirebaseAuth.getInstance();
        confirmButton.setOnClickListener(this);
        cancelRegistration.setOnClickListener(this);
    }
    public void registerUser()
    {
        final String registerEmail = email.getText().toString().trim();
        final String registerPassword = password.getText().toString().trim();
        final String registerConfirmPassword = confirmPassword.getText().toString().trim();
        final String registerLastName = lastName.getText().toString().trim();
        final String registerFirstName = firstName.getText().toString().trim();
        final String registerFunction = function.getText().toString().trim();
        final String registerFullName = registerFirstName + " " + registerLastName;
        // validate Email field
        if(registerEmail.isEmpty())
        {
            email.setError("The email field cannot be empty");
            email.requestFocus();
            return;
        }
        else
        {
            if( !Patterns.EMAIL_ADDRESS.matcher(registerEmail).matches())
            {
                email.setError("Please choose a valid email");
                email.requestFocus();
                return;
            }

        }
        //validate password field
        if(registerPassword.isEmpty())
        {
            password.setError("The password field cannot be empty !");
            password.requestFocus();
            return;
        }
        else
        {
            Pattern pattern;
            Matcher matcher;
            final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
            pattern = Pattern.compile(PASSWORD_PATTERN);
            matcher = pattern.matcher(registerPassword);
            if(!matcher.matches())
            {
                password.setError("You must choose a valid password!");
                password.requestFocus();
                return;
            }
        }
        //validate password confirmation
        if(registerConfirmPassword.isEmpty())
        {
            confirmPassword.setError("This field cannot be empty");
            confirmPassword.requestFocus();
            return;
        }
        else
        {
            if(!registerConfirmPassword.equals(registerPassword))
            {
                confirmPassword.setError("The passwords do not match!Please try again");
                confirmPassword.requestFocus();
                return;
            }
        }

        //validate last name
        if(registerLastName.isEmpty())
        {
            lastName.setError("This field cannot be empty!");
            lastName.requestFocus();
            return;
        }
       //validate first name
        if(registerFirstName.isEmpty())
        {
            firstName.setError("This field cannot be empty");
            firstName.requestFocus();
            return;
        }
        //validate function
        if(registerFunction.isEmpty())
        {
            function.setError("This field cannot be empty!");
            function.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(registerEmail, registerPassword).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    User user = new User(registerFullName,registerFunction);
                    FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user).
                            addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())

                                    {
                                        FirebaseUser user =  mAuth.getCurrentUser();
                                        user.sendEmailVerification();

                                        Toast.makeText(RegisterActivity.this, "User has been successfully created, please verify your email adress", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                    //FirebaseDatabase.getInstance().getReference("Users")
                         //   .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("LP").setValue("no LPs");
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(RegisterActivity.this, "User has not been created", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.confirmRegister:
                registerUser();
                break;
            case R.id.registerCancel:
                finish();
                break;
        }

    }
}
