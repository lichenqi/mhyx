package com.lianliantao.yuetuan.bean;

import java.util.List;

public class OrderListBean extends BaseBean {

    /**
     * orderInfo : [{"alipayTotalPrice":"0","avatar":"http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eo70Y5jG1NrGeOgqT4UMJTF2lA7ibCDFqOGSr5xaRhyjzOKwY9cQokPvZ1JyWW8gtJz8gNibNG7ibLUA/132","createTime":"2019-07-23 17:28:15","itemTitle":"爱仕达硅胶铲铁铲不粘锅适用护锅铲家用厨房炒菜铲子锅铲厨具套装","nickName":"昵称","orderType":"天猫","pictUrl":"https://img.alicdn.com/imgextra/i1/2935507133/O1CN01ePNfa322YxgU9KsQW_!!2935507133.jpg_310x310.jpg","seller":"0","tkStatus":"13","tradeId":"549952770124513904"}]
     * totalResults : 1
     */

    private String totalResults;
    private List<OrderInfoBean> orderInfo;

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public List<OrderInfoBean> getOrderInfo() {
        return orderInfo;
    }

    public void setOrderInfo(List<OrderInfoBean> orderInfo) {
        this.orderInfo = orderInfo;
    }

    public static class OrderInfoBean {
        /**
         * alipayTotalPrice : 0
         * avatar : http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eo70Y5jG1NrGeOgqT4UMJTF2lA7ibCDFqOGSr5xaRhyjzOKwY9cQokPvZ1JyWW8gtJz8gNibNG7ibLUA/132
         * createTime : 2019-07-23 17:28:15
         * itemTitle : 爱仕达硅胶铲铁铲不粘锅适用护锅铲家用厨房炒菜铲子锅铲厨具套装
         * nickName : 昵称
         * orderType : 天猫
         * pictUrl : https://img.alicdn.com/imgextra/i1/2935507133/O1CN01ePNfa322YxgU9KsQW_!!2935507133.jpg_310x310.jpg
         * seller : 0
         * tkStatus : 13
         * tradeId : 549952770124513904
         */

        private String alipayTotalPrice;
        private String avatar;
        private String createTime;
        private String itemTitle;
        private String nickName;
        private String orderType;
        private String pictUrl;
        private String seller;
        private String tkStatus;
        private String tradeId;
        private String itemId;

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getAlipayTotalPrice() {
            return alipayTotalPrice;
        }

        public void setAlipayTotalPrice(String alipayTotalPrice) {
            this.alipayTotalPrice = alipayTotalPrice;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getItemTitle() {
            return itemTitle;
        }

        public void setItemTitle(String itemTitle) {
            this.itemTitle = itemTitle;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getPictUrl() {
            return pictUrl;
        }

        public void setPictUrl(String pictUrl) {
            this.pictUrl = pictUrl;
        }

        public String getSeller() {
            return seller;
        }

        public void setSeller(String seller) {
            this.seller = seller;
        }

        public String getTkStatus() {
            return tkStatus;
        }

        public void setTkStatus(String tkStatus) {
            this.tkStatus = tkStatus;
        }

        public String getTradeId() {
            return tradeId;
        }

        public void setTradeId(String tradeId) {
            this.tradeId = tradeId;
        }
    }
}
