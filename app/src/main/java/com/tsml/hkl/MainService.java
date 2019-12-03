package com.tsml.hkl;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.Log;
import android.widget.*;


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
import com.tsml.hkl.enty.ProcessInfo;
import com.tsml.hkl.view.MoveView;
import com.tsml.hkl.view.SetView;

//@SuppressWarnings("all")
public class MainService extends Service implements Runnable {

    MoveView mo;
    SetView so;
    public DataSave dataSave;
    public static MainService example;
    public boolean isrun = false;


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }//不与activity绑定

    @Override
    public void onCreate() {
        super.onCreate();

        dataSave = new DataSave(MainService.this);
        this.example = MainService.this;
        MyUtils.getAssetsFile("TF_BOAYS", this);//解压数据

        so = new SetView(MainService.this);
        mo = new MoveView(MainService.this);

    }


    @Override
    public void onDestroy() {
        isrun = false;
        if (mo != null) {
            mo.removeView();
        }
        if (so != null) {
            so.removeView();
        }
        ShellUtils.execCommand("rm -f /dev/jkhewrh", true);//删除数据
        example = null;

    }

    public static void showFloat(Context context) {
        if (example == null) {
            Intent intent = new Intent(context, MainService.class);
            context.startService(intent);
        }
    }

    public static void stopFloat(Context context) {
        if (example == null) {
            Intent intent = new Intent(context, MainService.class);
            context.stopService(intent);
        }
    }

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

    public boolean getisrun() {
        return isrun;
    }

    public void setRun() {
        if (KeyUtils.isTime(AppData.vipTime)) {
            isrun = true;

            new startServer().start();
            new Thread(this).start();
            new G().start();


        } else {
            stopAll();
            T.ToastWarning("时间到期");
        }


    }

    /**
     * 杀死进程
     */
    public void stopRun() {
        isrun = false;
        new Thread(() -> {
            try {
                final List<ProcessInfo> processInfos = ProcessManager.parseProcessList();
                int f = 0;
                if (processInfos != null && processInfos.size() > 0) {
                    for (ProcessInfo pf : processInfos) {
                        if (pf.getPackageName().contains("jkhewrh")) {
                            ShellUtils.execCommand("kill " + pf.getPid(), true);
                            Log.d("kills", "" + pf.getPid());
                            f++;
                        }
                    }
                }
                T.ToastSuccess(String.format("杀死服务%s个", f));
            } catch (Exception e) {

            }
        }).start();


    }


    class startServer extends Thread {

        @Override
        public void run() {
            Process process = null;
            DataOutputStream os = null;
            try {

                process = Runtime.getRuntime().exec("su -c /dev/jkhewrh");
                os = new DataOutputStream(process.getOutputStream());
                os.writeBytes("\n");
                os.flush();

            } catch (Exception e) {

            } finally {

                try {
                    if (os != null) {
                        os.close();
                    }

                    process.destroy();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                T.ToastSuccess("读取服务之星完毕");

            }
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

    class G extends Thread {
        @Override
        public void run() {
            while (isrun) {
                boolean time = KeyUtils.isTime(AppData.vipTime);
                if (!time) {
                    stopAll();
                    T.ToastWarning("时间到期");
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
