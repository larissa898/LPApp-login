package com.example.larisa.leavingpermissionapp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.larisa.leavingpermissionapp.R;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private EditText lastName;
    private EditText firstName;
    private EditText function;
    private  Button  registerButton;

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
        registerButton.setOnClickListener(this);


        mAuth = FirebaseAuth.getInstance();





    }
    public void registerUser()
    {
        final String registerEmail = email.getText().toString().trim();
        final String registerPassword = password.getText().toString().trim();
        final String registerConfirmPassword = confirmPassword.getText().toString().trim();
        final String registerLastName = lastName.getText().toString().trim();
        final String registerFirstName = firstName.getText().toString().trim();
        final String registerFunction = function.getText().toString().trim();

        if(registerEmail.isEmpty())
        {
            email.setError("You must enter an email!");
            email.requestFocus();
            return;
        }
        else
        {

        }


    }

    @Override
    public void onClick(View v) {

    }
}
