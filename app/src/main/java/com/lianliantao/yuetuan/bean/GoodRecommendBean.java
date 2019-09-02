package com.lianliantao.yuetuan.bean;

import java.util.List;

public class GoodRecommendBean extends BaseBean {

    private List<RecommendInfoBean> recommendInfo;

    public List<RecommendInfoBean> getRecommendInfo() {
        return recommendInfo;
    }

    public void setRecommendInfo(List<RecommendInfoBean> recommendInfo) {
        this.recommendInfo = recommendInfo;
    }

    public static class RecommendInfoBean {
        /**
         * itemId : 598262691787
         * itemUrl : https://h5.m.taobao.com/awp/core/detail.htm?id=598262691787
         * userType : 0
         * pictUrl : https://gd2.alicdn.com/imgextra/i2/2201479094846/O1CN01VvqeCm1lfVsWJFO0C_!!2201479094846.jpg_310x310.jpg
         * title :  美国队长学生背包书包
         * payPrice : 69.00
         * couponAmount : 200
         */

        private String itemId;
        private String itemUrl;
        private String userType;
        private String pictUrl;
        private String title;
        private String payPrice;
        private String couponAmount;

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getItemUrl() {
            return itemUrl;
        }

        public void setItemUrl(String itemUrl) {
            this.itemUrl = itemUrl;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getPictUrl() {
            return pictUrl;
        }

        public void setPictUrl(String pictUrl) {
            this.pictUrl = pictUrl;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPayPrice() {
            return payPrice;
        }

        public void setPayPrice(String payPrice) {
            this.payPrice = payPrice;
        }

        public String getCouponAmount() {
            return couponAmount;
        }

        public void setCouponAmount(String couponAmount) {
            this.couponAmount = couponAmount;
        }
    }
}
