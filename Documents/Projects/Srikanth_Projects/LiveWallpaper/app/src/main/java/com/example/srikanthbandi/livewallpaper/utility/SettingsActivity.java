package com.example.srikanthbandi.livewallpaper.utility;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.srikanthbandi.livewallpaper.R;

import java.util.ArrayList;
import java.util.List;


public class SettingsActivity extends AppCompatActivity {

    private LinearLayout mLlyColorPallete;
    private LinearLayout mLlyDialPicker_image;
    private LinearLayout mLlyDialPicker;
    private String hexColor;
    private List<Integer> array_list_dials = new ArrayList<>();
    private List<Integer> array_list_dials_images = new ArrayList<>();
    private GradientView mTop;
    private GradientView mBottom;
    private TextView mTextView;
    private Drawable mIcon;
    private LinearLayout mLlyColorPalletePreview;
    private ImageView mImgPreview;

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

        array_list_dials_images.add(R.drawable.clock_one);
        array_list_dials_images.add(R.drawable.clock_two);
        array_list_dials_images.add(R.drawable.clock_three);
        array_list_dials_images.add(R.drawable.clock_four);
        array_list_dials_images.add(R.drawable.clock_five);
        array_list_dials_images.add(R.drawable.clock_six);
        array_list_dials_images.add(R.drawable.clock_seven);
        array_list_dials_images.add(R.drawable.clock_eight);
        array_list_dials_images.add(R.drawable.clock_nine);
        array_list_dials_images.add(R.drawable.clock_ten);

        mLlyColorPallete = (LinearLayout) findViewById(R.id.mLlyColorPallete);
        mImgPreview = (ImageView)findViewById(R.id.mImgPreview);
        mLlyColorPalletePreview = (LinearLayout) findViewById(R.id.mLlyColorPalletePreview);
        mLlyDialPicker = (LinearLayout) findViewById(R.id.mLlyDialPicker);
        mLlyDialPicker_image = (LinearLayout) findViewById(R.id.mLlyDialPicker_image);
        Button mBtnOkay = (Button) findViewById(R.id.mBtnOkay);
        mBtnOkay.setTypeface(Utility.setTypeFace_setTypeFace_proximanova_regular(this));
        for (int i = 0; i < array_list_dials.size(); i++) {
            final RelativeLayout layout = (RelativeLayout) getLayoutInflater().inflate(R.layout.single_image, null);
            ImageView mImgDial = (ImageView) layout.findViewById(R.id.mImgDial);
            final ImageView mImgCheck = (ImageView) layout.findViewById(R.id.mImgCheck);
            mImgDial.setImageResource(array_list_dials.get(i));
            layout.setId(i);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = v.getId();
                    Constants.image_dial = array_list_dials.get(pos);
                    mImgPreview.setImageResource(Constants.image_dial);

                    for(int k=0; k<array_list_dials.size();k++){
                        if(pos == k){
                            mImgCheck.setImageResource(R.drawable.check);
                        }
                        else {
                            mImgCheck.setImageResource(0);
                        }
                    }
                }
            });
            mLlyDialPicker.addView(layout);
        }

        for (int i = 0; i < array_list_dials_images.size(); i++) {
            final RelativeLayout layout = (RelativeLayout) getLayoutInflater().inflate(R.layout.single_image, null);
            ImageView mImgDial = (ImageView) layout.findViewById(R.id.mImgDial);
            final ImageView mImgCheck = (ImageView) layout.findViewById(R.id.mImgCheck);
            mImgDial.setImageResource(array_list_dials_images.get(i));
            layout.setId(i);
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = v.getId();
                    Constants.image_dial = array_list_dials_images.get(pos);
                    mImgPreview.setImageResource(Constants.image_dial);
                    for(int k=0; k<array_list_dials_images.size();k++){
                        if(pos == k){
                            mImgCheck.setImageResource(R.drawable.check);
                        }
                        else {
                            mImgCheck.setImageResource(0);
                        }
                    }
                }
            });
            mLlyDialPicker_image.addView(layout);
        }
        mBtnOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.color = hexColor;
                finish();


            }
        });

        mIcon = getResources().getDrawable(R.mipmap.ic_launcher);
        mTextView = (TextView)findViewById(R.id.color);
        mTextView.setCompoundDrawablesWithIntrinsicBounds(mIcon, null, null, null);
        mTop = (GradientView)findViewById(R.id.top);
        mBottom = (GradientView)findViewById(R.id.bottom);
        mTop.setBrightnessGradientView(mBottom);
        mBottom.setOnColorChangedListener(new GradientView.OnColorChangedListener() {
            @Override
            public void onColorChanged(GradientView view, int color) {
                mTextView.setTextColor(color);
                mTextView.setText("#" + Integer.toHexString(color));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mIcon.setTint(color);
                }
                hexColor = String.format("#%06X", (0xFFFFFF & color));
                Constants.color = hexColor;
                mLlyColorPallete.setBackgroundColor(color);
                mLlyColorPalletePreview.setBackgroundColor(color);
            }
        });

        int color = 0xFF394572;
        mTop.setColor(color);



        /*colorPickerView.setColorListener(new ColorPickerView.ColorListener() {
            @Override
            public void onColorSelected(int color) {
                hexColor = String.format("#%06X", (0xFFFFFF & color));
                Constants.color = hexColor;
                mLlyColorPallete.setBackgroundColor(color);
                //Toast.makeText(getApplicationContext(), "Color Code: " + hexColor, Toast.LENGTH_LONG).show();
            }
        });*/
    }
}