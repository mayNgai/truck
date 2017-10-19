package com.dtc.sevice.truckclub.view.driver.activity;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dtc.sevice.truckclub.R;
import com.dtc.sevice.truckclub.adapter.TaskListAdapter;
import com.dtc.sevice.truckclub.model.TblMember;
import com.dtc.sevice.truckclub.model.TblTask;
import com.dtc.sevice.truckclub.presenters.driver.DriverMainPresenter;
import com.dtc.sevice.truckclub.service.ApiService;
import com.dtc.sevice.truckclub.until.TaskController;
import com.dtc.sevice.truckclub.view.BaseActivity;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by May on 9/27/2017.
 */

public class DriverMainActivity2 extends BaseActivity implements View.OnClickListener,OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener , com.google.android.gms.location.LocationListener{

    //Map
    private GoogleApiClient client;
    private GoogleApiClient mGoogleApiClient;
    private Location locationUserRealTime;
    private SupportMapFragment dMapFragment2;
    private GoogleMap gMap;
    private Marker markerDriverRealTime;
    private Marker markerDes;
    private double longitude;
    private double latitude;
    private Marker[] markers = new Marker[10000];
    private int subLastText = 25;

    private static TaskController taskController;
    public List<TblMember> members;
    public static TblTask tblTask;
    public static List<TblTask> listTasks;
    private Activity _activity;
    public static LinearLayout linear_select_type;
    private static BaseActivity baseActivity;
    private ApiService mForum;
    private DriverMainPresenter driverMainPresenter;
    private static TaskListAdapter adapter;
    private static RecyclerView recycler_view;
    private SwipeRefreshLayout swipe_refresh;
    private static RelativeLayout linear_title;
    private static ImageButton img_step1,img_step2,img_step3;
    private static TextView txt_step1,txt_step2,txt_step3;
    private static Button btn_done,btn_arrive;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.activity_main_driver2);
        init();
        getSchedulesDriver();
    }

    private void init(){
        recycler_view = (RecyclerView)findViewById(R.id.recycler_view);
        linear_select_type = (LinearLayout)findViewById(R.id.linear_select_type);
        swipe_refresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        linear_title = (RelativeLayout)findViewById(R.id.linear_title);
        img_step1 = (ImageButton)findViewById(R.id.img_step1);
        img_step2 = (ImageButton)findViewById(R.id.img_step2);
        img_step3 = (ImageButton)findViewById(R.id.img_step3);
        txt_step1 = (TextView)findViewById(R.id.txt_step1);
        txt_step2 = (TextView)findViewById(R.id.txt_step2);
        txt_step3 = (TextView)findViewById(R.id.txt_step3);
        btn_done = (Button)findViewById(R.id.btn_done);
        btn_arrive = (Button)findViewById(R.id.btn_arrive);
        taskController =new TaskController();
        baseActivity = new BaseActivity();
        members = new ArrayList<TblMember>();
        members = baseActivity.listMembers;
        _activity = new DriverMainActivity2();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        dMapFragment2 = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.DriverMap);
        dMapFragment2.getMapAsync(this);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        setShowSelectList();
        setTitle();
        setRefreshList();
        btn_arrive.setOnClickListener(this);
        btn_done.setOnClickListener(this);
