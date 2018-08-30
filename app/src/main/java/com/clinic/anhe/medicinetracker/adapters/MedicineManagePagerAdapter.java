package com.clinic.anhe.medicinetracker.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.fragments.MedicineFragment;
import com.clinic.anhe.medicinetracker.fragments.MedicineSimpleFragment;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.utils.CounterFab;
import com.clinic.anhe.medicinetracker.utils.MedicineType;

import java.util.List;


public class MedicineManagePagerAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;
    private Context mContext;
    private CounterFab mCounterFab;
//    private List<MedicineCardViewModel> medicineList;

    private static final String[] tabTitles =
            {" 自費洗腎", " 口服藥物", " 注射藥物", " 外用藥物"};

    public MedicineManagePagerAdapter(FragmentManager fm, int numbersOfTabs, Context context) {
        super(fm);
        this.numberOfTabs = numbersOfTabs;
        this.mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MedicineSimpleFragment dialysis = MedicineSimpleFragment.newInstance(MedicineType.dialysis);
                Log.d("dialysis tab view pager is created", "CHLOE!!!");
//                transaction.add(dialysis, "dialysis").commit();
                return dialysis;
            case 1:
                MedicineSimpleFragment edible = MedicineSimpleFragment.newInstance(MedicineType.edible);
                return edible;
            case 2:
                MedicineSimpleFragment needle = MedicineSimpleFragment.newInstance(MedicineType.needle);
                return needle;
            case 3:
                MedicineSimpleFragment bandaid = MedicineSimpleFragment.newInstance(MedicineType.bandaid);
                return bandaid;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
        TextView tabTextView = view.findViewById(R.id.tabTextView);
        tabTextView.setText(tabTitles[position]);
        //set image for each tab
        ImageView tabImageView = view.findViewById(R.id.tabImageView);
        switch(position) {
            case 0://dialysis
                tabImageView.setImageResource(R.drawable.ic_medicine);
                break;
            case 1: //edible
                tabImageView.setImageResource(R.drawable.ic_edible);
                break;
            case 2: //needle
                tabImageView.setImageResource(R.drawable.ic_needle);
                break;
            case 3: //bandaid
                tabImageView.setImageResource(R.drawable.ic_bandaid);
        }

        return view;
    }

    public View getSelectedTabView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
        TextView tabTextView = view.findViewById(R.id.tabTextView);
        tabTextView.setText(tabTitles[position]);
        tabTextView.setTypeface(null, Typeface.BOLD);
        tabTextView.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        ImageView tabImageView = view.findViewById(R.id.tabImageView);
        //set image
        switch(position) {
            case 0://dialysis
                tabImageView.setImageResource(R.drawable.ic_medicine);
                break;
            case 1: //edible
                tabImageView.setImageResource(R.drawable.ic_edible);
                break;
            case 2: //needle
                tabImageView.setImageResource(R.drawable.ic_needle);
                break;
            case 3: //bandaid
                tabImageView.setImageResource(R.drawable.ic_bandaid);
        }

        tabImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        return view;
    }

}
