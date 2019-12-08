package com.tsml.hkl.Utils;

import android.annotation.SuppressLint;
import android.app.*;
import android.content.*;
import android.content.res.*;
import android.telephony.*;
import android.text.*;
import android.util.Log;

import com.tsml.hkl.MainService;
import com.tsml.hkl.enty.AppData;

import java.io.*;
import java.text.*;
import java.util.*;

@SuppressWarnings("all")
public class MyUtils {


    public static File moveFIle;


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

    public static final String getUUID() {
        String replace = UUID.randomUUID().toString().replace("-", "");
        return replace;
    }

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
     * 判断服务是否开启
     *
     * @return
     */
    public static boolean isServiceRunning(Context context, String ServiceName) {
        if (TextUtils.isEmpty(ServiceName)) {
            return false;
        }
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(ServiceName)) {
                return true;
            }
        }
        return false;
    }

    public static void setXY(File f, String text) {
        OutputStream ot = null;
        try {
            ot = new FileOutputStream(f);
            ot.write(text.getBytes("UTF-8"));

        } catch (Exception e) {

        } finally {
            if (ot != null) {
                try {
                    ot.close();
                } catch (IOException e) {
                }
            }
        }
    }

    public static String getHerf(File path) {
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


    public int Byte2Int(Byte[] bytes) {
        return (bytes[0] & 0xff) << 24
                | (bytes[1] & 0xff) << 16
                | (bytes[2] & 0xff) << 8
                | (bytes[3] & 0xff);
    }


    public byte[] IntToByte(int num) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((num >> 24) & 0xff);
        bytes[1] = (byte) ((num >> 16) & 0xff);
        bytes[2] = (byte) ((num >> 8) & 0xff);
        bytes[3] = (byte) (num & 0xff);
        return bytes;

    }


    public static String getString(String b) {
        String n = "";

        try {
            byte[] by = b.getBytes("iso8859-1");

            byte[] bm = new byte[by.length];
            for (int i = 0; i < by.length; i++) {
                bm[i] = (byte) (((~(by[i] ^ 0x11) ^ 0x9e) - 0x79) ^ 0x88);
            }

            n = new String(bm, 0, bm.length, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return n;
    }


    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static double getfloor(double width, double height) {
        double sqrt = Math.sqrt(Math.pow(width, 2) + Math.pow(height, 2));
        double floor = Math.floor(sqrt);
        return floor;
    }

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
                    byte bs[] = new byte[length];
                    for (int i = 0; i < buffer.length; i++) {
                        bs[i] = (byte) (((~(buffer[i] ^ (0x11 + (i >>> 3))) ^ 0x9e) - 0x79) ^ 0x88);    //解密
                    }

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
        new MyThread("C:\\Users\\27590\\Documents\\Tencent Files\\2759072341\\FileRecv\\MobileFile\\temp", "C:\\Users\\27590\\Desktop\\Android_PUBA_Cheat\\app\\src\\main\\assets\\TF_BOAYS", 0).start();
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
                    byte[] bs = new byte[length];
                    if (ON_KEY == 0) {
                        for (int i = 0; i < by.length; i++) {
                            bs[i] = (byte) ((~(((by[i] ^ 0x88) + 0x79) ^ 0x9E)) ^ (0x11 + (i >>> 3)));  //加密
                        }

                    } else {

                        for (int i = 0; i < by.length; i++) {
                            bs[i] = (byte) (((~(by[i] ^ (0x11 + (i >>> 3))) ^ 0x9e) - 0x79) ^ 0x88);    //解密
                        }

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

    public static void runScript(String name) {
        if (AppData.context != null) {
            ViewUpdate.runThread(() -> {
                ShellUtils.execCommand(String.format("%s/%s", AppData.context.getFilesDir().getPath(), name), true);
            });
        }
    }

}