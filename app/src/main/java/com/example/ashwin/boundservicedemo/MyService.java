package com.example.ashwin.boundservicedemo;

import android.app.Service;
import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Random;

/**
 * Created by ashwin on 21/11/17.
 */

public class MyService extends Service {

    private static final String TAG = MyService.class.getSimpleName();
    private int mRandomNumber;
    private boolean mIsRandomNumberGeneratorOn;
    private final int MIN = 0;
    private final int MAX = 100;

    class MyServiceBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

    private IBinder mBinder = new MyServiceBinder();

    @Override
    public void onCreate() {
        Log.d(Constants.DEBUG_LOGGING, TAG + " : onCreate()");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(Constants.DEBUG_LOGGING, TAG + " : onStartCommand() : Thread : " + Thread.currentThread().getId());
        mIsRandomNumberGeneratorOn = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(Constants.DEBUG_LOGGING, TAG + " : onStartCommand() : run() : Thread : " + Thread.currentThread().getId());
                startRandomNumberGenerator();
            }
        }).start();
        return START_STICKY;
    }

    private void startRandomNumberGenerator() {
        while (mIsRandomNumberGeneratorOn) {
            try {
                Thread.sleep(2000);
                if (mIsRandomNumberGeneratorOn) {
                    mRandomNumber = new Random().nextInt(MAX) + MIN;
                    Log.d(Constants.DEBUG_LOGGING, TAG + " : startRandomNumberGenerator() : random number : " + mRandomNumber);
                }
            } catch (InterruptedException ie) {
                Log.d(Constants.DEBUG_LOGGING, TAG + " : startRandomNumberGenerator() : interrupted exception : " + ie.toString());
            }
        }
    }

    public int getRandomNumber() {
        return mRandomNumber;
    }

    private void stopRandomNumberGenerator() {
        mIsRandomNumberGeneratorOn = false;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(Constants.DEBUG_LOGGING, TAG + " : onBind()");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(Constants.DEBUG_LOGGING, TAG + " : onUnbind()");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d(Constants.DEBUG_LOGGING, TAG + " : onDestroy()");
        stopRandomNumberGenerator();
        super.onDestroy();
    }
}
