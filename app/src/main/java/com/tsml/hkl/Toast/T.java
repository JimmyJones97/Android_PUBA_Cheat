package com.tsml.hkl.Toast;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.tsml.hkl.MainService;
import com.tsml.hkl.enty.AppData;

public class T {

    public static void ToastSuccess(Object s) {
        if (AppData.context != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> ToastS.success(AppData.context, s == null ? "" : s.toString(), Toast.LENGTH_LONG).show());
        }
    }

    public static void ToastInfo(Object s) {
        if (AppData.context != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> ToastS.info(AppData.context, s == null ? "" : s.toString(), Toast.LENGTH_LONG).show());
        }
    }

    public static void ToastError(Object s) {
        if (AppData.context != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> ToastS.error(AppData.context, s == null ? "" : s.toString(), Toast.LENGTH_LONG).show());
        }
    }

    public static void ToastWarning(Object s) {
        if (AppData.context != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> ToastS.warning(AppData.context, s == null ? "" : s.toString(), Toast.LENGTH_LONG).show());
        }
    }

}
