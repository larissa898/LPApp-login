/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Fragments;

public interface RegisterDetailsFragmentListener {
    void onDetailsSent(CharSequence lastName, CharSequence firstName,
                       CharSequence registrationNumber, CharSequence phoneNumber, CharSequence role);
}