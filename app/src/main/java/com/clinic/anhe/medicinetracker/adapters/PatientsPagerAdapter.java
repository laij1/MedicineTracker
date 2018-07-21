package com.clinic.anhe.medicinetracker.adapters;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.ImageView;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.fragments.EvenDayFragment;
import com.clinic.anhe.medicinetracker.fragments.OddDayFragment;
import com.clinic.anhe.medicinetracker.utils.MainActivity;
import com.clinic.anhe.medicinetracker.utils.Shift;

public class PatientsPagerAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;
    private Context mContext;
    private Shift shift;


    private int imageResId = R.drawable.ic_calender;
    private static final String[] tabTitles =
            {" 一 三 五", " 二 四 六"};


    public PatientsPagerAdapter(FragmentManager fm, int NumOfTabs, Context mContext, Shift shift) {
        super(fm);
        this.numberOfTabs = NumOfTabs;
        this.mContext = mContext;
        this.shift = shift;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                OddDayFragment oddDayFragment = new OddDayFragment(shift);
                return oddDayFragment;
            case 1:
                EvenDayFragment evenDayFragment = new EvenDayFragment(shift);
                return evenDayFragment;
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
        ImageView tabImageView = view.findViewById(R.id.tabImageView);
        tabImageView.setImageResource(imageResId);
        return view;
    }

    public View getSelectedTabView(int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_tab, null);
        TextView tabTextView = view.findViewById(R.id.tabTextView);
        tabTextView.setText(tabTitles[position]);
        tabTextView.setTypeface(null, Typeface.BOLD);
        tabTextView.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        ImageView tabImageView = view.findViewById(R.id.tabImageView);

        tabImageView.setImageResource(imageResId);
        tabImageView.setColorFilter(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
        return view;
    }

}

