package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FirstActivity extends AppCompatActivity {

    TextView out;
    EditText inp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        out = (TextView)findViewById(R.id.showText);
        inp = (EditText)findViewById(R.id.inpText);

        Button btn = (Button)findViewById(R.id.btn1);
    }

    public void btnClick(View v){
        Log.i("click","onClick ……");
        String str = inp.getText().toString();

        out.setText("Hello "+str);
    }

    public void openTemp(View btn){
        //打开温度转换页面MainActivity
        Log.i("open", "openTemp: ");
        Intent hello = new Intent(this,MainActivity.class);
        startActivity(hello);
    }

    public void openRate(View btn){
        //打开汇率计算页面RateActivity
        Log.i("open", "openRate: ");
        Intent hello = new Intent(this,RateActivity.class);
        startActivity(hello);
    }

    public void openScore(View btn){
        //打开汇率计算页面RateActivity
        Log.i("open", "openScore: ");
        Intent hello = new Intent(this,ScoreActivity.class);
        startActivity(hello);
    }

    public void openJD(View btn){
        //打开汇率计算页面RateActivity
        Log.i("open", "openJD: ");
        Intent web = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.jd.com"));
        startActivity(web);
    }
}