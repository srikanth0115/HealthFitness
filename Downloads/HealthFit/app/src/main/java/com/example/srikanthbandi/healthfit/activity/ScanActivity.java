package com.example.srikanthbandi.healthfit.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
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

import com.example.srikanthbandi.healthfit.R;
import com.example.srikanthbandi.healthfit.adapter.BleDevicesAdapter;
import com.example.srikanthbandi.healthfit.utility.Constants;
import com.example.srikanthbandi.healthfit.utility.GPSTracker;
import com.example.srikanthbandi.healthfit.utility.Utility;
import com.rodolfonavalon.shaperipplelibrary.ShapeRipple;
import com.rodolfonavalon.shaperipplelibrary.model.Circle;

import java.util.UUID;


/**
 * Created by srikanthbandi on 02/05/17.
 */
public class ScanActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private ShapeRipple ripple;
    private ListView mListDevices;
    private BleDevicesAdapter leDeviceListAdapter;
    private BluetoothAdapter bluetoothAdapter;
    public Scanner scanner;
    private static final int PERMISSION_REQUEST_CODE = 1;
    private boolean isScanning = false;



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
                checkBluetooth();
            }
        } else {
            init();
            setupToolbar();
            checkBluetooth();
        }


       // initBluetooth();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    init();
                    setupToolbar();
                    checkBluetooth();

                } else {
                    Toast.makeText(this, "GPS permission allows us to access location data. Please allow access location data.", Toast.LENGTH_LONG).show();
                    requestPermission();
                }
                break;
        }
    }
    private void checkBluetooth() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            // startWorkoutActivity.finish();
            return;
        }
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            bluetoothAdapter = bluetoothManager.getAdapter();
        }
        // Checks if Bluetooth is supported on the device.
        if (bluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent;
            enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            int REQUEST_ENABLE_BT = 1;
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            return;
        }

        final GPSTracker gps = new GPSTracker(this);
        if (gps.canGetLocation()) {
            initBluetooth();
        } else {
            showSettingsAlert(ScanActivity.this);
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


    private void initBluetooth() {


      /*  final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                ScanActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        leDeviceListAdapter = null;
                    }
                });
            }
        }, 15000);
*/


        leDeviceListAdapter = new BleDevicesAdapter(ScanActivity.this);
        mListDevices.setAdapter(leDeviceListAdapter);
        scanner = new Scanner(bluetoothAdapter, mLeScanCallback);
        scanner.startScanning();

    }

    public class Scanner extends Thread {


        Scanner(BluetoothAdapter adapter, BluetoothAdapter.LeScanCallback callback) {
            bluetoothAdapter = adapter;
            mLeScanCallback = callback;
        }

        public boolean isScanning() {
            return isScanning;
        }

        void startScanning() {
            synchronized (this) {
               // Utility.showLog("Start Scanning", "Start Scanning");
                isScanning = true;
                start();
            }
        }

        void stopScanning() {
            synchronized (this) {
               // Utility.showLog("Stop Scanning", "Stop Scanning");
                isScanning = false;
                bluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    synchronized (this) {
                        if (!isScanning)
                            break;
                        //UUID[] uuids = {UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb")};
                        UUID[] uuidArray = {UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb")};
                        bluetoothAdapter.startLeScan(uuidArray, mLeScanCallback);
                        // bluetoothAdapter.startLeScan(mLeScanCallback);
                    }

                    sleep(100);

                    /*synchronized (this) {
                        bluetoothAdapter.stopLeScan(mLeScanCallback);
                    }*/
                }
            } catch (InterruptedException ignore) {
            } /*finally {
                bluetoothAdapter.stopLeScan(mLeScanCallback);
            }*/
        }

    }


    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(final BluetoothDevice device, final int rssi, byte[] scanRecord) {

                    leDeviceListAdapter.addDevice(device, rssi);
                    leDeviceListAdapter.notifyDataSetChanged();
                }
            };


    public void init() {
        ripple = (ShapeRipple) findViewById(R.id.ripple);
        ripple.setRippleShape(new Circle());
        ripple.setRippleColor(getResources().getColor(R.color.colorAccent));
        ripple.setRippleFromColor(getResources().getColor(R.color.colorAccent));
        ripple.setRippleInterval((float) 88 / 100F);
        ripple.setRippleDuration(3781);
        mListDevices = (ListView)findViewById(R.id.mListDevices);
        TextView mTxtScanning = (TextView)findViewById(R.id.mTxtScanning);
        mTxtScanning.setTypeface(Utility.setTypeFace_setTypeFace_SFTransRoboticsExtended(this));
        mListDevices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String address = leDeviceListAdapter.getDevice(position).getAddress();
                Intent intent = new Intent(ScanActivity.this,WorkoutBluetoothActivity.class);
                intent.putExtra("address",address);
                startActivity(intent);
            }
        });
    }

    private void setupToolbar() {

    }



}

