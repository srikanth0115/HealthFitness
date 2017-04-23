package com.example.srikanthbandi.livewallpaper.activities;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.srikanthbandi.livewallpaper.R;
import com.example.srikanthbandi.livewallpaper.services.ClockWallpaperService;
import com.example.srikanthbandi.livewallpaper.utility.Utility;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;

import org.jsoup.Jsoup;

public class MainActivity extends AppCompatActivity {

    private String str_current_version;
    private String str_playstore_version;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StartAppSDK.init(this, "203399951", true);
        StartAppAd.disableSplash();
        setContentView(R.layout.activity_main);
        setUI();
        playStoreversionChecker();

    }
    private void playStoreversionChecker() {
        try {
            str_current_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        new GetVersionCode().execute();
    }
    private void setUI() {
        Button mBtnWallPaper = (Button)findViewById(R.id.mBtnWallPaper);
        mBtnWallPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        WallpaperManager.ACTION_CHANGE_LIVE_WALLPAPER);
                intent.putExtra(WallpaperManager.EXTRA_LIVE_WALLPAPER_COMPONENT,
                        new ComponentName(MainActivity.this, ClockWallpaperService.class));
                startActivity(intent);
                finish();
            }
        });
        Button mBtnShare = (Button)findViewById(R.id.mBtnShare);
        mBtnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "https://play.google.com/store/apps/details?id=com.erb&hl=en";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "ERetailBox");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        Button mBtnMoreWallPaper = (Button)findViewById(R.id.mBtnMoreWallPaper);
        mBtnMoreWallPaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "https://play.google.com/store/apps/details?id=com.erb&hl=en";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "ERetailBox");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
    }


    private class GetVersionCode extends AsyncTask<Void, String, String> {
        //  private CustomProgressDialog pdia;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(Void... voids) {

            str_playstore_version = null;
            String appPackageName = getPackageName();
            try {
                str_playstore_version = Jsoup.connect("https://play.google.com/store/apps/details?id=" + appPackageName + "&hl=it")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div[itemprop=softwareVersion]")
                        .first()
                        .ownText();
                return str_playstore_version;
            } catch (Exception e) {

                return str_playstore_version;
            }
        }

        @Override
        protected void onPostExecute(String onlineVersion) {
            super.onPostExecute(onlineVersion);

            if (onlineVersion != null && !onlineVersion.isEmpty()) {
                if (!str_current_version.equalsIgnoreCase(onlineVersion)) {
                    // Utility.showToastMessage(DashBoardActivity.this,"New version Available");
                    displayVersionAlert();
                }
            }
        }
    }

    private void displayVersionAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set title
        alertDialogBuilder.setTitle("Update");

        // set dialog message
        alertDialogBuilder
                .setMessage(Utility.getResourcesString(this, R.string.update_version))
                .setCancelable(false)
                .setPositiveButton("Update Now", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        final String appPackageName = getPackageName();
                        try {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                        } catch (android.content.ActivityNotFoundException anfe) {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                        }
                        finish();
                    }
                })
                .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }




}
