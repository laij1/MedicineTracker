package com.clinic.anhe.medicinetracker.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.fragments.AddMedicineDialogFragment;
import com.clinic.anhe.medicinetracker.fragments.SignatureDialogFragment;
import com.clinic.anhe.medicinetracker.model.MedicineRecordCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PatientDetailCashRecyclerViewAdapter extends RecyclerView.Adapter<PatientDetailCashRecyclerViewAdapter.PatientCashViewHolder> {

    private List<MedicineRecordCardViewModel> recordList;
    private VolleyController volleyController;
    private Context mContext;
    private Fragment mFragement;
    private Map<String,Integer> employee;
    private String[] employeeList;

    public PatientDetailCashRecyclerViewAdapter(List<MedicineRecordCardViewModel> recordList, Context mContext, Fragment fragment) {
        this.recordList = recordList;
        this.mContext = mContext;
        this.mFragement = fragment;
    }

    @Override
    public int getItemCount() {
        return recordList.size();
    }

    @NonNull
    @Override
    public PatientDetailCashRecyclerViewAdapter.PatientCashViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_patient_detail_cash, parent, false);
        employee = new HashMap<>();
        PatientCashViewHolder patientCashViewHolder = new PatientCashViewHolder(view);
        return patientCashViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PatientCashViewHolder holder, int position) {
        MedicineRecordCardViewModel current = recordList.get(position);
        holder.itemQuantity.setText(String.valueOf(current.getQuantity()));
        holder.itemSubtotal.setText(String.valueOf(current.getSubtotal()));
        holder.itemCreateDate.setText(current.getCreateAt());
        holder.itemCreateBy.setText(current.getCreateBy());
        holder.itemName.setText(current.getMedicineName());
        holder.itemPayment.setText(current.getPayment().equalsIgnoreCase("CASH") ? "現" : "月");
    }


    public class PatientCashViewHolder extends RecyclerView.ViewHolder {

        private TextView itemCreateDate;
        private TextView itemCreateBy;
        private TextView itemName;
        private TextView itemPayment;
        private TextView itemQuantity;
        private TextView itemSubtotal;
        public Button itemButton;

        public PatientCashViewHolder(final View itemView) {
            super(itemView);

            itemCreateDate = itemView.findViewById(R.id.patient_detail_cash_createdate);
            itemCreateBy = itemView.findViewById(R.id.patient_detail_cash_createby);
            itemName = itemView.findViewById(R.id.patient_detail_cash_itemname);
            itemPayment = itemView.findViewById(R.id.patient_detail_cash_payment);
            itemQuantity = itemView.findViewById(R.id.patient_detail_cash_quantity);
            itemSubtotal = itemView.findViewById(R.id.patient_detail_cash_subtotal);
            itemButton = itemView.findViewById(R.id.patient_detail_cash_button);

            itemButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MedicineRecordCardViewModel current = recordList.get(getAdapterPosition());
                    //TODO: needs to create a dialog to select charge person
//                    String url = "http://192.168.0.4:8080/anhe/record/update?rid=" + current.getRid() + "&chargeBy=1";
//                    chargeItem(url, new VolleyCallBack() {
//                        @Override
//                        public void onResult(VolleyStatus status) {
//                            if(status== VolleyStatus.SUCCESS) {
//                                itemCreateBy.setText("saved");
//                            }
//                        }
//                    });
                    String url = "http://192.168.0.4:8080/anhe/employee/all";
                    parseEmployeeData(url, new VolleyCallBack() {
                        @Override
                        public void onResult(VolleyStatus status) {
                            if(status == VolleyStatus.SUCCESS) {
                                SignatureDialogFragment signatureDialogFragment =
                                        SignatureDialogFragment.newInstance(employee, current.getRid(), getAdapterPosition());
                                signatureDialogFragment.show( mFragement.getChildFragmentManager(),"signature");
                            }
                        }
                    });

                }
            });
        }

//        private void chargeItem(String url, final VolleyCallBack volleyCallBack) {
//            StringRequest stringRequest =
//                    new StringRequest(Request.Method.GET, url,
//                            new Response.Listener<String>() {
//                                @Override
//                                public void onResponse(String response) {
//                                   if(response.toString().equals("saved")) {
//                                    volleyCallBack.onResult(VolleyStatus.SUCCESS);
//                                   }
//                                }
//                            },
//                            new Response.ErrorListener() {
//                                @Override
//                                public void onErrorResponse(VolleyError error) {
//                                    Log.d("VOLLEY", error.toString());
//                                    volleyCallBack.onResult(VolleyStatus.FAIL);
//                                }
//                            } );
//
//            volleyController.getInstance(mContext).addToRequestQueue(stringRequest);
//        }
    }

    private void parseEmployeeData(String url, final VolleyCallBack volleyCallBack) {
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for(int i = 0; i < response.length(); i++) {
                                    employeeList = new String[response.length()];
                                    JSONObject object = null;
                                    try {
                                        object = response.getJSONObject(i);
                                        String name = object.getString("name");
                                        Integer eid = object.getInt("eid");
                                        employee.put(name,eid);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
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

        volleyController.getInstance(mContext).addToRequestQueue(jsonArrayRequest);

    }
}
