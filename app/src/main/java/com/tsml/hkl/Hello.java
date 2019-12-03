package com.tsml.hkl;


import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tsml.hkl.Toast.ToastS;
import com.tsml.hkl.Utils.SettingsCompat;
import com.tsml.hkl.Utils.ViewUpdate;
import com.tsml.hkl.dialog.CustomDialog;

import java.io.DataOutputStream;

public class Hello extends AppCompatActivity {
    private static String[] PERMISSIONS_CAMERA_AND_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE,};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello);
        getRoot();
    }

    public void getRoot() {
        boolean root = isRoot();
        if (root) {

            getXF();

        } else {

            noRoot();

        }
    }

    public void getXF() {

        if (!SettingsCompat.canDrawOverlays(Hello.this)) {
            ToastS.warning(this, "请授权悬浮窗权限", Toast.LENGTH_LONG);
            SettingsCompat.manageDrawOverlays(Hello.this);
        }

        isGrantExternalRW();
    }


    @TargetApi(Build.VERSION_CODES.M)
    public void isGrantExternalRW() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            int storagePermission = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //检测是否有权限，如果没有权限，就需要申请
            if (storagePermission != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                requestPermissions(PERMISSIONS_CAMERA_AND_STORAGE, 0);
                return;
            }
        }
        startActivity();
    }


    public void startActivity() {
        new Thread(() -> {
            int time = 1;
            while (time != 0) {
                try {
                    time--;
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            ViewUpdate.threadUi(() -> {
                Intent in = new Intent(Hello.this, w.class);
                startActivity(in);
                finish();
            });
        }).start();
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 0:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String sdCard = Environment.getExternalStorageState();
                    if (sdCard.equals(Environment.MEDIA_MOUNTED)) {
                        startActivity();
                    }
                } else {
                    ViewUpdate.threadUi(() -> ToastS.warning(Hello.this, "您必须授权权限才能使用比软件", Toast.LENGTH_SHORT).show());
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public synchronized boolean isRoot() {
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("exit\n");
            os.flush();
            int exitValue = process.waitFor();
            if (exitValue == 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void noRoot() {
        ViewUpdate.threadUi(() -> {
            View view = getLayoutInflater().inflate(R.layout.dialog, null);
            LinearLayout ok = view.findViewById(R.id.dialogLinearLayoutok);
            LinearLayout on = view.findViewById(R.id.dialogLinearLayoutch);
            TextView info = view.findViewById(R.id.dialogTextView1);
            info.setText("请授权ROOT权限");

            final CustomDialog mydialog = new CustomDialog(Hello.this, view, R.style.DialogTheme, false);
            mydialog.setCancelable(true);
            mydialog.show();

            ok.setOnClickListener(view12 -> {
                getRoot();
                mydialog.cancel();
            });

            on.setOnClickListener(view1 -> {
                mydialog.cancel();
                finish();
            });
        });

    }

}
