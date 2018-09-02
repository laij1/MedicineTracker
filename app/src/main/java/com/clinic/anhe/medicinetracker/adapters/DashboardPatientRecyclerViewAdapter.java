package com.clinic.anhe.medicinetracker.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.DashboardViewModel;
import com.clinic.anhe.medicinetracker.ViewModel.SelectedPatientViewModel;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

public class DashboardPatientRecyclerViewAdapter extends RecyclerView.Adapter<DashboardPatientRecyclerViewAdapter.PatientsViewHolder>{

    private List<PatientsCardViewModel> patientList;
    private DashboardViewModel dashboardViewModel;
    private String nurseName;

    //constructor
    public DashboardPatientRecyclerViewAdapter(List<PatientsCardViewModel> patientList, DashboardViewModel dashboardViewModel, String nurseName) {
        this.patientList = patientList;
        this.dashboardViewModel = dashboardViewModel;
        this.nurseName = nurseName;

    }

    @NonNull
    @Override
    public DashboardPatientRecyclerViewAdapter.PatientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_patients, parent, false);
        DashboardPatientRecyclerViewAdapter.PatientsViewHolder patientsViewHolder = new DashboardPatientRecyclerViewAdapter.PatientsViewHolder(view);
        return patientsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PatientsViewHolder holder, int position) {

        PatientsCardViewModel current =  patientList.get(position);
        holder.patientId.setVisibility(View.GONE);
        holder.patientName.setText(current.getPatientName());
        holder.imageButton.setChecked(false);

        if(nurseName.equalsIgnoreCase(dashboardViewModel.getNurseLiveData().getValue())) {
            for (String name : dashboardViewModel.getSelectedPatientsList()) {
                if (name.equalsIgnoreCase(current.getPatientName())) {
                    holder.imageButton.setChecked(true);
                }
            }
        }

    }




    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public class PatientsViewHolder extends RecyclerView.ViewHolder {
        public TextView patientName;
        public TextView patientId;
        public RadioButton imageButton;

        public PatientsViewHolder(View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.patients_name);
            patientId = itemView.findViewById(R.id.patients_id);
            imageButton = itemView.findViewById(R.id.patients_addbutton);
            patientId.setVisibility(View.GONE);


            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PatientsCardViewModel current = patientList.get(getAdapterPosition());
//
                    if(nurseName.equalsIgnoreCase(dashboardViewModel.getNurseLiveData().getValue())) {
                        //uncheck the item
                        if (dashboardViewModel.getSelectedPatientsList().contains(current.getPatientName())) {
                            dashboardViewModel.getSelectedPatientsList().remove(current.getPatientName());
                            dashboardViewModel.getSelectedPatientsLiveData().setValue(dashboardViewModel.getSelectedPatientsList());
                            imageButton.setChecked(false);
                        } else {
                            dashboardViewModel.getSelectedPatientsList().add(current.getPatientName());
                            dashboardViewModel.getSelectedPatientsLiveData().setValue(dashboardViewModel.getSelectedPatientsList());
                            imageButton.setChecked(true);
                        }
                    }
                }
            });
        }
    }

}
