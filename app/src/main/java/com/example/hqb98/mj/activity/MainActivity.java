package com.example.hqb98.mj.activity;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.hqb98.mj.fragment.DateFragment;
import com.example.hqb98.mj.fragment.DateFragmentOne;
import com.example.hqb98.mj.fragment.KebiaoFragment;
import com.example.hqb98.mj.fragment.MeFragment;
import com.example.hqb98.mj.fragment.MonitorFragment;
import com.example.hqb98.mj.R;
import com.example.hqb98.mj.util.BottomNavigationViewHelper;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigation;
    private Fragment[] fragments;
    private Fragment kebiaoFragment;
    private Fragment monitorFragment;
    private Fragment dateFragment;
    private Fragment meFragment;
    private int lastShowFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Toolbar toolbar;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private Fragment dateFragmentOne;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();


    }

    private void initView() {
        bottomNavigation = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
        ActionBar actionBar = getSupportActionBar();
        initFragment();

    }

    private void initFragment() {
        kebiaoFragment = new KebiaoFragment();
        dateFragment = new DateFragment();
        monitorFragment = new MonitorFragment();
        meFragment = new MeFragment();
        lastShowFragment = 0;

        dateFragmentOne = new DateFragmentOne();
//        fragments = new Fragment[]{kebiaoFragment,dateFragment,monitorFragment,meFragment};
        fragments = new Fragment[]{kebiaoFragment,dateFragmentOne,monitorFragment,meFragment};
        getSupportFragmentManager().beginTransaction()
                .add(R.id.content_panel,fragments[0])
                .show(fragments[0])
                .commit();
    }

    //监听底部导航栏tab
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.action_kebiao:
                if (lastShowFragment!=0){
                    switchFragment(0);
                }
                lastShowFragment = 0;
                break;
            case R.id.action_date:
                if (lastShowFragment!=1){
                    switchFragment(1);
                }
                lastShowFragment = 1;
                break;
            case R.id.action_monitor:
                if (lastShowFragment!=2){
                    switchFragment(2);
                }
                lastShowFragment = 2;
                break;
            case R.id.action_me:
                if (lastShowFragment!=3){
                    switchFragment(3);
                }
                lastShowFragment = 3;
                break;
                default:
                    break;
        }
        return true;
    }
    //隐藏或显示fragment
    private void switchFragment( int position) {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        for(int i=0;i<4;i++){
            transaction.hide(fragments[i]);
        }

        if (!fragments[position].isAdded()){
            transaction.add(R.id.content_panel,fragments[position]);
        }
        transaction.show(fragments[position]).commitAllowingStateLoss();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }

    private static boolean isExit = false;  //表示是否退出

    private static Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isExit = false;
        }
    };

    //监听back键
    @Override
    public void onBackPressed() {
        exit();
    }
    private void exit() {
        if (!isExit){
            isExit = true;
            Toast.makeText(MainActivity.this,"再按一次后退键退出程序",Toast.LENGTH_SHORT).show();
            mHandler.sendEmptyMessageDelayed(0,2000);
        }else{
            this.finish();
        }
    }
}
