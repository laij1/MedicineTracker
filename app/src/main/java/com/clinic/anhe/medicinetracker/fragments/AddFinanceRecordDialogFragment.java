package com.clinic.anhe.medicinetracker.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.AddFinanceRecordViewModel;
import com.clinic.anhe.medicinetracker.adapters.AddFinanceRecordRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.adapters.AddMedicineRecordRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.EmployeeCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddFinanceRecordDialogFragment  extends DialogFragment {

    private RadioGroup mRadioGrop;
    private String itemName= "";
    private EditText mSubtotal;
    private RecyclerView mSignature;
    private GridLayoutManager mLayoutManager;
    private AddFinanceRecordRecyclerViewAdapter mAdapter;

    private TextView mConfirmButton;
    private TextView mCancelButton;


    private Context mContext;
    private List<EmployeeCardViewModel> employeeList;
    private AddFinanceRecordViewModel addFinanceRecordViewModel;
    private VolleyController volleyController;
    String url = "";
    private GlobalVariable globalVariable;
    private static CashflowMonthFragment parent;



    public static AddFinanceRecordDialogFragment newInstance(CashflowMonthFragment parentFrag){
        AddFinanceRecordDialogFragment fragment = new AddFinanceRecordDialogFragment();
        parent = parentFrag;
        return fragment;
    }

    @Override
    public void onDestroyView() {
//        Log.d("OnDestoryView", "Chloe");
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_finance_record, container, false);

        mContext = view.getContext();
        employeeList = new ArrayList<>();

        addFinanceRecordViewModel = ViewModelProviders.of(this).get(AddFinanceRecordViewModel.class);

        mRadioGrop = view.findViewById(R.id.add_finance_record_radiogroup);
        mSubtotal = view.findViewById(R.id.add_finance_record_subtotal);
        mSignature = view.findViewById(R.id.add_finance_record_recyclerview);
        mConfirmButton = view.findViewById(R.id.add_finance_record_confirmbutton);
        mCancelButton = view.findViewById(R.id.add_finance_record_cancelbutton);
        mSignature.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 3);
        mAdapter = new AddFinanceRecordRecyclerViewAdapter(addFinanceRecordViewModel);
        mSignature.setLayoutManager(mLayoutManager);
        mSignature.setAdapter(mAdapter);

        url =  "http://" + globalVariable.getInstance().getIpaddress() + ":" + globalVariable.getInstance().getPort()
                + "/anho/employee/all";
        parseEmployeeData(url, new VolleyCallBack() {
            @Override
            public void onResult(VolleyStatus status) {
                if(status == VolleyStatus.SUCCESS) {
                    addFinanceRecordViewModel.getEmployListLiveData().setValue(employeeList);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });


        mRadioGrop.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.add_finance_record_allowance:
                        itemName = "補零用金";
                        break;
                    case R.id.add_finance_record_bank:
                        itemName = "存入銀行";
                        break;
                }
            }
        });

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addFinanceRecordViewModel.getSelectedEmployee().getValue().getEid() == -1) {
                    Toast.makeText(mContext, "請簽名", Toast.LENGTH_SHORT).show();
                } else if(itemName.equalsIgnoreCase("")) {
                    Toast.makeText(mContext, "請選擇出納項目", Toast.LENGTH_SHORT).show();
                } else {
                    Integer create_by = addFinanceRecordViewModel.getSelectedEmployee().getValue().getEid();
                    if(itemName.equalsIgnoreCase("補零用金")) {
                        url =  "http://" + globalVariable.getInstance().getIpaddress() + ":" + globalVariable.getInstance().getPort()
                                + "/anho/record/add?name=" + itemName
                                + "&mid=1" + "&pid=1" + "&pname=帳本"
                                + "&create_by=" + create_by + "&subtotal=" + mSubtotal.getText().toString();
                    } else if (itemName.equalsIgnoreCase("存入銀行")) {
                        url =  "http://" + globalVariable.getInstance().getIpaddress() + ":" + globalVariable.getInstance().getPort()
                                + "/anho/record/add?name=" + itemName
                                + "&mid=4" + "&pid=1" + "&pname=帳本"
                                + "&create_by=" + create_by + "&subtotal=" + mSubtotal.getText().toString();
                    }

                    addMedicineRecord(url, new VolleyCallBack() {
                            @Override
                            public void onResult(VolleyStatus status) {
                                if(status == VolleyStatus.SUCCESS) {
                                    Toast.makeText(mContext, "加入"+ itemName+ "成功", Toast.LENGTH_SHORT).show();
                                    parent.getRecordList();
                                    dismiss();
                                }
                            }
                        });
                    dismiss();

                }


            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });



        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().getWindow().setBackgroundDrawableResource(R.drawable.dialog_round);

        }

        setRetainInstance(true);
        return view;
    }


    private void parseEmployeeData(String url, final VolleyCallBack volleyCallBack) {
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for(int i = 0; i < response.length(); i++) {
                                    JSONObject object = null;
                                    try {
                                        object = response.getJSONObject(i);
                                        String name = object.getString("name");
                                        Integer eid = object.getInt("eid");
                                        String position = object.getString("position");
                                        EmployeeCardViewModel employee = new EmployeeCardViewModel(name, eid, position);
                                        if(eid != 1) {
                                            employeeList.add(employee);
                                        }
                                        Log.d("employeename:" , name);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
//                                Log.d("the shift record live data is loaded: ", ""+ dashboardViewModel.getShiftRecordListLiveData().getValue().size());
                                volleyCallBack.onResult(VolleyStatus.SUCCESS);
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

    public void addMedicineRecord(String url, final VolleyCallBack volleyCallBack) {
        StringRequest stringRequest =
                new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.toString().equals("saved")) {
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

        volleyController.getInstance(mContext).addToRequestQueue(stringRequest);
    }

}
