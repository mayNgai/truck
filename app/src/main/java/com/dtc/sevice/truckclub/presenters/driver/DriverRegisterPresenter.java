package com.dtc.sevice.truckclub.presenters.driver;

import android.app.ProgressDialog;
import android.util.Log;

import com.dtc.sevice.truckclub.model.TblCarDetail;
import com.dtc.sevice.truckclub.model.TblMember;
import com.dtc.sevice.truckclub.model.TblProvince;
import com.dtc.sevice.truckclub.service.ApiService;
import com.dtc.sevice.truckclub.until.DialogController;
import com.dtc.sevice.truckclub.until.TaskController;
import com.dtc.sevice.truckclub.view.driver.activity.DriverRegisterActivity;

import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by admin on 9/20/2017 AD.
 */

public class DriverRegisterPresenter {
    private ProgressDialog dialog;
    private ApiService mForum;
    private DialogController dialogController;
    private DriverRegisterActivity mView;
    private TaskController taskController;

    public DriverRegisterPresenter(DriverRegisterActivity view , ApiService forum){
        mView = view;
        mForum = forum;
        dialogController = new DialogController();
        taskController = new TaskController();
    }

    public void loadRegister(){
        try {
            //dialog = ProgressDialog.show(mView, "Wait", "loading...");
            mForum.getApi()
                    .createMember(mView.member.getFirst_name(),mView.member.getLast_name(),mView.member.getEmail(),
                            mView.member.getUser_name(),mView.member.getTel(),mView.member.getBirth_date(),mView.member.getSex(),mView.member.getPassword()
                            ,mView.member.getMember_type(),mView.member.getAuthority(),mView.member.getDevice_id(),mView.member.getFace_book_id(),mView.member.getName_pic_path())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<TblMember>() {
                        @Override
                        public void onCompleted() {
                           // dialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("loadRegister Error", e.getMessage());
                            //dialog.dismiss();
                        }

                        @Override
                        public void onNext(TblMember member) {
                            updateSignUp(member);
                            //dialog.dismiss();
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateSignUp(TblMember member){
        try {
            if(member.getSuccess().equalsIgnoreCase("1")){
                taskController.createMember(member);
                loadRegisterCar(member.getMember_id());
            }else if(member.getSuccess().equalsIgnoreCase("0")){
                dialogController.dialogNolmal(mView,"Wanning",member.getMessage());
                Log.i("message", member.getMessage());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadRegisterCar(int member_id){
        try {
            //dialog = ProgressDialog.show(mView, "Wait", "loading...");
            mForum.getApi()
                    .createCar(member_id,mView.carDetail.getLicense_plate(),mView.carDetail.getCar_brand(),mView.carDetail.getProvince(),mView.carDetail.getCar_model(),
                            mView.carDetail.getGroup_id(),mView.carDetail.getCar_tow(),mView.carDetail.getCar_wheels(),mView.carDetail.getCar_tons(),
                            mView.carDetail.getOption_trailer(),mView.carDetail.getSum_weight())
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<TblCarDetail>() {
                        @Override
                        public void onCompleted() {
                            //dialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("loadRegisterCar Error", e.getMessage());
                            //dialog.dismiss();
                        }

                        @Override
                        public void onNext(TblCarDetail carDetail) {
                            updateSignUpCar(carDetail);
                            //dialog.dismiss();
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateSignUpCar(TblCarDetail carDetail){
        try {
            if(carDetail.getSuccess().equalsIgnoreCase("1")){
               // taskController.createMember(member);
            }else if(carDetail.getSuccess().equalsIgnoreCase("0")){
                dialogController.dialogNolmal(mView,"Wanning",carDetail.getMessage());
                Log.i("message", carDetail.getMessage());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadProvince(){
        try {
            dialog = ProgressDialog.show(mView, "Wait", "loading...");
            mForum.getApi()
                    .getProvince()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<TblProvince>>() {
                        @Override
                        public void onCompleted() {
                            dialog.dismiss();
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.e("getProvince Error", e.getMessage());
                            dialog.dismiss();
                        }

                        @Override
                        public void onNext(List<TblProvince> province) {
                            mView.addProvince(province);
                            dialog.dismiss();
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
