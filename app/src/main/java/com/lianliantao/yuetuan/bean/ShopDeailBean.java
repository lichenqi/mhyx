package com.lianliantao.yuetuan.bean;

public class ShopDeailBean extends BaseBean {


    /**
     * authUrl : https://oauth.taobao.com/authorize?response_type=code&client_id=26065425&redirect_uri=http://39.98.170.160/index/index/taobaoLogin?uId=MTUgNjQ1ZGE3NzgyZWU1ZjFmZmU0ZjA5YzRlNTI5OTE2M2ItLWJlOTc5ZWMzN2M5YjFjMjg1MDc1ZDMyZTZlYjI0NjAy&auth_type=1&state=1212&view=wap
     * goodsDetail : {"couponAmount":"40","couponClickUrl":"https://uland.taobao.com/coupon/edetail?e=im0KY%2FiwYHwGQASttHIRqTivyjhlPdbagL9MdCDWBf0lRDfdDSQcUVTXEGqjSuiPI6sFaWJ2YBL085mp2pSHBwaSXjoOPssCDSh3zlT4Ss4uFYaruP%2Frc%2BT3b4zuHjpz1ug731VBEQm1M7QmaYJz9PFaBe2GOkPYHvfjMSYvfkH0npN3ObK%2BGkMuxoRQ3C%2BHC1dsrmeaZ0Gie%2FpBy9wBFg%3D%3D&traceId=0b1b171c15631736427933651e&relationId=2169913634&union_lens=lensId:0b0b6072_0c2f_16bf468e245_1a9d&xId=qUhQs2d8eBNTwrGy48P7lz91vZSfR6DQ4GkI2QMdSsL5dsVKA71wJeuN3XwUqEDLNjT2gUKTSqVxd1nMmFzMJo","couponEndTime":"2019-07-17","couponRemainCount":"69000","couponStartTime":"2019-07-11","couponTotalCount":"100000","estimatedEarn":"7.16","hasCollect":"false","itemId":"590359612359","itemUrl":"https://detail.m.tmall.com/item.htm?id=590359612359","payPrice":"19.90","pictUrl":"https://img.alicdn.com/bao/uploaded/i1/2200714013591/O1CN01amsDGK1cOioSIQBEJ_!!0-item_pic.jpg","shopPict":"","shopTitle":"丸颜堂旗舰店","shopType":"天猫","shopUrl":"http://shop.m.taobao.com/shop/shop_index.htm?user_id=2200714013591","smallImages":"https://img.alicdn.com/i1/2200714013591/O1CN01BPARVe1cOioT0CyBC_!!2200714013591.jpg,https://img.alicdn.com/i2/2200714013591/O1CN018xMdcf1cOioXQQ5Kb_!!2200714013591.jpg,https://img.alicdn.com/i3/2200714013591/O1CN01VGRYrU1cOioThNDaZ_!!2200714013591.jpg,https://img.alicdn.com/i4/2200714013591/O1CN012O09nO1cOioNIeceC_!!2200714013591.jpg","title":"丸颜堂红豆薏米芡实茶 赤小豆薏仁男女茶","upgradeEarn":"8.35","userType":"1","volume":"523470","zkFinalPrice":"59.9"}
     */

    private String authUrl;
    private GoodsDetailBean goodsDetail;

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public GoodsDetailBean getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(GoodsDetailBean goodsDetail) {
        this.goodsDetail = goodsDetail;
    }

