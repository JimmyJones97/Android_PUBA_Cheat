package com.tsml.hkl.view;
/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 *
 */

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.tsml.hkl.MainService;
import com.tsml.hkl.Toast.T;
import com.tsml.hkl.Utils.MyUtils;
import com.tsml.hkl.enty.AppData;
import com.tsml.hkl.R;

@SuppressWarnings("all")
public class SetView extends FrameLayout implements View.OnClickListener, View.OnTouchListener, SeekBar.OnSeekBarChangeListener, CompoundButton.OnCheckedChangeListener {

    private WindowManager windowManager = null;
    private WindowManager.LayoutParams param1 = null;
    TextView viptime;
    LinearLayout cont;
    ImageView im;
    boolean isvib = false; //控制显示状态
    Context context;
    Switch sw;   //透视运行开关
    TextView tefps;

    public SetView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View toucherLayout1 = inflater.inflate(R.layout.window, null);
        SeekBar seekbar_nomal = toucherLayout1.findViewById(R.id.seekbar_nomal);
        SeekBar seekbar_nomab = toucherLayout1.findViewById(R.id.seekbar_nomab);
        SeekBar seekBar = toucherLayout1.findViewById(R.id.seekbar_fps);
        tefps = toucherLayout1.findViewById(R.id.tefps);
         viptime = toucherLayout1.findViewById(R.id.viptime);
        viptime.setText("到期时间:" + AppData.vipTime);

        int x = MainService.example.dataSave.getInt("x");
        int y = MainService.example.dataSave.getInt("y");
        int fps = MainService.example.dataSave.getInt("fps");

        seekbar_nomal.setProgress(x == -1 ? 200 : 200 + x);
        seekbar_nomab.setProgress(y == -1 ? 200 : 200 + y);
        seekBar.setProgress(fps == -1 ? 50 : 100 - fps);

        AppData.x = x == -1 ? 0 : x;
        AppData.y = y == -1 ? 0 : y;
        AppData.fps = fps == -1 ? 50 : fps;

        tefps.setText(isfps(AppData.fps));

        seekBar.setOnSeekBarChangeListener(this);
        ((Switch) toucherLayout1.findViewById(R.id.swfps)).setOnCheckedChangeListener(this);
        ((Switch) toucherLayout1.findViewById(R.id.swline)).setOnCheckedChangeListener(this);
        sw = toucherLayout1.findViewById(R.id.swfk);

        ((Switch) toucherLayout1.findViewById(R.id.swm)).setOnCheckedChangeListener(this);

        sw.setOnCheckedChangeListener(this);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        param1 = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            param1.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {//小于安卓8.0
            param1.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;

        }

        param1.format = PixelFormat.RGBA_8888;
        param1.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        param1.gravity = Gravity.LEFT | Gravity.TOP;
        param1.x = 100;
        param1.y = 300;
        param1.width = MyUtils.dp2px(context, 30);
        param1.height = MyUtils.dp2px(context, 30);
        windowManager.addView(this, param1);

        Button wei = toucherLayout1.findViewById(R.id.wei);


        im = toucherLayout1.findViewById(R.id.imageq);
        cont = toucherLayout1.findViewById(R.id.contor);
        im.setOnTouchListener(this);
        wei.setOnClickListener(this);


        seekbar_nomal.setOnSeekBarChangeListener(this);
        seekbar_nomab.setOnSeekBarChangeListener(this);
        getWH();
        addView(toucherLayout1);

    }


    public void setParams(WindowManager.LayoutParams param1) {
        this.param1 = param1;
    }

    private String isfps(int i) {
        if (i >= 0 && i <= 20) {
            return "(极高-不建议)";
        } else if (i > 20 && i <= 50) {
            return "(高)";
        } else if (i > 50 && i <= 70) {
            return "(中)";
        } else if (i > 70 && i <= 100) {
            return "(低)";
        }
        return "(低)";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wei://关闭
                MainService.example.stopAll();
            default:
                break;
        }
    }

    public void updateViewPosition() {

        int x = param1.x + (int) (moveX);
        int y = param1.y + (int) (moveY);
        if (x > AppData.height) {
            x = (int) AppData.height;
        }
        if (x < 0) {
            x = 0;
        }
        if (y > AppData.width) {
            y = (int) AppData.width;
        }
        if (y < 0) {
            y = 0;
        }
        param1.x = x;
        param1.y = y;
        windowManager.updateViewLayout(this, param1);
    }



    public void getWH() {
        Display display = windowManager.getDefaultDisplay();
        Point outPoint = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 可能有虚拟按键的情况
            display.getRealSize(outPoint);
        } else {
            // 不可能有虚拟按键
            display.getSize(outPoint);
        }
        AppData.height = outPoint.y;
        AppData.width = outPoint.x;
        AppData.floor = outPoint.x / 2 - 1;
    }


    float downX, downY, xx, yy;
    float moveX, moveY;
    private boolean ismovecl;

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (view.getId()) {
            case R.id.imageq:
                switch (motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        xx = downX = motionEvent.getRawX();
                        yy = downY = motionEvent.getRawY();
                        ismovecl = false;
                    case MotionEvent.ACTION_MOVE:
                        ismovecl = true;
                        moveX = motionEvent.getRawX() - downX;
                        moveY = motionEvent.getRawY() - downY;
                        downX += moveX;
                        downY += moveY;
                        updateViewPosition();
                        break;
                    case MotionEvent.ACTION_UP:
                        if ((xx == downX) && (yy == downY)) {
                            if (!isvib) {
                                param1.width = MyUtils.dp2px(context, 215);
                                param1.height = MyUtils.dp2px(context, 300);
                                cont.setVisibility(View.VISIBLE);
                                isvib = true;
                                windowManager.updateViewLayout(this, param1);

                            } else {

                                cont.setVisibility(View.GONE);
                                param1.width = MyUtils.dp2px(context, 30);
                                param1.height = MyUtils.dp2px(context, 30);
                                isvib = false;
                                windowManager.updateViewLayout(this, param1);
                            }
                        }
                }
        }
        return ismovecl;
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar.getId() == R.id.seekbar_nomal) {

            AppData.x = progress - 200;

        } else if (seekBar.getId() == R.id.seekbar_nomab) {

            AppData.y = progress - 200;

        } else if (seekBar.getId() == R.id.seekbar_fps) {

            AppData.fps = 100 - progress;

            tefps.setText(isfps(AppData.fps));
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

        if (seekBar.getId() == R.id.seekbar_nomal) {

            MainService.example.dataSave.saveInt("x", AppData.x);

        } else if (seekBar.getId() == R.id.seekbar_nomab) {

            MainService.example.dataSave.saveInt("y", AppData.y);

        } else if (seekBar.getId() == R.id.seekbar_fps) {

            MainService.example.dataSave.saveInt("fps", AppData.fps);

        }
    }

    public void removeView() {
        windowManager.removeView(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (buttonView.getId() == R.id.swfk) {
            AppData.isK = isChecked;

            if (isChecked) {
                MainService.example.setRun();
            } else {
                MainService.example.stopRun();

            }
        } else if (buttonView.getId() == R.id.swline) {
            AppData.isLine = isChecked;
        } else if (buttonView.getId() == R.id.swm) {
            AppData.isM = isChecked;
        } else if (buttonView.getId() == R.id.swfps) {
            AppData.isFps = isChecked;
        }
    }


}