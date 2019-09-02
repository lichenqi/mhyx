package com.lianliantao.yuetuan.bean;

import java.io.Serializable;
import java.util.List;

public class PosterBean extends BaseBean {

    private List<PosterInfoBean> posterInfo;

    public List<PosterInfoBean> getPosterInfo() {
        return posterInfo;
    }

    public void setPosterInfo(List<PosterInfoBean> posterInfo) {
        this.posterInfo = posterInfo;
    }

    public static class PosterInfoBean implements Serializable {
        /**
         * logo : http://47.110.14.213:82/static/bg_banner_laxinhuodong.png
         */

        private String logo;
        private boolean isChoose;

        public boolean isChoose() {
            return isChoose;
        }

        public void setChoose(boolean choose) {
            isChoose = choose;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }
    }
}
