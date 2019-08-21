/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.Activity.RaportActivity;
import com.example.larisa.leavingpermissionapp.Model.LeavingPermission;
import com.example.larisa.leavingpermissionapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LeavePermissionForUserAdapter extends RecyclerView.Adapter<LeavePermissionForUserAdapter.ViewHolder> {

    private static final String TAG = "LeavePermissionForUserA";

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private Context context;
    private List<LeavingPermission> permissionArrayList = new ArrayList<>();
    private LayoutInflater inflater;
    private String monthActual;
    private Float total;
    private int day;
    private int month;
    private int year;

    String[] strMonths = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};


    public LeavePermissionForUserAdapter(Context context, List<LeavingPermission> permissionArrayList, int day, int month, int year, String monthActual) {
        this.context = context;
        this.permissionArrayList = permissionArrayList;
        this.day = day;
        this.month = month;
        this.year = year;
        this.monthActual = monthActual;
    }

    @NonNull
    @Override
    public LeavePermissionForUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_lp, parent, false);
        return new LeavePermissionForUserAdapter.ViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(@NonNull LeavePermissionForUserAdapter.ViewHolder viewHolder, int i) {
        final LeavingPermission mylist = permissionArrayList.get(i);
        viewHolder.From.setText(mylist.getFrom());
        viewHolder.To.setText(mylist.getTo());
        viewHolder.Status.setText(mylist.getStatus());
        viewHolder.Total.setText(String.valueOf(mylist.getTotal()));

    }

    //Get list size
    @Override
    public int getItemCount() {
        return permissionArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView From;
        public TextView Status;
        public TextView To;
        public TextView Total;
        public Button editButton;
        public Button deleteButton;

        public ViewHolder(View v, final Context ctx) {
            super(v);
            context = ctx;
            From = v.findViewById(R.id.textViewFrom);
            To = v.findViewById(R.id.textViewTo);
            Total = v.findViewById(R.id.textViewTotal);
            Status = v.findViewById(R.id.textViewStatus);
            editButton = v.findViewById(R.id.editButton);
            deleteButton = v.findViewById(R.id.deleteButton);
            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                }
            });
        }

        //When you click edit or delete
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.deleteButton:
                    int position = getAdapterPosition();
                    LeavingPermission leavingPermission = LeavePermissionForUserAdapter.this.permissionArrayList.get(position);
                    deleteLP(position, leavingPermission);
                    break;
                case R.id.editButton:
                    position = getAdapterPosition();
                    leavingPermission = LeavePermissionForUserAdapter.this.permissionArrayList.get(position);

                    editLp(position, leavingPermission, view);
                    break;

            }
        }

        //delete LeavingPermission
        public void deleteLP(final int id, final LeavingPermission lplp) {

            //create alert dialog
            alertDialogBuilder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog, null);
            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            Button noButton = view.findViewById(R.id.noButton);
            Button yesButton = view.findViewById(R.id.yesButton);

            //When you select No, nothing happens, the dialog closes
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            //When press Yes,delete that LeavingPermission from Firebase
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: monthactual ===" + monthActual);
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users")
                            .child(user.getUid())
                            .child("LeavingPermission")
                            .child(day + " " + strMonths[month - 1] + " " + year)
                            .child(lplp.getId());


                    dbReference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog.dismiss();
                            notifyDataSetChanged();
                        }
                    })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: e = " + e.getMessage() + "\n" + e.getLocalizedMessage());
                                }
                            });


                }
            });
        }

        //Edit Lp
        //When editing an item you are redirected to report activity
        //Submit a Flag = "edit" that says you are editing and sending the necessary data from this activity
        public void editLp(final int id, final LeavingPermission leavingPermission, final View view) {

            String[] key = new String[6];
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            DatabaseReference dbReference = FirebaseDatabase.getInstance().getReference("Users")
                    .child(user.getUid())
                    .child("LeavingPermission")
                    .child(day + " " + strMonths[month - 1] + " " + year);


            dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int j = 0;
                    float sum = 0;

                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            key[j] = String.valueOf(snapshot.getKey());
                            j++;
                            String h = snapshot.child("total").getValue().toString();
                            sum = sum + Float.parseFloat(h);
                            total = sum;
                        }
                    }
                    String keyLP = key[id];
                    String Flag = "edit";
                    String LpTotal = String.valueOf(leavingPermission.getTotal());
                    Context context = view.getContext();
                    Intent intent = new Intent(context, RaportActivity.class);
                    intent.putExtra("Flag", Flag);
                    intent.putExtra("LpList", leavingPermission);
                    intent.putExtra("fromEdit", leavingPermission.getFrom());
                    intent.putExtra("toEdit", leavingPermission.getTo());
                    intent.putExtra("LpTotal", LpTotal);
                    intent.putExtra("key", keyLP);      //TODO: never used. delete ?
                    intent.putExtra("total", total);
                    intent.putExtra("idLp", leavingPermission.getId());
                    intent.putExtra("TotalLpActual", leavingPermission.getTotal());
                    intent.putExtra("day", day);
                    intent.putExtra("month", month);
                    intent.putExtra("year", year);
                    intent.putExtra("monthActual", monthActual);
                    view.getContext().startActivity(intent);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.d(TAG, "onCancelled: error: "
                            + "\n" + databaseError.getMessage()
                            + "\n" + databaseError.getDetails()
                            + "\n" + databaseError.getCode());
                }

            });


        }
    }
}
