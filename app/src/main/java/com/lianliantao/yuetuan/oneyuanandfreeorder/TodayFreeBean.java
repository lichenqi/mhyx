package com.lianliantao.yuetuan.oneyuanandfreeorder;

import com.lianliantao.yuetuan.bean.BaseBean;

import java.util.List;

public class TodayFreeBean extends BaseBean {


    /**
     * freeGoodsInfo : [{"couponAmount":"10","freeRemainCount":"1000","freeTotalCount":"1000","hasQualify":"true","hasUse":"false","itemId":"541490610307","payPrice":"36.00","pictUrl":"https://img.alicdn.com/imgextra/i4/2195788245/O1CN01sR9Jgi2AmG3vy889v_!!2195788245.jpg_310x310.jpg","rule":"拉新活动","title":" 2件收腹瘦身美体塑身裤夏天薄款","userRank":"","userType":"1","zkFinalPrice":"38.50"}]
     * freeGoodsInfoOld : [{"couponAmount":"10","payPrice":"36.00","pictUrl":"https://img.alicdn.com/imgextra/i4/2195788245/O1CN01sR9Jgi2AmG3vy889v_!!2195788245.jpg_310x310.jpg","title":" 2件收腹瘦身美体塑身裤夏天薄款","userType":"1","zkFinalPrice":"38.50"}]
     * ruleInfo : 1.用户满足活动条件即可参与； 2.免单商品通过先购买后补贴返现的方式； 3.返现时间：付款后次月25号返现到高拥账户（可提现）； 4.同一免单商品用户最多享受补贴一次，多拍免单商品无补贴； 5.新人定义修改为：注册起30天内未成功抢购新人专享免单商品的用户。
     * totalResults : 1
     */

    private String ruleInfo;
    private String totalResults;
    private List<FreeGoodsInfoBean> freeGoodsInfo;
    private List<FreeGoodsInfoOldBean> freeGoodsInfoOld;

    public String getRuleInfo() {
        return ruleInfo;
    }

    public void setRuleInfo(String ruleInfo) {
        this.ruleInfo = ruleInfo;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public List<FreeGoodsInfoBean> getFreeGoodsInfo() {
        return freeGoodsInfo;
    }

    public void setFreeGoodsInfo(List<FreeGoodsInfoBean> freeGoodsInfo) {
        this.freeGoodsInfo = freeGoodsInfo;
    }

    public List<FreeGoodsInfoOldBean> getFreeGoodsInfoOld() {
        return freeGoodsInfoOld;
    }

    public void setFreeGoodsInfoOld(List<FreeGoodsInfoOldBean> freeGoodsInfoOld) {
        this.freeGoodsInfoOld = freeGoodsInfoOld;
    }

    public class FreeGoodsInfoBean {
        /**
         * couponAmount : 10
         * freeRemainCount : 1000
         * freeTotalCount : 1000
         * hasQualify : true
         * hasUse : false
         * itemId : 541490610307
         * payPrice : 36.00
         * pictUrl : https://img.alicdn.com/imgextra/i4/2195788245/O1CN01sR9Jgi2AmG3vy889v_!!2195788245.jpg_310x310.jpg
         * rule : 拉新活动
         * title :  2件收腹瘦身美体塑身裤夏天薄款
         * userRank :
         * userType : 1
         * zkFinalPrice : 38.50
         */

        private String couponAmount;
        private String freeRemainCount;
        private String freeTotalCount;
        private String itemId;
        private String payPrice;
        private String pictUrl;
        private String rule;
        private String title;
        private String userRank;
        private String userType;
        private String zkFinalPrice;
        private String subsidyPrice;
        private String errorInfo;

        public String getErrorInfo() {
            return errorInfo;
        }

        public void setErrorInfo(String errorInfo) {
            this.errorInfo = errorInfo;
        }

        public String getSubsidyPrice() {
            return subsidyPrice;
        }

        public void setSubsidyPrice(String subsidyPrice) {
            this.subsidyPrice = subsidyPrice;
        }

        public String getCouponAmount() {
            return couponAmount;
        }

        public void setCouponAmount(String couponAmount) {
            this.couponAmount = couponAmount;
        }

        public String getFreeRemainCount() {
            return freeRemainCount;
        }

        public void setFreeRemainCount(String freeRemainCount) {
            this.freeRemainCount = freeRemainCount;
        }

        public String getFreeTotalCount() {
            return freeTotalCount;
        }

        public void setFreeTotalCount(String freeTotalCount) {
            this.freeTotalCount = freeTotalCount;
        }


        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getPayPrice() {
            return payPrice;
        }

        public void setPayPrice(String payPrice) {
            this.payPrice = payPrice;
        }

        public String getPictUrl() {
            return pictUrl;
        }

        public void setPictUrl(String pictUrl) {
            this.pictUrl = pictUrl;
        }

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUserRank() {
            return userRank;
        }

        public void setUserRank(String userRank) {
            this.userRank = userRank;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getZkFinalPrice() {
            return zkFinalPrice;
        }

        public void setZkFinalPrice(String zkFinalPrice) {
            this.zkFinalPrice = zkFinalPrice;
        }
    }

    public class FreeGoodsInfoOldBean {
        /**
         * couponAmount : 10
         * payPrice : 36.00
         * pictUrl : https://img.alicdn.com/imgextra/i4/2195788245/O1CN01sR9Jgi2AmG3vy889v_!!2195788245.jpg_310x310.jpg
         * title :  2件收腹瘦身美体塑身裤夏天薄款
         * userType : 1
         * zkFinalPrice : 38.50
         */

        private String couponAmount;
        private String payPrice;
        private String pictUrl;
        private String title;
        private String userType;
        private String zkFinalPrice;
        private String subsidyPrice;

        public String getSubsidyPrice() {
            return subsidyPrice;
        }

        public void setSubsidyPrice(String subsidyPrice) {
            this.subsidyPrice = subsidyPrice;
        }

        public String getCouponAmount() {
            return couponAmount;
        }

        public void setCouponAmount(String couponAmount) {
            this.couponAmount = couponAmount;
        }

        public String getPayPrice() {
            return payPrice;
        }

        public void setPayPrice(String payPrice) {
            this.payPrice = payPrice;
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

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getZkFinalPrice() {
            return zkFinalPrice;
        }

        public void setZkFinalPrice(String zkFinalPrice) {
            this.zkFinalPrice = zkFinalPrice;
        }
    }
}
