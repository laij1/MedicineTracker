package com.clinic.anhe.medicinetracker;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import com.clinic.anhe.medicinetracker.utils.GlobalVariable;
import com.clinic.anhe.medicinetracker.utils.MainActivity;

public class StartActivity extends AppCompatActivity {
    public static final String EXTRA_IPADDRESS= "ipaddress";
    public static final String EXTRA_PORT="port";
    FloatingActionButton mFloatingActionButton;
    EditText mIpaddress;
    EditText mPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        getSupportActionBar().hide();

        mFloatingActionButton = findViewById(R.id.connect);


        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMainActivity();
            }
        });

    }

    private void startMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        mIpaddress = findViewById(R.id.ipaddress);
        mPort = findViewById(R.id.port);
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

}

