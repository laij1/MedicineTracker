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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.CashFlowViewModel;
import com.clinic.anhe.medicinetracker.adapters.CashflowTodayRecyclerViewAdapter;
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
import java.util.List;
import java.util.Locale;

public class CashflowMonthFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private CashflowTodayRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<MedicineRecordCardViewModel> recordList;
    private List<MedicineRecordCardViewModel> diffList;
    private TextView mSelectEndDate;
    private ImageView mStartSearch;

    private int total = 0;
    private TextView mTotal;
    private EditText mActualCash;
    private ImageView mDifferenceButton;
    private boolean differenceButtonEnabled;

    private String firstDay;
    private TextView mDisplay;
    private Context mContext;
    private CashFlowViewModel cashFlowViewModel;
    String url = "";
    private VolleyController volleyController;
    private GlobalVariable globalVariable;


    public static CashflowMonthFragment newInstance(){
        CashflowMonthFragment fragment = new CashflowMonthFragment();
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ArgumentVariables.ARG_PATIENT_DETAIL_SEARCH_ENDDATE, mSelectEndDate.getText().toString());
        outState.putBoolean(ArgumentVariables.ARG_DIFFERENCEBUTTON, differenceButtonEnabled);
        outState.putString(ArgumentVariables.ARG_ACTUAL_CASH, mActualCash.getText().toString());
        outState.putString(ArgumentVariables.ARG_CASHFLOW_MONTH_TOTAL, mTotal.getText().toString());
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cashflow_month, container,false);

        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        String defaultDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);

        mSelectEndDate = view.findViewById(R.id.cashflow_month_enddate);
        mTotal = view.findViewById(R.id.cashflow_month_total);
        mActualCash = view.findViewById(R.id.cashflow_month_actualcash);

        if(savedInstanceState != null) {
            mSelectEndDate.setText(savedInstanceState.getString(ArgumentVariables.ARG_PATIENT_DETAIL_SEARCH_ENDDATE));
            differenceButtonEnabled = savedInstanceState.getBoolean(ArgumentVariables.ARG_DIFFERENCEBUTTON);
            mActualCash.setText(savedInstanceState.getString(ArgumentVariables.ARG_ACTUAL_CASH));
            mTotal.setText(savedInstanceState.getString(ArgumentVariables.ARG_CASHFLOW_MONTH_TOTAL));
        }
        else {
            mSelectEndDate.setText(defaultDate);
            differenceButtonEnabled = false;

        }

        cashFlowViewModel = ViewModelProviders.of(getParentFragment()).get(CashFlowViewModel.class);

        mContext = view.getContext();

        recordList = new ArrayList<>();
        diffList = new ArrayList<>();

        int month = c.get(Calendar.MONTH) + 1;

        mDisplay = view.findViewById(R.id.cashflow_month_display);
        String today = "" + c.get(Calendar.YEAR) + "年"
                + month  + "月份結算" ;
        mDisplay.setText(today);

        //set first day of the the month
        c.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = c.getTime();
        firstDay = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(firstDayOfMonth);

        mSelectEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "select end date", Toast.LENGTH_LONG ).show();
                EndDatePickerDialogFragment endDate = EndDatePickerDialogFragment.newInstance(
                        CashflowMonthFragment.this, mContext);
                endDate.show(getFragmentManager(), "enddate");
            }
        });

        mDifferenceButton = view.findViewById(R.id.cashflow_month_difference_button);
        mDifferenceButton.setEnabled(differenceButtonEnabled);
        if(!differenceButtonEnabled) {
            mDifferenceButton.setColorFilter(getResources().getColor(R.color.menuTextIconColor));
        } else {
            mDifferenceButton.setColorFilter(getResources().getColor(R.color.colorPrimaryDark));
        }

        mStartSearch = view.findViewById(R.id.cashflow_month_searchbutton);

        mRecyclerView = view.findViewById(R.id.cashflow_month_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new CashflowTodayRecyclerViewAdapter(cashFlowViewModel, "month");
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mDifferenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diffList.removeAll(diffList);
                int actual = Integer.parseInt(mActualCash.getText().toString());
                int diff = actual - total;
                url = "http://" + globalVariable.getInstance().getIpaddress() + ":" + globalVariable.getInstance().getPort()
                        + "/anhe/record/actualcash?cash=" + actual + "&diff=" + diff
                        + "&start=" + firstDay + "&end=" + mSelectEndDate.getText().toString();
                generateActualCash(url, new VolleyCallBack() {
                    @Override
                    public void onResult(VolleyStatus status) {
                        if(status == VolleyStatus.SUCCESS) {

                            cashFlowViewModel.getMonthListLiveData().setValue(recordList);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
                Toast.makeText(mContext, "正負金額產生中...", Toast.LENGTH_SHORT).show();
            }
        });

        mStartSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(mContext, "start search....", Toast.LENGTH_LONG ).show();
                url = "http://" + globalVariable.getInstance().getIpaddress() + ":" + globalVariable.getInstance().getPort()
                        + "/anhe/record/charged/rangedate?start=" +
                        firstDay + "&end=" + mSelectEndDate.getText().toString();
                refreshRecyclerView();
                parseRecordListData(url, new VolleyCallBack() {
                    @Override
                    public void onResult(VolleyStatus status) {
                        cashFlowViewModel.getMonthListLiveData().setValue(recordList);
                        mTotal.setText(String.valueOf(total));
                        mAdapter.notifyDataSetChanged();
                        mDifferenceButton.setEnabled(true);
                        differenceButtonEnabled = true;
                        mDifferenceButton.setColorFilter(getResources().getColor(R.color.colorPrimaryDark));
                    }
                });
            }
        });

        setRetainInstance(true);
        return view;
    }

    public void refreshRecyclerView() {
        recordList.removeAll(recordList);
        cashFlowViewModel.getMonthListLiveData().setValue(recordList);
        mAdapter.notifyDataSetChanged();

    }

    private void parseRecordListData(String url, final VolleyCallBack volleyCallBack) {
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                total = 0;
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
                                        String patientName = object.getString("patientName");
                                        MedicineRecordCardViewModel item = new MedicineRecordCardViewModel(rid, createAt, mid, name, quantity,
                                                subtotal, payment, pid, createBy);
                                        item.setPatientName(patientName);
                                        item.setChargeAt(chargeAt);
                                        item.setChargeBy(chargeBy);
                                        recordList.add(item);
                                        if(name.equalsIgnoreCase("實際金額")) {
                                            //do nothing
                                        } else {
                                            total += subtotal.intValue();
                                        }
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

    private void generateActualCash(String url, final VolleyCallBack volleyCallBack) {
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
                                        String[] createArray = createAt.split("T");
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
                                        String patientName = object.getString("patientName");
                                        MedicineRecordCardViewModel item = new MedicineRecordCardViewModel(rid, createArray[0], mid, name, quantity,
                                                subtotal, payment, pid, createBy);
                                        item.setPatientName(patientName);
                                        item.setChargeAt(chargeAt);
                                        item.setChargeBy(chargeBy);
                                        //Log.d("adding the actual cash data",name);
                                        if(!recordList.contains(item)) {
                                            Log.d("adding the actual cash data",name);
                                            recordList.add(item);
                                        } else {
                                            Log.d("data already in the record list",name);
                                        }
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


    public void setEndDateTextView(String endDate) {
        mSelectEndDate.setText(endDate);
    }
}
