package com.lianliantao.yuetuan.bean;

import java.util.List;

public class HomeListBean extends BaseBean {

    /**
     * goodsInfo : [{"couponAmount":"50","couponRemainCount":"100000","couponTotalCount":"100000","estimatedEarn":"27.48","hasCollect":"false","itemDescription":"","itemId":"592063544239","nick":"稻草人官方店","payPrice":"229.00","pictUrl":"https://gd1.alicdn.com/imgextra/i1/2864523376/O1CN01azHE2s1aoFdPiBgy4_!!2864523376.jpg_310x310.jpg","sales":"0","title":" 稻草人2019新款时尚单肩斜挎女包","upgradeEarn":"32.06","userType":"0","volume":"184","zkFinalPrice":"279.00"},{"couponAmount":"10","couponRemainCount":"99987","couponTotalCount":"100000","estimatedEarn":"1.54","hasCollect":"false","itemDescription":"","itemId":"597856429675","nick":"桃小心包袋TAOXIAOXIN","payPrice":"12.90","pictUrl":"https://gd2.alicdn.com/imgextra/i2/2540755735/O1CN01KPRpDw1sEfyl0tS2O_!!2540755735.jpg_310x310.jpg","sales":"0","title":" 【ins超火】网红小丸子单肩小方包","upgradeEarn":"1.80","userType":"0","volume":"77","zkFinalPrice":"22.90"}]
     * totalResults : 130629
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
         * couponAmount : 50
         * couponRemainCount : 100000
         * couponTotalCount : 100000
         * estimatedEarn : 27.48
         * hasCollect : false
         * itemDescription :
         * itemId : 592063544239
         * nick : 稻草人官方店
         * payPrice : 229.00
         * pictUrl : https://gd1.alicdn.com/imgextra/i1/2864523376/O1CN01azHE2s1aoFdPiBgy4_!!2864523376.jpg_310x310.jpg
         * sales : 0
         * title :  稻草人2019新款时尚单肩斜挎女包
         * upgradeEarn : 32.06
         * userType : 0
         * volume : 184
         * zkFinalPrice : 279.00
         */

        private String couponAmount;
        private String couponRemainCount;
        private String couponTotalCount;
        private String estimatedEarn;
        private String hasCollect;
        private String itemDescription;
        private String itemId;
        private String nick;
        private String payPrice;
        private String pictUrl;
        private String sales;
        private String title;
        private String upgradeEarn;
        private String userType;
        private String volume;
        private String zkFinalPrice;
        private boolean isDeleteShow;/*是否显示删除按钮*/
        private boolean isChecked;/*显示*/

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public boolean isDeleteShow() {
            return isDeleteShow;
        }

        public void setDeleteShow(boolean deleteShow) {
            isDeleteShow = deleteShow;
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

        public String getItemDescription() {
            return itemDescription;
        }

        public void setItemDescription(String itemDescription) {
            this.itemDescription = itemDescription;
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

        public String getSales() {
            return sales;
        }

        public void setSales(String sales) {
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
