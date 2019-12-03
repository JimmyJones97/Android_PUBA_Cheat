package com.tsml.hkl.Toast;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.tsml.hkl.MainService;

public class T {

    public static void ToastSuccess(Object s) {
        if (MainService.example != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> ToastS.success(MainService.example, s == null ? "" : s.toString(), Toast.LENGTH_LONG).show());
        }
    }

    public static void ToastInfo(Object s) {
        if (MainService.example != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> ToastS.info(MainService.example, s == null ? "" : s.toString(), Toast.LENGTH_LONG).show());
        }
    }

    public static void ToastError(Object s) {
        if (MainService.example != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> ToastS.error(MainService.example, s == null ? "" : s.toString(), Toast.LENGTH_LONG).show());
        }
    }

    public static void ToastWarning(Object s) {
        if (MainService.example != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> ToastS.warning(MainService.example, s == null ? "" : s.toString(), Toast.LENGTH_LONG).show());
        }
    }

}
