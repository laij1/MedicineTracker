package com.clinic.anhe.medicinetracker.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.model.PatientsCardViewModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.List;
import java.util.ArrayList;

public class AlarmReceiver extends BroadcastReceiver {
    private VolleyController volleyController;
    private List<String> reminder = new ArrayList();
    private String result = "";

    @Override
    public void onReceive(Context context, Intent intent) {
        getReminder("早班", new VolleyCallBack() {
            @Override
            public void onResult(VolleyStatus status) {
                if(status == VolleyStatus.SUCCESS) {
                    for(String s : reminder) {
                        result = result.concat(s).concat(" ");
                    }
//                    Toast.makeText(context, result,Toast.LENGTH_LONG).show();
                    NotificationHelper notificationHelper = new NotificationHelper(context);
                    NotificationCompat.Builder nb = notificationHelper.getChannel1Notification("提醒輸入病患自費項目", result + "尚未輸入");
                    notificationHelper.getNotificationManager().notify(1, nb.build());
                }
            }
        });


    }

    private void getReminder(String shift, final VolleyCallBack volleyCallBack) {
        Log.d("we are in get reminder", "weee");
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String url = "http://" + GlobalVariable.getInstance().getIpaddress() +
                ":" + GlobalVariable.getInstance().getPort() + "/anhe/shiftrecord/reminder?shift=" + shift + "&today=" + date;
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
                        } );

        volleyController.getInstance().addToRequestQueue(jsonArrayRequest);

    }



}
