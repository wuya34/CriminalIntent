package com.example.amyas.criminalintent.controller;

import android.support.v4.app.Fragment;

/**
 * author: amyas
 * date: 2017/10/26
 */

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
