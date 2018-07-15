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
import android.view.View;
import android.view.LayoutInflater;
import android.widget.TextView;
import android.widget.ImageView;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.fragments.EvenDayFragment;
import com.clinic.anhe.medicinetracker.fragments.OddDayFragment;
import java.util.List;
import java.util.ArrayList;

public class PatientsPagerAdapter extends FragmentPagerAdapter {

    private int numberOfTabs;
    private Context mContext;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();
    private final List<Integer> mFragmentIconList = new ArrayList<>();

    private int imageResId = R.drawable.ic_calender;
    private String[] tabTitles =
            {" Mon Wed Fri", " Tue Thur Sat"};


    public PatientsPagerAdapter(FragmentManager fm, int NumOfTabs, Context mContext) {
        super(fm);
        this.numberOfTabs = NumOfTabs;
        this.mContext = mContext;
    }

    @Override
    public CharSequence getPageTitle(int position) {
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

    public int getImageResId() {
        return imageResId;
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
        ImageView tabImageView = view.findViewById(R.id.tabImageView);
        tabImageView.setImageResource(imageResId);
        return view;
    }

    public void addFragment(Fragment fragment, String title, int tabIcon) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
        mFragmentIconList.add(tabIcon);
    }
}

