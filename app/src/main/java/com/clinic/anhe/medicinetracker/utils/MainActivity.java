package com.clinic.anhe.medicinetracker.utils;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;

import com.clinic.anhe.medicinetracker.fragments.MedicineCategoryFragment;
import com.clinic.anhe.medicinetracker.fragments.PatientsFragment;
import com.clinic.anhe.medicinetracker.model.GroupMenuModel;
import com.clinic.anhe.medicinetracker.adapters.NavigationDrawerAdapter;
import com.clinic.anhe.medicinetracker.R;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerlayout;
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
        //TODO: discuss what name the app should be and change the title later
        actionbar.setTitle("自費計價單");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        PatientsFragment fragment = PatientsFragment.newInstance(Shift.morning);
        transaction.replace(R.id.main_fragment_container, fragment, ArgumentVariables.TAG_MEDICINE_CATEGORY_FRAGMENT)
                   .commit();


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
//        expandableList = (ExpandableListView) findViewById(R.id.nav_expandable_list);

//        prepareListData();
       // mMenuAdapter = new NavigationDrawerAdapter(this, listDataHeader, listDataChild, expandableList);

        // setting list adapter
//        expandableList.setAdapter(mMenuAdapter);
//
//
//        //Listen for GroupMenu Click and SubMenu Click
//        expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long l) {
//                //TODO:1. add @drawer:selector
//                //TODO:2. add android:background="@drawable/selector to list_submenu
//                //TODO:3. view.setSelected
//                //TODO:4.(optional??) expandableList.setChoiceMode(ExpandableListView.CHOICE_MODE_SINGLE);
//               // view.setSelected(true);
//
//                //TODO: should we hightlight the clicked item?
////                TextView textView = view.findViewById(R.id.sub_menu_title);
////                textView.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.colorPrimaryDark));
//
//                //TODO: add fragements here
//                //TODO: see if we can use only one patientFragement and update data when switch case
//                //fragment transaction has to be in the click method and start everytime you have a tranaction
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                switch (childPosition) {
//                    case 0:
//                        PatientsFragment morningFragment = PatientsFragment.newInstance(Shift.morning);
//                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                                .replace(R.id.main_fragment_container, morningFragment).commit();
//                        break;
//                    case 1:
//                        PatientsFragment afternoonFragment = PatientsFragment.newInstance(Shift.afternoon);
//                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                                .replace(R.id.main_fragment_container, afternoonFragment).commit();
//                        break;
//                    case 2:
//                        PatientsFragment nightFragment = PatientsFragment.newInstance(Shift.night);
//                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                                .replace(R.id.main_fragment_container, nightFragment).commit();
//                        break;
//                    default:
//                        break;
//                }
//
//                //close drawerlayout
//                mDrawerlayout.closeDrawers();
//
//                return true;
//            }
//        });
//
//        expandableList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//            @Override
//            public boolean onGroupClick(ExpandableListView expandableListView, View view, int packedPosition, long l) {
//                //TODO: here we insert the fragments
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                switch(packedPosition) {
//                    case 0:
//                        break;
//                    case 1:
//                        MedicineFragment medicineFragment = MedicineFragment.newInstance();
//                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                                .replace(R.id.main_fragment_container, medicineFragment)
//                                .addToBackStack("medicine").commit();
//                        break;
////                    case 2:
////                        break;
//                }
//
//                if(packedPosition != 0) {
//                    mDrawerlayout.closeDrawers();
//                }
//                return false;
//
//            }
//        });
//
//        expandableList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//            int previousItem = -1;
//            @Override
//            public void onGroupExpand(int groupPosition) {
//                if (groupPosition != previousItem)
//                    expandableList.collapseGroup(previousItem);
//                previousItem = groupPosition;
//            }
//        });

        //this part of code doesn't work when we use expandableListView here
        NavigationView navigationView = findViewById(R.id.nav_view);
//        //TODO: is it necessary to check if naviagationView is not null
//        if (navigationView != null) {
//            setupDrawerContent(navigationView);
//        }

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        // set item as selected to persist highlight
                        item.setChecked(true);
                        //fragment transaction has to be in the click method and start everytime you have a tranaction
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        switch(item.getItemId()) {
                            //patient menu
                            case R.id.menu_morning:
                                PatientsFragment morningFragment = PatientsFragment.newInstance(Shift.morning);
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                           .replace(R.id.main_fragment_container, morningFragment).commit();
                                break;
                            case R.id.menu_afternoon:
                                PatientsFragment afternoonFragment = PatientsFragment.newInstance(Shift.afternoon);
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                           .replace(R.id.main_fragment_container, afternoonFragment).commit();
                                break;
                            case R.id.menu_night:
                                PatientsFragment nightFragment = PatientsFragment.newInstance(Shift.night);
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                           .replace(R.id.main_fragment_container, nightFragment).commit();
                                break;
                             //medicine menu
                            case R.id.menu_medicine:
                                //MedicineFragment medicineFragment = MedicineFragment.newInstance();
                                MedicineCategoryFragment medicineCategoryFragment = MedicineCategoryFragment.newInstance();
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                           .replace(R.id.main_fragment_container, medicineCategoryFragment,ArgumentVariables.TAG_MEDICINE_CATEGORY_FRAGMENT)
                                           .addToBackStack(ArgumentVariables.TAG_MEDICINE_CATEGORY_FRAGMENT)
                                           .commit();
                                break;
//                            case R.id.menu_edible:
//                                MedicineFragment medicineFragment = MedicineFragment.newInstance();
//                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                                           .replace(R.id.main_fragment_container, medicineFragment,"medicinefragment")
//                                           .commit();
//                                break;
//                            case R.id.menu_needle:
//                                break;
//                            case R.id.menu_bandaid:
//                                break;
                            //manage menu
                            case R.id.menu_record:
                                break;
                            case R.id.menu_cashflow:
                                break;
                            case R.id.menu_inventory:
                                break;
                        }
                        // close drawer when item is tapped
                        mDrawerlayout.closeDrawers();
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

//    private void prepareListData() {
//        listDataHeader = new ArrayList<GroupMenuModel>();
//        //listDataChild = new HashMap<GroupMenuModel, List<String>>();
//        listDataChild = new HashMap<>();
//
//        GroupMenuModel patients = new GroupMenuModel();
//        patients.setIconName("病患");
//        patients.setIconImg(R.drawable.ic_account);
//        // Adding data header
//        listDataHeader.add(patients);
//
//        GroupMenuModel medicine = new GroupMenuModel();
//        medicine.setIconName("藥品");
//        medicine.setIconImg(R.drawable.ic_medicine);
//        listDataHeader.add(medicine);
//
////        GroupMenuModel cart = new GroupMenuModel();
////        cart.setIconName("購物車");
////        cart.setIconImg(R.drawable.ic_cart);
////        listDataHeader.add(cart);
//
//        //Adding child data
//        List<GroupMenuModel> shifts = new ArrayList<>();
//        GroupMenuModel morning = new GroupMenuModel("早班", R.drawable.ic_morning);
//        shifts.add(morning);
//
//        GroupMenuModel afternoon = new GroupMenuModel("中班", R.drawable.ic_afternoon);
//        shifts.add(afternoon);
//
//        GroupMenuModel night = new GroupMenuModel("晚班", R.drawable.ic_night);
//        shifts.add(night);
//
//        listDataChild.put(listDataHeader.get(0), shifts);// Header, Child data
//
//
//    }
}
