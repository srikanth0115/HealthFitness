package com.example.srikanthbandi.livewallpaper.services;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.service.wallpaper.WallpaperService;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.LinearLayout;

import com.example.srikanthbandi.livewallpaper.R;
import com.example.srikanthbandi.livewallpaper.utility.AnalogClock;
import com.example.srikanthbandi.livewallpaper.utility.Constants;

import java.util.Date;


public class ClockWallpaperService extends WallpaperService {
	public AnalogClock clock;

	@Override
	public Engine onCreateEngine() {
		return new ClockWallpaperEngine();
	}

	private class ClockWallpaperEngine extends Engine implements
			OnSharedPreferenceChangeListener {
		private final Handler handler = new Handler();
		private final Runnable drawRunner = new Runnable() {
			@Override
			public void run() {
				draw();
			}

		};

		private Paint paint;
		/** hands colors for hour, min, sec */
		private int[] colors = { 0xFFFF0000, 0xFF0000FF, 0xFFA2BC13 };
		private int bgColor;
		private int width;
		private int height;
		private boolean visible = true;
		private boolean displayHandSec;

		private SharedPreferences prefs;

		ClockWallpaperEngine() {
			prefs = PreferenceManager
					.getDefaultSharedPreferences(ClockWallpaperService.this);
			prefs.registerOnSharedPreferenceChangeListener(this);
			displayHandSec = prefs.getBoolean(
					Constants.DISPLAY_HAND_SEC_KEY, true);
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStyle(Paint.Style.STROKE);
			paint.setStrokeWidth(5);
			bgColor = Color.parseColor(Constants.color);
			clock = new AnalogClock(getApplicationContext());
			handler.post(drawRunner);
		}

		@Override
		public void onVisibilityChanged(boolean visible) {
			draw();

			this.visible = visible;
			if (visible) {
				handler.post(drawRunner);
			} else {
				handler.removeCallbacks(drawRunner);
			}
		}

		@Override
		public void onSurfaceDestroyed(SurfaceHolder holder) {
			super.onSurfaceDestroyed(holder);
			this.visible = false;
			handler.removeCallbacks(drawRunner);
			prefs.unregisterOnSharedPreferenceChangeListener(this);
		}

		@Override
		public void onSurfaceChanged(SurfaceHolder holder, int format,
				int width, int height) {
			this.width = width;
			this.height = height;
			super.onSurfaceChanged(holder, format, width, height);
		}

		private void draw() {
			SurfaceHolder holder = getSurfaceHolder();
			Canvas canvas = null;
			try {
				canvas = holder.lockCanvas();
				if (canvas != null) {
					draw(canvas);
				}
			}
			catch (Exception e){
				e.printStackTrace();

			}
			finally {
				if (canvas != null && holder != null)
					holder.unlockCanvasAndPost(canvas);
			}

			handler.removeCallbacks(drawRunner);

			if (visible) {
				handler.postDelayed(drawRunner, 200);
			}
		}

		private void draw(Canvas canvas) {
			if(Constants.image_dial == 0)
				Constants.image_dial = R.drawable.clock_three;
			bgColor = Color.parseColor(Constants.color);
			canvas.drawColor(bgColor);

			Bitmap mBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.rsz_img_one);

			//canvas.drawBitmap(mBitmap, 0, 0, null);
			clock.config(width / 2, height / 2, (int) (width * 0.6f),
					new Date(), paint, colors, displayHandSec,Constants.image_dial);
			clock.draw(canvas);
		}

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			if (Constants.DISPLAY_HAND_SEC_KEY.equals(key)) {
				displayHandSec = sharedPreferences.getBoolean(
						Constants.DISPLAY_HAND_SEC_KEY, true);
			}
		}

	}


}
