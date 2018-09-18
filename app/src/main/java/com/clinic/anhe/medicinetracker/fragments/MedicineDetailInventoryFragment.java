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
import com.clinic.anhe.medicinetracker.ViewModel.InventoryViewModel;
import com.clinic.anhe.medicinetracker.ViewModel.MedicineDetailViewModel;
import com.clinic.anhe.medicinetracker.adapters.InventoryRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.adapters.MedicineDetailSearchRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.InventoryCardViewModel;
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

public class MedicineDetailInventoryFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private InventoryRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<InventoryCardViewModel> recordList;

    private VolleyController volleyController;
    private GlobalVariable globalVariable;
    private String ip;
    private String port;
    private Context mContext;
    private TextView mDisplay;

    private TextView mSelectStartDate;
    private TextView mSelectEndDate;
    private ImageView mStartSearch;
    String url = "";
    private String medicineName;
    private InventoryViewModel inventoryViewModel;



    public static MedicineDetailInventoryFragment newInstance(String medicineName) {
        MedicineDetailInventoryFragment fragment = new MedicineDetailInventoryFragment();
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
        View view = inflater.inflate(R.layout.fragment_medicine_detail_inventory, container, false);

        if(savedInstanceState != null) {
            medicineName = savedInstanceState.getString(ArgumentVariables.ARG_SELECTED_MEDICINE_NAME);
        }

        if(medicineName == null) {
            medicineName = getArguments().getString(ArgumentVariables.ARG_SELECTED_MEDICINE_NAME);
        }

        mContext = view.getContext();
        inventoryViewModel = ViewModelProviders.of(this).get(InventoryViewModel.class);

        globalVariable = GlobalVariable.getInstance();
        ip = globalVariable.getIpaddress();
        port = globalVariable.getPort();

        recordList = new ArrayList<>();

        mDisplay = view.findViewById(R.id.medicine_detail_inventory_display);
        mDisplay.setText(medicineName + " 進貨紀錄" );

        //here for the search
        mSelectStartDate = view.findViewById(R.id.medicine_detail_inventory_startdate);
        mSelectEndDate = view.findViewById(R.id.medicine_detail_inventory_enddate);
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
        mStartSearch = view.findViewById(R.id.medicine_detail_inventory_button);

        mSelectStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "select start date", Toast.LENGTH_LONG ).show();
                StartDatePickerDialogFragment startDate = StartDatePickerDialogFragment.newInstance(
                        MedicineDetailInventoryFragment.this, mContext);
                startDate.show(getFragmentManager(),"startdate");
            }
        });

        mSelectEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "select end date", Toast.LENGTH_LONG ).show();
                EndDatePickerDialogFragment endDate = EndDatePickerDialogFragment.newInstance(
                        MedicineDetailInventoryFragment.this, mContext);
                endDate.show(getFragmentManager(), "enddate");
            }
        });

        mStartSearch.setOnClickListener(new View.OnClickListener() {
//
//            localhost:8080/anhe/inventory/medname/rangedate?medname=HDF&start=2018-09-01&end=2018-09-20
            @Override
            public void onClick(View v) {
                url = "http://" + ip + ":" + port + "/anhe/inventory/medname/rangedate?medname="+ medicineName +"&start=" +
                        mSelectStartDate.getText().toString() + "&end=" + mSelectEndDate.getText().toString();
                refreshRecyclerView();
                parseRecordListData(url, new VolleyCallBack() {
                    @Override
                    public void onResult(VolleyStatus status) {
                        inventoryViewModel.getInventoryListLiveData().postValue(recordList);
                        mAdapter.notifyDataSetChanged();
                    }
                });

            }
        });


        mRecyclerView = view.findViewById(R.id.medicine_detail_inventory_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new InventoryRecyclerViewAdapter(inventoryViewModel);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        return view;
    }


    public void refreshRecyclerView() {
        recordList.removeAll(recordList);
        inventoryViewModel.getInventoryListLiveData().setValue(recordList);
        mAdapter.notifyDataSetChanged();

    }

    public void setStartDateTextView(String startDate) {
        mSelectStartDate.setText(startDate);
    }

    public void setEndDateTextView(String endDate) {
        mSelectEndDate.setText(endDate);
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
                                        Integer iid = object.getInt("iid");
                                        Integer mid = object.getInt("mid");
                                        String name = object.getString("medicine");
                                        Integer amount = object.getInt("amount");
                                        InventoryCardViewModel item = new InventoryCardViewModel(iid, mid, name, amount, createAt);
                                        Log.d("inventory is", createAt + iid+ mid+ name+amount);
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


}
