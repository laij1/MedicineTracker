package com.clinic.anhe.medicinetracker.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.CashFlowViewModel;
import com.clinic.anhe.medicinetracker.adapters.CashflowTodayRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.adapters.PatientDetailCashRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.MedicineRecordCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;
import com.clinic.anhe.medicinetracker.utils.PaymentType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CashflowTodayFragment extends Fragment {

    private TextView mDisplay;
    private TextView mRevenue;
    private RecyclerView mRecyclerView;
    private Context mContext;
    private String url = "";
    private VolleyController volleyController;
    private GlobalVariable globalVariable;
    private String ip;
    private String port;
    private String todayDate;
    private List<MedicineRecordCardViewModel> recordList;
//    private int totalRevenue = 0;
    private LinearLayoutManager mLayoutManager;
    private CashflowTodayRecyclerViewAdapter mAdapter;
    private FloatingActionButton mAddOtherMedicineFAB;
    private CashFlowViewModel cashFlowViewModel;



    public static CashflowTodayFragment newInstance(){
        CashflowTodayFragment fragment = new CashflowTodayFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cashflow_today, container, false);

        cashFlowViewModel = ViewModelProviders.of(getParentFragment()).get(CashFlowViewModel.class);

        mContext = view.getContext();

        globalVariable = GlobalVariable.getInstance();
        ip = globalVariable.getIpaddress();
        port = globalVariable.getPort();

        recordList = new ArrayList<>();

        mAddOtherMedicineFAB = view.findViewById(R.id.add_other_medicine_record_fab);
        mDisplay = view.findViewById(R.id.cashflow_today_display);
        //TODO: get current day and display
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        String today = "" + c.get(Calendar.YEAR) + "年"
                        + month + "月" + c.get(Calendar.DAY_OF_MONTH) + "日" ;

        mDisplay.setText(today);
//        mRevenue = view.findViewById(R.id.cashflow_today_revenue);


        mRecyclerView = view.findViewById(R.id.cashflow_today_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new CashflowTodayRecyclerViewAdapter(cashFlowViewModel, "today");

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);

        getRecordList();

        mAddOtherMedicineFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMedicineRecordDialogFragment addMedicineRecordDialogFragment = AddMedicineRecordDialogFragment.newInstance(CashflowTodayFragment.this);
                addMedicineRecordDialogFragment.show(getFragmentManager(), "addMedicineRecord");

            }
        });


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        setRetainInstance(true);
        return view;
    }

    public void getRecordList(){
        url = "http://" + ip + ":" + port + "/anhe/record/chargedate?start=" + todayDate;
        parseRecordListData(url, new VolleyCallBack() {
            @Override
            public void onResult(VolleyStatus status) {
                //TODO:update livedata
                cashFlowViewModel.getTodayListLiveData().setValue(recordList);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void parseRecordListData(String url, final VolleyCallBack volleyCallBack) {
        recordList.removeAll(recordList);
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
//                                totalRevenue = 0;
                                for(int i = 0; i < response.length(); i++){
                                    JSONObject object = null;
                                    try {
                                        object = response.getJSONObject(i);

                                        String createAt = object.getString("createAt");
                                        Integer rid = object.getInt("rid");
                                        Integer pid = object.getInt("pid");
                                        Integer mid = object.getInt("mid");
                                        String name = object.getString("medicineName");
                                        String pName = object.getString("patientName");
                                        Integer quantity = object.getInt("quantity");
                                        Integer subtotal = object.getInt("subtotal");
                                        String createBy = object.getString("createBy");
                                        String payment = object.getString("payment");
                                        String chargeAt = object.getString("chargeAt");
                                        String chargeBy = object.getString("chargeBy");
                                      //  Log.d("medicine record jason object" , name + pid + createAt);
                                        MedicineRecordCardViewModel item = new MedicineRecordCardViewModel(rid, createAt, mid, name, quantity,
                                                subtotal, payment, pid, createBy);
                                        item.setChargeAt(chargeAt);
                                        item.setChargeBy(chargeBy);
                                        item.setPatientName(pName);
                                      //  Log.d("setting chargeat and chargeby", "" + item.getPid());
                                        if(recordList.contains(item) || name.equalsIgnoreCase("實際金額") || name.equalsIgnoreCase("正負金額")
                                                ||name.equalsIgnoreCase("補零用金") || name.equalsIgnoreCase("存入銀行")) {
                                            //do nothing
                                        } else {
                                            recordList.add(item);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                  //  Log.d("getting patient data from database", "CHLOE");
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
