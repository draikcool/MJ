package com.example.hqb98.mj.fragment;

import android.app.Instrumentation;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hqb98.mj.R;
import com.example.hqb98.mj.data.Corse;
import com.example.hqb98.mj.data.InfoCourse;
import com.example.hqb98.mj.data.Information;
import com.example.hqb98.mj.util.HttpUtil;
import com.google.gson.Gson;
import org.litepal.LitePal;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;


public class KebiaoFragment extends Fragment implements View.OnClickListener {
    private View view;
    private RelativeLayout relativeLayouts[];
    private LinearLayout linearLayout0;
    private ArrayList<Corse> corses;
    private ArrayList<Integer> digital = new ArrayList<>();
    private ImageView more;
    private View pop;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private ProgressBar progressBar;
    private List<Corse> corses1 = new ArrayList<>();
    private TextView kebiao_view;
    private Calendar calendar = Calendar.getInstance();
    private ListView listView1;
    private int windowWidth;
    private int windowHeight;
    private DisplayMetrics displayMetrics;
    private Display display;
    private WindowManager windowManager;
    Integer[] ids = new Integer[]{R.id.kebiao_semester_change_1,R.id.kebiao_semester_change_2,R.id.kebiao_semester_change_3,R.id.kebiao_semester_change_4,
            R.id.kebiao_semester_change_5,R.id.kebiao_semester_change_6,R.id.kebiao_semester_change_7,R.id.kebiao_semester_change_8,R.id.kebiao_semester_change_9};
    Button[] buttons = new Button[9];

