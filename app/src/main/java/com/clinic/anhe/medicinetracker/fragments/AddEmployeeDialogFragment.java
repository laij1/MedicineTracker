package com.clinic.anhe.medicinetracker.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.DashboardViewModel;
import com.clinic.anhe.medicinetracker.adapters.DashboardSettingPagerAdapter;
import com.clinic.anhe.medicinetracker.model.EmployeeCardViewModel;
import com.clinic.anhe.medicinetracker.model.ShiftRecordModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class AddEmployeeDialogFragment extends DialogFragment {

    private EditText mName;
    private EditText mPosition;
    private TextView mConfirmButton;
    private TextView mCancelButton;
    private Context mContext;

    private static DashboardFragment parentFrag;
    private static List<EmployeeCardViewModel> parentList;
    private EmployeeCardViewModel newEmployee;
    private GlobalVariable globalVariable;
    private VolleyController volleyController;

    public static AddEmployeeDialogFragment newInstance(DashboardFragment parent, List<EmployeeCardViewModel> employeeList) {
        AddEmployeeDialogFragment fragment = new AddEmployeeDialogFragment();
        parentFrag = parent;
        parentList = employeeList;
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
        View view = inflater.inflate(R.layout.dialog_add_employee, container, false);

        mContext = view.getContext();

        mName = view.findViewById(R.id.add_employee_name);
        mPosition = view.findViewById(R.id.add_employee_position);
        mConfirmButton = view.findViewById(R.id.add_employee_confirmbutton);
        mCancelButton = view.findViewById(R.id.add_employee_cancelbutton);

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://" + globalVariable.getInstance().getIpaddress() +
                        ":" + globalVariable.getInstance().getPort() + "/anhe/employee/add?name=" + mName.getText().toString()
                        +"&position=" + mPosition.getText().toString();
                addEmployeeToDatabase(url, new VolleyCallBack() {
                    @Override
                    public void onResult(VolleyStatus status) {
                        if(status == VolleyStatus.SUCCESS) {
                            Toast.makeText(mContext, "加入員工成功",Toast.LENGTH_LONG).show();
                            parentList.add(newEmployee);
                            parentFrag.refreshRecycleview();
                            dismiss();
                        }
                    }
                });

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


    private void addEmployeeToDatabase(String url, final VolleyCallBack volleyCallBack) {

        JsonObjectRequest jsonObjectRequest =
                new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Integer eid = response.getInt("eid");
                                    String name = response.getString("name");
                                    String position = response.getString("position");
                                    newEmployee = new EmployeeCardViewModel(name, eid, position);

                                } catch (JSONException e) {
                                        e.printStackTrace();
                                }

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

        volleyController.getInstance(mContext).addToRequestQueue(jsonObjectRequest);

    }

}
