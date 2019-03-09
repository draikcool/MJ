package com.example.hqb98.mj.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hqb98.mj.R;
import com.example.hqb98.mj.activity.CreatDate;
import com.example.hqb98.mj.data.Date;
import com.example.hqb98.mj.data.DateHttp;
import com.example.hqb98.mj.util.HttpUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.shizhefei.fragment.LazyFragment;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.slidebar.ScrollBar;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class DateFragmentOne extends LazyFragment implements View.OnClickListener {

    public static final int TOAST = 123;
    public static final int UPDATE_DATE = 1234;
    private View view;
    private Toolbar toolbar;
    private Indicator indicator;
    private ViewPager viewPager;
    private IndicatorViewPager indicatorViewPager;
    private SharedPreferences preferences;
    private ImageView add_date;
    private List<DateHttp> dateHttps = new ArrayList<DateHttp>();
    public static List<Date> dateList = new ArrayList<>();
    private RequestDateReceiver requestDateReceiver;
    private LocalBroadcastManager localBroadcastManager;
    private IntentFilter intentFilter;



    private Fragment fragment1;
    private Fragment fragment2;
    private Fragment fragment3;
    private Fragment fragment4;



    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case TOAST:
                    Toast.makeText(getContext(),"网络出现了一点小问题哦~",Toast.LENGTH_SHORT).show();
                    break;
                case UPDATE_DATE:
                    break;
            }
        }

    };


    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.date_layout_one);
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        initData();
        initView1();
        initEvent();
    }

    private void initView1() {
        indicator = (Indicator)findViewById(R.id.date_indicator);
        viewPager = (ViewPager)findViewById(R.id.date_viewpager);
        //设置tab上的字体和颜色
        Resources resources = getResources();
        float size = 16;
        int selectColor = resources.getColor(R.color.white_word);
        int unSelectColor = resources.getColor(R.color.top_tab_uncheck);
        viewPager.setOffscreenPageLimit(4);
        indicator.setScrollBar(new ColorBar(getContext(),Color.WHITE,5,ScrollBar.Gravity.BOTTOM));
        indicator.setOnTransitionListener(new OnTransitionTextListener().setSize(size,size).setColor(selectColor,unSelectColor));
        indicatorViewPager = new IndicatorViewPager(indicator,viewPager);
        indicatorViewPager.setAdapter(new MyAdapter(getActivity().getSupportFragmentManager(),getContext()));
        add_date = (ImageView)findViewById(R.id.add_date);
        requestDateReceiver = new RequestDateReceiver();
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.hqb98.mj.activity.requestdate");
        localBroadcastManager.registerReceiver(requestDateReceiver,intentFilter);

    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.date_layout_one,container,false);
