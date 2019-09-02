package com.lianliantao.yuetuan.bean;

import java.io.Serializable;
import java.util.List;

public class BaseTitleBean {

    /**
     * bannerInfo : [{"logo":"http://47.110.14.213:82/static/bg_banner_laxinhuodong.png","type":"1","url":""}]
     * cateInfo : [{"cateName":"居家","id":"2"},{"cateName":"美食","id":"3"},{"cateName":"母婴","id":"4"},{"cateName":"美妆","id":"5"},{"cateName":"女装","id":"6"},{"cateName":"数码","id":"7"},{"cateName":"车品","id":"8"},{"cateName":"内衣","id":"9"},{"cateName":"家装","id":"10"},{"cateName":"鞋品","id":"11"},{"cateName":"男装","id":"12"},{"cateName":"配饰","id":"13"},{"cateName":"户外","id":"14"},{"cateName":"箱包","id":"15"}]
     * cheaper : 100
     * errmsg : success
     * errno : 200
     * hasNotice : true
     * usermsg : success
     */

    private String cheaper;
    private String errmsg;
    private int errno;
    private String hasNotice;
    private String usermsg;
    private List<BannerInfoBean> bannerInfo;
    private List<CateInfoBean> cateInfo;

    public String getCheaper() {
        return cheaper;
    }

    public void setCheaper(String cheaper) {
        this.cheaper = cheaper;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getHasNotice() {
        return hasNotice;
    }

    public void setHasNotice(String hasNotice) {
        this.hasNotice = hasNotice;
    }

    public String getUsermsg() {
        return usermsg;
    }

    public void setUsermsg(String usermsg) {
        this.usermsg = usermsg;
    }

    public List<BannerInfoBean> getBannerInfo() {
        return bannerInfo;
    }

    public void setBannerInfo(List<BannerInfoBean> bannerInfo) {
        this.bannerInfo = bannerInfo;
    }

    public List<CateInfoBean> getCateInfo() {
        return cateInfo;
    }

    public void setCateInfo(List<CateInfoBean> cateInfo) {
        this.cateInfo = cateInfo;
    }

    public static class BannerInfoBean {
        /**
         * logo : http://47.110.14.213:82/static/bg_banner_laxinhuodong.png
         * type : 1
         * url :
         */

        private String logo;
        private String type;
        private String url;

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class CateInfoBean implements Serializable {
        /**
         * cateName : 居家
         * id : 2
         */

        private String cateName;
        private String id;

        public String getCateName() {
            return cateName;
        }

        public void setCateName(String cateName) {
            this.cateName = cateName;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
