package com.dtc.sevice.truckclub.presenters.driver;

import android.util.Log;

import com.dtc.sevice.truckclub.model.TblTask;
import com.dtc.sevice.truckclub.service.ApiService;
import com.dtc.sevice.truckclub.view.driver.activity.DriverBookingActivity;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by May on 10/10/2017.
 */

public class DriverBookingPresenter {
    private ApiService mForum;
    private DriverBookingActivity mView;

    public DriverBookingPresenter(DriverBookingActivity view,ApiService forum){
        mForum = forum;
        mView = view;
    }

    public void loadTask(){
        try {
            mForum.getApi()
                    .getTask("Booking")
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<TblTask>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("loagTask Error", e.getMessage());
                        }

                        @Override
                        public void onNext(List<TblTask> tblTasks) {
                            mView.setListTask(tblTasks);
                        }
                    });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
