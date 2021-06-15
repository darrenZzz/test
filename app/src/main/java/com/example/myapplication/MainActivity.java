package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity{
    TextView out;
    EditText inp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        out = findViewById(R.id.textView2);
        inp = findViewById(R.id.inp);
    }

    public void btn(View v){
        String str = inp.getText().toString();
        if (str != null&& str.length()>0){
            Double a = Double.valueOf(str).doubleValue();
            a = 32 + a * 1.8;
            out.setText("结果为："+a+"华氏度");
        }else {
            out.setText("请输入你想转换的摄氏度");
        }
    }


}