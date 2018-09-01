package com.clinic.anhe.medicinetracker.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.SelectedPatientViewModel;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;

import java.util.List;

public class DashboardPatientRecyclerViewAdapter extends RecyclerView.Adapter<DashboardPatientRecyclerViewAdapter.PatientsViewHolder>{

    private List<PatientsCardViewModel> patientList;
    //    private int lastCheckedPosition = -1;
    private SelectedPatientViewModel selectedPatientViewModel;

    //constructor
    public DashboardPatientRecyclerViewAdapter(List<PatientsCardViewModel> patientList, SelectedPatientViewModel selectedPatientViewModel) {
        this.patientList = patientList;
        this.selectedPatientViewModel = selectedPatientViewModel;
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
        holder.patientName.setText(current.getPatientName());
        holder.patientId.setText(current.getPatientIC());
    }


//        if(selectedPatientViewModel.getPatientLiveData().getValue()!= null &&
//                selectedPatientViewModel.getPatientLiveData().getValue().getPatientName().equals(current.getPatientName())) {
//            holder.imageButton.setChecked(true);
//            Log.d("the select patient live data is: ", selectedPatientViewModel.getPatientLiveData().getValue().getPatientName());
//        } else {
//            holder.imageButton.setChecked(false);
//            // Log.d("the select patient live data is: ", selectedPatientViewModel==null?"null":"not null");
//        }


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

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    lastCheckedPosition = getAdapterPosition();
//                    //TODO:
//                    selectedPatientViewModel.getPatientLiveData().setValue(patientList.get(getAdapterPosition()));
//                    notifyDataSetChanged();
                }
            });
        }
    }

}
