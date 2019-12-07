package com.tsml.hkl.view;


import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.tsml.hkl.R;
import com.tsml.hkl.Toast.T;
import com.tsml.hkl.enty.AppData;

@SuppressWarnings("all")
public class MoveView extends SurfaceView implements SurfaceHolder.Callback {
    private WindowManager windowManager = null;
    private WindowManager.LayoutParams param1 = null;
    Context context;
    Paint paint_green, paint_red, textpaint_green, textpaintread, testPain;
    Paint thisPaint, thisTextPaint,hpPint;
    SurfaceHolder holder;
    File path = new File("/sdcard/b.log");

    public MoveView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MoveView(Context context) {
        super(context);
        this.context = context;
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        param1 = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            param1.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1) {
            param1.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        }
        getWH();
        param1.format = PixelFormat.RGBA_8888;
        param1.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        param1.gravity = Gravity.LEFT | Gravity.TOP;
        param1.x = 0;
        param1.y = 0;
        param1.width = (int) AppData.width;
        param1.height = (int) AppData.height;

        windowManager.addView(this, param1);
        paint_green = new Paint();
        paint_green.setStyle(Paint.Style.STROKE);
        paint_green.setStrokeWidth(3);
        paint_green.setAntiAlias(true);
        paint_green.setColor(Color.GREEN);

        paint_red = new Paint();
        paint_red.setStyle(Paint.Style.STROKE);
        paint_red.setStrokeWidth(3);
        paint_red.setAntiAlias(true);
        paint_red.setColor(Color.RED);


        thisPaint = paint_green;
        thisTextPaint = textpaint_green;


        textpaint_green = new Paint();
        textpaint_green.setTextSize(32);
        textpaint_green.setAntiAlias(true);
        textpaint_green.setColor(Color.GREEN);

        textpaintread = new Paint();
        textpaintread.setTextSize(32);
        textpaintread.setAntiAlias(true);
        textpaintread.setColor(Color.RED);

        hpPint = new Paint();
        hpPint.setAntiAlias(true);
        hpPint.setColor(Color.RED);
        hpPint.setStrokeWidth(10);

        testPain = new Paint();
        testPain.setTextSize(50);
        testPain.setAntiAlias(true);
        testPain.setColor(Color.RED);

        holder = this.getHolder();
        holder.addCallback(this);

        /*画布背景透明*/
        setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSLUCENT);

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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getWH();
        param1.width = (int) AppData.width;
        param1.height = (int) AppData.height;
        T.ToastInfo(AppData.width);
        uodateView();
    }

    public void uodateView() {
        windowManager.updateViewLayout(this, param1);
    }

    public void removeView() {
        windowManager.removeView(this);
    }

    BufferedReader br = null;
    int t_fps = 0;
    int f_fps = 0;
    long time = System.currentTimeMillis();
    int hs = 0;

    public void draw() {
        Canvas canvas = null;
        synchronized (holder) {
            try {
                canvas = holder.lockCanvas();
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                String hero;

                br = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));

                while ((hero = br.readLine()) != null) {
                    try {
                        if (hero == null || hero.equals("")) {
                            continue;
                        }
                        String[] split = hero.split(",");

                        int fx = Double.valueOf(split[0]).intValue();
                        int fy = Double.valueOf(split[1]).intValue();
                        int fw = Double.valueOf(split[2]).intValue();
                        // int fh = Double.valueOf(split[3]).intValue();
                        int m = Double.valueOf(split[4]).intValue();
                        int hp = Double.valueOf(split[5]).intValue();

                        if (m >= 2 && m < 450 && fw > 0 ) {
                            if (hp > 0) {
                                if (thisPaint != paint_green) {
                                    thisPaint = paint_green;
                                }
                                if (thisTextPaint != textpaint_green) {
                                    thisTextPaint = textpaint_green;
                                }

                            } else {
                                if (thisPaint != paint_red) {
                                    thisPaint = paint_red;
                                }
                                if (thisTextPaint != textpaintread) {
                                    thisTextPaint = textpaintread;
                                }

                            }
                            if (AppData.isK) {
                                canvas.drawRect(fx - fw / 2 + AppData.x, fy - fw + AppData.y, fx + fw / 2 + AppData.x, fy + fw + AppData.y, thisPaint);
                            }
                            if (AppData.isM) {
                                canvas.drawText(new StringBuffer().append(m).append("M").toString(), fx - 100 + AppData.x, fy + AppData.y - fw - 20, thisTextPaint);
                                String ms;
                                if (hp > 0) {
                                    ms = new StringBuffer().append(hp).append("%").toString();
                                } else {
                                    ms = context.getString(R.string.die);//死亡
                                }
                                int starty = fy + fw + AppData.y;
                                int endy = fy + AppData.y - fw;
                                int hply = starty - endy;
                               /* if (hp > 100) {
                                    hp = 100;
                                }*/
                                hply  = (hp / 100) * (hply / 100) + endy;
                                canvas.drawLine(fx + fw / 2 + AppData.x + 8, fy + fw + AppData.y , fx + fw / 2 + AppData.x + 8, hply , hpPint);
                            }

                            if (AppData.isLine) {
                                canvas.drawLine((int) AppData.floor, 0, fx + AppData.x, fy + AppData.y - fw, thisPaint);
                            }
                            hs += 1;
                        }
                    } catch (Exception e) {
                    }

                }

                if (AppData.isFps) {
                    t_fps++;
                    if (System.currentTimeMillis() - time >= 1000) {
                        f_fps = t_fps;
                        t_fps = 0;
                        time = System.currentTimeMillis();
                    }

                    //人数
                    canvas.drawText(new StringBuffer().append(context.getString(R.string.number)).append(hs).toString(), (float) AppData.floor - 80, 160, testPain);
                    canvas.drawText(new StringBuffer().append("FPS:").append(f_fps).toString(), (float) AppData.floor - 80, 100, testPain);
                }
            } catch (IOException e) {

            } finally {


                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                hs = 0;

                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas);
            }
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {

       /* flag = true;
        new Thread(this).start();*/
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}

