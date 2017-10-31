package com.example.amyas.criminalintent.model;

import java.util.Date;
import java.util.UUID;

/**
 * author: amyas
 * date: 2017/10/26
 */

public class Crime {
    private UUID mUUID;
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    public Crime(){
        this(UUID.randomUUID());
    }

    public Crime(UUID UUID) {
        mUUID = UUID;
        mDate = new Date();
    }

    public UUID getUUID() {
        return mUUID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

    @Override
    public String toString() {
        return "Crime{" +
                "mUUID=" + mUUID +
                ", mTitle='" + mTitle + '\'' +
                ", mDate=" + mDate +
                ", mSolved=" + mSolved +
                '}';
    }
}
