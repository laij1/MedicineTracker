package com.clinic.anhe.medicinetracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.adapters.MedicineManagePagerAdapter;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.utils.CounterFab;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MedicineManageFragment extends Fragment {

    private TabLayout mMedicineManageTabLayout;
    private Context mContext;
    private ViewPager mMedicineManageViewPager;
    private MedicineManagePagerAdapter mMedicineManagePagerAdapter;
    private CounterFab mCounterfab;
    private VolleyController volleyController;
    private static List<MedicineCardViewModel> medicineList;



    public static MedicineManageFragment newInstance(List<MedicineCardViewModel> mList){
        MedicineManageFragment fragment = new MedicineManageFragment();
        medicineList = mList;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_medicine_manage, container, false);

        mMedicineManageTabLayout = (TabLayout) view.findViewById(R.id.medicine_manage_tabLayout);
        mCounterfab = view.findViewById(R.id.medicine_manage_fab);

        //set up tab
        TabLayout.Tab dialysis = mMedicineManageTabLayout.newTab();
        mMedicineManageTabLayout.addTab(dialysis);

        TabLayout.Tab edible = mMedicineManageTabLayout.newTab();
        mMedicineManageTabLayout.addTab(edible);

        TabLayout.Tab needle = mMedicineManageTabLayout.newTab();
        mMedicineManageTabLayout.addTab(needle);

        TabLayout.Tab bandaid = mMedicineManageTabLayout.newTab();
        mMedicineManageTabLayout.addTab(bandaid);

        mMedicineManageTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mMedicineManageViewPager = (ViewPager) view.findViewById(R.id.medicine_manage_pager);
        mMedicineManageTabLayout.setupWithViewPager(mMedicineManageViewPager);

        mMedicineManagePagerAdapter = new MedicineManagePagerAdapter(
                getChildFragmentManager(), mMedicineManageTabLayout.getTabCount(), mContext, medicineList);

        mMedicineManageViewPager.setAdapter(mMedicineManagePagerAdapter);

        highLightCurrentTab(0);

        mMedicineManageViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mMedicineManageTabLayout) {
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

        mMedicineManageTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mMedicineManageViewPager.setCurrentItem(tab.getPosition());
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
        for (int i = 0; i < mMedicineManagePagerAdapter.getCount(); i++) {
            TabLayout.Tab tab = mMedicineManageTabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(mMedicineManagePagerAdapter.getTabView(i));
        }

        TabLayout.Tab tab = mMedicineManageTabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(mMedicineManagePagerAdapter.getSelectedTabView(position));
    }
}
