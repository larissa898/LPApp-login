package com.example.larisa.leavingpermissionapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.Model.LP;
import com.example.larisa.leavingpermissionapp.R;

import java.util.ArrayList;
import java.util.List;

public class  RecycleViewAdapterUser extends RecyclerView.Adapter <RecycleViewAdapterUser.ViewHolder> {

    private Context context;
    private List<LP> lp = new ArrayList<>();

    public RecycleViewAdapterUser(Context context, List<LP> lp) {
        this.context = context;
        this.lp = lp;
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
        viewHolder.Total.setText(String.valueOf(mylist.getTotal()));


    }

    @Override
    public int getItemCount() {
        return lp.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView From;
        public TextView To;
        public TextView Total;


        public ViewHolder(View v, final Context ctx) {
            super(v);
            context = ctx;
            From = v.findViewById(R.id.textViewFrom);
            To = v.findViewById(R.id.textViewTo);
            Total = v.findViewById(R.id.textViewTotal);

        }




        public void onClick(View v) {


        }
    }
}
