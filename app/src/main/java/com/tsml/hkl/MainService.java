package com.tsml.hkl;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.Log;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.Process;
import java.util.List;

import com.tsml.hkl.Toast.T;
import com.tsml.hkl.Utils.DataSave;
import com.tsml.hkl.Utils.KeyUtils;
import com.tsml.hkl.Utils.MyUtils;
import com.tsml.hkl.Utils.ProcessManager;
import com.tsml.hkl.Utils.ShellUtils;
import com.tsml.hkl.Utils.ViewUpdate;
import com.tsml.hkl.enty.AppData;
import com.tsml.hkl.view.MoveView;
import com.tsml.hkl.view.SetView;

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

        dataSave = new DataSave(MainService.this);
        example = MainService.this;
        AppData.context = MainService.this;

        MyUtils.getAssetsFile("TF_BOAYS", this);//解压数据
        so = new SetView(MainService.this);
        mo = new MoveView(MainService.this);

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
        ShellUtils.execCommand("rm -f /dev/jkhewrh", true);//删除数据
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
            T.ToastSuccess(String.format("杀死服务%s个", process));
        });
    }

    /**
     * 开启扫描坐标
     */
    class startServer extends Thread {

        @Override
        public void run() {

            final String path = "/dev/jkhewrh";
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
            T.ToastSuccess(String.format("执行读取结束：%s", s));
        }
    }


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
