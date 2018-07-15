package com.clinic.anhe.medicinetracker.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.fragments.EvenDayFragment;
import com.clinic.anhe.medicinetracker.fragments.OddDayFragment;

public class PatientsPagerAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;
    private Context mContext;

    private int imageResId = R.drawable.ic_calender;
    private String[] tabTitles =
            {"Mon Wed Fri", "Tue Thur Sat"};


    public PatientsPagerAdapter(FragmentManager fm, int NumOfTabs, Context mContext) {
        super(fm);
        this.numberOfTabs = NumOfTabs;
        this.mContext = mContext;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.d("we are in getPageTitle!", "good!");
//        // Generate title based on item position
//        Drawable image = mContext.getResources().getDrawable(imageResId);
//        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//        // Replace blank spaces with image icon
//        SpannableString sb = new SpannableString("   " + tabTitles[position]);
//        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
//        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return sb;

        return tabTitles[position];
    }



    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                OddDayFragment oddDayFragment = new OddDayFragment();
                return oddDayFragment;
            case 1:
                EvenDayFragment evenDayFragment = new EvenDayFragment();
                return evenDayFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}

