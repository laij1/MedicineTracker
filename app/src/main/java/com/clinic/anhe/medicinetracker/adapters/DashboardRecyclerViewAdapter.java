package com.clinic.anhe.medicinetracker.adapters;

import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.DashboardViewModel;
import com.clinic.anhe.medicinetracker.ViewModel.SelectedPatientViewModel;
import com.clinic.anhe.medicinetracker.fragments.DashboardFragment;
import com.clinic.anhe.medicinetracker.fragments.SelectPatientFragment;
import com.clinic.anhe.medicinetracker.fragments.SelectPatientsDialogFragment;
import com.clinic.anhe.medicinetracker.fragments.MedicineCategoryFragment;
import com.clinic.anhe.medicinetracker.model.EmployeeCardViewModel;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.model.ShiftRecordModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.clinic.anhe.medicinetracker.utils.Shift;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class DashboardRecyclerViewAdapter extends RecyclerView.Adapter<DashboardRecyclerViewAdapter.EmployeeViewHolder>{

    private List<EmployeeCardViewModel> employeeList;
    private Context mContext;
    private Fragment mFragment;
    private DashboardViewModel dashboardViewModel;
   // private SelectedPatientViewModel selectedPatientViewModel;
    private List<ShiftRecordModel> shiftList;
    private VolleyController volleyController;
    private GlobalVariable globalVariable;
    private String ip;
    private String port;
    private Shift shift;
    ClickPosition clickPosition;
    DialogCallBack dialogCallBack;
    ShiftRecordModel deletedShiftRecord;
    DashboardRecyclerViewAdapter.EmployeeViewHolder employeeViewHolder;



    public interface ClickPosition {
        public void getPosition(int position, boolean deleted);
    }

    public interface DialogCallBack {
        public void callBack(boolean result);
    }

    public DashboardRecyclerViewAdapter(Shift shift, List<EmployeeCardViewModel> employeeList,
                                        Fragment mFragment, DashboardViewModel dashboardViewModel, SelectedPatientViewModel s){
        this.shift = shift;
        this.employeeList = employeeList;
        this.mFragment = mFragment;
        this.dashboardViewModel = dashboardViewModel;
       // this.selectedPatientViewModel = s;
        this.shiftList = new ArrayList<>();

    }



    @NonNull

    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.cardview_nurse_simple, parent, false);
                    .inflate(R.layout.cardview_dashboard_nurse, parent, false);

        mContext = view.getContext();
        globalVariable = GlobalVariable.getInstance();
        ip = globalVariable.getIpaddress();
        port = globalVariable.getPort();

        employeeViewHolder = new DashboardRecyclerViewAdapter.EmployeeViewHolder(view);
        return employeeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        //when position ==1
//        Log.d("holder positon", ""+ position);
        EmployeeCardViewModel current = employeeList.get(position);
        holder.mNurseName.setText(current.getEmployeeName());

        holder.patientAssignList.removeAll(holder.patientAssignList);
        if(dashboardViewModel.getShiftRecordListLiveData().getValue() != null) {
            for (ShiftRecordModel s : dashboardViewModel.getShiftRecordListLiveData().getValue()) {
              //  Log.d("nurse is: " + s.getNurse(), "  patient Assign List" + s.getPatient());
                if (s.getNurse().equalsIgnoreCase(current.getEmployeeName()) && s.getShift().equalsIgnoreCase(shift.toString())) {
                    if (!holder.patientAssignList.contains(s.getPatient())) {
                        holder.patientAssignList.add(s.getPatient());
                    } else {
                    }
                }
            }
        }

            if(holder.patientAssignList.size() > 0) {
                holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.nurseAssignColor));
