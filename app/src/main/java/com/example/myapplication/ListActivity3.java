package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class ListActivity3 extends ListActivity implements AdapterView.OnItemClickListener,Runnable, AdapterView.OnItemLongClickListener {

    private static final String TAG = "ListActivity3";
    Handler handler = null;
    private ArrayList<HashMap<String,String>> listItems;
    MyAdapter adapter;
    ArrayList<HashMap<String,String>> res;

    private String logDate = "";
    private final String DATE_SP_KEY = "lastRateDateStr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_list3);
        //ListView list3 = findViewById(R.id.mylist3);

        SharedPreferences sp = getSharedPreferences("myrate", Context.MODE_PRIVATE);
        logDate = sp.getString(DATE_SP_KEY, "");
        Log.i("List","lastRateDateStr=" + logDate);

        /*
        //data
        listItems = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 50; i++){
            HashMap<String,String> map = new HashMap<String, String>();
            map.put("ItemTitle", "Rate:  " + i);//标题文字
            map.put("ItemDetail", "detail" + i);//详情描述
            listItems.add(map);
        }

        //adapter
        SimpleAdapter listItemAdapter = new SimpleAdapter(this,
                listItems,//listItems 数据源
                R.layout.list_item,//ListItem 的XML布局实现
                new String[]{"ItemTitle", "ItemDetail"},
                new int[]{R.id.itemTitle, R.id.itemDetail });

        MyAdapter adapter = new MyAdapter(this,R.layout.list_item,listItems);
        //list3.setAdapter(listItemAdapter);
        //getListView().setAdapter(listItemAdapter);
        setListAdapter(adapter);

         */

        Thread t = new Thread(this);
        t.start();
        handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg){
                if(msg.what == 8){
                    res = (ArrayList<HashMap<String,String>>)msg.obj;
                    adapter = new MyAdapter(ListActivity3.this,
                            android.R.layout.simple_list_item_1,res);
                    setListAdapter(adapter);
                }
                super.handleMessage(msg);
            }
        };
        getListView().setOnItemClickListener(this);
        getListView().setOnItemLongClickListener(this);
    }

    @Override
    public void run() {
        URL url = null;
        Document doc = null;
        final ArrayList<HashMap<String,String>> ret = new ArrayList<>();
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
                String str = tds.first().text();
                String rate = tds.get(3).text();
                Log.i(TAG,"run:td="+str);
                Log.i(TAG,"run: rate="+ rate);
                HashMap<String,String> map = new HashMap<String, String>();
                map.put("ItemTitle", str);//标题文字
                map.put("ItemDetail", rate);//详情描述
                ret.add(map);
            }
        }

        Message msg = handler.obtainMessage(8);
        msg.obj = ret;
        handler.sendMessage(msg);
    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object itemAtPosition = getListView().getItemAtPosition(position);
        HashMap<String,String> map = (HashMap<String,String>) itemAtPosition;
        String titleStr = map.get("ItemTitle");
        String detailStr = map.get("ItemDetail");
        Log.i(TAG, "onItemClick: titleStr=" + titleStr);
        Log.i(TAG, "onItemClick: detailStr=" + detailStr);


        TextView title = (TextView)view.findViewById(R.id.itemTitle);
        TextView detail = (TextView)view.findViewById(R.id.itemDetail);
        String title2 = String.valueOf(title.getText());
        String detail2 = String.valueOf(detail.getText());
        Log.i(TAG, "onItemClick: title2= " + title2);
        Log.i(TAG, "onItemClick: detail2= " + detail2);


        //打开汇率转换页面RateActivity2
        Log.i("open", "openRateActivity2: ");
        Intent hello = new Intent(this, RateActivity2.class);
        //加入传递的参数
        hello.putExtra("ItemTitle",titleStr);
        hello.putExtra("ItemDetail",detailStr);

        Log.i(TAG, "openRateActivity2: ItemTitle=" + titleStr);
        Log.i(TAG, "openRateActivity2: ItemDetail=" + detailStr);

        startActivityForResult(hello,5);



    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i(TAG, "onItemLongClick: 长按事件处理");
        //对话框提示
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("请确认是否删除当前数据")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i(TAG, "onClick: 对话框事件处理");
                        //删除操作
                        res.remove(position);
                        //更新适配器
                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("否",null);
        builder.create().show();
        return true;
    }
}