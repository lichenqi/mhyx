package com.lianliantao.yuetuan.bean;

public class WXLoginBean extends BaseBean {

    /**
     * userInfo : {"avatar":"http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eo70Y5jG1NrGeOgqT4UMJTF2lA7ibCDFqOGSr5xaRhyjzOKwY9cQokPvZ1JyWW8gtJz8gNibNG7ibLUA/132","balance":"","hasBindTbk":"false","hasUserLink":"false","invitationCode":"749501","lastMonthEarn":"","mobile":"","nickName":"昵称","thisMonthEarn":"","todayEarn":"","uId":"MTQgNjQ1ZGE3NzgyZWU1ZjFmZmU0ZjA5YzRlNTI5OTE2M2ItLTg0YTBjODcxMWNkNWQ3YzFiY2Y1MmIwOTY2YTE3MTUz","userRank":"会员","wechatNumber":"","yesterdayEarn":""}
     */

    private UserInfoBean userInfo;

    public UserInfoBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoBean userInfo) {
        this.userInfo = userInfo;
    }

    public static class UserInfoBean {
        /**
         * avatar : http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eo70Y5jG1NrGeOgqT4UMJTF2lA7ibCDFqOGSr5xaRhyjzOKwY9cQokPvZ1JyWW8gtJz8gNibNG7ibLUA/132
         * balance :
         * hasBindTbk : false
         * hasUserLink : false
         * invitationCode : 749501
         * lastMonthEarn :
         * mobile :
         * nickName : 昵称
         * thisMonthEarn :
         * todayEarn :
         * uId : MTQgNjQ1ZGE3NzgyZWU1ZjFmZmU0ZjA5YzRlNTI5OTE2M2ItLTg0YTBjODcxMWNkNWQ3YzFiY2Y1MmIwOTY2YTE3MTUz
         * userRank : 会员
         * wechatNumber :
         * yesterdayEarn :
         */

        private String avatar;
        private String balance;
        private String hasBindTbk;
        private String hasUserLink;
        private String invitationCode;
        private String lastMonthEarn;
        private String mobile;
        private String nickName;
        private String thisMonthEarn;
        private String todayEarn;
        private String uId;
        private String userRank;
        private String wechatNumber;
        private String yesterdayEarn;
        private String level;

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
        }

        public String getHasBindTbk() {
            return hasBindTbk;
        }

        public void setHasBindTbk(String hasBindTbk) {
            this.hasBindTbk = hasBindTbk;
        }

        public String getHasUserLink() {
            return hasUserLink;
        }

        public void setHasUserLink(String hasUserLink) {
            this.hasUserLink = hasUserLink;
        }

        public String getInvitationCode() {
            return invitationCode;
        }

        public void setInvitationCode(String invitationCode) {
            this.invitationCode = invitationCode;
        }

        public String getLastMonthEarn() {
            return lastMonthEarn;
        }

        public void setLastMonthEarn(String lastMonthEarn) {
            this.lastMonthEarn = lastMonthEarn;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getThisMonthEarn() {
            return thisMonthEarn;
        }

        public void setThisMonthEarn(String thisMonthEarn) {
            this.thisMonthEarn = thisMonthEarn;
        }

        public String getTodayEarn() {
            return todayEarn;
        }

        public void setTodayEarn(String todayEarn) {
            this.todayEarn = todayEarn;
        }

        public String getUId() {
            return uId;
        }

        public void setUId(String uId) {
            this.uId = uId;
        }

        public String getUserRank() {
            return userRank;
        }

        public void setUserRank(String userRank) {
            this.userRank = userRank;
        }

        public String getWechatNumber() {
            return wechatNumber;
        }

        public void setWechatNumber(String wechatNumber) {
            this.wechatNumber = wechatNumber;
        }

        public String getYesterdayEarn() {
            return yesterdayEarn;
        }

        public void setYesterdayEarn(String yesterdayEarn) {
            this.yesterdayEarn = yesterdayEarn;
        }
    }
}