//                holder.itemView.setOnClickListener(null);
            } else {
                holder.itemView.setBackgroundColor(mContext.getResources().getColor(R.color.dialog_bg_color));
            }
            holder.mAdapter.notifyDataSetChanged();


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder {
        private TextView mNurseName;
        private RecyclerView mRecyclerView;
        private DashboardPatientAssignViewAdapter mAdapter;
        private LinearLayoutManager mLayoutManager;
        private List<String> patientAssignList;

        public EmployeeViewHolder(View itemView) {
            super(itemView);
            mNurseName = itemView.findViewById(R.id.dashboard_nursename);
            mRecyclerView = itemView.findViewById(R.id.dashboard_patient_assign_recyclerview);
            //here we need to load database to live data
            patientAssignList = new ArrayList<>();
            //TODO: here we delete the patient related to the nurse if deleted is true
            //TODO: otherwise, we save the nurs info for cart
            clickPosition = new ClickPosition() {
                @Override
                public void getPosition(int position, boolean deleted) {
//                    Toast.makeText(mContext, "" + patientAssignList.get(position) + "and parent's" + employeeList.get(getAdapterPosition()).getEmployeeName() + deleted, Toast.LENGTH_SHORT).show();
                    String currentPatient = patientAssignList.get(position);
                    if(deleted) {
//                        Toast.makeText(mContext, "you are in the deleted section..", Toast.LENGTH_SHORT).show();
                        SweetAlertDialog deleteAlert = new SweetAlertDialog(mContext, SweetAlertDialog.NORMAL_TYPE);
                        deleteAlert.setTitleText("確定刪除" + currentPatient +"嗎?");
                        deleteAlert.setConfirmText("確定");
                        deleteAlert.setCancelText("取消");
                        deleteAlert.show();


                        deleteAlert.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                deletePatient(currentPatient, mNurseName.getText().toString(), new VolleyCallBack() {
                                    @Override
                                    public void onResult(VolleyStatus status) {
                                        if(status == VolleyStatus.SUCCESS) {
                                            patientAssignList.remove(currentPatient);
                                            notifyDataSetChanged();
                                            //TODO: update dash livedata
                                            if(deletedShiftRecord != null) {
                                                dashboardViewModel.getShiftRecordListLiveData().getValue().remove(deletedShiftRecord);
                                                for(ShiftRecordModel s :dashboardViewModel.getShiftRecordList()) {
                                                    Log.d("after deleting patient ", s.getPatient() + s.getNurse() );
                                                }
                                                mAdapter.notifyDataSetChanged();

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

                    } else {
                        findPatient(currentPatient, new VolleyCallBack() {
                        @Override
                        public void onResult(VolleyStatus status) {
                            FragmentTransaction transaction = mFragment.getActivity().getSupportFragmentManager().beginTransaction();
//                                    mFragment.getParentFragment().getChildFragmentManager().beginTransaction();

                            MedicineCategoryFragment medicineCategoryFragment = MedicineCategoryFragment.newInstance();
                            Bundle args = new Bundle();
                            args.putString(ArgumentVariables.ARG_CART_SELECTED_PATIENT_NAME, currentPatient);
                            args.putInt(ArgumentVariables.ARG_CART_SELECTED_EID,employeeList.get(getAdapterPosition()).getEid() );
                            medicineCategoryFragment.setArguments(args);
                            transaction.replace(R.id.dashboard_setting_layout, medicineCategoryFragment, ArgumentVariables.TAG_MEDICINE_CATEGORY_FRAGMENT)
                                    .addToBackStack(ArgumentVariables.TAG_MEDICINE_CATEGORY_FRAGMENT)
                                    .commit();


                        }
                    });
                    }
                }
            };
//            prepareShiftRecordData();
            mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
            mAdapter = new DashboardPatientAssignViewAdapter(patientAssignList, mFragment,
                    dashboardViewModel, DashboardRecyclerViewAdapter.this, clickPosition);

            mRecyclerView.setAdapter(mAdapter);
            mRecyclerView.setLayoutManager(mLayoutManager);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    prepareShiftRecordData();

                    EmployeeCardViewModel current = employeeList.get(getAdapterPosition());
                    // if the livedata nurse is the same as the ui, show patient list live data
                    if(current.getEmployeeName()
                            .equalsIgnoreCase(dashboardViewModel.getNurseLiveData().getValue())) {
                        //do nothing
                    } else {  // else remove patient list data
                        dashboardViewModel.getNurseLiveData().setValue(current.getEmployeeName());
                        dashboardViewModel.getSelectedPatientsList().removeAll(dashboardViewModel.getSelectedPatientsList());
                        dashboardViewModel.getSelectedPatientsLiveData().setValue(dashboardViewModel.getSelectedPatientsList());
                    }

                    dialogCallBack = new DialogCallBack() {
                        @Override
                        public void callBack(boolean result) {
                            if(result == true) {
//                                Toast.makeText(mContext, "callback is true", Toast.LENGTH_SHORT).show();
                                ((DashboardFragment)mFragment).prepareEmployeeData();

                            }
                        }
                    };

                    SelectPatientsDialogFragment fragment = SelectPatientsDialogFragment.newInstance(shift,
                            current.getEmployeeName(),
                            current.getEid(), dialogCallBack);
                    fragment.show(mFragment.getFragmentManager(), "selectdashboardpatients");



                }
            });
        }

    }
    public void  refreshChildRecyclerView(){
        if(employeeViewHolder != null && employeeViewHolder.mAdapter != null) {
            employeeViewHolder.mAdapter.notifyDataSetChanged();
        }
    }

    private void findPatient(String name, final VolleyCallBack volleyCallBack) {
//        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String url = "http://" + ip +
                ":" + port + "/anho/patient/name?name=" + name;
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
                                        boolean deleted = object.getBoolean("deleted");
                                        PatientsCardViewModel p = new PatientsCardViewModel(pid, name, ic, shift, day, deleted);
                                        //selectedPatientViewModel.getPatientLiveData().setValue(p);

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
                              //  Log.d("VOLLEY", error.toString());
                                volleyCallBack.onResult(VolleyStatus.FAIL);
                            }
                        } ){
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

        volleyController.getInstance().addToRequestQueue(jsonArrayRequest);

    }

    private void prepareShiftRecordData() {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String url = "http://" + ip + ":" + port + "/anho/shiftrecord?createAt=" + date;
        parseShiftRecordData(url, new VolleyCallBack() {
            @Override
            public void onResult(VolleyStatus status) {
                if(status==VolleyStatus.SUCCESS) {
//                    dashboardViewModel.getShiftRecordListLiveData().setValue(shiftList);
                }
            }
        });

    }

    private void parseShiftRecordData(String url, final VolleyCallBack volleyCallBack) {
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for(int i = 0; i < response.length(); i++) {
                                    JSONObject object = null;
                                    try {
                                        object = response.getJSONObject(i);
                                        String nurse = object.getString("nurse");
                                        Integer sid = object.getInt("sid");
                                        String patient = object.getString("patient");
                                        String shift = object.getString("shift");
                                        String day = object.getString("day");
                                        String createAt = object.getString("createAt");
                                        ShiftRecordModel s =new ShiftRecordModel(sid, createAt, nurse, patient,shift, day);
                                        if(!dashboardViewModel.getShiftRecordListLiveData().getValue().contains(s)) {
                                            dashboardViewModel.getShiftRecordListLiveData().getValue().add(s);
                                        }
                                     //   Log.d("getting shift record", nurse + patient);
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
                            //    Log.d("VOLLEY", error.toString());
                                volleyCallBack.onResult(VolleyStatus.FAIL);
                            }
                        } ){
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

        volleyController.getInstance(mContext).addToRequestQueue(jsonArrayRequest);

    }

    private void deletePatient(String pname, String nurse, final VolleyCallBack volleyCallBack) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String deletedShift = "";
        switch (shift) {
            case morning:
                deletedShift = "早班";
                break;
            case afternoon:
                deletedShift = "中班";
                break;
            case night:
                deletedShift ="晚班";
                break;
        }
        String url = "http://" + ip +
                ":" + port + "/anho/shiftrecord/delete?patient=" + pname + "&nurse=" + nurse
                + "&createAt=" + date + "&shift=" + deletedShift;
      //  Log.d("what is the url?", url);
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
                                        Log.d("we have the delete record", patient + sid);
                                        deletedShiftRecord = new ShiftRecordModel(sid, createAt, nurse, patient,shift, day);
//                                        List<ShiftRecordModel> list = dashboardViewModel.getShiftRecordList();
//                                        Iterator<ShiftRecordModel> iter = list.iterator();
//                                                while (iter.hasNext()) {
//                                                    // String str = iter.next();
//                                                    ShiftRecordModel item = iter.next();
//                                                    Log.d("see the comparison result" + item.getSid(), item.equals(deletedShiftRecord) + "");
//                                                    if (item.equals(deletedShiftRecord)) {
//                                                        iter.remove();
//                                                        Log.d("deleting patient from live data", "success");
//                                                    }
//                                                }
//                                        dashboardViewModel.getShiftRecordListLiveData().setValue(list);
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
                              //  Log.d("VOLLEY", error.toString());
                                volleyCallBack.onResult(VolleyStatus.FAIL);
                            }
                        } )
                {/**
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

        volleyController.getInstance().addToRequestQueue(jsonArrayRequest);

    }

}
