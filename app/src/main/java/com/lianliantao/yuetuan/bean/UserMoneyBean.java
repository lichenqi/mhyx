package com.lianliantao.yuetuan.bean;

public class UserMoneyBean extends BaseBean {

    /**
     * balanceInfo : {"balance":"","lastMonthEarn":"","lastMonthSettlement":"","pendingSettlement":"","thisMonthEarn":"","thisMonthSettlement":"","todayEarn":"","withdraw":"","yesterdayEarn":""}
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
         * balance :
         * lastMonthEarn :
         * lastMonthSettlement :
         * pendingSettlement :
         * thisMonthEarn :
         * thisMonthSettlement :
         * todayEarn :
         * withdraw :
         * yesterdayEarn :
         */

        private String balance;
        private String lastMonthEarn;
        private String lastMonthSettlement;
        private String pendingSettlement;
        private String thisMonthEarn;
        private String thisMonthSettlement;
        private String todayEarn;
        private String withdraw;
        private String yesterdayEarn;
        private String income;

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getLastMonthEarn() {
            return lastMonthEarn;
        }

        public void setLastMonthEarn(String lastMonthEarn) {
            this.lastMonthEarn = lastMonthEarn;
        }

        public String getLastMonthSettlement() {
            return lastMonthSettlement;
        }

        public void setLastMonthSettlement(String lastMonthSettlement) {
            this.lastMonthSettlement = lastMonthSettlement;
        }

        public String getPendingSettlement() {
            return pendingSettlement;
        }

        public void setPendingSettlement(String pendingSettlement) {
            this.pendingSettlement = pendingSettlement;
        }

        public String getThisMonthEarn() {
            return thisMonthEarn;
        }

        public void setThisMonthEarn(String thisMonthEarn) {
            this.thisMonthEarn = thisMonthEarn;
        }

        public String getThisMonthSettlement() {
            return thisMonthSettlement;
        }

        public void setThisMonthSettlement(String thisMonthSettlement) {
            this.thisMonthSettlement = thisMonthSettlement;
        }

        public String getTodayEarn() {
            return todayEarn;
        }

        public void setTodayEarn(String todayEarn) {
            this.todayEarn = todayEarn;
        }

        public String getWithdraw() {
            return withdraw;
        }

        public void setWithdraw(String withdraw) {
            this.withdraw = withdraw;
        }

        public String getYesterdayEarn() {
            return yesterdayEarn;
        }

        public void setYesterdayEarn(String yesterdayEarn) {
            this.yesterdayEarn = yesterdayEarn;
        }
    }
}
