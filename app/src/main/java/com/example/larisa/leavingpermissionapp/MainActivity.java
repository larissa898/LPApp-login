package com.example.larisa.leavingpermissionapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.larisa.leavingpermissionapp.Activity.CalendarActivity;
import com.example.larisa.leavingpermissionapp.Database.Database;
import com.example.larisa.leavingpermissionapp.Model.User;

public class MainActivity extends AppCompatActivity {
    private Button login;
    private Button cancel;
    private EditText userNM;
    private EditText password;

    public Database db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new Database(this);

        final User user = new User();
        user.setMatricol(1234);
        user.setUserNume("Maria");
        user.setÚserPrenume("Popescu");
        user.setFunctie("Inginer de sistem");
        user.setParola("parola");

        //db.insertDB(user);


        login = findViewById(R.id.button);
        cancel =  findViewById(R.id.button2);
        userNM =  findViewById(R.id.editTextNM);
        password=  findViewById(R.id.editText);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NumarMatricol = userNM.getText().toString();
                String Password = password.getText().toString();
                Boolean check =  db.CheckCredential(NumarMatricol,Password);

                if(check){
                    Toast.makeText(MainActivity.this,"Redirecting...",Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                            startActivity(intent);
                            finish();

                }else{
                    Toast.makeText(MainActivity.this,"Wrong Credentials",Toast.LENGTH_LONG).show();
                    userNM.setText("");
                    password.setText("");
                }


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
