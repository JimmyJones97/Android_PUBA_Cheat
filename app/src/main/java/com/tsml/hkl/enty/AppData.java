package com.tsml.hkl.enty;

import android.content.Context;

@SuppressWarnings("all")
public class AppData {
    public static double height;
    public static double width;
    public static double floor;
    public static int x = 0;
    public static int y = 0;
    public static int fps = 50;
    public static String vipTime;
    public static boolean isLine = false;
    public static boolean isK = false;
    public static boolean isRun = false;
    public static boolean isM = false;
    public static boolean isFps = false;

    public static boolean isHX = false;//默认不为横屏

    public static Context context; //全局上下文对象

    public final static String KEY = "uweioqjkdvxklamsfhieb";
    public final static String SDCRAD = "/sdcrad/b.log";

    public final static String URL = "http://27.124.47.145/crads/kw"; // 请求链接
    public final static String GAME_BACK = "game_back"; // 游戏是否备份

    public final static String GAME_FILE_PATH = "/data/data/com.tencent.ig/lib/"; // 国际服游戏文件路径
    public final static String GAME_FILE_PATHS = "/data/data/com.tencent.tmgp.pubgmhd/lib/"; // 国服游戏文件路径
}
