package com.lianliantao.yuetuan.bean;

public class SettingBean extends BaseBean{

    /**
     * setInfo : {"orderIncomeNotice":"1","orderPrivacy":"1","shareAppDownload":"1"}
     */

    private SetInfoBean setInfo;

    public SetInfoBean getSetInfo() {
        return setInfo;
    }

    public void setSetInfo(SetInfoBean setInfo) {
        this.setInfo = setInfo;
    }

    public static class SetInfoBean {
        /**
         * orderIncomeNotice : 1
         * orderPrivacy : 1
         * shareAppDownload : 1
         */

        private String orderIncomeNotice;
        private String orderPrivacy;
        private String shareAppDownload;

        public String getOrderIncomeNotice() {
            return orderIncomeNotice;
        }

        public void setOrderIncomeNotice(String orderIncomeNotice) {
            this.orderIncomeNotice = orderIncomeNotice;
        }

        public String getOrderPrivacy() {
            return orderPrivacy;
        }

        public void setOrderPrivacy(String orderPrivacy) {
            this.orderPrivacy = orderPrivacy;
        }

        public String getShareAppDownload() {
            return shareAppDownload;
        }

        public void setShareAppDownload(String shareAppDownload) {
            this.shareAppDownload = shareAppDownload;
        }
    }
}
