package com.clinic.anhe.medicinetracker.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.DashboardViewModel;
import com.clinic.anhe.medicinetracker.ViewModel.SelectedPatientViewModel;
import com.clinic.anhe.medicinetracker.adapters.DashboardSettingPagerAdapter;
import com.clinic.anhe.medicinetracker.adapters.PatientsPagerAdapter;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.utils.Shift;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DashboardSettingFragment extends Fragment {

    private ViewPager mDashboardSettingViewPager;
    private TabLayout mDashboardSettingTabLayout;
    private DashboardSettingPagerAdapter mDashboardSettingPagerAdapter;
    private Context mContext;
    private DashboardViewModel dashboardViewModel;
    private SelectedPatientViewModel selectedPatientViewModel;


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

        //set up volley for dashboardViewModel
        VolleyController.getInstance(mContext);

        ViewModelProviders.of(this).get(DashboardViewModel.class);
        ViewModelProviders.of(this).get(SelectedPatientViewModel.class);
//        dashboardViewModel.getDashboardMapLiveData().observe(this, new Observer<Map<String, List<String>>>() {
//            @Override
//            public void onChanged(@Nullable Map<String, List<String>> map) {
//                Log.d("we are at dashboardsetting",  " chloe");
//                for(Map.Entry<String, List<String>> s: map.entrySet()) {
//                    for(String n: s.getValue()) {
//                        Log.d(s.getKey(), n + " chloe");
//                    }
//                }
//            }
//        });


        mDashboardSettingTabLayout = (TabLayout) view.findViewById(R.id.dashboard_setting_tabLayout);
        //set up tab
        TabLayout.Tab morning = mDashboardSettingTabLayout.newTab();
        mDashboardSettingTabLayout.addTab(morning);

//        TabLayout.Tab afternoon = mDashboardSettingTabLayout.newTab();
//        mDashboardSettingTabLayout.addTab(afternoon);

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


//        mDashboardSettingFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Iterator<Map.Entry<String, List<String>>> it =  dashboardViewModel.getDashboardMap().entrySet().iterator();
////                while (it.hasNext()) {
////                    Map.Entry<String, List<String>> pair = it.next();
////                        Iterator<String> listIt = pair.getValue().iterator();
////                        while (listIt.hasNext()) {
////                            String s = listIt.next();
////                            //                    if(s.equalsIgnoreCase(current.getPatientName())){
////                            Log.d("setting: livedata for ",  pair.getKey() + s);
////                        }
////                    }
//                }
//
//        });

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
