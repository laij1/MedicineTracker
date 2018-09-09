package com.clinic.anhe.medicinetracker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.CashFlowViewModel;
import com.clinic.anhe.medicinetracker.model.MedicineRecordCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CashflowTodayRecyclerViewAdapter extends RecyclerView.Adapter<CashflowTodayRecyclerViewAdapter.CashflowTodayViewHolder> {

    private Context mContext;
    private Map<String,Integer> employee;
    private String[] employeeList;
    private VolleyController volleyController;
    private GlobalVariable globalVariable;
    private String ip;
    private String port;
    private CashFlowViewModel cashFlowViewModel;
    private String type;


    public CashflowTodayRecyclerViewAdapter(CashFlowViewModel cashFlowViewModel, String type) {
        this.cashFlowViewModel = cashFlowViewModel;
        this.type = type;
    }

    @NonNull
    @Override
    public CashflowTodayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_cashflow, parent, false);

        mContext = view.getContext();

        employee = new HashMap<>();

        globalVariable = GlobalVariable.getInstance();
        ip = globalVariable.getIpaddress();
        port = globalVariable.getPort();

        String url = "http://" + ip + ":" + port + "/anhe/employee/all";
        parseEmployeeData(url, new VolleyCallBack() {
            @Override
            public void onResult(VolleyStatus status) {
                notifyDataSetChanged();
            }
        });
        CashflowTodayViewHolder cashflowTodayViewHolder = new CashflowTodayViewHolder(view);
        return cashflowTodayViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CashflowTodayViewHolder holder, int position) {
        MedicineRecordCardViewModel current = null;
//                = patientDetailViewModel.getSearchListLiveData().getValue().get(position);
        switch (type) {
            case "today":
                current = cashFlowViewModel.getTodayListLiveData().getValue().get(position);
                break;
            case "uncharged":
                current = cashFlowViewModel.getUnchargedListLiveData().getValue().get(position);
                break;
            case "search":
                current = cashFlowViewModel.getSearchListLiveData().getValue().get(position);
                break;
        }
        holder.itemQuantity.setText(String.valueOf(current.getQuantity()));
        holder.itemSubtotal.setText(String.valueOf(current.getSubtotal()));
        holder.itemCreateDate.setText(current.getCreateAt().toString());
        for(Map.Entry<String, Integer> e : employee.entrySet()) {
            if (e.getValue() == Integer.parseInt(current.getCreateBy())) {
                holder.itemCreateBy.setText(e.getKey());
                break;
            }
        }
        holder.itemName.setText(current.getMedicineName());
        holder.itemPayment.setText(current.getPayment().equalsIgnoreCase("CASH") ? "現" : "月");

        if(current.getChargeBy().equalsIgnoreCase("null")) {
            holder.itemChargeBy.setText("尚未結帳");
            holder.itemChargeAt.setText("0000-00-00");
            holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.unchargeViewColor));
        } else {
            for(Map.Entry<String, Integer> e : employee.entrySet()) {
                if (e.getValue() == Integer.parseInt(current.getChargeBy())) {
                    holder.itemChargeBy.setText(e.getKey());
                    break;
                }
            }
            holder.itemChargeAt.setText(current.getChargeAt());
        }

        //get patient name
        for(Map.Entry<Integer, String> entry : cashFlowViewModel.getPatientMapLiveData().getValue().entrySet()) {
            if (entry.getKey().intValue() == current.getPid().intValue()) {
                holder.patientName.setText(entry.getValue());
                break;
            }
        }

    }

    @Override
    public int getItemCount() {
        switch(type) {
            case "today":
                return cashFlowViewModel.getTodayListLiveData().getValue().size();
            case "uncharged":
                return cashFlowViewModel.getUnchargedListLiveData().getValue().size();
            case "search":
                return cashFlowViewModel.getSearchListLiveData().getValue().size();
        }
        return 0;
    }

    public class CashflowTodayViewHolder extends RecyclerView.ViewHolder {
        private TextView itemCreateDate;
        private TextView itemCreateBy;
        private TextView itemName;
        private TextView patientName;
        private TextView itemPayment;
        private TextView itemQuantity;
        private TextView itemSubtotal;
        private TextView itemChargeBy;
        private TextView itemChargeAt;

        public CashflowTodayViewHolder(View itemView) {
            super(itemView);
            itemCreateDate = itemView.findViewById(R.id.cashflow_createdate);
            itemCreateBy = itemView.findViewById(R.id.cashflow_createby);
            itemName = itemView.findViewById(R.id.cashflow_itemname);
            itemPayment = itemView.findViewById(R.id.cashflow_payment);
            itemQuantity = itemView.findViewById(R.id.cashflow_quantity);
            itemSubtotal = itemView.findViewById(R.id.cashflow_subtotal);
            itemChargeBy = itemView.findViewById(R.id.cashflow_chargeby);
            itemChargeAt = itemView.findViewById(R.id.cashflow_chargedate);
            patientName = itemView.findViewById(R.id.cashflow_patientname);
        }
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
