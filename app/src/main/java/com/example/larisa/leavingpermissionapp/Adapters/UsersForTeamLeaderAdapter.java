/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.larisa.leavingpermissionapp.Activity.ItemClickListener;
import com.example.larisa.leavingpermissionapp.Activity.TeamLeaderViewTeam;
import com.example.larisa.leavingpermissionapp.Model.User;
import com.example.larisa.leavingpermissionapp.R;
import com.example.larisa.leavingpermissionapp.Utils.FirebaseOps;

import java.util.ArrayList;
import java.util.List;

public class UsersForTeamLeaderAdapter extends RecyclerView.Adapter<UsersForTeamLeaderAdapter.ViewHolder> {

    private static final String TAG = "UsersForTeamLeaderAdapt";
    public List<User> checkedUsers = new ArrayList<>();
    private Context context;
    private List<User> users;
    private boolean isSelectedAll;

    public UsersForTeamLeaderAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }


    @Override
    public UsersForTeamLeaderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row, parent, false);
        return new ViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(UsersForTeamLeaderAdapter.ViewHolder holder, final int position) {

        final User user = users.get(position);
        holder.numeAngajat.setText(user.getLastName() + " " + user.getFirstName());

        if (!isSelectedAll) {
            Log.d(TAG, "onBindViewHolder: isSelectedAll = false");
            holder.checkBox.setChecked(false);
        } else {
            Log.d(TAG, "onBindViewHolder: isSelectedAll = true");
            holder.checkBox.setChecked(true);
        }

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("Are you sure you want to remove this member from your team?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                FirebaseOps.getInstance().setTeamLeader(user.getRegistrationNumber(), null);
                                dialogInterface.dismiss();
                                Toast.makeText(v.getContext(), "User has been removed from the team", Toast.LENGTH_SHORT);


                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                builder.create();
                builder.show();
            }
        });

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                        checkedUsers.add(users.get(position));
                    } else {
                        checkedUsers.remove(users.get(position));
//                    ((TeamLeaderViewTeam)context).getCheckAllCB().setChecked(false);
                    }
            }
        });

    }

    public void setList(List<User> newUsersList) {
        users = newUsersList;
    }

    public void selectAll() {
        isSelectedAll = true;
        notifyDataSetChanged();
    }

    public void unselectall() {
        isSelectedAll = false;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView numeAngajat;
        public CheckBox checkBox;
        ItemClickListener itemClickListener;
        ImageView deleteButton;


        public ViewHolder(View v, final Context ctx) {
            super(v);
            context = ctx;
            numeAngajat = v.findViewById(R.id.numeAngajat);
            checkBox = v.findViewById(R.id.checkAngajat);
            deleteButton = v.findViewById(R.id.deleteTeamMember);


        }

    }
}




