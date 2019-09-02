package com.lianliantao.yuetuan.bean;

import java.io.Serializable;
import java.util.List;

public class CircleRecommendBean extends BaseBean {

    /**
     * info : [{"author":"xp","authorLogo":"https://img.alicdn.com/imgextra/i3/1633877670/O1CN01xltcBr26WuMmqMJrj_!!1633877670.jpg_310x310.jpg","createTime":"2019-07-27 15:47:48","description":"description","estimatedEarn":"4.30","goodsInfo":{"couponClickUrl":"","itemId":"563516532457","payPrice":"399.00","pictUrl":"https://img.alicdn.com/bao/uploaded/TB1I9L0SNjaK1RjSZKzXXXVwXXa.png_310x310.jpg","recommendInfo":"【价格】599.00元 【券后价】399.00元 -------- 复制这条口令，进入【Tao宝】即可抢购","title":"志高新款破壁料理机家用加热多功能全自动德国小型辅食养生豆浆","volume":"30874"},"id":"1","imgInfo":[{"itemId":"563973529181","url":"https://img.alicdn.com/imgextra/i3/1633877670/O1CN01xltcBr26WuMmqMJrj_!!1633877670.jpg_310x310.jpg"}],"shareNum":"14"}]
     * totalResults : 1
     */

    private String totalResults;
    private List<InfoBean> info;

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * author : xp
         * authorLogo : https://img.alicdn.com/imgextra/i3/1633877670/O1CN01xltcBr26WuMmqMJrj_!!1633877670.jpg_310x310.jpg
         * createTime : 2019-07-27 15:47:48
         * description : description
         * estimatedEarn : 4.30
         * goodsInfo : {"couponClickUrl":"","itemId":"563516532457","payPrice":"399.00","pictUrl":"https://img.alicdn.com/bao/uploaded/TB1I9L0SNjaK1RjSZKzXXXVwXXa.png_310x310.jpg","recommendInfo":"【价格】599.00元 【券后价】399.00元 -------- 复制这条口令，进入【Tao宝】即可抢购","title":"志高新款破壁料理机家用加热多功能全自动德国小型辅食养生豆浆","volume":"30874"}
         * id : 1
         * imgInfo : [{"itemId":"563973529181","url":"https://img.alicdn.com/imgextra/i3/1633877670/O1CN01xltcBr26WuMmqMJrj_!!1633877670.jpg_310x310.jpg"}]
         * shareNum : 14
         */

        private String author;
        private String authorLogo;
        private String createTime;
        private String description;
        private String estimatedEarn;
        private GoodsInfoBean goodsInfo;
        private String id;
        private String shareNum;
        private List<ImgInfoBean> imgInfo;

        public String getAuthor() {
            return author;
        }

        public void setAuthor(String author) {
            this.author = author;
        }

        public String getAuthorLogo() {
            return authorLogo;
        }

        public void setAuthorLogo(String authorLogo) {
            this.authorLogo = authorLogo;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getEstimatedEarn() {
            return estimatedEarn;
        }

        public void setEstimatedEarn(String estimatedEarn) {
            this.estimatedEarn = estimatedEarn;
        }

        public GoodsInfoBean getGoodsInfo() {
            return goodsInfo;
        }

        public void setGoodsInfo(GoodsInfoBean goodsInfo) {
            this.goodsInfo = goodsInfo;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShareNum() {
            return shareNum;
        }

        public void setShareNum(String shareNum) {
            this.shareNum = shareNum;
        }

        public List<ImgInfoBean> getImgInfo() {
            return imgInfo;
        }

        public void setImgInfo(List<ImgInfoBean> imgInfo) {
            this.imgInfo = imgInfo;
        }

        public static class GoodsInfoBean {
            /**
             * couponClickUrl :
             * itemId : 563516532457
             * payPrice : 399.00
             * pictUrl : https://img.alicdn.com/bao/uploaded/TB1I9L0SNjaK1RjSZKzXXXVwXXa.png_310x310.jpg
             * recommendInfo : 【价格】599.00元 【券后价】399.00元 -------- 复制这条口令，进入【Tao宝】即可抢购
             * title : 志高新款破壁料理机家用加热多功能全自动德国小型辅食养生豆浆
             * volume : 30874
             */

            private String couponClickUrl;
            private String itemId;
            private String payPrice;
            private String pictUrl;
            private String recommendInfo;
            private String title;
            private String volume;

            public String getCouponClickUrl() {
                return couponClickUrl;
            }

            public void setCouponClickUrl(String couponClickUrl) {
                this.couponClickUrl = couponClickUrl;
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

            public String getRecommendInfo() {
                return recommendInfo;
            }

            public void setRecommendInfo(String recommendInfo) {
                this.recommendInfo = recommendInfo;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public String getVolume() {
                return volume;
            }

            public void setVolume(String volume) {
                this.volume = volume;
            }
        }

        public static class ImgInfoBean implements Serializable {
            /**
             * itemId : 563973529181
             * url : https://img.alicdn.com/imgextra/i3/1633877670/O1CN01xltcBr26WuMmqMJrj_!!1633877670.jpg_310x310.jpg
             */

            private String itemId;
            private String url;

            public String getItemId() {
                return itemId;
            }

            public void setItemId(String itemId) {
                this.itemId = itemId;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