    public static class GoodsDetailBean {
        /**
         * couponAmount : 40
         * couponClickUrl : https://uland.taobao.com/coupon/edetail?e=im0KY%2FiwYHwGQASttHIRqTivyjhlPdbagL9MdCDWBf0lRDfdDSQcUVTXEGqjSuiPI6sFaWJ2YBL085mp2pSHBwaSXjoOPssCDSh3zlT4Ss4uFYaruP%2Frc%2BT3b4zuHjpz1ug731VBEQm1M7QmaYJz9PFaBe2GOkPYHvfjMSYvfkH0npN3ObK%2BGkMuxoRQ3C%2BHC1dsrmeaZ0Gie%2FpBy9wBFg%3D%3D&traceId=0b1b171c15631736427933651e&relationId=2169913634&union_lens=lensId:0b0b6072_0c2f_16bf468e245_1a9d&xId=qUhQs2d8eBNTwrGy48P7lz91vZSfR6DQ4GkI2QMdSsL5dsVKA71wJeuN3XwUqEDLNjT2gUKTSqVxd1nMmFzMJo
         * couponEndTime : 2019-07-17
         * couponRemainCount : 69000
         * couponStartTime : 2019-07-11
         * couponTotalCount : 100000
         * estimatedEarn : 7.16
         * hasCollect : false
         * itemId : 590359612359
         * itemUrl : https://detail.m.tmall.com/item.htm?id=590359612359
         * payPrice : 19.90
         * pictUrl : https://img.alicdn.com/bao/uploaded/i1/2200714013591/O1CN01amsDGK1cOioSIQBEJ_!!0-item_pic.jpg
         * shopPict :
         * shopTitle : 丸颜堂旗舰店
         * shopType : 天猫
         * shopUrl : http://shop.m.taobao.com/shop/shop_index.htm?user_id=2200714013591
         * smallImages : https://img.alicdn.com/i1/2200714013591/O1CN01BPARVe1cOioT0CyBC_!!2200714013591.jpg,https://img.alicdn.com/i2/2200714013591/O1CN018xMdcf1cOioXQQ5Kb_!!2200714013591.jpg,https://img.alicdn.com/i3/2200714013591/O1CN01VGRYrU1cOioThNDaZ_!!2200714013591.jpg,https://img.alicdn.com/i4/2200714013591/O1CN012O09nO1cOioNIeceC_!!2200714013591.jpg
         * title : 丸颜堂红豆薏米芡实茶 赤小豆薏仁男女茶
         * upgradeEarn : 8.35
         * userType : 1
         * volume : 523470
         * zkFinalPrice : 59.9
         */

        private String couponAmount;
        private String couponClickUrl;
        private String couponEndTime;
        private String couponRemainCount;
        private String couponStartTime;
        private String couponTotalCount;
        private String estimatedEarn;
        private String hasCollect;
        private String itemId;
        private String itemUrl;
        private String payPrice;
        private String pictUrl;
        private String shopPict;
        private String shopTitle;
        private String shopType;
        private String shopUrl;
        private String sellerId;
        private String smallImages;
        private String title;
        private String upgradeEarn;
        private String userType;
        private String volume;
        private String zkFinalPrice;
        private String upgradeEarnInfo;

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        public String getUpgradeEarnInfo() {
            return upgradeEarnInfo;
        }

        public void setUpgradeEarnInfo(String upgradeEarnInfo) {
            this.upgradeEarnInfo = upgradeEarnInfo;
        }

        public String getCouponAmount() {
            return couponAmount;
        }

        public void setCouponAmount(String couponAmount) {
            this.couponAmount = couponAmount;
        }

        public String getCouponClickUrl() {
            return couponClickUrl;
        }

        public void setCouponClickUrl(String couponClickUrl) {
            this.couponClickUrl = couponClickUrl;
        }

        public String getCouponEndTime() {
            return couponEndTime;
        }

        public void setCouponEndTime(String couponEndTime) {
            this.couponEndTime = couponEndTime;
        }

        public String getCouponRemainCount() {
            return couponRemainCount;
        }

        public void setCouponRemainCount(String couponRemainCount) {
            this.couponRemainCount = couponRemainCount;
        }

        public String getCouponStartTime() {
            return couponStartTime;
        }

        public void setCouponStartTime(String couponStartTime) {
            this.couponStartTime = couponStartTime;
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

        public String getItemUrl() {
            return itemUrl;
        }

        public void setItemUrl(String itemUrl) {
            this.itemUrl = itemUrl;
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

        public String getShopPict() {
            return shopPict;
        }

        public void setShopPict(String shopPict) {
            this.shopPict = shopPict;
        }

        public String getShopTitle() {
            return shopTitle;
        }

        public void setShopTitle(String shopTitle) {
            this.shopTitle = shopTitle;
        }

        public String getShopType() {
            return shopType;
        }

        public void setShopType(String shopType) {
            this.shopType = shopType;
        }

        public String getShopUrl() {
            return shopUrl;
        }

        public void setShopUrl(String shopUrl) {
            this.shopUrl = shopUrl;
        }

        public String getSmallImages() {
            return smallImages;
        }

        public void setSmallImages(String smallImages) {
            this.smallImages = smallImages;
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
