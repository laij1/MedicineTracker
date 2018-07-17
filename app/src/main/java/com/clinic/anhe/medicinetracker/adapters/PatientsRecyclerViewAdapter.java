package com.clinic.anhe.medicinetracker.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import java.util.List;

public class PatientsRecyclerViewAdapter extends RecyclerView.Adapter<PatientsRecyclerViewAdapter.PatientsViewHolder> {

    private List<PatientsCardViewModel> patientList;
    //constructor
    public PatientsRecyclerViewAdapter(List<PatientsCardViewModel> patientList){
        this.patientList = patientList;
    }

    @NonNull
    @Override
    public PatientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                  .inflate(R.layout.cardview_patients, parent, false);
        PatientsViewHolder patientsViewHolder = new PatientsViewHolder(view);
        return patientsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PatientsViewHolder holder, int position) {
        PatientsCardViewModel current =  patientList.get(position);
        holder.patientName.setText(current.getPatientName());
        holder.patientId.setText(current.getPatientId());
    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public static class PatientsViewHolder extends RecyclerView.ViewHolder {
        public TextView patientName;
        public TextView patientId;

        public PatientsViewHolder(View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.patients_name);
            patientId = itemView.findViewById(R.id.patients_id);
        }
    }


}
