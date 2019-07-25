package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.Adapters.RecycleViewAdapterUser;
import com.example.larisa.leavingpermissionapp.Model.LP;
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

import static java.lang.String.valueOf;

public class RaportActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Button Confirm;
    private Spinner From;
    private Spinner To;
    private TextView date;
    private TextView Nume;
    private Button Beckraport;
    public int minnn;
    public int ora;
    private TextView TotalOre;
    private int day;
    private int month;
    private int year;
    private String fromEdit;
    private String toEdit;
    private String status = "neconfirmat";
    private Float total;
    String first = "";
    String second = "";
    private int minn;
    private String[] listFinal;
    private boolean[] takenIntervals;
    private String monthActual;
    String[] toListFirebase = new String[]{};
    String[] fromListFirebase = new String[]{};
    private String Flag;
    private int FromMinutes;
    private int FromHour;
    private String[] hourMinTo;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapteredit;
    String LpTotal;
    String key ;
    LP LpList;
    Float TotalLpActual;
    public int abc;
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
        Confirm = findViewById(R.id.ConfirmButtonRaport);
        From = findViewById(R.id.spinnerFrom);
        To = findViewById(R.id.spinnerTo);
        TotalOre = findViewById(R.id.TotalTextView);
        date = findViewById(R.id.editTextDate);
        Nume = findViewById(R.id.textViewNume);
        Beckraport = findViewById(R.id.buttonBackRaport);
        LpTotal = getIntent().getStringExtra("LpTotal");
        Flag = getIntent().getStringExtra("Flag");
        fromEdit = getIntent().getStringExtra("fromEdit");
        toEdit = getIntent().getStringExtra("toEdit");
        day = getIntent().getIntExtra("day", 0);
        month = getIntent().getIntExtra("month", 0);
        year = getIntent().getIntExtra("year", 0);
        total = getIntent().getFloatExtra("total", 0);
        key = getIntent().getStringExtra("keyy");
        TotalLpActual = getIntent().getFloatExtra("TotalLpActual",0);
        monthActual =  getIntent().getStringExtra("monthActual");
        date.setText(day + " " + monthActual + " " + year);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        final String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference functionRef = FirebaseDatabase.getInstance().getReference("Users");
        final DatabaseReference dbReference;
        dbReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("LP").
                child(day + " " + monthActual + " " + year);
        final List<String> listFrom = new ArrayList<>();
        final List<String> listTo = new ArrayList<>();

        //create listFrom and listTo from Firebase and generate listFinal
        dbReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listFrom.clear();
                listTo.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        listFrom.add(valueOf(snapshot.child("from").getValue()));
                        listTo.add(valueOf(snapshot.child("to").getValue()));
                    }
                }
                toListFirebase = new String[listFrom.size() + 1];
                fromListFirebase = new String[listFrom.size() + 1];
                takenIntervals = GetTakenInterval( listFrom,    listTo,  items );

                    int i = 0;
                    for (int j = 0; j < takenIntervals.length; j++) {
                        if (takenIntervals[j] == false) {
                            listFinal[i] = items[j];
                            i++;
                        }
                    }

                toListFirebase[listFrom.size()] = "20:00";
                fromListFirebase[listFrom.size()] = "20:00";

                // create adapter
                if (listFrom.size() == 0) {
                    adapter = new ArrayAdapter<>(RaportActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, items);
                }
                else {
                    adapter = new ArrayAdapter<>(RaportActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, listFinal);
                }

                if(Flag.equals("edit")){
                    adapteredit = new ArrayAdapter<>(RaportActivity.this,
                            android.R.layout.simple_spinner_dropdown_item, items);
                    From.setAdapter(adapteredit);
                    To.setAdapter(adapteredit);
                    int positionFrom = indexOf(adapteredit, fromEdit);
                    int positionTo = indexOf(adapteredit, toEdit);
                    From.setSelection(positionFrom);
                    To.setSelection(positionTo);
                    first=fromEdit;
                    second=toEdit;
                    Confirm.setText("EDIT");
                    From.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            first = From.getSelectedItem().toString();
                            String[] hourMinFrom = first.split(":");
                            FromMinutes = Integer.valueOf(hourMinFrom[1]);
                            FromHour = Integer.valueOf(hourMinFrom[0]);
                            hourMinTo = toEdit.split(":");
                            SetTotal(hourMinFrom, hourMinTo);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                }
                else if (Flag.equals("add")){
                    From.setAdapter(adapter);
                    From.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            first = From.getSelectedItem().toString();
                            String[] hourMinFrom = first.split(":");
                            FromMinutes = Integer.valueOf(hourMinFrom[1]);
                            FromHour = Integer.valueOf(hourMinFrom[0]);

                            List<String> ToList = new ArrayList<>();
                            Log.d("*****" , String.valueOf(FromHour));

                            ToList = GenerateList(listFrom, FromHour, FromMinutes);

                            ArrayAdapter<String> adapterTo = new ArrayAdapter<>(RaportActivity.this, android.R.layout.simple_spinner_dropdown_item, ToList);
                            To.setAdapter(adapterTo);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });
                    Confirm.setText("ADD");
                }
                           }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        final Query query = functionRef.child(userId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String nume = dataSnapshot.child("fullName").getValue(String.class);
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
                if(Flag.equals("edit")) {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    final DatabaseReference dbReference;
                    dbReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("LP").
                            child(day+ " "+  monthActual+ " "+year);

                    dbReference.addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                String nume = dataSnapshot.child("fullName").getValue(String.class);
                                Log.d("data", "exists");
                                Float total;
                                if (minnn == 30) {
                                    total = Float.valueOf(valueOf(ora + ".5"));
                                } else {
                                    total = Float.valueOf(valueOf(ora));
                                }
                                LP lp = new LP(nume, From.getSelectedItem().toString()
                                        , To.getSelectedItem().toString(), total, status);
                                FirebaseDatabase.getInstance().getReference("Users")
                                        .child(FirebaseAuth.getInstance()
                                                .getCurrentUser().getUid())
                                        .child("LP").child(date.getText()
                                        .toString()).child(time).setValue(lp);
                            }
                            if(dataSnapshot.exists()){

                                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                                {
                                    if (snapshot.getKey().equals(key)){
                                        snapshot.getRef().removeValue();
                                        return;
                                    }
                                }
                                adapter.notifyDataSetChanged();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
                else if (Flag.equals("add")){
                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                String nume = dataSnapshot.child("fullName").getValue(String.class);
                                Log.d("data", "exists");
                                Float total;
                                if (minnn == 30) {
                                    total = Float.valueOf(valueOf(ora + ".5"));
                                } else {
                                    total = Float.valueOf(valueOf(ora));
                                }
                                LP lp = new LP(nume, From.getSelectedItem().toString(), To.getSelectedItem().toString(), total, status);
                                FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("LP").child(date.getText().toString()).child(time).setValue(lp);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }

                Intent intent = new Intent(RaportActivity.this, LeavingPermissionList.class);
                intent.putExtra("day", day);
                intent.putExtra("month", month);
                intent.putExtra("year", year);
                intent.putExtra("monthActual", monthActual);
                startActivity(intent);

            }

        });

        To.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hourMinFrom = first.split(":");
                               second = To.getSelectedItem().toString();
                hourMinTo = second.split(":");
                SetTotal(hourMinFrom, hourMinTo);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }

        });



        Beckraport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RaportActivity.this, LeavingPermissionList.class);
                intent.putExtra("day", day);
                intent.putExtra("month", month);
                intent.putExtra("year", year);
                intent.putExtra("monthActual", monthActual);
                startActivity(intent);
            }
        });

    }


    boolean[] GetTakenInterval(List listFrom, List listTo , String[] items){

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

    List<String> GenerateList(List<String> listFrom, int FromHour, int FromMinutes) {
        Boolean ok = false;
        List<String> ToList = new ArrayList<>();
        String[] FromListH = new String[listFrom.size()+1];
        String[] FromListM = new String[listFrom.size()+1];
        String[] ToListH = new String[listFrom.size()+1];
        String[] ToListM = new String[listFrom.size()+1];
        int iterator;
        int index;
        for (index = 0; index < listFrom.size()  +1 ; index++) {
            String[] hourMinToListFirs = toListFirebase[index].split(":");
            String[] hourMinFromListFirst = fromListFirebase[index].split(":");
            FromListH[index] = hourMinFromListFirst[0];
            Log.d("***", FromListH[index]);
            FromListM[index] = hourMinFromListFirst[1];
            ToListH[index] = hourMinToListFirs[0];
            ToListM[index] = hourMinToListFirs[1];
        }
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
        }else if (listFrom.size() > 0){
            for (iterator = 0; iterator < listFrom.size()+1; iterator++) {
                for (index = 0; index < listFrom.size()+1 ; index++) {
                    if ((FromHour < Integer.valueOf(FromListH[index]) && ok==false) ||
                            (FromHour == Integer.valueOf(FromListH[index]) &&
                                    FromMinutes < Integer.valueOf(FromListM[index]) && ok==false) ) {
                        ok=true;
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
                            if(FromMinutes != Integer.valueOf(30) &&
                                    Integer.valueOf(FromListM[index]) == 30){
                                ToList.add(FromHour + ":" + 30);
                            }
                        }
                   }
                }
            }
        }
        return ToList;
    }
    private int indexOf(final Adapter adapter, Object value)
    {
        for (int index = 0, count = adapter.getCount(); index < count; ++index)
        {
            if (adapter.getItem(index).equals(value))
            {
                return index;
            }
        }
        return -1;
    }
    void SetTotal (String[] hourMinFrom, String[] hourMinTo){
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
        if(minResult==30){
            result = Float.valueOf(hourResult + ".5");
        }else
        {
            result = Float.valueOf(hourResult);
        }
        if ((((result + (total % 10) - TotalLpActual) > 3)||
                ((((hourResult + (total % 10) + plus - TotalLpActual)) == 3)
                        && (minfin != 0)))) {
            TotalOre.setText("Select again!");
            Confirm.setEnabled(false);
        } else {
            TotalOre.setText(hourResult + " hours and " + minResult + " minutes");
            Confirm.setEnabled(true);
            minnn = minResult;
            ora = hourResult;
        }

    }
}
