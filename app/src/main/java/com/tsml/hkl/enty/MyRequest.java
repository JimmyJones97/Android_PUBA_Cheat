package com.tsml.hkl.enty;

public class MyRequest {

    private String ups;  //卡密
    private String imei; //手机imei
    private String cum;  //状态数字

    public String getUps() {
        return ups;
    }

    public void setUps(String ups) {
        this.ups = ups;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getCum() {
        return cum;
    }

    public void setCum(String cum) {
        this.cum = cum;
    }

    @Override
    public String toString() {
        return "MyRequest{" +
                "ups='" + ups + '\'' +
                ", imei='" + imei + '\'' +
                ", cum='" + cum + '\'' +
                '}';
    }
}
