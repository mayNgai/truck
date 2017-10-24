package com.dtc.sevice.truckclub.presenters.driver;

import android.util.Log;

import com.dtc.sevice.truckclub.R;
import com.dtc.sevice.truckclub.model.TblMember;
import com.dtc.sevice.truckclub.model.TblTask;
import com.dtc.sevice.truckclub.service.ApiService;
import com.dtc.sevice.truckclub.until.DialogController;
import com.dtc.sevice.truckclub.until.NetworkUtils;
import com.dtc.sevice.truckclub.view.driver.activity.DriverMainActivity2;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by may on 10/4/2017.
 */

public class DriverMainPresenter {
    private ApiService mForum;
    private DriverMainActivity2 mView;
    private DialogController dialogController;

    public DriverMainPresenter(DriverMainActivity2 view,ApiService forum){
        mForum = forum;
        mView = view;
        dialogController = new DialogController();
    }

    public void loadSchedulesDriver(){
        try {
            if(!NetworkUtils.isConnected(mView)){
                dialogController.dialogNolmal(mView,"Warning","Internet is not stable.");
            }else {
                mForum.getApi()
                        .getSchedulesDriver(mView.members.get(0))
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<List<TblTask>>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("loadSchedulesDriver Error", e.getMessage());
                            }

                            @Override
                            public void onNext(List<TblTask> tblTasks) {
                                mView.setUpdateListSchedulesTask(tblTasks);
                            }
                        });
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadTask(){
        try {
            if(!NetworkUtils.isConnected(mView)){
                dialogController.dialogNolmal(mView,"Warning","Internet is not stable.");
            }else {
                mForum.getApi()
                        .getTask(mView.tblTask)
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

    public void sentUpdateDriver(TblTask tblTask){
        try {
//            if(!NetworkUtils.isConnected(mView)){
//                dialogController.dialogNolmal(mView,"Wanning","Internet is not stable.");
//            }else {
            // dialog = ProgressDialog.show(mView, "Wait", "loading...");
            mForum.getApi()
                    .sentUpdateDriver(tblTask)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<TblTask>() {
                        @Override
                        public void onCompleted() {
                            //dialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("loadDataMember Error", e.getMessage());
                            // dialog.dismiss();
                        }

                        @Override
                        public void onNext(TblTask tblTasks) {
                            mView.setTitle();
                            //  dialog.dismiss();
                        }
                    });
//            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void sentUpdateLatlon(){
        try {
            mForum.getApi()
                    .sentUpdateLatLon(mView.members.get(0))
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
                        public void onNext(TblMember member) {


                        }
                    });
//            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadTaskByID(){
        try {
//            if(!NetworkUtils.isConnected(mView)){
//                dialogController.dialogNolmal(mView,mView.getString(R.string.txtWarning),mView.getString(R.string.txt_internet_is_not));
//            }else {
            TblTask tblTask = new TblTask();
            tblTask.setId(mView.members.get(0).getTask_id());
                mForum.getApi()
                        .getTaskByID(tblTask)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<TblTask>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("loadTaskByID Error", e.getMessage());
                            }

                            @Override
                            public void onNext(TblTask tblTasks) {
                                //setDialogBottom(tblTasks);
                                mView.tblTaskBooking = tblTasks;
                                mView.setTitle();
                                Log.i("loadTaskByID","Ok");

                            }
                        });
//            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
