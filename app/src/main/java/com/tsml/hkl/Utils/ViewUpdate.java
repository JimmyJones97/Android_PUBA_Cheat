package com.tsml.hkl.Utils;

import android.os.Handler;
import android.os.Looper;

public class ViewUpdate {

    public static void run(Runnable task, boolean isView) {
        if (isView) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(task);
        } else {
            new Thread(task).start();
        }
    }

    public static void threadUi(Runnable task) {
        run(task, true);
    }
    public static void runThread(Runnable task) {
        run(task, false);
    }
}
