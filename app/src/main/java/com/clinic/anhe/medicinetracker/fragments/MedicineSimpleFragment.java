package com.clinic.anhe.medicinetracker.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.CartViewModel;
import com.clinic.anhe.medicinetracker.ViewModel.MedicineManageViewModel;
import com.clinic.anhe.medicinetracker.adapters.MedicineRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.adapters.MedicineSimpleRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.model.MedicineRecordCardViewModel;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;
import com.clinic.anhe.medicinetracker.utils.MedicineType;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;

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

public class MedicineSimpleFragment extends Fragment implements View.OnKeyListener {

    private MedicineType medicineType;
    private RecyclerView mRecyclerView;
    private MedicineSimpleRecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<MedicineCardViewModel> medicineList;
    private VolleyController volleyController;
    private GlobalVariable globalVariable;
    private String ip;
    private String port;
    private Context mContext;
    private FloatingActionButton mAddItem;
    private MedicineManageViewModel medicineManageViewModel;

    public static MedicineSimpleFragment newInstance(MedicineType medicineType) {
        MedicineSimpleFragment fragment = new MedicineSimpleFragment();
        Bundle args = new Bundle();
        args.putString(ArgumentVariables.ARG_MEDICINE_TYPE, medicineType.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //TODO: medicineType could be null....
        outState.putString(ArgumentVariables.ARG_MEDICINE_TYPE, medicineType.toString());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view  = inflater.inflate(R.layout.fragment_medicine_simple, container, false);

        medicineManageViewModel = ViewModelProviders.of(getParentFragment()).get(MedicineManageViewModel.class);

        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        String endDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
        //set first day of the the month
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = cal.getTime();
        String startDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(firstDayOfMonth);
        String url = "http://" + globalVariable.getInstance().getIpaddress() + ":" + globalVariable.getInstance().getPort() + "/anho/record/all/rangedate?start=" +
                startDate + "&end=" + endDate;


        mContext = view.getContext();

        globalVariable = GlobalVariable.getInstance();
        ip = globalVariable.getIpaddress();
        port = globalVariable.getPort();

        if(savedInstanceState != null) {
            medicineType = medicineType.fromString(savedInstanceState.getString(ArgumentVariables.ARG_MEDICINE_TYPE));
        }

        if(medicineType == null) {
            medicineType = medicineType.fromString(getArguments().getString(ArgumentVariables.ARG_MEDICINE_TYPE));
        }
//
        medicineList = new ArrayList<>();
        prepareMedicineData();


        mAddItem = view.findViewById(R.id.add_medicine_fab);

        mRecyclerView = view.findViewById(R.id.medicine_simple_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        //mLayoutManager = new LinearLayoutManager(getContext());
         mLayoutManager = new GridLayoutManager(getContext(), 2);

        mAdapter = new MedicineSimpleRecyclerViewAdapter(medicineType, medicineList, this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        parseRecordListData(url, new VolleyCallBack() {
            @Override
            public void onResult(VolleyStatus status) {
                if(status == VolleyStatus.SUCCESS) {
                    Log.d("in medicine simple, get all the record", "sucess");
                    //update recyclerview
                    refreshRecyclerView();
                }
            }
        });

        mAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddMedicineDialogFragment addMedicineDialogFragment = AddMedicineDialogFragment.newInstance(MedicineSimpleFragment.this, medicineType, medicineList);
                addMedicineDialogFragment.show(getFragmentManager(), "addmedicine");
//                Toast.makeText(mContext, "adding medicine...", Toast.LENGTH_LONG).show();
            }
        });

//setRetainInstance to true is important so that onSaveInstanceState will work
        setRetainInstance(true);
        return view;
    }

    public MedicineManageViewModel getMedicineManageViewModel() {
        return medicineManageViewModel;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                //use getChildFragmentManager instead of getSupportedFragmentManager
                getActivity().getSupportFragmentManager().popBackStack();
                return true;
            }
        }

        return false;
    }

    public void refreshRecyclerView(){
        mAdapter.notifyDataSetChanged();
    }
    private void prepareMedicineData() {
        String url = "http://" + ip + ":" + port + "/anho/medicine?category=" + medicineType.toString();
               parseMedicineList(url, new VolleyCallBack() {
                   @Override
                   public void onResult(VolleyStatus status) {
                       if (status == VolleyStatus.SUCCESS) {
                           mAdapter.notifyDataSetChanged();
                       }
                   }
               });
    }


    private void parseMedicineList(String url, final VolleyCallBack volleyCallBack) {
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for(int i = 0; i < response.length(); i++){
                                    JSONObject object = null;
                                    try {
                                        object = response.getJSONObject(i);
                                        String category = object.getString("category");
                                        String name = object.getString("name");
                                        Integer id = object.getInt("mid");
                                        Integer price = object.getInt("price");
                                        String dose = object.getString("dose");
                                        Integer stock = object.getInt("stock");
                                       // Log.d("jason object" , name + id +price +dose + stock);

                                        medicineList.add(new MedicineCardViewModel(id, name, Integer.toString(price), dose, stock, category));
//
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
                        } ){
                    /**
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
                    }
                };

        volleyController.getInstance(mContext).addToRequestQueue(jsonArrayRequest);
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
//                                        String chargeAt = object.getString("chargeAt");
//                                        String chargeBy = object.getString("chargeBy");
                                        String patientName = object.getString("patientName");
                                        MedicineRecordCardViewModel item = new MedicineRecordCardViewModel(rid, createAt, mid, name, quantity,
                                                subtotal, payment, pid, createBy);
                                        item.setPatientName(patientName);
//                                        item.setChargeAt(chargeAt);
//                                        item.setChargeBy(chargeBy);
                                        if(!medicineManageViewModel.getMedicineListLiveData().getValue().contains(item)){
                                            medicineManageViewModel.getMedicineListLiveData().getValue().add(item);
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
}
