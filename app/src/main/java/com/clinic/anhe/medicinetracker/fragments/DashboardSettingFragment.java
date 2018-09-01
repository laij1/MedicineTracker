package com.clinic.anhe.medicinetracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.adapters.DashboardSettingPagerAdapter;
import com.clinic.anhe.medicinetracker.adapters.PatientsPagerAdapter;
import com.clinic.anhe.medicinetracker.utils.Shift;

public class DashboardSettingFragment extends Fragment {

    private ViewPager mDashboardSettingViewPager;
    private TabLayout mDashboardSettingTabLayout;
    private DashboardSettingPagerAdapter mDashboardSettingPagerAdapter;
    private Context mContext;


    public static DashboardSettingFragment newInstance(){
        DashboardSettingFragment fragment = new DashboardSettingFragment();
        return fragment;
    }


//    @Override
//    public void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putString(ARG_PATIENT_SHIFT, shift.toString());
//    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_setting, container, false);

        mDashboardSettingTabLayout = (TabLayout) view.findViewById(R.id.dashboard_setting_tabLayout);
        //set up tab
        TabLayout.Tab morning = mDashboardSettingTabLayout.newTab();
        mDashboardSettingTabLayout.addTab(morning);

        TabLayout.Tab afternoon = mDashboardSettingTabLayout.newTab();
        mDashboardSettingTabLayout.addTab(afternoon);

        TabLayout.Tab night = mDashboardSettingTabLayout.newTab();
        mDashboardSettingTabLayout.addTab(night);

        mContext = getContext();

        mDashboardSettingTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mDashboardSettingViewPager = (ViewPager) view.findViewById(R.id.dashboard_setting_pager);
        mDashboardSettingTabLayout.setupWithViewPager(mDashboardSettingViewPager);
        mDashboardSettingPagerAdapter = new DashboardSettingPagerAdapter(
                getChildFragmentManager(),mDashboardSettingTabLayout.getTabCount(), mContext);
        mDashboardSettingViewPager.setAdapter(mDashboardSettingPagerAdapter);

        highLightCurrentTab(0);


        mDashboardSettingViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mDashboardSettingTabLayout) {
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
        mDashboardSettingTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mDashboardSettingViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        setRetainInstance(true);
        return view;
    }

    private void highLightCurrentTab(int position) {
        for (int i = 0; i < mDashboardSettingPagerAdapter.getCount(); i++) {
            TabLayout.Tab tab = mDashboardSettingTabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(mDashboardSettingPagerAdapter.getTabView(i));
        }

        TabLayout.Tab tab = mDashboardSettingTabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(mDashboardSettingPagerAdapter.getSelectedTabView(position));
    }


}