    public static final int SEND_REQUEST = 123;
    Handler handler1 = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SEND_REQUEST:
                    sendReqest();
                    break;
            }
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        windowManager = getActivity().getWindowManager();
        display = windowManager.getDefaultDisplay();
        displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        windowHeight = displayMetrics.heightPixels;
        windowWidth = displayMetrics.widthPixels;
        Log.d("windowsize",windowWidth+"kuan"+windowHeight+"gao");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.kebiao_layout,container,false);
        addKebiaoLayout();
        LitePal.getDatabase();
        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        editor = preferences.edit();
        progressBar = (ProgressBar)view.findViewById(R.id.kebiao_progressbar);
        progressBar.setVisibility(View.GONE);
        initView();
        return view;
    }

    private void initView() {
        corses = new ArrayList<Corse>();
        corses.addAll(LitePal.findAll(Corse.class));
        if (corses.size()!=0){
            choose_week(corses,6);

        }else {
            sendReqest();
        }
        more = (ImageView)view.findViewById(R.id.kebiao_more);
        more.setOnClickListener(this);
        kebiao_view = (TextView)view.findViewById(R.id.kebiao_view);
        kebiao_view.setText("第"+getWeeks()+"周");
        kebiao_view.setOnClickListener(this);
        pop = getActivity().getLayoutInflater().inflate(R.layout.kebiao_popup,null);
        String[] data = {"导入课表","更改学期","选择周次"};
        listView1 = (ListView)pop.findViewById(R.id.kebiao_more_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(pop.getContext(),android.R.layout.simple_list_item_1,data);
        listView1.setAdapter(adapter);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        sendReqest();
                        kebiao_view.setText("第"+getWeeks()+"周");
                        break;
                    case 1:
                        final LinearLayout popup = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.kebiao_semester,null);
                        for (int i=0;i<9;i++){
                            buttons[i] = (Button)popup.findViewById(ids[i]);
                            buttons[i].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    switch (v.getId()){
                                        case R.id.kebiao_semester_change_1:
                                            setSelect();
                                            buttons[0].setSelected(true);
                                            break;
                                        case R.id.kebiao_semester_change_2:
                                            setSelect();
                                            buttons[1].setSelected(true);
                                            break;
                                        case R.id.kebiao_semester_change_3:
                                            setSelect();
                                            buttons[2].setSelected(true);
                                            break;
                                        case R.id.kebiao_semester_change_4:
                                            setSelect();
                                            buttons[3].setSelected(true);
                                            break;
                                        case R.id.kebiao_semester_change_5:
                                            setSelect();
                                            buttons[4].setSelected(true);
                                            break;
                                        case R.id.kebiao_semester_change_6:
                                            setSelect();
                                            buttons[5].setSelected(true);
                                            break;
                                        case R.id.kebiao_semester_change_7:
                                            setSelect();
                                            buttons[6].setSelected(true);
                                            break;
                                        case R.id.kebiao_semester_change_8:
                                            setSelect();
                                            buttons[7].setSelected(true);
                                            break;
                                        case R.id.kebiao_semester_change_9:
                                            setSelect();
                                            buttons[8].setSelected(true);
                                            break;
                                    }
                                }
                            });
                        }
                        final AlertDialog.Builder dialog= new AlertDialog.Builder(getContext());
                        dialog.setView(popup)
                                .create()
                                .show();
                        Button ok = (Button)popup.findViewById(R.id.kebiao_semester_change_ok);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (buttons[0].isSelected()){
                                    editor.putString("semester","2014-2015-1");
                                    editor.apply();
                                }else if (buttons[1].isSelected()){
                                    editor.putString("semester","2014-2015-2");
                                    editor.apply();
                                }else if (buttons[2].isSelected()){
                                    editor.putString("semester","2015-2016-1");
                                    editor.apply();
                                }else if (buttons[3].isSelected()){
                                    editor.putString("semester","2015-2016-2");
                                    editor.apply();
                                }else if (buttons[4].isSelected()){
                                    editor.putString("semester","2016-2017-1");
                                    editor.apply();
                                }else if (buttons[5].isSelected()){
                                    editor.putString("semester","2016-2017-2");
                                    editor.apply();
                                }else if (buttons[6].isSelected()){
                                    editor.putString("semester","2017-2018-1");
                                    editor.apply();
                                }else if (buttons[7].isSelected()){
                                    editor.putString("semester","2017-2018-2");
                                    editor.apply();
                                }else if (buttons[8].isSelected()){
                                    editor.putString("semester","2018-2019-1");
                                    editor.apply();
                                }
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Instrumentation inst = new Instrumentation();
                                        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                                        Message message = new Message();
                                        message.what = 123;
                                        handler1.sendMessage(message);

                                    }
                                }).start();



                            }
                        });
                        final Button cancel = (Button)popup.findViewById(R.id.kebiao_semester_change_cancel);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Instrumentation inst = new Instrumentation();
                                        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                                    }
                                }).start();
                            }
                        });
                        break;
                    case 2:
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                        builder1.setTitle("请选择周次");
                        String[] weekss = getResources().getStringArray(R.array.weeks);
                        builder1.setItems(weekss, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                choose_week(corses,which+1);
                                kebiao_view.setText("第"+(which+1)+"周");
                                dialog.dismiss();
                            }
                        });
                        builder1.create().show();
                        break;
                }
            }
        });

    }

    private String getWeeks() {
        String week = new String();
        int day = 0;
        day = Integer.parseInt(getTwoDay(calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH),getResources().getString(R.string.semesteDate)))-1;
        if (day%7==0){
            week = (int)(day/7)+"";
        }else {
            week = (int)(day/7)+1+"";
        }
        if (Integer.parseInt(week)<0||Integer.parseInt(week)>20){
            week=1+"";
        }

        return week;
    }
    /**
     * 得到二个日期间的间隔天数
     */
    public static String getTwoDay(String sj1, String sj2) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        long day = 0;
        try {
            java.util.Date date = myFormatter.parse(sj1);
            java.util.Date mydate = myFormatter.parse(sj2);
            day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        } catch (Exception e) {
            return "";
        }
        return day + "";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.kebiao_more:
                int result = 0;
                int resourceId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
                if (resourceId > 0){
                    result = getContext().getResources().getDimensionPixelSize(resourceId);
                }
                final PopupWindow popupWindow = new PopupWindow(pop,600,400);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAtLocation(more,Gravity.END|Gravity.TOP,0,result);
                break;
            case R.id.kebiao_view:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setTitle("请选择周次");
                String[] weekss = getResources().getStringArray(R.array.weeks);
                builder1.setItems(weekss, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        choose_week(corses,which+1);
                        kebiao_view.setText("第"+(which+1)+"周");
                        dialog.dismiss();
                    }
                });
                builder1.create().show();
                break;
            case R.id.kebiao_semester_change_1:
                setSelect();
                buttons[0].setSelected(true);
                break;
            case R.id.kebiao_semester_change_2:
                setSelect();
                buttons[1].setSelected(true);
                break;
            case R.id.kebiao_semester_change_3:
                setSelect();
                buttons[2].setSelected(true);
                break;
            case R.id.kebiao_semester_change_4:
                setSelect();
                buttons[3].setSelected(true);
                break;
            case R.id.kebiao_semester_change_5:
                setSelect();
                buttons[4].setSelected(true);
                break;
            case R.id.kebiao_semester_change_6:
                setSelect();
                buttons[5].setSelected(true);
                break;
            case R.id.kebiao_semester_change_7:
                setSelect();
                buttons[6].setSelected(true);
                break;
            case R.id.kebiao_semester_change_8:
                setSelect();
                buttons[7].setSelected(true);
                break;
            case R.id.kebiao_semester_change_9:
                setSelect();
                buttons[8].setSelected(true);
                break;

        }
    }

    private void setSelect(){
        for (int j=0;j<9;j++){
            buttons[j].setSelected(false);
        }
    }

    private void sendReqest(){
//        progressBar.setVisibility(View.VISIBLE);
        String account = preferences.getString("account","");
        String password = preferences.getString("password","");
        String semester = preferences.getString("semester","2018-2019-1");
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("努力加载中，请稍后~");
        builder.create().show();
        HttpUtil.getCourseRequest(account,password,semester ,new okhttp3.Callback(){

            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("getCourse",e.toString());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getContext(),"网络出现了一点小问题哦~",Toast.LENGTH_SHORT).show();

                    }
                });
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                Log.d("getCourse",responseData);
                Gson gson = new Gson();
                Information information = gson.fromJson(responseData,Information.class);
                int code = information.getCode();
                if (code==200) {
                    LitePal.deleteAll(Corse.class);
                    final List<InfoCourse> courseList = information.getInfo();
                    setData(courseList);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int count = preferences.getInt("weekNum",1);
                            choose_week(corses,count);
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Instrumentation inst = new Instrumentation();
                                    inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
                                }
                            }).start();
