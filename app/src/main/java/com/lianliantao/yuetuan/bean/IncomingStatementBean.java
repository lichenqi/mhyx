package com.lianliantao.yuetuan.bean;

public class IncomingStatementBean extends BaseBean {

    /**
     * balanceInfo : {"myMoney":"0","myOrderNum":"0","teamMoney":"0","teamOrderNum":"0","totalMoney":"0"}
     */

    private BalanceInfoBean balanceInfo;

    public BalanceInfoBean getBalanceInfo() {
        return balanceInfo;
    }

    public void setBalanceInfo(BalanceInfoBean balanceInfo) {
        this.balanceInfo = balanceInfo;
    }

    public static class BalanceInfoBean {
        /**
         * myMoney : 0
         * myOrderNum : 0
         * teamMoney : 0
         * teamOrderNum : 0
         * totalMoney : 0
         */

        private String myMoney;
        private String myOrderNum;
        private String teamMoney;
        private String teamOrderNum;
        private String totalMoney;

        public String getMyMoney() {
            return myMoney;
        }

        public void setMyMoney(String myMoney) {
            this.myMoney = myMoney;
        }

        public String getMyOrderNum() {
            return myOrderNum;
        }

        public void setMyOrderNum(String myOrderNum) {
            this.myOrderNum = myOrderNum;
        }

        public String getTeamMoney() {
            return teamMoney;
        }

        public void setTeamMoney(String teamMoney) {
            this.teamMoney = teamMoney;
        }

        public String getTeamOrderNum() {
            return teamOrderNum;
        }

        public void setTeamOrderNum(String teamOrderNum) {
            this.teamOrderNum = teamOrderNum;
        }

        public String getTotalMoney() {
            return totalMoney;
        }

        public void setTotalMoney(String totalMoney) {
            this.totalMoney = totalMoney;
        }
    }
}
