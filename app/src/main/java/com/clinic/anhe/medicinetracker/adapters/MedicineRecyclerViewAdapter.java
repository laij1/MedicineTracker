package com.clinic.anhe.medicinetracker.adapters;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;

import java.util.List;

public class MedicineRecyclerViewAdapter extends RecyclerView.Adapter<MedicineRecyclerViewAdapter.MedicineViewHolder> {

    private List<MedicineCardViewModel> medicineList;
    private static Context mContext;


    public MedicineRecyclerViewAdapter(List<MedicineCardViewModel> medicineList) {
        this.medicineList = medicineList;
    }

    @NonNull
    @Override
    public MedicineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview_medicine, parent, false);
        mContext = parent.getContext();
        MedicineViewHolder medicineViewHolder = new MedicineViewHolder(view);
        return medicineViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        MedicineCardViewModel current = medicineList.get(position);
        holder.imageIcon.setImageResource(current.getMedicineIcon());
        holder.medicineName.setText(current.getMedicinName());
        holder.medicineId.setText(current.getMedicineId());

    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    public static class MedicineViewHolder extends RecyclerView.ViewHolder {
        ImageView imageIcon;
        TextView medicineName;
        TextView medicineId;
        ImageButton imageButton;

        AnimatedVectorDrawable mAddDrawable;
        AnimatedVectorDrawable mCheckDrawable;
        boolean addButtonClicked = false;

        public MedicineViewHolder(View itemView) {
            super(itemView);
            imageIcon = itemView.findViewById(R.id.medicine_icon);
            medicineName = itemView.findViewById(R.id.medicine_name);
            medicineId = itemView.findViewById(R.id.medicine_id);
            imageButton = itemView.findViewById(R.id.medicine_add_button);

            mAddDrawable = (AnimatedVectorDrawable) mContext.getDrawable(R.drawable.ic_add_animatable);

            //setOnClickListener
            imageButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //once the medicine item is add, you cannot remove it till next fragment
                    if(addButtonClicked) {
                        //do nothing
                    }else {
                        imageButton.setImageDrawable(mAddDrawable);
                        mAddDrawable.start();
                    }
                    addButtonClicked = true;
                }
            });


        }
    }
}