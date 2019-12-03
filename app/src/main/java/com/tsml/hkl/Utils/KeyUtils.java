package com.tsml.hkl.Utils;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.tsml.hkl.BASE64.BASE64Decoder;
import com.tsml.hkl.BASE64.BASE64Encoder;

public class KeyUtils {

    public static final String ENC = "utf-8";

    /**
     * BASE64解密
     */
    public static String decryptBASE64(String key) throws Exception {
        return new String(new BASE64Decoder().decodeBuffer(key), ENC);
    }

    /**
     * BASE64加密
     */
    public static String encryptBASE64(String js) throws Exception {
        byte[] key = js.getBytes(ENC);
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    public static String jia(String s) throws Exception {
        String s1 = encryptBASE64(s);
        byte[] bytes = s1.getBytes(ENC);
        byte[] h = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            h[i] = (byte) ((bytes[i] ^ 0x01));
        }
        return new String(h);
    }

    public static String jie(String s) throws Exception {
        byte[] bytes = s.getBytes(ENC);
        byte[] h = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            h[i] = (byte) ((bytes[i] ^ 0x01));
        }
        String s1 = decryptBASE64(new String(h, ENC));
        return s1;
    }


    static public String getCU(int s) {
        int c = ((s / 2 + 27) * 3) | 2;
        return "" + c;
    }

    static public boolean isTime(String time) {
        Date date = new Date();
        String str = "yyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(str);
        try {
            if(time !=null && !time.equals("")){
                Date parse = sdf.parse(time);
                return date.getTime() < parse.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }
}
