package com.lianliantao.yuetuan.bean;

import java.util.List;

public class TeamMemberBean extends BaseBean{


    /**
     * teamList : [{"avatar":"http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTLQJnwTZPPNbIe6BKsv3rNAibrVeug6nZ3vWLZSOPyyxWeRDWu4Xu5PXLsiaTY9qf9ljhTZv5UVh9cw/132","createTime":"2019-07-25 17:23:49","directMember":"0","level":"1","mobile":"18610943053","myEarn":"0","nickname":"L-ING","sex":"1"}]
     * totalResults : 1
     */

    private int totalResults;
    private List<TeamListBean> teamList;

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<TeamListBean> getTeamList() {
        return teamList;
    }

    public void setTeamList(List<TeamListBean> teamList) {
        this.teamList = teamList;
    }

    public static class TeamListBean {
        /**
         * avatar : http://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTLQJnwTZPPNbIe6BKsv3rNAibrVeug6nZ3vWLZSOPyyxWeRDWu4Xu5PXLsiaTY9qf9ljhTZv5UVh9cw/132
         * createTime : 2019-07-25 17:23:49
         * directMember : 0
         * level : 1
         * mobile : 18610943053
         * myEarn : 0
         * nickname : L-ING
         * sex : 1
         */

        private String avatar;
        private String createTime;
        private String directMember;
        private String level;
        private String mobile;
        private String myEarn;
        private String nickname;
        private String sex;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getDirectMember() {
            return directMember;
        }

        public void setDirectMember(String directMember) {
            this.directMember = directMember;
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

        public String getMyEarn() {
            return myEarn;
        }

        public void setMyEarn(String myEarn) {
            this.myEarn = myEarn;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }
}
