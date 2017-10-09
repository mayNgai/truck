package com.dtc.sevice.truckclub.view.driver.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.dtc.sevice.truckclub.R;

/**
 * Created by May on 10/9/2017.
 */

public class DriverBookingActivity extends AppCompatActivity {
    private Toolbar toolbar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_booking);
        init();
    }

    private void init(){
        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("ตารางงาน");
            toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