//        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
//        initData();
//        initView();
//        initEvent();
//
//        return view;
//    }

    private void initData() {
        requestData();
    }

    public  void requestData() {
        String userid = preferences.getString("account","");
        Log.d("getDate",userid);
        HttpUtil.getDateRequest(userid, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("getDate",e.toString());
                Message message = new Message();
                message.what = TOAST;
                handler.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                dateHttps.clear();
                Gson gson = new Gson();
                dateHttps = gson.fromJson(responseData,new TypeToken<List<DateHttp>>(){}.getType());
                if (dateHttps.size()!=0){
                    LitePal.deleteAll(Date.class);
                    dateList.clear();
                    for (int i=0;i<dateHttps.size();i++){
                        Date date = new Date();
                        date.setDate_type(dateHttps.get(i).getType());
                        date.setDate_title(dateHttps.get(i).getTitle());
                        date.setDate_content(dateHttps.get(i).getContent());
                        date.setDate_time(dateHttps.get(i).getDatetime());
                        date.setDate_id(dateHttps.get(i).getId());
                        switch (dateHttps.get(i).getType()){
                            case "记录心情":
                                date.setDate_image(R.drawable.mood);
                                break;
                            case "生活经验":
                                date.setDate_image(R.drawable.experience);
                                break;
                            case "课堂笔记":
                                date.setDate_image(R.drawable.note);
                                break;
                            case "待办事项":
                                date.setDate_image(R.drawable.backlog);
                                break;
                        }
                        date.save();
                        dateList.add(0,date);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Date_item_fragment_one.refreshData1();
                            if (fragment2!=null){
                                Date_item_fragment_two.refreshData1();
                            }
                            if (fragment3!=null){
                                Date_item_fragment_three.refreshData1();
                            }
                            if (fragment4!=null){
                                Date_item_fragment_four.refreshData1();
                            }

                        }
                    });


//                    Message message = new Message();
//                    message.what = UPDATE_DATE;
//                    handler.sendMessage(message);
                }

            }
        });
    }


    private void initView() {
        indicator = (Indicator)view.findViewById(R.id.date_indicator);
        viewPager = (ViewPager)view.findViewById(R.id.date_viewpager);
        //设置tab上的字体和颜色
        Resources resources = getResources();
        float size = 16;
        int selectColor = resources.getColor(R.color.white_word);
        int unSelectColor = resources.getColor(R.color.tab_uncheck);
        viewPager.setOffscreenPageLimit(4);
        indicator.setScrollBar(new ColorBar(view.getContext(),Color.WHITE,5,ScrollBar.Gravity.BOTTOM));
        indicator.setOnTransitionListener(new OnTransitionTextListener().setSize(size,size).setColor(selectColor,unSelectColor));
        indicatorViewPager = new IndicatorViewPager(indicator,viewPager);
        indicatorViewPager.setAdapter(new MyAdapter(getActivity().getSupportFragmentManager(),getContext()));
        add_date = (ImageView)view.findViewById(R.id.add_date);
        requestDateReceiver = new RequestDateReceiver();
        localBroadcastManager = LocalBroadcastManager.getInstance(getContext());
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.example.hqb98.mj.activity.requestdate");
        localBroadcastManager.registerReceiver(requestDateReceiver,intentFilter);
    }

    private void initEvent() {
        add_date.setOnClickListener(this);

    }

    int i;
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_date:
                String[] itemts = new String[]{"记录心情","生活经验","课堂笔记","待办事项"};
                final AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
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
                        Intent intent = new Intent(getContext(),CreatDate.class);
                        intent.putExtra("extra_data",i+"");
                        startActivityForResult(intent,1);
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if (resultCode==RESULT_OK){
                    Bundle bundle = data.getExtras();
                    String string = bundle.getString("Date_type");

                    if (string.equals("记录心情")){
                        viewPager.setCurrentItem(0);

//                        Date_item_fragment_one.refreshDate();

                        Date_item_fragment_one.refreshData1();
                    }else if (string.equals("生活经验")){

                        viewPager.setCurrentItem(1);
                        Date_item_fragment_two.refreshData1();
                    }else if (string.equals("课堂笔记")){
                        viewPager.setCurrentItem(2);
                        Date_item_fragment_three.refreshData1();
                    }else if (string.equals("待办事项")){
                        viewPager.setCurrentItem(3);
                        Date_item_fragment_four.refreshData1();
                    }

                }
                break;
        }
    }

    private class MyAdapter extends IndicatorViewPager.IndicatorFragmentPagerAdapter {
        private Context context;
        private String[] tabname = {"心情","生活","笔记","待办事项"};

        public MyAdapter(FragmentManager fragmentManager, Context context) {
            super(fragmentManager);
            this.context = context;
        }



        @Override
        public int getCount() {
            return tabname.length;
        }

        @Override
        public View getViewForTab(int position, View convertView, ViewGroup container) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.tab_top, container, false);
            }
            TextView textView = (TextView) convertView;
            textView.setText(tabname[position]);
            return convertView;
        }

        @Override
        public Fragment getFragmentForPage(int position) {
            Fragment fragment;
            switch (position){
                case 0:
                    fragment1 = new Date_item_fragment_one();
                    fragment = fragment1;
                    break;
                case 1:
                    fragment2 = new Date_item_fragment_two();
                    fragment = fragment2;
                    break;
                case 2:
                    fragment3 = new Date_item_fragment_three();
                    fragment = fragment3;
                    break;
                case 3:
                    fragment4 = new Date_item_fragment_four();
                    fragment = fragment4;
                    break;
                    default:
                        return null;

            }

            return fragment;
        }
    }

    public class RequestDateReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            requestData();
        }
    }



}
