package com.example.larisa.leavingpermissionapp.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.larisa.leavingpermissionapp.Model.LP;
import com.example.larisa.leavingpermissionapp.R;

import java.util.ArrayList;
import java.util.List;

public class  RecycleViewAdapterUser extends RecyclerView.Adapter <RecycleViewAdapter.ViewHolder> {

    private Context context;
    private List<LP> lp = new ArrayList<>();

    public RecycleViewAdapterUser(Context context, List<LP> lp)
    {
        this.context = context;
        this.lp = lp;
    }
    @NonNull
    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_lp, parent, false);
        return new RecycleViewAdapter.ViewHolder(v, context) ;
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter.ViewHolder holder, int position) {
        LP mylist = lp.get(position);


    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
