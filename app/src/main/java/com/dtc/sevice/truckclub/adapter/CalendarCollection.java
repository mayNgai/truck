package com.dtc.sevice.truckclub.adapter;

import java.util.ArrayList;

/**
 * Created by May on 10/10/2017.
 */

public class CalendarCollection {
    public String date="";
    public String event_message="";

    public static ArrayList<CalendarCollection> date_collection_arr;
    public CalendarCollection(String date, String event_message){
        this.date=date;
        this.event_message=event_message;
    }
}
