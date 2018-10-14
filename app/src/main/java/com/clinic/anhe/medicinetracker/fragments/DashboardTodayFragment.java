package com.clinic.anhe.medicinetracker.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.DashboardTodayViewModel;
import com.clinic.anhe.medicinetracker.adapters.CashflowTodayRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.adapters.DashboardTodayRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.model.MedicineRecordCardViewModel;
import com.clinic.anhe.medicinetracker.model.ShiftRecordModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
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

public class DashboardTodayFragment extends Fragment {

    private Context mContext;
    private GlobalVariable globalVariable;
    private VolleyController volleyController;
    private String ip ="";
    private String port ="";
    private String url ="";
    private List<MedicineRecordCardViewModel> recordList;
    private DashboardTodayViewModel dashboardTodayViewModel;

    private TextView mDisplay;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private DashboardTodayRecyclerViewAdapter mAdapter;


    public static DashboardTodayFragment newInstance(){
        DashboardTodayFragment fragment = new DashboardTodayFragment();
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard_today, container, false);

        mContext = view.getContext();

        //livedata
        dashboardTodayViewModel = ViewModelProviders.of(this).get(DashboardTodayViewModel.class);

        globalVariable = GlobalVariable.getInstance();
        ip = globalVariable.getIpaddress();
        port = globalVariable.getPort();

        recordList = new ArrayList<>();

        mDisplay = view.findViewById(R.id.dashboard_today_display);
        //TODO: get current day and display
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH) + 1;
        String today = "" + c.get(Calendar.YEAR) + "年"
                + month + "月" + c.get(Calendar.DAY_OF_MONTH) + "日" ;

        mDisplay.setText(today);

        mRecyclerView = view.findViewById(R.id.dashboard_today_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new DashboardTodayRecyclerViewAdapter(dashboardTodayViewModel);

        Date date = c.getTime();
        url = "http://" + ip + ":" + port + "/anho/record/day" +
                "?today=" + new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
        parseRecordListData(url, new VolleyCallBack() {
            @Override
            public void onResult(VolleyStatus status) {
                //TODO:update livedata
                if(status == VolleyStatus.SUCCESS) {
                    dashboardTodayViewModel.getMedicineRecordListLiveData().setValue(recordList);
                    mAdapter.notifyDataSetChanged();
                }
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
                                recordList.removeAll(recordList);
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
                                        // Log.d("medicine record jason object" , name + pid + createAt);
                                        MedicineRecordCardViewModel item = new MedicineRecordCardViewModel(rid, createAt, mid, name, quantity,
                                                subtotal, payment, pid, createBy);
                                        item.setChargeAt(chargeAt);
                                        item.setChargeBy(chargeBy);
                                        item.setPatientName(pName);
                                        //Log.d("setting chargeat and chargeby", "" + item.getPid());
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
                                //  Log.d("VOLLEY", error.toString());
                                volleyCallBack.onResult(VolleyStatus.FAIL);
                            }
                        } );

        volleyController.getInstance(mContext).addToRequestQueue(jsonArrayRequest);
    }

}
