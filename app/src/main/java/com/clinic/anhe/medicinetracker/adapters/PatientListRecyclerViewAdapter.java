package com.clinic.anhe.medicinetracker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.fragments.PatientDetailFragment;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientListRecyclerViewAdapter extends RecyclerView.Adapter<PatientListRecyclerViewAdapter.PatientsListViewHolder> {

    private List<PatientsCardViewModel> patientList;
    private Fragment mFragment;
    private Context mContext;
    private VolleyController volleyController;
    private GlobalVariable globalVariable;
//
    //constructor
    public PatientListRecyclerViewAdapter(List<PatientsCardViewModel> patientList, Fragment fragment){
        this.patientList = patientList;
        this.mFragment = fragment;
    }

    @NonNull
    @Override
    public PatientsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_patients, parent, false);
        mContext = view.getContext();
        PatientsListViewHolder patientsListViewHolder = new PatientsListViewHolder(view);
        return patientsListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PatientsListViewHolder holder, int position) {
        PatientsCardViewModel current =  patientList.get(position);
        holder.patientName.setText(current.getPatientName());
        holder.patientId.setText(current.getPatientIC());
        holder.imageButton.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void filterList(List<PatientsCardViewModel> filterPatientList) {
        if(filterPatientList.size() > 0) {
            patientList = filterPatientList;
            notifyDataSetChanged();
        }
    }

    public class PatientsListViewHolder extends RecyclerView.ViewHolder {
        public TextView patientName;
        public TextView patientId;
        public RadioButton imageButton;
        public ImageButton deleteButton;

        public PatientsListViewHolder(View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.patients_name);
            patientId = itemView.findViewById(R.id.patients_id);
            imageButton = itemView.findViewById(R.id.patients_addbutton);
            deleteButton = itemView.findViewById(R.id.patients_delete);
            imageButton.setVisibility(View.GONE);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int deletePosition = getAdapterPosition();
                    PatientsCardViewModel p = patientList.get(deletePosition);
                    SweetAlertDialog deleteAlert = new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE);
                    deleteAlert.setTitleText("確定刪除" + p.getPatientName() +"嗎?");
                    deleteAlert.setConfirmText("確定");
                    deleteAlert.setCancelText("取消");
                    deleteAlert.show();

                    deleteAlert.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            deletePatient(p.getPID(), new VolleyCallBack() {
                                @Override
                                public void onResult(VolleyStatus status) {
                                    if(status == VolleyStatus.SUCCESS) {
                                        patientList.remove(deletePosition);
                                        notifyItemRemoved(deletePosition);
                                        Toast.makeText(mContext,"刪除" + p.getPatientName() +"成功",Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(mContext, "刪除" + p.getPatientName() +"失敗", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                            deleteAlert.dismiss();
                        }
                    });

                    deleteAlert.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            deleteAlert.dismiss();
                        }
                    });
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //TODO: here we enter the patient detail view
                    PatientDetailFragment patientDetailFragment = PatientDetailFragment.newInstance(patientList.get(getAdapterPosition()));

                    FragmentTransaction transaction = mFragment.getActivity().getSupportFragmentManager().beginTransaction();
                   // FragmentTransaction transaction = mFragment.getFragmentManager().beginTransaction();
                    transaction.replace(R.id.patient_list_layout,patientDetailFragment)
                            .addToBackStack("PatientDetail")
                            .commit();

                }
            });

        }

        public void deletePatient(Integer pid, final VolleyCallBack volleyCallBack) {

            String url = "http://" + globalVariable.getInstance().getIpaddress() +
                    ":" + globalVariable.getInstance().getPort() + "/anho/patient/delete?pid=" + pid;
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


}
