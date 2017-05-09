package com.example.srikanthbandi.healthfit.activity;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.example.srikanthbandi.healthfit.R;
import com.example.srikanthbandi.healthfit.bluetooth.BleHeartRateSensor;
import com.example.srikanthbandi.healthfit.bluetooth.BleSensor;
import com.example.srikanthbandi.healthfit.bluetooth.BleSensors;
import com.example.srikanthbandi.healthfit.bluetooth.BleService;
import com.example.srikanthbandi.healthfit.bluetooth.BleServicesAdapter;
import com.example.srikanthbandi.healthfit.utility.Utility;

import java.util.List;

/**
 * Created by srikanthbandi on 05/05/17.
 */

public class WorkoutBluetoothActivity extends AppCompatActivity{
    private String mStrDeviceAddress;
    private Intent gattServiceIntent;
    public BleService bleService;
    public static boolean isConnected = false;
    private boolean isTimmerRunning = false;
    private long startTime = 0L;
    private Handler customHandler = new Handler();
    private long timeInMilliseconds = 0L;
    private long timeSwapBuff = 0L;
    public BleSensor<?> heartRateSensor;
    public BleServicesAdapter gattServiceAdapter;
    private boolean isWorkoutStartedYet = false;
    public int int_hrm = 0;
    private int mIntLogic = 0;
    private TextView mTxtHeartRate;
    private TextView mTxtTime;
    private boolean mIsRunning;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_bluetooth_activity);
        getIntentData();
        initUI();
        connectDevice();
        startTimer();
    }

    private void startTimer() {
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if(intent != null){
            mStrDeviceAddress = intent.getStringExtra("address");
        }
    }

    private void initUI() {
        mTxtTime = (TextView)findViewById(R.id.mTxtTime);
        mTxtHeartRate = (TextView)findViewById(R.id.mTxtHeartRate);

    }
    private void connectDevice() {
        gattServiceIntent = new Intent(WorkoutBluetoothActivity.this, BleService.class);
        bindService(gattServiceIntent, serviceConnection, BIND_AUTO_CREATE);
    }
    // Code to manage Service lifecycle.
    public ServiceConnection serviceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            bleService = ((BleService.LocalBinder) service).getService();
            if (!bleService.initialize()) {
                Utility.showLog("", "Unable to initialize Bluetooth");
            }
            bleService.connect(mStrDeviceAddress);
            registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter());
            if (bleService != null) {
                final boolean result = bleService.connect(mStrDeviceAddress);
                Utility.showLog("", "Connect request result=" + result);
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            bleService = null;
        }

    };

    public BroadcastReceiver gattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
