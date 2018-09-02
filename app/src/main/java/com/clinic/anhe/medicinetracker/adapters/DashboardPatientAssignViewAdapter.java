package com.clinic.anhe.medicinetracker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.model.EmployeeCardViewModel;

import java.util.List;

public class DashboardPatientAssignViewAdapter extends RecyclerView.Adapter<DashboardPatientAssignViewAdapter.PatientAssignViewHolder> {

    private List<String> patientList;
    private Context mContext;

    public DashboardPatientAssignViewAdapter(List<String> list) {
        this.patientList = list;

    }

    @NonNull
    @Override
    public DashboardPatientAssignViewAdapter.PatientAssignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_dashboard_patient, parent, false);
        mContext = view.getContext();
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

    public class PatientAssignViewHolder  extends RecyclerView.ViewHolder{
        TextView mPatientName;
        public PatientAssignViewHolder(View itemView) {
            super(itemView);
            mPatientName = itemView.findViewById(R.id.dashbaord_patientname);
        }
    }
}
