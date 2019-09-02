package com.lianliantao.yuetuan.bean;

import java.util.List;

public class ConsultBean extends BaseBean {

    private List<ConsultInfoBean> consultInfo;

    public List<ConsultInfoBean> getConsultInfo() {
        return consultInfo;
    }

    public void setConsultInfo(List<ConsultInfoBean> consultInfo) {
        this.consultInfo = consultInfo;
    }

    public static class ConsultInfoBean {
        /**
         * title : 震惊！是皮业超开发的
         * url :
         */

        private String title;
        private String url;
        private String redirectType;
        private String type;

        public String getRedirectType() {
            return redirectType;
        }

        public void setRedirectType(String redirectType) {
            this.redirectType = redirectType;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
