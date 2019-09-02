package com.lianliantao.yuetuan.bean;

import java.util.List;

public class JieSuanBean extends BaseBean{

    /**
     * settlementLog : [{"actualMoney":"0.18","createTime":"2019-07-18 10:01:14","nickName":"超超","remarks":"","serviceFee":"0.02","settlementMoney":"0.2","subsidyAdd":"0"}]
     * totalResults : 1
     */

    private String totalResults;
    private List<SettlementLogBean> settlementLog;

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public List<SettlementLogBean> getSettlementLog() {
        return settlementLog;
    }

    public void setSettlementLog(List<SettlementLogBean> settlementLog) {
        this.settlementLog = settlementLog;
    }

    public static class SettlementLogBean {
        /**
         * actualMoney : 0.18
         * createTime : 2019-07-18 10:01:14
         * nickName : 超超
         * remarks :
         * serviceFee : 0.02
         * settlementMoney : 0.2
         * subsidyAdd : 0
         */

        private String actualMoney;
        private String createTime;
        private String nickName;
        private String remarks;
        private String serviceFee;
        private String settlementMoney;
        private String subsidyAdd;

        public String getActualMoney() {
            return actualMoney;
        }

        public void setActualMoney(String actualMoney) {
            this.actualMoney = actualMoney;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getServiceFee() {
            return serviceFee;
        }

        public void setServiceFee(String serviceFee) {
            this.serviceFee = serviceFee;
        }

        public String getSettlementMoney() {
            return settlementMoney;
        }

        public void setSettlementMoney(String settlementMoney) {
            this.settlementMoney = settlementMoney;
        }

        public String getSubsidyAdd() {
            return subsidyAdd;
        }

        public void setSubsidyAdd(String subsidyAdd) {
            this.subsidyAdd = subsidyAdd;
        }
    }
}
