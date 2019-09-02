package com.lianliantao.yuetuan.bean;

public class MyTeamBean extends BaseBean{

    /**
     * teamInfo : {"directMember":"1","directorNum":"0","indirectMember":"0","referrerLogo":"http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eo70Y5jG1NrGeOgqT4UMJTF2lA7ibCDFqOGSr5xaRhyjzOKwY9cQokPvZ1JyWW8gtJz8gNibNG7ibLUA/132","referrerMobile":"18827035411","referrerName":"超超","todayNum":"0","totalNum":"1","yesterdayNum":"1"}
     */

    private TeamInfoBean teamInfo;

    public TeamInfoBean getTeamInfo() {
        return teamInfo;
    }

    public void setTeamInfo(TeamInfoBean teamInfo) {
        this.teamInfo = teamInfo;
    }

    public static class TeamInfoBean {
        /**
         * directMember : 1
         * directorNum : 0
         * indirectMember : 0
         * referrerLogo : http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eo70Y5jG1NrGeOgqT4UMJTF2lA7ibCDFqOGSr5xaRhyjzOKwY9cQokPvZ1JyWW8gtJz8gNibNG7ibLUA/132
         * referrerMobile : 18827035411
         * referrerName : 超超
         * todayNum : 0
         * totalNum : 1
         * yesterdayNum : 1
         */

        private String directMember;
        private String directorNum;
        private String indirectMember;
        private String referrerLogo;
        private String referrerMobile;
        private String referrerName;
        private String todayNum;
        private String totalNum;
        private String yesterdayNum;

        public String getDirectMember() {
            return directMember;
        }

        public void setDirectMember(String directMember) {
            this.directMember = directMember;
        }

        public String getDirectorNum() {
            return directorNum;
        }

        public void setDirectorNum(String directorNum) {
            this.directorNum = directorNum;
        }

        public String getIndirectMember() {
            return indirectMember;
        }

        public void setIndirectMember(String indirectMember) {
            this.indirectMember = indirectMember;
        }

        public String getReferrerLogo() {
            return referrerLogo;
        }

        public void setReferrerLogo(String referrerLogo) {
            this.referrerLogo = referrerLogo;
        }

        public String getReferrerMobile() {
            return referrerMobile;
        }

        public void setReferrerMobile(String referrerMobile) {
            this.referrerMobile = referrerMobile;
        }

        public String getReferrerName() {
            return referrerName;
        }

        public void setReferrerName(String referrerName) {
            this.referrerName = referrerName;
        }

        public String getTodayNum() {
            return todayNum;
        }

        public void setTodayNum(String todayNum) {
            this.todayNum = todayNum;
        }

        public String getTotalNum() {
            return totalNum;
        }

        public void setTotalNum(String totalNum) {
            this.totalNum = totalNum;
        }

        public String getYesterdayNum() {
            return yesterdayNum;
        }

        public void setYesterdayNum(String yesterdayNum) {
            this.yesterdayNum = yesterdayNum;
        }
    }
}
