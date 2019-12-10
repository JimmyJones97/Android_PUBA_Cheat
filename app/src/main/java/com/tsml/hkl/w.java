package com.tsml.hkl;

import android.annotation.*;
import android.app.Activity;
import android.content.*;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.*;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.view.View.*;
import android.widget.*;

import com.google.gson.Gson;


import java.io.*;
import java.util.Locale;
import java.util.UUID;


import com.tsml.hkl.Toast.T;
import com.tsml.hkl.Toast.ToastS;
import com.tsml.hkl.Utils.DataSave;
import com.tsml.hkl.Utils.HttpUtils;
import com.tsml.hkl.Utils.KeyUtils;
import com.tsml.hkl.Utils.MyUtils;
import com.tsml.hkl.Utils.ViewUpdate;
import com.tsml.hkl.Utils.fh;
import com.tsml.hkl.enty.AppData;
import com.tsml.hkl.enty.MyRequest;
import com.tsml.hkl.enty.UserOut;
import com.tsml.hkl.R;
import com.tsml.hkl.hosts.vservice.VhostsService;
import com.tsml.hkl.view.SetView;

import static com.tsml.hkl.Utils.HttpUtils.postStringParameters;


public class w extends AppCompatActivity implements OnClickListener {


    boolean isVip = false;
    EditText user;
    Button submit;

    boolean bo = false;
    String us;
    DataSave dataSave;

    private static final String TAG = w.class.getSimpleName();
    private static final int VPN_REQUEST_CODE = 0x0F;
    private static final int SELECT_FILE_CODE = 0x05;
    public static final String PREFS_NAME = w.class.getName();
    public static final String IS_LOCAL = "IS_LOCAL";
    public static final String HOSTS_URL = "HOSTS_URL";
    public static final String HOSTS_URI = "HOST_URI";
    public static final String NET_HOST_FILE = "net_hosts";


    private String imei() {
        if (dataSave == null) {
            dataSave = new DataSave(w.this);
        }
        String imei = dataSave.getString("token");
        fh f = new fh();
        try {
            f.EncryptionDecryption(AppData.KEY);
            if (imei != null && !imei.equals("")) {
                return f.decrypt(imei);
            }
            String uuid = MyUtils.getUUID();
            String encrypt = f.encrypt(uuid);
            dataSave.saveString("token", encrypt);
            return uuid;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private void startVPN() {
        Intent vpnIntent = VhostsService.prepare(this);
        if (vpnIntent != null)
            startActivityForResult(vpnIntent, VPN_REQUEST_CODE);
        else
            onActivityResult(VPN_REQUEST_CODE, RESULT_OK, null);
    }
    private String user() {
        if (dataSave == null) {
            dataSave = new DataSave(w.this);
        }
        return dataSave.getString("user");//获取账号
    }
   /* public void test(){
        AppData.vipTime = "2078-01-24 12:00:00";
        T.ToastSuccess(getString(R.string.login_success));//登陆成功
        dataSave.saveString("user", us);
        isVip = true;
        bo = false;
        MainService.showFloat(w.this);
    }
*/

    public void initView() {
        setContentView(R.layout.activity_main);
        user = findViewById(R.id.et_login_pwd);
        submit = findViewById(R.id.bt_login_submit);
        submit.setOnClickListener(this);
        dataSave = new DataSave(w.this);
        startVPN();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        AppData.context = w.this;
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
                            T.ToastWarning(getString(R.string.plase_key));//卡密错误
                        }
                    } else {
                        T.ToastWarning(getString(R.string.key_not_null));//卡密不能为空
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

                            T.ToastSuccess(getString(R.string.login_success));//登陆成功

                            dataSave.saveString("user", us);
                            isVip = true;
                            bo = false;

                            MainService.showFloat(w.this);

                            finish();
                        } else if (userOut.getState().equals("errer")) {
                            T.ToastWarning(userOut.getMessge());
                        }
                        bo = false;
                    } else {
                        bo = false;
                        T.ToastWarning(getString(R.string.network_errer));//网络连接失败
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

 /*   @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == VPN_REQUEST_CODE && resultCode == RESULT_OK) {
            startService(new Intent(this, VhostsService.class).setAction(VhostsService.ACTION_CONNECT));
        } else if (requestCode == SELECT_FILE_CODE && resultCode == RESULT_OK) {
        }
    }*/

}
