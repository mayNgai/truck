package com.dtc.sevice.truckclub.presenters.user;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;

import com.dtc.sevice.truckclub.R;
import com.dtc.sevice.truckclub.model.TblMember;
import com.dtc.sevice.truckclub.model.TblTask;
import com.dtc.sevice.truckclub.service.ApiService;
import com.dtc.sevice.truckclub.until.ApplicationController;
import com.dtc.sevice.truckclub.until.DialogController;
import com.dtc.sevice.truckclub.until.NetworkUtils;
import com.dtc.sevice.truckclub.view.user.activity.UserMainActivity2;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by May on 9/28/2017.
 */

public class UserMainPresenter {
    private ProgressDialog dialog;
    private ApiService mForum;
    private DialogController dialogController;
    private UserMainActivity2 mView;
    private Activity activity;
    public UserMainPresenter(UserMainActivity2 view , ApiService forum){
        mForum = forum;
        mView = view;
        dialogController = new DialogController();
    }

    public void loadDriverInScope(){
        try {
            //dialog = ProgressDialog.show(mView, "Wait", "loading...");
            mForum.getApi().getDriverInScope(mView.members.get(0))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<TblMember>>() {
                        @Override
                        public void onCompleted() {
                            //dialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("loadDriverInScope Error", e.getMessage());
                            //dialog.dismiss();
                        }

                        @Override
                        public void onNext(List<TblMember> member) {

                            mView.updateMarkerDriverInScope(member);
                            //dialog.dismiss();
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void sentCreateTask(){
        try {
            if(!NetworkUtils.isConnected(mView)){
                dialogController.dialogNolmal(mView,mView.getString(R.string.txtWarning),mView.getString(R.string.txt_internet_is_not));
            }else {
                dialog = ProgressDialog.show(mView, mView.getString(R.string.txtWait), mView.getString(R.string.txt_loading));
                mForum.getApi()
                        .sentCreateTask(mView.tblTask)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<TblTask>() {
                            @Override
                            public void onCompleted() {
                                dialog.dismiss();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("loadDriverInScope Error", e.getMessage());
                                dialog.dismiss();
                            }

                            @Override
                            public void onNext(TblTask tblTask) {
                                mView.setDefault();
                                dialog.dismiss();
                                Log.i("sentCreateTask", "Ok");
                                if(tblTask.getService_type().equalsIgnoreCase("Now")){
                                    mView.setCountDownTime(tblTask);
                                }

                            }
                        });
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateMain(TblTask tblTask){
        try {
            if(!NetworkUtils.isConnected(mView)){
                dialogController.dialogNolmal(mView,mView.getString(R.string.txtWarning),mView.getString(R.string.txt_internet_is_not));
            }else {
                dialog = ProgressDialog.show(mView, "Wait", "loading...");
                mForum.getApi()
                        .sentUpdateTask(tblTask)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<TblTask>() {
                            @Override
                            public void onCompleted() {
                                dialog.dismiss();
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("updateMain Error", e.getMessage());
                                dialog.dismiss();
                            }

                            @Override
                            public void onNext(TblTask tblTasks) {
                                //mView.updateTask(tblTasks);
                                Log.i("updateMain", "OK");
                                dialog.dismiss();
                                activity = ApplicationController.getAppActivity();
                                Intent intent = new Intent(activity, UserMainActivity2.class);
                                activity.startActivity(intent);
                                activity.finish();

                            }
                        });
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
