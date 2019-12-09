package com.tsml.hkl;

import android.app.*;
import android.content.*;
import android.graphics.Color;
import android.os.*;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Process;
import java.util.List;

import com.tsml.hkl.Toast.T;
import com.tsml.hkl.Toast.ToastS;
import com.tsml.hkl.Utils.DataSave;
import com.tsml.hkl.Utils.KeyUtils;
import com.tsml.hkl.Utils.MyUtils;
import com.tsml.hkl.Utils.ProcessManager;
import com.tsml.hkl.Utils.SettingsCompat;
import com.tsml.hkl.Utils.ShellUtils;
import com.tsml.hkl.Utils.ViewUpdate;
import com.tsml.hkl.enty.AppData;
import com.tsml.hkl.view.MoveView;
import com.tsml.hkl.view.SetView;

import static android.os.Build.VERSION_CODES.LOLLIPOP_MR1;
import static com.tsml.hkl.Utils.MyUtils.writeText;
import static com.tsml.hkl.enty.AppData.SDCRAD;

//@SuppressWarnings("all")
public class MainService extends Service implements Runnable {

    MoveView mo;//绘制服务

    SetView so;//悬浮工具

    public DataSave dataSave;//数据保存

    public static MainService example;//服务实例

    public boolean isrun = false; //是否正在运行绘制


    /**
     * 绑定activity
     *
     * @param intent
     * @return
     */
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }//不与activity绑定

    /**
     * 服务初始化
     */
    @Override
    public void onCreate() {
        super.onCreate();
        if (!SettingsCompat.canDrawOverlays(this)) {
            ToastS.warning(this, getString(R.string.float_window), Toast.LENGTH_LONG);
            SettingsCompat.manageDrawOverlays(this);
        }
        dataSave = new DataSave(MainService.this);
        example = MainService.this;
        AppData.context = MainService.this;

        MyUtils.getAssetsFile("TF_BOAYS", this);//解压数据
        MyUtils.getAssetsFiles(this);//解压数据

        so = new SetView(MainService.this);
        mo = new MoveView(MainService.this);
        //foregroundRun();
        notification();
    }


    /**
     * 服务销毁
     */
    @Override
    public void onDestroy() {
        isrun = false;
        if (mo != null) {
            mo.removeView();
        }
        if (so != null) {
            so.removeView();
        }

        stopRun();
        ShellUtils.execCommand(String.format("rm -f  %s/jkhewrh", MainService.example.getFilesDir().getPath()), true);//删除数据
        example = null;

    }

    /**
     * 开启服务
     *
     * @param context
     */
    public static void showFloat(Context context) {
        if (example == null) {
            Intent intent = new Intent(context, MainService.class);
            context.startService(intent);
        }
    }

    /**
     * 关闭服务
     *
     * @param context
     */
    public static void stopFloat(Context context) {
        if (example == null) {
            Intent intent = new Intent(context, MainService.class);
            context.stopService(intent);
        }
    }

    /**
     * 绘制线程
     */
    @Override
    public void run() {
        while (isrun) {
            long start = System.currentTimeMillis();

            if (mo != null) {
                mo.draw();
            }
            long end = System.currentTimeMillis();
            try {
                if (end - start < AppData.fps) {
                    Thread.sleep(AppData.fps - (end - start));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }


    /**
     * 开启功能
     */
    public void setRun() {
        if (KeyUtils.isTime(AppData.vipTime)) {
            isrun = true;

            new startServer().start();
            new Thread(this).start();
            new G().start();


        } else {
            stopAll();
            T.ToastWarning(getString(R.string.time_expires));
        }


    }

    /**
     * 杀死进程
     */
    public void stopRun() {
        isrun = false;
        ViewUpdate.runThread(() -> {
            final int process = ProcessManager.parseProcessList();
            // T.ToastSuccess(String.format("杀死服务%s个", process));
        });
    }

    /**
     * 开启扫描坐标
     */
    class startServer extends Thread {

        @Override
        public void run() {

            writeText(new File(String.format("%s/%s",
                    Environment.getExternalStorageDirectory().toString(), "b.log")),
                    "0,0,0,0,0,0");

            final String path = String.format("%s/jkhewrh %s %s %s",
                    MainService.example.getFilesDir().getPath(),
                    (AppData.isHX ? AppData.height : AppData.width),
                    (AppData.isHX ? AppData.width : AppData.height),
                    "16508");
            String s = "";
            Process process = null;
            InputStream ip = null;
            DataOutputStream os = null;
            try {
                process = Runtime.getRuntime().exec(String.format("su - c %s", path));
                os = new DataOutputStream(process.getOutputStream());
                os.writeBytes(path);
                os.writeBytes("\n");
                os.flush();

                ip = process.getInputStream();
                byte b[] = new byte[8192];
                int ten = 0;

                while ((ten = ip.read(b)) != -1) {
                    s = new String(b, 0, ten);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (os != null) {
                        os.close();
                    }
                    if (ip != null) {
                        ip.close();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (process != null) {
                    process.destroy();
                }
            }
            //T.ToastSuccess(String.format("执行读取结束：%s", s));
        }
    }


    /**
     * 使服务更好的运行在后台， 不被销毁（手机内存低时不优先销毁）
     */
    public void notification() {
        Notification notification = MyUtils.showNotification(MainService.this);
        startForeground(0x1989, notification);
    }

    /**
     * 关闭所有
     */
    public void stopAll() {
        ViewUpdate.runThread(() -> {
            if (mo != null) {
                mo.removeView();
                mo = null;
            }

            if (so != null) {
                so.removeView();
                so = null;
            }
            isrun = false;
            MainService.this.stopSelf();
        });
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    /**
     * VIP时间检测，过期退出使用
     */
    class G extends Thread {
        @Override
        public void run() {
            while (isrun) {
                boolean time = KeyUtils.isTime(AppData.vipTime);
                if (!time) {
                    stopAll();
                    T.ToastWarning(getString(R.string.time_expires));
                    break;
                }
                try {
                    Thread.sleep(1000 * 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
