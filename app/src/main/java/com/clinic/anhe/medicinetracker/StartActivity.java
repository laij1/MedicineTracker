package com.clinic.anhe.medicinetracker;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.clinic.anhe.medicinetracker.model.EmployeeCardViewModel;
import com.clinic.anhe.medicinetracker.model.ShiftRecordModel;
import com.clinic.anhe.medicinetracker.networking.VolleyCallBack;
import com.clinic.anhe.medicinetracker.networking.VolleyController;
import com.clinic.anhe.medicinetracker.networking.VolleyStatus;
import com.clinic.anhe.medicinetracker.utils.GlobalVariable;
import com.clinic.anhe.medicinetracker.utils.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StartActivity extends AppCompatActivity {
    public static final String EXTRA_IPADDRESS= "ipaddress";
    public static final String EXTRA_PORT="port";
    private VolleyController volleyController;
    FloatingActionButton mFloatingActionButton;
    TextView mConnectStatus;
    EditText mIpaddress;
    EditText mPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();

        mFloatingActionButton = findViewById(R.id.connect);
        mConnectStatus = findViewById(R.id.connect_status);
        mIpaddress = findViewById(R.id.ipaddress);
        mPort = findViewById(R.id.port);


        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://" + mIpaddress.getText().toString() + ":"
                        + mPort.getText().toString() + "/anhe/employee/all";
                parseEmployeeData(url, new VolleyCallBack() {
                    @Override
                    public void onResult(VolleyStatus status) {
                        if(status==VolleyStatus.SUCCESS) {
                            mConnectStatus.setText("連線成功");
                            startMainActivity();
                        } else {
                            mConnectStatus.setText("連線失敗");
                        }
                    }
                });

//                startMainActivity();
            }
        });

    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        String ipaddress = mIpaddress.getText().toString();
        String port = mPort.getText().toString();

        GlobalVariable globalVariable = GlobalVariable.setInstance(getApplicationContext());
        globalVariable.setIpaddress(ipaddress);
        globalVariable.setPort(port);

        intent.putExtra(EXTRA_IPADDRESS, ipaddress);
        intent.putExtra(EXTRA_PORT,port);

//        Toast.makeText(getApplicationContext(),globalVariable.getIpaddress()+" /" + globalVariable.getPort(),Toast.LENGTH_LONG ).show();
        startActivity(intent);

    }

    private void prepareEmployeeData() {
        String url = "http://" + mIpaddress.getText().toString() + ":"
                + mPort.getText().toString() + "/anhe/employee/all";
        parseEmployeeData(url, new VolleyCallBack() {
            @Override
            public void onResult(VolleyStatus status) {
                if(status==VolleyStatus.SUCCESS) {

                }
            }
        });

    }

    private void parseEmployeeData(String url, final VolleyCallBack volleyCallBack) {
        JsonArrayRequest jsonArrayRequest =
                new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                volleyCallBack.onResult(VolleyStatus.SUCCESS);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                               // Log.d("VOLLEY", error.toString());
                                volleyCallBack.onResult(VolleyStatus.FAIL);
                            }
                        } );

        volleyController.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequest);

    }

}

