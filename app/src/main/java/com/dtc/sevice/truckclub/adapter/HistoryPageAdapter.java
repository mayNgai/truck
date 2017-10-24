package com.dtc.sevice.truckclub.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dtc.sevice.truckclub.R;
import com.dtc.sevice.truckclub.model.TblTask;
import com.dtc.sevice.truckclub.until.DateController;
import com.dtc.sevice.truckclub.until.DialogController;
import com.dtc.sevice.truckclub.view.user.activity.UserTaskActivity;

import java.util.List;

/**
 * Created by May on 10/24/2017.
 */

public class HistoryPageAdapter extends RecyclerView.Adapter<HistoryPageAdapter.ViewHolder>{
    private static Context mcontext;
    private List<TblTask> arrayList;
    private DateController dateController;
    private DialogController dialogController;
    public HistoryPageAdapter(Context context, List<TblTask> _arrayList){
        this.arrayList = _arrayList;
        this.mcontext = context;
        dateController = new DateController();
        dialogController = new DialogController();
    }

    @Override
    public void onBindViewHolder(final HistoryPageAdapter.ViewHolder holder, final int i) {
        holder.linear_wait.setVisibility(View.GONE);
        holder.txt_date_start.setText(dateController.convertDateFormat2To1(arrayList.get(i).getStart_date()));
        holder.txt_time_start.setText(arrayList.get(i).getStart_date().substring(10,16));
        //holder.txt_driver_wait.setText(String.valueOf(arrayList.get(i).getMember().size()));
        holder.txt_start_position.setText(arrayList.get(i).getDest_location());
        holder.linear_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setDialogBottom(view,i);
            }
        });

    }

    @Override
    public HistoryPageAdapter.ViewHolder onCreateViewHolder(ViewGroup vGroup, int i) {

        View view = LayoutInflater.from(vGroup.getContext()).inflate(R.layout.list_wait_accept, vGroup, false);
        return new HistoryPageAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_date_start,txt_time_start,txt_driver_wait,txt_start_position;
        private LinearLayout linear_list,linear_wait;

        public ViewHolder(View v) {
            super(v);
            txt_date_start = (TextView) v.findViewById(R.id.txt_date_start);
            txt_time_start = (TextView) v.findViewById(R.id.txt_time_start);
            txt_driver_wait = (TextView) v.findViewById(R.id.txt_driver_wait);
            txt_start_position = (TextView) v.findViewById(R.id.txt_start_position);
            linear_list = (LinearLayout) v.findViewById(R.id.linear_list);
            linear_wait = (LinearLayout) v.findViewById(R.id.linear_wait);
        }
    }
}
