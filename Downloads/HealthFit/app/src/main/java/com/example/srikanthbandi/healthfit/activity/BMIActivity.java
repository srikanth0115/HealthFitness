package com.example.srikanthbandi.healthfit.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.srikanthbandi.healthfit.R;

/**
 * Created by srikanthbandi on 03/05/17.
 */
public class BMIActivity extends Activity {

    EditText mEdtHeight;
    EditText mEdtweight;
    Button mBtnCalculate;
    TextView mTxtResult;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bmi_activity);
        initUI();
mBtnCalculate.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        calculateBmi(Integer.parseInt(mEdtHeight.getText().toString()),Integer.parseInt(mEdtweight.getText().toString()));
    }
});

    }

private void initUI(){
    mBtnCalculate=(Button)findViewById(R.id.mBtnCalculate);
    mEdtHeight=(EditText)findViewById(R.id.mEdtHeight);
    mEdtweight=(EditText)findViewById(R.id.mEdtweight);
    mTxtResult=(TextView)findViewById(R.id.mTxtResult);
}
    public void calculateBmi(int height, int weight) {
        double bMI = weight * 703 / (height * height);
        String category = getCategory(bMI);
        String message = String.format("Your BMI is: %.1f (%s)", bMI, category);
        mTxtResult.setText(message);
    }

    private String getCategory(Double score) {
        if (score <= 15)
            return "Very severely underweight";
        if (score <= 16)
            return "Severely underweight";
        if (score <= 18.5)
            return "Underweight";
        if (score <= 25)
            return "Normal (healthy weight)";
        if (score <= 30)
            return "Overweight";
        if (score <= 35)
            return "Moderately obese";
        if (score <= 40)
            return "Severely obese";
        return "";
    }
}

