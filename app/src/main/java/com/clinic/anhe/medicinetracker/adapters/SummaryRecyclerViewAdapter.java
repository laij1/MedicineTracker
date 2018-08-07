package com.clinic.anhe.medicinetracker.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;

import java.util.ArrayList;
import java.util.List;

public class SummaryRecyclerViewAdapter extends RecyclerView.Adapter<SummaryRecyclerViewAdapter.SummaryViewHolder> {
    private List<MedicineCardViewModel> cartlist;

    public SummaryRecyclerViewAdapter(List<MedicineCardViewModel> cartlist) {
        this.cartlist = cartlist;
    }
    @NonNull
    @Override
    public SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_summary, parent, false);
        SummaryViewHolder summaryViewHolder = new SummaryViewHolder(view);
        return summaryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryViewHolder holder, int position) {
        holder.ItemName.setText(cartlist.get(position).getMedicinName());
    }



    @Override
    public int getItemCount() {
        return cartlist.size();
    }

    public static class SummaryViewHolder extends RecyclerView.ViewHolder {
        private TextView ItemName;
        public SummaryViewHolder(View itemView) {
            super(itemView);
            ItemName = itemView.findViewById(R.id.summary_itemname);
        }

    }
}