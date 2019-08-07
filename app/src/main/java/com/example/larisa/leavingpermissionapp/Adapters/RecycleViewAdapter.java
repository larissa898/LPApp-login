/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
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

public class RecycleViewAdapter extends RecyclerView.Adapter <RecycleViewAdapter.ViewHolder> {

    private Context context;
    private List<User> users = new ArrayList<>();
    public List<User> checkedUsers = new ArrayList<>();

    public RecycleViewAdapter(Context context, List<User> users)
    {
        this.context = context;
        this.users = users;
    }



    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_row, parent, false);
        return new ViewHolder(v, context) ;
    }

    @Override
    public void onBindViewHolder(RecycleViewAdapter.ViewHolder holder, final int position) {

        final User user = users.get(position);
        holder.numeAngajat.setText(user.getFullName());
        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                CheckBox ck = (CheckBox) v;
                if(ck.isChecked())

                {
                  checkedUsers.add(users.get(position));
                }
                else
                {
                 checkedUsers.remove(users.get(position));
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        public TextView numeAngajat;
        public CheckBox checkBox;
        ItemClickListener itemClickListener;


        public ViewHolder(View v, final Context ctx)
        {
            super(v);
            context = ctx;
            numeAngajat = v.findViewById(R.id.numeAngajat);
            checkBox = v.findViewById(R.id.checkAngajat);
            checkBox.setOnClickListener(this);
//



        }
        public  void setItemClickListener(ItemClickListener ic)
        {
            this.itemClickListener  = ic;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(v, getLayoutPosition());
        }
    }
}
