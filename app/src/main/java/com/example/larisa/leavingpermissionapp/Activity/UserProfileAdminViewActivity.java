/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.larisa.leavingpermissionapp.Model.User;
import com.example.larisa.leavingpermissionapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserProfileAdminViewActivity extends AppCompatActivity {

    private static final String TAG = "UserProfileActivity";
    private final int SIGNATURE_REQUEST_CODE = 100;

    // UI
    private EditText userFirstNameEditText;
    private EditText userLastNameEditText;
    private EditText userRoleEditText;
    private EditText userPhoneNumberEditText;
    private EditText userRegistrationNumberEditText;
    private EditText userTeamLeaderEditText;
    private Button editSaveUserButton;
    private ImageView deleteUser;

    // Firebase
    private DatabaseReference usersRef;

    // Vars
    private User user;

    private boolean inEditMode = false;


    private void initUI() {
        userFirstNameEditText = findViewById(R.id.adminUserFirstNameTV);
        userLastNameEditText = findViewById(R.id.adminUserLastNameTV);
        userRoleEditText = findViewById(R.id.adminUserFunctieTV);
        userPhoneNumberEditText = findViewById(R.id.adminUserPhoneTV);
        userRegistrationNumberEditText = findViewById(R.id.nr_matricol_admin);
        userTeamLeaderEditText = findViewById(R.id.admin_team_leader_user_profile_TV);
        editSaveUserButton = findViewById(R.id.adminEditUser);
        deleteUser = findViewById(R.id.deleteUserButton);

        disableEditing(userFirstNameEditText);
        disableEditing(userLastNameEditText);
        disableEditing(userRoleEditText);
        disableEditing(userPhoneNumberEditText);
        disableEditing(userRegistrationNumberEditText);
        disableEditing(userTeamLeaderEditText);
        setOnEditorActionListenerForEditText(userFirstNameEditText);
        setOnEditorActionListenerForEditText(userLastNameEditText);
        setOnEditorActionListenerForEditText(userRoleEditText);
        setOnEditorActionListenerForEditText(userPhoneNumberEditText);
        setOnEditorActionListenerForEditText(userRegistrationNumberEditText);
        setOnEditorActionListenerForEditText(userTeamLeaderEditText);


        editSaveUserButton.setOnClickListener(view -> {
            if (!inEditMode) {
                enableEditing(userFirstNameEditText);
                enableEditing(userLastNameEditText);

                enableEditing(userRoleEditText);
                enableEditing(userPhoneNumberEditText);
                enableEditing(userRegistrationNumberEditText);
                enableEditing(userTeamLeaderEditText);
                inEditMode = true;
                editSaveUserButton.setText("Save");
                userRegistrationNumberEditText.requestFocus();
            } else {
                disableEditing(userFirstNameEditText);
                disableEditing(userLastNameEditText);
                disableEditing(userRoleEditText);
                disableEditing(userPhoneNumberEditText);
                disableEditing(userRegistrationNumberEditText);
                disableEditing(userTeamLeaderEditText);
                inEditMode = false;
                changeValue("firstName", userFirstNameEditText.getText().toString());
                changeValue("lastName", userLastNameEditText.getText().toString());
                changeValue("role", userRoleEditText.getText().toString());
                changeValue("phoneNumber", userPhoneNumberEditText.getText().toString());
                changeValue("registrationNumber", userRegistrationNumberEditText.getText().toString());
                changeValue("teamLeader", userTeamLeaderEditText.getText().toString());
                editSaveUserButton.setText("Edit User");
            }
        });

        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteUser();
                finish();
            }
        });
    }

    public void setOnEditorActionListenerForEditText(EditText editText) {
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_NEXT
                    || actionId == EditorInfo.IME_ACTION_DONE
                    || event.getAction() == KeyEvent.ACTION_DOWN
                    || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard();
            }
            return false;
        });

    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void enableEditing(EditText editText) {
        editText.setKeyListener((KeyListener) editText.getTag());
    }

    private void disableEditing(EditText editText) {
        editText.setTag(editText.getKeyListener());
        editText.setKeyListener(null);
    }

    private void changeValue(String field, String value) {
        usersRef.child(user.getId()).child(field).setValue(value);
    }

    private void deleteUser(){
        usersRef.child(user.getId()).removeValue();
    }

    private void getUserData() {

        user = (User) getIntent().getExtras().getSerializable("userData");
        Log.d(TAG, "getUserData: andieprst" + user);
        userFirstNameEditText.setText(user.getFirstName());
        userLastNameEditText.setText(user.getLastName());
        userRoleEditText.setText(user.getRole());
        userRegistrationNumberEditText.setText(user.getRegistrationNumber());
        userPhoneNumberEditText.setText(user.getPhoneNumber());
        userTeamLeaderEditText.setText(user.getTeamLeader());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_admin_view);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usersRef = FirebaseDatabase.getInstance().getReference("Users");

        initUI();
        getUserData();

    }
}
