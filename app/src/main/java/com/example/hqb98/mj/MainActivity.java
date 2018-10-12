package com.example.hqb98.mj;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigation;
    private Fragment[] fragments;
    private Fragment kebiaoFragment;
    private Fragment monitorFragment;
    private Fragment dateFragment;
    private int lastShowFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout mDrawerLayout;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();


    }

    private void initView() {
        bottomNavigation = (BottomNavigationView)findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnNavigationItemSelectedListener(this);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_us);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_switchaccount:
                        ActivityCollector.finishAll();
                        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                        startActivityForResult(intent,1);
                        preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
                        editor = preferences.edit();
                        editor.putBoolean("autologin",false);
                        editor.apply();
                        break;
                        default:
                            break;
                }
                return true;
            }
        });
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.menu);
        }
        initFragment();

    }

    private void initFragment() {
        kebiaoFragment = new KebiaoFragment();
        dateFragment = new DateFragment();
        monitorFragment = new MonitorFragment();
        lastShowFragment = 0;
        fragments = new Fragment[]{kebiaoFragment,dateFragment,monitorFragment};
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
                default:
                    break;
        }
        return true;
    }
    //隐藏或显示fragment
    private void switchFragment( int position) {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        for(int i=0;i<3;i++){
            transaction.hide(fragments[i]);
        }

        if (!fragments[position].isAdded()){
            transaction.add(R.id.content_panel,fragments[position]);
        }
        transaction.show(fragments[position]).commitAllowingStateLoss();
    }
    //初始化toolbar上的menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return true;
    }
    int i;
    //给toolbar上的menu添加监听器
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.add_date:
                String[] itemts = new String[]{"记录心情","生活经验","课堂笔记","待办事项"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(this)
                        .setTitle("请选择以下一项")
                        .setSingleChoiceItems(itemts, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0:
                                        i = 0; break;
                                    case 1:
                                        i = 1; break;
                                    case 2:
                                        i = 2; break;
                                    case 3:
                                        i = 3; break;
                                }
                            }
                        });
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.this,CreatDate.class);
                        intent.putExtra("extra_data",i+"");
                        startActivityForResult(intent,1);
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;


        }
        return true;
    }

    /**
     *
     * @param menu
     * @return
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (lastShowFragment){
            case 0:
                menu.findItem(R.id.add_course).setVisible(true);
                menu.findItem(R.id.import_schedule).setVisible(true);
                menu.findItem(R.id.noshow).setVisible(true);
                menu.findItem(R.id.change_semester).setVisible(true);
                menu.findItem(R.id.add_date).setVisible(false);
                invalidateOptionsMenu();
                break;
            case 1:
                menu.findItem(R.id.add_course).setVisible(false);
                menu.findItem(R.id.import_schedule).setVisible(false);
                menu.findItem(R.id.noshow).setVisible(false);
                menu.findItem(R.id.change_semester).setVisible(false);
                menu.findItem(R.id.add_date).setVisible(true);
                invalidateOptionsMenu();
                break;
            case 2:
                menu.findItem(R.id.add_course).setVisible(false);
                menu.findItem(R.id.import_schedule).setVisible(false);
                menu.findItem(R.id.noshow).setVisible(false);
                menu.findItem(R.id.change_semester).setVisible(false);
                menu.findItem(R.id.add_date).setVisible(false);
                invalidateOptionsMenu();
                default:
                    break;
        }

        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode==RESULT_OK){
                    DateFragment dateFragment = (DateFragment)getSupportFragmentManager().findFragmentById(R.id.content_panel);
                    dateFragment.addData();

                }
                break;
        }
    }


}
