package com.tsml.hkl.enty;

import android.graphics.drawable.Drawable;

public class ProcessInfo {
    public String name;
    public long memSize;
    public Drawable icon;
    public boolean isCheck;
    public boolean isSystem;
    public String packageName;
    public int pid;
    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getMemSize() {
        return memSize;
    }

    public void setMemSize(long memSize) {
        this.memSize = memSize;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean isCheck) {
        this.isCheck = isCheck;
    }

    public boolean isSystem() {
        return isSystem;
    }

    public void setSystem(boolean isSystem) {
        this.isSystem = isSystem;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return "ProcessInfo{" +
                "name='" + name + '\'' +
                ", memSize=" + memSize +
                ", icon=" + icon +
                ", isCheck=" + isCheck +
                ", isSystem=" + isSystem +
                ", packageName='" + packageName + '\'' +
                ", pid=" + pid +
                '}';
    }
}
