package com.clinic.anhe.medicinetracker.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.DashboardViewModel;
import com.clinic.anhe.medicinetracker.ViewModel.SelectedPatientViewModel;
import com.clinic.anhe.medicinetracker.fragments.MedicineCategoryFragment;
import com.clinic.anhe.medicinetracker.model.EmployeeCardViewModel;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.model.ShiftRecordModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class DashboardPatientAssignViewAdapter extends RecyclerView.Adapter<DashboardPatientAssignViewAdapter.PatientAssignViewHolder> {

    private List<String> patientList;
    private Context mContext;
    private Fragment mFragment;
//    private SelectedPatientViewModel selectedPatientViewModel;
    private DashboardRecyclerViewAdapter parentAdapter;
    private DashboardViewModel dashboardViewModel;
    private ShiftRecordModel deletedShiftRecord;
    private VolleyController volleyController;
    private String ip;
    private String port;
    private String url;
    DashboardRecyclerViewAdapter.ClickPosition clickPosition;

    public DashboardPatientAssignViewAdapter(List<String> list, Fragment mFragment,
                                             DashboardViewModel d, DashboardRecyclerViewAdapter parent, DashboardRecyclerViewAdapter.ClickPosition clickPosition) {
        this.patientList = list;
        this.mFragment = mFragment;
//        this.selectedPatientViewModel = s;
        this.dashboardViewModel = d;
        this.parentAdapter = parent;
        this.clickPosition = clickPosition;

    }

    @NonNull
    @Override
    public DashboardPatientAssignViewAdapter.PatientAssignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_dashboard_patient, parent, false);
        mContext = view.getContext();
        ip = GlobalVariable.getInstance().getIpaddress();
        port = GlobalVariable.getInstance().getPort();

        DashboardPatientAssignViewAdapter.PatientAssignViewHolder patientAssignViewHolder = new DashboardPatientAssignViewAdapter.PatientAssignViewHolder(view);
        return patientAssignViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PatientAssignViewHolder holder, int position) {
        holder.mPatientName.setText(patientList.get(position));
    }


    @Override
    public int getItemCount() {
        return patientList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class PatientAssignViewHolder  extends RecyclerView.ViewHolder{
         public TextView mPatientName;
         private ImageButton mdeleteButton;

        public PatientAssignViewHolder(View itemView) {
            super(itemView);
            mPatientName = itemView.findViewById(R.id.dashboard_patientname);
            mdeleteButton = itemView.findViewById(R.id.dashboard_delete);

            //TODO: allow for deletion
            mdeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickPosition.getPosition(getAdapterPosition(), true);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickPosition.getPosition(getAdapterPosition(), false);
                }
            });
        }
    }

//    private void findPatient(String name, final VolleyCallBack volleyCallBack) {
////        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//        url = "http://" + ip +
//                ":" + port + "/anho/patient/name?name=" + name;
//        JsonArrayRequest jsonArrayRequest =
//                new JsonArrayRequest(Request.Method.GET, url, null,
//                        new Response.Listener<JSONArray>() {
//                            @Override
//                            public void onResponse(JSONArray response) {
//                                for(int i = 0; i < response.length(); i++) {
//                                    JSONObject object = null;
//                                    try {
//                                        object = response.getJSONObject(i);
//                                        Integer pid = object.getInt("pid");
//                                        String name = object.getString("name");
//                                        String shift = object.getString("shift");
//                                        String ic = object.getString("ic");
//                                        String day = object.getString("day");
//                                        PatientsCardViewModel p = new PatientsCardViewModel(pid, name, ic, shift, day);
////                                        selectedPatientViewModel.getPatientLiveData().setValue(p);
//
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                                volleyCallBack.onResult(VolleyStatus.SUCCESS);
//                            }
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.d("VOLLEY", error.toString());
//                                volleyCallBack.onResult(VolleyStatus.FAIL);
//                            }
//                        } );
//
//        volleyController.getInstance().addToRequestQueue(jsonArrayRequest);
//
//    }

//    private void deletePatient(String name, final VolleyCallBack volleyCallBack) {
//        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
//        url = "http://" + ip +
//                ":" + port + "/anho/shiftrecord/delete?patient=" + name + "&createAt=" + date;
//        JsonArrayRequest jsonArrayRequest =
//                new JsonArrayRequest(Request.Method.GET, url, null,
//                        new Response.Listener<JSONArray>() {
//                            @Override
//                            public void onResponse(JSONArray response) {
//                                for(int i = 0; i < response.length(); i++){
//                                    JSONObject object = null;
//                                    try {
//                                        object = response.getJSONObject(i);
//                                        String nurse = object.getString("nurse");
//                                        Integer sid = object.getInt("sid");
//                                        String patient = object.getString("patient");
//                                        String shift = object.getString("shift");
//                                        String day = object.getString("day");
//                                        String createAt = object.getString("createAt");
//                                        deletedShiftRecord = new ShiftRecordModel(sid, createAt, nurse, patient,shift, day);
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
////                                if(deletedShiftRecord != null) {
////                                   List<ShiftRecordModel> temp = dashboardViewModel.getShiftRecordList();
////                                   temp.remove(deletedShiftRecord);
////                                   dashboardViewModel.getShiftRecordListLiveData().setValue(temp);
////                                }
//                                volleyCallBack.onResult(VolleyStatus.SUCCESS);
//
//                        }
//
//                        },
//                        new Response.ErrorListener() {
//                            @Override
//                            public void onErrorResponse(VolleyError error) {
//                                Log.d("VOLLEY", error.toString());
//                                volleyCallBack.onResult(VolleyStatus.FAIL);
//                            }
//                        } );
//
//        volleyController.getInstance().addToRequestQueue(jsonArrayRequest);
//
//    }

}
