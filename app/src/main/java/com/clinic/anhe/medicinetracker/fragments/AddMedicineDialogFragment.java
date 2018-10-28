package com.clinic.anhe.medicinetracker.fragments;



import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.adapters.MedicineManagePagerAdapter;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;
import com.clinic.anhe.medicinetracker.utils.MedicineType;

import android.graphics.drawable.ColorDrawable;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddMedicineDialogFragment extends DialogFragment {

    private EditText mName;
    private EditText mDose;
    private EditText mPrice;
    private EditText mStock;
    private TextView mTitle;

    private TextView mConfirmButton;
    private TextView mCancelButton;

    private static MedicineType medicineType = null;
    private VolleyController volleyController;
    private Context mContext;
    private static MedicineSimpleFragment parentFragment;
    private static List<MedicineCardViewModel> parentList;
    private Integer mid;


    @Override
    public void onDestroyView() {
//        Log.d("OnDestoryView", "Chloe");
        if (getDialog() != null && getRetainInstance()) {
            getDialog().setDismissMessage(null);
        }
        super.onDestroyView();
    }

    public static AddMedicineDialogFragment newInstance(MedicineSimpleFragment parentFrag, MedicineType type, List<MedicineCardViewModel> list){
        AddMedicineDialogFragment fragment = new AddMedicineDialogFragment();
        parentFragment = parentFrag;
        medicineType = type;
        parentList = list;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_medicine, container, false);

        mContext = view.getContext();

        mTitle = view.findViewById(R.id.add_medicine_title);
        mName = view.findViewById(R.id.add_medicine_item);
        mDose = view.findViewById(R.id.add_medicine_dose);
        mPrice = view.findViewById(R.id.add_medicine_price);
        mStock = view.findViewById(R.id.add_medicine_stock);
        mConfirmButton = view.findViewById(R.id.add_medicine_confirmbutton);
        mCancelButton = view.findViewById(R.id.add_medicine_cancelbutton);

        String type = "";
        switch (medicineType) {
            case dialysis:
                type = "自費洗腎";
                break;
            case edible:
                type="口服藥物";
                break;
            case needle:
                type="注射藥物";
                break;
            case bandaid:
                type="外用藥物";
                break;
        }

        mTitle.setText("加入" + type );

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    addMedicineToDatabase(new VolleyCallBack() {
                        @Override
                        public void onResult(VolleyStatus status) {
                            if(status == VolleyStatus.SUCCESS) {
                                Toast.makeText(mContext, "藥物加入成功", Toast.LENGTH_LONG).show();
                                parentList.add(new MedicineCardViewModel(mid,
                                        mName.getText().toString(), mPrice.getText().toString(), mDose.getText().toString(),
                                        Integer.valueOf(mStock.getText().toString()), medicineType.toString()));
                                parentFragment.refreshRecyclerView();
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


    public void addMedicineToDatabase(final VolleyCallBack volleyCallBack){
        //TODO: needs to modified create_by and subtotal
        String url = "http://" + GlobalVariable.getInstance().getIpaddress() +
                ":" + GlobalVariable.getInstance().getPort() + "/anho/medicine/add";
        JSONObject jsonObject = new JSONObject();
        try {
                jsonObject.put("name", mName.getText().toString());
                jsonObject.put("dose", mDose.getText().toString());
                jsonObject.put("price", mPrice.getText().toString());
                jsonObject.put("category", medicineType.toString());
                jsonObject.put("stock", mStock.getText().toString());
        } catch (JSONException e) {

        }
        final String requestBody = jsonObject.toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mid = Integer.valueOf(response);
                        volleyCallBack.onResult(VolleyStatus.SUCCESS);
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

        volleyController.getInstance().addToRequestQueue(stringRequest);
    }
}
