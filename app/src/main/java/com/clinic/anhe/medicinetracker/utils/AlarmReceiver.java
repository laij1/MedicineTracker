package com.clinic.anhe.medicinetracker.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.model.MedicineRecordCardViewModel;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class AlarmReceiver extends BroadcastReceiver {
    private VolleyController volleyController;
    private List<String> reminder = new ArrayList();
    private List<MedicineRecordCardViewModel> todayCashReminder = new ArrayList<>();
    private String result = "";
    private String resultToday = "";
    private String shift = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        result = "";
        shift = "";

        String arg = intent.getStringExtra("Reminder");
        if (arg.equalsIgnoreCase("firstReminder")) {
            shift = "早班";
        } else if (arg.equalsIgnoreCase("secondReminder")) {
            shift = "早班";
        } else if (arg.equalsIgnoreCase("thirdReminder")) {
            shift = "晚班";
        } else if (arg.equalsIgnoreCase("firstTodayCashReminder")) {
            shift = "";
        } else if (arg.equalsIgnoreCase("secondTodayCashReminder")) {
            shift = "";
        }

        if (!shift.equals("")) {
            getReminder(shift, new VolleyCallBack() {
                @Override
                public void onResult(VolleyStatus status) {
                    result = "";
                    if (status == VolleyStatus.SUCCESS) {
                        for (String s : reminder) {
                            result = result.concat(s).concat(" ");
                        }
                        if (!result.equalsIgnoreCase("")) {
                            NotificationHelper notificationHelper = new NotificationHelper(context);
                            NotificationCompat.Builder nb = notificationHelper.getChannel1Notification("提醒輸入病患自費項目", result + "尚未輸入");
                            notificationHelper.getNotificationManager().notify(1, nb.build());
                        }
                    }
                }
            });
        } else {
            //here to launch today cash's notification
           // Toast.makeText(context, "there reminding unpaid cash today", Toast.LENGTH_LONG).show();
            getTodayCashReminder(new VolleyCallBack() {
                @Override
                public void onResult(VolleyStatus status) {
                    resultToday ="";
                    if(status == VolleyStatus.SUCCESS) {
                        for (MedicineRecordCardViewModel r : todayCashReminder) {
                            resultToday = resultToday.concat(r.getPatientName()).concat(" ");
                        }
                        if (!resultToday.equalsIgnoreCase("")) {
                            NotificationHelper notificationHelper = new NotificationHelper(context);
                            NotificationCompat.Builder nb = notificationHelper.getChannel1Notification("提醒今日現金未結帳", resultToday+ "未結" );
                            notificationHelper.getNotificationManager().notify(2, nb.build());
                        }
                    }
                }
            });

        }

    }

    private void getReminder(String shift, final VolleyCallBack volleyCallBack) {
        reminder.removeAll(reminder);
        Log.d("we are in get reminder", "weee");
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String url = "http://" + GlobalVariable.getInstance().getIpaddress() +
                ":" + GlobalVariable.getInstance().getPort() + "/anho/shiftrecord/reminder?shift=" + shift + "&today=" + date;
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for(int i = 0; i < response.length(); i++) {
                                    String object = null;
                                    try {
                                        object = (String)response.get(i);
                                        Log.d("here is our object", object.toString());
                                        reminder.add(object.toString());
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

    private void getTodayCashReminder (final VolleyCallBack volleyCallBack) {
        todayCashReminder.removeAll(todayCashReminder);
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String url = "http://" + GlobalVariable.getInstance().getIpaddress() +
                ":" + GlobalVariable.getInstance().getPort() + "/anho/record/day" +
                "?today=" + date;
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                for(int i = 0; i < response.length(); i++) {
                                    JSONObject object = null;
                                    try {
                                        object = response.getJSONObject(i);

                                        String createAt = object.getString("createAt");
                                        Integer rid = object.getInt("rid");
                                        Integer pid = object.getInt("pid");
                                        Integer mid = object.getInt("mid");
                                        String name = object.getString("medicineName");
                                        String pName = object.getString("patientName");
                                        Integer quantity = object.getInt("quantity");
                                        Integer subtotal = object.getInt("subtotal");
                                        String createBy = object.getString("createBy");
                                        String payment = object.getString("payment");
                                        String chargeAt = object.getString("chargeAt");
                                        String chargeBy = object.getString("chargeBy");
                                        // Log.d("medicine record jason object" , name + pid + createAt);
                                        if(chargeBy.equalsIgnoreCase("null")){
                                            MedicineRecordCardViewModel item = new MedicineRecordCardViewModel(rid, createAt, mid, name, quantity,
                                                    subtotal, payment, pid, createBy);
                                            item.setChargeAt(chargeAt);
                                            item.setChargeBy(chargeBy);
                                            item.setPatientName(pName);
                                            todayCashReminder.add(item);
                                        }

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
