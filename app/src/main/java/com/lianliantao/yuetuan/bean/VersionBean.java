package com.lianliantao.yuetuan.bean;

public class VersionBean extends BaseBean {


    /**
     * hasUpdate : 1
     * latestVersion : 101
     * showUpdate : 1
     * versionAddr : http://www.mahuayouxuan.com//static/mhyx.apk
     * versionDesc : 1.优化首页 2.优化搜索数据 3.美化界面，增强体验效果
     */

    private String hasUpdate;
    private String latestVersion;
    private String showUpdate;
    private String versionAddr;
    private String versionDesc;

    public String getHasUpdate() {
        return hasUpdate;
    }

    public void setHasUpdate(String hasUpdate) {
        this.hasUpdate = hasUpdate;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public String getShowUpdate() {
        return showUpdate;
    }

    public void setShowUpdate(String showUpdate) {
        this.showUpdate = showUpdate;
    }

    public String getVersionAddr() {
        return versionAddr;
    }

    public void setVersionAddr(String versionAddr) {
        this.versionAddr = versionAddr;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }
}
