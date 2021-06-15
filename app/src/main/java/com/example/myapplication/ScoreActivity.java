package com.example.myapplication;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class ScoreActivity extends AppCompatActivity {

    private static final String TAG = "ScoreIndicatorActivity";
    int scorea_int,scoreb_int=0;
    TextView scorea,scoreb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        scorea = findViewById(R.id.scorea);
        scoreb = findViewById(R.id.scoreb);
    }
    void show(){
        scorea.setText(String.valueOf(scorea_int));
        scoreb.setText(String.valueOf(scoreb_int));
        Log.i(TAG,"scorea="+scorea_int);
        Log.i(TAG,"scoreb="+scoreb_int);
    }
    public void btna(View v){
        if(v.getId() == R.id.btna1){
            scorea_int += 1;
        }else if(v.getId() == R.id.btna2){
            scorea_int += 2;
        }else{
            scorea_int += 3;
        }
        show();
        Log.i(TAG,"btna:");
    }
    public void btnb(View v){
        if(v.getId() == R.id.btnb1){
            scoreb_int += 1;
        }else if(v.getId() == R.id.btnb2){
            scoreb_int += 2;
        }else{
            scoreb_int += 3;
        }
        show();
        Log.i(TAG,"btnb:");
    }
    public void reset(View v){
        scorea_int = 0;
        scoreb_int = 0;
        show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        //String scorea = ((TextView)findViewById(R.id.scorea)).getText().toString();
        //String scoreb = ((TextView)findViewById(R.id.scoreb)).getText().toString();

        Log.i(TAG, "onSaveInstanceState:  ");
        outState.putInt("teama_score",scorea_int);
        outState.putInt("teamb_score",scoreb_int);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int scorea = savedInstanceState.getInt("teama_score");
        int scoreb = savedInstanceState.getInt("teamb_score");

        Log.i(TAG, "onRestoreInstanceState:  ");
        scorea_int = scorea;
        scoreb_int = scoreb;
        ((TextView)findViewById(R.id.scorea)).setText(String.valueOf(scorea));
        ((TextView)findViewById(R.id.scoreb)).setText(String.valueOf(scoreb));
    }
}
