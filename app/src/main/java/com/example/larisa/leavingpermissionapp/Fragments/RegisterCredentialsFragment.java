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
import android.widget.Button;
import android.widget.EditText;

import com.example.larisa.leavingpermissionapp.R;
import com.example.larisa.leavingpermissionapp.Utils.Validator;

public class RegisterCredentialsFragment extends Fragment {

    private RegisterCredentialsFragmentListener listener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.register_credentials_fragment, container, false);
        initFirstPageUI(v);
        return v;
    }

    private void initFirstPageUI(View view) {
        EditText emailEditText = view.findViewById(R.id.registerEmail);
        EditText passwordEditText = view.findViewById(R.id.registerPassword);
        EditText confirmPassword = view.findViewById(R.id.confirmPassword);

        Button nextPageButton = view.findViewById(R.id.next);
        nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateData(emailEditText, passwordEditText, confirmPassword)) {
                    listener.onCredentialsSent(emailEditText.getText(), passwordEditText.getText());
                }
            }
        });
    }

    private boolean validateData(EditText emailEditText, EditText passwordEditText, EditText confirmPassword) {
        return Validator.validateEmail(emailEditText)
                && Validator.validatePasswordMatch(passwordEditText, confirmPassword);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RegisterCredentialsFragmentListener) {
            listener = (RegisterCredentialsFragmentListener) context;
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
