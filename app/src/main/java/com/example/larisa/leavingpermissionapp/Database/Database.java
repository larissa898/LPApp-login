package com.example.larisa.leavingpermissionapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.larisa.leavingpermissionapp.User;
import com.example.larisa.leavingpermissionapp.Utils;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private Context context;

    public Database(Context context) {
        super(context, Utils.DATABASE, null, Utils.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE_USERS = "CREATE TABLE " + Utils.TABLE_USERS + " (" + Utils.USERS_MATRICOL + " INTEGER PRIMARY KEY, " + Utils.USERS_NUME + " TEXT," + Utils.USERS_PRENUME +
                " TEXT," + Utils.USERS_PAROLA + " TEXT," + Utils.USERS_FUNCTIE + " TEXT);";
        /* String CREATE_TABLE_LP = "CREATE TABLE " + Utils.TABLE_LEAVING_PERM + "(" + Utils.LP_ID + " INTEGER PRIMARY KEY," + Utils.USERS_MATRICOL  + " INTEGER," + Utils.LP_FROM + "TEXT, " + Utils.LP_TO + "TEXT, " + Utils.LP_DATE  + " TEXT,"+ "FOREIGN KEY (" + Utils.USERS_MATRICOL + ") " + "REFERENCES " + Utils.TABLE_USERS + "(" + Utils.USERS_MATRICOL + "));";*/
        sqLiteDatabase.execSQL(CREATE_TABLE_USERS);
        /* sqLiteDatabase.execSQL(CREATE_TABLE_LP);*/
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + Utils.TABLE_USERS);
        onCreate(sqLiteDatabase);
    }


    public void insertDB(User user)
    {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Utils.USERS_MATRICOL, user.getMatricol());
        contentValues.put(Utils.USERS_NUME, user.getUserNume());
        contentValues.put(Utils.USERS_PRENUME,user.getÚserPrenume());
        contentValues.put(Utils.USERS_FUNCTIE, user.getFunctie());
        contentValues.put(Utils.USERS_PAROLA, user.getParola());
        db.insert(Utils.TABLE_USERS, null,contentValues);


    }
    public User getUser (int id)
    {
        SQLiteDatabase db = getReadableDatabase();
        User user = new User ();
        Cursor cursor = db.query(Utils.TABLE_USERS, new String[]{Utils.USERS_MATRICOL, Utils.USERS_NUME, Utils.USERS_PRENUME, Utils.USERS_FUNCTIE, Utils.USERS_PAROLA},Utils.USERS_MATRICOL + "=?", new String[]{String.valueOf(id)}, null, null, null);

        if(cursor.moveToFirst())
        {
            user.setMatricol(cursor.getColumnIndex(Utils.USERS_MATRICOL));
            user.setUserNume(cursor.getString(cursor.getColumnIndex(Utils.USERS_NUME)));
            user.setÚserPrenume(cursor.getString(cursor.getColumnIndex(Utils.USERS_PRENUME)));
            user.setFunctie(cursor.getString(cursor.getColumnIndex(Utils.USERS_FUNCTIE)));
            user.setParola(cursor.getString(cursor.getColumnIndex(Utils.USERS_PAROLA)));
        }

        cursor.close();
        return user;


    }

    public List<User> getAllItems()
    {
        SQLiteDatabase db = getReadableDatabase();
        List<User> users = new ArrayList<User>();

        Cursor cursor = db.query(Utils.TABLE_USERS, new String[]{Utils.USERS_MATRICOL, Utils.USERS_NUME, Utils.USERS_PRENUME, Utils.USERS_FUNCTIE, Utils.USERS_PAROLA},null, null, null, null, Utils.USERS_NUME);
        do
        {
            if(cursor.moveToFirst())
            {
                User user = new User();
                user.setMatricol(cursor.getColumnIndex(Utils.USERS_MATRICOL));
                user.setUserNume(cursor.getString(cursor.getColumnIndex(Utils.USERS_NUME)));
                user.setÚserPrenume(cursor.getString(cursor.getColumnIndex(Utils.USERS_PRENUME)));
                user.setFunctie(cursor.getString(cursor.getColumnIndex(Utils.USERS_FUNCTIE)));
                user.setParola(cursor.getString(cursor.getColumnIndex(Utils.USERS_PAROLA)));
                users.add(user);

            }
        }while (cursor.moveToNext());

        cursor.close();
        return users;


    }
    public String getLastMatricol()
    {
        SQLiteDatabase db = getReadableDatabase();
        String matricol = "";

        Cursor cursor = db.query(Utils.TABLE_USERS, new String[]{"MAX( " + Utils.USERS_MATRICOL +")"},null, null, null, null, null);
        if(cursor.moveToFirst())
        {
            matricol = cursor.getString(0);
        }

        cursor.close();
        return matricol;




    }



}