package com.example.msn_r.bignerdranch;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

public class CrimePagerActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private static final String EXTRA_CIRME_ID="com.example.msn_r.bignerdranch.crime_id";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_crime_pager);

        UUID crimeId= (UUID)getIntent().getSerializableExtra(EXTRA_CIRME_ID);


        mViewPager=(ViewPager)findViewById(R.id.crime_view_pager);
        mCrimes=CrimeLab.get(this).getmCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Crime crime= mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getmId());
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });

        for(int i=0;i<mCrimes.size();i++){
            if(mCrimes.get(i).getmId().equals(crimeId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }

    }

    public static Intent newIntent(Context context, UUID crimeId){
        Intent intent = new Intent(context,CrimePagerActivity.class);
        intent.putExtra(EXTRA_CIRME_ID,crimeId);
        return intent;
    }



}
