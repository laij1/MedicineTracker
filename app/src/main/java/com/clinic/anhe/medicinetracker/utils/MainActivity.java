package com.clinic.anhe.medicinetracker.utils;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.ViewModel.CartViewModel;
import com.clinic.anhe.medicinetracker.fragments.CashflowFragment;
import com.clinic.anhe.medicinetracker.fragments.DashboardSettingFragment;
import com.clinic.anhe.medicinetracker.fragments.DashboardTodayFragment;
import com.clinic.anhe.medicinetracker.fragments.MedicineCategoryFragment;
import com.clinic.anhe.medicinetracker.fragments.MedicineManageFragment;
import com.clinic.anhe.medicinetracker.fragments.PatientsFragment;
import com.clinic.anhe.medicinetracker.fragments.PatientListFragment;
import com.clinic.anhe.medicinetracker.model.GroupMenuModel;
import com.clinic.anhe.medicinetracker.adapters.NavigationDrawerAdapter;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyController;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerlayout;
    private AnimatedVectorDrawable mMenuDrawable;
    private AnimatedVectorDrawable mBackDrawable;
    private AnimatedVectorDrawable mCurrentDrawable;
    private List<MedicineCardViewModel> medicineList;
    private Context mContext;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private PendingIntent alarmIntent1;
    private PendingIntent alarmIntent2;
    private VolleyController volleyController;
//    NavigationDrawerAdapter mMenuAdapter;
//    ExpandableListView expandableList;
//    List<GroupMenuModel> listDataHeader;
//    HashMap<GroupMenuModel, List<GroupMenuModel>> listDataChild;

//    private String currentFragment = "patient_morning";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putString("currentFragment",currentFragment );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        ActionBar actionbar = getSupportActionBar();
        //TODO: figure out if we need to setDisplayHome
       // actionbar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_vector);
        //TODO: discuss what name the app should be and change the title later
        actionbar.setTitle("自費計價單");


        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();


        mMenuDrawable = (AnimatedVectorDrawable) getDrawable(R.drawable.ic_menu_animatable);
        mBackDrawable = (AnimatedVectorDrawable) getDrawable(R.drawable.ic_back_animatable);
        //set current to be backDrwable, initial state
        mCurrentDrawable = mBackDrawable;

        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerlayout.addDrawerListener(new DrawerListener() {
            @Override
            public void onDrawerSlide(View view, float v) {
            }
            @Override
            public void onDrawerOpened(View view) {
                if(mCurrentDrawable == mBackDrawable) {
                    getSupportActionBar().setHomeAsUpIndicator(mMenuDrawable);
                    mMenuDrawable.start();
                    mCurrentDrawable = mMenuDrawable;
                }
            }

            @Override
            public void onDrawerClosed(View view) {
                if(mCurrentDrawable == mMenuDrawable) {
                   getSupportActionBar().setHomeAsUpIndicator(mBackDrawable);
                   mBackDrawable.start();
                   mCurrentDrawable = mBackDrawable;

                }
            }

            @Override
            public void onDrawerStateChanged(int i) {
            }
        });


        //this part of code doesn't work when we use expandableListView here
        NavigationView navigationView = findViewById(R.id.nav_view);


        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        item.setChecked(true);
                        //fragment transaction has to be in the click method and start everytime you have a tranaction
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        //remove all the prev fragments
                        int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
                        for (int i = 0; i < backStackCount; i++) {
                        // Get the back stack fragment id.
                            getSupportFragmentManager().popBackStack(
                                    getSupportFragmentManager().getBackStackEntryAt(i).getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                           //  Log.d("removing fragments",i+"");
                        } /* end of for */

                        switch(item.getItemId()) {
                            //dashboard menu
//                            case R.id.menu_medicine:
////                                currentFragment = "medicine_category";
//                                //MedicineFragment medicineFragment = MedicineFragment.newInstance();
//                                MedicineCategoryFragment medicineCategoryFragment = MedicineCategoryFragment.newInstance();
//                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                                        .replace(R.id.main_fragment_container, medicineCategoryFragment,ArgumentVariables.TAG_MEDICINE_CATEGORY_FRAGMENT)
//                                        .addToBackStack(ArgumentVariables.TAG_MEDICINE_CATEGORY_FRAGMENT)
//                                        .commit();
//                                break;
                            case R.id.menu_home:
                                DashboardSettingFragment dashboardSettingFragment = DashboardSettingFragment.newInstance();
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                        .replace(R.id.main_fragment_container, dashboardSettingFragment)
                                        .addToBackStack(ArgumentVariables.TAG_DASHBOARD_SETTING_FRAGMENT)
                                        .commit();
                                break;
                            //today's item
                            case R.id.menu_today:
                                DashboardTodayFragment dashboardTodayFragment = DashboardTodayFragment.newInstance();
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                        .replace(R.id.main_fragment_container, dashboardTodayFragment)
                                        .addToBackStack(ArgumentVariables.TAG_DASHBOARD_TODAY_FRAGMENT)
                                        .commit();
                                break;
                            //patient menu
                            case R.id.menu_morning:
//                                currentFragment = "patient_morning";
                                PatientListFragment morningFragment = PatientListFragment.newInstance(Shift.morning);
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                           .replace(R.id.main_fragment_container, morningFragment)
                                           .addToBackStack(ArgumentVariables.TAG_MORNING_FRAGMENT)
                                           .commit();
                                break;
                            case R.id.menu_afternoon:
//                                currentFragment = "patient_afternoon";
                                PatientListFragment afternoonFragment = PatientListFragment.newInstance(Shift.afternoon);
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                           .replace(R.id.main_fragment_container, afternoonFragment)
                                           .addToBackStack(ArgumentVariables.TAG_AFTERNOON_FRAGMENT)
                                           .commit();
                                break;
                            case R.id.menu_night:
//                                currentFragment = "patient_night";
                                PatientListFragment nightFragment = PatientListFragment.newInstance(Shift.night);
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                           .replace(R.id.main_fragment_container, nightFragment)
                                           .addToBackStack(ArgumentVariables.TAG_NIGHT_FRAGMENT)
                                           .commit();
                                break;
                            case R.id.menu_record:
//                                currentFragment= "medicine_manage";
                                medicineList = new ArrayList<>();
                                MedicineManageFragment medicineManageFragment = MedicineManageFragment.newInstance();
                                getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                        .replace(R.id.main_fragment_container, medicineManageFragment)
                                        .addToBackStack(ArgumentVariables.TAG_MEDICINE_MANAGE_FRAGMENT)
                                        .commit();

                                //populateMedicineList();
                                break;
                            case R.id.menu_cashflow:
                                CashflowFragment cashflowFragment = CashflowFragment.newInstance();
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                        .replace(R.id.main_fragment_container,cashflowFragment,"cashflow")
                                        .addToBackStack(ArgumentVariables.TAG_MEDICINE_CATEGORY_FRAGMENT)
                                        .commit();
                                break;

                        }
                        // close drawer when item is tapped
                        mDrawerlayout.closeDrawers();
                        return true;
                    }
                }
        );

        //TODO:set start up main fragment
        if(savedInstanceState == null) {
            navigationView.getMenu().performIdentifierAction(R.id.menu_home, 0);
            navigationView.getMenu().findItem(R.id.menu_home).setChecked(false);
        }

        //here set the alarm manager
        setReminderAlarm();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
