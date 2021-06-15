package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RateActivity2 extends AppCompatActivity {

    TextView RateKind;
    EditText input;
    TextView result;
    Float value;

    private static final String TAG = "RateActivity2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate2);
        input = findViewById(R.id.rmb);
        result = findViewById(R.id.result);

        //接收数据
        Intent intent = getIntent();
        String RateStr = intent.getStringExtra("ItemTitle");
        String RateValue = intent.getStringExtra("ItemDetail");
        value = Float.valueOf(RateValue).floatValue();

        Log.i(TAG, "onCreate: ItemTitle=" + RateStr);
        Log.i(TAG, "onCreate: ItemDetail=" + RateValue);

        //获取控件
        RateKind = findViewById(R.id.RateKind);

        //将汇率值放入控件
        RateKind.setText(RateStr);

    }


    public void onClick(View btn){
        Log.d(TAG, "click: ");

        float r = 0.0f;
        r = 100f / value;
        //获得用户输入
        String str = input.getText().toString();
        Log.i(TAG,"click: str=" + str);
        if(str==null || str.length()==0){
            //提示
            Toast.makeText(this,"请输入金额",Toast.LENGTH_SHORT).show();
        }else{
            //进行计算
            float a = Float.valueOf(str).floatValue();
            Log.i(TAG, "click: a=" + a);
            Log.i(TAG, "click: r=" + r);

            a = a * r;
            result.setText(String.valueOf(a));
        }
    }
}