//todo stop scanning after scanning


            if (BleService.ACTION_GATT_CONNECTED.equals(action)) {
                //Utility.showLog("ACTION_GATT_CONNECTED", "ACTION_GATT_CONNECTED");
                isConnected = true;
                Utility.showLog("ACTION_GATT_CONNECTED", "Called");
                /*displayData(intent.getStringExtra(BleService.EXTRA_SERVICE_UUID), intent.getStringExtra(BleService.EXTRA_TEXT));*/
            } else if (BleService.ACTION_GATT_DISCONNECTED.equals(action)) {

                isTimmerRunning = true;
                timeSwapBuff = timeInMilliseconds;
                customHandler.removeCallbacks(updateTimerThread);

                // Utility.showLog("ACTION_GATT_DISCONNECTED", "ACTION_GATT_DISCONNECTED");
                isConnected = false;
                // Toast.makeText(StartWorkoutActivity.this, "Device is disconnected", Toast.LENGTH_LONG).show();
                mTxtHeartRate.setText(getResources().getString(R.string.na));
                Utility.showLog("ACTION_GATT_DISCONNECTED", "Called");
                //bleService.disconnect();
                //bleService.stopService(gattServiceIntent);
                unbindService(serviceConnection);
                //gattServiceIntent = null;
                //Utility.showLog("bleService.stopService", "bleService.stopService");
                try {
                    Thread.sleep(500L);
                    heartRateSensor = null;
                    bleService.connect(mStrDeviceAddress);
                    //gattServiceIntent = new Intent(StartWorkoutActivity.this, BleService.class);
                    bindService(gattServiceIntent, serviceConnection, BIND_AUTO_CREATE);
                    /*bleService.connect(device_address);
                    if (bleService != null) {
                        final boolean result = bleService.connect(device_address);
                        Log.d("", "Connect request result=" + result);
                    }*/
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else if (BleService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                //Utility.showLog("ACTION_GATT_SERVICES_DISCOVERED", "ACTION_GATT_SERVICES_DISCOVERED");
                // Show all the supported services and characteristics on the user interface.
                Utility.showLog("ACTION_GATT_SERVICES_DISCOVERED", "Called");
                displayGattServices(bleService.getSupportedGattServices());
                enableHeartRateSensor();
            } else if (BleService.ACTION_DATA_AVAILABLE.equals(action)) {
                if (isTimmerRunning && isWorkoutStartedYet) {
                    isTimmerRunning = false;
                    timeSwapBuff = timeInMilliseconds;
                    startTime = SystemClock.uptimeMillis();
                    customHandler.postDelayed(updateTimerThread, 0);
                }

                displayData(intent.getStringExtra(BleService.EXTRA_SERVICE_UUID), intent.getStringExtra(BleService.EXTRA_TEXT));

            }
        }
    };
    public void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;

        gattServiceAdapter = new BleServicesAdapter(this, gattServices);
    }
    private Runnable updateTimerThread = new Runnable() {

        public void run() {
            isTimmerRunning = true;
            timeInMilliseconds = timeSwapBuff + SystemClock.uptimeMillis() - startTime;
            String str_time = "" + milliSecondsToTimer(timeInMilliseconds);
            mTxtTime.setText(str_time);
            customHandler.postDelayed(this, 0);
        }
    };

    public boolean enableHeartRateSensor() {
        if (gattServiceAdapter == null)
            return false;

        BluetoothGattCharacteristic characteristic = gattServiceAdapter
                .getHeartRateCharacteristic();
        //Log.d("", "characteristic: " + characteristic);
        if (characteristic != null) {
            BleSensor<?> sensor = BleSensors.getSensor(characteristic
                    .getService()
                    .getUuid()
                    .toString());
            if (heartRateSensor != null)
                bleService.enableSensor(heartRateSensor, false);

            if (sensor == null) {
                bleService.readCharacteristic(characteristic);
                return true;
            }

            if (sensor == heartRateSensor)
                return true;

            heartRateSensor = sensor;
            bleService.enableSensor(sensor, true);
        }

        return true;
    }


    public String milliSecondsToTimer(long milliseconds) {
        String finalTimerString;
        String secondsString;
        String minutesString;
        String hoursString;

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there

        if (hours < 10) {
            hoursString = "0" + hours;
        } else {
            hoursString = "" + hours;
        }

        // Prepending 0 to seconds if it is one digit
        if (minutes < 10) {
            minutesString = "0" + minutes;
        } else {
            minutesString = "" + minutes;
        }

        // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }

        finalTimerString = hoursString + ":" + minutesString + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (gattUpdateReceiver != null) {
            registerReceiver(gattUpdateReceiver, makeGattUpdateIntentFilter());
        }
        if (bleService != null) {
            bleService.connect(mStrDeviceAddress);
            // Log.d(TAG, "Connect request result=" + result);
        }
    }

    public IntentFilter makeGattUpdateIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BleService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BleService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BleService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BleService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
    public void displayData(String uuid, String data) {
        Utility.showLog("data :", " " + data);
        Utility.showLog("uuid :", "" + uuid);

        if (data != null) {
            if (uuid.equals(BleHeartRateSensor.getServiceUUIDString())) {
                Utility.showLog("step2 :", "Display data");
                String[] array_strings = data.split("=");
                float reading = Float.parseFloat(array_strings[1].replace("interval", ""));
                int hrm = (int) Math.ceil(reading);

                if (hrm > 0) {
                    int_hrm = hrm;
                  /*mHrList.add(int_hrm);*/
                    String str_count = "" + hrm;
                    mTxtHeartRate.setText(str_count);
                    //todo for % visibility
                }


            }
        } else {
            noValuesWhenConnected();
        }
    }

    private void noValuesWhenConnected() {
        isTimmerRunning = true;
        timeSwapBuff = timeInMilliseconds;
        customHandler.removeCallbacks(updateTimerThread);
        isConnected = false;
        // Toast.makeText(StartWorkoutActivity.this, "Device is disconnected", Toast.LENGTH_LONG).show();
        mTxtHeartRate.setText(getResources().getString(R.string.na));
        Utility.showLog("ACTION_GATT_DISCONNECTED", "Called");
        //bleService.disconnect();
        //bleService.stopService(gattServiceIntent);
        unbindService(serviceConnection);
        //gattServiceIntent = null;
        //Utility.showLog("bleService.stopService", "bleService.stopService");
        try {
            Thread.sleep(500L);
            heartRateSensor = null;
            bleService.connect(mStrDeviceAddress);
            //gattServiceIntent = new Intent(StartWorkoutActivity.this, BleService.class);
            bindService(gattServiceIntent, serviceConnection, BIND_AUTO_CREATE);
                    /*bleService.connect(device_address);
                    if (bleService != null) {
                        final boolean result = bleService.connect(device_address);
                        Log.d("", "Connect request result=" + result);
                    }*/
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mIsRunning) {
        }
        if (serviceConnection != null && bleService != null) {
            unbindService(serviceConnection);
            bleService = null;
            unregisterReceiver(gattUpdateReceiver);
        }

    }



}
