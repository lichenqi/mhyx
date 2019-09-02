package com.lianliantao.yuetuan.bean;

import java.util.List;

public class GoodsDecribeBean extends BaseBean {

    /**
     * evaluateContent : 宝贝超级喜欢啊
     * evaluateTotal : 34
     * evaluateUrl : https://h5.m.taobao.com/app/rate/www/rate-list/index.html?auctionNumId=589387567370
     * evaluateUserLogo : https://gtms03.alicdn.com/tps/i3/TB1yeWeIFXXXXX5XFXXuAZJYXXX-210-210.png_80x80.jpg
     * evaluateUsername : 虎**店
     * itemInfo : []
     * itemInfoUrl : https://mdetail.tmall.com/templates/pages/desc?id=589387567370
     * shopEvaluate : [{"rank":"down","score":"4.7 ","title":"宝贝描述"},{"rank":"equal","score":"4.8 ","title":"卖家服务"},{"rank":"up","score":"4.8 ","title":"物流服务"}]
     */

    private String evaluateContent;
    private String evaluateTotal;
    private String evaluateUrl;
    private String evaluateUserLogo;
    private String evaluateUsername;
    private String itemInfoUrl;
    private List<ShopEvaluateBean> shopEvaluate;
    private String shopId;

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getEvaluateContent() {
        return evaluateContent;
    }

    public void setEvaluateContent(String evaluateContent) {
        this.evaluateContent = evaluateContent;
    }

    public String getEvaluateTotal() {
        return evaluateTotal;
    }

    public void setEvaluateTotal(String evaluateTotal) {
        this.evaluateTotal = evaluateTotal;
    }

    public String getEvaluateUrl() {
        return evaluateUrl;
    }

    public void setEvaluateUrl(String evaluateUrl) {
        this.evaluateUrl = evaluateUrl;
    }

    public String getEvaluateUserLogo() {
        return evaluateUserLogo;
    }

    public void setEvaluateUserLogo(String evaluateUserLogo) {
        this.evaluateUserLogo = evaluateUserLogo;
    }

    public String getEvaluateUsername() {
        return evaluateUsername;
    }

    public void setEvaluateUsername(String evaluateUsername) {
        this.evaluateUsername = evaluateUsername;
    }

    public String getItemInfoUrl() {
        return itemInfoUrl;
    }

    public void setItemInfoUrl(String itemInfoUrl) {
        this.itemInfoUrl = itemInfoUrl;
    }

    public List<ShopEvaluateBean> getShopEvaluate() {
        return shopEvaluate;
    }

    public void setShopEvaluate(List<ShopEvaluateBean> shopEvaluate) {
        this.shopEvaluate = shopEvaluate;
    }

    public static class ShopEvaluateBean {
        /**
         * rank : down
         * score : 4.7
         * title : 宝贝描述
         */

        private String rank;
        private String score;
        private String title;

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
