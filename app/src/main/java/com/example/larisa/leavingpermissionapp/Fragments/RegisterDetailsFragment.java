/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.larisa.leavingpermissionapp.R;
import com.example.larisa.leavingpermissionapp.Utils.FirebaseOps;
import com.example.larisa.leavingpermissionapp.Utils.Validator;

import java.util.Objects;

public class RegisterDetailsFragment extends Fragment {

    private RegisterDetailsFragmentListener listener;
    private FirebaseOps firebaseOps = FirebaseOps.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.register_details_fragment, container, false);
        initSecondPageUI(v);
        return v;
    }

    private void initSecondPageUI(View v) {
        EditText lastName = v.findViewById(R.id.registerLastName);
        EditText firstName = v.findViewById(R.id.registerFirstName);
        EditText registrationNumber = v.findViewById(R.id.registerNumber);
        EditText phoneNumber = v.findViewById(R.id.phoneNumber);

        Spinner rolesDropdown = v.findViewById(R.id.userRole);
        rolesDropdown.setAdapter(new ArrayAdapter<>(Objects.requireNonNull(getActivity()),
                android.R.layout.simple_spinner_dropdown_item,
                firebaseOps.getRoles()));

        Button registerButton = v.findViewById(R.id.confirmRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData(phoneNumber, registrationNumber, lastName, firstName)) {
                    listener.onDetailsSent(lastName.getText(), firstName.getText(),
                            registrationNumber.getText(), phoneNumber.getText(), rolesDropdown.getSelectedItem().toString());
                }
            }
        });
    }

    public boolean validateData(EditText phoneNumber, EditText registerNumber, EditText lastName, EditText firstName) {
        return Validator.validatePhoneNumber(phoneNumber)
                && Validator.validateNonEmptyField(registerNumber)
                && Validator.validateNonEmptyField(lastName)
                && Validator.validateNonEmptyField(firstName);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegisterDetailsFragmentListener) {
            listener = (RegisterDetailsFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement RegisterDetailsFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}


