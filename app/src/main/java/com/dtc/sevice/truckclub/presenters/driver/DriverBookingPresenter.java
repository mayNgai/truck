package com.dtc.sevice.truckclub.presenters.driver;

import android.util.Log;

import com.dtc.sevice.truckclub.model.TblMember;
import com.dtc.sevice.truckclub.model.TblTask;
import com.dtc.sevice.truckclub.service.ApiService;
import com.dtc.sevice.truckclub.until.DialogController;
import com.dtc.sevice.truckclub.until.NetworkUtils;
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
    private DialogController dialogController;

    public DriverBookingPresenter(DriverBookingActivity view,ApiService forum){
        mForum = forum;
        mView = view;
        dialogController = new DialogController();
    }

    public void loadTask(String type){
        try {
            if(!NetworkUtils.isConnected(mView)){
                dialogController.dialogNolmal(mView,"Wanning","Internet is not stable.");
            }else {
                mForum.getApi()
                        .getTask("Booking",type,mView.members.get(0).getAuthority(),mView.members.get(0).getMember_id())
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
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadDataMember(){
        try {
            if(!NetworkUtils.isConnected(mView)){
                dialogController.dialogNolmal(mView,"Wanning","Internet is not stable.");
            }else {
                mForum.getApi()
                        .getDataMember(mView.members.get(0).getMember_id(),mView.members.get(0).getAuthority())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<TblMember>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("loadDataMember Error", e.getMessage());
                            }

                            @Override
                            public void onNext(TblMember tblTasks) {
                                //mView.setListTask(tblTasks);
                            }
                        });
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
