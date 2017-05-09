package com.example.srikanthbandi.healthfit.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.srikanthbandi.healthfit.R;
import com.example.srikanthbandi.healthfit.utility.ParticlesDrawable;
import com.example.srikanthbandi.healthfit.utility.Utility;

/**
 * Created by srikanthbandi on 04/05/17.
 */

public class SplashActivity extends AppCompatActivity {

    private ParticlesDrawable mDrawable;
    private ImageView mImgDraw;
    private TextView mTxtSplashName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        setContentView(R.layout.splash_activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mDrawable = (ParticlesDrawable) ContextCompat
                    .getDrawable(this, R.drawable.particles_customized);
        } else {
            mDrawable = new ParticlesDrawable();
        }
        mImgDraw = (ImageView) findViewById(R.id.mImgDraw);
        mImgDraw.setImageDrawable(mDrawable);
        mTxtSplashName = (TextView) findViewById(R.id.mTxtSplashName);
        mTxtSplashName.setTypeface(Utility.setTypeFace_setTypeFace_SFTransRoboticsExtended(this));
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDrawable.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDrawable.stop();
    }
}
