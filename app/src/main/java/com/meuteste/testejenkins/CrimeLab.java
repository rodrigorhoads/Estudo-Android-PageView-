package com.meuteste.testejenkins;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.meuteste.testejenkins.database.CrimeBaseHelper;
import com.meuteste.testejenkins.database.CrimeCursorWrapper;
import com.meuteste.testejenkins.database.CrimeDbSchema;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;
//    private List<Crime> mCrimes;

    private Context mContext;
    private SQLiteDatabase mDataBase;

    public static CrimeLab get(Context context){
        if(sCrimeLab==null){
            sCrimeLab=new CrimeLab(context);
        }
        return sCrimeLab;
    }
    private  CrimeLab(Context context){
        mContext=context.getApplicationContext();
        mDataBase=new CrimeBaseHelper(context).getWritableDatabase();
//        mCrimes=new ArrayList<>();
    }

    public List<Crime> getmCrimes(){
//        return mCrimes;
        List<Crime> crimes= new ArrayList<>();
        CrimeCursorWrapper cursor= queryCrimes(null,null);
        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return crimes;
    }
    public void addCrime(Crime crime){
//        mCrimes.add(crime);
        ContentValues values= getContentValues(crime);
        mDataBase.insert(CrimeDbSchema.CrimeTable.Name,null,values);
    }

    public Crime getCrime(UUID id){
//        for(Crime crime:mCrimes){
//            if(crime.getmId().equals(id)){
//                return crime;
//            }
//        }
         CrimeCursorWrapper cursor = queryCrimes(
                 CrimeDbSchema.CrimeTable.Cols.UUID + " =?",
                new String[]{id.toString()});
         try{
             if(cursor.getCount()==0){
                 return null;
             }
             cursor.moveToFirst();
             return cursor.getCrime();
         }finally {
             cursor.close();
         }

    }


    public File getFoto(Crime crime){
        File filesDir=mContext.getFilesDir();
        return new File(filesDir,crime.getFotoFileName());
    }

    private static ContentValues getContentValues(Crime crime){
        ContentValues values = new ContentValues();
        values.put(CrimeDbSchema.CrimeTable.Cols.UUID,crime.getmId().toString());
        values.put(CrimeDbSchema.CrimeTable.Cols.TITLE,crime.getmTitle());
        values.put(CrimeDbSchema.CrimeTable.Cols.DATE,crime.getmDate().getTime());
        values.put(CrimeDbSchema.CrimeTable.Cols.SOLVED,crime.ismSolves()?1:0);
        values.put(CrimeDbSchema.CrimeTable.Cols.SUSPECT,crime.getmSuspect());
        return values;
    }

    public void updateCrime(Crime crime){
        String uuidString=crime.getmId().toString();
        ContentValues values =getContentValues(crime);
        mDataBase.update(CrimeDbSchema.CrimeTable.Name,values,
                CrimeDbSchema.CrimeTable.Cols.UUID+" =?",
                new String[]{uuidString});
    }

//    private Cursor queryCrimes(String whereClause,String[] whereArgs){
    private CrimeCursorWrapper queryCrimes(String whereClause,String[] whereArgs){
        Cursor cursor = mDataBase.query(
                CrimeDbSchema.CrimeTable.Name,
                null,//colunas -nulo seleciona todas as colunas
                whereClause,
                whereArgs,
                null,//groupBy
                null,//having
                null//orderBy
        );
        return new CrimeCursorWrapper(cursor);
    }

}
