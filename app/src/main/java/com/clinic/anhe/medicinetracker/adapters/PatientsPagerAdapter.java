package com.clinic.anhe.medicinetracker.adapters;

import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.content.Context;
import android.view.View;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.ImageView;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.fragments.DashboardPatientsFragment;
import com.clinic.anhe.medicinetracker.fragments.PatientListDayFragment;
import com.clinic.anhe.medicinetracker.fragments.PatientDayFragment;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.clinic.anhe.medicinetracker.utils.DayType;
import com.clinic.anhe.medicinetracker.utils.Shift;

public class PatientsPagerAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;
    private Context mContext;
    private Shift shift;
    private String kind = "";
    private String nurseName;


    private int imageResId = R.drawable.ic_calender;
    private static final String[] tabTitles =
            {" 一 三 五", " 二 四 六"};


    public PatientsPagerAdapter(FragmentManager fm, int NumOfTabs, Context mContext, Shift shift, String kind, String nurseName) {
        super(fm);
        this.numberOfTabs = NumOfTabs;
        this.mContext = mContext;
        this.shift = shift;
        this.kind = kind;
        this.nurseName = nurseName;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        if(kind.equals(ArgumentVariables.KIND_PATIENTS)) {
//        switch (position) {
//            case 0:
//                PatientDayFragment oddDayFragment = PatientDayFragment.newInstance(shift, DayType.oddDay);
//                return oddDayFragment;
//            case 1:
//                PatientDayFragment evenDayFragment = PatientDayFragment.newInstance(shift, DayType.evenDay);
//                return evenDayFragment;
//            default:
//                return null;
//        }
            return null;
        } else if (kind.equals(ArgumentVariables.KIND_PATIENTLIST)) {
            switch (position) {
            case 0:
                 PatientListDayFragment patientListOddDayFragment = PatientListDayFragment.newInstance(shift, DayType.oddDay);
                return patientListOddDayFragment;
            case 1:
                 PatientListDayFragment patientListEvenDayFragment = PatientListDayFragment.newInstance(shift, DayType.evenDay);
                return patientListEvenDayFragment;
            default:
                return null;
        }
        } else {
            //TODO: dashboard patients
            switch(position) {
                case 0:
                    DashboardPatientsFragment dashboardPatientsOddDayFragment = DashboardPatientsFragment.newInstance(DayType.oddDay, shift, nurseName);
                    return dashboardPatientsOddDayFragment;

                case 1:
                    DashboardPatientsFragment dashboardPatientsEvenDayFragment = DashboardPatientsFragment.newInstance(DayType.evenDay,shift, nurseName);
                    return dashboardPatientsEvenDayFragment;
            }
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

