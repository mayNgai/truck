package com.dtc.sevice.truckclub.presenters.user;

import android.app.ProgressDialog;
import android.util.Log;

import com.dtc.sevice.truckclub.R;
import com.dtc.sevice.truckclub.model.TblMember;
import com.dtc.sevice.truckclub.model.TblTask;
import com.dtc.sevice.truckclub.service.ApiService;
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
