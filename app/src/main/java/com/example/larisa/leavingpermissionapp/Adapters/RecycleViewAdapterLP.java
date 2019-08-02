package com.example.larisa.leavingpermissionapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.HardwarePropertiesManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.Activity.ItemClickListener;
import com.example.larisa.leavingpermissionapp.Model.LP;
import com.example.larisa.leavingpermissionapp.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecycleViewAdapterLP extends RecyclerView.Adapter <RecycleViewAdapterLP.ViewHolder> {

    private Context context;
    private List<LP> lps = new ArrayList<>();
    public HashMap<String,LP> modifiedLP = new HashMap<>();


        public RecycleViewAdapterLP(Context context, List<LP> lps) {
        this.context = context;
        this.lps = lps;
    }

    @NonNull
    @Override
    public RecycleViewAdapterLP.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.lp_row, parent, false);
        return new RecycleViewAdapterLP.ViewHolder(v, context);

    }


    @Override
    public void onBindViewHolder(RecycleViewAdapterLP.ViewHolder holder, final int position) {

        LP lp = lps.get(position);
        holder.numeAngajat.setText(lp.getNume());
        holder.fromTime.setText(lp.getFrom());
        holder.toTime.setText(lp.getTo());
        holder.totalHours.setText(lp.getTotal().toString());
        holder.status.setText(lp.getStatus());
        holder.setItemClickListener((new ItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {


                modifiedLP.put(lps.get(position).getId(),lps.get(position));
            }
        }));



    }


    @Override
    public int getItemCount() {
        return lps.size();
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
                    LP LivingPerm = lps.get(position);
                    acceptLP(LivingPerm);
                    status.setTextColor(Color.GREEN);
                    statusLabel.setTextColor(Color.GREEN);
                    this.itemClickListener.onItemClick(v, getLayoutPosition());


                    break;
                case R.id.refuseButton:
                    position = getAdapterPosition();
                     LP LivingPerm2 = lps.get(position);
                    status.setTextColor(Color.RED);
                    statusLabel.setTextColor(Color.RED);

                    refuseLP(LivingPerm2);
                    this.itemClickListener.onItemClick(v, getLayoutPosition());


                    break;


            }


        }

        public void acceptLP(final LP LivingPerm) {
            LivingPerm.setStatus("confirmat");
            notifyDataSetChanged();

        }

        public void refuseLP(final LP LivingPerm) {
            LivingPerm.setStatus("refuzat");
            notifyDataSetChanged();

        }

    }
}


