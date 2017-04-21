package com.example.srikanthbandi.livewallpaper.utility;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.srikanthbandi.livewallpaper.R;
import com.skydoves.colorpickerview.ColorPickerView;

import java.util.ArrayList;
import java.util.List;


public class SettingsActivity extends AppCompatActivity {

    private LinearLayout mLlyColorPallete;
    private LinearLayout mLlyDialPicker;
    private String hexColor;
    private List<Integer> array_list_dials = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        array_list_dials.add(R.drawable.rsz_img_one);
        array_list_dials.add(R.drawable.rsz_img_two);
        array_list_dials.add(R.drawable.rsz_img_three);
        array_list_dials.add(R.drawable.rsz_img_four);
        array_list_dials.add(R.drawable.rsz_img_five);
        array_list_dials.add(R.drawable.rsz_img_six);
        array_list_dials.add(R.drawable.rsz_img_seven);
        array_list_dials.add(R.drawable.rsz_img_eight);
        ColorPickerView colorPickerView = (ColorPickerView) findViewById(R.id.colorPickerView);
        mLlyColorPallete = (LinearLayout) findViewById(R.id.mLlyColorPallete);
        mLlyDialPicker = (LinearLayout) findViewById(R.id.mLlyDialPicker);
        Button mBtnOkay = (Button) findViewById(R.id.mBtnOkay);
        for (int i = 0; i < array_list_dials.size(); i++) {
            LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.single_image, null);
            ImageView mImgDial = (ImageView) layout.findViewById(R.id.mImgDial);
            mImgDial.setImageResource(array_list_dials.get(i));
            layout.setId(i);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = v.getId();
                    Constants.image_dial = array_list_dials.get(pos);
                }
            });
            mLlyDialPicker.addView(layout);
        }
        mBtnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.color = hexColor;
                finish();


            }
        });
        colorPickerView.setColorListener(new ColorPickerView.ColorListener() {
            @Override
            public void onColorSelected(int color) {
                hexColor = String.format("#%06X", (0xFFFFFF & color));
                Constants.color = hexColor;
                mLlyColorPallete.setBackgroundColor(color);
                //Toast.makeText(getApplicationContext(), "Color Code: " + hexColor, Toast.LENGTH_LONG).show();
            }
        });
    }
}