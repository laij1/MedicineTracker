package com.clinic.anhe.medicinetracker.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.CashFlowViewModel;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.model.MedicineRecordCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class CashflowFragment extends Fragment {

    BottomNavigationView mBottomNavigationView;
    private CashFlowViewModel cashFlowViewModel;
    private VolleyController volleyController;
//    private Map<Integer, String> patientMap;
    private Context mContext;
    private GlobalVariable globalVariable;


    public static CashflowFragment newInstance() {
        CashflowFragment fragment = new CashflowFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // What i have added is this
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate( R.layout.fragment_cashflow, container, false);
        mBottomNavigationView = view.findViewById(R.id.cashflow_bottom_navigation);
        
        mContext = view.getContext();
        //TODO: livedata
//        patientMap = new HashMap<>();
////        String url = url = "http://" + globalVariable.getInstance().getIpaddress() +
////                ":" + globalVariable.getInstance().getPort() + "/anho/patient/all";
////        populatePatientMap(url, new VolleyCallBack() {
////            @Override
////            public void onResult(VolleyStatus status) {
////                cashFlowViewModel.getPatientMapLiveData().setValue(patientMap);
////            }
////        });

        cashFlowViewModel = ViewModelProviders.of(this).get(CashFlowViewModel.class);

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                item.setChecked(true);
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                switch (item.getItemId()) {
                    case R.id.cashflow_today:
                        CashflowTodayFragment cashflowTodayFragment = CashflowTodayFragment.newInstance();
                        transaction.replace(R.id.cashflow_fragment_container, cashflowTodayFragment)
                                   .commit();
                        break;
                    case R.id.cashflow_uncharged:
                        CashflowUnchargedFragment cashflowUnchargedFragment = CashflowUnchargedFragment.newInstance();
                        transaction.replace(R.id.cashflow_fragment_container, cashflowUnchargedFragment)
                                   .commit();
                        break;
                    case R.id.cashflow_search:
                        List<MedicineRecordCardViewModel> searchList = cashFlowViewModel.getSearchListLiveData().getValue();
                        searchList.removeAll(searchList);
                        cashFlowViewModel.getSearchListLiveData().setValue(searchList);

                        CashflowSearchFragment cashflowSearchFragment = CashflowSearchFragment.newInstance();
                        transaction.replace(R.id.cashflow_fragment_container, cashflowSearchFragment)
                                   .commit();
                        break;
                    case R.id.cashflow_month:
                        List<MedicineRecordCardViewModel> monthList = cashFlowViewModel.getMonthListLiveData().getValue();
                        monthList.removeAll(monthList);
                        cashFlowViewModel.getMonthListLiveData().setValue(monthList);

                        CashflowMonthFragment cashflowMonthFragment = new CashflowMonthFragment();
                        transaction.replace(R.id.cashflow_fragment_container, cashflowMonthFragment)
                                    .commit();
                }
                return false;
            }
        });

        if(savedInstanceState == null) {
            mBottomNavigationView.getMenu().performIdentifierAction(R.id.cashflow_today, 0);
        }
        return view;
    }

//    public void populatePatientMap(String url, final VolleyCallBack volleyCallBack) {
//        JsonArrayRequest jsonArrayRequest =
//                new JsonArrayRequest(Request.Method.GET, url, null,
//                        new Response.Listener<JSONArray>() {
//                            @Override
//                            public void onResponse(JSONArray response) {
//                                for(int i = 0; i < response.length(); i++){
//                                    JSONObject object = null;
//                                    try {
//                                        object = response.getJSONObject(i);
//                                        Integer pid = object.getInt("pid");
//                                        String name = object.getString("name");
////                                        String shift = object.getString("shift");
////                                        String ic = object.getString("ic");
////                                        String day = object.getString("day");
////
//                                        patientMap.put(new Integer(pid), name);
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                    Log.d("getting patient data from database", "CashFlowViewModel");
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
//                        } );
//
//        volleyController.getInstance(mContext).addToRequestQueue(jsonArrayRequest);
//    }
}
