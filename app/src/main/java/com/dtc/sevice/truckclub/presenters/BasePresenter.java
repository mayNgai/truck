package com.dtc.sevice.truckclub.presenters;

import android.content.Intent;
import android.util.Log;

import com.dtc.sevice.truckclub.R;
import com.dtc.sevice.truckclub.model.TblMember;
import com.dtc.sevice.truckclub.model.TblTask;
import com.dtc.sevice.truckclub.service.ApiService;
import com.dtc.sevice.truckclub.until.DialogController;
import com.dtc.sevice.truckclub.until.NetworkUtils;
import com.dtc.sevice.truckclub.until.TaskController;
import com.dtc.sevice.truckclub.view.BaseActivity;
import com.dtc.sevice.truckclub.view.LoginFirstActivity;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by may on 10/4/2017.
 */

public class BasePresenter {
    private ApiService mForum;
    private BaseActivity mView;
    private TaskController taskController;
    private DialogController dialogController;

    public BasePresenter(BaseActivity view,ApiService forum){
        mForum = forum;
        mView = view;
        taskController = new TaskController();
        dialogController = new DialogController();
    }

    public void updateStatus(){
        try {
            if(!NetworkUtils.isConnected(mView)){
                dialogController.dialogNolmal(mView,mView.getString(R.string.txtWarning),mView.getString(R.string.txt_internet_is_not));
            }else {
                mForum.getApi()
                        .updateStatusMember(mView.listMembers.get(0).getMember_id(),mView.listMembers.get(0).getStatus(),mView.listMembers.get(0).getStatus_id())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<TblMember>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e("updateStatus Error", e.getMessage());
                            }

                            @Override
                            public void onNext(TblMember member) {
                                //updateCarGroup(groups);
                            }
                        });
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void logOut(){
        try {
            mForum.getApi()
                    .logOut(mView.listMembers.get(0))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<TblMember>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("logOut Error", e.getMessage());
                        }

                        @Override
                        public void onNext(TblMember member) {
                            updateLogOut(member);
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateLogOut(TblMember member){
        try {
            if(member.getSuccess().equalsIgnoreCase("1")){
                List<TblMember> listMembers = new ArrayList<TblMember>();
                member.setGuid(mView.listMembers.get(0).getGuid());
                listMembers.add(member);
                taskController.deleteMember(listMembers);
                Intent i = new Intent(mView,LoginFirstActivity.class);
                mView.startActivity(i);
                mView.finish();
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadTask(){
        try {
            if(!NetworkUtils.isConnected(mView)){
                dialogController.dialogNolmal(mView,mView.getString(R.string.txtWarning),mView.getString(R.string.txt_internet_is_not));
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
                                if(tblTasks.get(0).getTask_status()>2){
                                    mView.updateTaskBooking(tblTasks);
                                }else {
                                    mView.updateTaskWait(tblTasks);
                                }

                            }
                        });
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
