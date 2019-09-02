package com.lianliantao.yuetuan.bean;

import java.util.List;

public class BrandBean extends BaseBean {


    /**
     * brandInfo : [{"brandName":"美旅(AMERICAN TOURISTER)","logo":"https://img.alicdn.com/bao/uploaded///img.taobaocdn.com/tps/TB1A9Eicf5TBuNjSspmXXaDRVXa","rebateRate":"平均返佣5%","shopUrl":"http://shop.m.taobao.com/shop/shop_index.htm?user_id=498385633"},{"brandName":"酷蔓(COOME)","logo":"https://img.alicdn.com/bao/uploaded/d2/8e/TB1X6TKhStYBeNjSspkSuvU8VXa.jpg_80x80.jpg","rebateRate":"平均返佣5%","shopUrl":""},{"brandName":"新秀丽(Samsonite)","logo":"https://img.alicdn.com/bao/uploaded///img.taobaocdn.com/tps/TB16EzQm4rI8KJjy0FpXXb5hVXa","rebateRate":"平均返佣5%","shopUrl":"http://shop.m.taobao.com/shop/shop_index.htm?user_id=674134672"},{"brandName":"菲安妮(Fion)","logo":"https://img.alicdn.com/bao/uploaded///img.taobaocdn.com/tps/TB1_Uhqnf2H8KJjy1zkXXXr7pXa","rebateRate":"平均返佣5%","shopUrl":"http://shop.m.taobao.com/shop/shop_index.htm?user_id=732434891"},{"brandName":"浩沙(hosa)","logo":"https://img.alicdn.com/bao/uploaded///img.taobaocdn.com/tps/TB1Bcs1jb_I8KJjy1XaXXbsxpXa","rebateRate":"平均返佣5%","shopUrl":"http://shop.m.taobao.com/shop/shop_index.htm?user_id=2432775879"},{"brandName":"斯伯丁(Spalding)","logo":"https://img.alicdn.com/bao/uploaded///img.taobaocdn.com/tps/TB1UaeCbYSYBuNjSspiXXXNzpXa","rebateRate":"平均返佣5%","shopUrl":"http://shop.m.taobao.com/shop/shop_index.htm?user_id=3823786400"},{"brandName":"洲克(Zoke)","logo":"https://img.alicdn.com/bao/uploaded/i3/796913055/O1CN01WIQMTe1YREVJTx6Cl_!!796913055.png_80x80.jpg","rebateRate":"平均返佣5%","shopUrl":"http://shop.m.taobao.com/shop/shop_index.htm?user_id=3217234243"},{"brandName":"探路者","logo":"https://img.alicdn.com/bao/uploaded///img.taobaocdn.com/tps/TB1h5YNb9tYBeNjSspaXXaOOFXa","rebateRate":"平均返佣5%","shopUrl":"http://shop.m.taobao.com/shop/shop_index.htm?user_id=71851678"}]
     * cateInfo : [{"cateName":"精选","id":0},{"cateName":"居家","id":2},{"cateName":"美食","id":3},{"cateName":"母婴","id":4},{"cateName":"美妆","id":5},{"cateName":"女装","id":6},{"cateName":"数码","id":7},{"cateName":"车品","id":8},{"cateName":"内衣","id":9},{"cateName":"家装","id":10},{"cateName":"鞋品","id":11},{"cateName":"男装","id":12},{"cateName":"配饰","id":13},{"cateName":"户外","id":14},{"cateName":"箱包","id":15}]
     * totalResults : 8
     */

    private String totalResults;
    private List<BrandInfoBean> brandInfo;
    private List<CateInfoBean> cateInfo;

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public List<BrandInfoBean> getBrandInfo() {
        return brandInfo;
    }

    public void setBrandInfo(List<BrandInfoBean> brandInfo) {
        this.brandInfo = brandInfo;
    }

    public List<CateInfoBean> getCateInfo() {
        return cateInfo;
    }

    public void setCateInfo(List<CateInfoBean> cateInfo) {
        this.cateInfo = cateInfo;
    }

    public static class BrandInfoBean {
        /**
         * brandName : 美旅(AMERICAN TOURISTER)
         * logo : https://img.alicdn.com/bao/uploaded///img.taobaocdn.com/tps/TB1A9Eicf5TBuNjSspmXXaDRVXa
         * rebateRate : 平均返佣5%
         * shopUrl : http://shop.m.taobao.com/shop/shop_index.htm?user_id=498385633
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

    public static class CateInfoBean {
        /**
         * cateName : 精选
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
}
