package com.example.amyas.criminalintent.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.amyas.criminalintent.R;
import com.example.amyas.criminalintent.model.Crime;
import com.example.amyas.criminalintent.model.CrimeLab;

import java.util.List;
import java.util.UUID;

/**
 * author: amyas
 * date: 2017/10/30
 */

public class CrimePagerActivity extends AppCompatActivity {
    private static final String EXTRA_CRIME_ID =
            "com.amyas.criminalintent.crime_id";
    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    //    private Button jumpToFirst;
    //    private Button jumpToLast;

    public static Intent newIntent(Context packageContext, UUID uuid) {
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, uuid);
        return intent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);

        UUID crime_id = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);

        //        jumpToFirst = (Button) findViewById(R.id.crime_jump_to_first);
        //        jumpToLast = (Button) findViewById(R.id.crime_jump_to_last);
        //        jumpToFirst.setOnClickListener(this);
        //        jumpToLast.setOnClickListener(this);

        mCrimes = CrimeLab.get(this).getCrimes();
        FragmentManager manager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(manager) {
            @Override
            public Fragment getItem(int position) {
                return CrimeFragment.newInstance(mCrimes.get(position).getUUID());

            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getUUID().equals(crime_id)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
        //        mViewPager.setCurrentItem(crime_id);


    }

    //    @Override
    //    public void onClick(View view) {
    //        switch (view.getId()){
    //            case R.id.crime_jump_to_first:
    //                mViewPager.setCurrentItem(0);
    //                jumpToFirst.setEnabled(false);
    //            case R.id.crime_jump_to_last:
    //                mViewPager.setCurrentItem(mCrimes.size()-1);
    //                jumpToLast.setEnabled(false);
    //        }
    //    }
}
