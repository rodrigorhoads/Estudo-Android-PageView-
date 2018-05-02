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
    }

    public List<Crime> getmCrimes(){
        return mCrimes;
    }
    public void addCrime(Crime crime){
        mCrimes.add(crime);
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
