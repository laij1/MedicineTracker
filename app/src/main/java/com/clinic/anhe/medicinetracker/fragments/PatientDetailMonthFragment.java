package com.clinic.anhe.medicinetracker.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.CheckoutViewModel;
import com.clinic.anhe.medicinetracker.adapters.PatientDetailCashRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.MedicineRecordCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.clinic.anhe.medicinetracker.utils.CounterFab;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;
import com.clinic.anhe.medicinetracker.utils.PaymentType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientDetailMonthFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private PatientDetailCashRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<MedicineRecordCardViewModel> recordList;
    private VolleyController volleyController;
    private GlobalVariable globalVariable;
    private String ip;
    private String port;
    private Context mContext;
    private String selectedPatientName;
    private String selectedPatientIC;
    private Integer selectedPatientPID;
    private TextView mPatientName;
    private TextView mPatientIC;
    private TextView mCheckoutTotal;
    private int checkoutTotal;
    String url = "";
    private CounterFab counterFab;
    private CheckoutViewModel checkoutViewModel;

    public static PatientDetailMonthFragment newInstance(String selectedPatientName, String selectedPatientIC, Integer PID) {

        PatientDetailMonthFragment fragment = new PatientDetailMonthFragment();
        Bundle args = new Bundle();
        args.putString(ArgumentVariables.ARG_SELECTED_PATIENT_NAME, selectedPatientName);
        args.putString(ArgumentVariables.ARG_SELECTED_PATIENT_ID, selectedPatientIC);
        args.putInt(ArgumentVariables.ARG_SELECTED_PATIENT_PID, PID);

        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(ArgumentVariables.ARG_SELECTED_PATIENT_NAME, selectedPatientName);
        outState.putString(ArgumentVariables.ARG_SELECTED_PATIENT_ID, selectedPatientIC);
        outState.putInt(ArgumentVariables.ARG_SELECTED_PATIENT_PID, selectedPatientPID);
       // outState.putInt(ArgumentVariables.ARG_MONTH_CHECKOUT_TOTAL, checkoutTotal);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_patient_detail_month, container, false);
        if(savedInstanceState != null) {
            selectedPatientName = savedInstanceState.getString(ArgumentVariables.ARG_SELECTED_PATIENT_NAME);
            selectedPatientIC = savedInstanceState.getString(ArgumentVariables.ARG_SELECTED_PATIENT_ID);
            selectedPatientPID = savedInstanceState.getInt(ArgumentVariables.ARG_SELECTED_PATIENT_PID);
//            checkoutTotal = savedInstanceState.getInt(ArgumentVariables.ARG_MONTH_CHECKOUT_TOTAL);
        }
        if(selectedPatientName == null) {
            selectedPatientName = getArguments().getString(ArgumentVariables.ARG_SELECTED_PATIENT_NAME);
            selectedPatientIC = getArguments().getString(ArgumentVariables.ARG_SELECTED_PATIENT_ID);
            selectedPatientPID = getArguments().getInt(ArgumentVariables.ARG_SELECTED_PATIENT_PID);
        }

        checkoutViewModel = ViewModelProviders.of(getParentFragment()).get(CheckoutViewModel.class);
        mContext = view.getContext();

        globalVariable = GlobalVariable.getInstance();
        ip = globalVariable.getIpaddress();
        port = globalVariable.getPort();

        recordList = new ArrayList<>();

        Log.d("selected patient in patient month detail", selectedPatientName + selectedPatientIC + selectedPatientPID);
        mPatientName = view.findViewById(R.id.patient_detail_month_name);
        mPatientIC = view.findViewById(R.id.patient_detail_month_ic);
        mPatientName.setText(selectedPatientName);
        mPatientIC.setText(selectedPatientIC);

        counterFab = view.findViewById(R.id.patient_detail_month_fab);
        mCheckoutTotal = view.findViewById(R.id.patient_detail_month_checkout);
        checkoutTotal = 0;
        for(MedicineRecordCardViewModel m : checkoutViewModel.getMonthCheckoutLiveData().getValue()){
            checkoutTotal += m.getSubtotal();
        }
        mCheckoutTotal.setText(String.valueOf(checkoutTotal));

        mRecyclerView = view.findViewById(R.id.patient_detail_month_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new PatientDetailCashRecyclerViewAdapter(recordList, mContext, this, counterFab, checkoutViewModel);


        //TODO: needs to get pid from parent fragment
        url = "http://" + ip + ":" + port + "/anho/record/pid/unpaid?pid="+ selectedPatientPID;
        parseRecordListData(url, new VolleyCallBack() {
            @Override
            public void onResult(VolleyStatus status) {
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
                                        String patientName = object.getString("patientName");
                                        Log.d("medicine record jason object" , name + pid + createAt);

                                        if(payment.equalsIgnoreCase(PaymentType.MONTH.toString())){
                                            MedicineRecordCardViewModel record = new MedicineRecordCardViewModel(rid, createAt, mid, name, quantity,
                                                    subtotal, payment, pid, createBy);
                                            record.setPatientName(patientName);
                                            recordList.add(record);
                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.d("getting patient data from database", "CHLOE");
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
                        } ){/**
                 * Passing some request headers
                 */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = "admin1:secret1";
                    String auth = "Basic "
                            + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }};

        volleyController.getInstance(mContext).addToRequestQueue(jsonArrayRequest);
    }

    public void refreshRecyclerView(int index) {
        if(checkoutViewModel.getMonthCheckoutLiveData().getValue().contains(recordList.get(index))) {
            checkoutViewModel.getMonthCheckoutLiveData().getValue().remove(recordList.get(index));
            counterFab.decrease();
        }
        recordList.remove(index);
        mAdapter.notifyDataSetChanged();
    }

    public void refreshRecyclerViewFromCheckout() {
        for(MedicineRecordCardViewModel item : checkoutViewModel.getMonthCheckoutLiveData().getValue()){
            recordList.remove(item);
        }
        checkoutViewModel.getMonthCheckoutLiveData().getValue().removeAll(checkoutViewModel.getMonthCheckoutLiveData().getValue());
        counterFab.setCount(0);
        mAdapter.notifyDataSetChanged();
    }

    public void setCheckoutTotal(int subtotal) {
        checkoutTotal = subtotal;
        mCheckoutTotal.setText(String.valueOf(subtotal));
    }
}
