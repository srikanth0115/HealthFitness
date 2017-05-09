package com.example.srikanthbandi.healthfit.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;
import com.dsi.ant.plugins.antplus.pcc.defines.DeviceState;
import com.dsi.ant.plugins.antplus.pcc.defines.EventFlag;
import com.dsi.ant.plugins.antplus.pcc.defines.RequestAccessResult;
import com.dsi.ant.plugins.antplus.pccbase.AntPluginPcc;
import com.dsi.ant.plugins.antplus.pccbase.AsyncScanController;
import com.dsi.ant.plugins.antplus.pccbase.PccReleaseHandle;
import com.example.srikanthbandi.healthfit.R;
import com.example.srikanthbandi.healthfit.utility.Constants;
import com.example.srikanthbandi.healthfit.utility.Utility;

import java.math.BigDecimal;
import java.util.EnumSet;

/**
 * Created by srikanthbandi on 04/05/17.
 */
public class WorkoutActivity extends AppCompatActivity{
    TextView mTxtHeartRate;
    private PccReleaseHandle<AntPlusHeartRatePcc> releaseHandle;
    private AntPlusHeartRatePcc hrPcc = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_activity);
        setUI();
        getHeartRate();
    }

    private void setUI() {
        mTxtHeartRate = (TextView)findViewById(R.id.mTxtHeartRate);
    }

    private void getHeartRate() {

        if (Utility.getSharedPrefBooleanData(this, Constants.IS_ANT_AVAILABLE)) {
            if (Constants.asyncScanResultDeviceInfo != null) {
                requestConnectToResult(Constants.asyncScanResultDeviceInfo);
            }
        }
    }

    /**
     * Requests access to the given search result.
     *
     * @param asyncScanResultDeviceInfo The search result to attempt to connect to.
     */
    protected void requestConnectToResult(final AsyncScanController.AsyncScanResultDeviceInfo asyncScanResultDeviceInfo) {
        //Inform the user we are connecting
        runOnUiThread(new Runnable() {
            public void run() {
                releaseHandle = Constants.hrScanCtrl.requestDeviceAccess(asyncScanResultDeviceInfo,
                        new AntPluginPcc.IPluginAccessResultReceiver<AntPlusHeartRatePcc>() {
                            @Override
                            public void onResultReceived(AntPlusHeartRatePcc result,
                                                         RequestAccessResult resultCode, DeviceState initialDeviceState) {
                                if (resultCode == RequestAccessResult.SEARCH_TIMEOUT) {
                                    //On a connection timeout the scan automatically resumes, so we inform the user, and go back to scanning
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(WorkoutActivity.this, "Timed out attempting to connect, try again", Toast.LENGTH_LONG).show();
                                            //mTextView_Status.setText("Scanning for heart rate devices asynchronously...");
                                        }
                                    });
                                } else {
                                    //Otherwise the results, including SUCCESS, behave the same as
                                    base_IPluginAccessResultReceiver.onResultReceived(result, resultCode, initialDeviceState);
                                    Constants.hrScanCtrl = null;
                                }
                            }
                        }, base_IDeviceStateChangeReceiver);
            }
        });
    }

    public void subscribeToHrEvents() {
        hrPcc.subscribeHeartRateDataEvent(new AntPlusHeartRatePcc.IHeartRateDataReceiver() {
            @Override
            public void onNewHeartRateData(final long estTimestamp, EnumSet<EventFlag> eventFlags,
                                           final int computedHeartRate, final long heartBeatCount,
                                           final BigDecimal heartBeatEventTime, final AntPlusHeartRatePcc.DataState dataState) {
                // Mark heart rate with asterisk if zero detected
                final String textHeartRate = String.valueOf(computedHeartRate)
                        + ((AntPlusHeartRatePcc.DataState.ZERO_DETECTED.equals(dataState)) ? "" : "");

                // Mark heart beat count and heart beat event time with asterisk if initial value
                final String textHeartBeatCount = String.valueOf(heartBeatCount)
                        + ((AntPlusHeartRatePcc.DataState.INITIAL_VALUE.equals(dataState)) ? "" : "");
                final String textHeartBeatEventTime = String.valueOf(heartBeatEventTime)
                        + ((AntPlusHeartRatePcc.DataState.INITIAL_VALUE.equals(dataState)) ? "" : "");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //tv_estTimestamp.setText(String.valueOf(estTimestamp));


                        //tv_heartBeatCounter.setText(textHeartBeatCount);
                        //tv_heartBeatEventTime.setText(textHeartBeatEventTime);

                        //tv_dataStatus.setText(dataState.toString());
                        mTxtHeartRate.setText(textHeartRate);
                        int int_hrm = Integer.parseInt(textHeartRate);

                    }
                });
            }
        });
    }

    protected AntPluginPcc.IPluginAccessResultReceiver<AntPlusHeartRatePcc> base_IPluginAccessResultReceiver =
            new AntPluginPcc.IPluginAccessResultReceiver<AntPlusHeartRatePcc>() {
                //Handle the result, connecting to events on success or reporting failure to user.
                @Override
                public void onResultReceived(AntPlusHeartRatePcc result, RequestAccessResult resultCode,
                                             DeviceState initialDeviceState) {
                    switch (resultCode) {
                        case SUCCESS:
                            Utility.showLog("SUCCESS", "SUCCESS");
                            hrPcc = result;
                            //tv_status.setText(result.getDeviceName() + ": " + initialDeviceState);
                            subscribeToHrEvents();
                            if (!result.supportsRssi()) mTxtHeartRate.setText("N/A");
                            break;
                        case CHANNEL_NOT_AVAILABLE:
                            Utility.showLog("CHANNEL_NOT_AVAILABLE", "CHANNEL_NOT_AVAILABLE");
                            Toast.makeText(WorkoutActivity.this, "Channel Not Available", Toast.LENGTH_SHORT).show();
                            // tv_status.setText("Error. Do Menu->Reset.");
                            break;
                        case ADAPTER_NOT_DETECTED:
                            Utility.showLog("ADAPTER_NOT_DETECTED", "ADAPTER_NOT_DETECTED");
                            Toast.makeText(WorkoutActivity.this, "ANT Adapter Not Available. Built-in ANT hardware or external adapter required.", Toast.LENGTH_SHORT).show();
                            //tv_status.setText("Error. Do Menu->Reset.");
                            break;
                        case BAD_PARAMS:
                            Utility.showLog("BAD_PARAMS", "BAD_PARAMS");
                            //Note: Since we compose all the params ourself, we should never see this result
                            Toast.makeText(WorkoutActivity.this, "Bad request parameters.", Toast.LENGTH_SHORT).show();
                            //tv_status.setText("Error. Do Menu->Reset.");
                            break;
                        case OTHER_FAILURE:
                            Utility.showLog("OTHER_FAILURE", "OTHER_FAILURE");
                            Toast.makeText(WorkoutActivity.this, "RequestAccess failed. See logcat for details.", Toast.LENGTH_SHORT).show();
                            //tv_status.setText("Error. Do Menu->Reset.");
                            break;
                        case DEPENDENCY_NOT_INSTALLED:
                            Utility.showLog("DEPENDENCY_NOT_INSTALLED", "DEPENDENCY_NOT_INSTALLED");
                            break;
                        case USER_CANCELLED:
                            Utility.showLog("USER_CANCELLED", "USER_CANCELLED");
                            // tv_status.setText("Cancelled. Do Menu->Reset.");
                            break;
                        case UNRECOGNIZED:
                            Utility.showLog("UNRECOGNIZED", "UNRECOGNIZED");
                            Toast.makeText(WorkoutActivity.this,
                                    "Failed: UNRECOGNIZED. PluginLib Upgrade Required?",
                                    Toast.LENGTH_SHORT).show();
                            //tv_status.setText("Error. Do Menu->Reset.");
                            break;
                        default:
                            Utility.showLog("default", "default");
                            Toast.makeText(WorkoutActivity.this, "Unrecognized result: " + resultCode, Toast.LENGTH_SHORT).show();
                            //tv_status.setText("Error. Do Menu->Reset.");
                            break;
                    }
                }
            };

    //Receives state changes and shows it on the status display line
    protected AntPluginPcc.IDeviceStateChangeReceiver base_IDeviceStateChangeReceiver =
            new AntPluginPcc.IDeviceStateChangeReceiver() {
                @Override
                public void onDeviceStateChange(final DeviceState newDeviceState) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // txt_hrm_count.setText(hrPcc.getDeviceName() + ": " + newDeviceState);
                        }
                    });
                }
            };

}

