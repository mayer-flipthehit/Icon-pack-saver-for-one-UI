package com.iconpacksaver.oneui;

import android.graphics.Bitmap;

public class AppIconInfo {
    private String appName;
    private String componentName;
    private String iconPath;
    private Bitmap iconBitmap;

    public AppIconInfo(String appName, String componentName, String iconPath, Bitmap iconBitmap) {
        this.appName = appName;
        this.componentName = componentName;
        this.iconPath = iconPath;
        this.iconBitmap = iconBitmap;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getComponentName() {
        return componentName;
    }

    public void setComponentName(String componentName) {
        this.componentName = componentName;
    }

    public String getIconPath() {
        return iconPath;
    }

    public void setIconPath(String iconPath) {
        this.iconPath = iconPath;
    }

    public Bitmap getIconBitmap() {
        return iconBitmap;
    }

    public void setIconBitmap(Bitmap iconBitmap) {
        this.iconBitmap = iconBitmap;
    }
}
