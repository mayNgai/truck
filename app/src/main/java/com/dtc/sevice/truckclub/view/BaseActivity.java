package com.dtc.sevice.truckclub.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dtc.sevice.truckclub.R;
import com.dtc.sevice.truckclub.helper.GlobalVar;
import com.dtc.sevice.truckclub.model.TblMember;
import com.dtc.sevice.truckclub.presenters.BasePresenter;
import com.dtc.sevice.truckclub.service.ApiService;
import com.dtc.sevice.truckclub.until.ApplicationController;
import com.dtc.sevice.truckclub.until.TaskController;
import com.dtc.sevice.truckclub.view.driver.activity.DriverProfileActivity;
import com.dtc.sevice.truckclub.view.user.activity.UserBookActivity;
import com.dtc.sevice.truckclub.view.user.activity.UserProfileActivity;
import com.facebook.AccessToken;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by May on 9/26/2017.
 */

public class BaseActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener{
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private ActionBarDrawerToggle drawerToggle;
    private int selectedNavItemId;
    private TaskController taskController;
    public List<TblMember> listMembers;
    private Activity _activity;
    private MenuItem status;

    private ApiService mForum;
    private BasePresenter basePresenter;
    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        FrameLayout activityContainer = (FrameLayout) drawerLayout.findViewById(R.id.activity_content);
        getLayoutInflater().inflate(layoutResID, activityContainer, true);
        super.setContentView(drawerLayout);
        taskController = new TaskController();
        _activity = BaseActivity.this;
        ApplicationController.setAppActivity(_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        View header=navigationView.getHeaderView(0);
        Menu nav_Menu = navigationView.getMenu();
        getMember();
        if(listMembers.get(0).getAuthority().equalsIgnoreCase(GlobalVar.chooseServiceDriver)){
            nav_Menu.findItem(R.id.action_wait_accept).setVisible(false);
            nav_Menu.findItem(R.id.action_books).setTitle(_activity.getString(R.string.txt_schedules));
        }
        TextView name = (TextView)header.findViewById(R.id.username);
        ImageView profile_image = (ImageView) header.findViewById(R.id.profile_image);
        name.setText(listMembers.get(0).getFirst_name());
        if(listMembers.get(0).getMember_type() == 0){
            Picasso.with(_activity).load(GlobalVar.url_up_pic + listMembers.get(0).getName_pic_path())
                    .placeholder( R.drawable.progress_animation )
                    .fit().centerCrop().error( R.drawable.no_images ).into(profile_image);
        }else if(listMembers.get(0).getMember_type() == 1){
            Picasso.with(_activity).load(ApiService.url_facebook + listMembers.get(0).getFace_book_id() + ApiService.pic_facebook)
                    .placeholder( R.drawable.progress_animation )
                    .fit().centerCrop().error( R.drawable.no_images ).into(profile_image);
        }

        if (useToolbar()) {
            setSupportActionBar(toolbar);
        } else {
            toolbar.setVisibility(View.GONE);
        }
        setUpNavView();
    }

