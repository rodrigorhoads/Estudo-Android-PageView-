package com.meuteste.testejenkins.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CrimeBaseHelper extends SQLiteOpenHelper {
    public static final int VERSION=1;
    public static final String DATABASE_NAME="crimeBase.db";

    public CrimeBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+ CrimeDbSchema.CrimeTable.Name+"("+
                CrimeDbSchema.CrimeTable.Cols.UUID+", "+
                CrimeDbSchema.CrimeTable.Cols.TITLE+", "
                + CrimeDbSchema.CrimeTable.Cols.DATE+", "
                + CrimeDbSchema.CrimeTable.Cols.SOLVED+", "
                + CrimeDbSchema.CrimeTable.Cols.SUSPECT+")" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
