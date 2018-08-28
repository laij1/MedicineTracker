package com.clinic.anhe.medicinetracker.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.clinic.anhe.medicinetracker.ViewModel.SelectedPatientViewModel;
import com.clinic.anhe.medicinetracker.adapters.PatientsRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.clinic.anhe.medicinetracker.utils.DayType;
import com.clinic.anhe.medicinetracker.utils.Shift;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class PatientDayFragment extends Fragment implements ArgumentVariables {
        //patients cardview
        private List<PatientsCardViewModel> patientList;
        private RecyclerView mRecyclerView;
        private PatientsRecyclerViewAdapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;
        private Context mContext;
        private Shift shift;
        private DayType dayType;
        private static SelectedPatientViewModel selectedPatientViewModel;
        private VolleyController volleyController;


        public static PatientDayFragment newInstance(Shift shift, DayType dayType) {
            PatientDayFragment fragment = new PatientDayFragment();
            Bundle args = new Bundle();
            args.putString(ARG_PATIENT_SHIFT, shift.toString());
            args.putString(ARG_DAY_TYPE, dayType.toString());
            fragment.setArguments(args);
            return fragment;
        }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_PATIENT_SHIFT, shift.toString());
        outState.putString(ARG_DAY_TYPE, dayType.toString());
    }

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            View view  = inflater.inflate(R.layout.fragment_patient_day, container, false);
            mContext = view.getContext();

            if(savedInstanceState != null) {
                shift = shift.fromString(savedInstanceState.getString(ARG_PATIENT_SHIFT));
                dayType = dayType.fromString(savedInstanceState.getString(ARG_DAY_TYPE));
            }

            if(shift == null) {
                shift = shift.fromString(getArguments().getString(ARG_PATIENT_SHIFT));
                dayType = dayType.fromString(getArguments().getString(ARG_DAY_TYPE));
            }

            //TODO:
            selectedPatientViewModel = ViewModelProviders.of(getParentFragment().getParentFragment()).get(SelectedPatientViewModel.class);

            preparePatientData();
            mRecyclerView = view.findViewById(R.id.patient_day_recyclerview);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(getContext());
            mAdapter = new PatientsRecyclerViewAdapter(patientList, selectedPatientViewModel);

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);

            selectedPatientViewModel.getPatientLiveData().observe(getParentFragment().getParentFragment(), new Observer<PatientsCardViewModel>() {
            @Override
            public void onChanged(@Nullable PatientsCardViewModel patientsCardViewModel) {
                Log.d("I have the selected patient in PatientDayFragment", patientsCardViewModel.getPatientName());
                //update all patients in different tabs when a new patient is selected
                mAdapter.notifyDataSetChanged();
                }
            });

            setRetainInstance(true);
            return view;

        }


    private void preparePatientData() {
        patientList = new ArrayList<>();
        String url = "";
        if(dayType == DayType.evenDay) {
            switch(shift) {
                case morning:
                    url = "http://192.168.0.4:8080/anhe/patient?day=二四六&shift=早班";
                    parsePatientList(url, new VolleyCallBack() {
                        @Override
                        public void onResult(VolleyStatus status) {
                            if(status == VolleyStatus.SUCCESS) {
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    break;
                case afternoon:
                    url = "http://192.168.0.4:8080/anhe/patient?day=二四六&shift=中班";
                    parsePatientList(url, new VolleyCallBack() {
                        @Override
                        public void onResult(VolleyStatus status) {
                            if(status == VolleyStatus.SUCCESS) {
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    break;
                case night:
                    url = "http://192.168.0.4:8080/anhe/patient?day=二四六&shift=晚班";
                    parsePatientList(url, new VolleyCallBack() {
                        @Override
                        public void onResult(VolleyStatus status) {
                            if(status == VolleyStatus.SUCCESS) {
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    break;
            }
        } else {
            switch(shift) {
                case morning:
                    url = "http://192.168.0.4:8080/anhe/patient?day=一三五&shift=早班";
                    parsePatientList(url, new VolleyCallBack() {
                        @Override
                        public void onResult(VolleyStatus status) {
                            if(status == VolleyStatus.SUCCESS) {
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    break;
                case afternoon:
                    url = "http://192.168.0.4:8080/anhe/patient?day=一三五&shift=中班";
                    parsePatientList(url, new VolleyCallBack() {
                        @Override
                        public void onResult(VolleyStatus status) {
                            if(status == VolleyStatus.SUCCESS) {
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    break;
                case night:
                    url = "http://192.168.0.4:8080/anhe/patient?day=一三五&shift=晚班";
                    parsePatientList(url, new VolleyCallBack() {
                        @Override
                        public void onResult(VolleyStatus status) {
                            if(status == VolleyStatus.SUCCESS) {
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
                    break;

            }
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