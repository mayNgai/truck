package com.dtc.sevice.truckclub.view.driver.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dtc.sevice.truckclub.R;
import com.dtc.sevice.truckclub.view.user.activity.UserMainActivity;
import com.dtc.sevice.truckclub.view.user.activity.UserProfileActivity;

/**
 * Created by May on 9/27/2017.
 */

public class DriverProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private Button btn_info,btn_vehicle;
    private ImageView img_profile,img_first_name,img_last_name,img_mail,img_tel;
    private EditText edt_first_name,edt_last_name,edt_mail,edt_tel;
    private TextView txt_birth;
    private Spinner spn_sex;
    private ScrollView scroll_profile,scroll_vehicle;
    private MenuItem save,edit;
    private boolean flagEdit = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);
        init();
    }

    private void init(){
        btn_info = (Button)findViewById(R.id.btn_info);
        btn_vehicle = (Button)findViewById(R.id.btn_vehicle);
        img_profile = (ImageView)findViewById(R.id.img_profile);
        img_first_name = (ImageView)findViewById(R.id.img_first_name);
        img_last_name = (ImageView)findViewById(R.id.img_last_name);
        img_mail = (ImageView)findViewById(R.id.img_mail);
        img_tel = (ImageView)findViewById(R.id.img_tel);
        edt_first_name = (EditText)findViewById(R.id.edt_first_name);
        edt_last_name = (EditText)findViewById(R.id.edt_last_name);
        edt_mail = (EditText)findViewById(R.id.edt_mail);
        edt_tel = (EditText)findViewById(R.id.edt_tel);
        txt_birth = (TextView)findViewById(R.id.txt_birth);
        spn_sex = (Spinner)findViewById(R.id.spn_sex);
        scroll_profile = (ScrollView)findViewById(R.id.scroll_profile);
        scroll_vehicle = (ScrollView)findViewById(R.id.scroll_vehicle);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_close_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backButton();
            }
        });
        btn_info.setOnClickListener(this);
        btn_vehicle.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_info:
                scroll_vehicle.setVisibility(View.GONE);
                scroll_profile.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_vehicle:
                scroll_profile.setVisibility(View.GONE);
                scroll_vehicle.setVisibility(View.VISIBLE);
                break;

        }
    }

    private void setEdit(){
        try {

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setDefault(){
        try {

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_profile, menu);
        save = menu.findItem(R.id.action_save);
        edit = menu.findItem(R.id.action_edit);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                flagEdit = false;
                save.setVisible(false);
                edit.setVisible(true);
                setDefault();
                break;
            case R.id.action_edit:
                flagEdit = true;
                save.setVisible(true);
                edit.setVisible(false);
                setEdit();
                break;
            default:
                break;
        }

        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backButton();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    private void backButton(){
        Intent i = new Intent(DriverProfileActivity.this,DriverMainActivity2.class);
        startActivity(i);
        finish();
    }
}
