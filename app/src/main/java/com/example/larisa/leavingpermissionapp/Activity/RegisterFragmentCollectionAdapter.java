/*
 * Copyright (c) 2019. Parrot Faurecia Automotive S.A.S. All rights reserved.
 */

package com.example.larisa.leavingpermissionapp.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class RegisterFragmentCollectionAdapter extends FragmentPagerAdapter {

    public RegisterFragmentCollectionAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        RegisterFragment registerFragment = new RegisterFragment();
        Bundle bundle = new Bundle();

        bundle.putString("pageNumber", String.valueOf(i));
        registerFragment.setArguments(bundle);
        return registerFragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
