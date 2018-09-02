package com.clinic.anhe.medicinetracker.adapters;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.DashboardViewModel;
import com.clinic.anhe.medicinetracker.ViewModel.SelectedPatientViewModel;
import com.clinic.anhe.medicinetracker.fragments.SelectPatientFragment;
import com.clinic.anhe.medicinetracker.fragments.SelectPatientsDialogFragment;
import com.clinic.anhe.medicinetracker.model.EmployeeCardViewModel;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;

import java.util.ArrayList;
import java.util.List;

public class DashboardRecyclerViewAdapter extends RecyclerView.Adapter<DashboardRecyclerViewAdapter.EmployeeViewHolder>{

    private List<EmployeeCardViewModel> employeeList;
    private Context mContext;
    private Fragment mFragment;
    private DashboardViewModel dashboardViewModel;

    public DashboardRecyclerViewAdapter(List<EmployeeCardViewModel> employeeList, Fragment mFragment, DashboardViewModel dashboardViewModel){
        this.employeeList = employeeList;
        this.mFragment = mFragment;
        this.dashboardViewModel = dashboardViewModel;
    }


    @NonNull

    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.cardview_nurse_simple, parent, false);
                    .inflate(R.layout.cardview_dashboard_nurse, parent, false);

        mContext = view.getContext();

        DashboardRecyclerViewAdapter.EmployeeViewHolder employeeViewHolder = new DashboardRecyclerViewAdapter.EmployeeViewHolder(view);
        return employeeViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        EmployeeCardViewModel current = employeeList.get(position);
        holder.mNurseName.setText(current.getEmployeeName());
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public class EmployeeViewHolder extends RecyclerView.ViewHolder {
        private TextView mNurseName;
//        private RecyclerView mRecyclerView;
//        private DashboardPatientAssignViewAdapter mAdapter;
//        private LinearLayoutManager mLayoutManager;

        public EmployeeViewHolder(View itemView) {
            super(itemView);
//            mNurseName = itemView.findViewById(R.id.nurse_simple_name);
            mNurseName = itemView.findViewById(R.id.dashboard_nursename);
//            mRecyclerView = itemView.findViewById(R.id.dashboard_patient_assign_recyclerview);
            //list should get from shift_record
//            List<String> patientAssignList = new ArrayList<>();
//            mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
//            mAdapter = new DashboardPatientAssignViewAdapter(patientAssignList);
//            mRecyclerView.setAdapter(mAdapter);
//            mRecyclerView.setLayoutManager(mLayoutManager);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: if the livedata nurse is the same as the ui, show patient list live data
                    if(employeeList.get(getAdapterPosition()).getEmployeeName()
                            .equalsIgnoreCase(dashboardViewModel.getNurseLiveData().getValue()) ){
                        Log.d("ui clicked nurse is the same as livedata", "chloe");

                    } else {  //TODO: else remove patient list data
                        Log.d("ui clicked nurse is NOT the same as livedata" + dashboardViewModel.getNurseLiveData().getValue()
                                , "chloe " + employeeList.get(getAdapterPosition()).getEmployeeName());
                        dashboardViewModel.getNurseLiveData().setValue(employeeList.get(getAdapterPosition()).getEmployeeName());
                        dashboardViewModel.getSelectedPatientsList().removeAll(dashboardViewModel.getSelectedPatientsList());
                        dashboardViewModel.getSelectedPatientsLiveData().setValue(dashboardViewModel.getSelectedPatientsList());
                    }
//                    Toast.makeText(mContext, "item is clicked", Toast.LENGTH_LONG).show();
                    SelectPatientsDialogFragment fragment = SelectPatientsDialogFragment.newInstance(
                            employeeList.get(getAdapterPosition()).getEmployeeName());
                    fragment.show(mFragment.getFragmentManager(), "selectdashboardpatients");

//                    patientAssignList.add("賴蓉瑩蝇");
//                    patientAssignList.add("賴蓉瑩");
//                    patientAssignList.add("賴蓉瑩");
//                    patientAssignList.add("賴蓉瑩");
//                    mAdapter.notifyDataSetChanged();

                }
            });
        }
    }
}
