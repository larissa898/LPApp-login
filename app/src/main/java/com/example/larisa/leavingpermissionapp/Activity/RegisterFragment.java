/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.example.larisa.leavingpermissionapp.Model.User;
import com.example.larisa.leavingpermissionapp.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class RegisterFragment extends Fragment {

    private static final String TAG = "RegisterFragment";
    private List<User> teamLeaders = new ArrayList<>();
    private FirebaseOps firebaseOps;


    public RegisterFragment() {
        firebaseOps = FirebaseOps.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        String pageNumber = getArguments().getString("pageNumber");
        assert pageNumber != null;
        if (pageNumber.equals("0")) {
            View view = inflater.inflate(R.layout.fragment_register, container, false);
            Button nextButton = view.findViewById(R.id.next);
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RegisterViewPagerActivity.viewPager.setCurrentItem(1, true);
                }
            });
            return view;
        } else {
            View view = inflater.inflate(R.layout.fragment_register2, container, false);
            Spinner dropdown = view.findViewById(R.id.teamLeader);

            List<String> items = firebaseOps.getUsersByRole("Team Leader").stream()
                    .map(User::getFullName).collect(Collectors.toList());

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
            dropdown.setAdapter(adapter);

            return view;
        }
    }


}
