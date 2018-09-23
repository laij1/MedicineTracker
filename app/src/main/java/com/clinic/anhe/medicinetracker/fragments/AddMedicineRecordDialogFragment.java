package com.clinic.anhe.medicinetracker.fragments;

import android.app.Dialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.AddMedicineRecordViewModel;
import com.clinic.anhe.medicinetracker.adapters.AddMedicineRecordRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.adapters.SignatureRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.EmployeeCardViewModel;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.model.ShiftRecordModel;
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

public class AddMedicineRecordDialogFragment extends DialogFragment {

    private EditText mTarget;
    private EditText mItem;
    private EditText mSubtotal;
    private RecyclerView mSignature;
    private GridLayoutManager mLayoutManager;
    private AddMedicineRecordRecyclerViewAdapter mAdapter;

    private TextView mConfirmButton;
    private TextView mCancelButton;

    private AddMedicineRecordViewModel addMedicineRecordViewModel;
    private List<EmployeeCardViewModel> employeeList;
//    private static Map<String, Integer> employee;
    private Map<String, Integer> patientMap;

    private Context mContext;
    private VolleyController volleyController;
    String url = "";
    private GlobalVariable globalVariable;

    public static AddMedicineRecordDialogFragment newInstance() {
        AddMedicineRecordDialogFragment fragment = new AddMedicineRecordDialogFragment();
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
        View view = inflater.inflate(R.layout.dialog_add_medicine_record, container, false);
        mContext = view.getContext();

        addMedicineRecordViewModel = ViewModelProviders.of(this).get(AddMedicineRecordViewModel.class);
        employeeList = new ArrayList<>();
        patientMap = new HashMap<>();

        mTarget = view.findViewById(R.id.add_medicine_record_target);
        mItem = view.findViewById(R.id.add_medicine_record_item);
        mSubtotal = view.findViewById(R.id.add_medicine_record_subtotal);

        mConfirmButton = view.findViewById(R.id.add_medicine_record_confirmbutton);
        mCancelButton = view.findViewById(R.id.add_medicine_record_cancelbutton);

        mSignature = view.findViewById(R.id.add_medicine_record_recyclerview);
        mSignature.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 3);
        mAdapter = new AddMedicineRecordRecyclerViewAdapter(addMedicineRecordViewModel);
        mSignature.setLayoutManager(mLayoutManager);
        mSignature.setAdapter(mAdapter);

        url =  "http://" + globalVariable.getInstance().getIpaddress() + ":" + globalVariable.getInstance().getPort()
                + "/anhe/employee/all";
        parseEmployeeData(url, new VolleyCallBack() {
            @Override
            public void onResult(VolleyStatus status) {
                if(status == VolleyStatus.SUCCESS) {
                    addMedicineRecordViewModel.getEmployListLiveData().setValue(employeeList);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        url = "http://" + globalVariable.getInstance().getIpaddress() + ":" + globalVariable.getInstance().getPort()
                + "/anhe/patient/all";
        populatePatientMap(url, new VolleyCallBack() {
            @Override
            public void onResult(VolleyStatus status) {
                if(status == VolleyStatus.SUCCESS) {
                    addMedicineRecordViewModel.getPatientMapLiveData().setValue(patientMap);
                }
            }
        });


        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addMedicineRecordViewModel.getSelectedEmployee().getValue().getEid()== -1) {
                    Toast.makeText(mContext, "請簽名", Toast.LENGTH_SHORT).show();
                } else if (mItem.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(mContext, "請輸入項目名稱", Toast.LENGTH_SHORT).show();
                } else {
                    //see if target is a patient
                    String targetName = mTarget.getText().toString();
                    Integer targetPID;
                    if(addMedicineRecordViewModel.getPatientMapLiveData().getValue().containsKey(targetName)) {
                        Log.d("we have patient in our database" ,targetName);
                        targetPID = addMedicineRecordViewModel.getPatientMapLiveData().getValue().get(targetName);
                    } else {
                        targetPID = Integer.valueOf(3);
                        Log.d("we do not have patient in our database" ,targetName);
                    }

                    Integer create_by = addMedicineRecordViewModel.getSelectedEmployee().getValue().getEid();
                    //if pid is not 0,
                    if(targetPID!= 3) {
//                        @RequestParam String name, @RequestParam Integer mid,
//                        @RequestParam Integer pid, @RequestParam String pname,
//                        @RequestParam Integer create_by,@RequestParam Integer subtotal)
                         url =  "http://" + globalVariable.getInstance().getIpaddress() + ":" + globalVariable.getInstance().getPort()
                                 + "/anhe/record/add?name=" + mItem.getText().toString()
                                 + "&mid=5" + "&pid=" + targetPID + "&pname=" + mTarget.getText().toString()
                                 + "&create_by=" + create_by + "&subtotal=" + mSubtotal.getText().toString();
                         addMedicineRecord(url, new VolleyCallBack() {
                             @Override
                             public void onResult(VolleyStatus status) {
                                 if(status == VolleyStatus.SUCCESS) {
                                     Toast.makeText(mContext, "加入項目成功", Toast.LENGTH_SHORT).show();
                                     dismiss();
                                 }
                             }
                         });
                    //if pid is 2
                    } else {
                        url =  "http://" + globalVariable.getInstance().getIpaddress() + ":" + globalVariable.getInstance().getPort()
                                + "/anhe/record/add?name=" + mItem.getText().toString()
                                + "&mid=5" + "&pid=3" + "&pname=" + mTarget.getText().toString()
                                + "&create_by=" + create_by + "&subtotal=" + mSubtotal.getText().toString();
                        addMedicineRecord(url, new VolleyCallBack() {
                            @Override
                            public void onResult(VolleyStatus status) {
                                if(status == VolleyStatus.SUCCESS) {
                                    Toast.makeText(mContext, "加入項目成功", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                }
                            }
                        });

                    }
                }

                //Toast.makeText(mContext, "confirm to add medicine record", Toast.LENGTH_SHORT).show();
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
                                        employeeList.add(employee);
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
                        } );

        volleyController.getInstance(mContext).addToRequestQueue(jsonArrayRequest);

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
                                        patientMap.put(name, new Integer(pid) );
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
                        } );

        volleyController.getInstance(mContext).addToRequestQueue(stringRequest);
    }

}
