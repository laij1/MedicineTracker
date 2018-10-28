package com.clinic.anhe.medicinetracker.fragments;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.MedicineManageViewModel;
import com.clinic.anhe.medicinetracker.adapters.MedicineManagePagerAdapter;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.model.MedicineRecordCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.CounterFab;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class MedicineManageFragment extends Fragment {

    private TabLayout mMedicineManageTabLayout;
    private Context mContext;
    private ViewPager mMedicineManageViewPager;
    private MedicineManagePagerAdapter mMedicineManagePagerAdapter;
    private MedicineManageViewModel medicineManageViewModel;
    private VolleyController volleyController;
    private GlobalVariable globalVariable;
//    private FloatingActionButton mAddItem;
   // private static List<MedicineCardViewModel> medicineList;



    public static MedicineManageFragment newInstance(){
        MedicineManageFragment fragment = new MedicineManageFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // What i have added is this
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_medicine_manage, container, false);

        medicineManageViewModel = ViewModelProviders.of(this).get(MedicineManageViewModel.class);

//        mAddItem = view.findViewById(R.id.add_medicine_fab);

        mMedicineManageTabLayout = (TabLayout) view.findViewById(R.id.medicine_manage_tabLayout);

//        Calendar cal = Calendar.getInstance();
//        Date date = cal.getTime();
//        String endDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
//        //set first day of the the month
//        cal.set(Calendar.DAY_OF_MONTH, 1);
//        Date firstDayOfMonth = cal.getTime();
//        String startDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(firstDayOfMonth);
//        String url = "http://" + globalVariable.getInstance().getIpaddress() + ":" + globalVariable.getInstance().getPort() + "/anho/record/all/rangedate?start=" +
//                startDate + "&end=" + endDate;
//
//
//        parseRecordListData(url, new VolleyCallBack() {
//            @Override
//            public void onResult(VolleyStatus status) {
//                if(status == VolleyStatus.SUCCESS) {
//
//                }
//            }
//        });

        //set up tab
        TabLayout.Tab dialysis = mMedicineManageTabLayout.newTab();
        mMedicineManageTabLayout.addTab(dialysis);

        TabLayout.Tab edible = mMedicineManageTabLayout.newTab();
        mMedicineManageTabLayout.addTab(edible);

        TabLayout.Tab needle = mMedicineManageTabLayout.newTab();
        mMedicineManageTabLayout.addTab(needle);

        TabLayout.Tab bandaid = mMedicineManageTabLayout.newTab();
        mMedicineManageTabLayout.addTab(bandaid);

        mMedicineManageTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mMedicineManageViewPager = (ViewPager) view.findViewById(R.id.medicine_manage_pager);
        mMedicineManageTabLayout.setupWithViewPager(mMedicineManageViewPager);

        mMedicineManagePagerAdapter = new MedicineManagePagerAdapter(
                getChildFragmentManager(), mMedicineManageTabLayout.getTabCount(), mContext);

        mMedicineManageViewPager.setAdapter(mMedicineManagePagerAdapter);

        highLightCurrentTab(0);

        mMedicineManageViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mMedicineManageTabLayout) {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                highLightCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }


        });

        mMedicineManageTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mMedicineManageViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });




        setRetainInstance(true);
        return view;
    }



    private void highLightCurrentTab(int position) {
        for (int i = 0; i < mMedicineManagePagerAdapter.getCount(); i++) {
            TabLayout.Tab tab = mMedicineManageTabLayout.getTabAt(i);
            assert tab != null;
            tab.setCustomView(null);
            tab.setCustomView(mMedicineManagePagerAdapter.getTabView(i));
        }

        TabLayout.Tab tab = mMedicineManageTabLayout.getTabAt(position);
        assert tab != null;
        tab.setCustomView(null);
        tab.setCustomView(mMedicineManagePagerAdapter.getSelectedTabView(position));
    }
//    private void parseRecordListData(String url, final VolleyCallBack volleyCallBack) {
//        JsonArrayRequest jsonArrayRequest =
//                new JsonArrayRequest(Request.Method.GET, url, null,
//                        new Response.Listener<JSONArray>() {
//                            @Override
//                            public void onResponse(JSONArray response) {
//                                for(int i = 0; i < response.length(); i++){
//                                    JSONObject object = null;
//                                    try {
//                                        object = response.getJSONObject(i);
//
//                                        String createAt = object.getString("createAt");
//                                        Integer rid = object.getInt("rid");
//                                        Integer pid = object.getInt("pid");
//                                        Integer mid = object.getInt("mid");
//                                        String name = object.getString("medicineName");
//                                        Integer quantity = object.getInt("quantity");
//                                        Integer subtotal = object.getInt("subtotal");
//                                        String createBy = object.getString("createBy");
//                                        String payment = object.getString("payment");
////                                        String chargeAt = object.getString("chargeAt");
////                                        String chargeBy = object.getString("chargeBy");
//                                        String patientName = object.getString("patientName");
//                                        MedicineRecordCardViewModel item = new MedicineRecordCardViewModel(rid, createAt, mid, name, quantity,
//                                                subtotal, payment, pid, createBy);
//                                        item.setPatientName(patientName);
////                                        item.setChargeAt(chargeAt);
////                                        item.setChargeBy(chargeBy);
//                                        if(!medicineManageViewModel.getMedicineListLiveData().getValue().contains(item)){
//                                            medicineManageViewModel.getMedicineListLiveData().getValue().add(item);
//                                        }
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                    volleyCallBack.onResult(VolleyStatus.SUCCESS);
//                                }
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.d("VOLLEY", error.toString());
//                                volleyCallBack.onResult(VolleyStatus.FAIL);
//                            }
//                        } ){/**
//                 * Passing some request headers
//                 */
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String, String> headers = new HashMap<>();
//                    String credentials = "admin1:secret1";
//                    String auth = "Basic "
//                            + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
//                    headers.put("Content-Type", "application/json");
//                    headers.put("Authorization", auth);
//                    return headers;
//                }};
//
//        volleyController.getInstance(mContext).addToRequestQueue(jsonArrayRequest);
//    }
}
