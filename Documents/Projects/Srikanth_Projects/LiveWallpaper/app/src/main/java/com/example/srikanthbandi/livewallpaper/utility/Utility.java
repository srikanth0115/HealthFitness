package com.example.srikanthbandi.livewallpaper.utility;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by srikanthbandi on 22/04/17.
 */
public class Utility {
    public static String getResourcesString(Context context, int id) {
        String value = null;
        if (context != null && id != -1) {
            value = context.getResources().getString(id);
        }
        return value;
    }
    public static Typeface setTypeFace_setTypeFace_proximanova_regular(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "SFTransRoboticsExtended.ttf");
    }

}
