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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.ViewModel.CartViewModel;
import com.clinic.anhe.medicinetracker.fragments.CashflowFragment;
import com.clinic.anhe.medicinetracker.fragments.DashboardSettingFragment;
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

public class MainActivity extends AppCompatActivity {

    private DrawerLayout mDrawerlayout;
    private AnimatedVectorDrawable mMenuDrawable;
    private AnimatedVectorDrawable mBackDrawable;
    private AnimatedVectorDrawable mCurrentDrawable;
    private List<MedicineCardViewModel> medicineList;
    private Context mContext;
    private VolleyController volleyController;
//    NavigationDrawerAdapter mMenuAdapter;
//    ExpandableListView expandableList;
//    List<GroupMenuModel> listDataHeader;
//    HashMap<GroupMenuModel, List<GroupMenuModel>> listDataChild;

    private String currentFragment = "patient_morning";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("currentFragment",currentFragment );
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

        //TODO: there is a bug in this part of the code, maybe the following post is the solution
        //TODO: https://stackoverflow.com/questions/28133600/set-initial-fragment-on-startup/34856256#34856256
//        if(savedInstanceState != null) {
//            switch(savedInstanceState.getString("currentFragment")) {
//                case "medicine_category":
//                    getFragmentManager().popBackStack(ArgumentVariables.TAG_MEDICINE_CATEGORY_FRAGMENT, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//                    break;
//
//            }
//        } else {
//            PatientsFragment fragment = PatientsFragment.newInstance(Shift.morning);
//            transaction.replace(R.id.main_fragment_container, fragment, ArgumentVariables.TAG_MEDICINE_CATEGORY_FRAGMENT)
//                    .commit();
//        }

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
                        // set item as selected to persist highlight
                        item.setChecked(true);
                        //fragment transaction has to be in the click method and start everytime you have a tranaction
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        switch(item.getItemId()) {
                            //dashboard menu
                            case R.id.menu_medicine:
//                                currentFragment = "medicine_category";
                                //MedicineFragment medicineFragment = MedicineFragment.newInstance();
                                MedicineCategoryFragment medicineCategoryFragment = MedicineCategoryFragment.newInstance();
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                        .replace(R.id.main_fragment_container, medicineCategoryFragment,ArgumentVariables.TAG_MEDICINE_CATEGORY_FRAGMENT)
                                        .addToBackStack(ArgumentVariables.TAG_MEDICINE_CATEGORY_FRAGMENT)
                                        .commit();
                                break;
                            case R.id.menu_home:
                                DashboardSettingFragment dashboardSettingFragment = DashboardSettingFragment.newInstance();
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                        .replace(R.id.main_fragment_container, dashboardSettingFragment).commit();
                                break;
                            //patient menu
                            case R.id.menu_morning:
//                                currentFragment = "patient_morning";
                                PatientListFragment morningFragment = PatientListFragment.newInstance(Shift.morning);
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                           .replace(R.id.main_fragment_container, morningFragment).commit();
                                break;
                            case R.id.menu_afternoon:
//                                currentFragment = "patient_afternoon";
                                PatientListFragment afternoonFragment = PatientListFragment.newInstance(Shift.afternoon);
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                           .replace(R.id.main_fragment_container, afternoonFragment).commit();
                                break;
                            case R.id.menu_night:
//                                currentFragment = "patient_night";
                                PatientListFragment nightFragment = PatientListFragment.newInstance(Shift.night);
                                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                           .replace(R.id.main_fragment_container, nightFragment).commit();
                                break;
                            case R.id.menu_record:
//                                currentFragment= "medicine_manage";
                                medicineList = new ArrayList<>();
                                MedicineManageFragment medicineManageFragment = MedicineManageFragment.newInstance();
                                getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                                        .replace(R.id.main_fragment_container, medicineManageFragment).commit();

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

//    private  void populateMedicineList (){
//        String url = "http://192.168.0.4:8080/anhe/medicine/all/";
//        JsonArrayRequest jsonArrayRequest =
//                new JsonArrayRequest(Request.Method.GET, url, null,
//                        new Response.Listener<JSONArray>() {
//                            @Override
//                            public void onResponse(JSONArray response) {
//                                for(int i = 0; i < response.length(); i++){
//                                    JSONObject object = null;
//                                    try {
//                                        object = response.getJSONObject(i);
//                                        String category = object.getString("category");
//                                        String name = object.getString("name");
//                                        Integer id = object.getInt("mid");
//                                        Integer price = object.getInt("price");
//                                        String dose = object.getString("dose");
//                                        Integer stock = object.getInt("stock");
//                                        Log.d("jason object" , name + id +price +dose + stock);
//
//                                        medicineList.add(new MedicineCardViewModel(id, name, Integer.toString(price), dose, stock, category));
//
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                    Log.d("getting data from database", "CHLOE");
//                                    MedicineManageFragment medicineManageFragment = MedicineManageFragment.newInstance(medicineList);
//                                    getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
//                                            .replace(R.id.main_fragment_container, medicineManageFragment).commit();
//
//                                }
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.d("VOLLEY", error.toString());
//                            }
//                        } );
//
//        volleyController.getInstance(mContext).addToRequestQueue(jsonArrayRequest);
//    }
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
