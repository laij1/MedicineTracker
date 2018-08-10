package com.clinic.anhe.medicinetracker.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;

import java.util.ArrayList;
import java.util.List;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.utils.MedicineType;

public class CartViewModel extends ViewModel {

    private MutableLiveData<List<MedicineCardViewModel>> medicineLiveData;
//    List<MedicineCardViewModel> medicineList;

    private MutableLiveData<List<MedicineCardViewModel>> dialysisLiveData;
    List<MedicineCardViewModel> dialysisList;

    private MutableLiveData<List<MedicineCardViewModel>> edibleLiveData;
    List<MedicineCardViewModel> edibleList;

    private MutableLiveData<List<MedicineCardViewModel>> needleLiveData;
    List<MedicineCardViewModel> needleList;

    private MutableLiveData<List<MedicineCardViewModel>> bandaidLiveData;
    List<MedicineCardViewModel> bandaidList;

    private MutableLiveData<Integer> count;

    public CartViewModel(){
        initDialysisList();
        initEdibleList();
        initNeedleList();
        initBandaidList();
        initCount();
        Log.d("all the medical list has been initiated", "CHLOE!!!!");
    }

    public void initCount() {
        count = new MutableLiveData<>();
        Integer i = Integer.valueOf(0);
        count.setValue(i);
        Log.d("current count is: " + count.getValue(), "CHLOE!!!");
    }

    public void initDialysisList() {
        dialysisLiveData = new MutableLiveData<>();
        dialysisList = new ArrayList<>();
        dialysisList.add(new MedicineCardViewModel("Vit D3","500","/瓶"));
        dialysisList.add(new MedicineCardViewModel("Vit B3","1200","/瓶"));
        dialysisList.add(new MedicineCardViewModel("Cinacalcet","5100","/盒"));

        //dialysisLiveData.postValue(dialysisList);
        dialysisLiveData.setValue(dialysisList);
    }

    public void initEdibleList() {
        edibleLiveData = new MutableLiveData<>();
        edibleList = new ArrayList<>();
        edibleList.add(new MedicineCardViewModel("Vit D3","500","/瓶"));
        edibleList.add(new MedicineCardViewModel("Vit B3","1200","/瓶"));
        edibleList.add(new MedicineCardViewModel("Cinacalcet","5100","/盒"));
        edibleList.add(new MedicineCardViewModel("Kremezin","165","/包"));
        edibleList.add(new MedicineCardViewModel("Kremezin","13500","/盒"));
        edibleList.add(new MedicineCardViewModel("Renvela(錠)","6500", "/180顆"));
        edibleList.add(new MedicineCardViewModel("Renvela(粉)","3250", "/90包"));
        edibleList.add(new MedicineCardViewModel("Dephos","5000", "/2瓶1組"));
        edibleList.add(new MedicineCardViewModel("Regpara","6300", "/30顆"));
        edibleList.add(new MedicineCardViewModel("Fosrenal 750mg","6750", "/3+1瓶"));
        edibleList.add(new MedicineCardViewModel("Forsrenal 1000mg","6750", "/3瓶"));
        edibleList.add(new MedicineCardViewModel("普寧腎","90", "/瓶"));
        edibleList.add(new MedicineCardViewModel("普寧腎","2160", "/箱"));
        edibleList.add(new MedicineCardViewModel("福寧補","600", "/盒"));
        edibleList.add(new MedicineCardViewModel("阿德比(錠)","350", "/瓶"));
        edibleList.add(new MedicineCardViewModel("阿德比(粉)","1500", "/60包"));
       // edibleList.add(new MedicineCardViewModel("福寧補","600", "/盒"));

       // edibleLiveData.postValue(edibleList);
        edibleLiveData.setValue(edibleList);
        //medicineLiveData.postValue(edibleList);


    }
    public void initNeedleList() {
        needleLiveData = new MutableLiveData<>();
        needleList = new ArrayList<>();
        needleList.add(new MedicineCardViewModel("(needle)Vit D3","500","/瓶"));
        needleList.add(new MedicineCardViewModel("(needle)Vit B3","1200","/瓶"));
        needleList.add(new MedicineCardViewModel("(needle)Cinacalcet","5100","/盒"));

       // needleLiveData.postValue(needleList);
        needleLiveData.setValue(needleList);
    }
    public void initBandaidList() {
        bandaidLiveData = new MutableLiveData<>();
        bandaidList = new ArrayList<>();
        bandaidList.add(new MedicineCardViewModel("(bandaid)Vit D3","500","/瓶"));
        bandaidList.add(new MedicineCardViewModel("(bandaid)Vit B3","1200","/瓶"));
        bandaidList.add(new MedicineCardViewModel("(bandaid)Cinacalcet","5100","/盒"));

       // bandaidLiveData.postValue(bandaidList);
        bandaidLiveData.setValue(bandaidList);

    }

