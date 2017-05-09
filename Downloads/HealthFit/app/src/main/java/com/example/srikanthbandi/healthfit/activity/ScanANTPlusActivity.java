package com.example.srikanthbandi.healthfit.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult;
import com.dsi.ant.plugins.antplus.pccbase.AsyncScanController;
import com.example.srikanthbandi.healthfit.R;
import com.example.srikanthbandi.healthfit.adapter.AntPlusAdapter;
import com.example.srikanthbandi.healthfit.adapter.BleDevicesAdapter;
import com.example.srikanthbandi.healthfit.utility.Constants;
import com.example.srikanthbandi.healthfit.utility.Utility;
import com.rodolfonavalon.shaperipplelibrary.ShapeRipple;
import com.rodolfonavalon.shaperipplelibrary.model.Circle;

import java.util.ArrayList;

/**
 * Created by srikanthbandi on 04/05/17.
 */
public class ScanANTPlusActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private ShapeRipple ripple;
    private ListView mListDevices;
    private BleDevicesAdapter leDeviceListAdapter;
    private BluetoothAdapter bluetoothAdapter;
    public ScanActivity.Scanner scanner;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private boolean isScanning = false;
    private ArrayList<AsyncScanController.AsyncScanResultDeviceInfo> mAlreadyConnectedDeviceInfos;
    private ArrayList<AsyncScanController.AsyncScanResultDeviceInfo> mScannedDeviceInfos;
    private AntPlusAdapter antPlusAdapter;
    private AsyncScanController.AsyncScanResultDeviceInfo asyncScanResultDeviceInfo;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scan_activity);
        if (Utility.isMarshmallowOS()) {
            if (!checkPermission()) {
                requestPermission();
            } else {
                init();
                setupToolbar();
                connectAntPlus();
            }
        } else {
            init();
            setupToolbar();
            connectAntPlus();
        }


        // initBluetooth();

    }

    private void connectAntPlus() {
        mAlreadyConnectedDeviceInfos = new ArrayList<AsyncScanController.AsyncScanResultDeviceInfo>();
        mScannedDeviceInfos = new ArrayList<AsyncScanController.AsyncScanResultDeviceInfo>();
        antPlusAdapter = new AntPlusAdapter(this);
        mListDevices.setAdapter(antPlusAdapter);
        //Release the old access if it exists
        requestAccessToPcc();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                    setupToolbar();


                } else {
                    Toast.makeText(this, "GPS permission allows us to access location data. Please allow access location data.", Toast.LENGTH_LONG).show();
                    requestPermission();
                }
                break;
        }
    }

    private void showSettingsAlert(final Activity startWorkoutActivity) {
        if (startWorkoutActivity != null) {
            android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(
                    startWorkoutActivity);
            alertDialog.setTitle("SETTINGS");
            alertDialog
                    .setMessage("Enable Location Provider! Go to settings menu?");
            alertDialog.setPositiveButton("Settings",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            Intent intent = new Intent(
                                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startWorkoutActivity.startActivityForResult(intent, Constants.REQUEST_ENABLE_LOCATION);
                        }
                    });
            alertDialog.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            alertDialog.show();
        }
    }


    public void init() {
        ripple = (ShapeRipple) findViewById(R.id.ripple);
        ripple.setRippleShape(new Circle());
        ripple.setRippleColor(getResources().getColor(R.color.colorAccent));
        ripple.setRippleFromColor(getResources().getColor(R.color.colorAccent));
        ripple.setRippleInterval((float) 88 / 100F);
        ripple.setRippleDuration(3781);
        mListDevices = (ListView) findViewById(R.id.mListDevices);
        TextView mTxtScanning = (TextView) findViewById(R.id.mTxtScanning);
        mTxtScanning.setTypeface(Utility.setTypeFace_setTypeFace_SFTransRoboticsExtended(this));
        mListDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Constants.asyncScanResultDeviceInfo = mScannedDeviceInfos.get(position);
                Intent intent = new Intent(ScanANTPlusActivity.this,WorkoutActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupToolbar() {

    }

    private void requestAccessToPcc() {
        Constants.hrScanCtrl = AntPlusHeartRatePcc.requestAsyncScanController(this, 0,
                new AsyncScanController.IAsyncScanResultReceiver() {
                    @Override
                    public void onSearchStopped(RequestAccessResult reasonStopped) {
                        //The triggers calling this function use the same codes and require the same actions as those received by the standard access result receiver
                        //base_IPluginAccessResultReceiver.onResultReceived(null, reasonStopped, DeviceState.DEAD);
                    }

                    @Override
                    public void onSearchResult(final AsyncScanController.AsyncScanResultDeviceInfo deviceFound) {
                        for (AsyncScanController.AsyncScanResultDeviceInfo i : mScannedDeviceInfos) {
                            //The current implementation of the async scan will reset it's ignore list every 30s,
                            //So we have to handle checking for duplicates in our list if we run longer than that
                            if (i.getAntDeviceNumber() == deviceFound.getAntDeviceNumber()) {
                                //Found already connected device, ignore
                                return;
                            }
                        }

                        //We split up devices already connected to the plugin from un-connected devices to make this information more visible to the user,
                        //since the user most likely wants to be aware of which device they are already using in another app
                        if (deviceFound.isAlreadyConnected()) {
                            mAlreadyConnectedDeviceInfos.add(deviceFound);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (antPlusAdapter.isEmpty())   //connected device category is invisible unless there are some present
                                    {

                                    }
                                    antPlusAdapter.addDevice(deviceFound.getDeviceDisplayName());
                                    antPlusAdapter.notifyDataSetChanged();
                                }
                            });
                        } else {
                            mScannedDeviceInfos.add(deviceFound);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    antPlusAdapter.addDevice(deviceFound.getDeviceDisplayName());
                                    antPlusAdapter.notifyDataSetChanged();
                                }
                            });
                        }
                    }
                });
    }

}

