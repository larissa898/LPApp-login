/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.Model.LeavingPermission;
import com.example.larisa.leavingpermissionapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static com.example.larisa.leavingpermissionapp.R.id.spinnerFrom;
import static com.example.larisa.leavingpermissionapp.R.id.spinnerTo;
import static java.lang.String.valueOf;

public class RaportActivity extends AppCompatActivity {

    private static final String TAG = "RaportActivity";

    private DatabaseReference mDatabase;
    private Button Confirm;
    private Spinner From;
    private Spinner To;
    private TextView date;
    private TextView Nume;
    private Button Backraport;
    public int minutes;
    public int ora;
    private TextView TotalOre;
    private int day;
    private int month;
    private int year;
    private String LPid;
    private String fromEdit;
    private String toEdit;
    private String status = "neconfirmat";
    private Float total;
    private String first = "";
    private String second = "";
    private String[] listFinal;
    private String[] listFinalEdit;
    private boolean[] takenIntervals;
    private boolean[] takenIntervalsEdit;
    private String monthActual;
    private String[] toListFirebase = new String[]{};
    private String[] fromListFirebase = new String[]{};
    private String[] toListFirebaseEdit = new String[]{};
    private String[] fromListFirebaseEdit = new String[]{};
    private String Flag;
    private int FromMinutes;
    private int FromHour;
    private String[] hourMinTo;
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapteredit;
    private String key;
    private Float TotalLpActual;
    private String[] hourMinFrom;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        listFinal = new String[24];
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raport);
        final String[] items = new String[]{"7:30", "8:00", "8:30", "9:00", "9:30",
                "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30",
                "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30",
                "18:00", "18:30", "19:00"};

        takenIntervals = new boolean[items.length];
        Arrays.fill(takenIntervals, false);
        takenIntervalsEdit = new boolean[items.length];
        Arrays.fill(takenIntervalsEdit, false);
        Confirm = findViewById(R.id.ConfirmButtonRaport);
        From = findViewById(spinnerFrom);
        To = findViewById(spinnerTo);
        TotalOre = findViewById(R.id.lblUserRep);
        date = findViewById(R.id.editTextDate);
        Nume = findViewById(R.id.textViewNume);
        Backraport = findViewById(R.id.buttonBackRaport);
        //If you get into this activity from editing you get "edit" in Flag
        //If you get into this activity from add you get "add" in the Flag
        Flag = getIntent().getStringExtra("Flag");
        fromEdit = getIntent().getStringExtra("fromEdit");
        toEdit = getIntent().getStringExtra("toEdit");
        day = getIntent().getIntExtra("day", 0);
        month = getIntent().getIntExtra("month", 0);
        year = getIntent().getIntExtra("year", 0);
        total = getIntent().getFloatExtra("total", 0);
        key = getIntent().getStringExtra("key");    //TODO: never used. delete ?
        LPid = getIntent().getStringExtra("idLp");
        TotalLpActual = getIntent().getFloatExtra("TotalLpActual", 0);
        monthActual = getIntent().getStringExtra("monthActual");
        mDatabase = FirebaseDatabase.getInstance().getReference();
        date.setText(day + " " + monthActual + " " + year);

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
        final DatabaseReference dbReference;
        dbReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("LeavingPermission").
                child(day + " " + monthActual + " " + year);
        final List<String> listFrom = new ArrayList<>();
        final List<String> listTo = new ArrayList<>();
        final List<String> listFromEdit = new ArrayList<>();
        final List<String> listToEdit = new ArrayList<>();

        //create listFrom and listTo from Firebase and generate listFinal
        dbReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listFrom.clear();
                listTo.clear();
                listFromEdit.clear();
                listToEdit.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        listFrom.add(valueOf(snapshot.child("from").getValue()));
                        listTo.add(valueOf(snapshot.child("to").getValue()));
                        if (!snapshot.child("from").getValue().equals(fromEdit)) {
                            listFromEdit.add(valueOf(snapshot.child("from").getValue()));
                            listToEdit.add(valueOf(snapshot.child("to").getValue()));
                        }

                    }
                }

                toListFirebase = new String[listFrom.size() + 1];
                fromListFirebase = new String[listFrom.size() + 1];
                toListFirebaseEdit = new String[listFrom.size() + 1];
                fromListFirebaseEdit = new String[listFrom.size() + 1];
                takenIntervals = GetTakenInterval(listFrom, listTo, items);
                takenIntervalsEdit = GetTakenIntervalEdit(listFromEdit, listToEdit, items);
                listFinalEdit = new String[takenIntervalsEdit.length];
                int i = 0;
                int k = 0;
                for (int j = 0; j < takenIntervals.length; j++) {
                    takenIntervalsEdit[j] = takenIntervals[j];
                    if (takenIntervals[j] == false) {
                        listFinal[i] = items[j];
                        i++;
                    }
                    if (takenIntervalsEdit[j] == false) {
                        listFinalEdit[k] = items[j];
                        k++;
                    }
                }

                toListFirebase[listFrom.size()] = "20:00";
                fromListFirebase[listFrom.size()] = "20:00";
                toListFirebaseEdit[listFromEdit.size()] = "20:00";
                fromListFirebaseEdit[listFromEdit.size()] = "20:00";

                // create adapter
                //If listFrom size = 0 then the adapter is made up of everything that contains items
                //If listFrom size != 0 then the adapter is created by eliminating existing time intervals
                if (listFrom.size() == 0) {
                    adapter = new ArrayAdapter<>(RaportActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, items);
                } else {
                    adapter = new ArrayAdapter<>(RaportActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, listFinal);

                }

                //Edit
                if (Flag.equals("edit")) {
                    adapteredit = new ArrayAdapter<>(RaportActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, items);
                    From.setAdapter(adapteredit);
                    int positionFrom = indexOf(adapteredit, fromEdit);

                    From.setSelection(positionFrom);
                    first = fromEdit;
                    second = toEdit;
                    Confirm.setText("EDIT");
                    //Select From Hour for LPEdit
                    From.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            first = From.getSelectedItem().toString();
                            String[] hourMinFrom = first.split(":");
                            FromMinutes = Integer.valueOf(hourMinFrom[1]);
                            FromHour = Integer.valueOf(hourMinFrom[0]);
                            hourMinTo = toEdit.split(":");
                            List<String> ToListEdit = new ArrayList<>();
                            //Generate ToList
                            ToListEdit = GenerateList(listFromEdit, FromHour, FromMinutes, toListFirebaseEdit,
                                    fromListFirebaseEdit);
                            //create adapterTo
                            ArrayAdapter<String> adapterTo = new ArrayAdapter<>(RaportActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item, ToListEdit);
                            To.setAdapter(adapterTo);
                            final int positionTo = indexOf(adapterTo, toEdit);
                            To.setSelection(positionTo);
                            SetTotal(hourMinFrom, hourMinTo);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                }
                //Add
                else if (Flag.equals("add")) {
                    From.setAdapter(adapter);
                    Confirm.setText("ADD");
                    //Select From Hour from LPAdd
                    From.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            first = From.getSelectedItem().toString();
                            String[] hourMinFrom = first.split(":");
                            FromMinutes = Integer.valueOf(hourMinFrom[1]);
                            FromHour = Integer.valueOf(hourMinFrom[0]);
                            List<String> ToList = new ArrayList<>();
                            //Generate ToList
                            ToList = GenerateList(listFrom, FromHour, FromMinutes, toListFirebase, fromListFirebase);
                            //create adapterTo
                            ArrayAdapter<String> adapterTo = new ArrayAdapter<>(RaportActivity.this,
                                    android.R.layout.simple_spinner_dropdown_item, ToList);
                            To.setAdapter(adapterTo);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        final Query myUserRefQuery = usersRef.child(userId);
        myUserRefQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String nume = dataSnapshot.child("lastName").getValue(String.class) + " "
                            + dataSnapshot.child("firstName").getValue(String.class);
                    Nume.setText(nume);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Confirm.setEnabled(false);


        Confirm.setOnClickListener(new View.OnClickListener() {
            DateFormat df = new SimpleDateFormat("HH:mm:ss");
            String time = df.format(Calendar.getInstance().getTime());

            @Override
            public void onClick(View v) {
                //Edit LeavingPermission
                if (Flag.equals("edit")) {
                    Log.d(TAG, "xxxxxxx onClick: LPid = " + LPid);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("LeavingPermission").
                            child(day + " " + monthActual + " " + year).child(LPid);

                    dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            //Create new LeavingPermission after edit original LeavingPermission in Firebase
                            if (dataSnapshot.exists()) {
                                Log.d(TAG, "xxxxxxx onDataChange: ds = " + dataSnapshot);
                                String nume = dataSnapshot.child("fullName").getValue(String.class);
                                Log.d(TAG, "xxxxxxx onDataChange: nume = " + nume);

                                Float total = minutes == 30 ? Float.valueOf(ora + ".5") : Float.valueOf(valueOf(ora));


                                LeavingPermission leavingPermission = new LeavingPermission(LPid, nume, From.getSelectedItem().toString(), To.getSelectedItem().toString(), total, status);

                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("LeavingPermission").child(date.getText()
                                        .toString()).child(LPid).setValue(leavingPermission);
                            }

                            adapter.notifyDataSetChanged();
                            finish();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                //Add LeavingPermission
                else if (Flag.equals("add")) {
                    myUserRefQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //Add LeavingPermission in Firebase
                            if (dataSnapshot.exists()) {
                                String fullName = dataSnapshot.child("lastName").getValue(String.class) + " " + dataSnapshot.child("firstName").getValue(String.class);

                                Float total = minutes == 30 ? Float.valueOf(valueOf(ora + ".5")) : Float.valueOf(valueOf(ora));

                                String id = UUID.randomUUID().toString();
                                LeavingPermission leavingPermission = new LeavingPermission(id, fullName, From.getSelectedItem().toString(), To.getSelectedItem().toString(), total, status);
                                FirebaseDatabase.getInstance()
                                        .getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                        .child("LeavingPermission")
                                        .child(date.getText().toString()).child(id).setValue(leavingPermission);
                            }

                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
                //Create new Intent
                //When press Confirm Button redirection in Leaving Permission List
//                Intent intent = new Intent(RaportActivity.this, UserLeavingPermissionList.class);
//                intent.putExtra("day", day);
//                intent.putExtra("month", month);
//                intent.putExtra("year", year);
//                intent.putExtra("monthActual", monthActual);
//                startActivity(intent);


            }

        });
        //To Hour Select
        To.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hourMinFrom = first.split(":");
                second = To.getSelectedItem().toString();
                hourMinTo = second.split(":");
                //Call Set Total to calculate the total hours on that LeavingPermission
                SetTotal(hourMinFrom, hourMinTo);
                if (first.equals(second)) {
                    Confirm.setEnabled(false);
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }

        });

        //When press Back button return to previous activity <<Leaving Permission List>> without changing
        Backraport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RaportActivity.this, UserLeavingPermissionList.class);
                intent.putExtra("day", day);
                intent.putExtra("month", month);
                intent.putExtra("year", year);
                intent.putExtra("monthActual", monthActual);
                startActivity(intent);
                finish();
            }
        });

    }

    //The function to create a boolean list of the hours used
    //true-used
    //false-unused
    // and generate toListFirebase/ fromListFirebase for ADD
    boolean[] GetTakenInterval(List listFrom, List listTo, String[] items) {

        if (listFrom.size() != 0) {
            boolean foundFirstEntry = false;
            for (int j = 0; j < listFrom.size(); j++) {
                for (int i = 0; i < items.length; i++) {
                    if (items[i].equals(listFrom.get(j))) {
                        foundFirstEntry = true;
                        takenIntervals[i] = true;
                        fromListFirebase[j] = items[i];
                    } else if (foundFirstEntry) {
                        if (items[i].equals(listTo.get(j))) {
                            foundFirstEntry = false;
                            takenIntervals[i] = false;
                            toListFirebase[j] = items[i];
                        } else {
                            takenIntervals[i] = true;
                        }
                    }
                }
            }
        }
        return takenIntervals;
    }

    //The function to create a boolean list of the hours used
    //true-used
    //false-unused
    // and generate toListFirebase/ fromListFirebase for EDIT
    boolean[] GetTakenIntervalEdit(List listFrom, List listTo, String[] items) {

        if (listFrom.size() != 0) {
            boolean foundFirstEntry = false;
            for (int j = 0; j < listFrom.size(); j++) {
                for (int i = 0; i < items.length; i++) {
                    if (items[i].equals(listFrom.get(j))) {
                        foundFirstEntry = true;
                        takenIntervals[i] = true;
                        fromListFirebaseEdit[j] = items[i];
                    } else if (foundFirstEntry) {
                        if (items[i].equals(listTo.get(j))) {
                            foundFirstEntry = false;
                            takenIntervals[i] = false;
                            toListFirebaseEdit[j] = items[i];
                        } else {
                            takenIntervals[i] = true;
                        }
                    }
                }
            }
        }
        return takenIntervals;
    }

    //Generate the To list based on existing time intervals
    //Removes existing time intervals in Firebase
    List<String> GenerateList(List<String> listFrom, int FromHour, int FromMinutes, String[] toListFirebase,
                              String[] fromListFirebase) {
        Boolean ok = false;
        List<String> ToList = new ArrayList<>();
        String[] FromListH = new String[listFrom.size() + 1];
        String[] FromListM = new String[listFrom.size() + 1];
        String[] ToListH = new String[listFrom.size() + 1];
        String[] ToListM = new String[listFrom.size() + 1];
        int iterator;
        int index;
        for (index = 0; index < listFrom.size() + 1; index++) {
            String[] hourMinToListFirs = toListFirebase[index].split(":");
            String[] hourMinFromListFirst = fromListFirebase[index].split(":");
            FromListH[index] = hourMinFromListFirst[0];
            FromListM[index] = hourMinFromListFirst[1];
            ToListH[index] = hourMinToListFirs[0];
            ToListM[index] = hourMinToListFirs[1];
        }
        //If listFrom.size == 0 generates the full range of hours
        if (listFrom.size() == 0) {
            if (((FromHour == 8) && (FromMinutes == 0)) || ((FromHour == 9) && (FromMinutes == 0)) ||
                    ((FromHour == 10) && (FromMinutes == 0)) || ((FromHour == 11) && (FromMinutes == 0)) ||
                    ((FromHour == 12) && (FromMinutes == 0)) || ((FromHour == 13) && (FromMinutes == 0)) ||
                    ((FromHour == 14) && (FromMinutes == 0)) || ((FromHour == 15) && (FromMinutes == 0)) ||
                    ((FromHour == 16) && (FromMinutes == 0)) || ((FromHour == 17) && (FromMinutes == 0)) ||
                    ((FromHour == 18) && (FromMinutes == 0)) || ((FromHour == 19) && (FromMinutes == 0))) {
                for (int j = 0; j <= 2 * (20 - FromHour); j = j + 2) {
                    ToList.add(FromHour + ":" + 30);
                    ToList.add(FromHour + 1 + ":" + "00");
                    FromHour++;
                }
            } else {
                for (int j = 0; j <= 2 * (18 - FromHour); j = j + 2) {
                    ToList.add(FromHour + 1 + ":" + "00");
                    ToList.add(FromHour + ":" + 30);
                    FromHour++;
                }
            }
        } else if (listFrom.size() > 0) {
            for (iterator = 0; iterator < listFrom.size() + 1; iterator++) {
                for (index = 0; index < listFrom.size() + 1; index++) {
                    if ((FromHour < Integer.valueOf(FromListH[index]) && ok == false) ||
                            (FromHour == Integer.valueOf(FromListH[index]) &&
                                    FromMinutes < Integer.valueOf(FromListM[index]) && ok == false)) {
                        ok = true;
                        while (FromHour < Integer.valueOf(FromListH[index])) {
                            if (((FromMinutes == Integer.valueOf(30)))) {
                                ToList.add(FromHour + 1 + ":" + "00");
                                ToList.add(FromHour + 1 + ":" + 30);
                                FromHour++;
                            } else {
                                ToList.add(FromHour + ":" + 30);
                                ToList.add(FromHour + 1 + ":" + "00");
                                FromHour++;
                            }
                            if (FromMinutes != Integer.valueOf(30) &&
                                    Integer.valueOf(FromListM[index]) == 30) {
                                ToList.add(FromHour + ":" + 30);
                            }
                        }
                    }
                }
            }
        }
        return ToList;
    }

    //The function finds the index of the selected value
    private int indexOf(final Adapter adapter, Object value) {
        for (int index = 0; index < adapter.getCount(); ++index) {
            if (adapter.getItem(index).equals(value)) {
                return index;
            }
        }
        return -1;
    }

    //Calculates the total hours on current Leaving Permission and does not allow to exceed 3 hours in total per day
    void SetTotal(String[] hourMinFrom, String[] hourMinTo) {
        int hourResult = 0;
        int minResult = 0;
        if (Integer.valueOf(hourMinTo[1]) < Integer.valueOf(hourMinFrom[1])) {
            hourResult = Integer.valueOf(hourMinTo[0]) - Integer.valueOf(hourMinFrom[0]) - 1;
            minResult = 30;
        } else {
            hourResult = Integer.valueOf(hourMinTo[0]) - Integer.valueOf(hourMinFrom[0]);
            minResult = Integer.valueOf(hourMinTo[1]) - Integer.valueOf(hourMinFrom[1]);
        }
        int plus = 0;
        int minfin = 0;
        if (total % 10 == 3) {
            TotalOre.setText("No!");
        }
        if (minResult == 30 && (total - total % 10) == 0.5) {
            plus = 1;
            minfin = 0;

        } else if ((minResult == 30 && (total - total % 10) == 0) || (minResult == 0 && (total - total % 10) == 0.5)) {
            minfin = 30;
        } else if ((minResult == 0 && (total - total % 10) == 0)) {
            minfin = 0;
        }
        Float result = null;
        if (minResult == 30) {
            result = Float.valueOf(hourResult + ".5");
        } else {
            result = Float.valueOf(hourResult);
        }

        if ((((result + (total % 10) - TotalLpActual) > 3) ||
                ((((hourResult + (total % 10) + plus - TotalLpActual)) == 3)
                        && (minfin != 0)))) {
            TotalOre.setTextSize(14);
            TotalOre.setText("You exceeded 3 hours a day. Choose another interval!");
            Confirm.setEnabled(false);
        } else {
            TotalOre.setTextSize(18);
            TotalOre.setText(hourResult + " hours and " + minResult + " minutes");
            if (hourResult == 0 && minResult == 0) {
                Confirm.setEnabled(false);
            }
            Confirm.setEnabled(true);
            minutes = minResult;
            ora = hourResult;
        }

    }
}
