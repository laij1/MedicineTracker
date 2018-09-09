package com.clinic.anhe.medicinetracker.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.model.MedicineRecordCardViewModel;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CashFlowViewModel extends ViewModel {


    private MutableLiveData<List<MedicineRecordCardViewModel>> todayListLiveData;
    private List<MedicineRecordCardViewModel> todayList;
    private MutableLiveData<List<MedicineRecordCardViewModel>> unchargedListLiveData;
    private List<MedicineRecordCardViewModel> unchargedList;
    private MutableLiveData<List<MedicineRecordCardViewModel>> searchListLiveData;
    private List<MedicineRecordCardViewModel> searchList;

    private MutableLiveData<Map<Integer, String>> patientMapLiveData;
    private Map<Integer,String> patientMap;

    private GlobalVariable globalVariable;
    private VolleyController volleyController;
    String url = "";


    public CashFlowViewModel() {
        todayListLiveData = new MutableLiveData<>();
        todayList = new ArrayList<>();
        todayListLiveData.setValue(todayList);

        unchargedListLiveData = new MutableLiveData<>();
        unchargedList = new ArrayList<>();
        unchargedListLiveData.setValue(unchargedList);

        searchListLiveData = new MutableLiveData<>();
        searchList = new ArrayList<>();
        searchListLiveData.setValue(searchList);

        patientMapLiveData = new MutableLiveData<>();
        patientMap = new HashMap<>();
        patientMapLiveData.postValue(patientMap);

//        url = "http://" + globalVariable.getInstance().getIpaddress() +
//                ":" + globalVariable.getInstance().getPort() + "/anhe/patient/all";
//
//        populatePatientMap(url, new VolleyCallBack() {
//            @Override
//            public void onResult(VolleyStatus status) {
//
//                patientMapLiveData.setValue(patientMap);
//            }
//        });

    }

    public MutableLiveData<List<MedicineRecordCardViewModel>> getTodayListLiveData() {
        return todayListLiveData;
    }

    public MutableLiveData<List<MedicineRecordCardViewModel>> getUnchargedListLiveData() {
        return unchargedListLiveData;
    }

    public MutableLiveData<List<MedicineRecordCardViewModel>> getSearchListLiveData() {
        return searchListLiveData;
    }

    public MutableLiveData<Map<Integer, String>> getPatientMapLiveData() {
        return patientMapLiveData;
    }

    public void populatePatientMap(String url, final VolleyCallBack volleyCallBack) {
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for(int i = 0; i < response.length(); i++){
                                    JSONObject object = null;
                                    try {
                                        object = response.getJSONObject(i);
                                        Integer pid = object.getInt("pid");
                                        String name = object.getString("name");
//                                        String shift = object.getString("shift");
//                                        String ic = object.getString("ic");
//                                        String day = object.getString("day");
//
                                        patientMap.put(new Integer(pid), name);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                     Log.d("getting patient data from database", "CashFlowViewModel");
                                    volleyCallBack.onResult(VolleyStatus.SUCCESS);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("VOLLEY", error.toString());
                                volleyCallBack.onResult(VolleyStatus.FAIL);
                            }
                        } );

        volleyController.getInstance().addToRequestQueue(jsonArrayRequest);
    }
}
