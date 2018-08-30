package com.clinic.anhe.medicinetracker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.utils.MedicineType;

import java.util.ArrayList;
import java.util.List;

public class MedicineSimpleRecyclerViewAdapter extends RecyclerView.Adapter<MedicineSimpleRecyclerViewAdapter.MedicineSimpleViewHolder> {

    private MedicineType medicineType;
    private Context mContext;
    private List<MedicineCardViewModel> medicineList;
//    private List<MedicineCardViewModel> currentList;

    public MedicineSimpleRecyclerViewAdapter( MedicineType medicineType, List<MedicineCardViewModel> medicineList) {
        this.medicineType = medicineType;
        this.medicineList = medicineList;
//        currentList = new ArrayList<>();
//        for(MedicineCardViewModel m : medicineList) {
//            if(medicineType.toString().equals(m.getMedicineCategory())) {
//                currentList.add(m);
//            }
//        }
    }

    @NonNull
    @Override
    public MedicineSimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_medicine_simple, parent, false);
        mContext = parent.getContext();
        MedicineSimpleViewHolder medicineSimpleViewHolder = new MedicineSimpleViewHolder(view);
        return medicineSimpleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineSimpleViewHolder holder, int position) {
        holder.name.setText(medicineList.get(position).getMedicinName());

    }


    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public class MedicineSimpleViewHolder extends RecyclerView.ViewHolder {

        private TextView name;

        public MedicineSimpleViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.medicine_simple_name);
        }
    }
}
