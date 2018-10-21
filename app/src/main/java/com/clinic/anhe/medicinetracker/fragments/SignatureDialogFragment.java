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

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import android.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.CheckoutViewModel;
import com.clinic.anhe.medicinetracker.ViewModel.SelectedEmployeeViewModel;
import com.clinic.anhe.medicinetracker.adapters.SignatureRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
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
    private Integer eid;
    private static  int index;
    private GlobalVariable globalVariable;
    private String ip;
    private String port;


    //TODO:
    private TextView mConfirmButton;
    private TextView mCancelButton;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private SignatureRecyclerViewAdapter mAdapter;
    private CheckoutViewModel checkoutViewModel;


    public static SignatureDialogFragment newInstance(Map<String, Integer> employeeMap) {
//        Bundle args = new Bundle();
        SignatureDialogFragment fragment = new SignatureDialogFragment();
        employee = employeeMap;
//        rid = recordID;
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray(ArgumentVariables.ARG_EMPLOYEE_LIST, employeeList);
    }


    @Override
    public void onDestroyView() {
//        Log.d("OnDestoryView", "ChloeSignature");
//        Log.d("Dialog is ", getDialog()==null?"null": "notnull");
//        Log.d("getRetainInstance() ", getRetainInstance()==true?"true": "false");
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signature, container, false);

        if(savedInstanceState != null) {
            employeeList = savedInstanceState.getStringArray(ArgumentVariables.ARG_EMPLOYEE_LIST);
        }

        mContext = this.getContext();
        globalVariable = GlobalVariable.getInstance();
        ip = globalVariable.getIpaddress();
        port = globalVariable.getPort();

        int i = 0;
        employeeList = new String[employee.size()];
        for(Map.Entry<String, Integer> entry : employee.entrySet()) {
            employeeList[i] = entry.getKey();
            i++;
        }

        SelectedEmployeeViewModel selectedEmployeeViewModel = ViewModelProviders.of(this).get(SelectedEmployeeViewModel.class);
        checkoutViewModel = ViewModelProviders.of(getParentFragment().getParentFragment()).get(CheckoutViewModel.class);

        mConfirmButton = view.findViewById(R.id.signature_confirmbutton);
        mCancelButton = view.findViewById(R.id.signature_cancelbutton);
        mRecyclerView = view.findViewById(R.id.signature_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 3);
        mAdapter = new SignatureRecyclerViewAdapter(employee, employeeList, selectedEmployeeViewModel);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedEmployeeViewModel.getSelectedEmployee().getValue().getEid()== -1) {
                    Toast.makeText(getActivity(), "請簽名再結帳",Toast.LENGTH_LONG).show();
                }
                else {

                     eid = selectedEmployeeViewModel.getSelectedEmployee().getValue().getEid();
//                            String url = "http://" + ip + ":" + port + "/anho/record/update?rid=" + rid + "&chargeBy=" + eid;
                            chargeItemList( new VolleyCallBack() {
                                @Override
                                public void onResult(VolleyStatus status) {
                                    if (status == VolleyStatus.SUCCESS) {
                                        if(getParentFragment() instanceof  PatientDetailCashFragment) {
                                            PatientDetailCashFragment fragment = (PatientDetailCashFragment)getParentFragment();
                                            fragment.refreshRecyclerViewFromCheckout();
                                        } else if ( getParentFragment() instanceof PatientDetailMonthFragment) {
                                            PatientDetailMonthFragment fragment = (PatientDetailMonthFragment)getParentFragment();
                                            fragment.refreshRecyclerViewFromCheckout();
                                        }

                                        Toast.makeText(getParentFragment().getContext(), "結帳完成",Toast.LENGTH_SHORT).show();
                                        dismiss();
                                    } else {
                                        Toast.makeText(getParentFragment().getContext(), "結帳未完成", Toast.LENGTH_SHORT).show();
                                        dismiss();
                                    }
                                }
                            });

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

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {

//        if(savedInstanceState!= null) {
//            employeeList = savedInstanceState.getStringArray(ArgumentVariables.ARG_EMPLOYEE_LIST);
//        }
//
//        int i = 0;
//        employeeList = new String[employee.size()];
//        for(Map.Entry<String, Integer> entry : employee.entrySet()) {
//            employeeList[i] = entry.getKey();
//            i++;
//        }
//
//        mContext = this.getContext();
//        globalVariable = GlobalVariable.getInstance();
//        ip = globalVariable.getIpaddress();
//        port = globalVariable.getPort();
//
//        Log.d("on create dialog", "employeelist" + employeeList[0] );
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//        final int[] checkedItem = {-1};
//        dialog = builder.setTitle("請簽名")
//                .setItems(employeeList, null)
//                .setPositiveButton("簽名", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //TODO: can't get the item clicked position
//                        if(checkedItem[0] == -1) {
//                            Toast.makeText(getActivity(), "請簽名再結帳",Toast.LENGTH_LONG).show();
//
//                        } else {
//                            Integer eid = employee.get(employeeList[checkedItem[0]]);
//                            String url = "http://" + ip + ":" + port + "/anho/record/update?rid=" + rid + "&chargeBy=" + eid;
//                            chargeItem(url, new VolleyCallBack() {
//                                @Override
//                                public void onResult(VolleyStatus status) {
//                                    if (status == VolleyStatus.SUCCESS) {
//                                        if(getParentFragment() instanceof  PatientDetailCashFragment) {
//                                            PatientDetailCashFragment fragment = (PatientDetailCashFragment)getParentFragment();
//                                            fragment.refreshRecyclerView(index);
//                                        } else if ( getParentFragment() instanceof PatientDetailMonthFragment) {
//                                            PatientDetailMonthFragment fragment = (PatientDetailMonthFragment)getParentFragment();
//                                            fragment.refreshRecyclerView(index);
//                                        }
//
//                                        Toast.makeText(getParentFragment().getContext(), "結帳完成",Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        Toast.makeText(getParentFragment().getContext(), "結帳未完成", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                        }
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dismiss();
//                    }
//                })
//                .create();
//
//        // add this listener after dialog creation to stop auto dismiss on selection
//        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    if(checkedItem[0] != -1) {
//                        parent.getChildAt(checkedItem[0]).setBackgroundColor(getResources().getColor(R.color.dialog_bg_color));
//                    }
//                    checkedItem[0] = position;
//                    view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
//            }
//        };
//        dialog.getListView().setOnItemClickListener(listener);
//
//        if (dialog.getWindow() != null) {
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//            dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_round);
//
//        }
//
//        return dialog;
//    }

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

    private void chargeItemList(final VolleyCallBack volleyCallBack) {
        // Log.d("what is the eid", cartViewModel.getCartSelectedEid().getValue() +"");
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        String defaultDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
        //TODO: needs to modified create_by and subtotal
        String url = "http://" + ip +
                ":" + port + "/anho/record/updatelist";
        JSONArray jsonArray = new JSONArray();
        try {
            List<MedicineRecordCardViewModel> curentList = new ArrayList<>();
            if(getParentFragment() instanceof PatientDetailCashFragment) {
                curentList = checkoutViewModel.getCashCheckoutLiveData().getValue();
            } else if (getParentFragment() instanceof  PatientDetailMonthFragment) {
                curentList = checkoutViewModel.getMonthCheckoutLiveData().getValue();
            }
            for(MedicineRecordCardViewModel item : curentList) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("rid", item.getRid());
                jsonObject.put("mid", item.getMid());
                jsonObject.put("medicineName", item.getMedicineName());
                jsonObject.put("payment", item.getPayment());
                jsonObject.put("pid", item.getPid());
                jsonObject.put("quantity", item.getQuantity());
                jsonObject.put("createBy", item.getCreateBy());
                jsonObject.put("createAt", item.getCreateAt());
                jsonObject.put("chargeAt", defaultDate);
                jsonObject.put("chargeBy", eid);
                jsonObject.put("patientName", item.getPatientName());
                jsonObject.put("subtotal", item.getSubtotal());
                jsonArray.put(jsonObject);
                Log.d("updateing all checkoutlist ", item.getRid() + item.getMedicineName());
            }
        } catch (JSONException e) {

        }
        final String requestBody = jsonArray.toString();
        // Log.d(requestBody, "");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("saved")) {
                            volleyCallBack.onResult(VolleyStatus.SUCCESS);
                        } else {
                            volleyCallBack.onResult(VolleyStatus.FAIL);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY", error.toString());
                        volleyCallBack.onResult(VolleyStatus.FAIL);
                    }
                })
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

        };

        volleyController.getInstance().addToRequestQueue(stringRequest);
    }

}
