package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListActivity2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list2);
        ListView listview = findViewById(R.id.mylist2);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        Handler handler = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg){
                if(msg.what==9){
                    ArrayList<String> list2 = (ArrayList)msg.obj;
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListActivity2.this,
                            android.R.layout.simple_list_item_1,list2);
                    listview.setAdapter(adapter);

                    progressBar.setVisibility(View.GONE);
                    listview.setVisibility(View.VISIBLE);
                }
                super.handleMessage(msg);
                }
        };

        MyTask task = new MyTask();
        task.setHandler(handler);
        Thread t = new Thread(task);
        t.start();//task.run();
    }
}