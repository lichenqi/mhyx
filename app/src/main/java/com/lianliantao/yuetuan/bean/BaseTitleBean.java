package com.lianliantao.yuetuan.bean;

import java.io.Serializable;
import java.util.List;

public class BaseTitleBean extends BaseBean {


    private List<ActivityInfoBean> activityInfo;
    private List<BannerInfoBean> bannerInfo;
    private List<CateInfoBean> cateInfo;
    private List<MenuInfoBean> menuInfo;

    public List<ActivityInfoBean> getActivityInfo() {
        return activityInfo;
    }

    public void setActivityInfo(List<ActivityInfoBean> activityInfo) {
        this.activityInfo = activityInfo;
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

    public List<MenuInfoBean> getMenuInfo() {
        return menuInfo;
    }

    public void setMenuInfo(List<MenuInfoBean> menuInfo) {
        this.menuInfo = menuInfo;
    }

    public static class ActivityInfoBean {
        /**
         * description : 新人0元返利购
         * logo : http://47.110.14.213:82/static/images/chanpin.png,http://47.110.14.213:82/static/images/chanpinkouhong.png,http://47.110.14.213:82/static/images/chanpinmianmo.png,http://47.110.14.213:82/static/images/chanpinshuang.png
         * redirectType :
         * title : 今日免单
         * type : native
         * url : jinrimiandan
         */

        private String description;
        private String logo;
        private String redirectType;
        private String title;
        private String type;
        private String url;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getRedirectType() {
            return redirectType;
        }

        public void setRedirectType(String redirectType) {
            this.redirectType = redirectType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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

    public static class BannerInfoBean {
        /**
         * color : #FA8C0F
         * logo : http://47.110.14.213:82/static/images/bg_banner_chudanpaihang.png
         * redirectType :
         * title : 出单排行榜
         * type : native
         * url : chudan
         */

        private String color;
        private String logo;
        private String redirectType;
        private String title;
        private String type;
        private String url;

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getRedirectType() {
            return redirectType;
        }

        public void setRedirectType(String redirectType) {
            this.redirectType = redirectType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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
         * cateName : 优选
         * id : 0
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

    public static class MenuInfoBean {
        /**
         * description : 最高返佣90%
         * logo : http://47.110.14.213:82/static/images/sy_cet_icontaobao.png
         * redirectType :
         * title : 淘宝
         * type : native
         * url : taobao
         */

        private String description;
        private String logo;
        private String redirectType;
        private String title;
        private String type;
        private String url;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getRedirectType() {
            return redirectType;
        }

        public void setRedirectType(String redirectType) {
            this.redirectType = redirectType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
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
}
