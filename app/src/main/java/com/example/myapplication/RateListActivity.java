package com.example.myapplication;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RateListActivity extends ListActivity implements Runnable{

    private static final String TAG = "RateListActivity";
    Handler handler = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rate_list);

        List<String> list1 = new ArrayList<String>();
        for (int i = 1; i<100; i++){
            list1.add("item"+i);
        }
        String[] list_data = {"one", "two", "three", "four"};

        Thread t = new Thread(this);
        t.start();
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg){
                if(msg.what == 8){
                    ArrayList<String> res = (ArrayList<String>)msg.obj;
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(RateListActivity.this,
                            android.R.layout.simple_list_item_1,res);
                    setListAdapter(adapter);
                    Toast.makeText(RateListActivity.this,"ret size="+ res.size(),Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };
    }

    @Override
    public void run() {
        URL url = null;
        Document doc = null;
        final ArrayList<String> ret = new ArrayList<>();
        try {
            doc = Jsoup.connect("https://www.usd-cny.com/icbc.htm").get();
            Log.i(TAG,"run:title"+doc.title());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element table = doc.getElementsByTag("table").first();
        Elements trs =table.getElementsByTag("tr");
        //int j = 0;
        for(Element tr : trs){
            Elements tds = tr.getElementsByTag("td");
            if (tds.size()>0){
                String kind = tds.first().text();
                float rate = Float.valueOf(tds.get(3).text().toString());
                rate =
                        Log.i(TAG,"run:td="+kind);
                Log.i(TAG,"run: rate="+ rate);
                ret.add(kind+"---->"+rate);
            }
        }

        Message msg = handler.obtainMessage(8);
        msg.obj = ret;
        handler.sendMessage(msg);
    }

}