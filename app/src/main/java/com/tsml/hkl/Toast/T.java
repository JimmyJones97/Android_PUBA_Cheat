package com.tsml.hkl.Toast;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.tsml.hkl.MainService;

public class T {

    public static void ToastSuccess(Object s, Context context) {
        if (MainService.example != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> ToastS.success(context, s == null ? "" : s.toString(), Toast.LENGTH_LONG).show());
        }
    }

    public static void ToastInfo(Object s, Context context) {
        if (MainService.example != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> ToastS.info(context, s == null ? "" : s.toString(), Toast.LENGTH_LONG).show());
        }
    }

    public static void ToastError(Object s, Context context) {
        if (MainService.example != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> ToastS.error(context, s == null ? "" : s.toString(), Toast.LENGTH_LONG).show());
        }
    }

    public static void ToastWarning(Object s, Context context) {
        if (MainService.example != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> ToastS.warning(context, s == null ? "" : s.toString(), Toast.LENGTH_LONG).show());
        }
    }

}
