package com.dtc.sevice.truckclub.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dtc.sevice.truckclub.R;
import com.dtc.sevice.truckclub.model.TblTask;

import java.util.List;

/**
 * Created by May on 10/11/2017.
 */

public class TaskListWaitAdapter extends RecyclerView.Adapter<TaskListWaitAdapter.ViewHolder>{
    private static Context mcontext;
    private List<TblTask> arrayList;
    public TaskListWaitAdapter(Context context, List<TblTask> _arrayList){
        this.arrayList = _arrayList;
        this.mcontext = context;
    }

    @Override
    public void onBindViewHolder(final TaskListWaitAdapter.ViewHolder holder, final int i) {
//        holder.txt_date_start.setText(arrayList.get(i).getDest_location());
//        holder.txt_time_start.setText(arrayList.get(i).getDest_location());
//        holder.txt_driver_wait.setText(arrayList.get(i).getDest_location());
//        holder.txt_start_position.setText(arrayList.get(i).getDest_location());
        holder.linear_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDialogBottom(view,i);
            }
        });

    }

    private void setDialogBottom(View v,final int pos){
        try {
            LayoutInflater inflater = (LayoutInflater)v.getContext().getSystemService(v.getContext().LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate (R.layout.dialog_detail_task_and_info_driver, null);
            ImageView img_back = (ImageView)view.findViewById( R.id.img_back);
            final Dialog mBottomSheetDialog = new Dialog (v.getContext(),R.style.MaterialDialogSheet);
            mBottomSheetDialog.setContentView (view);
            mBottomSheetDialog.setCancelable (true);
            mBottomSheetDialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            mBottomSheetDialog.getWindow ().setGravity (Gravity.BOTTOM);
            mBottomSheetDialog.show ();
            img_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBottomSheetDialog.dismiss();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public TaskListWaitAdapter.ViewHolder onCreateViewHolder(ViewGroup vGroup, int i) {

        View view = LayoutInflater.from(vGroup.getContext()).inflate(R.layout.list_wait_accept, vGroup, false);
        return new TaskListWaitAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txt_date_start,txt_time_start,txt_driver_wait,txt_start_position;
        private LinearLayout linear_list;

        public ViewHolder(View v) {
            super(v);
            txt_date_start = (TextView) v.findViewById(R.id.txt_date_start);
            txt_time_start = (TextView) v.findViewById(R.id.txt_time_start);
            txt_driver_wait = (TextView) v.findViewById(R.id.txt_driver_wait);
            txt_start_position = (TextView) v.findViewById(R.id.txt_start_position);
            linear_list = (LinearLayout) v.findViewById(R.id.linear_list);
        }
    }
}
