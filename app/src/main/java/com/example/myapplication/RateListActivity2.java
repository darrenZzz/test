package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RateListActivity2 extends AppCompatActivity implements Runnable{
    private static final String TAG = "RateListActivity2";
    Handler handler = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_list2);
        ListView listView = findViewById(R.id.mylist);

        List<String> list1 = new ArrayList<String>();
        for(int i = 1;i<100;i++){
            list1.add("item"+i);
        }
        Thread t = new Thread(this);
        t.start();
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg){
                if(msg.what == 9){
                    ArrayList<String> res = (ArrayList<String>)msg.obj;
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(RateListActivity2.this,
                            android.R.layout.simple_list_item_1,res);
                    listView.setAdapter(adapter);
                    Toast.makeText(RateListActivity2.this,"ret size="+ res.size(),Toast.LENGTH_SHORT).show();
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
            doc = Jsoup.connect("https://www.boc.cn/sourcedb/whpj/").get();
            Log.i(TAG,"run:title"+doc.title());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element table = doc.getElementsByTag("table").get(1);
        Elements trs =table.getElementsByTag("tr");
        for(Element tr : trs){
            Elements tds = tr.getElementsByTag("td");
            if (tds.size()>0){
                String kind = tds.first().text();
                String rate = tds.get(5).text();
                Log.i(TAG,"run:td="+kind);
                Log.i(TAG,"run: rate="+ rate);
                ret.add(kind+"---->"+rate);
            }
        }

        Message msg = handler.obtainMessage(9);
        msg.obj = ret;
        handler.sendMessage(msg);
    }
}