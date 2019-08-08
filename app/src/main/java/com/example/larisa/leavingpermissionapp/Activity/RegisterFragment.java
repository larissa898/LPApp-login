/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.larisa.leavingpermissionapp.Model.User;
import com.example.larisa.leavingpermissionapp.R;
import com.example.larisa.leavingpermissionapp.Utils.ExternalStoragePermission;
import com.example.larisa.leavingpermissionapp.Utils.FirebaseOps;
import com.example.larisa.leavingpermissionapp.Utils.Validator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class RegisterFragment extends Fragment {

    private static final String TAG = "RegisterFragment";
    private static String email;
    private static String password;

    private FirebaseOps firebaseOps;

    public RegisterFragment() {
        firebaseOps = FirebaseOps.getInstance();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String pageNumber = getArguments().getString("pageNumber");
        assert pageNumber != null;
        if (pageNumber.equals("0")) {
            View view = inflater.inflate(R.layout.fragment_register, container, false);
            initFirstPageUI(view);
            return view;
        } else {
            View view = inflater.inflate(R.layout.fragment_register2, container, false);
            initSecondPageUI(view);
            Spinner rolesDropdown = view.findViewById(R.id.userRole);
            List<String> roles = firebaseOps.getRoles();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, roles);
            rolesDropdown.setAdapter(adapter);
            return view;
        }
    }

    private void initSecondPageUI(View view) {
        EditText lastName = view.findViewById(R.id.registerLastName);
        EditText firstName = view.findViewById(R.id.registerFirstName);
        EditText registerNumber = view.findViewById(R.id.registerNumber);
        EditText phoneNumber = view.findViewById(R.id.phoneNumber);
        Spinner rolesDropdown = view.findViewById(R.id.userRole);

        Button registerButton = view.findViewById(R.id.confirmRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateSecondFragmentData(phoneNumber, registerNumber, lastName, firstName)
                        && ExternalStoragePermission.verifyStoragePermissions(getActivity())) {

                    Log.d(TAG, "onClick: RESTORED" + email + " " + password);
                    firebaseOps.createUser(email, password);

                    User newUser = new User(lastName.getText().toString() + " " + firstName.getText().toString(),
                            rolesDropdown.getSelectedItem().toString(), phoneNumber.getText().toString(), registerNumber.getText().toString());

                    String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
                    File file = new File(dirPath, "user.txt");
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(file);
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                        objectOutputStream.writeObject(newUser);
                        objectOutputStream.close();
                        fileOutputStream.flush();
                        fileOutputStream.close();

                        getActivity().setResult(RESULT_OK);
                        getActivity().finish();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void initFirstPageUI(View view) {
        EditText emailEditText = view.findViewById(R.id.registerEmail);
        EditText passwordEditText = view.findViewById(R.id.registerPassword);
        EditText confirmPassword = view.findViewById(R.id.confirmPassword);

        Button nextPageButton = view.findViewById(R.id.next);
        email = emailEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();
        nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFirstFragmentData(emailEditText, passwordEditText, confirmPassword)) {
                    email = emailEditText.getText().toString().trim();
                    password = passwordEditText.getText().toString().trim();
                    RegisterViewPagerActivity.viewPager.setCurrentItem(1, true);
                }
            }
        });

    }

    public boolean validateFirstFragmentData(EditText emailEditText, EditText passwordEditText, EditText confirmPassword) {
        return Validator.validateEmail(emailEditText)
                && Validator.validatePasswordMatch(passwordEditText, confirmPassword);
    }

    public boolean validateSecondFragmentData(EditText phoneNumber, EditText registerNumber, EditText lastName, EditText firstName) {
        return Validator.validatePhoneNumber(phoneNumber)
                && Validator.validateNonEmptyField(registerNumber)
                && Validator.validateNonEmptyField(lastName)
                && Validator.validateNonEmptyField(firstName);

    }

}
