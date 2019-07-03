package com.example.larisa.leavingpermissionapp.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.larisa.leavingpermissionapp.Database.Database;
import com.example.larisa.leavingpermissionapp.R;
import com.example.larisa.leavingpermissionapp.Model.User;

public class UserFrom extends AppCompatActivity {
    private Database db;
    private TextView matricol;
    private EditText nume;
    private EditText prenume;
    private EditText functie;
    private EditText parola;
    private Button save;
    private Button cancel;



/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_from);

        cancel = findViewById(R.id.Cancel);
        matricol = findViewById(R.id.matricol_value);
        nume = findViewById(R.id.nume_value);
        prenume = findViewById(R.id.prenume_value);
        functie = findViewById(R.id.functie_value);
        parola = findViewById(R.id.parola_value);
        db = new Database(this);

        Integer numar_matricol = Integer.parseInt(db.getLastMatricol())+ 1;
        matricol.setText(String.valueOf(numar_matricol));



        save = findViewById(R.id.Save);
        save.setOnClickListener(new View.OnClickListener() {
            User user = new User();
            @Override
            public void onClick(View view) {

                user.setMatricol(Integer.parseInt(matricol.getText().toString()));
                user.setUserNume(nume.getText().toString());
                user.setÚserPrenume(prenume.getText().toString());
                user.setFunctie(functie.getText().toString());
                user.setParola(parola.getText().toString());


                db.insertDB(user);

                if (db.getUser(user.getMatricol()) != null)

                {

                    Toast.makeText(UserFrom.this, "Angajatul" + user.getUserNume() + user.getÚserPrenume() + "a fost salvat", Toast.LENGTH_SHORT);

                    matricol.clearComposingText();
                    nume.clearComposingText();
                    prenume.clearComposingText();
                    functie.clearComposingText();
                    parola.clearComposingText();
                }







            }
        });


    }
    */
}
