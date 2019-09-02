package com.lianliantao.yuetuan.bean;

import java.util.List;

public class OtherListBean {

    /**
     * brandInfo : [{"brandName":"绅士(PLANTERS)","logo":"//img.alicdn.com/bao/uploaded///img.taobaocdn.com/tps/TB1gBHsNpXXXXcOaXXXSutbFXXX.jpg","rebateRate":"0","shopUrl":"http://shop.m.taobao.com/shop/shop_index.htm?user_id=3036733485"},{"brandName":"家乐氏(KELLOGG\u2019S)","logo":"https://img.alicdn.com/bao/uploaded///img.taobaocdn.com/tps/TB1R2iBLFXXXXbtXVXXSutbFXXX.jpg","rebateRate":"0","shopUrl":"http://shop.m.taobao.com/shop/shop_index.htm?user_id=2604362886"}]
     * errmsg : success
     * errno : 200
     * totalResults : 117
     * usermsg : success
     */

    private String errmsg;
    private int errno;
    private String totalResults;
    private String usermsg;
    private List<BrandInfoBean> brandInfo;

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

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public String getUsermsg() {
        return usermsg;
    }

    public void setUsermsg(String usermsg) {
        this.usermsg = usermsg;
    }

    public List<BrandInfoBean> getBrandInfo() {
        return brandInfo;
    }

    public void setBrandInfo(List<BrandInfoBean> brandInfo) {
        this.brandInfo = brandInfo;
    }

    public static class BrandInfoBean {
        /**
         * brandName : 绅士(PLANTERS)
         * logo : //img.alicdn.com/bao/uploaded///img.taobaocdn.com/tps/TB1gBHsNpXXXXcOaXXXSutbFXXX.jpg
         * rebateRate : 0
         * shopUrl : http://shop.m.taobao.com/shop/shop_index.htm?user_id=3036733485
         */

        private String brandName;
        private String logo;
        private String rebateRate;
        private String shopId;

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getRebateRate() {
            return rebateRate;
        }

        public void setRebateRate(String rebateRate) {
            this.rebateRate = rebateRate;
        }

    }
}
