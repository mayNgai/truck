package com.dtc.sevice.truckclub.presenters.driver;

import android.util.Log;

import com.dtc.sevice.truckclub.model.TblMember;
import com.dtc.sevice.truckclub.service.ApiService;
import com.dtc.sevice.truckclub.view.driver.activity.DriverMainActivity2;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by may on 10/4/2017.
 */

public class DriverMainPresenter {
    private ApiService mForum;
    private DriverMainActivity2 mView;

    public DriverMainPresenter(DriverMainActivity2 view,ApiService forum){
        mForum = forum;
        mView = view;
    }


}
