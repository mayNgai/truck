package com.dtc.sevice.truckclub.broadcasts;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dtc.sevice.truckclub.view.user.activity.UserMainActivity2;

/**
 * Created by may on 10/30/2017.
 */

public class UserBackgroundService extends Service {
    public static CountDownTimer countDownTimer;
    private UserMainActivity2 mView;

    @Override
    public void onCreate() {
        super.onCreate();
        mView = new UserMainActivity2();
        countDownTimer = new CountDownTimer(mView.timeCountInMilliSeconds, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                try {
                    mView.txt_wait_time.setText(mView.hmsTimeFormatter(millisUntilFinished));
                    mView.progressBarCircle.setProgress(60 - (int) ((mView.timeCountInMilliSeconds - millisUntilFinished)/ (1000 * mView.tblTask2.getTime_wait())));
                    Log.i("wait_time",mView.hmsTimeFormatter(millisUntilFinished));
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onFinish() {
                Log.i("wait_time","Time out...");
                mView.setTimeOut();
                countDownTimer.cancel();
            }

        }.start();
        countDownTimer.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //copy_time=0;
//        Toast.makeText(User_Now_Service_Background.this, "Calling", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        countDownTimer.cancel();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
