package com.lianliantao.yuetuan.bean;

public class LoginBean extends BaseBean {


    /**
     * userInfo : {"appLink":"http://suo.im/4vHC8V","authUrl":"https://oauth.taobao.com/authorize?response_type=code&client_id=26065425&redirect_uri=http://www.taokemafu.com/index/index/taobaoLogin?uId=MzAgNjQ1ZGE3NzgyZWU1ZjFmZmU0ZjA5YzRlNTI5OTE2M2ItLWY0MjBkMjU4ZmVmZDY2YWQ5OWQ3NDU4Y2RhYmYxYmZi&state=1212&view=wap","avatar":"http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eo70Y5jG1NrGeOgqT4UMJTF2lA7ibCDFqOGSr5xaRhyjzOKwY9cQokPvZ1JyWW8gtJz8gNibNG7ibLUA/132","hasBindTbk":"true","hasUserLink":"true","invitationCode":"999999","level":2,"mobile":"13367267317","nickName":"就这样看着你就好","uId":"MzAgNjQ1ZGE3NzgyZWU1ZjFmZmU0ZjA5YzRlNTI5OTE2M2ItLWY0MjBkMjU4ZmVmZDY2YWQ5OWQ3NDU4Y2RhYmYxYmZi","userRank":"超级会员","wechatNumber":""}
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
         * appLink : http://suo.im/4vHC8V
         * authUrl : https://oauth.taobao.com/authorize?response_type=code&client_id=26065425&redirect_uri=http://www.taokemafu.com/index/index/taobaoLogin?uId=MzAgNjQ1ZGE3NzgyZWU1ZjFmZmU0ZjA5YzRlNTI5OTE2M2ItLWY0MjBkMjU4ZmVmZDY2YWQ5OWQ3NDU4Y2RhYmYxYmZi&state=1212&view=wap
         * avatar : http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eo70Y5jG1NrGeOgqT4UMJTF2lA7ibCDFqOGSr5xaRhyjzOKwY9cQokPvZ1JyWW8gtJz8gNibNG7ibLUA/132
         * hasBindTbk : true
         * hasUserLink : true
         * invitationCode : 999999
         * level : 2
         * mobile : 13367267317
         * nickName : 就这样看着你就好
         * uId : MzAgNjQ1ZGE3NzgyZWU1ZjFmZmU0ZjA5YzRlNTI5OTE2M2ItLWY0MjBkMjU4ZmVmZDY2YWQ5OWQ3NDU4Y2RhYmYxYmZi
         * userRank : 超级会员
         * wechatNumber :
         */

        private String appLink;
        private String authUrl;
        private String avatar;
        private String hasBindTbk;
        private String hasUserLink;
        private String invitationCode;
        private String level;
        private String mobile;
        private String nickName;
        private String uId;
        private String userRank;
        private String wechatNumber;
        private String uniqId;

        public String getUniqId() {
            return uniqId;
        }

        public void setUniqId(String uniqId) {
            this.uniqId = uniqId;
        }

        public String getAppLink() {
            return appLink;
        }

        public void setAppLink(String appLink) {
            this.appLink = appLink;
        }

        public String getAuthUrl() {
            return authUrl;
        }

        public void setAuthUrl(String authUrl) {
            this.authUrl = authUrl;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
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

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
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
    }
}
