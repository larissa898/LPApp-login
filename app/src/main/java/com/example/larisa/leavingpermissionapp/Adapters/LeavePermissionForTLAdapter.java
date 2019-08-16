/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.Activity.ItemClickListener;
import com.example.larisa.leavingpermissionapp.Model.LeavingPermission;
import com.example.larisa.leavingpermissionapp.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LeavePermissionForTLAdapter extends RecyclerView.Adapter<LeavePermissionForTLAdapter.ViewHolder> {

    private Context context;
    private List<LeavingPermission> leavingPermissions = new ArrayList<>();
    public HashMap<String, LeavingPermission> modifiedLP = new HashMap<>();


    public LeavePermissionForTLAdapter(Context context, List<LeavingPermission> leavingPermissions) {
        this.context = context;
        this.leavingPermissions = leavingPermissions;
    }

    @NonNull
    @Override
    public LeavePermissionForTLAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lp_row, parent, false);
        return new LeavePermissionForTLAdapter.ViewHolder(v, context);

    }


    @Override
    public void onBindViewHolder(LeavePermissionForTLAdapter.ViewHolder holder, final int position) {

        LeavingPermission leavingPermission = leavingPermissions.get(position);
        holder.numeAngajat.setText(leavingPermission.getNume());
        holder.fromTime.setText(leavingPermission.getFrom());
        holder.toTime.setText(leavingPermission.getTo());
        holder.totalHours.setText(leavingPermission.getTotal().toString());
        holder.status.setText(leavingPermission.getStatus());
        holder.setItemClickListener((new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {


                modifiedLP.put(leavingPermissions.get(position).getId(), leavingPermissions.get(position));
            }
        }));
    }


    @Override
    public int getItemCount() {
        return leavingPermissions.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView numeAngajatLabel;
        public TextView fromTimeLabel;
        public TextView toTimeLabel;
        public TextView totalHoursLabel;
        public TextView numeAngajat;
        public TextView fromTime;
        public TextView toTime;
        public TextView totalHours;
        public Button acceptButton;
        public Button refuseButton;
        public TextView statusLabel;
        public TextView status;

        ItemClickListener itemClickListener;

        public void setItemClickListener(ItemClickListener ic) {
            this.itemClickListener = ic;
        }

        public ViewHolder(View v, final Context ctx) {
            super(v);
            context = ctx;
            numeAngajatLabel = v.findViewById(R.id.numeAngajat);
            fromTimeLabel = v.findViewById(R.id.fromLP);
            toTimeLabel = v.findViewById(R.id.toLP);
            totalHoursLabel = v.findViewById(R.id.totalOre);
            numeAngajat = v.findViewById(R.id.textViewNumeEdit);
            fromTime = v.findViewById(R.id.textViewFromLP);
            toTime = v.findViewById(R.id.textViewToLP);
            totalHours = v.findViewById(R.id.textViewTotal);
            acceptButton = v.findViewById(R.id.acceptButton);
            refuseButton = v.findViewById(R.id.refuseButton);
            statusLabel = v.findViewById(R.id.statusLP);
            status = v.findViewById(R.id.textViewStatus);


            acceptButton.setOnClickListener(this);
            refuseButton.setOnClickListener(this);


        }


        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.acceptButton:
                    int position = getAdapterPosition();
                    LeavingPermission LivingPerm = leavingPermissions.get(position);
                    acceptLP(LivingPerm);
                    status.setTextColor(Color.GREEN);
                    statusLabel.setTextColor(Color.GREEN);
                    this.itemClickListener.onItemClick(v, getLayoutPosition());


                    break;
                case R.id.refuseButton:
                    position = getAdapterPosition();
                    LeavingPermission LivingPerm2 = leavingPermissions.get(position);
                    refuseLP(LivingPerm2);
                    status.setTextColor(Color.RED);
                    statusLabel.setTextColor(Color.RED);
                    this.itemClickListener.onItemClick(v, getLayoutPosition());


                    break;


            }


        }

        public void acceptLP(final LeavingPermission LivingPerm) {
            LivingPerm.setStatus("confirmat");
            notifyDataSetChanged();


        }

        public void refuseLP(final LeavingPermission LivingPerm) {
            LivingPerm.setStatus("refuzat");
            notifyDataSetChanged();

        }

    }
}


