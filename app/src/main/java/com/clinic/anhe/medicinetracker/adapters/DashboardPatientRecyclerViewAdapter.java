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
    //    private int lastCheckedPosition = -1;
    private DashboardViewModel dashboardViewModel;
    private String nurseName;

    //constructor
    public DashboardPatientRecyclerViewAdapter(List<PatientsCardViewModel> patientList, DashboardViewModel dashboardViewModel, String nurseName) {
        this.patientList = patientList;
        this.dashboardViewModel = dashboardViewModel;
        this.nurseName = nurseName;
        Log.d("nurseName is ", nurseName);

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
//        holder.imageButton.setSelected(false);

        if(nurseName.equalsIgnoreCase(dashboardViewModel.getNurseLiveData().getValue())) {
            for (String name : dashboardViewModel.getSelectedPatientsList()) {
                Log.d("the live data has:" + dashboardViewModel.getSelectedPatientsList().size(), name);
                if (name.equalsIgnoreCase(current.getPatientName())) {
                    Log.d("check radio button:", name);
                    holder.imageButton.setChecked(true);
//                holder.imageButton.setSelected(true);
                }
            }
        }
//        Iterator<Map.Entry<String, List<String>>> it =  dashboardViewModel.getDashboardMap().entrySet().iterator();
//        while (it.hasNext()) {
//            Map.Entry<String, List<String>> pair = it.next();
//            if(pair.getKey().equalsIgnoreCase(nurseName)){
//                Iterator<String> listIt = pair.getValue().iterator();
//                while (listIt.hasNext()) {
//                    String s = listIt.next();
//                    if(s.equalsIgnoreCase(current.getPatientName())){
//                        holder.imageButton.setChecked(true);
//                    }
//                }
//            }
//        }

//        for(Map.Entry<String,List<String>> entry: dashboardViewModel.getDashboardMap().entrySet()) {
//                if(entry.getKey().equalsIgnoreCase(nurseName)) {
//                    for(String p : entry.getValue()) {
//                        if(p.equalsIgnoreCase(current.getPatientName())){
//                            Log.d("checking radio button" , p);
//                            holder.imageButton.setChecked(true);
//                        }
//                    }
//                }
//        }
//
//        Log.d(holder.imageButton.isChecked()?"imagebutton is checked":"not check", "chloe blind view" );

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
            patientId.setVisibility(View.GONE);

//            imageButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    PatientsCardViewModel current = patientList.get(getAdapterPosition());
//                    if(isChecked == true) {
//                        Log.d("radio button is :" , isChecked==true?"true":"false");
//                        dashboardViewModel.getSelectedPatientsList().add(current.getPatientName());
//                        dashboardViewModel.getSelectedPatientsLiveData().setValue(dashboardViewModel.getSelectedPatientsList());
//                        dashboardViewModel.getDashboardMap().put(nurseName,dashboardViewModel.getSelectedPatientsList());
//                        dashboardViewModel.getDashboardMapLiveData().setValue(dashboardViewModel.getDashboardMap());
//                    } else {
//                        if (dashboardViewModel.getSelectedPatientsList().contains(current.getPatientName())) {
//                            dashboardViewModel.getSelectedPatientsList().remove(current.getPatientName());
//                            dashboardViewModel.getSelectedPatientsLiveData().setValue(dashboardViewModel.getSelectedPatientsList());
//                            dashboardViewModel.getDashboardMap().put(nurseName, dashboardViewModel.getSelectedPatientsList());
//                            dashboardViewModel.getDashboardMapLiveData().setValue(dashboardViewModel.getDashboardMap());
//                        }
//                    }
//                }
//            });

            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PatientsCardViewModel current = patientList.get(getAdapterPosition());
//                    if (imageButton.isChecked()) {
//                        imageButton.setChecked(false);
//                    } else {
//                        imageButton.setChecked(true);
//                    }

//                    lastCheckedPosition = getAdapterPosition();
                    //TODO: set nurseName
                    if(nurseName.equalsIgnoreCase(dashboardViewModel.getNurseLiveData().getValue())) {
                        Log.d(imageButton.isChecked() ? "imagebutton is checked" : "not check", "chloe before live data");
//                    //TODO:
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
                    Log.d(imageButton.isChecked()?"imagebutton is checked":"not check", "chloe after live data" );
//                    dashboardViewModel.getDashboardMap().put(nurseName,dashboardViewModel.getSelectedPatientsList());
//                    dashboardViewModel.getDashboardMapLiveData().setValue(dashboardViewModel.getDashboardMap());
//                    Iterator<Map.Entry<String, List<String>>> it =  dashboardViewModel.getDashboardMap().entrySet().iterator();
//                    while (it.hasNext()) {
//                        Map.Entry<String, List<String>> pair = it.next();
//                        if(pair.getKey().equalsIgnoreCase(nurseName)){
//                            Iterator<String> listIt = pair.getValue().iterator();
//                            while (listIt.hasNext()) {
//                                String s = listIt.next();
//            //                    if(s.equalsIgnoreCase(current.getPatientName())){
//                                    Log.d("livedata for ",  pair.getKey() + s);
//                                }
//                            }
//                        }

                }
            });
        }
    }

}