//        setClickList();
    }

    public void setTitle(){
        try {
            if(members == null ||members.size()==0){
                members = baseActivity.listMembers;
            }
            if(members.get(0).getStatus_id()>3){
                linear_title.setVisibility(View.VISIBLE);
                if(members.get(0).getStatus_id()==4){
                    img_step1.setBackgroundResource(R.drawable.button_cricle_orange);
                    img_step2.setBackgroundResource(R.drawable.button_cricle_orange_stock);
                    img_step3.setBackgroundResource(R.drawable.button_cricle_orange_stock);
                    txt_step1.setTextColor(getResources().getColor(R.color.black_1));
                    txt_step2.setTextColor(getResources().getColor(R.color.LightGray));
                    txt_step3.setTextColor(getResources().getColor(R.color.LightGray));
                    btn_arrive.setVisibility(View.VISIBLE);
                    btn_done.setVisibility(View.GONE);
                }else if(members.get(0).getStatus_id()==5){
                    img_step1.setBackgroundResource(R.drawable.button_cricle_orange_stock);
                    img_step2.setBackgroundResource(R.drawable.button_cricle_orange);
                    img_step3.setBackgroundResource(R.drawable.button_cricle_orange_stock);
                    txt_step1.setTextColor(getResources().getColor(R.color.LightGray));
                    txt_step2.setTextColor(getResources().getColor(R.color.black_1));
                    txt_step3.setTextColor(getResources().getColor(R.color.LightGray));
                    btn_arrive.setVisibility(View.GONE);
                    btn_done.setVisibility(View.VISIBLE);
                }
            }else {
                linear_title.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setShowSelectList(){
        try {
            if(members == null ||members.size()==0){
                members = baseActivity.listMembers;
            }
            if(members.get(0).getStatus_id()==2){
                linear_select_type.setVisibility(View.VISIBLE);
                getTask();
            }else {
                linear_select_type.setVisibility(View.GONE);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getTask(){
        try {
            tblTask = new TblTask();
            tblTask.setService_type("Now");
            tblTask.setTask_status(1);
            tblTask.setMember(members);
            mForum = new ApiService();
            driverMainPresenter = new DriverMainPresenter(DriverMainActivity2.this , mForum);
            driverMainPresenter.loadTask();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setListTask(List<TblTask> lists){
        try {
            recycler_view.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            recycler_view.setLayoutManager(mLayoutManager);
            listTasks = new ArrayList<TblTask>();
            listTasks = lists;
            adapter = new TaskListAdapter(DriverMainActivity2.this,listTasks,0);
            recycler_view.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void getSchedulesDriver(){
        try {
            mForum = new ApiService();
            driverMainPresenter = new DriverMainPresenter(DriverMainActivity2.this , mForum);
            driverMainPresenter.loadSchedulesDriver();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setUpdateListSchedulesTask(List<TblTask> lists){
        try {
            taskController.createTask(lists);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setRefreshList(){
        try {
            swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getTask();
                    swipe_refresh.setRefreshing(false);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    public void setClickList(){
//        try {
//            recycler_view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    setDialogBottom();
//                }
//            });
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

//    private static Dialog mBottomSheetDialog;
//    private void setDialogBottom(){
//        try {
//            LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
//            View view = inflater.inflate (R.layout.dialog_driver_offer, null);
//            ImageView img_cancel = (ImageView)view.findViewById( R.id.img_cancel);
//            mBottomSheetDialog = new Dialog (this,R.style.MaterialDialogSheet);
//            mBottomSheetDialog.setContentView (view);
//            mBottomSheetDialog.setCancelable (true);
//            mBottomSheetDialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
//            mBottomSheetDialog.getWindow ().setGravity (Gravity.BOTTOM);
//            mBottomSheetDialog.show ();
//            img_cancel.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mBottomSheetDialog.dismiss();
//
//                }
//            });
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("DriverMainActivity2 Page")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onLocationChanged(Location location) {
        locationUserRealTime = location;
        if (location == null) {
            return;
        }
        longitude = location.getLatitude();
        longitude= location.getLongitude();

        LatLng coordinate = new LatLng(longitude, longitude);
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 17));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        gMap = googleMap;
        markerDriverRealTime = gMap.addMarker(new MarkerOptions().position(new LatLng(1, 1)).title("Your Driver"));
        markerDes = gMap.addMarker(new MarkerOptions().position(new LatLng(1, 1)).title("Your Destination"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);

        View locationButton = ((View) findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
//        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
//        rlp.setMargins(0, 1100, 0, 0);

//        getDestinationByFinger();

    }

    //Getting current location
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            //Getting longitude and latitude
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            //moving the map to location
            moveMap(latitude ,longitude);
            //String strAddress = getAddressByLatLng(latitude,longitude);
            //edt_start.setText((strAddress.length()>25) ? strAddress.substring(0,25) + ".." : strAddress);
            if(members == null || members.size() == 0)
                members = baseActivity.listMembers;
            members.get(0).setLat(Float.valueOf(String.valueOf(latitude)));
            members.get(0).setLon(Float.valueOf(String.valueOf(longitude)));
            members.get(0).setRadius(0.05);
            //callDriverInScope();
        }
    }
    private void moveMap(double lat ,double lon) {
        LatLng latLng = new LatLng(lat, lon);

        gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        gMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        gMap.getUiSettings().setZoomControlsEnabled(true);


    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (lastLocation != null) {
            latitude = lastLocation.getLatitude();
            longitude = lastLocation.getLongitude();
            getCurrentLocation();

        } else {
            Toast.makeText(this, "ไม่สามารถระบุตำแหน่งได้", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    protected void onStart() {
        super.onStart();
        client.connect();
        mGoogleApiClient.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
        super.onStop();
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_done:

                break;
            case R.id.btn_arrive:
                mForum = new ApiService();
                driverMainPresenter = new DriverMainPresenter(DriverMainActivity2.this , mForum);
//                driverMainPresenter.loadTask();
                break;
//            case R.id.img_del_start:
//                edt_start.setText("");
//                removeMarkerDestination();
//                break;
//            case R.id.btn_now:
//
//                break;
//            case R.id.btn_booking:
//
//                break;

        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent i = new Intent(DriverMainActivity2.this, LoginSecondActivity.class);
//            startActivity(i);
//            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