//                            progressBar.setVisibility(View.GONE);
                        }
                    });

                }else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Toast.makeText(getContext(),"课表查询失败,查询课表失败",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
    }

    private void choose_week(List<Corse> corses,int count){
        corses1.clear();
        for (int j=0;j<corses.size();j++){
            String weeks = corses.get(j).getCorseWeek();
            if (weeks.length()>3){
                String week_info = weeks.substring(0,weeks.length()-3);
                String[] ws = week_info.split(",");
                List<Integer> week = new ArrayList<>();
                for (int i=0;i<ws.length;i++){
                    String[] strings = ws[i].split("-");
                    if(strings.length%2==0){
                        int fdigit = Integer.parseInt(strings[0]);
                        int ldigit = Integer.parseInt(strings[1]);
                        for (int x=fdigit;x<=ldigit;x++){
                            week.add(x);
                        }
                    }else {
                        week.add(Integer.parseInt(strings[0]));
                    }
                }
                for (int i=0;i<week.size();i++){
                    if (count == week.get(i)){
                        corses1.add(corses.get(j));
                    }
                }
            }
        }
        addView(corses1);
    }

    //给每个Relative添加显示课表的view
    private void addView(final List<Corse> corseList) {

        for (int j=0;j<relativeLayouts.length;j++){
            relativeLayouts[j].removeAllViews();
        }
        for (int i=0;i<corseList.size();i++){
            int section =  corseList.get(i).getSection();
            int weekday = corseList.get(i).getWeekDay();
            int d = section*7+weekday;
            digital.clear();
            if (!digital.contains(d)){
                digital.add(d);
                String str = corseList.get(i).getCorseName()+"@"+corseList.get(i).getClassRoom();
                String string=str.replace("@null","");
                final TextView textView = new TextView(relativeLayouts[d-1].getContext());
                textView.setId(i);
                textView.setText(string);
                textView.setTextSize(12);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
                params.setMargins(2,3,2,3);
                textView.setGravity(Gravity.CENTER);
                textView.setBackground(getResources().getDrawable(R.drawable.shape));
                textView.setLayoutParams(params);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int i = v.getId();
                        TableLayout kebiao_dialog = (TableLayout)getActivity().getLayoutInflater().inflate(R.layout.kebiao_detail,null);
                        TextView textView1 = (TextView)kebiao_dialog.findViewById(R.id.detail_kch);
                        TextView textView2 = (TextView)kebiao_dialog.findViewById(R.id.detail_kcjs);
                        TextView textView3 = (TextView)kebiao_dialog.findViewById(R.id.detail_js);
                        TextView textView4 = (TextView)kebiao_dialog.findViewById(R.id.detail_zc);
                        textView1.setText(corseList.get(i).getCorseNumber());
                        textView2.setText(corseList.get(i).getTeacherName());
                        textView3.setText(corseList.get(i).getClassRoom());
                        textView4.setText(corseList.get(i).getCorseWeek());
                        new AlertDialog.Builder(getActivity())
                                .setTitle(corseList.get(i).getCorseName())
                                .setView(kebiao_dialog)
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                })
                                .create()
                                .show();

                    }
                });
                relativeLayouts[d-1].addView(textView);
            }

        }

    }
    //获取获取课表信息
    private void setData(List<InfoCourse> courseList) {
        List<InfoCourse> info = courseList;
        corses.clear();
        for (int i = 0; i < info.size() - 1; i++) {
            InfoCourse info1 = info.get(i);
            for (int j = 0;j<7;j++){
                getdata(i,j,info1);
            }
        }
    }
    //精确课表信息
    private void getdata(int section,int day, InfoCourse info){
        int weekday;
        if (info.getMonday()!=null&&day==0) {
            String string = info.getMonday();
            weekday = 1;
            String s = string.replace("---------------------", "@");
            String[] strings = s.split("@");
            if_two(section,weekday,strings);
        }
        if (info.getTuesday()!=null&&day==1) {
            String string = info.getTuesday();
            weekday = 2;
            String s = string.replace("---------------------", "@");
            String[] strings = s.split("@");
            if_two(section,weekday,strings);
        }
        if (info.getWednesday()!=null&&day==2) {
            String string = info.getWednesday();
            weekday = 3;
            String s = string.replace("---------------------", "@");
            String[] strings = s.split("@");
            if_two(section,weekday,strings);
        }
        if (info.getThursday()!=null&&day==3) {
            String string = info.getThursday();
            weekday = 4;
            String s = string.replace("---------------------", "@");
            String[] strings = s.split("@");
            if_two(section,weekday,strings);
        }
        if (info.getFriday()!=null&&day==4) {
            String string = info.getFriday();
            weekday = 5;
            String s = string.replace("---------------------", "@");
            String[] strings = s.split("@");
            if_two(section,weekday,strings);
        }
        if (info.getSaturday()!=null&&day==5) {
            String string = info.getSaturday();
            weekday = 6;
            String s = string.replace("---------------------", "@");
            String[] strings = s.split("@");
            if_two(section,weekday,strings);
        }
        if (info.getSunday()!=null&&day==6) {
            String string = info.getSunday();
            weekday = 7;
            String s = string.replace("---------------------", "@");
            String[] strings = s.split("@");
            if_two(section,weekday,strings);
        }
    }

    private void if_two(int i,int weekday,String[] strings){
        if (strings.length == 10) {
            Corse corse = new Corse();
            corse.setCorseNumber(strings[0]);
            corse.setCorseName(strings[1]);
            corse.setTeacherName(strings[2]);
            corse.setCorseWeek(strings[3]);
            corse.setClassRoom(strings[4]);
            corse.setSection(i);
            corse.setWeekDay(weekday);
            Corse corse1 = new Corse();
            corse1.setCorseNumber(strings[5]);
            corse1.setCorseName(strings[6]);
            corse1.setTeacherName(strings[7]);
            corse1.setCorseWeek(strings[8]);
            corse1.setClassRoom(strings[9]);
            corse1.setSection(i);
            corse1.setWeekDay(weekday);
            corses.add(corse);
            corses.add(corse1);
            corse.save();
            corse1.save();
        } else if (strings.length==4){
            Corse corse = new Corse();
            corse.setCorseNumber(strings[0]);
            corse.setCorseName(strings[1]);
            corse.setTeacherName(strings[2]);
            corse.setCorseWeek(strings[3]);
            corse.setSection(i);
            corse.setWeekDay(weekday);
            corses.add(corse);
            corse.save();
        }else if (strings.length==5){
            Corse corse = new Corse();
            corse.setCorseNumber(strings[0]);
            corse.setCorseName(strings[1]);
            corse.setTeacherName(strings[2]);
            corse.setCorseWeek(strings[3]);
            corse.setClassRoom(strings[4]);
            corse.setSection(i);
            corse.setWeekDay(weekday);
            corses.add(corse);
            corse.save();
        }
        else if (strings.length==8){
            Corse corse = new Corse();
            corse.setCorseNumber(strings[0]);
            corse.setCorseName(strings[1]);
            corse.setTeacherName(strings[2]);
            corse.setCorseWeek(strings[3]);
            corse.setSection(i);
            corse.setWeekDay(weekday);
            Corse corse1 = new Corse();
            corse1.setCorseNumber(strings[4]);
            corse1.setCorseName(strings[5]);
            corse1.setTeacherName(strings[6]);
            corse1.setCorseWeek(strings[7]);
            corse1.setSection(i);
            corse1.setWeekDay(weekday);
            corses.add(corse);
            corses.add(corse1);
            corse.save();
            corse1.save();
        }
        else if (strings.length==15){
            Corse corse = new Corse();
            corse.setCorseNumber(strings[0]);
            corse.setCorseName(strings[1]);
            corse.setTeacherName(strings[2]);
            corse.setCorseWeek(strings[3]);
            corse.setClassRoom(strings[4]);
            corse.setSection(i);
            corse.setWeekDay(weekday);
            Corse corse1 = new Corse();
            corse1.setCorseNumber(strings[5]);
            corse1.setCorseName(strings[6]);
            corse1.setTeacherName(strings[7]);
            corse1.setCorseWeek(strings[8]);
            corse1.setClassRoom(strings[9]);
            corse1.setSection(i);
            corse1.setWeekDay(weekday);
            Corse corse2 = new Corse();
            corse2.setCorseNumber(strings[10]);
            corse2.setCorseName(strings[11]);
            corse2.setTeacherName(strings[12]);
            corse2.setCorseWeek(strings[13]);
            corse2.setClassRoom(strings[14]);
            corse2.setSection(i);
            corse2.setWeekDay(weekday);
            corses.add(corse);
            corses.add(corse1);
            corses.add(corse2);
            corse.save();
            corse1.save();
            corse2.save();
        }
    }

    private void addKebiaoSideBar(){
        int height = windowHeight/4;
        int width = 35;
        linearLayout0 = (LinearLayout)view.findViewById(R.id.line0);
        RelativeLayout relativeLayout1 = new RelativeLayout(linearLayout0.getContext());
        RelativeLayout relativeLayout2 = new RelativeLayout(getContext());
        RelativeLayout relativeLayout3 = new RelativeLayout(getContext());
        RelativeLayout relativeLayout4 = new RelativeLayout(getContext());
        RelativeLayout relativeLayout5 = new RelativeLayout(getContext());
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)linearLayout0.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        relativeLayout1.setLayoutParams(layoutParams);
        relativeLayout1.setBackgroundColor(getResources().getColor(R.color.black_word));
        linearLayout0.addView(relativeLayout1);
    }

    private int dip2px(Context context,float dipValue){
        Resources resources = context.getResources();
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dipValue,resources.getDisplayMetrics());
    }
    private void addKebiaoLayout() {
        relativeLayouts = new RelativeLayout[35];
        relativeLayouts[0] = (RelativeLayout)view.findViewById(R.id.view1);
        relativeLayouts[1] = (RelativeLayout)view.findViewById(R.id.view2);
        relativeLayouts[2] = (RelativeLayout)view.findViewById(R.id.view3);
        relativeLayouts[3] = (RelativeLayout)view.findViewById(R.id.view4);
        relativeLayouts[4] = (RelativeLayout)view.findViewById(R.id.view5);
        relativeLayouts[5] = (RelativeLayout)view.findViewById(R.id.view6);
        relativeLayouts[6] = (RelativeLayout)view.findViewById(R.id.view7);
        relativeLayouts[7] = (RelativeLayout)view.findViewById(R.id.view8);
        relativeLayouts[8] = (RelativeLayout)view.findViewById(R.id.view9);
        relativeLayouts[9] = (RelativeLayout)view.findViewById(R.id.view10);
        relativeLayouts[10] = (RelativeLayout)view.findViewById(R.id.view11);
        relativeLayouts[11] = (RelativeLayout)view.findViewById(R.id.view12);
        relativeLayouts[12] = (RelativeLayout)view.findViewById(R.id.view13);
        relativeLayouts[13] = (RelativeLayout)view.findViewById(R.id.view14);
        relativeLayouts[14] = (RelativeLayout)view.findViewById(R.id.view15);
        relativeLayouts[15] = (RelativeLayout)view.findViewById(R.id.view16);
        relativeLayouts[16] = (RelativeLayout)view.findViewById(R.id.view17);
        relativeLayouts[17] = (RelativeLayout)view.findViewById(R.id.view18);
        relativeLayouts[18] = (RelativeLayout)view.findViewById(R.id.view19);
        relativeLayouts[19] = (RelativeLayout)view.findViewById(R.id.view20);
        relativeLayouts[20] = (RelativeLayout)view.findViewById(R.id.view21);
        relativeLayouts[21] = (RelativeLayout)view.findViewById(R.id.view22);
        relativeLayouts[22] = (RelativeLayout)view.findViewById(R.id.view23);
        relativeLayouts[23] = (RelativeLayout)view.findViewById(R.id.view24);
        relativeLayouts[24] = (RelativeLayout)view.findViewById(R.id.view25);
        relativeLayouts[25] = (RelativeLayout)view.findViewById(R.id.view26);
        relativeLayouts[26] = (RelativeLayout)view.findViewById(R.id.view27);
        relativeLayouts[27] = (RelativeLayout)view.findViewById(R.id.view28);
        relativeLayouts[28] = (RelativeLayout)view.findViewById(R.id.view29);
        relativeLayouts[29] = (RelativeLayout)view.findViewById(R.id.view30);
        relativeLayouts[30] = (RelativeLayout)view.findViewById(R.id.view31);
        relativeLayouts[31] = (RelativeLayout)view.findViewById(R.id.view32);
        relativeLayouts[32] = (RelativeLayout)view.findViewById(R.id.view33);
        relativeLayouts[33] = (RelativeLayout)view.findViewById(R.id.view34);
        relativeLayouts[34] = (RelativeLayout)view.findViewById(R.id.view35);
    }



}
