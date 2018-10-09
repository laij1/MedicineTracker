package com.clinic.anhe.medicinetracker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.graphics.drawable.GradientDrawable;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.DashboardViewModel;
import com.clinic.anhe.medicinetracker.ViewModel.SelectedPatientViewModel;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.model.ShiftRecordModel;
import com.clinic.anhe.medicinetracker.utils.Shift;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

public class DashboardPatientRecyclerViewAdapter extends RecyclerView.Adapter<DashboardPatientRecyclerViewAdapter.PatientsViewHolder>{

    private List<PatientsCardViewModel> patientList;
    private DashboardViewModel dashboardViewModel;
    private String nurseName;
    private Context mContext;
    private Shift shift;

    //constructor
    public DashboardPatientRecyclerViewAdapter(List<PatientsCardViewModel> patientList, DashboardViewModel dashboardViewModel,
                                               String nurseName, Shift shift) {
        this.patientList = patientList;
        this.dashboardViewModel = dashboardViewModel;
        this.nurseName = nurseName;
        this.shift = shift;

    }

    @NonNull
    @Override
    public DashboardPatientRecyclerViewAdapter.PatientsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_patients, parent, false);
        mContext = view.getContext();
        DashboardPatientRecyclerViewAdapter.PatientsViewHolder patientsViewHolder = new DashboardPatientRecyclerViewAdapter.PatientsViewHolder(view);
        return patientsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PatientsViewHolder holder, int position) {

        PatientsCardViewModel current =  patientList.get(position);
        holder.patientId.setVisibility(View.GONE);
        holder.patientName.setText(current.getPatientName());

        holder.imageButton.setEnabled(true);
        for(ShiftRecordModel s : dashboardViewModel.getShiftRecordList()) {
            if(s.getPatient().equalsIgnoreCase(current.getPatientName()) && nurseName.equals(s.getNurse())
                    && s.getShift().equalsIgnoreCase(shift.toString())) {
                Log.d("disabling button for", s.getPatient() + s.getNurse());
                holder.imageButton.setEnabled(false);
                break;
            }
        }

        holder.imageButton.setChecked(false);
        if(nurseName.equalsIgnoreCase(dashboardViewModel.getNurseLiveData().getValue())) {
            for (String name : dashboardViewModel.getSelectedPatientsList()) {
                if (name.equalsIgnoreCase(current.getPatientName())) {
                    holder.imageButton.setChecked(true);
                }
            }
        }

//        change background color according to shift
        if(current.getPatientShift().equals(Shift.afternoon.toString())) {
            ((CardView)holder.itemView).setRadius(12);
            ((CardView)holder.itemView).setCardBackgroundColor(mContext.getResources().getColor(R.color.actualcashBackground));
        } else if (current.getPatientShift().equals(Shift.night.toString())) {
            ((CardView)holder.itemView).setRadius(12);
            ((CardView)holder.itemView).setCardBackgroundColor(mContext.getResources().getColor(R.color.allowanceBackground));
        }

    }




    @Override
    public int getItemCount() {
        return patientList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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
