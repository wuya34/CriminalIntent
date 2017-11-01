package com.example.amyas.criminalintent.controller;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.amyas.criminalintent.R;
import com.example.amyas.criminalintent.model.Crime;
import com.example.amyas.criminalintent.model.CrimeLab;

/**
 * author: amyas
 * date: 2017/10/26
 */

public class CrimeListActivity extends SingleFragmentActivity
        implements CrimeListFragment.CallBack, CrimeFragment.CallBacks {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = CrimePagerActivity.newIntent(this, crime.getUUID());
            startActivityForResult(intent, CrimeListFragment.CRIME_PAGER_CODE);
        } else {
            CrimeFragment fragment = CrimeFragment.newInstance(crime.getUUID());
            FragmentManager manager = getSupportFragmentManager();
            manager.beginTransaction().replace(R.id.detail_fragment_container, fragment).commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment fragment = (CrimeListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        fragment.updateUI();
    }

    @Override
    public void onDetachFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        //        manager.beginTransaction().detach(fragment).commit();
        //        manager.beginTransaction().remove(fragment).commit();
        Crime crime = CrimeLab.get(this).getFirstCrime();
        CrimeFragment firstFragment = CrimeFragment.newInstance(crime.getUUID());
        manager.beginTransaction().replace(R.id.detail_fragment_container, firstFragment).commit();
    }
}
