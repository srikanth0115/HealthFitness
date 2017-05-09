package com.example.srikanthbandi.healthfit.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.srikanthbandi.healthfit.R;
import com.example.srikanthbandi.healthfit.utility.Utility;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private Button mBtnStartWorkout;
    private LinearLayout mLlyPace;
    private LinearLayout mLlyBMI;
    private LinearLayout mLlyAgeCalsy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setUI();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initUI() {
        TextView mTxtOne = (TextView) findViewById(R.id.mTxtOne);
        mTxtOne.setTypeface(Utility.setTypeFace_setTypeFace_SFTransRoboticsExtended(this));
        TextView mTxtTwo = (TextView) findViewById(R.id.mTxtTwo);
        mTxtTwo.setTypeface(Utility.setTypeFace_setTypeFace_SFTransRoboticsExtended(this));
        TextView mTxtThree = (TextView) findViewById(R.id.mTxtThree);
        mTxtThree.setTypeface(Utility.setTypeFace_setTypeFace_SFTransRoboticsExtended(this));
        TextView mTxtFour = (TextView) findViewById(R.id.mTxtFour);
        mTxtFour.setTypeface(Utility.setTypeFace_setTypeFace_SFTransRoboticsExtended(this));
        TextView mTxtFive = (TextView) findViewById(R.id.mTxtFive);
        mTxtFive.setTypeface(Utility.setTypeFace_setTypeFace_SFTransRoboticsExtended(this));
        TextView mTxtSix = (TextView) findViewById(R.id.mTxtSix);
        mTxtSix.setTypeface(Utility.setTypeFace_setTypeFace_SFTransRoboticsExtended(this));
        TextView mTxtstartWorkout = (TextView) findViewById(R.id.mTxtstartWorkout);
        mTxtstartWorkout.setTypeface(Utility.setTypeFace_setTypeFace_SFTransRoboticsExtended(this));
        TextView mTxtseven = (TextView) findViewById(R.id.mTxtseven);
        mTxtseven.setTypeface(Utility.setTypeFace_setTypeFace_SFTransRoboticsExtended(this));
        TextView mTxtEight = (TextView) findViewById(R.id.mTxtEight);
        mTxtEight.setTypeface(Utility.setTypeFace_setTypeFace_SFTransRoboticsExtended(this));
        LinearLayout mLlyBluetooth = (LinearLayout) findViewById(R.id.mLlyBluetooth);
        mLlyBluetooth.setOnClickListener(this);
        LinearLayout mLlyAntPlus = (LinearLayout) findViewById(R.id.mLlyAntPlus);
        mLlyAntPlus.setOnClickListener(this);
        LinearLayout mLlyAgeCalsy = (LinearLayout) findViewById(R.id.mLlyAgeCalsy);
        mLlyAgeCalsy.setOnClickListener(this);
    }

    private void setUI() {
        mBtnStartWorkout = (Button) findViewById(R.id.mBtnStartWorkout);
        mBtnStartWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ConnectActivity.class);
                startActivity(intent);
            }
        });
        mLlyPace = (LinearLayout) findViewById(R.id.mLlyPace);
        mLlyBMI = (LinearLayout) findViewById(R.id.mLlyBMI);
        mLlyPace.setOnClickListener(this);
        mLlyBMI.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mLlyPace:
                Intent intent = new Intent(MainActivity.this, TrainingOverviewActivity.class);
                startActivity(intent);
                break;
            case R.id.mLlyBMI:
                Intent intent_bmi = new Intent(MainActivity.this, BMIActivity.class);
                startActivity(intent_bmi);
                break;
            case R.id.mLlyBluetooth:
                Intent intent_bluetooth = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(intent_bluetooth);
                break;
            case R.id.mLlyAntPlus:
                Intent intent_ant = new Intent(MainActivity.this, ScanANTPlusActivity.class);
                startActivity(intent_ant);
                break;
            case R.id.mLlyAgeCalsy:
                Intent intent_age_calsy = new Intent(MainActivity.this, AgeCalsyActivity.class);
                startActivity(intent_age_calsy);
                break;
        }

    }
}
