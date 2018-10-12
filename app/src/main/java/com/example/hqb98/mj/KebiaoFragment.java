package com.example.hqb98.mj;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.hqb98.mj.db.Corse;
import com.example.hqb98.mj.gson.Information;

import java.util.ArrayList;

public class KebiaoFragment extends Fragment{
    private static String TAG = "kebiao";
    View view;
    private RelativeLayout relativeLayouts[];
    private Information information;
    private ArrayList<Corse> corses;
    private ArrayList<Integer> digital;
    public static final int UPDATE_VIEW = 1;
    private Corse corse;
    private  TableLayout kebiao_detail;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.kebiao_layout,container,false);
        kebiao_detail = (TableLayout) getLayoutInflater().inflate(R.layout.kebiao_detail,null);
        addKebiaoLayout();
        initView();
        return view;
    }

    private void initView() {
        information = MyApplication.information;
        corses = new ArrayList<Corse>();
        setData(information);

        addView();
    }

    private void addView() {
        digital = new ArrayList<Integer>();
        for (int i=0;i<corses.size();i++){
            int section =  corses.get(i).getSection();
            int weekday = corses.get(i).getWeekDay();
            int d = section*7+weekday;
            if (!digital.contains(d)){
                digital.add(d);
                String str = corses.get(i).getCorseName()+"@"+corses.get(i).getClassRoom();
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
                        TableLayout kebiao_dialog = (TableLayout)getLayoutInflater().inflate(R.layout.kebiao_detail,null);
                        TextView textView1 = (TextView)kebiao_dialog.findViewById(R.id.detail_kch);
                        TextView textView2 = (TextView)kebiao_dialog.findViewById(R.id.detail_kcjs);
                        TextView textView3 = (TextView)kebiao_dialog.findViewById(R.id.detail_js);
                        TextView textView4 = (TextView)kebiao_dialog.findViewById(R.id.detail_zc);
                        textView1.setText(corses.get(i).getCorseNumber());
                        textView2.setText(corses.get(i).getTeacherName());
                        textView3.setText(corses.get(i).getClassRoom());
                        textView4.setText(corses.get(i).getCorseWeek());
                        new AlertDialog.Builder(getActivity())
                                .setTitle(corses.get(i).getCorseName())
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

    private void setData(Information information) {
        ArrayList<Information.Info> info = information.getInfo();
        for (int i = 0; i < info.size() - 1; i++) {
            Information.Info info1 = info.get(i);
            for (int j = 0;j<7;j++){
                getdata(i,j,info1);
            }
        }
    }

    private void getdata(int section,int day, Information.Info info){
        int weekday;
        if ((!info.getMonday().equals(""))&&day==0) {
            String string = info.getMonday();
            weekday = 1;
            String s = string.replace("---------------------", "@");
            String[] strings = s.split("@");
            if_two(section,weekday,strings);
        }
        if ((!info.getTuesday().equals(""))&&day==1) {
            String string = info.getTuesday();
            weekday = 2;
            String s = string.replace("---------------------", "@");
            String[] strings = s.split("@");
            if_two(section,weekday,strings);
        }
        if ((!info.getWednesday().equals(""))&&day==2) {
            String string = info.getWednesday();
            weekday = 3;
            String s = string.replace("---------------------", "@");
            String[] strings = s.split("@");
            if_two(section,weekday,strings);
        }
        if ((!info.getThursday().equals(""))&&day==3) {
            String string = info.getThursday();
            weekday = 4;
            String s = string.replace("---------------------", "@");
            String[] strings = s.split("@");
            if_two(section,weekday,strings);
        }
        if ((!info.getFriday().equals(""))&&day==4) {
            String string = info.getFriday();
            weekday = 5;
            String s = string.replace("---------------------", "@");
            String[] strings = s.split("@");
            if_two(section,weekday,strings);
        }
        if ((!info.getSaturday().equals(""))&&day==5) {
            String string = info.getSaturday();
            weekday = 6;
            String s = string.replace("---------------------", "@");
            String[] strings = s.split("@");
            if_two(section,weekday,strings);
        }
        if ((!info.getSunday().equals(""))&&day==6) {
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
        } else if (strings.length==4){
            Corse corse = new Corse();
            corse.setCorseNumber(strings[0]);
            corse.setCorseName(strings[1]);
            corse.setTeacherName(strings[2]);
            corse.setCorseWeek(strings[3]);
            corse.setSection(i);
            corse.setWeekDay(weekday);
            corses.add(corse);
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
        }
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
