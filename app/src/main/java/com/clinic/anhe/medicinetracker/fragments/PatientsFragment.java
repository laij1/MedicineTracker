package com.clinic.anhe.medicinetracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.adapters.PatientsPagerAdapter;
import com.clinic.anhe.medicinetracker.utils.MainActivity;
import com.clinic.anhe.medicinetracker.utils.Shift;

public class PatientsFragment  extends Fragment {

    private ViewPager mPatientsViewPager;
    private TabLayout mPatientsTabLayout;
    private PatientsPagerAdapter mPatientsPagerAdapter;
    private Context mContext;
    private Shift shift;

    public PatientsFragment(Shift shift) {
        this.shift = shift;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patients, container, false);

        mPatientsTabLayout = (TabLayout) view.findViewById(R.id.patients_tabLayout);

        //set up tab
        TabLayout.Tab OddDays = mPatientsTabLayout.newTab();
        mPatientsTabLayout.addTab(OddDays);

        TabLayout.Tab EvenDays = mPatientsTabLayout.newTab();
        mPatientsTabLayout.addTab(EvenDays);

        mContext = getContext();

        mPatientsTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mPatientsViewPager = (ViewPager) view.findViewById(R.id.patients_pager);
        mPatientsTabLayout.setupWithViewPager(mPatientsViewPager);
        mPatientsPagerAdapter = new PatientsPagerAdapter
                (getChildFragmentManager(), mPatientsTabLayout.getTabCount(), mContext, shift);
        mPatientsViewPager.setAdapter(mPatientsPagerAdapter);

        highLightCurrentTab(0);

        mPatientsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mPatientsTabLayout) {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                highLightCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }


        });
        mPatientsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPatientsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });


        return view;
    }

    private void highLightCurrentTab(int position) {
        for (int i = 0; i < mPatientsPagerAdapter.getCount(); i++) {
            TabLayout.Tab tab = mPatientsTabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(mPatientsPagerAdapter.getTabView(i));
        }

        TabLayout.Tab tab = mPatientsTabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(mPatientsPagerAdapter.getSelectedTabView(position));
    }
}