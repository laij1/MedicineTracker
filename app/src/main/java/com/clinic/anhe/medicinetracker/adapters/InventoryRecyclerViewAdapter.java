package com.clinic.anhe.medicinetracker.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.InventoryViewModel;
import com.clinic.anhe.medicinetracker.model.InventoryCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;

public class InventoryRecyclerViewAdapter extends RecyclerView.Adapter<InventoryRecyclerViewAdapter.InventoryViewHolder> {
    private InventoryViewModel inventoryViewModel;
    private Context mContext;

    public InventoryRecyclerViewAdapter(InventoryViewModel inventoryViewModel) {
       this.inventoryViewModel = inventoryViewModel;
    }
    @NonNull
    @Override
    public InventoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_inventory, parent, false);

        mContext = view.getContext();

        InventoryViewHolder inventoryViewHolder = new InventoryViewHolder(view);
        return inventoryViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InventoryViewHolder holder, int position) {
        InventoryCardViewModel current = inventoryViewModel.getInventoryListLiveData().getValue().get(position);
        holder.itemName.setText(current.getMedicineName());
        holder.itemCreateDate.setText(current.getCreateAt());
        holder.itemAmount.setText(String.valueOf(current.getAmount()));
    }

    @Override
    public int getItemCount() {
        return inventoryViewModel.getInventoryListLiveData().getValue().size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class InventoryViewHolder extends RecyclerView.ViewHolder {
        private TextView itemCreateDate;
        private TextView itemName;
        private TextView itemAmount;


        public InventoryViewHolder(View itemView) {
            super(itemView);
            itemCreateDate = itemView.findViewById(R.id.inventory_createdate);
            itemName = itemView.findViewById(R.id.inventory_itemname);
            itemAmount = itemView.findViewById(R.id.inventory_amount);
        }
    }
}
