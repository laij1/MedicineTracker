package com.clinic.anhe.medicinetracker.adapters;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.fragments.PatientsFragment;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.utils.CounterFab;
import com.clinic.anhe.medicinetracker.utils.Shift;

import java.util.ArrayList;
import java.util.List;

public class MedicineRecyclerViewAdapter extends RecyclerView.Adapter<MedicineRecyclerViewAdapter.MedicineViewHolder> {

    private static List<MedicineCardViewModel> medicineList;
    private static List<MedicineCardViewModel> cartList;
    private static Context mContext;
    private static CounterFab counterFab;



    public MedicineRecyclerViewAdapter(List<MedicineCardViewModel> medicineList, CounterFab counterFab) {
        this.medicineList = medicineList;
        this.counterFab = counterFab;
        cartList = new ArrayList<>();
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
                        //add to cartList
                        int position = getAdapterPosition();
                        cartList.add(medicineList.get(position));
                        Log.d("cartList added" + position, ""+ position);
                        counterFab.increase();
                    }
                    addButtonClicked = true;
                }
            });

        }
    }
}