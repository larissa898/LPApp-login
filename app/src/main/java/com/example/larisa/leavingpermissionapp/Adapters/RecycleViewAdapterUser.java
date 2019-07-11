package com.example.larisa.leavingpermissionapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.Activity.CalendarActivity;
import com.example.larisa.leavingpermissionapp.Activity.FinalCalendar;
import com.example.larisa.leavingpermissionapp.Activity.LeavingPermissionList;
import com.example.larisa.leavingpermissionapp.Activity.RaportActivity;
import com.example.larisa.leavingpermissionapp.Activity.ViewTeam;
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
import java.util.Map;

public class  RecycleViewAdapterUser extends RecyclerView.Adapter <RecycleViewAdapterUser.ViewHolder> {

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private Context context;
    private List<LP> lp = new ArrayList<>();
    private LayoutInflater inflater;
    private int day;
    private int month;
    private int year;
    String[] strMonths = {"January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"};

    public RecycleViewAdapterUser(Context context, List<LP> lp, int day, int month, int year) {
        this.context = context;
        this.lp = lp;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    @NonNull
    @Override
    public RecycleViewAdapterUser.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_lp, parent, false);
        return new RecycleViewAdapterUser.ViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapterUser.ViewHolder viewHolder, int i) {
        final LP mylist = lp.get(i);
        viewHolder.From.setText(mylist.getFrom());
        viewHolder.To.setText(mylist.getTo());
        viewHolder.Status.setText(mylist.getStatus());
        viewHolder.Total.setText(String.valueOf(mylist.getTotal()));
    }

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
            editButton =  v.findViewById(R.id.EditButton);
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
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.EditButton:
                    int position = getAdapterPosition();
                    LP LivingPerm = lp.get(position);
                    editLp(LivingPerm);

                    break;
                case R.id.deleteButton:
                    position = getAdapterPosition();
                    LP LivingPermission = lp.get(position);
                    deleteLP(position,LivingPermission);
                    break;
            }
        }

        public void deleteLP (final int id, final LP lplp){
            alertDialogBuilder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog, null);
            Button noButton =  view.findViewById(R.id.noButton);
            Button yesButton =  view.findViewById(R.id.yesButton);
            alertDialogBuilder.setView(view);
            dialog = alertDialogBuilder.create();
            dialog.show();

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    final DatabaseReference dbReference;
                   // Log.d("eeeeee", day+ " "+ strMonths[month ]+ " "+year);
                    dbReference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid()).child("LP").
                            child(day+ " "+ strMonths[month] + " "+year);
                    dbReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                            lp.clear();
//                            Log.d("@@@", String.valueOf(id));
                            String[] key = new String[6];
                            int j=0;

                            if(dataSnapshot.exists()){
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                    key[j]=String.valueOf(snapshot.getKey());
//                                    Log.d("!!!", String.valueOf(snapshot.getKey()));
                                    j++;
                                }

                                int i=0;
                                //int k=0;

                                for(DataSnapshot snapshot : dataSnapshot.getChildren())
                               {

                                   if (snapshot.child("from").getValue().equals(lplp.getFrom()) && snapshot.child(
                                           "to").getValue().equals(lplp.getTo())) {

                                       Log.i(RecycleViewAdapterUser.class.getSimpleName(),
                                               "Value: " + snapshot.getValue());
                                       Log.i(RecycleViewAdapterUser.class.getSimpleName(), "Key: " + snapshot.getKey());
                                       snapshot.getRef().removeValue();
                                       dialog.dismiss();
                                       Log.i(RecycleViewAdapterUser.class.getSimpleName(), "List Size1: " + lp.size());
                                       lp.remove(id);
                                       Log.i(RecycleViewAdapterUser.class.getSimpleName(), "List Size2: " + lp.size());
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

        public void editLp(final LP lp){

        }
    }
}
