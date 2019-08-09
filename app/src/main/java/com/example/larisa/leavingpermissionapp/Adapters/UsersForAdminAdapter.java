/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.Activity.ItemClickListener;
import com.example.larisa.leavingpermissionapp.Activity.UserProfileAdminViewActivity;
import com.example.larisa.leavingpermissionapp.Model.User;
import com.example.larisa.leavingpermissionapp.R;

import java.util.ArrayList;
import java.util.List;

public class UsersForAdminAdapter extends RecyclerView.Adapter<UsersForAdminAdapter.ViewHolder> {

    private static final String TAG = "RecycleViewAdapterNoChe";
    private Context context;
    private List<User> users = new ArrayList<>();

    public UsersForAdminAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }


    @Override
    public UsersForAdminAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_user_row, parent, false);
        return new ViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(UsersForAdminAdapter.ViewHolder holder, final int position) {

        final User user = users.get(position);
        holder.numeAngajat.setText(user.getLastName() + " " + user.getFirstName());
        holder.setItemClickListener((v, pos) -> {
            Intent intent = new Intent(context, UserProfileAdminViewActivity.class);
            context.startActivity(intent);
        });


    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView numeAngajat;
        ItemClickListener itemClickListener;


        public ViewHolder(View v, final Context ctx) {
            super(v);
            context = ctx;
            numeAngajat = v.findViewById(R.id.adminNumeAngajat);
            v.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
