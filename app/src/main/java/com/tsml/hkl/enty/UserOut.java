package com.tsml.hkl.enty;

public class UserOut {
    private String state;
    private String messge;
    private String time;
    private String pws;
    private String count;
    public UserOut() {
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public UserOut(String state, String messge, String time, String pws, String count) {
        this.state = state;
        this.messge = messge;
        this.time = time;
        this.pws = pws;
        this.count = count;
    }

    public String getPws() {
        return pws;
    }

    public void setPws(String pws) {
        this.pws = pws;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMessge() {
        return messge;
    }

    public void setMessge(String messge) {
        this.messge = messge;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "UserOut{" +
                "state='" + state + '\'' +
                ", messge='" + messge + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}

