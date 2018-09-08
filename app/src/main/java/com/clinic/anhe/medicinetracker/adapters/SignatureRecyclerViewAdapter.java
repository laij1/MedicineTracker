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
import com.clinic.anhe.medicinetracker.ViewModel.SelectedEmployeeViewModel;
import com.clinic.anhe.medicinetracker.model.EmployeeCardViewModel;

import java.util.Map;

public class SignatureRecyclerViewAdapter extends RecyclerView.Adapter<SignatureRecyclerViewAdapter.SignatureViewHolder> {

    private Map<String, Integer> employee;
    private String[] employeeList;
    private SelectedEmployeeViewModel selectedEmployeeViewModel;

    public SignatureRecyclerViewAdapter(Map<String, Integer> employee, String[] employeeList, SelectedEmployeeViewModel selectedEmployeeViewModel) {
        this.employee = employee;
        this.employeeList = employeeList;
        this.selectedEmployeeViewModel = selectedEmployeeViewModel;
    }

    @NonNull
    @Override
    public SignatureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_nurse_simple, parent, false);
         SignatureViewHolder signatureViewHolder = new SignatureViewHolder(view);
         return signatureViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SignatureViewHolder holder, int position) {
        String current = employeeList[position];
        holder.mEmployeeName.setText(current);

        if(selectedEmployeeViewModel.getSelectedEmployee().getValue()!=null &&
                selectedEmployeeViewModel.getSelectedEmployee().getValue().getEmployeeName().equalsIgnoreCase(current)) {
            holder.mSelectButton.setChecked(true);
        } else {
            holder.mSelectButton.setChecked(false);
        }

    }

    @Override
    public int getItemCount() {
        return employeeList.length;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class SignatureViewHolder extends RecyclerView.ViewHolder {
        private TextView mEmployeeName;
        private RadioButton mSelectButton;


        public SignatureViewHolder(View itemView) {
            super(itemView);
            mEmployeeName = itemView.findViewById(R.id.nurse_simple_name);
            mSelectButton = itemView.findViewById(R.id.nurse_simple_addbutton);
            mSelectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition() != -1) {
                        String current = employeeList[getAdapterPosition()];
                        Integer eid = employee.get(current);
                        EmployeeCardViewModel e = new EmployeeCardViewModel(current, eid, "");
                        selectedEmployeeViewModel.getSelectedEmployee().setValue(e);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }


}
