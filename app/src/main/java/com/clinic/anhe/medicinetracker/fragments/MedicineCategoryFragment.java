package com.clinic.anhe.medicinetracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.adapters.MedicineCategoryPagerAdapter;


public class MedicineCategoryFragment extends Fragment {

    private TabLayout mMedicineCategoryTabLayout;
    private Context mContext;
    private ViewPager mMedicineCategoryViewPager;
    private MedicineCategoryPagerAdapter mMedicineCategoryPagerAdapter;

    public static MedicineCategoryFragment newInstance(){
        MedicineCategoryFragment fragment = new MedicineCategoryFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_medicine_category, container, false);

        mMedicineCategoryTabLayout = (TabLayout) view.findViewById(R.id.medicine_category_tabLayout);

        //set up tab
        TabLayout.Tab dialysis = mMedicineCategoryTabLayout.newTab();
        mMedicineCategoryTabLayout.addTab(dialysis);

        TabLayout.Tab edible = mMedicineCategoryTabLayout.newTab();
        mMedicineCategoryTabLayout.addTab(edible);

        TabLayout.Tab needle = mMedicineCategoryTabLayout.newTab();
        mMedicineCategoryTabLayout.addTab(needle);

        TabLayout.Tab bandaid = mMedicineCategoryTabLayout.newTab();
        mMedicineCategoryTabLayout.addTab(bandaid);

        mContext = getContext();

        mMedicineCategoryTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mMedicineCategoryViewPager = (ViewPager) view.findViewById(R.id.medicine_category_pager);
        mMedicineCategoryTabLayout.setupWithViewPager(mMedicineCategoryViewPager);

        mMedicineCategoryPagerAdapter = new MedicineCategoryPagerAdapter(
                getChildFragmentManager(), mMedicineCategoryTabLayout.getTabCount(), mContext);

        mMedicineCategoryViewPager.setAdapter(mMedicineCategoryPagerAdapter);
//        mMedicineCategoryViewPager.setCurrentItem(1);
        highLightCurrentTab(0);

        mMedicineCategoryViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mMedicineCategoryTabLayout) {
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
        mMedicineCategoryTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mMedicineCategoryViewPager.setCurrentItem(tab.getPosition());
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
        for (int i = 0; i < mMedicineCategoryPagerAdapter.getCount(); i++) {
            TabLayout.Tab tab = mMedicineCategoryTabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(mMedicineCategoryPagerAdapter.getTabView(i));
        }

        TabLayout.Tab tab = mMedicineCategoryTabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(mMedicineCategoryPagerAdapter.getSelectedTabView(position));
    }
}
