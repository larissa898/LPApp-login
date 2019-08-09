/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.Activity.ItemClickListener;
import com.example.larisa.leavingpermissionapp.Model.User;
import com.example.larisa.leavingpermissionapp.R;

import java.util.ArrayList;
import java.util.List;

public class UnassignedUserForTeamLeaderAdapter extends RecyclerView.Adapter<UnassignedUserForTeamLeaderAdapter.ViewHolder> {

    private static final String TAG = "UnassignedUserForTeamLe";
    private Context context;
    private List<User> users;
    public List<User> checkedUsers = new ArrayList<>();

    public UnassignedUserForTeamLeaderAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = new ArrayList<>(users);
    }


    @Override
    public UnassignedUserForTeamLeaderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.unassigned_user_row, parent, false);
        return new ViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(UnassignedUserForTeamLeaderAdapter.ViewHolder holder, final int position) {

        final User user = users.get(position);
        holder.numeAngajat.setText(user.getLastName() + " " + user.getFirstName());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                CheckBox ck = (CheckBox) v;
                if (ck.isChecked()) {
                    checkedUsers.add(users.get(position));
                } else {
                    checkedUsers.remove(users.get(position));
                }
            }
        });
    }

    public void setList(List<User> newUsersList) {
        Log.d(TAG, "setList: newUserList = " + newUsersList);
        users.clear();
        users.addAll(newUsersList);
        Log.d(TAG, "setList: users = " + users);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView numeAngajat;
        public CheckBox checkBox;
        ItemClickListener itemClickListener;


        public ViewHolder(View v, final Context ctx) {
            super(v);
            context = ctx;
            numeAngajat = v.findViewById(R.id.numeAngajat);
            checkBox = v.findViewById(R.id.checkAngajat);
            checkBox.setOnClickListener(this);
//


        }

        public void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v, getLayoutPosition());
        }
    }
}