//        Log.d("on option" + item.getItemId(), "Chloe");
        menuIconClick();
        return true;
    }


    private void menuIconClick() {

        if (mCurrentDrawable == mMenuDrawable) {
            getSupportActionBar().setHomeAsUpIndicator(mBackDrawable);
            mCurrentDrawable = mBackDrawable;
            mDrawerlayout.closeDrawers();
            mBackDrawable.start();

        } else {
            getSupportActionBar().setHomeAsUpIndicator(mMenuDrawable);
            mCurrentDrawable = mMenuDrawable;
            mDrawerlayout.openDrawer(GravityCompat.START);
            mMenuDrawable.start();

        }

    }

    private void setReminderAlarm() {

        //Log.d("we are setting reminder", "Chloe");
        alarmMgr = (AlarmManager)mContext.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(mContext, AlarmReceiver.class);
        intent.putExtra("Reminder", "firstReminder");
        Intent intent1 = new Intent(mContext, AlarmReceiver.class);
        intent1.putExtra("Reminder", "secondReminder");
        Intent intent2 = new Intent(mContext, AlarmReceiver.class);
        intent2.putExtra("Reminder", "thirdReminder");

        alarmIntent = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarmIntent1 = PendingIntent.getBroadcast(mContext, 1, intent1, PendingIntent.FLAG_CANCEL_CURRENT );
        alarmIntent2 = PendingIntent.getBroadcast(mContext, 2, intent2, PendingIntent.FLAG_CANCEL_CURRENT);

        // Set the alarm to start at 11am.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 0);

        //set second alarm to start at 15pm
        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(System.currentTimeMillis());
        calendar1.set(Calendar.HOUR_OF_DAY, 15);
        calendar1.set(Calendar.MINUTE, 0);


        //set third alarm to start at 9pm
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(System.currentTimeMillis());
        calendar2.set(Calendar.HOUR_OF_DAY, 21);
        calendar2.set(Calendar.MINUTE, 0);

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent);

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent1);

        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, alarmIntent2);

    }
}
