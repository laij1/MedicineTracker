package com.clinic.anhe.medicinetracker.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.AddMedicineRecordViewModel;
import com.clinic.anhe.medicinetracker.ViewModel.SelectedEmployeeViewModel;
import com.clinic.anhe.medicinetracker.model.EmployeeCardViewModel;
import com.clinic.anhe.medicinetracker.model.MedicineRecordCardViewModel;

import java.util.Map;

public class AddMedicineRecordRecyclerViewAdapter extends RecyclerView.Adapter<AddMedicineRecordRecyclerViewAdapter.AddMedicineRecordViewHolder>{

//    private Map<String, Integer> employeeMap;
    private AddMedicineRecordViewModel addMedicineRecordViewModel;

    public AddMedicineRecordRecyclerViewAdapter(AddMedicineRecordViewModel addMedicineRecordViewModel) {
//        this.employeeMap = employee;
        this.addMedicineRecordViewModel = addMedicineRecordViewModel;

    }

    @NonNull
    @Override
    public AddMedicineRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_nurse_simple, parent, false);

        AddMedicineRecordViewHolder addMedicineRecordViewHolder = new AddMedicineRecordViewHolder(view);
        return addMedicineRecordViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddMedicineRecordViewHolder holder, int position) {
        EmployeeCardViewModel current = addMedicineRecordViewModel.getEmployListLiveData().getValue().get(position);
        holder.mEmployeeName.setText(current.getEmployeeName());

        if(addMedicineRecordViewModel.getSelectedEmployee()!= null &&
                addMedicineRecordViewModel.getSelectedEmployee().getValue().getEmployeeName().equalsIgnoreCase(current.getEmployeeName())) {
            holder.mSelectButton.setChecked(true);
        } else {
            holder.mSelectButton.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return addMedicineRecordViewModel.getEmployListLiveData().getValue().size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class AddMedicineRecordViewHolder extends RecyclerView.ViewHolder{

        private TextView mEmployeeName;
        private RadioButton mSelectButton;

        public AddMedicineRecordViewHolder(View itemView) {
            super(itemView);
            mEmployeeName = itemView.findViewById(R.id.nurse_simple_name);
            mSelectButton = itemView.findViewById(R.id.nurse_simple_addbutton);
            mSelectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition() != -1) {
                        EmployeeCardViewModel current = addMedicineRecordViewModel.getEmployListLiveData().getValue().get(getAdapterPosition());
                        addMedicineRecordViewModel.getSelectedEmployee().setValue(current);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }
}
