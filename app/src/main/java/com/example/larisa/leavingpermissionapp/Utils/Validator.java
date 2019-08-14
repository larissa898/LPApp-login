/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Utils;

import android.util.Patterns;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    public static boolean validateEmail(EditText editText) {
        final String email = editText.getText().toString().trim();
        if (!validateNonEmptyField(editText)
                || (!Patterns.EMAIL_ADDRESS.matcher(email).matches())) {
            editText.setError("Please enter a valid email");
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean validatePasswordMatch(EditText editText, EditText editText1) {
        final String password = editText.getText().toString().trim();
        final String confirmPassword = editText1.getText().toString().trim();
        if (validateNonEmptyField(editText) && validateNonEmptyField(editText1)) {
            Pattern pattern;
            Matcher matcher;
            final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";
            pattern = Pattern.compile(PASSWORD_PATTERN);
            matcher = pattern.matcher(password);
            if (!matcher.matches()) {
                editText.setError("You must choose a password that contains at least one number, one uppercase and " +
                        "one special character");
                editText.requestFocus();
                return false;
            }

            if (!confirmPassword.equals(password)) {
                editText1.setError("The passwords do not match!Please try again");
                editText1.requestFocus();
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    public static boolean validatePhoneNumber(EditText editText) {
        final String phoneNumber = editText.getText().toString().trim();

        if (!validateNonEmptyField(editText)
                || (phoneNumber.length() != 9 && phoneNumber.length() != 10 && phoneNumber.length() != 13) ||
                (phoneNumber.length() == 9 && !phoneNumber.startsWith("7")) ||
                (phoneNumber.length() == 10 && !phoneNumber.startsWith("07")) ||
                (phoneNumber.length() == 13 && !phoneNumber.startsWith("00407"))) {
            editText.setError("The phone number must have at least 10 digits");
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static boolean validateNonEmptyField(EditText editText) {
        final String field = editText.getText().toString().trim();
        if (field.isEmpty()) {
            editText.setError("This field cannot be empty!");
            editText.requestFocus();
            return false;
        }
        return true;
    }
}
