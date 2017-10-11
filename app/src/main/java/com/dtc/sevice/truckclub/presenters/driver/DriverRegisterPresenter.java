package com.dtc.sevice.truckclub.presenters.driver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;

import com.dtc.sevice.truckclub.model.TblCarDetail;
import com.dtc.sevice.truckclub.model.TblMember;
import com.dtc.sevice.truckclub.model.TblPicture;
import com.dtc.sevice.truckclub.model.TblProvince;
import com.dtc.sevice.truckclub.service.ApiService;
import com.dtc.sevice.truckclub.until.DialogController;
import com.dtc.sevice.truckclub.until.NetworkUtils;
import com.dtc.sevice.truckclub.until.TaskController;
import com.dtc.sevice.truckclub.view.LoginSecondActivity;
import com.dtc.sevice.truckclub.view.driver.activity.DriverRegisterActivity;
import com.dtc.sevice.truckclub.view.driver.fragment.RegisterDriverFragmentFirst;
import com.dtc.sevice.truckclub.view.driver.fragment.RegisterDriverFragmentThird;

import java.util.ArrayList;
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
    private static DriverRegisterActivity mView;
    private TaskController taskController;
    private static TblMember tblMember;
    private static int size = 0;
    private RegisterDriverFragmentThird fragmentThird;
    private static boolean success =false;

    public DriverRegisterPresenter(DriverRegisterActivity view , ApiService forum){
        mView = view;
        mForum = forum;
        dialogController = new DialogController();
        taskController = new TaskController();
    }

    public boolean successRegister(){
        try {
//            if(!NetworkUtils.isConnected(mView)){
//                dialogController.dialogNolmal(mView,"Wanning","Internet is not stable.");
//            }else {
                loadRegisterAndCar();
//            }

        }catch (Exception e){
            e.printStackTrace();

        }
        return success;
    }

    public void loadRegisterAndCar(){
        try {
            //dialog = ProgressDialog.show(mView, "Wait", "loading...");
            mForum.getApi()
                    .createMemberandcar(mView.member)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<List<TblMember>>() {
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
                        public void onNext(List<TblMember> member) {
                            Log.i("loadRegisterAndCar", "OK");
                            success = true;
                            //updateSignUp(member);
                            //dialog.dismiss();
                        }
                    });
        }catch (Exception e){
            e.printStackTrace();
        }
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
                tblMember = new TblMember();
                tblMember = member;
                //taskController.createMember(member);
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
                    .createCar(member_id,mView.carDetail.get(0).getLicense_plate(),mView.carDetail.get(0).getCar_brand(),mView.carDetail.get(0).getProvince(),mView.carDetail.get(0).getCar_model(),
                            mView.carDetail.get(0).getGroup_id(),mView.carDetail.get(0).getCar_tow(),mView.carDetail.get(0).getCar_wheels(),mView.carDetail.get(0).getCar_tons(),
                            mView.carDetail.get(0).getOption_trailer(),mView.carDetail.get(0).getSum_weight())
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
                for(TblPicture p : mView.tblPicture){
                    if(p.getPath()!= null && p.getPath().length()>0){
                        loadPictureCar(carDetail.getCar_id(), p.getName());
                        size ++;
                    }

                }

            }else if(carDetail.getSuccess().equalsIgnoreCase("0")){
                dialogController.dialogNolmal(mView,"Wanning",carDetail.getMessage());
                Log.i("message", carDetail.getMessage());
            }
            success = true;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadPictureCar(int car_id , String mane_path){
        try {
            //dialog = ProgressDialog.show(mView, "Wait", "loading...");
            mForum.getApi()
                    .createPictureCar(car_id,mane_path)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<TblPicture>() {
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
                        public void onNext(TblPicture picture) {

                        }
                    });
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
