package com.dtc.sevice.truckclub.view.user.activity;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dtc.sevice.truckclub.R;
import com.dtc.sevice.truckclub.adapter.TypeCarAdapter;
import com.dtc.sevice.truckclub.helper.GlobalVar;
import com.dtc.sevice.truckclub.model.TblCarGroup;
import com.dtc.sevice.truckclub.model.TblMember;
import com.dtc.sevice.truckclub.model.TblTask;
import com.dtc.sevice.truckclub.presenters.user.UserMainPresenter;
import com.dtc.sevice.truckclub.service.ApiService;
import com.dtc.sevice.truckclub.until.DateController;
import com.dtc.sevice.truckclub.until.DialogController;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by admin on 9/20/2017 AD.
 */

public class UserMainActivity2 extends BaseActivity implements View.OnClickListener,OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener , com.google.android.gms.location.LocationListener{
    private static Button btn_now,btn_booking,btc_call_service;
    private static EditText edt_start,edt_count_date,edt_end_date,edt_start_date,edt_iden,edt_start_time,edt_end_time;
    private CheckBox chk_cash,chk_credit;
    private ImageView img_start,img_del_start;
    private static LinearLayout linear_select_type,linear_current,linear_detail,linear_main1,linear_main2,linear_bottom,linear_head;
    private static TextView txtname_car;
    //private LinearLayout linear_detail;
    //Map
    private GoogleApiClient client;
    private GoogleApiClient mGoogleApiClient;
    private Location locationUserRealTime;
    private SupportMapFragment dMapFragment2;
    private GoogleMap gMap;
    private Marker markerDriverRealTime;
    private static Marker markerDes;
    private double longitude;
    private double latitude;
    private Marker[] markers = new Marker[10000];
    private int subLastText = 25;

