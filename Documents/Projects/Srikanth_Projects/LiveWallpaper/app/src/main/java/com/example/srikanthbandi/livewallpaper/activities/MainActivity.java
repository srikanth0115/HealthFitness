package com.example.srikanthbandi.livewallpaper.activities;

import android.app.WallpaperManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.srikanthbandi.livewallpaper.R;
import com.example.srikanthbandi.livewallpaper.services.ClockWallpaperService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    }
}
