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
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerlayout;

    //https://github.com/andremion/Android-Animated-Icons
    private AnimatedVectorDrawable mMenuDrawable;
    private AnimatedVectorDrawable mBackDrawable;
    private AnimatedVectorDrawable mCurrentDrawable;
    private boolean mMenuFlag;
//    private boolean mMenuClicked = false;

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
        //set current to be menu icon
        mCurrentDrawable = mMenuDrawable;

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
                    mMenuFlag = !mMenuFlag;
                }
            }

            @Override
            public void onDrawerClosed(View view) {
                if(mCurrentDrawable == mMenuDrawable) {
                   getSupportActionBar().setHomeAsUpIndicator(mBackDrawable);
                   mBackDrawable.start();
                   mCurrentDrawable = mBackDrawable;
                   mMenuFlag = !mMenuFlag;
                }
            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });

        NavigationView navigationView = findViewById(R.id.nav_view);
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
                menuClick();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void menuClick() {
        if (mMenuFlag) {
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
           mMenuFlag = !mMenuFlag;
    }
}
