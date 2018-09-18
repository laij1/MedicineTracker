package com.clinic.anhe.medicinetracker.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.fragments.CashflowSearchFragment;
import com.clinic.anhe.medicinetracker.fragments.DashboardFragment;
import com.clinic.anhe.medicinetracker.fragments.PatientDayFragment;
import com.clinic.anhe.medicinetracker.fragments.PatientListDayFragment;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.clinic.anhe.medicinetracker.utils.DayType;
import com.clinic.anhe.medicinetracker.utils.Shift;

public class DashboardSettingPagerAdapter extends FragmentPagerAdapter{

    private int numberOfTabs;
    private Context mContext;



    private int imageResId = R.drawable.ic_calender;
    private static final String[] tabTitles =
            {" 早班", " 中班", " 晚班"};


    public DashboardSettingPagerAdapter(FragmentManager fm, int NumOfTabs, Context mContext) {
        super(fm);
        this.numberOfTabs = NumOfTabs;
        this.mContext = mContext;

    }


    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                DashboardFragment morningDashboardFragment = DashboardFragment.newInstance(Shift.morning);
                return morningDashboardFragment;
            case 1:
                DashboardFragment afternoonDashboardFragment = DashboardFragment.newInstance(Shift.afternoon);
                return afternoonDashboardFragment;
            case 2:
                DashboardFragment nightDashboardFragment = DashboardFragment.newInstance(Shift.night);
                return nightDashboardFragment;
            }
            return null;
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
