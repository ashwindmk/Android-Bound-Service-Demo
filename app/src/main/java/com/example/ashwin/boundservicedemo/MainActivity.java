package com.example.ashwin.boundservicedemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private MyService mMyService;
    private ServiceConnection mServiceConnection;
    private Intent mServiceIntent;
    private boolean mIsServiceBound = false;

    private TextView mRandomNumberTextView;
    private Button mStartButton, mBindButton, mGetRandomNumberButton, mUnbindButton, mStopButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mServiceIntent = new Intent(MainActivity.this, MyService.class);

        initViews();
    }

    private void initViews() {
        mRandomNumberTextView = (TextView) findViewById(R.id.randomNumberTextView);

        mStartButton = (Button) findViewById(R.id.startButton);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService();
            }
        });

        mBindButton = (Button) findViewById(R.id.bindButton);
        mBindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bindService();
            }
        });

        mGetRandomNumberButton = (Button) findViewById(R.id.getRandomNumberButton);
        mGetRandomNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRandomNumber();
            }
        });

        mUnbindButton = (Button) findViewById(R.id.unbindButton);
        mUnbindButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unbindService();
            }
        });

        mStopButton = (Button) findViewById(R.id.stopButton);
        mStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopService();
            }
        });
    }

    private void startService() {
        startService(mServiceIntent);
    }

    private void bindService() {
        if (mServiceConnection == null) {
            mServiceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder iBinder) {
                    MyService.MyServiceBinder myServiceBinder = (MyService.MyServiceBinder) iBinder;
                    mMyService = myServiceBinder.getService();
                    mIsServiceBound = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    mIsServiceBound = false;
                }
            };
        }

        bindService(mServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void getRandomNumber() {
        if (mIsServiceBound) {
            mRandomNumberTextView.setText("Random Number : " + mMyService.getRandomNumber());
        } else {
            mRandomNumberTextView.setText("Service Not Bound");
        }
    }

    private void unbindService() {
        if (mIsServiceBound) {
            unbindService(mServiceConnection);
            mIsServiceBound = false;
        }
    }

    private void stopService() {
        stopService(mServiceIntent);
    }

}
