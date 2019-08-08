/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.Activity.RaportActivity;
import com.example.larisa.leavingpermissionapp.Model.LP;
import com.example.larisa.leavingpermissionapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LeavePermissionForUserAdapter extends RecyclerView.Adapter <LeavePermissionForUserAdapter.ViewHolder> {

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private Context context;
    private List<LP> lp = new ArrayList<>();
    private LayoutInflater inflater;
    private String monthActual;
    private Float total;
    private int day;
    private int month;
    private int year;


    public LeavePermissionForUserAdapter(Context context, List<LP> lp, int day, int month, int year, String monthActual) {
        this.context = context;
        this.lp = lp;
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
        final LP mylist = lp.get(i);
        viewHolder.From.setText(mylist.getFrom());
        viewHolder.To.setText(mylist.getTo());
        viewHolder.Status.setText(mylist.getStatus());
        viewHolder.Total.setText(String.valueOf(mylist.getTotal()));

    }

    //Get list size
    @Override
    public int getItemCount() {
        return lp.size();
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
            Status=  v.findViewById(R.id.textViewStatus);
            editButton = v.findViewById(R.id.editButton);
            deleteButton =  v.findViewById(R.id.deleteButton);
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
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.deleteButton:
                    int position = getAdapterPosition();
                    LP LivingPermission = lp.get(position);
                    deleteLP(position,LivingPermission);
                    break;
                case R.id.editButton:
                    position = getAdapterPosition();
                    LivingPermission = lp.get(position);

                    editLp(position,LivingPermission,v);
                    break;

            }
        }

        //delete LP
        public void deleteLP (final int id, final LP lplp){

            //create alert dialog
            alertDialogBuilder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog, null);
            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            Button noButton =  view.findViewById(R.id.noButton);
            Button yesButton =  view.findViewById(R.id.yesButton);

            //When you select No, nothing happens, the dialog closes
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            //When press Yes,delete that LP from Firebase
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    final DatabaseReference dbReference;
                    dbReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("LP").
                            child(day+ " "+  monthActual+ " "+year);
                    dbReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String[] key = new String[6];
                            int j=0;

                            if(dataSnapshot.exists()){
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    key[j]=String.valueOf(snapshot.getKey());
                                    j++;
                                }
                                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                               {
                                   if (snapshot.child("from").getValue().equals(lplp.getFrom()) && snapshot.child(
                                           "to").getValue().equals(lplp.getTo())) {
                                       snapshot.getRef().removeValue();
                                       dialog.dismiss();
                                       lp.clear();
                                       return;
                                   }
                               }
                                notifyDataSetChanged();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }

                    });
                }
            });
        }

        //Edit Lp
        //When editing an item you are redirected to report activity
        //Submit a Flag= "edit" that says you are editing and sending the necessary data from this activity
        public void editLp(final int id, final LP lp, final View v){

            final String[] key = new String[6];
            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final DatabaseReference dbReference;
            dbReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("LP").
                    child(day+ " "+  monthActual+ " "+year);
            dbReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int j=0;
                    float sum = 0;

                    if(dataSnapshot.exists()){
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                            key[j]=String.valueOf(snapshot.getKey());
                            j++;
                            String h = snapshot.child("total").getValue().toString();
                            sum = sum + Float.parseFloat(h);
                            total=sum;
                        }
                    }
                    String keyLP = key[id];
                    String Flag = "edit";
                    String LpTotal = String.valueOf(lp.getTotal());
                    Context context = v.getContext();
                    Intent intent = new Intent(context, RaportActivity.class);
                    intent.putExtra("Flag", Flag);
                    intent.putExtra("LpList", lp);
                    intent.putExtra("fromEdit", lp.getFrom());
                    intent.putExtra("toEdit", lp.getTo());
                    intent.putExtra("LpTotal", LpTotal);
                    intent.putExtra("key", keyLP);
                    intent.putExtra("total", total);
                    intent.putExtra("idLp", id);
                    intent.putExtra("idLp", id);
                    intent.putExtra("TotalLpActual", lp.getTotal());
                    intent.putExtra("day", day);
                    intent.putExtra("month", month);
                    intent.putExtra("year", year);
                    intent.putExtra("monthActual", monthActual);
                    v.getContext().startActivity(intent);


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }

            });


        }
    }
}
