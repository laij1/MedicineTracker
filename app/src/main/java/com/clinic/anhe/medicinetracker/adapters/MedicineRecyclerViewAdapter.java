package com.clinic.anhe.medicinetracker.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
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

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.ViewModel.CartViewModel;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.utils.CounterFab;
import com.clinic.anhe.medicinetracker.utils.PaymentType;
import com.ramotion.fluidslider.FluidSlider;

import java.util.List;

public class MedicineRecyclerViewAdapter extends RecyclerView.Adapter<MedicineRecyclerViewAdapter.MedicineViewHolder> {

    private static CartViewModel medicineList;
    private static List<MedicineCardViewModel> mlist;
    private static Context mContext;
    private static CounterFab counterFab;




    public MedicineRecyclerViewAdapter(CartViewModel medicineList, CounterFab counterFab) {
        this.medicineList = medicineList;
        this.counterFab = counterFab;
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
        MedicineCardViewModel current = medicineList.getMedicineLiveData().getValue().get(position);
//        holder.imageIcon.setImageResource(current.getMedicineIcon());
        holder.medicineName.setText(current.getMedicinName());
        holder.medicineId.setText(current.getMedicineId());

        //TODO: if item in the cart, we need the ui to be ic_check
        boolean isAddtoCart = medicineList.getMedicineLiveData().getValue().get(position).getIsAddToCart();
        Log.d("position: " + position, "is added to cart: " + isAddtoCart);
        if(medicineList.getMedicineLiveData().getValue().get(position).getIsAddToCart()) {
            holder.imageButton.setImageResource(R.drawable.ic_check);
        } else {
            holder.imageButton.setImageResource(R.drawable.ic_add);
        }


        //TODO: set radio button
        if(medicineList.getMedicineList().get(position).isCashPayment() == PaymentType.CASH) {
            holder.cashRadioButton.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorPrimaryDark)));
            holder.creditRadioButton.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.menuTextIconColor)));
        } else if(medicineList.getMedicineList().get(position).isCashPayment() == PaymentType.MONTH) {
            holder.cashRadioButton.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.menuTextIconColor)));
            holder.creditRadioButton.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorPrimaryDark)));
        } else {
            holder.cashRadioButton.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.menuTextIconColor)));
            holder.creditRadioButton.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.menuTextIconColor)));
        }

    }

    @Override
    public int getItemCount() {
        if (medicineList.getMedicineLiveData().getValue() != null) {
            return medicineList.getMedicineLiveData().getValue().size();
        }
        return 0;
    }


    public void setList(List<MedicineCardViewModel> list) {
        mlist = list;
        notifyDataSetChanged();
    }

    public static class MedicineViewHolder extends RecyclerView.ViewHolder {
//        ImageView imageIcon;
        TextView medicineName;
        TextView medicineId;
        ImageButton imageButton;
        RadioGroup paymentRadioGroup;
//        RadioButton checkedRadioButton;
        RadioButton cashRadioButton;
        RadioButton creditRadioButton;
        FluidSlider fluidSlider;
//        AnimatedVectorDrawable mAddDrawable;
//        AnimatedVectorDrawable mCheckDrawable;
//        boolean addButtonClicked = false;

        public MedicineViewHolder(View itemView) {
            super(itemView);
//            imageIcon = itemView.findViewById(R.id.medicine_icon);
            medicineName = itemView.findViewById(R.id.medicine_name);
            medicineId = itemView.findViewById(R.id.medicine_id);
            imageButton = itemView.findViewById(R.id.medicine_add_button);
            paymentRadioGroup = itemView.findViewById(R.id.payment_radiogroup);
            cashRadioButton = itemView.findViewById(R.id.cash_radiobutton);
            creditRadioButton = itemView.findViewById(R.id.credit_radiobutton);
            fluidSlider = itemView.findViewById(R.id.medicine_fluidslider);
//
//            mAddDrawable = (AnimatedVectorDrawable) mContext.getDrawable(R.drawable.ic_add_animatable);
//            mCheckDrawable =(AnimatedVectorDrawable) mContext.getDrawable(R.drawable.ic_check_animatable);

            //set default of payment to cash
//            checkedRadioButton = itemView.findViewById(R.id.cash_radiobutton);
//            checkedRadioButton.setSelected(true);
//            checkedRadioButton.setBackgroundTintList(
//                    ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorPrimaryDark)));


            //setOnClickListener
            imageButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    final int position = getAdapterPosition();

                    if(medicineList.getMedicineList().get(position).getIsAddToCart()) {
//                        imageButton.setImageDrawable(mCheckDrawable);
//                        mCheckDrawable.start();
                        imageButton.setImageResource(R.drawable.ic_add);
                        medicineList.removeFromCart(position);
//                        cartList.remove(medicineList.get(position));
                        Log.d("cartList removed: " + position, ""+ position);
                        counterFab.decrease();

                    }else {
                        imageButton.setImageResource(R.drawable.ic_check);
//                        List<MedicineCardViewModel> list = medicineList.getMedicineList();
//                        list.get(position).addToCart();
//                        medicineList.getMedicineLiveData().setValue(list);
                        medicineList.addToCart(position);
                        Log.d("cartList added: ", "" + position);
                        //Log.d("" + item.getMedicinName(), "cash payment: "+ item.isCashPayment());
                        counterFab.increase();

                    }
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
//                   checkedRadioButton = itemView.findViewById(R.id.cash_radiobutton);
                   cashRadioButton.setBackgroundTintList(
                           ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorPrimaryDark)));
                   int position = getAdapterPosition();
                   medicineList.setCashPayment(position);
                   break;
                case R.id.credit_radiobutton:
//                    checkedRadioButton = itemView.findViewById(R.id.credit_radiobutton);
                    creditRadioButton.setBackgroundTintList(
                            ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorPrimaryDark)));
                    position = getAdapterPosition();
                    medicineList.setCreditPayment(position);
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