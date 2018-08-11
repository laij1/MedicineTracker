package com.clinic.anhe.medicinetracker.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.utils.PaymentType;

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
        holder.itemName.setText(cartlist.get(position).getMedicinName());
        holder.itemPayment.setText(cartlist.get(position).isCashPayment() == PaymentType.CASH ? "現" : "月");
        //TODO: 買十送二 needs special calculation
        int quantity = cartlist.get(position).getQuantity();
        int subtotal = Integer.valueOf(cartlist.get(position).getMedicineId()).intValue() * quantity;
        holder.itemQuantity.setText(String.valueOf(cartlist.get(position).getQuantity()));
        holder.itemSubtotal.setText(String.valueOf(subtotal));
    }



    @Override
    public int getItemCount() {
        return cartlist.size();
    }

    public static class SummaryViewHolder extends RecyclerView.ViewHolder {
        private TextView itemName;
        private TextView itemPayment;
        private TextView itemQuantity;
        private TextView itemSubtotal;

        public SummaryViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.summary_itemname);
            itemPayment = itemView.findViewById(R.id.summary_payment);
            itemQuantity = itemView.findViewById(R.id.summary_quantity);
            itemSubtotal = itemView.findViewById(R.id.summary_subtotal);

        }

    }
}
