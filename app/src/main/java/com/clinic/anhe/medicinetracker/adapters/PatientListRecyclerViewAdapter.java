package com.clinic.anhe.medicinetracker.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.fragments.PatientDetailFragment;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import java.util.List;

public class PatientListRecyclerViewAdapter extends RecyclerView.Adapter<PatientListRecyclerViewAdapter.PatientsListViewHolder> {

    private List<PatientsCardViewModel> patientList;
    private Fragment mFragment;
//    private OnItemClickListener mListener;
//
//    public interface OnItemClickListener {
//        void onItemClick(int position);
//    }
//
//    public void setOnItemClickListener( OnItemClickListener listener) {
//        mListener = listener;
//    }
    //constructor
    public PatientListRecyclerViewAdapter(List<PatientsCardViewModel> patientList, Fragment fragment){
        this.patientList = patientList;
        this.mFragment = fragment;
    }

    @NonNull
    @Override
    public PatientsListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_patients, parent, false);
        PatientsListViewHolder patientsListViewHolder = new PatientsListViewHolder(view);
        return patientsListViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PatientsListViewHolder holder, int position) {
        PatientsCardViewModel current =  patientList.get(position);
        holder.patientName.setText(current.getPatientName());
        holder.patientId.setText(current.getPatientIC());
        holder.imageButton.setVisibility(View.GONE);


    }

    @Override
    public int getItemCount() {
        return patientList.size();
    }

    public class PatientsListViewHolder extends RecyclerView.ViewHolder {
        public TextView patientName;
        public TextView patientId;
        public RadioButton imageButton;

        public PatientsListViewHolder(View itemView) {
            super(itemView);
            patientName = itemView.findViewById(R.id.patients_name);
            patientId = itemView.findViewById(R.id.patients_id);
            imageButton = itemView.findViewById(R.id.patients_addbutton);
            imageButton.setVisibility(View.GONE);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //TODO: here we enter the patient detail view
                    PatientDetailFragment patientDetailFragment = PatientDetailFragment.newInstance(patientList.get(getAdapterPosition()));

                    FragmentTransaction transaction = mFragment.getFragmentManager().beginTransaction();
                    transaction.replace(R.id.patient_list_layout,patientDetailFragment).commit();

                }
            });

        }
    }


}
