package com.dtc.sevice.truckclub.view.driver.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dtc.sevice.truckclub.R;
import com.dtc.sevice.truckclub.adapter.CalendarAdapter;
import com.dtc.sevice.truckclub.adapter.CalendarCollection;
import com.dtc.sevice.truckclub.adapter.TaskListAdapter;
import com.dtc.sevice.truckclub.model.SheduleTable;
import com.dtc.sevice.truckclub.model.TblTask;
import com.dtc.sevice.truckclub.presenters.driver.DriverBookingPresenter;
import com.dtc.sevice.truckclub.service.ApiService;
import com.dtc.sevice.truckclub.until.DateController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by May on 10/9/2017.
 */

public class DriverBookingActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private GridView gridview;
    private static RecyclerView recycler_view;
    private CalendarAdapter cal_adapter;
    public GregorianCalendar cal_month;
    private TextView tv_month;
    private ImageButton previous ;
    private ImageButton next;
    private String action = "";
    private String strMonth="",strDate="";
    public static List<String> listFreeDate;
    private DateController dateController;
    private List<TblTask> listTasks;
    private static TaskListAdapter adapter;
    private ApiService mForum;
    private DriverBookingPresenter driverBookingPresenter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_booking);
        init();
    }

    private void init(){
        try {
            dateController = new DateController();
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            gridview = (GridView) findViewById(R.id.gv_calendar);
            cal_month = (GregorianCalendar) GregorianCalendar.getInstance();
            tv_month = (TextView) findViewById(R.id.tv_month);
            previous = (ImageButton) findViewById(R.id.ib_prev);
            next = (ImageButton) findViewById(R.id.Ib_next);
            recycler_view = (RecyclerView)findViewById(R.id.recycler_view);
            tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
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
            listFreeDate = new ArrayList<>();
            cal_adapter = new CalendarAdapter(DriverBookingActivity.this, cal_month, CalendarCollection.date_collection_arr,action,listFreeDate);
            gridview.setAdapter(cal_adapter);
            previous.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if(action.equalsIgnoreCase("setting")) {
//                        flagNext = false;
//                        saveFreeDate();
                    }else {
                        setPreviousMonth();
                        refreshCalendar();
                    }

                }
            });


            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(action.equalsIgnoreCase("setting")) {
//                        flagNext = true;
//                        saveFreeDate();
                    }else {
                        setNextMonth();
                        refreshCalendar();
                    }

                }
            });
            getTask();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void setNextMonth() {
        int x1 = cal_month.get(GregorianCalendar.MONTH);
        if (x1 == cal_month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) + 1),
                    cal_month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) + 1);
        }
    }

    protected void setPreviousMonth() {
        int x1 = cal_month.get(GregorianCalendar.MONTH);
        if (x1 == cal_month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            cal_month.set((cal_month.get(GregorianCalendar.YEAR) - 1),
                    cal_month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            cal_month.set(GregorianCalendar.MONTH,
                    cal_month.get(GregorianCalendar.MONTH) - 1);
        }
    }

    public void refreshCalendar() {
        cal_adapter.refreshDays();
        cal_adapter.notifyDataSetChanged();
        tv_month.setText(android.text.format.DateFormat.format("MMMM yyyy", cal_month));
        strMonth = dateController.dateTimeToStringFormat5(cal_month.getTime());
        //setFreeDate();
    }

    private void getTask(){
        try {
            mForum = new ApiService();
            driverBookingPresenter = new DriverBookingPresenter(DriverBookingActivity.this , mForum);
            driverBookingPresenter.loadTask();
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
            adapter = new TaskListAdapter(DriverBookingActivity.this,listTasks);
            recycler_view.setAdapter(adapter);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void CalendarFill(ArrayList<SheduleTable> sheduleTableArrayList) {
        //==== Fill data to Driver Calendar ====
        if (sheduleTableArrayList != null) {
            CalendarCollection.date_collection_arr = new ArrayList<CalendarCollection>();

            /// startDate
            for (int i = 0; i < sheduleTableArrayList.size(); i++) {
                CalendarCollection.date_collection_arr.add(new CalendarCollection(sheduleTableArrayList.get(i).getStartDate().toString().substring(0, 10)
                        , sheduleTableArrayList.get(i).getStartLocation() +
                        "-" + sheduleTableArrayList.get(i).getDestLocation()));

                CalendarCollection.date_collection_arr.add(new CalendarCollection(sheduleTableArrayList.get(i).getEndDate().toString().substring(0, 10)
                        , sheduleTableArrayList.get(i).getStartLocation() +
                        "-" + sheduleTableArrayList.get(i).getDestLocation()));

                if (sheduleTableArrayList.get(i).getDateCount() > 1)
                    for (int j = 0; j <= sheduleTableArrayList.get(i).getDateCount() - 2; j++) {
                        CalendarCollection.date_collection_arr.add(new CalendarCollection
                                (findBetween(sheduleTableArrayList.get(i).getStartDate().toString(), sheduleTableArrayList.get(i).getDateCount())
                                        .get(j).substring(0, 10)
                                        , sheduleTableArrayList.get(i).getStartLocation() +
                                        "-" + sheduleTableArrayList.get(i).getDestLocation()));
                    }
            }

        }
    }

    private ArrayList<String> findBetween(String startDate, int count) {
        ArrayList<String> dateBetween = new ArrayList<String>();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        String FirstBetweenDate = "000";
        try {
            c.setTime(sdf.parse(startDate));
            c.add(Calendar.DATE, 1);
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
            FirstBetweenDate = sdf1.format(c.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar c2 = Calendar.getInstance();
        for (int i = 0; i <= count - 1; i++) {
            try {
                c2.setTime(sdf.parse(FirstBetweenDate));
                c2.add(Calendar.DATE, i);
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
                dateBetween.add(sdf2.format(c2.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Log.d("fff", String.valueOf(dateBetween));
        return dateBetween;

    }
}
