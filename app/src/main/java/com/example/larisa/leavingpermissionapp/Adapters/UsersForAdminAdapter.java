/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.Activity.ItemClickListener;
import com.example.larisa.leavingpermissionapp.Activity.UserProfileAdminViewActivity;
import com.example.larisa.leavingpermissionapp.Model.User;
import com.example.larisa.leavingpermissionapp.R;

import java.util.ArrayList;
import java.util.List;

public class UsersForAdminAdapter extends RecyclerView.Adapter<UsersForAdminAdapter.ViewHolder> implements Filterable {

    private static final String TAG = "RecycleViewAdapterNoChe";
    private Context context;
    private List<User> users = new ArrayList<>();
    private List<User> usersFull = new ArrayList<>();

    public UsersForAdminAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
        usersFull = new ArrayList<>(users);

    }


    @Override
    public UsersForAdminAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_row_admin, parent, false);
        return new ViewHolder(v, context);
    }

    @Override
    public void onBindViewHolder(UsersForAdminAdapter.ViewHolder holder, final int position) {

        final User user = users.get(position);
        holder.numeAngajat.setText(user.getLastName() + " " + user.getFirstName());
        holder.setItemClickListener((v, pos) -> {
            Intent intent = new Intent(context, UserProfileAdminViewActivity.class);
            intent.putExtra("userData", user);
            context.startActivity(intent);
        });
    }



    @Override
    public int getItemCount() {
        return users.size();
    }


    @Override
    public Filter getFilter() {
        return exampleFilter;
    }

    private Filter exampleFilter = new Filter() {

        // this method will return the filteredList to the publishResults methods
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<User> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(usersFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (User user : usersFull) {
                    if (user.getFullName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(user);
                    }
                }
            }

//            Log.d(TAG, "@@@@@ performFiltering: filteredList = " + filteredList);
            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            users.clear();
            users.addAll((List) results.values);
            Log.d(TAG, "@@@@@ publishResults: filteredList = " + results.values);
            notifyDataSetChanged();
        }
    };


    public void setList(List<User> list) {
        users.clear();
        users.addAll(list);
        Log.d(TAG, "@@@@@ setList: list=" + list);


        this.notifyDataSetChanged();
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
