package com.clinic.anhe.medicinetracker.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.model.MedicineRecordCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;
import com.clinic.anhe.medicinetracker.utils.PaymentType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SignatureDialogFragment extends DialogFragment {

    private String[] employeeList;
    private static Map<String, Integer> employee;
    private VolleyController volleyController;
    private Context mContext;
    private static Integer rid;
    private static  int index;
    private AlertDialog dialog;
    private GlobalVariable globalVariable;
    private String ip;
    private String port;

    public static SignatureDialogFragment newInstance(Map<String, Integer> employeeMap, Integer recordID, int i) {
//        Bundle args = new Bundle();
        SignatureDialogFragment fragment = new SignatureDialogFragment();
        index = i;
        employee = employeeMap;
        rid = recordID;
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray(ArgumentVariables.ARG_EMPLOYEE_LIST, employeeList);
    }


    @Override
    public void onDestroyView() {
        Log.d("OnDestoryView", "ChloeSignature");
        Log.d("Dialog is ", getDialog()==null?"null": "notnull");
        Log.d("getRetainInstance() ", getRetainInstance()==true?"true": "false");
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        if(savedInstanceState!= null) {
            employeeList = savedInstanceState.getStringArray(ArgumentVariables.ARG_EMPLOYEE_LIST);
        }

        int i = 0;
        employeeList = new String[employee.size()];
        for(Map.Entry<String, Integer> entry : employee.entrySet()) {
            employeeList[i] = entry.getKey();
            i++;
        }

        mContext = this.getContext();
        globalVariable = GlobalVariable.getInstance();
        ip = globalVariable.getIpaddress();
        port = globalVariable.getPort();

        Log.d("on create dialog", "employeelist" + employeeList[0] );

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final int[] checkedItem = {-1};
        dialog = builder.setTitle("請簽名")
                .setItems(employeeList, null)
                .setPositiveButton("簽名", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //TODO: can't get the item clicked position
                        if(checkedItem[0] == -1) {
                            Toast.makeText(getActivity(), "請簽名再結帳",Toast.LENGTH_LONG).show();

                        } else {
                            Integer eid = employee.get(employeeList[checkedItem[0]]);
                            String url = "http://" + ip + ":" + port + "/anhe/record/update?rid=" + rid + "&chargeBy=" + eid;
                            chargeItem(url, new VolleyCallBack() {
                                @Override
                                public void onResult(VolleyStatus status) {
                                    if (status == VolleyStatus.SUCCESS) {
                                        if(getParentFragment() instanceof  PatientDetailCashFragment) {
                                            PatientDetailCashFragment fragment = (PatientDetailCashFragment)getParentFragment();
                                            fragment.refreshRecyclerView(index);
                                        } else if ( getParentFragment() instanceof PatientDetailMonthFragment) {
                                            PatientDetailMonthFragment fragment = (PatientDetailMonthFragment)getParentFragment();
                                            fragment.refreshRecyclerView(index);
                                        }

                                        Toast.makeText(getParentFragment().getContext(), "結帳完成",Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getParentFragment().getContext(), "結帳未完成", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .create();

        // add this listener after dialog creation to stop auto dismiss on selection
        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(checkedItem[0] != -1) {
                        parent.getChildAt(checkedItem[0]).setBackgroundColor(getResources().getColor(R.color.dialog_bg_color));
                    }
                    checkedItem[0] = position;
                    view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        };
        dialog.getListView().setOnItemClickListener(listener);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_round);

        }

        return dialog;
    }

    private void chargeItem(String url, final VolleyCallBack volleyCallBack) {
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
