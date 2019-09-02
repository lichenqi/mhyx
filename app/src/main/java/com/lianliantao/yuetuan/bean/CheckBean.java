package com.lianliantao.yuetuan.bean;

public class CheckBean extends BaseBean {

    /**
     * userInfo : {"avatar":"http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eo70Y5jG1NrGeOgqT4UMJTF2lA7ibCDFqOGSr5xaRhyjzOKwY9cQokPvZ1JyWW8gtJz8gNibNG7ibLUA/132","invitationCode":"445121","nickname":"超超"}
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
         * invitationCode : 445121
         * nickname : 超超
         */

        private String avatar;
        private String invitationCode;
        private String nickname;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getInvitationCode() {
            return invitationCode;
        }

        public void setInvitationCode(String invitationCode) {
            this.invitationCode = invitationCode;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