    protected void setUpNavView() {
        navigationView.setNavigationItemSelectedListener(this);

        if (useDrawerToggle()) { // use the hamburger menu
            drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                    R.string.drawer_open,
                    R.string.drawer_close);

            drawerLayout.addDrawerListener(drawerToggle);
            drawerToggle.syncState();

        } else if (useToolbar() && getSupportActionBar() != null) {
            // Use home/back button instead
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(getResources()
                    .getDrawable(R.drawable.vans));
        }
    }

    protected boolean useDrawerToggle() {
        return true;
    }

    protected boolean useToolbar() {
        return true;
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        drawerLayout.closeDrawer(GravityCompat.START);
        selectedNavItemId = menuItem.getItemId();

        return onOptionsItemSelected(menuItem);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_driver_menu, menu);
        status = menu.findItem(R.id.status);
        if(listMembers==null || listMembers.size()==0){
            getMember();
        }
        if(listMembers!=null && listMembers.size()>0){
            if(listMembers.get(0).getAuthority().equalsIgnoreCase(GlobalVar.chooseServiceDriver)){
                status.setVisible(true);
                if(listMembers.get(0).getStatus_id()==1){
                    status.setIcon(R.drawable.driver_off);
                }else if(listMembers.get(0).getStatus_id()==2){
                    status.setIcon(R.drawable.driver_on);
                }else {
                    status.setIcon(R.drawable.driver_off);
                    status.setEnabled(false);
                }
            }else {
                status.setVisible(false);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_profile:
                if(listMembers==null || listMembers.size()==0){
                    getMember();
                }
                getMember();
                profilePage();

                return true;
            case R.id.action_books:
                if(listMembers==null&&listMembers.size()>0){
                    if(listMembers.get(0).getAuthority().equalsIgnoreCase("User")){
                        Intent b = new Intent(this , UserBookActivity.class);
                        startActivity(b);
                        finish();
                    }else if(listMembers.get(0).getAuthority().equalsIgnoreCase("Driver")){

                    }
                }
                return true;
            case R.id.action_wait_accept:
                setDialogBottom("Wait accept");
                return true;
            case R.id.action_report:
                setDialogBottom("Report");

                return true;
            case R.id.action_history:


                return true;
            case R.id.action_setting:
                setDialogBottom("Setting");
                return true;
            case R.id.action_about:
                setDialogBottom("About");
                return true;
            case R.id.status:
                if(listMembers==null || listMembers.size()==0){
                    getMember();
                }
                setStatus();
                return true;
            case R.id.action_logout:
                if(listMembers==null || listMembers.size()==0){
                    getMember();
                }
                if(listMembers.size()>0){
                    if(listMembers.get(0).getAuthority().equalsIgnoreCase("User")){
                        if(listMembers.get(0).getStatus_id()<=3){
                            if(listMembers.get(0).getMember_type() == 1)
                                AccessToken.setCurrentAccessToken(null);

                            setLogOut();
//                            taskController.deleteMember(listMembers);
//                            Intent i = new Intent(_activity,LoginFirstActivity.class);
//                            startActivity(i);
//                            finish();
                        }
                    }else if(listMembers.get(0).getAuthority().equalsIgnoreCase("Driver")){
                        if(listMembers.get(0).getStatus_id()<3){
                            if(listMembers.get(0).getMember_type() == 1)
                                AccessToken.setCurrentAccessToken(null);

                            setLogOut();
//                            taskController.deleteMember(listMembers);
//                            Intent i = new Intent(_activity,LoginFirstActivity.class);
//                            startActivity(i);
//                            finish();
                        }
                    }
                }

                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void getMember(){
        try {
            listMembers = new ArrayList<TblMember>();
            listMembers = taskController.getMember();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void profilePage(){
        if(listMembers.get(0).getAuthority().equalsIgnoreCase(_activity.getString(R.string.txtUser))){
            Intent i = new Intent(this , UserProfileActivity.class);
            startActivity(i);
            finish();
        }else if(listMembers.get(0).getAuthority().equalsIgnoreCase(_activity.getString(R.string.txtDriver))){
            Intent i = new Intent(this , DriverProfileActivity.class);
            startActivity(i);
            finish();
        }
    }

    private void setDialogBottom(String txtHead){
        try {
            LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate (R.layout.bottom_dialog_recycle, null);
            TextView txt_head = (TextView)view.findViewById( R.id.txt_head);
            ImageView img_down = (ImageView)view.findViewById( R.id.img_down);
            final Dialog mBottomSheetDialog = new Dialog (this,R.style.MaterialDialogSheet);
            mBottomSheetDialog.setContentView (view);
            mBottomSheetDialog.setCancelable (true);
            mBottomSheetDialog.getWindow ().setLayout (LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
            mBottomSheetDialog.getWindow ().setGravity (Gravity.BOTTOM);
            mBottomSheetDialog.show ();
            txt_head.setText(txtHead);
            img_down.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mBottomSheetDialog.dismiss();

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setStatus(){
        try {
            if(listMembers.get(0).getStatus_id()==1){
                listMembers.get(0).setStatus_id(2);
                listMembers.get(0).setStatus("Searching");
                status.setIcon(R.drawable.driver_on);
                taskController.updateMember(listMembers.get(0));
            }else if(listMembers.get(0).getStatus_id()==2){
                listMembers.get(0).setStatus_id(1);
                listMembers.get(0).setStatus("Log in");
                status.setIcon(R.drawable.driver_off);
                taskController.updateMember(listMembers.get(0));
            }
            mForum = new ApiService();
            basePresenter = new BasePresenter(this,mForum);
            basePresenter.updateStatus();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void setLogOut(){
        try {
            mForum = new ApiService();
            basePresenter = new BasePresenter(this,mForum);
            basePresenter.logOut();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