    //TODO: add get all four tabs livedata


    public MutableLiveData<List<MedicineCardViewModel>> getDialysisLiveData() {
        return dialysisLiveData;
    }

    public MutableLiveData<List<MedicineCardViewModel>> getEdibleLiveData() {
        return edibleLiveData;
    }

    public MutableLiveData<List<MedicineCardViewModel>> getNeedleLiveData() {
        return needleLiveData;
    }

    public MutableLiveData<List<MedicineCardViewModel>> getBandaidLiveData() {
        return bandaidLiveData;
    }

    public MutableLiveData getCountLiveData() {
        return count;
    }

//    public MutableLiveData<List<MedicineCardViewModel>> getMedicineLiveData() {
//        return medicineLiveData;
//    }


    public List<MedicineCardViewModel> getDialysisList() {
        return dialysisLiveData.getValue();
    }

    public List<MedicineCardViewModel> getEdibleList() {
        return edibleLiveData.getValue();
    }

    public List<MedicineCardViewModel> getNeedleList() {
        return needleLiveData.getValue();
    }

    public List<MedicineCardViewModel> getBandaidList() {
        return bandaidLiveData.getValue();
    }



    public MutableLiveData increaseCount() {
        Integer i = count.getValue();
        int added = i.intValue() + 1;
        count.setValue(Integer.valueOf(added));
        return count;
    }

    public MutableLiveData decreaseCount() {
        Integer i = count.getValue();
        int deduced = i.intValue() - 1;
        count.setValue(Integer.valueOf(deduced));
        return count;
    }
//    public List<MedicineCardViewModel> getMedicineList(){
//        return medicineLiveData.getValue();
//    }


    public void addToCart(int position, MedicineType medicineType) {
        MedicineCardViewModel item = null;
        switch(medicineType) {
            case dialysis:
                item = getDialysisList().get(position);
                item.addToCart();
                dialysisLiveData.postValue(dialysisList);
                break;
            case edible:
                item = getEdibleList().get(position);
                item.addToCart();
                edibleLiveData.postValue(edibleList);
                break;
            case needle:
                item = getNeedleList().get(position);
                item.addToCart();
                needleLiveData.postValue(needleList);
                break;
            case bandaid:
                item = getBandaidList().get(position);
                item.addToCart();
                bandaidLiveData.postValue(bandaidList);
                break;
        }
//        MedicineCardViewModel item = getMedicineList().get(position);
//        item.addToCart();
//        //medicineLiveData.postValue(medicineList);
//        medicineLiveData.postValue(edibleList);
    }

