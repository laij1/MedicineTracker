package com.clinic.anhe.medicinetracker.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
        RadioGroup paymentRadioGroup;
        RadioButton checkedRadioButton;

        AnimatedVectorDrawable mAddDrawable;
        AnimatedVectorDrawable mCheckDrawable;
        boolean addButtonClicked = false;

        public MedicineViewHolder(View itemView) {
            super(itemView);
            imageIcon = itemView.findViewById(R.id.medicine_icon);
            medicineName = itemView.findViewById(R.id.medicine_name);
            medicineId = itemView.findViewById(R.id.medicine_id);
            imageButton = itemView.findViewById(R.id.medicine_add_button);
            paymentRadioGroup = itemView.findViewById(R.id.payment_radiogroup);

            mAddDrawable = (AnimatedVectorDrawable) mContext.getDrawable(R.drawable.ic_add_animatable);
            mCheckDrawable =(AnimatedVectorDrawable) mContext.getDrawable(R.drawable.ic_check_animatable);

            //set default of payment to cash
            checkedRadioButton = itemView.findViewById(R.id.cash_radiobutton);
            checkedRadioButton.setSelected(true);
            checkedRadioButton.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorPrimaryDark)));

            //setOnClickListener
            imageButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(addButtonClicked) {
                        imageButton.setImageDrawable(mCheckDrawable);
                        mCheckDrawable.start();
                        int position = getAdapterPosition();
                        cartList.remove(medicineList.get(position));
                        Log.d("cartList removed: " + position, ""+ position);
                        counterFab.decrease();

                        //when remove from cart, you can change payment radio button
                        for(int i = 0; i < paymentRadioGroup.getChildCount(); i++) {
                            RadioButton radioButton = (RadioButton) paymentRadioGroup.getChildAt(i);
                            radioButton.setEnabled(true);
                        }
                    }else {
                        imageButton.setImageDrawable(mAddDrawable);
                        mAddDrawable.start();
                        //add to cartList
                        int position = getAdapterPosition();
                        MedicineCardViewModel item = medicineList.get(position);
                        cartList.add(item);
                        Log.d("cartList added: ", ""+ position);
                        Log.d("" + item.getMedicinName(), "cash payment: "+ item.IsCashPayment());
                        counterFab.increase();

                        //when added to cart, you can not change payment radio button
                        for(int i = 0; i < paymentRadioGroup.getChildCount(); i++) {
                            RadioButton radioButton = (RadioButton) paymentRadioGroup.getChildAt(i);
                            radioButton.setEnabled(false);
                        }

                    }
                    addButtonClicked = !addButtonClicked;
                }
            });


            paymentRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    selectItem(group, checkedId);
                }
            });
        }


        public void selectItem(RadioGroup group, int checkedId) {
            changeItemBackgroundTint();
            switch (checkedId){
                case R.id.cash_radiobutton:
                   checkedRadioButton = itemView.findViewById(R.id.cash_radiobutton);
                   checkedRadioButton.setBackgroundTintList(
                           ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorPrimaryDark)));
                   int position = getAdapterPosition();
                   medicineList.get(position).setCashPayment(true);
                   break;
                case R.id.credit_radiobutton:
                    checkedRadioButton = itemView.findViewById(R.id.credit_radiobutton);
                    checkedRadioButton.setBackgroundTintList(
                            ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorPrimaryDark)));
                    position = getAdapterPosition();
                    medicineList.get(position).setCashPayment(false);
                    break;
            }
        }

        private void changeItemBackgroundTint() {
            int count = paymentRadioGroup.getChildCount();
            for(int i = 0; i < count; i++) {
                View v = paymentRadioGroup.getChildAt(i);
                if (v instanceof RadioButton) {
                    ((RadioButton)v).setBackgroundTintList(
                            ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.menuTextIconColor)));
                }
            }

        }
    }
}