package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RateActivity extends AppCompatActivity implements Runnable{

    private static final String TAG = "RateActivity";
    EditText input;
    TextView result;
    float dollarRate = 0.1528f, euroRate = 0.1284f, wonRate = 171.9927f;
    Handler handler;
    String nowtime,runtime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);
        input = findViewById(R.id.rmb);
        result = findViewById(R.id.result);

        //获取当前时间
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-dd");//可以方便地修改日期格式
        nowtime = dateFormat.format( now );
        Log.i(TAG, "run: nowtime=" + nowtime);

        //获取SharedPreferences对象
        SharedPreferences sharedPreferences = getSharedPreferences("myrate", Activity.MODE_PRIVATE);

        dollarRate = sharedPreferences.getFloat("dollar_rate",0.0f);
        euroRate = sharedPreferences.getFloat("euro_rate",0.0f);
        wonRate = sharedPreferences.getFloat("won_rate",0.0f);

        Thread t = new Thread(this);
        t.start();

        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg){
                if(msg.what == 7){
                    String str = (String)msg.obj;
                    Log.i(TAG, "handleMessage: get str= " + str);
                    result.setText(str);
                }
                super.handleMessage(msg);
            }
        };
    }

    public void onClick(View btn){
        Log.d(TAG, "click: ");

        float r = 0.0f;
        switch (btn.getId()){
            case R.id.btn_dollar:
                r = dollarRate;
                break;
            case R.id.btn_euro:
                r = euroRate;
                break;
            case R.id.btn_won:
                r = wonRate;
                break;
        }
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

    public void openRateChange(View btn){
        //打开汇率配置页面RateChangeActivity
        Log.i("open", "openRateChange: ");
        open();
    }

    private void open() {
        Intent hello = new Intent(this, RateChangeActivity.class);
        //加入传递的参数
        hello.putExtra("dollar_rate_key",dollarRate);
        hello.putExtra("euro_rate_key",euroRate);
        hello.putExtra("won_rate_key",wonRate);

        Log.i(TAG, "openRateChange: dollarRate=" + dollarRate);
        Log.i(TAG, "openRateChange: euroRate=" + euroRate);
        Log.i(TAG, "openRateChange: wonRate=" + wonRate);

        startActivityForResult(hello,5);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==5 && resultCode==2){
            Bundle bundle = data.getExtras();
            dollarRate = bundle.getFloat("key_dollar",0.0f);
            euroRate = bundle.getFloat("key_euro",0.0f);
            wonRate = bundle.getFloat("key_won",0.0f);

            Log.i(TAG, "onActivityResult: dollarRate=" + dollarRate);
            Log.i(TAG, "onActivityResult: euroRate=" + euroRate);
            Log.i(TAG, "onActivityResult: womRate=" + wonRate);


            //修改保存内容
            SharedPreferences sp = getSharedPreferences("myrate", Activity.MODE_PRIVATE);

            SharedPreferences.Editor editor = sp.edit();
            editor.putFloat("dollar_rate",dollarRate);
            editor.putFloat("euro_rate",euroRate);
            editor.putFloat("won_rate",wonRate);
            editor.apply();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.mymenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item){
        if (item.getItemId()==R.id.menu_setting){
            open();
        }else if (item.getItemId()==R.id.open_list){
            Intent list = new Intent(this,ListActivity3.class);
            startActivity(list);
        }
        ;
        return super.onOptionsItemSelected(item);

    }

    private String inputStream2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer =new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream,"utf-8");
        while (true){
            int rsz = in.read(buffer, 0, buffer.length);
            if(rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

    @Override
    public void run() {
        Log.i(TAG, "run: ……");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //线程中完成的任务

        //获取网络数据
        URL url = null;
        try {
            /*
            Log.i(TAG, "run: 1");
            url = new URL("https://www.boc.cn/sourcedb/whpj/");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            Log.i(TAG, "run: 22222");
            InputStream in = http.getInputStream();
            String html = inputStream2String(in);
            Log.i(TAG, "run: html=" + html);

             */
            Document doc = Jsoup.connect("https://www.usd-cny.com/bankofchina.htm").get();
            Log.i(TAG, "run: title=" + doc.title());
            //获取时间
            //body > section > div > div > article > p

            Element publicTime = doc.getElementsByClass("time").first();
            Log.i(TAG, "run: time=" + publicTime.html());

            //用正则表达式获取进入汇率网页的时间
            Pattern p= Pattern.compile("(\\d{4})-(\\d{1,2})-(\\d{1,2})");
            Matcher m=p.matcher(publicTime.text());
            if(m.find())
                runtime = m.group();
            Log.i(TAG, "run: runtime=" + runtime);



            Element table = doc.getElementsByTag("table").first();
            Elements trs = table.getElementsByTag("tr");
            for(Element tr : trs){
                Elements tds = tr.getElementsByTag("td");
                if(tds.size()>0){
                    Log.i(TAG, "run: td=" + tds.first().text());
                    Log.i(TAG, "run: rate=" + tds.get(5).text());
                    float rate = Float.parseFloat(tds.get(5).text().toString());
                    if(nowtime.equals(runtime)==false) {  //当前时间和进入网页的日期不一样时，更新汇率
                        if ("美元".equals(tds.first().text())){
                            dollarRate = 100f / rate;
                            Log.i(TAG, "run: dollarRate= " + dollarRate);
                        }
                        else if ("欧元".equals(tds.first().text())){
                            euroRate = 100f / rate;
                            Log.i(TAG, "run: euroRate= " + euroRate);
                        }
                        else if ("韩元".equals(tds.first().text()))
                            wonRate = 100f / rate;
                            Log.i(TAG, "run: wonRate= " + wonRate);
                        }
                    }
                }


        } catch (MalformedURLException e) {
            Log.e(TAG, "run: ex=" + e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "run: ex=" + e.toString());
            e.printStackTrace();
        }


        //返回数据给主线程

        Message msg = handler.obtainMessage(7);
        msg.obj = "from message";
        handler.sendMessage(msg);
    }
}