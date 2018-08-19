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
import com.clinic.anhe.medicinetracker.utils.MedicineType;
import com.clinic.anhe.medicinetracker.utils.PaymentType;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.ramotion.fluidslider.FluidSlider;


import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;

public class MedicineRecyclerViewAdapter extends RecyclerView.Adapter<MedicineRecyclerViewAdapter.MedicineViewHolder> {

    private CartViewModel cartViewModel;
    private List<MedicineCardViewModel> mlist;
    private Context mContext;
    private static CounterFab counterFab;
    private MedicineType medicineType;
    private final int min = 0;
    private final int max = 20;
    private final int total = max - min;
    private String sliderQuantity = "0";







    public MedicineRecyclerViewAdapter(CartViewModel cartViewModel, CounterFab counterFab, MedicineType medicineType) {
        this.cartViewModel = cartViewModel;
        this.counterFab = counterFab;
        this.medicineType = medicineType;

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
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull MedicineViewHolder holder, int position) {
        MedicineCardViewModel current = null;
        switch (medicineType) {
            case dialysis:
                current = cartViewModel.getDialysisList().get(position);
                break;
            case edible:
                current = cartViewModel.getEdibleList().get(position);
                break;
            case needle:
                current = cartViewModel.getNeedleList().get(position);
                break;
            case bandaid:
                current = cartViewModel.getBandaidList().get(position);
                break;
        }
        //MedicineCardViewModel current = cartViewModel.getMedicineLiveData().getValue().get(position);
//        holder.imageIcon.setImageResource(current.getMedicineIcon());
        holder.medicineName.setText(current.getMedicinName());
        holder.medicinePrice.setText(current.getMedicinePrice());
        holder.medicineDose.setText(current.getMedicineDose());
        holder.fluidSlider.setBubbleText( "" + current.getQuantity());
        holder.fluidSlider.setPosition(current.getSliderPosition());

        //TODO: if item in the cart, we need the ui to be ic_check
       boolean isAddtoCart = current.getIsAddToCart();
        // boolean isAddtoCart = cartViewModel.getMedicineLiveData().getValue().get(position).getIsAddToCart();
        Log.d("position: " + position, "is added to cart: " + isAddtoCart);
        if(isAddtoCart) {
            holder.imageButton.setImageResource(R.drawable.ic_check);
        } else {
            holder.imageButton.setImageResource(R.drawable.ic_add);
        }


        //TODO: set radio button
        if(current.isCashPayment() == PaymentType.CASH) {
            holder.cashRadioButton.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorPrimaryDark)));
            holder.creditRadioButton.setBackgroundTintList(
                    ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.menuTextIconColor)));
        } else if(current.isCashPayment() == PaymentType.MONTH) {
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
        switch(medicineType) {
            case dialysis:
                return cartViewModel.getDialysisList().size();
            case edible:
                return cartViewModel.getEdibleList().size();
            case needle:
                return cartViewModel.getNeedleList().size();
            case bandaid:
                return cartViewModel.getBandaidList().size();
        }
        return 0;
    }


    public void setList(List<MedicineCardViewModel> list) {
        mlist = list;
        notifyDataSetChanged();
    }

    public class MedicineViewHolder extends RecyclerView.ViewHolder {
//        ImageView imageIcon;
        TextView medicineName;
        TextView medicinePrice;
        TextView medicineDose;
        ImageButton imageButton;
        RadioGroup paymentRadioGroup;
//        RadioButton checkedRadioButton;
        RadioButton cashRadioButton;
        RadioButton creditRadioButton;
        FluidSlider fluidSlider;
        ImageView imageView;

//        AnimatedVectorDrawable mAddDrawable;
//        AnimatedVectorDrawable mCheckDrawable;
//        boolean addButtonClicked = false;

        public MedicineViewHolder(View itemView) {
            super(itemView);
//            imageIcon = itemView.findViewById(R.id.medicine_icon);
            medicineName = itemView.findViewById(R.id.medicine_name);
            medicinePrice = itemView.findViewById(R.id.medicine_price);
            medicineDose = itemView.findViewById(R.id.medicine_dose);
            imageButton = itemView.findViewById(R.id.medicine_add_button);
            paymentRadioGroup = itemView.findViewById(R.id.payment_radiogroup);
            cashRadioButton = itemView.findViewById(R.id.cash_radiobutton);
            creditRadioButton = itemView.findViewById(R.id.credit_radiobutton);
            fluidSlider = itemView.findViewById(R.id.medicine_fluidslider);
            imageView = itemView.findViewById(R.id.medicine_price_sign);



            fluidSlider.setPosition(0);
            fluidSlider.setEndText(String.valueOf(max));
            fluidSlider.setStartText(String.valueOf(min));
            fluidSlider.setPositionListener(new Function1<Float, Unit>() {
                @Override
                public Unit invoke(Float pos) {
                   // int position = getAdapterPosition();
                    sliderQuantity = String.valueOf((int) (min + total * pos));
                    Log.d("setPositionListener :" + sliderQuantity, "CHLOE");
                    //cartViewModel.setQuantity(position, medicineType, Integer.valueOf(value).intValue());
                    fluidSlider.setBubbleText(String.valueOf(sliderQuantity));

                    return Unit.INSTANCE;
                }
            });

            fluidSlider.setBeginTrackingListener(new Function0<Unit>() {
                @Override
                public Unit invoke() {
                    Log.d("begintrackingListener", "CHLOE");
                    return Unit.INSTANCE;
                }
            });

            fluidSlider.setEndTrackingListener(new Function0<Unit>() {
                @Override
                public Unit invoke() {
                    //we need livedata to save fluidSlider's position
                    int position = getAdapterPosition();
                    Log.d("endtrackingListener: " + sliderQuantity + " position is: " + position, "CHLOE"+ fluidSlider.getPosition() );
                    cartViewModel.setQuantity(position, medicineType, Integer.valueOf(sliderQuantity).intValue());
                    cartViewModel.setSliderPosition(position, medicineType, fluidSlider.getPosition());

                    return Unit.INSTANCE;
                }
            });

//
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
                    int position = getAdapterPosition();
                    MedicineCardViewModel current = null;
                    switch (medicineType) {
                        case dialysis:
                            current = cartViewModel.getDialysisList().get(position);
                            break;
                        case edible:
                            current = cartViewModel.getEdibleList().get(position);
                            break;
                        case needle:
                            current = cartViewModel.getNeedleList().get(position);
                            break;
                        case bandaid:
                            current = cartViewModel.getBandaidList().get(position);
                            break;

                    }

                    if(current.getIsAddToCart()) {
//                        imageButton.setImageDrawable(mCheckDrawable);
//                        mCheckDrawable.start();
                        imageButton.setImageResource(R.drawable.ic_add);
                        cartViewModel.removeFromCart(position, medicineType);
//                        cartList.remove(cartViewModel.get(position));
                        //counterFab.decrease();
                        cartViewModel.decreaseCount();
                        Log.d("cartList removed: " + position, " counterfab count is: "+ counterFab.getCount());


                    }else {

                         if(current.isCashPayment() == PaymentType.UNSELECT) {
                            //show the dialog to remind that these values should be set
                            final SweetAlertDialog paymentAlert = new SweetAlertDialog(mContext);
                                    paymentAlert
                                    .setTitleText("請選擇付款方式")
                                    .show();

                                  paymentAlert.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                      @Override
                                      public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            Log.d("sweet dialog confirm button is clicked","CHLOE" );
                                          sweetAlertDialog.dismiss();
                                      }
                                  });
                         } else if(current.getQuantity() == 0) {
                             //show the dialog to remind that these values should be set
                             new SweetAlertDialog(mContext)
                                     .setTitleText("請選擇藥品數量")
                                     .show();

                         } else {
                            imageButton.setImageResource(R.drawable.ic_check);
//                        List<MedicineCardViewModel> list = cartViewModel.getMedicineList();
//                        list.get(position).addToCart();
//                        cartViewModel.getMedicineLiveData().setValue(list);
                            cartViewModel.addToCart(position, medicineType);
                            //Log.d("" + item.getMedicinName(), "cash payment: "+ item.isCashPayment());
                            // counterFab.increase();
                            cartViewModel.increaseCount();
                            Log.d("cartList added: " + position, " counterfab count is: " + counterFab.getCount());
                        }
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
                   cartViewModel.setCashPayment(position, medicineType);
                   break;
                case R.id.credit_radiobutton:
//                    checkedRadioButton = itemView.findViewById(R.id.credit_radiobutton);
                    creditRadioButton.setBackgroundTintList(
                            ColorStateList.valueOf(ContextCompat.getColor(mContext, R.color.colorPrimaryDark)));
                    position = getAdapterPosition();
                    cartViewModel.setCreditPayment(position, medicineType);
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