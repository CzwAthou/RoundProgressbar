package com.athou.roundpb;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    RoundProgressBar progressBar;
    RoundProgressBar progressBar2;
    RoundProgressBar progressBar3;
    RoundProgressBar progressBar4;

    int count = 0;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (count >= progressBar.getMax()) {
                return;
            }
            count++;
            progressBar.setProgress(count);
            progressBar2.setProgress(count);
            progressBar4.setProgress(count);

            sendEmptyMessageDelayed(0x01, 300);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (RoundProgressBar) findViewById(R.id.duration_progress);
        progressBar2 = (RoundProgressBar) findViewById(R.id.duration_progress2);
        progressBar3 = (RoundProgressBar) findViewById(R.id.duration_progress3);
        progressBar4 = (RoundProgressBar) findViewById(R.id.duration_progress4);

        progressBar3.setCurProgress(50);

        mHandler.sendEmptyMessageDelayed(0x01, 300);
    }
}
