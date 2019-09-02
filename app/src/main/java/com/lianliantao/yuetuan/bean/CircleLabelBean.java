package com.lianliantao.yuetuan.bean;

import java.io.Serializable;
import java.util.List;

public class CircleLabelBean extends BaseBean {


    private List<CateInfoBean> cateInfo;

    public List<CateInfoBean> getCateInfo() {
        return cateInfo;
    }

    public void setCateInfo(List<CateInfoBean> cateInfo) {
        this.cateInfo = cateInfo;
    }

    public static class CateInfoBean {
        /**
         * cidInfo : []
         * name : 商品推荐
         * pid : 1
         */

        private String name;
        private String pid;
        private List<CidInfo> cidInfo;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public List<CidInfo> getCidInfo() {
            return cidInfo;
        }

        public void setCidInfo(List<CidInfo> cidInfo) {
            this.cidInfo = cidInfo;
        }
    }

    public class CidInfo implements Serializable {

        private String cid;
        private String name;
        private boolean checked;

        public boolean isChecked() {
            return checked;
        }

        public void setChecked(boolean checked) {
            this.checked = checked;
        }

        public String getCid() {
            return cid;
        }

        public void setCid(String cid) {
            this.cid = cid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
