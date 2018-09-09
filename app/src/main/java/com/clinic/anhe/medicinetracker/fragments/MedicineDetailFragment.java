package com.clinic.anhe.medicinetracker.fragments;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.CashFlowViewModel;
import com.clinic.anhe.medicinetracker.ViewModel.MedicineDetailViewModel;
import com.clinic.anhe.medicinetracker.adapters.CashflowTodayRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.adapters.MedicineDetailRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.MedicineRecordCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
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

public class MedicineDetailFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private MedicineDetailRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<MedicineRecordCardViewModel> recordList;
    private Map<Integer, String> patientMap;
    private VolleyController volleyController;
    private GlobalVariable globalVariable;
    private String ip;
    private String port;
    private Context mContext;
    private TextView mDisplay;
    //here is for the select dates layout
    private TextView mSelectStartDate;
    private TextView mSelectEndDate;
    private ImageView mStartSearch;
    String url = "";
    private String medicineName;
    private MedicineDetailViewModel medicineDetailViewModel;

    public static MedicineDetailFragment newInstance(String medicineName) {
        MedicineDetailFragment fragment = new MedicineDetailFragment();
        Bundle args = new Bundle();
        args.putString(ArgumentVariables.ARG_SELECTED_MEDICINE_NAME, medicineName);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ArgumentVariables.ARG_PATIENT_DETAIL_SEARCH_STARTDATE, mSelectStartDate.getText()!=null?mSelectStartDate.getText().toString():"");
        outState.putString(ArgumentVariables.ARG_PATIENT_DETAIL_SEARCH_ENDDATE, mSelectEndDate.getText()!=null?mSelectEndDate.getText().toString():"");
        outState.putString(ArgumentVariables.ARG_SELECTED_MEDICINE_NAME, medicineName);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medicine_detail, container, false);
        if(savedInstanceState != null) {
            medicineName = savedInstanceState.getString(ArgumentVariables.ARG_SELECTED_MEDICINE_NAME);
        }

        if(medicineName == null) {
            medicineName = getArguments().getString(ArgumentVariables.ARG_SELECTED_MEDICINE_NAME);
        }
        mContext = view.getContext();

        medicineDetailViewModel = ViewModelProviders.of(this).get(MedicineDetailViewModel.class);

        globalVariable = GlobalVariable.getInstance();
        ip = globalVariable.getIpaddress();
        port = globalVariable.getPort();

        recordList = new ArrayList<>();

        mDisplay = view.findViewById(R.id.medicine_detail_display);
        mDisplay.setText(medicineName);

        //here for the search
        mSelectStartDate = view.findViewById(R.id.medicine_detail_startdate);
        mSelectEndDate = view.findViewById(R.id.medicine_detail_enddate);
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String defaultDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);

        if(savedInstanceState != null) {
            mSelectStartDate.setText(savedInstanceState.getString(ArgumentVariables.ARG_PATIENT_DETAIL_SEARCH_STARTDATE));
            mSelectEndDate.setText(savedInstanceState.getString(ArgumentVariables.ARG_PATIENT_DETAIL_SEARCH_ENDDATE));
        }
        else {
            mSelectStartDate.setText(defaultDate);
            mSelectEndDate.setText(defaultDate);
        }
        mStartSearch = view.findViewById(R.id.medicine_detail_button);

        mSelectStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "select start date", Toast.LENGTH_LONG ).show();
                StartDatePickerDialogFragment startDate = StartDatePickerDialogFragment.newInstance(
                        MedicineDetailFragment.this, mContext);
                startDate.show(getFragmentManager(),"startdate");
            }
        });

        mSelectEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "select end date", Toast.LENGTH_LONG ).show();
                EndDatePickerDialogFragment endDate = EndDatePickerDialogFragment.newInstance(
                        MedicineDetailFragment.this, mContext);
                endDate.show(getFragmentManager(), "enddate");
            }
        });

        mStartSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = "http://" + ip + ":" + port + "/anhe/record/medname/rangedate?medname="+ medicineName +"&start=" +
                        mSelectStartDate.getText().toString() + "&end=" + mSelectEndDate.getText().toString();
                parseRecordListData(url, new VolleyCallBack() {
                    @Override
                    public void onResult(VolleyStatus status) {
                        medicineDetailViewModel.getMedicineListLiveData().setValue(recordList);
                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });


        mRecyclerView = view.findViewById(R.id.medicine_detail_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new MedicineDetailRecyclerViewAdapter(medicineDetailViewModel);
        //TODO: livedata
        patientMap = new HashMap<>();
        url = "http://" + ip +
                ":" + port + "/anhe/patient/all";
        populatePatientMap(url, new VolleyCallBack() {
            @Override
            public void onResult(VolleyStatus status) {
                medicineDetailViewModel.getPatientMapLiveData().setValue(patientMap);
                mAdapter.notifyDataSetChanged();
            }
        });
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }

    private void parseRecordListData(String url, final VolleyCallBack volleyCallBack) {
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for(int i = 0; i < response.length(); i++){
                                    JSONObject object = null;
                                    try {
                                        object = response.getJSONObject(i);

                                        String createAt = object.getString("createAt");
                                        Integer rid = object.getInt("rid");
                                        Integer pid = object.getInt("pid");
                                        Integer mid = object.getInt("mid");
                                        String name = object.getString("medicineName");
                                        Integer quantity = object.getInt("quantity");
                                        Integer subtotal = object.getInt("subtotal");
                                        String createBy = object.getString("createBy");
                                        String payment = object.getString("payment");
                                        String chargeAt = object.getString("chargeAt");
                                        String chargeBy = object.getString("chargeBy");
                                        MedicineRecordCardViewModel item = new MedicineRecordCardViewModel(rid, createAt, mid, name, quantity,
                                                subtotal, payment, pid, createBy);
                                        item.setChargeAt(chargeAt);
                                        item.setChargeBy(chargeBy);
                                        recordList.add(item);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

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

    public void setStartDateTextView(String startDate) {
        mSelectStartDate.setText(startDate);
    }

    public void setEndDateTextView(String endDate) {
        mSelectEndDate.setText(endDate);
    }

    public void populatePatientMap(String url, final VolleyCallBack volleyCallBack) {
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
//                                        String shift = object.getString("shift");
//                                        String ic = object.getString("ic");
//                                        String day = object.getString("day");
//
                                        patientMap.put(new Integer(pid), name);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.d("getting patient data from database", "CashFlowViewModel");
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