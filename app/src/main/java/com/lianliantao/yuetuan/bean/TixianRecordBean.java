package com.lianliantao.yuetuan.bean;

import java.util.List;

public class TixianRecordBean extends BaseBean{

    /**
     * totalResults : 14
     * withdrawInfo : [{"createTime":"2019-07-27 11:16:24","mobile":"18827035411","money":"1","nickName":"超超","reason":"","status":"1"},{"createTime":"2019-07-27 11:16:16","mobile":"18827035411","money":"1","nickName":"超超","reason":"","status":"2"},{"createTime":"2019-07-27 11:16:00","mobile":"18827035411","money":"1","nickName":"超超","reason":"","status":"2"},{"createTime":"2019-07-27 11:15:56","mobile":"18827035411","money":"1","nickName":"超超","reason":"","status":"2"},{"createTime":"2019-07-27 11:07:38","mobile":"18827035411","money":"1","nickName":"超超","reason":"","status":"1"},{"createTime":"2019-07-27 11:07:20","mobile":"18827035411","money":"1","nickName":"超超","reason":"","status":"2"},{"createTime":"2019-07-27 11:07:08","mobile":"18827035411","money":"1","nickName":"超超","reason":"","status":"1"},{"createTime":"2019-07-27 11:00:19","mobile":"18827035411","money":"1","nickName":"超超","reason":"","status":"0"},{"createTime":"2019-07-27 10:59:03","mobile":"18827035411","money":"1","nickName":"超超","reason":"","status":"2"},{"createTime":"2019-07-27 10:59:01","mobile":"18827035411","money":"1","nickName":"超超","reason":"","status":"2"}]
     */

    private String totalResults;
    private List<WithdrawInfoBean> withdrawInfo;

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public List<WithdrawInfoBean> getWithdrawInfo() {
        return withdrawInfo;
    }

    public void setWithdrawInfo(List<WithdrawInfoBean> withdrawInfo) {
        this.withdrawInfo = withdrawInfo;
    }

    public static class WithdrawInfoBean {
        /**
         * createTime : 2019-07-27 11:16:24
         * mobile : 18827035411
         * money : 1
         * nickName : 超超
         * reason :
         * status : 1
         */

        private String createTime;
        private String mobile;
        private String money;
        private String nickName;
        private String reason;
        private String status;

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getMoney() {
            return money;
        }

        public void setMoney(String money) {
            this.money = money;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getReason() {
            return reason;
        }

        public void setReason(String reason) {
            this.reason = reason;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
