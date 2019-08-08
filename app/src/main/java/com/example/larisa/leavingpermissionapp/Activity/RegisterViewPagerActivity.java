/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.larisa.leavingpermissionapp.R;

public class RegisterViewPagerActivity extends AppCompatActivity {

    public static SwipeDisabledViewPager viewPager;
    RegisterFragmentCollectionAdapter registerFragmentCollectionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_view_pager);
        viewPager = findViewById(R.id.view_pager);
        registerFragmentCollectionAdapter = new RegisterFragmentCollectionAdapter(getSupportFragmentManager());
        viewPager.setAdapter(registerFragmentCollectionAdapter);
    }


}
