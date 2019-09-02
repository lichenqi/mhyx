package com.lianliantao.yuetuan.bean;

import java.util.List;

public class SearchListBean extends BaseBean {

    /**
     * goodsInfo : [{"couponAmount":"5","couponRemainCount":"100000","couponTotalCount":"100000","estimatedEarn":"1.74","hasCollect":"false","itemDescription":[],"itemId":"588909002047","nick":"安玖服饰专营店","payPrice":"14.50","pictUrl":"https://img.alicdn.com/bao/uploaded/i3/2314585201/O1CN01pRqS6a1oI6Tet0sy0_!!0-item_pic.jpg","sales":0,"title":"俞兆林纯棉短袖t恤女2019新款 韩范半袖夏装上衣纯色百搭体恤衣服","upgradeEarn":"2.03","userType":"1","volume":"65968","zkFinalPrice":"19.5"}]
     * totalResults : 44455089
     */

    private String totalResults;
    private List<GoodsInfoBean> goodsInfo;

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public List<GoodsInfoBean> getGoodsInfo() {
        return goodsInfo;
    }

    public void setGoodsInfo(List<GoodsInfoBean> goodsInfo) {
        this.goodsInfo = goodsInfo;
    }

    public static class GoodsInfoBean {
        /**
         * couponAmount : 5
         * couponRemainCount : 100000
         * couponTotalCount : 100000
         * estimatedEarn : 1.74
         * hasCollect : false
         * itemDescription : []
         * itemId : 588909002047
         * nick : 安玖服饰专营店
         * payPrice : 14.50
         * pictUrl : https://img.alicdn.com/bao/uploaded/i3/2314585201/O1CN01pRqS6a1oI6Tet0sy0_!!0-item_pic.jpg
         * sales : 0
         * title : 俞兆林纯棉短袖t恤女2019新款 韩范半袖夏装上衣纯色百搭体恤衣服
         * upgradeEarn : 2.03
         * userType : 1
         * volume : 65968
         * zkFinalPrice : 19.5
         */

        private String couponAmount;
        private String couponRemainCount;
        private String couponTotalCount;
        private String estimatedEarn;
        private String hasCollect;
        private String itemId;
        private String nick;
        private String payPrice;
        private String pictUrl;
        private int sales;
        private String title;
        private String upgradeEarn;
        private String userType;
        private String volume;
        private String zkFinalPrice;
        private String itemDescription;

        public String getItemDescription() {
            return itemDescription;
        }

        public void setItemDescription(String itemDescription) {
            this.itemDescription = itemDescription;
        }

        public String getCouponAmount() {
            return couponAmount;
        }

        public void setCouponAmount(String couponAmount) {
            this.couponAmount = couponAmount;
        }

        public String getCouponRemainCount() {
            return couponRemainCount;
        }

        public void setCouponRemainCount(String couponRemainCount) {
            this.couponRemainCount = couponRemainCount;
        }

        public String getCouponTotalCount() {
            return couponTotalCount;
        }

        public void setCouponTotalCount(String couponTotalCount) {
            this.couponTotalCount = couponTotalCount;
        }

        public String getEstimatedEarn() {
            return estimatedEarn;
        }

        public void setEstimatedEarn(String estimatedEarn) {
            this.estimatedEarn = estimatedEarn;
        }

        public String getHasCollect() {
            return hasCollect;
        }

        public void setHasCollect(String hasCollect) {
            this.hasCollect = hasCollect;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getNick() {
            return nick;
        }

        public void setNick(String nick) {
            this.nick = nick;
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

        public int getSales() {
            return sales;
        }

        public void setSales(int sales) {
            this.sales = sales;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUpgradeEarn() {
            return upgradeEarn;
        }

        public void setUpgradeEarn(String upgradeEarn) {
            this.upgradeEarn = upgradeEarn;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getVolume() {
            return volume;
        }

        public void setVolume(String volume) {
            this.volume = volume;
        }

        public String getZkFinalPrice() {
            return zkFinalPrice;
        }

        public void setZkFinalPrice(String zkFinalPrice) {
            this.zkFinalPrice = zkFinalPrice;
        }

    }
}
