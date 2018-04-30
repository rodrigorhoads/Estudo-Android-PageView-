package com.example.msn_r.bignerdranch;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {
    private static CrimeLab sCrimeLab;

    private List<Crime> mCrimes;

    public static CrimeLab get(Context context){
        if(sCrimeLab==null){
            sCrimeLab=new CrimeLab(context);
        }
        return sCrimeLab;
    }
    private  CrimeLab(Context contect){
        mCrimes=new ArrayList<>();
        for(int i=0;i<100;i++){
            Crime crime = new Crime();
            crime.setmTitle("Crime #"+i);
            crime.setmSolves(i%2==0);
            mCrimes.add(crime);
        }
    }

    public List<Crime> getmCrimes(){
        return mCrimes;
    }

    public Crime getCrime(UUID id){
        for(Crime crime:mCrimes){
            if(crime.getmId().equals(id)){
                return crime;
            }
        }
        return null;
    }



}
