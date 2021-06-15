package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RateChangeActivity extends AppCompatActivity {

    EditText dollarEditor;
    EditText euroEditor;
    EditText wonEditor;

    private static final String TAG = "RateChangeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_change);

        //接收数据
        Intent intent = getIntent();
        float dollar = intent.getFloatExtra("dollar_rate_key",0.0f);
        float euro = intent.getFloatExtra("euro_rate_key",0.0f);
        float won = intent.getFloatExtra("won_rate_key",0.0f);

        Log.i(TAG, "onCreate: dollar2=" + dollar);
        Log.i(TAG, "onCreate: euro2=" + euro);
        Log.i(TAG, "onCreate: won2=" + won);

        //获取控件
        dollarEditor = findViewById(R.id.edit_dollar);
        euroEditor = findViewById(R.id.edit_euro);
        wonEditor = findViewById(R.id.edit_won);

        //将汇率值放入控件
        dollarEditor.setText(String.valueOf(dollar));
        euroEditor.setText(String.valueOf(euro));
        wonEditor.setText(String.valueOf(won));
    }

    public void RateChange(View btn){
        //获得输入的数据
        float newDollar = Float.parseFloat(dollarEditor.getText().toString());
        float newEuro = Float.parseFloat(euroEditor.getText().toString());
        float newWon = Float.parseFloat(wonEditor.getText().toString());

        //将输入的新汇率带回计算页面
        Intent intent = getIntent();
        Bundle bdl = new Bundle();
        bdl.putFloat("key_dollar",newDollar);
        bdl.putFloat("key_euro",newEuro);
        bdl.putFloat("key_won",newWon);
        intent.putExtras(bdl);
        setResult(2,intent);//设置resultCode及带回的数据
        //返回到调用页面
        finish();


    }


}