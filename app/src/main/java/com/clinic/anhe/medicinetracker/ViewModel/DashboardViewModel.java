package com.clinic.anhe.medicinetracker.ViewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.model.MedicineCardViewModel;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.model.ShiftRecordModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DashboardViewModel extends ViewModel {

//    private MutableLiveData<Map<String, List<String>>> dashboardLiveData;
//    private Map<String, List<String>> dashboardMap;

    private VolleyController volleyController;
    private GlobalVariable globalVariable;
    private MutableLiveData<List<String>> selectedPatientsLiveData;
    private List<String> patientList;
    private MutableLiveData<String> nurseLiveData;
    private MutableLiveData<List<ShiftRecordModel>> shiftRecordListLiveData;
    private List<ShiftRecordModel> shiftRecordList;


    public DashboardViewModel(){
//        dashboardLiveData = new MutableLiveData<>();
//        dashboardMap = new HashMap<>();

        selectedPatientsLiveData = new MutableLiveData<>();
        patientList = new ArrayList<>();
        selectedPatientsLiveData.setValue(patientList);

        nurseLiveData = new MutableLiveData<>();
        shiftRecordListLiveData = new MutableLiveData<>();
        shiftRecordList = new ArrayList<>();
        prepareShiftRecordData();
        //shiftRecordListLiveData.setValue(shiftRecordList);
//        dashboardMap.put("test",selectedPatientsLiveData.getValue());
//        dashboardLiveData.setValue(dashboardMap);
    }

//    public Map<String, List<String>> getDashboardMap() {
//        return dashboardLiveData.getValue();

//    }

    public MutableLiveData<String> getNurseLiveData(){ return nurseLiveData; }

    public List<String> getSelectedPatientsList(){
        return selectedPatientsLiveData.getValue();
    }

    public MutableLiveData<List<String>> getSelectedPatientsLiveData()  { return selectedPatientsLiveData; }

    public MutableLiveData<List<ShiftRecordModel>> getShiftRecordListLiveData(){ return shiftRecordListLiveData; }

    public List<ShiftRecordModel> getShiftRecordList() { return shiftRecordListLiveData.getValue(); }

//    public MutableLiveData<Map<String, List<String>>> getDashboardMapLiveData() {
//        return dashboardLiveData;
//    }


    private void prepareShiftRecordData() {
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String url = "http://" + globalVariable.getInstance().getIpaddress()
                + ":" + globalVariable.getInstance().getPort() + "/anho/shiftrecord?createAt=" + date;
        parseShiftRecordData(url, new VolleyCallBack() {
            @Override
            public void onResult(VolleyStatus status) {
                if(status==VolleyStatus.SUCCESS) {
                    shiftRecordListLiveData.postValue(shiftRecordList);

                }
            }
        });

    }

    private void parseShiftRecordData(String url, final VolleyCallBack volleyCallBack) {
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for(int i = 0; i < response.length(); i++) {
                                    JSONObject object = null;
                                    try {
                                        object = response.getJSONObject(i);
                                        String nurse = object.getString("nurse");
                                        Integer sid = object.getInt("sid");
                                        String patient = object.getString("patient");
                                        String shift = object.getString("shift");
                                        String day = object.getString("day");
                                        String createAt = object.getString("createAt");
                                        ShiftRecordModel s = new ShiftRecordModel(sid, createAt, nurse, patient,shift, day);
                                        if(! shiftRecordList.contains(s)) {
                                            shiftRecordList.add(s);
                                        }
                                        Log.d("getting shift record", nurse + patient);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                volleyCallBack.onResult(VolleyStatus.SUCCESS);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("VOLLEY", error.toString());
                                volleyCallBack.onResult(VolleyStatus.FAIL);
                            }
                        } ){/**
                 * Passing some request headers
                 */
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = "admin1:secret1";
                    String auth = "Basic "
                            + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }};

        volleyController.getInstance().addToRequestQueue(jsonArrayRequest);

    }
}
