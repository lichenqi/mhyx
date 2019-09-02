package com.lianliantao.yuetuan.oneyuanandfreeorder;

import com.lianliantao.yuetuan.bean.BaseBean;

import java.util.List;

public class OneYuanBean extends BaseBean {

    /**
     * freeGoodsInfo : [{"discountPrice":"17.9","freeRemainCount":"100","hasQualify":"true","hasUse":"false","itemId":"529337715036","payPrice":"19.80","pictUrl":"https://img.alicdn.com/imgextra/i4/2378443470/O1CN01UPTckt1bVItdXZ3TN_!!2378443470.jpg_310x310.jpg","subsidyPrice":"1.9","title":" 【daydays】家用大容量保温水壶","userType":"0"}]
     * totalResults : 1
     */

    private String totalResults;
    private List<FreeGoodsInfoBean> freeGoodsInfo;

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

    public static class FreeGoodsInfoBean {
        /**
         * discountPrice : 17.9
         * freeRemainCount : 100
         * hasQualify : true
         * hasUse : false
         * itemId : 529337715036
         * payPrice : 19.80
         * pictUrl : https://img.alicdn.com/imgextra/i4/2378443470/O1CN01UPTckt1bVItdXZ3TN_!!2378443470.jpg_310x310.jpg
         * subsidyPrice : 1.9
         * title :  【daydays】家用大容量保温水壶
         * userType : 0
         */

        private String discountPrice;
        private String freeRemainCount;
        private String itemId;
        private String payPrice;
        private String pictUrl;
        private String subsidyPrice;
        private String title;
        private String userType;
        private String errorInfo;

        public String getErrorInfo() {
            return errorInfo;
        }

        public void setErrorInfo(String errorInfo) {
            this.errorInfo = errorInfo;
        }

        public String getDiscountPrice() {
            return discountPrice;
        }

        public void setDiscountPrice(String discountPrice) {
            this.discountPrice = discountPrice;
        }

        public String getFreeRemainCount() {
            return freeRemainCount;
        }

        public void setFreeRemainCount(String freeRemainCount) {
            this.freeRemainCount = freeRemainCount;
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

        public String getSubsidyPrice() {
            return subsidyPrice;
        }

        public void setSubsidyPrice(String subsidyPrice) {
            this.subsidyPrice = subsidyPrice;
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
    }
}