    private TaskController taskController;
    public List<TblMember> members;
    private Activity _activity;
    private ApiService mApiService;
    private UserMainPresenter mUserMainPresenter;
    private static List<TblCarGroup> listCarGroups;
    public static TblTask tblTask;
    private DateController dateController;
    private String service_type = "";
    private DialogController dialog;
    private boolean flagCountDate =  false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user4);
        init();
    }

    private void init(){
        taskController = new TaskController();
        members = new ArrayList<TblMember>();
        tblTask = new TblTask();
        _activity = new UserMainActivity2();
        dateController = new DateController();
        dialog = new DialogController();
        //linear_detail = (LinearLayout)findViewById(R.id.linear_detail);
        btn_now = (Button)findViewById(R.id.btn_now);
        btn_booking = (Button)findViewById(R.id.btn_booking);
        btc_call_service = (Button)findViewById(R.id.btc_call_service);
        edt_start = (EditText)findViewById(R.id.edt_start);
        edt_count_date = (EditText)findViewById(R.id.edt_count_date);
        edt_end_date = (EditText)findViewById(R.id.edt_end_date);
        edt_start_date = (EditText)findViewById(R.id.edt_start_date);
        edt_end_time = (EditText)findViewById(R.id.edt_end_time);
        edt_start_time = (EditText)findViewById(R.id.edt_start_time);
        edt_iden = (EditText)findViewById(R.id.edt_iden);
        chk_cash = (CheckBox)findViewById(R.id.chk_cash);
        img_start = (ImageView)findViewById(R.id.img_start);
        linear_current = (LinearLayout)findViewById(R.id.linear_current);
        img_del_start = (ImageView)findViewById(R.id.img_del_start);
        linear_select_type = (LinearLayout)findViewById(R.id.linear_select_type);
        linear_detail = (LinearLayout)findViewById(R.id.linear_detail);
        linear_main1 = (LinearLayout)findViewById(R.id.linear_main1);
        linear_main2 = (LinearLayout)findViewById(R.id.linear_main2);
        linear_bottom = (LinearLayout)findViewById(R.id.linear_bottom);
        linear_head = (LinearLayout)findViewById(R.id.linear_head);
        txtname_car = (TextView)findViewById(R.id.txtname_car);
        //img_del_iden = (ImageView)findViewById(R.id.img_del_iden);

        members = taskController.getMember();
        linear_select_type.setOnClickListener(this);
        linear_current.setOnClickListener(this);
        img_start.setOnClickListener(this);
        img_del_start.setOnClickListener(this);
        btn_now.setOnClickListener(this);
        btn_booking.setOnClickListener(this);
        btc_call_service.setOnClickListener(this);
        edt_end_date.setOnClickListener(this);
        edt_start_date.setOnClickListener(this);
        edt_start_time.setOnClickListener(this);
        edt_end_time.setOnClickListener(this);
        //img_del_iden.setOnClickListener(this);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        dMapFragment2 = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.UserMainActivity);
        dMapFragment2.getMapAsync(this);

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
        setChangeTextStart();
        setCarGroup();
        //setChangeTextIden();
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("UserMainActivity Page")
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
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinate, 9));

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
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        rlp.setMargins(0, 60, 10, 0);

        getDestinationByFinger();

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
                members = taskController.getMember();
            members.get(0).setLat(Float.valueOf(String.valueOf(latitude)));
            members.get(0).setLon(Float.valueOf(String.valueOf(longitude)));
            members.get(0).setRadius(0.05);
            callDriverInScope();
        }
    }

    private void getDestinationByFinger() {
        try {
            gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latlon) {
                    try {
                        if(latlon != null) {
                            addMarkerDestination(latlon);
                            moveMap(latlon.latitude,latlon.longitude);
                            String strAddress = getAddressByLatLng(latlon.latitude,latlon.longitude);
                            edt_start.setText((strAddress.length()>40) ? strAddress.substring(0,40) + ".." : strAddress);
                            String strProvince = getProvinceByLatLng(latlon.latitude,latlon.longitude);
                            setDestination(latlon.latitude,latlon.longitude,strProvince,strAddress);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private String starDistination = "";
    private void getDestinationBySearch() {
        try {
            starDistination = edt_start.getText().toString().trim();
            List<Address> addressList = null;
            if(starDistination.length()>0){
                Geocoder geocoder = new Geocoder(this);
                try {
                    addressList = geocoder.getFromLocationName(starDistination, 1);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Address address;

                if (addressList != null && addressList.size() != 0) {
                    address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    addMarkerDestination(latLng);
                    moveMap(address.getLatitude(),address.getLongitude());
                    String strAddress = getAddressByLatLng(address.getLatitude(),address.getLongitude());
                    edt_start.setText((strAddress.length()>40) ? strAddress.substring(0,40) + ".." : strAddress);
                    setDestination(address.getLatitude(),address.getLongitude(),address.getLocality(),strAddress);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDestination(double lat , double lon , String province ,String location){
        try {
            tblTask = new TblTask();
            tblTask.setDes_lat(Float.valueOf(String.valueOf(lat)));
            tblTask.setDes_lon(Float.valueOf(String.valueOf(lon)));
            tblTask.setDest_province(province);
            tblTask.setDest_location(location);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addMarkerDestination(LatLng latlon){
        removeMarkerDestination();

        markerDes = gMap.addMarker(new MarkerOptions().position(latlon).icon(BitmapDescriptorFactory.fromResource(R.drawable.flag)).title("Your Destination"));

    }

    private void moveMap(double lat ,double lon) {
        LatLng latLng = new LatLng(lat, lon);

        gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        gMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        gMap.getUiSettings().setZoomControlsEnabled(true);


    }

    public String getAddressByLatLng(double lat ,double lon) {
        String strAddress = "";
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
            strAddress = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return strAddress;
    }

    public String getProvinceByLatLng(double lat ,double lon) {
        String strAddress = "",city = "";
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
            strAddress = addresses.get(0).getAddressLine(0);
            city = addresses.get(0).getLocality();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return city;
    }

    private void removeMarkerDestination(){
        if (markerDes != null)
            markerDes.remove();
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
            case R.id.linear_current:
                getCurrentLocation();
                break;
            case R.id.img_start:
                getDestinationBySearch();
                break;
            case R.id.img_del_start:
                setDefault();
                break;
            case R.id.btn_now:
                if(edt_start.getText().toString().length()>0){
                    edt_start_date.setText(GlobalVar.getSystemDateOnly(UserMainActivity2.this));
                    edt_start_time.setText(GlobalVar.getSystemTimeOnly(UserMainActivity2.this));
                    edt_start_date.setEnabled(false);
                    edt_start_time.setEnabled(false);
                    linear_detail.setVisibility(View.VISIBLE);
                    linear_select_type.setVisibility(View.INVISIBLE);
                    btn_now.setTextColor(getResources().getColor(R.color.colorPrimary));
                    btn_booking.setTextColor(getResources().getColor(R.color.black_1));
                    service_type = "Now";
                }
                break;
            case R.id.btn_booking:
                if(edt_start.getText().toString().length()>0){
                    edt_start_date.setText("");
                    edt_start_time.setText("");
                    edt_start_date.setEnabled(true);
                    edt_start_time.setEnabled(true);
                    linear_detail.setVisibility(View.VISIBLE);
                    linear_select_type.setVisibility(View.INVISIBLE);
                    btn_now.setTextColor(getResources().getColor(R.color.black_1));
                    btn_booking.setTextColor(getResources().getColor(R.color.colorPrimary));
                    service_type = "Booking";
                }

                break;
            case R.id.linear_select_type:
                setDialogBottom();
                break;

            case R.id.btc_call_service:
                if(edt_start.getText().toString().length()>0){
                    setdata();
                }

                break;
            case R.id.edt_start_date:
                flagCountDate = false;
                edt_end_date.setText("");
                edt_start_time.setText("");
                edt_end_time.setText("");
                edt_count_date.setText("");
                setDatePicker(edt_start_date);
                break;
            case R.id.edt_end_date:
                if(edt_start_date.getText().toString().length()>0&&edt_start_time.getText().toString().length()>0) {
                    flagCountDate = true;
                    setDatePicker(edt_end_date);
                }else
                    dialog.dialogNolmal(UserMainActivity2.this, getResources().getString(R.string.txtWarning),getResources().getString(R.string.txtPleaseChooseDate));
                break;
            case R.id.edt_start_time:
                if(edt_start_date.getText().toString().length()>0)
                    setTimePicker(edt_start_time);
                else
                    dialog.dialogNolmal(UserMainActivity2.this, getResources().getString(R.string.txtWarning),getResources().getString(R.string.txtPleaseChooseDate));
                break;
            case R.id.edt_end_time:
                if(edt_start_date.getText().toString().length()>0&&edt_start_time.getText().toString().length()>0&&edt_end_date.getText().toString().length()>0)
                    setTimePicker(edt_end_time);
                else
                    dialog.dialogNolmal(UserMainActivity2.this, getResources().getString(R.string.txtWarning),getResources().getString(R.string.txtPleaseChooseDate));
                break;

        }
    }

    private void setdata(){
        try {
            boolean cancel = false;
            if(edt_start_date.getText().toString().length()==0){
                cancel = true;
            }else if(edt_start_time.getText().toString().length()==0){
                cancel = true;
            }else if(edt_end_date.getText().toString().length()==0){
                cancel = true;
            }else if(edt_end_time.getText().toString().length()==0){
                cancel = true;
            }

            if(!cancel){
                tblTask.setUser_id(members.get(0).getMember_id());
                tblTask.setGroup_id(position);
                tblTask.setService_type(service_type);
                tblTask.setDate_count(Integer.parseInt(edt_count_date.getText().toString()));
                tblTask.setType_create("1");
                tblTask.setIdentify(edt_iden.getText().toString());
                if(service_type.equalsIgnoreCase("Now"))
                    tblTask.setTime_wait(10);
                tblTask.setStart_date(dateController.convertDateFormat1To2(edt_start_date.getText().toString()) + " " + edt_start_time.getText().toString());
                tblTask.setEnd_date(dateController.convertDateFormat1To2(edt_end_date.getText().toString()) + " " + edt_end_time.getText().toString());
                mApiService = new ApiService();
                mUserMainPresenter = new UserMainPresenter(UserMainActivity2.this,mApiService);
                mUserMainPresenter.sentCreateTask();

            }else {
                dialog.dialogNolmal(UserMainActivity2.this, getResources().getString(R.string.txtWarning),getResources().getString(R.string.txtPleaseChooseDate));
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void setDatePicker(final EditText editText) {
        String txtDate = "";
        Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(UserMainActivity2.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String aa = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;

                if (monthOfYear + 1 < 10 || dayOfMonth < 10) {
                    if (monthOfYear + 1 < 10)
                        aa = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                    if (dayOfMonth < 10)
                        aa = year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth;
                    if (monthOfYear + 1 < 10 && dayOfMonth < 10)
                        aa = year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth;
                }
                editText.setText(dateController.convertDateFormat2To1(aa));
                if(flagCountDate){
                    int days = dateController.daysBetween(dateController.dateFormat1Tolong(edt_start_date.getText().toString()),dateController.dateFormat2Tolong(aa));
                    //int days = Days.daysBetween(dateController.convertDateFormat2To1(edt_start_date.getText().toString()), aa).getDays();
                    edt_count_date.setText(String.valueOf(days));
                }
            }
        }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    private void setTimePicker(final EditText editText){
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        if(minute<10) {
                            String time = "0" + minute;
                            //minute = Integer.parseInt(time);
                            editText.setText(hourOfDay + ":" + time);
                        }else {
                            editText.setText(hourOfDay + ":" + minute);
                        }

                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }
    private void setCarGroup(){
        try {
            listCarGroups = taskController.getCarGroup();
            updateNameCar();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void updateNameCar(){
        for(TblCarGroup c : listCarGroups){
            if(c.getIsSelect() == 1){
                txtname_car.setText(c.getName_group());
            }
        }

    }
    private static Dialog mBottomSheetDialog;
    private void setDialogBottom(){
        try {
            LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate (R.layout.bottom_type_car, null);
            RecyclerView mRecyclerView = (RecyclerView)view.findViewById(R.id.recycler_view);
//            TextView txt_head = (TextView)view.findViewById( R.id.txt_head);
            ImageView img_cancel = (ImageView)view.findViewById( R.id.img_cancel);
            mBottomSheetDialog = new Dialog (this,R.style.MaterialDialogSheet);
            mBottomSheetDialog.setContentView (view);
            mBottomSheetDialog.setCancelable (true);
            mRecyclerView.setHasFixedSize(true);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLayoutManager);
            TypeCarAdapter adapter = new TypeCarAdapter(this,listCarGroups);
            mRecyclerView.setAdapter(adapter);
            mBottomSheetDialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            mBottomSheetDialog.getWindow ().setGravity (Gravity.BOTTOM);
            mBottomSheetDialog.show ();
//            txt_head.setText(txtHead);
            img_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBottomSheetDialog.dismiss();

                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    static int position = 1;
    public void setSelectCar(int i){
        position = i + 1;
        for(int a = 0 ; a<listCarGroups.size();a++){
            if(a == i){
                listCarGroups.get(a).setIsSelect(1);
                txtname_car.setText(listCarGroups.get(a).getName_group());

            }else {
                listCarGroups.get(a).setIsSelect(0);
            }
        }
        mBottomSheetDialog.dismiss();
    }

    private void setChangeTextStart(){
        ((EditText) findViewById(R.id.edt_start)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s != null && s.length() > 0) {
                    //dp something
                    img_del_start.setVisibility(View.VISIBLE);
                } else {
                    img_del_start.setVisibility(View.INVISIBLE);
                }
            }
        });
    }



    private void callDriverInScope(){
        try {
            mApiService = new ApiService();
            mUserMainPresenter = new UserMainPresenter(UserMainActivity2.this,mApiService);
            mUserMainPresenter.loadDriverInScope();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updateMarkerDriverInScope(List<TblMember> m){
        try {
            markers = new Marker[10000];
            for (int i = 0; i < m.size(); i++) {
                markers[i] = gMap.addMarker(new MarkerOptions()
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_truck_marker))
                        .position(new LatLng(Double.parseDouble(String.valueOf(m.get(i).getLat())), Double.parseDouble(String.valueOf(m.get(i).getLon()))))
                        .title("คุณ" + " " + m.get(i).getFirst_name()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setDefault(){
        try {
            edt_start.setText("");
            edt_start_date.setText("");
            edt_start_time.setText("");
            edt_end_date.setText("");
            edt_end_time.setText("");
            edt_count_date.setText("");
            edt_iden.setText("");
            linear_detail.setVisibility(View.GONE);
            btn_now.setTextColor(getResources().getColor(R.color.black_1));
            btn_booking.setTextColor(getResources().getColor(R.color.black_1));
            removeMarkerDestination();
            linear_select_type.setVisibility(View.VISIBLE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Intent i = new Intent(UserMainActivity.this, LoginSecondActivity.class);
//            startActivity(i);
//            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }
}
