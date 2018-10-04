package com.clinic.anhe.medicinetracker.fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android .support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class AddInventoryDialogFragment extends DialogFragment {

    private EditText mAmount;
    private TextView mMedicineTitle;
    private TextView mConfirmButton;
    private TextView mCancelButton;

    private Context mContext;
    private GlobalVariable globalVariable;
    private VolleyController volleyController;
    private static Fragment parentFragment;

    private String medicineName;
    private Integer mid;
    private Integer stock;


    public static AddInventoryDialogFragment newInstance(String medicineName , Integer mid, Integer stock, Fragment parent){
        AddInventoryDialogFragment fragment = new AddInventoryDialogFragment();
        parentFragment = parent;
        Bundle args = new Bundle();
        args.putString(ArgumentVariables.ARG_INVENTORY_MEDICINE_NAME, medicineName);
        args.putInt(ArgumentVariables.ARG_INVENTORY_MID, mid);
        args.putInt(ArgumentVariables.ARG_INVENTORY_STOCK, stock);
        fragment.setArguments(args);
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
        if(savedInstanceState != null) {
            medicineName = savedInstanceState.getString(ArgumentVariables.ARG_INVENTORY_MEDICINE_NAME);
            mid = savedInstanceState.getInt(ArgumentVariables.ARG_INVENTORY_MID);
            stock = savedInstanceState.getInt(ArgumentVariables.ARG_INVENTORY_STOCK);
        }

        if(medicineName == null) {
            medicineName = getArguments().getString(ArgumentVariables.ARG_INVENTORY_MEDICINE_NAME);
            mid = getArguments().getInt(ArgumentVariables.ARG_INVENTORY_MID);
            stock = getArguments().getInt(ArgumentVariables.ARG_INVENTORY_STOCK);
        }

        View view = inflater.inflate(R.layout.fragment_add_inventory, container, false);

        mContext = view.getContext();

        mAmount = view.findViewById(R.id.add_inventory_amount);
        mMedicineTitle = view.findViewById(R.id.add_inventory_medicine);
        mMedicineTitle.setText(medicineName);
        mConfirmButton = view.findViewById(R.id.add_inventory_confirmbutton);
        mCancelButton = view.findViewById(R.id.add_inventory_cancelbutton);

        mConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: 1.call add inventory to database table inventory
                //TODO: 2. update medicine.stock value
                //TODO: 3. Toast to show the result
                addStock(new VolleyCallBack() {
                    @Override
                    public void onResult(VolleyStatus status) {
                        if(status == VolleyStatus.SUCCESS) {
                            Toast.makeText(mContext, "進貨輸入成功", Toast.LENGTH_SHORT).show();
                            updateStock();
                            ((MedicineSimpleFragment)parentFragment).refreshRecyclerView();
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


    private void addStock(final VolleyCallBack volleyCallBack) {
        String url = "http://" + globalVariable.getInstance().getIpaddress() +
                ":" + globalVariable.getInstance().getPort() + "/anho/inventory/add";
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mid", mid);
            jsonObject.put("medicine", medicineName);
            jsonObject.put("amount", Integer.valueOf(mAmount.getText().toString()));

        } catch (JSONException e) {

        }
        final String requestBody = jsonObject.toString();

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

    public void updateStock() {
        String url = "http://" + globalVariable.getInstance().getIpaddress() +
                ":" + globalVariable.getInstance().getPort() + "/anho/medicine/update?mid=" + mid +"&stock=" + Integer.valueOf(mAmount.getText().toString());
        StringRequest stringRequest =
                new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if(response.toString().equals("saved")) {
                                    Toast.makeText(mContext, "進貨更新成功", Toast.LENGTH_SHORT).show();
//                                    volleyCallBack.onResult(VolleyStatus.SUCCESS);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("VOLLEY", error.toString());
                                Toast.makeText(mContext, "進貨更新失敗", Toast.LENGTH_SHORT).show();
//                                volleyCallBack.onResult(VolleyStatus.FAIL);
                            }
                        } );

        volleyController.getInstance(mContext).addToRequestQueue(stringRequest);
    }
}
