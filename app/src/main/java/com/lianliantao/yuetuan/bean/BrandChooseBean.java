package com.lianliantao.yuetuan.bean;

import java.util.List;

public class BrandChooseBean extends BaseBean {


    /**
     * bannerInfo : [{"brandDetail":"枣就知道你，楼兰蜜语！楼兰蜜语连续十二年新疆特产销量领先，成为线上红枣热销品牌，我们始终只专注西域特产美食领域，以天然绿色食品及完善可靠的服务赢得百万客户好评。","brandName":"楼兰蜜语","goodsInfo":[{"couponAmount":"15","estimatedEarn":"0.00","itemId":"592783311282","payPrice":"13.50","pictUrl":"https://gd4.alicdn.com/imgextra/i4/2570507997/O1CN01iT4wYM28wfrC1x3SI_!!2570507997.png_310x310.jpg"},{"couponAmount":"15","estimatedEarn":"0.00","itemId":"567896116141","payPrice":"24.90","pictUrl":"https://img.alicdn.com/imgextra/i1/2056595595/O1CN019hpbu11rCYVFEHYKU_!!2056595595.jpg_310x310.jpg"},{"couponAmount":"0","estimatedEarn":"0.24","itemId":"559444671681","payPrice":"9.90","pictUrl":"https://img.alicdn.com/imgextra/i3/1669216803/O1CN014m0KyF207oxMtQwyS_!!1669216803.jpg_310x310.jpg"}],"logo":"https://img.alicdn.com/bao/uploaded///img.taobaocdn.com/tps/TB1.VHdMPDpK1RjSZFrXXa78VXa","shopUrl":"http://shop.m.taobao.com/shop/shop_index.htm?user_id=1783198133"},{"brandDetail":"TCL集团股份有限公司是中国最大、全球性规模经营的消费类电子企业集团之一，旗下有四家上市公司并形成五大产业。TCL创立于1981年，秉承敬业奉献、锐意创新的企业精神，迅速发展成为中国电子信息产业中的佼佼者。2014年TCL品牌价值668.59亿，继续蝉联中国彩电业第一品牌。","brandName":"TCL(TCL)","goodsInfo":[{"couponAmount":"200","estimatedEarn":"0.00","itemId":"530376957803","payPrice":"1799.00","pictUrl":"https://img.alicdn.com/imgextra/i1/61072423/O1CN01iPNchT1TlmCs1Xiy0_!!61072423.jpg_310x310.jpg"},{"couponAmount":"200","estimatedEarn":"0.00","itemId":"593979804648","payPrice":"1379.00","pictUrl":"https://img.alicdn.com/imgextra/i1/2201226897289/O1CN013zQBeA23iPS1rs38N_!!2201226897289.jpg_310x310.jpg"},{"couponAmount":"150","estimatedEarn":"0.00","itemId":"594346366327","payPrice":"1229.00","pictUrl":"https://img.alicdn.com/imgextra/i2/2201226897289/O1CN01QRFF4T23iPRyt1ETR_!!2201226897289.jpg_310x310.jpg"}],"logo":"https://img.alicdn.com/bao/uploaded///img.taobaocdn.com/tps/TB1sp2Vz4jaK1RjSZFAXXbdLFXa","shopUrl":"http://shop.m.taobao.com/shop/shop_index.htm?user_id=1589163849"}]
     * totalResults : 458
     */

    private String totalResults;
    private List<BannerInfoBean> bannerInfo;

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public List<BannerInfoBean> getBannerInfo() {
        return bannerInfo;
    }

    public void setBannerInfo(List<BannerInfoBean> bannerInfo) {
        this.bannerInfo = bannerInfo;
    }

    public static class BannerInfoBean {
        /**
         * brandDetail : 枣就知道你，楼兰蜜语！楼兰蜜语连续十二年新疆特产销量领先，成为线上红枣热销品牌，我们始终只专注西域特产美食领域，以天然绿色食品及完善可靠的服务赢得百万客户好评。
         * brandName : 楼兰蜜语
         * goodsInfo : [{"couponAmount":"15","estimatedEarn":"0.00","itemId":"592783311282","payPrice":"13.50","pictUrl":"https://gd4.alicdn.com/imgextra/i4/2570507997/O1CN01iT4wYM28wfrC1x3SI_!!2570507997.png_310x310.jpg"},{"couponAmount":"15","estimatedEarn":"0.00","itemId":"567896116141","payPrice":"24.90","pictUrl":"https://img.alicdn.com/imgextra/i1/2056595595/O1CN019hpbu11rCYVFEHYKU_!!2056595595.jpg_310x310.jpg"},{"couponAmount":"0","estimatedEarn":"0.24","itemId":"559444671681","payPrice":"9.90","pictUrl":"https://img.alicdn.com/imgextra/i3/1669216803/O1CN014m0KyF207oxMtQwyS_!!1669216803.jpg_310x310.jpg"}]
         * logo : https://img.alicdn.com/bao/uploaded///img.taobaocdn.com/tps/TB1.VHdMPDpK1RjSZFrXXa78VXa
         * shopUrl : http://shop.m.taobao.com/shop/shop_index.htm?user_id=1783198133
         */

        private String brandDetail;
        private String brandName;
        private String logo;
        private String shopUrl;
        private String bid;
        private List<GoodsInfoBean> goodsInfo;
        private String shopId;

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getBid() {
            return bid;
        }

        public void setBid(String bid) {
            this.bid = bid;
        }

        public String getBrandDetail() {
            return brandDetail;
        }

        public void setBrandDetail(String brandDetail) {
            this.brandDetail = brandDetail;
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

        public String getShopUrl() {
            return shopUrl;
        }

        public void setShopUrl(String shopUrl) {
            this.shopUrl = shopUrl;
        }

        public List<GoodsInfoBean> getGoodsInfo() {
            return goodsInfo;
        }

        public void setGoodsInfo(List<GoodsInfoBean> goodsInfo) {
            this.goodsInfo = goodsInfo;
        }

        public static class GoodsInfoBean {
            /**
             * couponAmount : 15
             * estimatedEarn : 0.00
             * itemId : 592783311282
             * payPrice : 13.50
             * pictUrl : https://gd4.alicdn.com/imgextra/i4/2570507997/O1CN01iT4wYM28wfrC1x3SI_!!2570507997.png_310x310.jpg
             */

            private String couponAmount;
            private String estimatedEarn;
            private String itemId;
            private String payPrice;
            private String pictUrl;

            public String getCouponAmount() {
                return couponAmount;
            }

            public void setCouponAmount(String couponAmount) {
                this.couponAmount = couponAmount;
            }

            public String getEstimatedEarn() {
                return estimatedEarn;
            }

            public void setEstimatedEarn(String estimatedEarn) {
                this.estimatedEarn = estimatedEarn;
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
        }
    }
}
