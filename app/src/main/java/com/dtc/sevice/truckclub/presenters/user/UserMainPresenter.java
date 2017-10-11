package com.dtc.sevice.truckclub.presenters.user;

import android.app.ProgressDialog;
import android.util.Log;

import com.dtc.sevice.truckclub.model.TblMember;
import com.dtc.sevice.truckclub.model.TblProvince;
import com.dtc.sevice.truckclub.model.TblTask;
import com.dtc.sevice.truckclub.service.ApiService;
import com.dtc.sevice.truckclub.until.DialogController;
import com.dtc.sevice.truckclub.until.NetworkUtils;
import com.dtc.sevice.truckclub.view.user.activity.UserMainActivity;
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
    public UserMainPresenter(UserMainActivity2 view , ApiService forum){
        mForum = forum;
        mView = view;
        dialogController = new DialogController();
    }

    public void loadDriverInScope(){
        try {
            //dialog = ProgressDialog.show(mView, "Wait", "loading...");
            mForum.getApi().getDriverInScope(mView.members.get(0).getMember_id(),mView.members.get(0).getLat(),mView.members.get(0).getLon(),mView.members.get(0).getRadius())
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


    public void sentCrarteTask(){
        try {
            if(!NetworkUtils.isConnected(mView)){
                dialogController.dialogNolmal(mView,"Wanning","Internet is not stable.");
            }else {
                mForum.getApi()
                        .sentCreateTask(mView.tblTask.getUser_id(),mView.tblTask.getGroup_id(),mView.tblTask.getService_type(),mView.tblTask.getStart_date(),mView.tblTask.getEnd_date(),mView.tblTask.getDate_count(),
                                mView.tblTask.getType_create(),mView.tblTask.getDes_lat(),mView.tblTask.getDes_lon(),mView.tblTask.getDest_location(),mView.tblTask.getDest_province(),mView.tblTask.getIdentify(),
                                mView.tblTask.getTime_wait())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<TblTask>() {
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
                            public void onNext(TblTask tblTask) {
                                updateMain(tblTask);
                                //mView.updateMarkerDriverInScope(member);
                                //dialog.dismiss();
                                Log.i("sentCrarteTask", "Ok");
                            }
                        });
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateMain(TblTask tblTask){
        try {

        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
