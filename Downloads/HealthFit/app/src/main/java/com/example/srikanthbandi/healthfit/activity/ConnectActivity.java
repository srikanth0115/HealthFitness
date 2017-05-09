package com.example.srikanthbandi.healthfit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.srikanthbandi.healthfit.R;
import com.example.srikanthbandi.healthfit.utility.Constants;
import com.example.srikanthbandi.healthfit.utility.Utility;

/**
 * Created by srikanthbandi on 04/05/17.
 */

public class ConnectActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connect_activity);
        checkAntService();
        setUI();

    }

    private void setUI() {
        Button mBtnBluetooth = (Button)findViewById(R.id.mBtnBluetooth);
        Button mBtnAntPlus = (Button)findViewById(R.id.mBtnAntPlus);
        mBtnBluetooth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectActivity.this, ScanActivity.class);
                startActivity(intent);
            }
        });
        mBtnAntPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ConnectActivity.this, ScanANTPlusActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkAntService() {
        boolean installed = Utility.appInstalledOrNot("com.dsi.ant.service.socket", this);
        if (installed) {
            Utility.setSharedPrefBooleanData(this, Constants.IS_ANT_AVAILABLE, installed);
        } else {
            Utility.setSharedPrefBooleanData(this, Constants.IS_ANT_AVAILABLE, installed);
        }
    }
}
