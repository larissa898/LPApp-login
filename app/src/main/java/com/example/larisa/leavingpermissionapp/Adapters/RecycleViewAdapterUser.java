package com.example.larisa.leavingpermissionapp.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.larisa.leavingpermissionapp.Model.LP;
import com.example.larisa.leavingpermissionapp.R;

import java.util.ArrayList;
import java.util.List;

public class  RecycleViewAdapterUser extends RecyclerView.Adapter <RecycleViewAdapterUser.ViewHolder> {

    private AlertDialog.Builder alertDialogBuilder;
    private AlertDialog dialog;
    private Context context;
    private List<LP> lp = new ArrayList<>();
    private LayoutInflater inflater;


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
        public Button editButton;
        public Button deleteButton;



        public ViewHolder(View v, final Context ctx) {
            super(v);
            context = ctx;
            From = v.findViewById(R.id.textViewFrom);
            To = v.findViewById(R.id.textViewTo);
            Total = v.findViewById(R.id.textViewTotal);
            editButton =  v.findViewById(R.id.EditButton);
            deleteButton =  v.findViewById(R.id.deleteButton);


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
                    LivingPerm = lp.get(position);
                    deleteLP(LivingPerm);

                    break;



            }


        }
        public void deleteLP (LP id){
            //create an Alert dialog
            alertDialogBuilder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_dialog, null);

            Button noButton = (Button) view.findViewById(R.id.noButton);
            Button yesButton = (Button) view.findViewById(R.id.yesButton);

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
                    //delete the item.





                }
            });

        }

        public void editLp(final LP lp){

        }
    }
}
