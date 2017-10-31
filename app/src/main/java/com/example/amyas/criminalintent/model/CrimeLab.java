package com.example.amyas.criminalintent.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * author: amyas
 * date: 2017/10/26
 */

public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;

    public void addCrime(Crime crime) {
        mCrimes.add(crime);
    }

    public void remove(Crime crime) {
        mCrimes.remove(crime);
    }

    private CrimeLab(Context context) {
        mCrimes = new ArrayList<>();


    }

    public static CrimeLab get(Context context) {
        if (sCrimeLab == null) {
            sCrimeLab = new CrimeLab(context);
        }
        return sCrimeLab;
    }
    public List<Crime> getCrimes(){
        return mCrimes;
    }
    public Crime getCrime(UUID uuid){
        for (Crime crime : mCrimes) {
            if (crime.getUUID().equals(uuid)) {
                return crime;
            }
        }
        return null;
    }
}
