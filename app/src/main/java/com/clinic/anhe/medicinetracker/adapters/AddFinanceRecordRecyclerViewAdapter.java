package com.clinic.anhe.medicinetracker.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.AddFinanceRecordViewModel;
import com.clinic.anhe.medicinetracker.model.EmployeeCardViewModel;

public class AddFinanceRecordRecyclerViewAdapter extends RecyclerView.Adapter<AddFinanceRecordRecyclerViewAdapter.AddFinanceRecordViewHolder>{

    private AddFinanceRecordViewModel addFinanceRecordViewModel;

    public AddFinanceRecordRecyclerViewAdapter(AddFinanceRecordViewModel addFinanceRecordViewModel){
        this.addFinanceRecordViewModel = addFinanceRecordViewModel;
    }

    @NonNull
    @Override
    public AddFinanceRecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_nurse_simple, parent, false);

        AddFinanceRecordViewHolder addFinanceRecordViewHolder = new AddFinanceRecordViewHolder(view);
        return  addFinanceRecordViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddFinanceRecordViewHolder holder, int position) {
        EmployeeCardViewModel current = addFinanceRecordViewModel.getEmployListLiveData().getValue().get(position);
        holder.mEmployeeName.setText(current.getEmployeeName());

        if(addFinanceRecordViewModel.getSelectedEmployee()!= null &&
                addFinanceRecordViewModel.getSelectedEmployee().getValue().getEmployeeName().equalsIgnoreCase(current.getEmployeeName())) {
            holder.mSelectButton.setChecked(true);
        } else {
            holder.mSelectButton.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return addFinanceRecordViewModel.getEmployListLiveData().getValue().size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class AddFinanceRecordViewHolder extends RecyclerView.ViewHolder {
        private TextView mEmployeeName;
        private RadioButton mSelectButton;

        public AddFinanceRecordViewHolder(View itemView) {
            super(itemView);
            mEmployeeName = itemView.findViewById(R.id.nurse_simple_name);
            mSelectButton = itemView.findViewById(R.id.nurse_simple_addbutton);
            mSelectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition() != -1) {
                        EmployeeCardViewModel current = addFinanceRecordViewModel.getEmployListLiveData().getValue().get(getAdapterPosition());
                        addFinanceRecordViewModel.getSelectedEmployee().setValue(current);
                        notifyDataSetChanged();
                    }
                }
            });
        }
    }
}
