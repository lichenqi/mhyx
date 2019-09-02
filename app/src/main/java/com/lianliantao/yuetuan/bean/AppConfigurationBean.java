package com.lianliantao.yuetuan.bean;

public class AppConfigurationBean extends BaseBean {


    /**
     * appConfig : {"businessLink":"https://dk.gaoyong666.com/gylm/business/v1/index","debugMode":"1","guideLink":"http://www.mahuayouxuan.com//html/guide/index.html","guidebookLink":"http://www.mahuayouxuan.com//html/guidebook.html","offlineLink":"https://dk.gaoyong666.com/gylm/tuoshop/v1/offline?v=4&uid=8589470&utype=9&version=3.6.2","privacyLink":"http://www.mahuayouxuan.com//html/privacy_xy.html","questionListLink":"http://www.mahuayouxuan.com//html/questionList.html","userRuleLink":"http://www.mahuayouxuan.com//html/userRule.html"}
     * cdpConfig : {"bolHttps":"","mahuaErrorThd":"0","mahuaIp":"","mediaErrorThd":"0","mediaIp":""}
     * version : {"hasUpdate":"1","latestVersion":"101","showUpdate":"1","versionAddr":"http://www.mahuayouxuan.com//static/mhyx.apk","versionDesc":"1.优化首页 2.优化搜索数据 3.美化界面，增强体验效果"}
     */

    private AppConfigBean appConfig;
    private CdpConfigBean cdpConfig;
    private VersionBean version;

    public AppConfigBean getAppConfig() {
        return appConfig;
    }

    public void setAppConfig(AppConfigBean appConfig) {
        this.appConfig = appConfig;
    }

    public CdpConfigBean getCdpConfig() {
        return cdpConfig;
    }

    public void setCdpConfig(CdpConfigBean cdpConfig) {
        this.cdpConfig = cdpConfig;
    }

    public VersionBean getVersion() {
        return version;
    }

    public void setVersion(VersionBean version) {
        this.version = version;
    }

    public static class AppConfigBean {
        /**
         * businessLink : https://dk.gaoyong666.com/gylm/business/v1/index
         * debugMode : 1
         * guideLink : http://www.mahuayouxuan.com//html/guide/index.html
         * guidebookLink : http://www.mahuayouxuan.com//html/guidebook.html
         * offlineLink : https://dk.gaoyong666.com/gylm/tuoshop/v1/offline?v=4&uid=8589470&utype=9&version=3.6.2
         * privacyLink : http://www.mahuayouxuan.com//html/privacy_xy.html
         * questionListLink : http://www.mahuayouxuan.com//html/questionList.html
         * userRuleLink : http://www.mahuayouxuan.com//html/userRule.html
         */

        private String businessLink;
        private String debugMode;
        private String guideLink;
        private String guidebookLink;
        private String offlineLink;
        private String privacyLink;
        private String questionListLink;
        private String userRuleLink;

        public String getBusinessLink() {
            return businessLink;
        }

        public void setBusinessLink(String businessLink) {
            this.businessLink = businessLink;
        }

        public String getDebugMode() {
            return debugMode;
        }

        public void setDebugMode(String debugMode) {
            this.debugMode = debugMode;
        }

        public String getGuideLink() {
            return guideLink;
        }

        public void setGuideLink(String guideLink) {
            this.guideLink = guideLink;
        }

        public String getGuidebookLink() {
            return guidebookLink;
        }

        public void setGuidebookLink(String guidebookLink) {
            this.guidebookLink = guidebookLink;
        }

        public String getOfflineLink() {
            return offlineLink;
        }

        public void setOfflineLink(String offlineLink) {
            this.offlineLink = offlineLink;
        }

        public String getPrivacyLink() {
            return privacyLink;
        }

        public void setPrivacyLink(String privacyLink) {
            this.privacyLink = privacyLink;
        }

        public String getQuestionListLink() {
            return questionListLink;
        }

        public void setQuestionListLink(String questionListLink) {
            this.questionListLink = questionListLink;
        }

        public String getUserRuleLink() {
            return userRuleLink;
        }

        public void setUserRuleLink(String userRuleLink) {
            this.userRuleLink = userRuleLink;
        }
    }

    public static class CdpConfigBean {
        /**
         * bolHttps :
         * mahuaErrorThd : 0
         * mahuaIp :
         * mediaErrorThd : 0
         * mediaIp :
         */

        private String bolHttps;
        private String mahuaErrorThd;
        private String mahuaIp;
        private String mediaErrorThd;
        private String mediaIp;

        public String getBolHttps() {
            return bolHttps;
        }

        public void setBolHttps(String bolHttps) {
            this.bolHttps = bolHttps;
        }

        public String getMahuaErrorThd() {
            return mahuaErrorThd;
        }

        public void setMahuaErrorThd(String mahuaErrorThd) {
            this.mahuaErrorThd = mahuaErrorThd;
        }

        public String getMahuaIp() {
            return mahuaIp;
        }

        public void setMahuaIp(String mahuaIp) {
            this.mahuaIp = mahuaIp;
        }

        public String getMediaErrorThd() {
            return mediaErrorThd;
        }

        public void setMediaErrorThd(String mediaErrorThd) {
            this.mediaErrorThd = mediaErrorThd;
        }

        public String getMediaIp() {
            return mediaIp;
        }

        public void setMediaIp(String mediaIp) {
            this.mediaIp = mediaIp;
        }
    }

    public static class VersionBean {
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
}
