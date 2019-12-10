package com.tsml.hkl.Utils;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.*;
import android.content.res.*;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.telephony.*;
import android.text.*;
import android.util.Log;

import com.tsml.hkl.MainService;
import com.tsml.hkl.R;
import com.tsml.hkl.enty.AppData;

import java.io.*;
import java.text.*;
import java.util.*;

import static android.os.Build.VERSION_CODES.LOLLIPOP_MR1;
import static com.tsml.hkl.Utils.KeyUtils.FileJK;
import static com.tsml.hkl.Utils.KeyUtils.FileK;

@SuppressWarnings("all")
public class MyUtils {


    public static File moveFIle;

    /**
     * 获取imei
     *
     * @param context 上下文对象
     * @return imei
     */
    public static final String getIMEI(Context context) {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission")
            String imei = telephonyManager.getDeviceId();
            if (imei == null) {
                imei = "";
            }

            return imei;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 生成uuid
     *
     * @return uuid
     */
    public static final String getUUID() {
        String replace = UUID.randomUUID().toString().replace("-", "");
        return replace;
    }

    /**
     * 判断时间是否到期
     *
     * @param time 需要判断的时间
     * @return
     */
    static public boolean vipTime(String time) {
        try {
            Date now = new Date();
            SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date vip_time = sp.parse(time);
            if (vip_time.getTime() > now.getTime()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    /**
     * 写入文本到到指定文件中
     *
     * @param file 文件路径
     * @param text 需要写入的内容
     */
    public static void writeText(File file, String text) {
        FileWriter fw = null;
        if (file.exists()) {
            try {
                fw = new FileWriter(file);
                fw.write(text);

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fw != null) {
                    try {
                        fw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /**
     * 读取文本文件
     *
     * @param path 文本文件路径
     * @return 文本文件的内容
     */
    public static String getFileText(File path) {
        InputStreamReader isr;
        BufferedReader br = null;
        String hero;
        try {
            isr = new InputStreamReader(new FileInputStream(path), "UTF-8");
            br = new BufferedReader(isr);
            hero = br.readLine();
            return hero;
        } catch (Exception e) {

        } finally {
            try {
                if (br != null) {
                    br.close();
                }

            } catch (IOException e) {

            }
        }
        return null;
    }


    /**
     * byte数组转int
     *
     * @param bytes byte数组
     * @return
     */
    public int Byte2Int(Byte[] bytes) {
        return (bytes[0] & 0xff) << 24
                | (bytes[1] & 0xff) << 16
                | (bytes[2] & 0xff) << 8
                | (bytes[3] & 0xff);
    }


    /**
     * int 转byte
     *
     * @param num
     * @return
     */
    public byte[] IntToByte(int num) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((num >> 24) & 0xff);
        bytes[1] = (byte) ((num >> 16) & 0xff);
        bytes[2] = (byte) ((num >> 8) & 0xff);
        bytes[3] = (byte) (num & 0xff);
        return bytes;

    }


    /**
     * px转dp
     *
     * @param context 上下文对象
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * 获取屏幕对边长度
     *
     * @param width
     * @param height
     * @return
     */
    public static double getfloor(double width, double height) {
        double sqrt = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
        double floor = Math.floor(sqrt);
        return floor;
    }


    /**
     * 解压所有资源文件
     * @param context
     */
    public static void getAssetsFiles(Context context) {
        ViewUpdate.runThread(() -> {
            AssetManager assetManager = context.getAssets();
            File filesDir = context.getFilesDir();
            String[] fileNamelist = new String[0];
            try {
                fileNamelist = assetManager.list("");
            } catch (IOException e) {
                e.printStackTrace();
            }
            for (String fileName : fileNamelist) {
                InputStream ip = null;
                OutputStream ot = null;
                try {
                    ip = assetManager.open(fileName);
                    File dataFile = new File(String.format("%s/%s", filesDir.getPath(), fileName));
                    ot = new FileOutputStream(dataFile);
                    int ten = 0;
                    int length = 1024;
                    byte buffer[] = new byte[length * length];
                    while ((ten = ip.read(buffer)) != -1) {
                        ot.write(buffer, 0, ten);
                    }

                    if (ot != null) {
                        try {
                            ot.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (ip != null) {
                        try {
                            ip.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    ShellUtils.CommandResult commandResult = ShellUtils.execCommand(String.format("chmod 755 %s", dataFile.getPath()), true);

                } catch (IOException e) {

                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * 解压并解密方框可执行文件
     *
     * @param fileName
     * @param context
     * @return
     */

    public static boolean getAssetsFile(String fileName, Context context) {
        AssetManager assetManager = context.getAssets();
        InputStream ip = null;
        OutputStream ot = null;
        File filesDir = context.getFilesDir();
        File dataFile = new File(filesDir.getPath() + "/jkhewrh");
        String gg = dataFile.getPath();

        try {
            ip = assetManager.open(fileName);
            if (!dataFile.exists()) {
                ot = new FileOutputStream(dataFile);
                int ten = 0;
                int length = 1024;
                byte buffer[] = new byte[length];
                while ((ten = ip.read(buffer)) != -1) {
                    byte[] bs = FileJK(buffer);
                    ot.write(bs, 0, ten);
                }
            }
            return true;
        } catch (IOException e) {

            e.printStackTrace();
            return false;

        } finally {
            if (ot != null) {
                try {
                    ot.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ip != null) {
                try {
                    ip.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String shell =
                    "chmod  755 " + dataFile.getPath();
            ShellUtils.CommandResult commandResult = ShellUtils.execCommand(shell, true);

            return true;
        }
    }

    public static void main(String[] args) {
        new MyThread("D:\\备份\\游戏\\MOD\\libtersafe.so", "C:\\Users\\Administrator\\Desktop\\AndroidCE_Demo\\Android_PUBA_Cheat\\app\\src\\main\\assets\\libtersafe.so", 0).start();
    }

    static class MyThread extends Thread {
        String path;
        String putPath;
        int ON_KEY;

        public MyThread(String path, String putPath, int ON_KEY) {
            this.path = path;
            this.putPath = putPath;
            this.ON_KEY = ON_KEY;
        }

        OutputStream ot;
        InputStream is;
        File encryption;
        File Decrypt;
        int length = 1024;

        public void run() {
            encryption = new File(path);           //需要操作做的文件
            Decrypt = new File(putPath);           //操作之后输出路径
            try {

                is = new FileInputStream(encryption);
                ot = new FileOutputStream(Decrypt);

                long filesize = encryption.length();
                int len = 0;
                long size = 0;
                int ss = 0;
                int bb = 0;
                byte[] by = new byte[length];

                while ((len = is.read(by)) != -1) {
                    byte[] bs;

                    if (ON_KEY == 0) {
                        bs = FileJK(by);
                    } else {
                        bs = FileK(by);
                    }
                    size += len;
                    ss = (int) ((size * 100) / filesize);
                    if (bb != ss) {
                        System.out.println("当前进度" + ss);
                        bb = ss;
                    }
                    ot.write(bs, 0, len);
                }
                if (ON_KEY == 0) {

                    System.out.println("加密完成");

                } else {

                    System.out.println("解密完成");

                }
            } catch (FileNotFoundException e) {
                System.out.println("文件不存在，请确认文件路径填写正确");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {


                if (ot != null) {
                    try {
                        ot.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    /**
     * 运行应用缓存目录下的可执行文件
     * @param name
     */
    public static void runScript(String name) {
        if (AppData.context != null) {
            ViewUpdate.runThread(() -> {
                ShellUtils.execCommand(String.format("%s/%s", AppData.context.getFilesDir().getPath(), name), true);
            });
        }
    }

    /**
     * 使服务更好的运行在后台， 不被销毁（手机内存低时不优先销毁）
     */
    public static Notification showNotification(Context context) {

        final int NOTIFICATION_ID = 12234;

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //准备intent
        Intent intent = new Intent();
        String action = "com.tsml.hkl.action";
        intent.setAction(action);

        //notification
        Notification notification = null;

        // 构建 PendingIntent
        PendingIntent pi = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //版本兼容

        if (Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.O && Build.VERSION.SDK_INT >= LOLLIPOP_MR1) {
            notification = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.sss)

                    .setTicker("ZS")
                    .setContentTitle("Service")
                    .setContentText("Service is Running!")
                    .setAutoCancel(false)
                    .setSmallIcon(R.drawable.nfc)
                    .setContentIntent(pi).build();

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN &&
                Build.VERSION.SDK_INT <= LOLLIPOP_MR1) {
            notification = new Notification.Builder(context)
                    .setAutoCancel(false)
                    .setContentIntent(pi)
                    .setTicker("ZS")
                    .setContentTitle("Service")
                    .setContentText("Service is Running!")
                    .setSmallIcon(R.drawable.nfc)
                    .setWhen(System.currentTimeMillis())
                    .build();

        } else if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {


            String CHANNEL_ID = "my_channel_01";
            CharSequence name = "my_channel";
            String Description = "This is my channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mChannel.setShowBadge(false);

            notificationManager.createNotificationChannel(mChannel);
            notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.nfc)
                    .setTicker("ZS")
                    .setContentTitle("Service")
                    .setContentText("Service is Running!")
                    .build();
        }
        return notification;
    }

}