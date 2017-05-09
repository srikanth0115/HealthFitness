package com.example.srikanthbandi.healthfit.utility;

import com.dsi.ant.plugins.antplus.pcc.AntPlusHeartRatePcc;
import com.dsi.ant.plugins.antplus.pccbase.AsyncScanController;

/**
 * Created by srikanthbandi on 02/05/17.
 */
public class Constants {
    public static int REQUEST_ENABLE_LOCATION = 100;
    public static AsyncScanController.AsyncScanResultDeviceInfo asyncScanResultDeviceInfo;
    public static final boolean logMessageOnOrOff = true;
    public static AsyncScanController<AntPlusHeartRatePcc> hrScanCtrl;
    public static final String APP_PREF = "HealthFit_Pref";
    public static final String IS_ANT_AVAILABLE = "is_ant_available";
}