    public void removeFromCart(int position, MedicineType medicineType) {
        MedicineCardViewModel item = null;
        switch(medicineType) {
            case dialysis:
                item = getDialysisList().get(position);
                item.removeFromCart();
                dialysisLiveData.postValue(dialysisList);
                break;
            case edible:
                item = getEdibleList().get(position);
                item.removeFromCart();
                edibleLiveData.postValue(edibleList);
                break;
            case needle:
                item = getNeedleList().get(position);
                item.removeFromCart();
                needleLiveData.postValue(needleList);
                break;
            case bandaid:
                item = getBandaidList().get(position);
                item.removeFromCart();
                bandaidLiveData.postValue(bandaidList);
                break;
        }


//       MedicineCardViewModel item = medicineList.get(position);
//       item.removeFromCart();
//      // medicineLiveData.postValue(medicineList);
//       medicineLiveData.postValue(edibleList);
    }

    public void setCashPayment(int position, MedicineType medicineType) {
        MedicineCardViewModel item = null;
        switch(medicineType) {
            case dialysis:
                item = getDialysisList().get(position);
                item.setCashPayment(true);
                dialysisLiveData.postValue(dialysisList);
                break;
            case edible:
                item = getEdibleList().get(position);
                item.setCashPayment(true);
                edibleLiveData.postValue(edibleList);
                break;
            case needle:
                item = getNeedleList().get(position);
                item.setCashPayment(true);
                needleLiveData.postValue(needleList);
                break;
            case bandaid:
                item = getBandaidList().get(position);
                item.setCashPayment(true);
                bandaidLiveData.postValue(bandaidList);
                break;
        }

//        MedicineCardViewModel item = getMedicineList().get(position);
//        item.setCashPayment(true);
//        medicineLiveData.postValue(medicineList);
    }

    public void setQuantity(int position, MedicineType medicineType,  int quantity) {
        MedicineCardViewModel item = null;
        switch(medicineType) {
            case dialysis:
                item = getDialysisList().get(position);
                item.setQuantity(quantity);
                dialysisLiveData.postValue(dialysisList);
                break;
            case edible:
                item = getEdibleList().get(position);
                item.setQuantity(quantity);
                edibleLiveData.postValue(edibleList);
                break;
            case needle:
                item = getNeedleList().get(position);
                item.setQuantity(quantity);
                needleLiveData.postValue(needleList);
                break;
            case bandaid:
                item = getBandaidList().get(position);
                item.setQuantity(quantity);
                bandaidLiveData.postValue(bandaidList);
                break;
        }
    }

    public void setSliderPosition(int position, MedicineType medicineType, float sliderPosition) {
        MedicineCardViewModel item = null;
        switch(medicineType) {
            case dialysis:
                item = getDialysisList().get(position);
                item.setSliderPosition(sliderPosition);
                dialysisLiveData.postValue(dialysisList);
                break;
            case edible:
                item = getEdibleList().get(position);
                item.setSliderPosition(sliderPosition);
                edibleLiveData.postValue(edibleList);
                break;
            case needle:
                item = getNeedleList().get(position);
                item.setSliderPosition(sliderPosition);
                needleLiveData.postValue(needleList);
                break;
            case bandaid:
                item = getBandaidList().get(position);
                item.setSliderPosition(sliderPosition);
                bandaidLiveData.postValue(bandaidList);
                break;
        }
    }

   public void setCreditPayment(int position, MedicineType medicineType) {
       MedicineCardViewModel item = null;
       switch(medicineType) {
           case dialysis:
               item = getDialysisList().get(position);
               item.setCashPayment(false);
               dialysisLiveData.postValue(dialysisList);
               break;
           case edible:
               item = getEdibleList().get(position);
               item.setCashPayment(false);
               edibleLiveData.postValue(edibleList);
               break;
           case needle:
               item = getNeedleList().get(position);
               item.setCashPayment(false);
               needleLiveData.postValue(needleList);
               break;
           case bandaid:
               item = getBandaidList().get(position);
               item.setCashPayment(false);
               bandaidLiveData.postValue(bandaidList);
               break;
       }

//        MedicineCardViewModel item = getMedicineList().get(position);
//        item.setCashPayment(false);
//        medicineLiveData.postValue(medicineList);
    }

}
