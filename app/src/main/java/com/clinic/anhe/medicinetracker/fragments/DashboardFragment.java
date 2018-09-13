package com.clinic.anhe.medicinetracker.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.DashboardViewModel;
import com.clinic.anhe.medicinetracker.ViewModel.SelectedPatientViewModel;
import com.clinic.anhe.medicinetracker.adapters.DashboardRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.adapters.PatientListRecyclerViewAdapter;
import com.clinic.anhe.medicinetracker.model.EmployeeCardViewModel;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.model.ShiftRecordModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.ArgumentVariables;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;
import com.clinic.anhe.medicinetracker.utils.Shift;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DashboardFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private DashboardRecyclerViewAdapter mAdapter;
    private List<EmployeeCardViewModel> employeeList;
    private List<ShiftRecordModel> shiftList;
    private VolleyController volleyController;
    private GlobalVariable globalVariable;
    private String ip;
    private String port;
    private Context mContext;
    private DashboardViewModel dashboardViewModel;
    private SelectedPatientViewModel selectedPatientViewModel;
    private Shift shift;

    public static DashboardFragment newInstance(Shift s){
        DashboardFragment fragment = new DashboardFragment();
        Bundle args = new Bundle();
        args.putString(ArgumentVariables.ARG_PATIENT_SHIFT, s.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //TODO: medicineType could be null....
        outState.putString(ArgumentVariables.ARG_PATIENT_SHIFT, shift.toString());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        if(savedInstanceState != null) {
            shift = Shift.fromString(savedInstanceState.getString(ArgumentVariables.ARG_PATIENT_SHIFT));
        }

        if(shift == null) {
            shift = Shift.fromString(getArguments().getString(ArgumentVariables.ARG_PATIENT_SHIFT));
        }

        dashboardViewModel = ViewModelProviders.of(getParentFragment()).get(DashboardViewModel.class);
        selectedPatientViewModel = ViewModelProviders.of(getParentFragment()).get(SelectedPatientViewModel.class);

        selectedPatientViewModel.getPatientLiveData().observe(getParentFragment(), new Observer<PatientsCardViewModel>() {
            @Override
            public void onChanged(@Nullable PatientsCardViewModel patientsCardViewModel) {
                Log.d("Wee","CHLOE");
            }
        });

        mContext = getContext();

        globalVariable = GlobalVariable.getInstance();
        ip = globalVariable.getIpaddress();
        port = globalVariable.getPort();

        mRecyclerView = view.findViewById(R.id.dashboard_recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new GridLayoutManager(getContext(), 1);
        employeeList = new ArrayList<>();
        shiftList = new ArrayList<>();
        prepareShiftRecordData();
        prepareEmployeeData();
        mAdapter = new DashboardRecyclerViewAdapter(shift, employeeList, this, dashboardViewModel,selectedPatientViewModel);

        dashboardViewModel.getShiftRecordListLiveData().observe(getParentFragment(), new Observer<List<ShiftRecordModel>>() {
            @Override
            public void onChanged(@Nullable List<ShiftRecordModel> shiftList) {
                //TODO: here we rearrange emplyeelist
                //get all the nurses on the today's shift
//                List<String> nurseList = new ArrayList<>();
//                for(ShiftRecordModel s : shiftList) {
//                       if(!nurseList.contains(s.getNurse())){
//                           nurseList.add(s.getNurse());
//                       }
//                }
//                //compare whith employeeList
//                for(int i = 0; i < employeeList.size(); i++) {
//                    EmployeeCardViewModel e = employeeList.get(i);
//                    if(!nurseList.contains(e.getEmployeeName())){
//                        employeeList.add(employeeList.remove(i));
//                    }
//                }
                mAdapter.notifyDataSetChanged();
            }
        });
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        setRetainInstance(true);
        return view;
    }

    private void prepareEmployeeData() {
        String url = "http://" + ip + ":" + port + "/anhe/employee/all";
        parseEmployeeData(url, new VolleyCallBack() {
            @Override
            public void onResult(VolleyStatus status) {
                if(status==VolleyStatus.SUCCESS) {
                    mAdapter.notifyDataSetChanged();
//                    Log.d("refreshing employee", employeeList.get(0).getEmployeeName());
                }
            }
        });

    }

    private void parseEmployeeData(String url, final VolleyCallBack volleyCallBack) {
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for(int i = 0; i < response.length(); i++) {
                                    JSONObject object = null;
                                    try {
                                        object = response.getJSONObject(i);
                                        String name = object.getString("name");
                                        Integer eid = object.getInt("eid");
                                        String position = object.getString("position");
                                        //TODO: here we rearrange employlist by whether it is assigned with patients
                                        EmployeeCardViewModel e = new EmployeeCardViewModel(name, eid, position);
                                        for(ShiftRecordModel s :dashboardViewModel.getShiftRecordListLiveData().getValue()) {
                                            if(s.getNurse().equalsIgnoreCase(name) && s.getShift().equalsIgnoreCase(shift.toString())) {
                                                employeeList.add(0,e);
                                                break;
                                            }
                                        }
                                        if(!employeeList.contains(e)) {
                                            employeeList.add(e);
                                        }
                                        Log.d("employeename:" , name);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                Log.d("the shift record live data is loaded: ", ""+ dashboardViewModel.getShiftRecordListLiveData().getValue().size());
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

    public void refreshRecycleview(){
        mAdapter.notifyDataSetChanged();
    }


    private void prepareShiftRecordData( ) {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String url = "http://" + ip + ":" + port + "/anhe/shiftrecord?createAt=" + date;
        parseShiftRecordData(url, new VolleyCallBack() {
            @Override
            public void onResult(VolleyStatus status) {
                if(status==VolleyStatus.SUCCESS) {
                    dashboardViewModel.getShiftRecordListLiveData().setValue(shiftList);
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
                                        shiftList.add(new ShiftRecordModel(sid, createAt, nurse, patient,shift, day));
                                        Log.d("getting shift record", nurse + patient);
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

