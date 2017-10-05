package com.dtc.sevice.truckclub.presenters.user;

import android.app.ProgressDialog;
import android.util.Log;

import com.dtc.sevice.truckclub.model.TblMember;
import com.dtc.sevice.truckclub.service.ApiService;
import com.dtc.sevice.truckclub.until.DialogController;
import com.dtc.sevice.truckclub.until.TaskController;
import com.dtc.sevice.truckclub.view.user.activity.UserProfileActivity;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by May on 9/28/2017.
 */

public class UserProfilePresenter {
    private ProgressDialog dialog;
    private ApiService mForum;
    private UserProfileActivity mView;
    private DialogController dialogController;
    private TaskController taskController;

    public UserProfilePresenter(UserProfileActivity view , ApiService forum){
        mForum = forum;
        mView = view;
        dialogController = new DialogController();
        taskController = new TaskController();
    }

    public void updateProfile(){
        try {
            dialog = ProgressDialog.show(mView, "Wait", "loading...");
            mForum.getApi()
                    .updateUserProfile(mView.listMembers.get(0).getMember_id(),mView.listMembers.get(0).getFirst_name(),mView.listMembers.get(0).getLast_name(),
                            mView.listMembers.get(0).getEmail(),mView.listMembers.get(0).getTel(),mView.listMembers.get(0).getBirth_date(),mView.listMembers.get(0).getSex(),
                            mView.listMembers.get(0).getName_pic_path())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<TblMember>() {
                        @Override
                        public void onCompleted() {
                            dialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("updateProfile Error", e.getMessage());
                            dialog.dismiss();
                        }

                        @Override
                        public void onNext(TblMember member) {
                            updateProfile(member);
                            dialog.dismiss();
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateProfile(TblMember member){
        try {
            if(member.getSuccess().equalsIgnoreCase("1")){
                member.setGuid(mView.listMembers.get(0).getGuid());
                taskController.updateMember(member);
                mView.setDefault();
            }else if(member.getSuccess().equalsIgnoreCase("0")){
                dialogController.dialogNolmal(mView,"Wanning",member.getMessage());
                Log.i("message", member.getMessage());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
