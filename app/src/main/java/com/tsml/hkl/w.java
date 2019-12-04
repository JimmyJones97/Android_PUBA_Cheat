package com.tsml.hkl;

import android.annotation.*;
import android.app.Activity;
import android.content.*;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.google.gson.Gson;


import java.io.*;
import java.util.UUID;


import com.tsml.hkl.Toast.T;
import com.tsml.hkl.Toast.ToastS;
import com.tsml.hkl.Utils.DataSave;
import com.tsml.hkl.Utils.HttpUtils;
import com.tsml.hkl.Utils.KeyUtils;
import com.tsml.hkl.Utils.MyUtils;
import com.tsml.hkl.Utils.ViewUpdate;
import com.tsml.hkl.enty.AppData;
import com.tsml.hkl.enty.MyRequest;
import com.tsml.hkl.enty.UserOut;
import com.tsml.hkl.R;

import static com.tsml.hkl.Utils.HttpUtils.postStringParameters;


public class w extends AppCompatActivity implements OnClickListener {


    boolean isVip = false;
    EditText user;
    Button submit;

    boolean bo = false;
    String us;
    DataSave dataSave;

    private String imei() {
        if (dataSave == null) {
            dataSave = new DataSave(w.this);
        }
        String imei = dataSave.getString("imei");
        if (imei != null && !imei.equals("")) {
            return imei;
        }

        String uuid = MyUtils.getUUID();
        dataSave.saveString("imei", uuid);
        return uuid;

    }

    private String user() {
        if (dataSave == null) {
            dataSave = new DataSave(w.this);
        }
        return dataSave.getString("user");//获取账号
    }

    public void initView() {
        setContentView(R.layout.activity_main);
        user = findViewById(R.id.et_login_pwd);
        submit = findViewById(R.id.bt_login_submit);
        submit.setOnClickListener(this);
        dataSave = new DataSave(w.this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        final String uss = user();
        if (uss != null && !uss.equals("")) {
            user.setText(uss);
            bo = true;
            this.us = uss;
            login(uss);

        }

    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onClick(View p1) {
        switch (p1.getId()) {
            case R.id.bt_login_submit:
                if (!bo) {
                    String ua = user.getText().toString();
                    if (!ua.equals("")) {
                        if (ua.length() == 24) {
                            bo = true;
                            this.us = ua;
                            login(ua);
                        } else {
                            T.ToastWarning("请填写正确卡密",w.this);
                        }
                    } else {
                        T.ToastWarning("卡密不能为空",w.this);
                    }
                }
                break;
            default:
                break;
        }
    }


    public void login(String key) {

        final int count = (int) ((Math.random() * 9 + 1) * 100000);
        String rokey = imei();
        String code;
        String counts;

        try {
            code = KeyUtils.encryptBASE64(key);
            counts = KeyUtils.encryptBASE64(String.valueOf(count));

            MyRequest my = new MyRequest();
            my.setCum(counts);
            my.setUps(code);
            my.setImei(rokey);
            Gson gs = new Gson();

            String s = gs.toJson(my);
            String request = KeyUtils.jia(s);

            try {
                postStringParameters(request, userOut -> {

                    if (userOut != null) {
                        if (userOut.getState().equals("ok") &&
                                userOut.getCount() != null &&
                                userOut.getCount().equals(KeyUtils.getCU(count))) {
                            AppData.vipTime = userOut.getTime();
                            T.ToastSuccess("登录成功",w.this);
                            dataSave.saveString("user", us);
                            isVip = true;
                            bo = false;

                            MainService.showFloat(w.this);

                            finish();
                        } else if (userOut.getState().equals("errer")) {
                            T.ToastError(userOut.getMessge(),w.this);
                        }
                        bo = false;
                    } else {
                        bo = false;
                        T.ToastError("网络连接失败",w.this);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
