package com.clinic.anhe.medicinetracker.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;

import java.util.ArrayList;
import java.util.List;

import com.clinic.anhe.medicinetracker.R;

public class CartViewModel extends ViewModel {

    private MutableLiveData<List<MedicineCardViewModel>> medicineLiveData;
    List<MedicineCardViewModel> medicineList;

//    private MutableLiveData<List<MedicineCardViewModel>> cartLiveData;
//    List<MedicineCardViewModel> cartList;

    public CartViewModel(){

        medicineLiveData = new MutableLiveData<>();
        medicineList = new ArrayList<>();

        medicineList.add(new MedicineCardViewModel("HDF", "", R.drawable.ic_medicine));
        medicineList.add(new MedicineCardViewModel("NESP", "20ug", R.drawable.ic_medicine));
        medicineList.add(new MedicineCardViewModel("NESP", "40ug", R.drawable.ic_medicine));
        medicineList.add(new MedicineCardViewModel("EPO", "2000", R.drawable.ic_medicine));
        medicineList.add(new MedicineCardViewModel("Carnitine", "原廠", R.drawable.ic_medicine));
        medicineList.add(new MedicineCardViewModel("Carnitine", "台廠", R.drawable.ic_medicine));
        medicineList.add(new MedicineCardViewModel("Provita", " ", R.drawable.ic_medicine));
        medicineList.add(new MedicineCardViewModel("循利寧", "", R.drawable.ic_medicine));
        medicineList.add(new MedicineCardViewModel("Nephrosteril", "", R.drawable.ic_medicine));
        medicineList.add(new MedicineCardViewModel("IDPN", "", R.drawable.ic_medicine));
        medicineList.add(new MedicineCardViewModel("Methionin-B", "", R.drawable.ic_medicine));
        medicineList.add(new MedicineCardViewModel("Anol", "", R.drawable.ic_medicine));


        medicineLiveData.postValue(medicineList);

//        //for cart
//        cartLiveData = new MutableLiveData<>();
//        cartList = new ArrayList<>();


    }

    public MutableLiveData<List<MedicineCardViewModel>> getMedicineLiveData() {
        return medicineLiveData;
    }

    public List<MedicineCardViewModel> getMedicineList(){
        return medicineLiveData.getValue();
    }


    public void addToCart(int position) {
        MedicineCardViewModel item = getMedicineList().get(position);
        item.addToCart();
        medicineLiveData.postValue(medicineList);
    }

   public void removeFromCart(int position) {
       MedicineCardViewModel item = medicineList.get(position);
       item.removeFromCart();
       medicineLiveData.postValue(medicineList);
    }


    public void setCashPayment(int position) {
        MedicineCardViewModel item = getMedicineList().get(position);
        item.setCashPayment(true);
        medicineLiveData.postValue(medicineList);
    }

    public void setCreditPayment(int position) {
        MedicineCardViewModel item = getMedicineList().get(position);
        item.setCashPayment(false);
        medicineLiveData.postValue(medicineList);
    }

//    public void addtoCartList(MedicineCardViewModel item) {
//        cartList.add(item);
//        cartLiveData.postValue(cartList);
//    }
//
//    public void removeFromCartList(MedicineCardViewModel item) {
//        cartList.remove(item);
//        cartLiveData.postValue(cartList);
//    }

//    public List<MedicineCardViewModel> getCartList() {
//        return cartLiveData.getValue();
//    }
}
