package com.clinic.anhe.medicinetracker.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.DashboardViewModel;
import com.clinic.anhe.medicinetracker.adapters.DashboardPatientRecyclerViewAdapter;

import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.clinic.anhe.medicinetracker.utils.DayType;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DashboardPatientsFragment extends Fragment implements ArgumentVariables{

    private List<PatientsCardViewModel> patientList;
    private RecyclerView mRecyclerView;
    private DashboardPatientRecyclerViewAdapter mAdapter;
    private GridLayoutManager mLayoutManager;
//    private Shift shift;
    private DayType dayType;
    private VolleyController volleyController;
    private Context mContext;
    private DashboardViewModel dashboardViewModel;
    private String nurseName= "";


    public static DashboardPatientsFragment newInstance(DayType dayType, String nurseName) {
        DashboardPatientsFragment fragment = new DashboardPatientsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DAY_TYPE, dayType.toString());
        args.putString(ARG_NURSE_NAME, nurseName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_DAY_TYPE, dayType.toString());
        outState.putString(ARG_NURSE_NAME, nurseName);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dashboardViewModel = ViewModelProviders.of(getParentFragment().getParentFragment()).get(DashboardViewModel.class);
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_patient_list_day, container, false);
        mContext = getContext();

        if(savedInstanceState != null) {
            dayType = dayType.fromString(savedInstanceState.getString(ARG_DAY_TYPE));
            nurseName= savedInstanceState.getString(ARG_NURSE_NAME);
        }

        if(dayType == null) {
            dayType= dayType.fromString(getArguments().getString(ARG_DAY_TYPE));
            nurseName = getArguments().getString(ARG_NURSE_NAME);
        }

//
        preparePatientData();

        mRecyclerView = view.findViewById(R.id.patient_list_day_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 3);
        //here we need to filter
        mAdapter = new DashboardPatientRecyclerViewAdapter(patientList, dashboardViewModel,nurseName);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        setRetainInstance(true);
        return view;

    }

    private void preparePatientData() {
        patientList = new ArrayList<>();
        String url = "";
        if(dayType == DayType.evenDay) {
            url = "http://192.168.0.4:8080/anhe/patient/day?day=二四六";
            parsePatientList(url, new VolleyCallBack() {
                @Override
                public void onResult(VolleyStatus status) {
                    if(status == VolleyStatus.SUCCESS) {
                        mAdapter.notifyDataSetChanged();
                        }
                }
            });
        } else {
            url = "http://192.168.0.4:8080/anhe/patient/day?day=一三五";
            parsePatientList(url, new VolleyCallBack() {
                @Override
                public void onResult(VolleyStatus status) {
                    if(status == VolleyStatus.SUCCESS) {
                        mAdapter.notifyDataSetChanged();

                    }
                }
            });
            }
        }



    private void parsePatientList(String url, final VolleyCallBack volleyCallBack) {
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for(int i = 0; i < response.length(); i++){
                                    JSONObject object = null;
                                    try {
                                        object = response.getJSONObject(i);
                                        Integer pid = object.getInt("pid");
                                        String name = object.getString("name");
                                        String shift = object.getString("shift");
                                        String ic = object.getString("ic");
                                        String day = object.getString("day");
//                                        Log.d("patient jason object" , name + pid + shift + day + ic);

                                        patientList.add(new PatientsCardViewModel(pid, name, ic, shift, day));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    // Log.d("getting patient data from database", "CHLOE");
                                    volleyCallBack.onResult(VolleyStatus.SUCCESS);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("VOLLEY", error.toString());
                                volleyCallBack.onResult(VolleyStatus.FAIL);
                            }
                        } );

        volleyController.getInstance(mContext).addToRequestQueue(jsonArrayRequest);
    }

}
