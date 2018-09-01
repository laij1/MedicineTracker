package com.clinic.anhe.medicinetracker.adapters;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.SelectedPatientViewModel;
import com.clinic.anhe.medicinetracker.fragments.SelectPatientFragment;
import com.clinic.anhe.medicinetracker.fragments.SelectPatientsDialogFragment;
import com.clinic.anhe.medicinetracker.model.EmployeeCardViewModel;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;

import java.util.List;

public class DashboardRecyclerViewAdapter extends RecyclerView.Adapter<DashboardRecyclerViewAdapter.EmployeeViewHolder>{

    private List<EmployeeCardViewModel> employeeList;
    private Context mContext;
    private Fragment mFragment;

    public DashboardRecyclerViewAdapter(List<EmployeeCardViewModel> employeeList, Fragment mFragment){
        this.employeeList = employeeList;
        this.mFragment = mFragment;
    }


    @NonNull

    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_nurse_simple, parent, false);

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

        public EmployeeViewHolder(View itemView) {
            super(itemView);
            mNurseName = itemView.findViewById(R.id.nurse_simple_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO: show patient selection
//                    Toast.makeText(mContext, "item is clicked", Toast.LENGTH_LONG).show();
                    SelectPatientsDialogFragment fragment = new SelectPatientsDialogFragment();
                    fragment.show(mFragment.getFragmentManager(), "selectdashboardpatients");

                }
            });
        }
    }
}
