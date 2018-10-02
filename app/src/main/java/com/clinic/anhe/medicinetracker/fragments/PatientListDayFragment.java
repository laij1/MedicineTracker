package com.clinic.anhe.medicinetracker.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.adapters.PatientListRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.clinic.anhe.medicinetracker.utils.DayType;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;
import com.clinic.anhe.medicinetracker.utils.Shift;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


public class PatientListDayFragment extends Fragment implements ArgumentVariables {

    //patients cardview
    private List<PatientsCardViewModel> patientList;
    private RecyclerView mRecyclerView;
    private PatientListRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton mAddPatient;
    private Shift shift;
    private DayType dayType;
    private VolleyController volleyController;
    private Context mContext;
    private GlobalVariable globalVariable;
    private String ip;
    private String port;

    public static PatientListDayFragment newInstance(Shift shift, DayType dayType) {
        PatientListDayFragment fragment = new PatientListDayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PATIENT_SHIFT, shift.toString());
        args.putString(ARG_DAY_TYPE, dayType.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ARG_PATIENT_SHIFT, shift.toString());
        outState.putString(ARG_DAY_TYPE, dayType.toString());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  = inflater.inflate(R.layout.fragment_patient_list_day, container, false);
        mAddPatient = view.findViewById(R.id.add_patient_fab);
        mContext = getContext();

        //set ip and port
        ip = globalVariable.getInstance().getIpaddress();
        port = globalVariable.getInstance().getPort();

        if(savedInstanceState != null) {
            shift = shift.fromString(savedInstanceState.getString(ARG_PATIENT_SHIFT));
            dayType = dayType.fromString(savedInstanceState.getString(ARG_DAY_TYPE));
        }

        if(shift == null) {
            shift = shift.fromString(getArguments().getString(ARG_PATIENT_SHIFT));
            dayType= dayType.fromString(getArguments().getString(ARG_DAY_TYPE));
        }


        patientList = new ArrayList<>();
        preparePatientData();
        mRecyclerView = view.findViewById(R.id.patient_list_day_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        //here we need to filter
        mAdapter = new PatientListRecyclerViewAdapter(patientList, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAddPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddPatientDialogFragment addPatientDialogFragment = AddPatientDialogFragment.newInstance(PatientListDayFragment.this);
                addPatientDialogFragment.show(getFragmentManager(), "addPatient");
                //Toast.makeText(mContext, "adding patient..", Toast.LENGTH_SHORT).show();
            }
        });

        setRetainInstance(true);
        return view;

    }

    public void refreshrecyclerView() {
        preparePatientData();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(mAdapter != null) {
           // Log.d("we are in set user visble hint", "" + shift.toString() + " / " + dayType.toString());
            preparePatientData();
        }
    }

    private void preparePatientData() {
        String url = "";
        if(dayType == DayType.evenDay) {
        switch(shift) {
            case morning:
                url = "http://" + ip +
                        ":" + port + "/anhe/patient?day=二四六&shift=早班";
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
                url = "http://" + ip +
                        ":" + port  + "/anhe/patient?day=二四六&shift=中班";
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
                url = "http://" + ip +
                        ":" + port + "/anhe/patient?day=二四六&shift=晚班";
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
                    url = "http://" + ip +
                            ":" + port +  "/anhe/patient?day=一三五&shift=早班";
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
                    url = "http://" + ip +
                            ":" + port +  "/anhe/patient?day=一三五&shift=中班";
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
                    url = "http://" + ip +
                            ":" + port + "/anhe/patient?day=一三五&shift=晚班";
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
                                        PatientsCardViewModel patient = new PatientsCardViewModel(pid, name, ic, shift, day);
                                        if(!patientList.contains(patient)) {
                                            patientList.add(new PatientsCardViewModel(pid, name, ic, shift, day));
                                        }
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
                              //  Log.d("VOLLEY", error.toString());
                                volleyCallBack.onResult(VolleyStatus.FAIL);
                            }
                        } );

        volleyController.getInstance(mContext).addToRequestQueue(jsonArrayRequest);
    }

}