package com.example.larisa.leavingpermissionapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.example.larisa.leavingpermissionapp.Activity.UserFrom;
import com.example.larisa.leavingpermissionapp.R;
import com.example.larisa.leavingpermissionapp.Model.User;
import java.util.ArrayList;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter <RecycleViewAdapter.ViewHolder> {

    private Context context;
    private List<User> users = new ArrayList<>();

    public RecycleViewAdapter(Context context, List<User> users)
    {
        this.context = context;
        this.users = users;
    }



    @Override
    public RecycleViewAdapter.ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.team_row, parent, false);
        return new ViewHolder(v, context) ;
    }

    @Override
    public void onBindViewHolder( RecycleViewAdapter.ViewHolder holder, int position) {

        User user = users.get(position);
        holder.numeAngajat.setText(user.getNume() +  "" + user.getPrenume());

    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder
    {

        public EditText numeAngajat;
        public CheckBox checkBox;
        public Button addEmployee;

        public ViewHolder(View v, final Context ctx)
        {
            super(v);
            context = ctx;
            numeAngajat = v.findViewById(R.id.numeAngajat);
            checkBox = v.findViewById(R.id.checkAngajat);
            addEmployee = v.findViewById(R.id.Add_new_employee);

           /* addEmployee.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ctx, UserFrom.class);
                    ctx.startActivity(intent);

                }
            });*/




        }
    }
}
