package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
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
    private Spinner function;
    private  Button  confirmButton;
    private Button cancelRegistration;
    private List <LP> LPs;
    private String [] availableFunctions;
    private EditText registerNumber;
    private EditText phoneNumber;
    private FirebaseAuth mAuth;
    private Button fileChooser;


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
        registerNumber = findViewById(R.id.registerNumber);
        phoneNumber = findViewById(R.id.phoneNumber);
        confirmButton = findViewById(R.id.confirmRegister);
        cancelRegistration = findViewById(R.id.registerCancel);
        availableFunctions = new String[]{"Analist", "Analist Ajutor","Inginer de Sistem","Team Leader",
                "Tehnician",
                "Tehnician ajutor"};
        mAuth = FirebaseAuth.getInstance();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,
                availableFunctions);
        function.setAdapter(adapter);
        confirmButton.setOnClickListener(this);
        cancelRegistration.setOnClickListener(this);




    }
    public void registerUser()
    {   final String number  = registerNumber.getText().toString().trim();
        final String registerEmail = email.getText().toString().trim();
        final String registerPassword = password.getText().toString().trim();
        final String registerConfirmPassword = confirmPassword.getText().toString().trim();
        final String registerLastName = lastName.getText().toString().trim();
        final String registerFirstName = firstName.getText().toString().trim();
        final String registerFunction = function.getSelectedItem().toString().trim();
        final String registerFullName = registerFirstName + " " + registerLastName;
        final String registerPhone  = phoneNumber.getText().toString().trim();


         //validate register number
        if(number.isEmpty())
        {

            registerNumber.setError("The register number field cannot be empty");
            registerNumber.requestFocus();
            return;
        }

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
                email.setError("Please enter a valid email");
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
                password.setError("You must choose a password that contains at least one number, one uppercase and " +
                        "one special character");
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
        if(registerFirstName.isEmpty()) {
            firstName.setError("This field cannot be empty");
            firstName.requestFocus();
            return;
        }
        //validate function
        if(registerPhone.isEmpty())
        {   phoneNumber.setError("This field cannot be empty");
            phoneNumber.requestFocus();
            return;

        }
        else
        {
            if(registerPhone.length()<10)
            {
                phoneNumber.setError("The phone number must have at least 10 digits");
                phoneNumber.requestFocus();
                return;

            }
        }

        mAuth.createUserWithEmailAndPassword(registerEmail, registerPassword).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Context context = RegisterActivity.this;
                    SharedPreferences sharedPref = context.getSharedPreferences(
                          "LPAppSharedPreferences"  , Context.MODE_PRIVATE);
                    FirebaseUser user =  mAuth.getCurrentUser();
                    user.sendEmailVerification();
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("registerFullName", registerFullName);
                    editor.putString("registerFunction", registerFunction);
                    editor.putString("registerNumber", number);
                    editor.putString("registerPhone", registerPhone);
                    editor.apply();
//                    intent.putExtra("registerFullName", registerFullName);
//                    intent.putExtra("registerFunction", registerFunction);
//                    intent.putExtra("registerNumber", number);
//                    intent.putExtra("registerPhone", registerPhone);
//                    intent.putExtra("message","Success");
                    startActivity(intent);
                }
                else

                {
                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    intent.putExtra("message","Failed");
                    startActivity(intent);

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
