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
    private SelectedPatientViewModel selectedPatientViewModel;
    private DashboardRecyclerViewAdapter parentAdapter;
    private DashboardViewModel dashboardViewModel;
    private ShiftRecordModel deletedShiftRecord;
    private VolleyController volleyController;
    private String ip;
    private String port;
    private String url;
    DashboardRecyclerViewAdapter.ClickPosition clickPosition;

    public DashboardPatientAssignViewAdapter(List<String> list, Fragment mFragment, SelectedPatientViewModel s,
                                             DashboardViewModel d, DashboardRecyclerViewAdapter parent, DashboardRecyclerViewAdapter.ClickPosition clickPosition) {
        this.patientList = list;
        this.mFragment = mFragment;
        this.selectedPatientViewModel = s;
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
                    clickPosition.getPosition(getAdapterPosition());
                    final SweetAlertDialog deleteAlert = new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE);
                    deleteAlert.setTitleText("確定刪除" + mPatientName.getText().toString() +"嗎?");
                    deleteAlert.setConfirmText("確定");
                    deleteAlert.setCancelText("取消");
                    deleteAlert.show();

                    deleteAlert.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            deletePatient(mPatientName.getText().toString(), new VolleyCallBack() {
                            @Override
                                public void onResult(VolleyStatus status) {
                                    if(status == VolleyStatus.SUCCESS) {
                                        patientList.remove(mPatientName.getText().toString());
                                        notifyDataSetChanged();
                                        //TODO: update dash livedata
                                        if(deletedShiftRecord != null) {
                                            List<ShiftRecordModel> list = dashboardViewModel.getShiftRecordList();
                                            List<ShiftRecordModel> currentShiftList = dashboardViewModel.getShiftRecordList();
//                                            list.remove(deletedShiftRecord);
                                            Iterator<ShiftRecordModel> iter = currentShiftList.iterator();
                                            while (iter.hasNext()) {
                                               // String str = iter.next();
                                                ShiftRecordModel item = iter.next();
                                                if (item.getPatient().equalsIgnoreCase(deletedShiftRecord.getPatient())) {
                                                    iter.remove();
                                                }
                                            }
                                            dashboardViewModel.getShiftRecordListLiveData().setValue(currentShiftList);
                                            for(ShiftRecordModel s :dashboardViewModel.getShiftRecordList()) {
                                                Log.d("after deleting patient ", s.getPatient() );
                                            }
                                            parentAdapter.notifyDataSetChanged();

                                        }
//                                        dashboardViewModel.getShiftRecordListLiveData().getValue().remove(deletedShiftRecord);
                                        Toast.makeText(mContext, "刪除成功", Toast.LENGTH_SHORT).show();

                                    } else {
                                        Toast.makeText(mContext, "刪除失敗", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            //dashboardViewModel.getShiftRecordListLiveData().setValue(dashboardViewModel.getShiftRecordListLiveData().getValue());
                            deleteAlert.dismiss();
                        }
                    });
                    deleteAlert.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                deleteAlert.dismiss();
                        }
                    });

//
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(mContext, patientList.get(getAdapterPosition()), Toast.LENGTH_LONG).show();
                    //TODO: here goes to medicine catergory
                    findPatient(patientList.get(getAdapterPosition()), new VolleyCallBack() {
                        @Override
                        public void onResult(VolleyStatus status) {
                            FragmentTransaction transaction = mFragment.getActivity().getSupportFragmentManager().beginTransaction();
//                                    mFragment.getParentFragment().getChildFragmentManager().beginTransaction();

                            MedicineCategoryFragment medicineCategoryFragment = MedicineCategoryFragment.newInstance();
                            Bundle args = new Bundle();
                            args.putString(ArgumentVariables.ARG_CART_SELECTED_PATIENT_NAME, patientList.get(getAdapterPosition()));
                            medicineCategoryFragment.setArguments(args);
                            transaction.replace(R.id.dashboard_setting_layout, medicineCategoryFragment, ArgumentVariables.TAG_MEDICINE_CATEGORY_FRAGMENT)
                                    .addToBackStack(ArgumentVariables.TAG_MEDICINE_CATEGORY_FRAGMENT)
                                    .commit();


                        }
                    });

                }
            });
        }
    }

    private void findPatient(String name, final VolleyCallBack volleyCallBack) {
//        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        url = "http://" + ip +
                ":" + port + "/anhe/patient/name?name=" + name;
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for(int i = 0; i < response.length(); i++) {
                                    JSONObject object = null;
                                    try {
                                        object = response.getJSONObject(i);
                                        Integer pid = object.getInt("pid");
                                        String name = object.getString("name");
                                        String shift = object.getString("shift");
                                        String ic = object.getString("ic");
                                        String day = object.getString("day");
                                        PatientsCardViewModel p = new PatientsCardViewModel(pid, name, ic, shift, day);
                                        selectedPatientViewModel.getPatientLiveData().setValue(p);

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

        volleyController.getInstance().addToRequestQueue(jsonArrayRequest);

    }

    private void deletePatient(String name, final VolleyCallBack volleyCallBack) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        url = "http://" + ip +
                ":" + port + "/anhe/shiftrecord/delete?patient=" + name + "&createAt=" + date;
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for(int i = 0; i < response.length(); i++){
                                    JSONObject object = null;
                                    try {
                                        object = response.getJSONObject(i);
                                        String nurse = object.getString("nurse");
                                        Integer sid = object.getInt("sid");
                                        String patient = object.getString("patient");
                                        String shift = object.getString("shift");
                                        String day = object.getString("day");
                                        String createAt = object.getString("createAt");
                                        deletedShiftRecord = new ShiftRecordModel(sid, createAt, nurse, patient,shift, day);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
//                                if(deletedShiftRecord != null) {
//                                   List<ShiftRecordModel> temp = dashboardViewModel.getShiftRecordList();
//                                   temp.remove(deletedShiftRecord);
//                                   dashboardViewModel.getShiftRecordListLiveData().setValue(temp);
//                                }
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

        volleyController.getInstance().addToRequestQueue(jsonArrayRequest);

    }

}
