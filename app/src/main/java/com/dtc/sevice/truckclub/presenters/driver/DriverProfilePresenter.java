package com.dtc.sevice.truckclub.presenters.driver;

import android.util.Log;

import com.dtc.sevice.truckclub.model.TblCarDetail;
import com.dtc.sevice.truckclub.model.TblMember;
import com.dtc.sevice.truckclub.model.TblPicture;
import com.dtc.sevice.truckclub.service.ApiService;
import com.dtc.sevice.truckclub.until.DialogController;
import com.dtc.sevice.truckclub.until.NetworkUtils;
import com.dtc.sevice.truckclub.view.driver.activity.DriverProfileActivity;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by May on 10/9/2017.
 */

public class DriverProfilePresenter {
    private DriverProfileActivity mView;
    private ApiService mForum;
    private DialogController dialogController;
    public DriverProfilePresenter(DriverProfileActivity view , ApiService forum){
        mView = view;
        mForum = forum;
        dialogController = new DialogController();
    }

    public void loadCarDetail(){
        try {
            if(!NetworkUtils.isConnected(mView)){
                dialogController.dialogNolmal(mView,"Wanning","Internet is not stable.");
            }else {
                mForum.getApi()
                        .getCarDetail(mView.listMembers.get(0).getMember_id())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<TblCarDetail>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("loadCarDetail Error", e.getMessage());
                            }

                            @Override
                            public void onNext(TblCarDetail carDetail) {
                                mView.updateCarDetail(carDetail);
                            }
                        });
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadPictureCar(){
        try {
            mForum.getApi()
                    .getPictureCar(0)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<TblPicture>>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("loadPictureCar Error", e.getMessage());

                        }

                        @Override
                        public void onNext(List<TblPicture> pictures) {
                            //updateProfile(member);

                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
