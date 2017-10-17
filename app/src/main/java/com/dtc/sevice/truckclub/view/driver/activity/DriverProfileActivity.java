package com.dtc.sevice.truckclub.view.driver.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dtc.sevice.truckclub.R;
import com.dtc.sevice.truckclub.adapter.AddImageAdapter;
import com.dtc.sevice.truckclub.adapter.ItemOffsetDecoration;
import com.dtc.sevice.truckclub.helper.GlobalVar;
import com.dtc.sevice.truckclub.model.TblCarDetail;
import com.dtc.sevice.truckclub.model.TblCarGroup;
import com.dtc.sevice.truckclub.model.TblMember;
import com.dtc.sevice.truckclub.presenters.driver.DriverProfilePresenter;
import com.dtc.sevice.truckclub.service.ApiService;
import com.dtc.sevice.truckclub.until.DateController;
import com.dtc.sevice.truckclub.until.TaskController;
import com.dtc.sevice.truckclub.until.Updown_Image;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by May on 9/27/2017.
 */

public class DriverProfileActivity extends AppCompatActivity implements View.OnClickListener{
    private Toolbar toolbar;
    private Button btn_info,btn_vehicle;
    private ImageView img_profile,img_first_name,img_last_name,img_mail,img_tel;
    private EditText edt_first_name,edt_last_name,edt_mail,edt_tel,edt_car_brand,edt_car_model,edt_license_plate,edt_license_province,edt_sum1,edt_sum2;
    private TextView txt_birth;
    private Spinner spn_sex,spn_car_group,spn_tow;
    private CheckBox chk_weight,chk_option;
    private LinearLayout linear_tow;
    private ScrollView scroll_profile,scroll_vehicle;
    private static RecyclerView recycler_view;
    private MenuItem save,edit;
    private boolean flagEdit = false;
    public static List<TblMember> listMembers;
    private TaskController taskController;
    private Updown_Image download_image;
    private DateController dateController;
    private Activity _activity;
    private static List<TblCarGroup> listCarGroups;

    private DriverProfilePresenter driverProfilePresenter;
    private ApiService apiService;
    private static TblCarDetail carDetail;
    private static AddImageAdapter mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);
        init();
    }

    private void init(){
        taskController = new TaskController();
        download_image = new Updown_Image();
        dateController = new DateController();
        _activity = new DriverProfileActivity();
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
        edt_car_brand = (EditText)findViewById(R.id.edt_car_brand);
        edt_car_model = (EditText)findViewById(R.id.edt_car_model);
        edt_license_plate = (EditText)findViewById(R.id.edt_license_plate);
        edt_license_province = (EditText)findViewById(R.id.edt_license_province);
        edt_sum1 = (EditText)findViewById(R.id.edt_sum1);
        edt_sum2 = (EditText)findViewById(R.id.edt_sum2);
        spn_car_group = (Spinner) findViewById(R.id.spn_car_group);
        spn_tow = (Spinner)findViewById(R.id.spn_tow);
        txt_birth = (TextView)findViewById(R.id.txt_birth);
        spn_sex = (Spinner)findViewById(R.id.spn_sex);
        chk_weight = (CheckBox) findViewById(R.id.chk_weight);
        chk_option = (CheckBox)findViewById(R.id.chk_option);
        scroll_profile = (ScrollView)findViewById(R.id.scroll_profile);
        scroll_vehicle = (ScrollView)findViewById(R.id.scroll_vehicle);
        recycler_view = (RecyclerView)findViewById(R.id.recycler_view);
        linear_tow = (LinearLayout)findViewById(R.id.linear_tow);
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
        getMember();
        if(listMembers != null && listMembers.size()>0){
            setText();
            setCarDetail();
        }
        recycler_view.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recycler_view.setLayoutManager(mLayoutManager);
        recycler_view.setItemAnimator(new DefaultItemAnimator());
        recycler_view.addItemDecoration(new ItemOffsetDecoration(this, R.dimen.item_offset));

    }

    private void getMember(){
        try {
            listMembers = new ArrayList<TblMember>();
            listMembers = taskController.getMember();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setText(){
        try {
            if(listMembers.get(0).getMember_type() == 0){
                Picasso.with(_activity).load(GlobalVar.url_up_pic + listMembers.get(0).getName_pic_path())
                        .placeholder( R.drawable.progress_animation )
                        .fit().centerCrop().error( R.drawable.no_images ).into(img_profile);
            }else if(listMembers.get(0).getMember_type() == 1){
                Picasso.with(_activity).load(ApiService.url_facebook + listMembers.get(0).getFace_book_id() + ApiService.pic_facebook)
                        .placeholder( R.drawable.progress_animation )
                        .fit().centerCrop().error( R.drawable.no_images ).into(img_profile);
            }

            edt_first_name.setText(listMembers.get(0).getFirst_name());
            edt_last_name.setText(listMembers.get(0).getLast_name());
            edt_mail.setText(listMembers.get(0).getEmail());
            txt_birth.setText((listMembers.get(0).getBirth_date() ==null) ? "":dateController.convertDateFormat2To1(listMembers.get(0).getBirth_date()));
            edt_tel.setText(listMembers.get(0).getTel());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setCarDetail(){
        try {
            apiService = new ApiService();
            driverProfilePresenter = new DriverProfilePresenter(DriverProfileActivity.this,apiService);
            driverProfilePresenter.loadCarDetail();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateCarDetail(TblCarDetail detail){
        try {
            carDetail = new TblCarDetail();
            carDetail = detail;
            edt_car_brand.setText(carDetail.getCar_brand());
            edt_car_model.setText(carDetail.getCar_model());
            edt_license_plate.setText(carDetail.getLicense_plate());
            edt_license_province.setText(carDetail.getProvince());
            edt_sum1.setText(String.valueOf(carDetail.getCar_wheels()));
            edt_sum2.setText(String.valueOf(carDetail.getCar_tons()));
            if(carDetail.getGroup_id()==3)
                linear_tow.setVisibility(View.VISIBLE);
            else
                linear_tow.setVisibility(View.GONE);

            if(carDetail.getOption_trailer()==0)
                chk_option.setChecked(false);
            else
                chk_option.setChecked(true);

            if(carDetail.getSum_weight()==0)
                chk_weight.setChecked(false);
            else
                chk_weight.setChecked(true);
            setCarGroup();
            setAdapter();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setAdapter(){
        try {
            mAdapter = new AddImageAdapter(this,carDetail.getPicture());
            recycler_view.setAdapter(mAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setCarGroup(){
        try {
            listCarGroups = taskController.getCarGroup();
            setSpinnerCarGroup();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void setSpinnerCarGroup(){
        try {

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_info:
                if(!flagEdit){
                    btn_info.setBackgroundResource(R.drawable.buttonshape_orange);
                    btn_vehicle.setBackgroundResource(R.drawable.buttonshape_find_location_search_white);
                    scroll_vehicle.setVisibility(View.GONE);
                    scroll_profile.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_vehicle:
                if(!flagEdit){
                    btn_vehicle.setBackgroundResource(R.drawable.buttonshape_orange);
                    btn_info.setBackgroundResource(R.drawable.buttonshape_find_location_search_white);
                    scroll_profile.setVisibility(View.GONE);
                    scroll_vehicle.setVisibility(View.VISIBLE);
                }

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
