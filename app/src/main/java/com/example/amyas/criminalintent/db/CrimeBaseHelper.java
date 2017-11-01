package com.example.amyas.criminalintent.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.amyas.criminalintent.db.CrimeDbSchema.CrimeTable;

/**
 * author: amyas
 * date: 2017/10/31
 */

public class CrimeBaseHelper extends SQLiteOpenHelper {
    public static final int VERSION = 1;
    public static final String DATABASE_NAME = "crimeBase.db";

    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CrimeTable.NAME + " (" +
                "_id integer primary key autoincrement, " +
                CrimeTable.Cols.UUID + ", " + CrimeTable.Cols.TITLE + ", " +
                CrimeTable.Cols.DATE + ", " + CrimeTable.Cols.SOLVED + ", " +
                CrimeTable.Cols.SUSPECT + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
