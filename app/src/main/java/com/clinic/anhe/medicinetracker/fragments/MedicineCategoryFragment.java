package com.clinic.anhe.medicinetracker.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.CartViewModel;
import com.clinic.anhe.medicinetracker.adapters.MedicineCategoryPagerAdapter;
import com.clinic.anhe.medicinetracker.utils.CounterFab;


public class MedicineCategoryFragment extends Fragment {

    private TabLayout mMedicineCategoryTabLayout;
    private Context mContext;
    private ViewPager mMedicineCategoryViewPager;
    private MedicineCategoryPagerAdapter mMedicineCategoryPagerAdapter;
    private CounterFab mCounterfab;
    private CartViewModel cartViewModel;

    public static MedicineCategoryFragment newInstance(){
        MedicineCategoryFragment fragment = new MedicineCategoryFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_medicine_category, container, false);

        //set up view model
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        //observe addtocart and removefrom cart to update ui
        cartViewModel.getCountLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer i) {
//                Log.d("counter fab get count is: " + mCounterfab.getCount(), "live data is: "+ i.intValue());
                    if(mCounterfab.getCount() + 1 == i.intValue()) {
                        mCounterfab.increase();
                    } else if (mCounterfab.getCount() - 1 == i.intValue()) {
                        mCounterfab.decrease();
                    }

            }
        });

//        cartViewModel.decreaseCount().observe(this, new Observer() {
//            @Override
//            public void onChanged(@Nullable Object o) {
//                mCounterfab.decrease();
//            }
//        });


        mMedicineCategoryTabLayout = (TabLayout) view.findViewById(R.id.medicine_category_tabLayout);
        mCounterfab = view.findViewById(R.id.medicine_category_fab);

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
                getChildFragmentManager(), mMedicineCategoryTabLayout.getTabCount(), mCounterfab, mContext);

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

        mCounterfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cannot use getSupportFragmentManger(), it is for calling from activity, use getChildFragmentManager
                //https://stackoverflow.com/questions/7508044/android-fragment-no-view-found-for-id
               // FragmentTransaction transaction = ((FragmentActivity)getContext()).getSupportFragmentManager().beginTransaction();
               FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

                SelectPatientFragment selectPatientFragment = SelectPatientFragment.newInstance();
//                Bundle args = new Bundle();
//                ArrayList<String> cartlist = new ArrayList<>();
//                for(MedicineCardViewModel item :medicineList.getMedicineList()) {
//                    if(item.getIsAddToCart() == true) {
//                        cartlist.add(item.getMedicinName());
//                    }
//                }
//                args.putStringArrayList(ArgumentVariables.ARG_CARTLIST, cartlist);
//                selectPatientFragment.setArguments(args);
                transaction.replace(R.id.medicine_category_layout, selectPatientFragment)
                        .addToBackStack("selectp")
                        .commit();
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
