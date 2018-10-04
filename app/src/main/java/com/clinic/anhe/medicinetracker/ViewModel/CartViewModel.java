package com.clinic.anhe.medicinetracker.ViewModel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.model.EmployeeCardViewModel;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;

import java.util.ArrayList;
import java.util.List;

import com.clinic.anhe.medicinetracker.R;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;
import com.clinic.anhe.medicinetracker.utils.MedicineType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CartViewModel extends ViewModel {


//    private Context context;


    private MutableLiveData<List<MedicineCardViewModel>> dialysisLiveData;
    List<MedicineCardViewModel> dialysisList;

    private MutableLiveData<List<MedicineCardViewModel>> edibleLiveData;
    List<MedicineCardViewModel> edibleList;

    private MutableLiveData<List<MedicineCardViewModel>> needleLiveData;
    List<MedicineCardViewModel> needleList;

    private MutableLiveData<List<MedicineCardViewModel>> bandaidLiveData;
    List<MedicineCardViewModel> bandaidList;

    private MutableLiveData<PatientsCardViewModel> cartSelectedPatientLiveData;

    private MutableLiveData<Integer> cartSelectedEid;

    //for fluidslider
    private MutableLiveData<Integer> count;

    //for payment alert Dialog
    private MutableLiveData<Boolean> paymentAlert;

    //for quantity alert Dialog
    private MutableLiveData<Boolean> quantityAlert;

    private GlobalVariable globalVariable;


    public CartViewModel(){
        initMedicineList();
        initCount();
    }

    public void initCount() {
        count = new MutableLiveData<>();
        Integer i = Integer.valueOf(0);
        count.setValue(i);
    }

    public void initMedicineList() {
        dialysisLiveData = new MutableLiveData<>();
        dialysisList = new ArrayList<>();

        edibleLiveData = new MutableLiveData<>();
        edibleList = new ArrayList<>();

        needleLiveData = new MutableLiveData<>();
        needleList = new ArrayList<>();

        bandaidLiveData = new MutableLiveData<>();
        bandaidList = new ArrayList<>();

        cartSelectedPatientLiveData = new MutableLiveData<>();
        PatientsCardViewModel p = new PatientsCardViewModel(-1, "","","", "");
        cartSelectedPatientLiveData.postValue(p);

        cartSelectedEid = new MutableLiveData<>();


        String url = "http://"+ globalVariable.getInstance().getIpaddress() +":" +
                globalVariable.getInstance().getPort() + "/anho/medicine/all/";
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for(int i = 0; i < response.length(); i++){
                                    JSONObject object = null;
                                    try {
                                        object = response.getJSONObject(i);
                                        String category = object.getString("category");
                                        String name = object.getString("name");
                                        Integer id = object.getInt("mid");
                                        Integer price = object.getInt("price");
                                        String dose = object.getString("dose");
                                        Integer stock = object.getInt("stock");
                                        //Log.d("jason object" , name + id +price +dose + stock);
                                        switch (category) {
                                            case "dialysis":
                                                dialysisList.add(new MedicineCardViewModel(id, name, Integer.toString(price), dose, stock, category));
                                                break;
                                            case "edible":
                                                edibleList.add(new MedicineCardViewModel(id, name, Integer.toString(price), dose, stock, category));
                                                break;
                                            case "needle":
                                                needleList.add(new MedicineCardViewModel(id, name, Integer.toString(price), dose, stock, category));
                                                break;
                                            case "bandaid":
                                                bandaidList.add(new MedicineCardViewModel(id, name, Integer.toString(price), dose, stock, category));
                                                break;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    //Log.d("getting data from database", "CHLOE");

                                }
                                dialysisLiveData.postValue(dialysisList);
                                edibleLiveData.postValue(edibleList);
                                needleLiveData.postValue(needleList);
                                bandaidLiveData.postValue(bandaidList);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                               // Log.d("VOLLEY", error.toString());
                            }
                        } );

        VolleyController.getInstance().addToRequestQueue(jsonArrayRequest);
        dialysisLiveData.setValue(this.dialysisList);
        edibleLiveData.setValue(this.edibleList);
        needleLiveData.setValue(this.needleList);
        bandaidLiveData.setValue(this.bandaidList);
    }



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

    public MutableLiveData<PatientsCardViewModel> getCartSelectedPatientLiveData() {
        return cartSelectedPatientLiveData;
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


    public MutableLiveData<Integer> getCartSelectedEid() {
        return cartSelectedEid;
    }

    public void addToCart(int position, MedicineType medicineType) {
        MedicineCardViewModel item = null;
        switch(medicineType) {
            case dialysis:
                item = getDialysisList().get(position);
                item.calculateSubtotal();
                item.addToCart();
                dialysisLiveData.postValue(dialysisList);
                break;
            case edible:
                item = getEdibleList().get(position);
                item.calculateSubtotal();
                item.addToCart();
                edibleLiveData.postValue(edibleList);
                break;
            case needle:
                item = getNeedleList().get(position);
                item.calculateSubtotal();
                item.addToCart();
                needleLiveData.postValue(needleList);
                break;
            case bandaid:
                item = getBandaidList().get(position);
                item.calculateSubtotal();
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
                item.calculateSubtotal();
                dialysisLiveData.postValue(dialysisList);
                break;
            case edible:
                item = getEdibleList().get(position);
                item.setQuantity(quantity);
                item.calculateSubtotal();
                edibleLiveData.postValue(edibleList);
                break;
            case needle:
                item = getNeedleList().get(position);
                item.setQuantity(quantity);
                item.calculateSubtotal();
                needleLiveData.postValue(needleList);
                break;
            case bandaid:
                item = getBandaidList().get(position);
                item.setQuantity(quantity);
                item.calculateSubtotal();
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

//    public void setContext(Context context) {
//        this.context = context;
//    }
}
