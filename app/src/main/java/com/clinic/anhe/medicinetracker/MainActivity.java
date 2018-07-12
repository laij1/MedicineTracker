package com.clinic.anhe.medicinetracker;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerlayout;

    //https://github.com/andremion/Android-Animated-Icons
    private AnimatedVectorDrawable mMenuDrawable;
    private AnimatedVectorDrawable mBackDrawable;
    private AnimatedVectorDrawable mCurrentDrawable;
    NavigationDrawerAdapter mMenuAdapter;
    ExpandableListView expandableList;
    List<GroupMenuModel> listDataHeader;
    HashMap<GroupMenuModel, List<GroupMenuModel>> listDataChild;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActionBar actionbar = getSupportActionBar();
        //TODO: figure out if we need to setDisplayHome
       // actionbar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_TITLE);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_vector);

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

        //for expandableListview
        expandableList = (ExpandableListView) findViewById(R.id.nav_expandable_list);

        prepareListData();
        mMenuAdapter = new NavigationDrawerAdapter(this, listDataHeader, listDataChild, expandableList);

        // setting list adapter
        expandableList.setAdapter(mMenuAdapter);
        NavigationView navigationView = findViewById(R.id.nav_view);
        //TODO: is it necessary to check if naviagationView is not null
//        if (navigationView != null) {
//            setupDrawerContent(navigationView);
//        }

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // set item as selected to persist highlight
                        item.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerlayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                menuIconClick();
                return true;
        }
        return super.onOptionsItemSelected(item);
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

    private void prepareListData() {
        listDataHeader = new ArrayList<GroupMenuModel>();
        //listDataChild = new HashMap<GroupMenuModel, List<String>>();
        listDataChild = new HashMap<>();

        GroupMenuModel patients = new GroupMenuModel();
        patients.setIconName("Patients");
        patients.setIconImg(R.drawable.ic_account);
        // Adding data header
        listDataHeader.add(patients);

        GroupMenuModel medicine = new GroupMenuModel();
        medicine.setIconName("Medicine");
        medicine.setIconImg(R.drawable.ic_medicine);
        listDataHeader.add(medicine);

        GroupMenuModel cart = new GroupMenuModel();
        cart.setIconName("Cart");
        cart.setIconImg(R.drawable.ic_cart);
        listDataHeader.add(cart);

        //Adding child data
        List<GroupMenuModel> shifts = new ArrayList<>();
        GroupMenuModel morning = new GroupMenuModel("Morning", R.drawable.ic_morning);
        shifts.add(morning);

        GroupMenuModel afternoon = new GroupMenuModel("Afternoon", R.drawable.ic_afternoon);
        shifts.add(afternoon);

        GroupMenuModel night = new GroupMenuModel("Night", R.drawable.ic_night);
        shifts.add(night);
        // Adding child data
//        List<String> shifts = new ArrayList<String>();
//        shifts.add("Morning");
//        shifts.add("Afternoon");
//        shifts.add("Night");

        listDataChild.put(listDataHeader.get(0), shifts);// Header, Child data


    }
}
