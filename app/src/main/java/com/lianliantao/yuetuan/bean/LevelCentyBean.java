package com.lianliantao.yuetuan.bean;

import java.util.List;

public class LevelCentyBean extends BaseBean{

    /**
     * rank : 总监
     * rankInfo : 如何晋升为总监
     * upgradeCondition : [{"rank":"有效订单","remain":"0","total":"1","unit":"笔"},{"rank":"直属会员","remain":"0","total":"100","unit":"人"},{"rank":"间属会员","remain":"0","total":"200","unit":"人"}]
     * upgradeUser : [{"avatar":"http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83erVKRKEhGNJ70FHFMNECwbpiaGvgeXIjRfVfyicyibnlRgoPD5bgPf8IaJ32R6KRSAJfKUpWp3PKScJQ/132","lastMonthEarn":"0.29","nickname":"超超"}]
     */

    private String rank;
    private String rankInfo;
    private List<UpgradeConditionBean> upgradeCondition;
    private List<UpgradeUserBean> upgradeUser;

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRankInfo() {
        return rankInfo;
    }

    public void setRankInfo(String rankInfo) {
        this.rankInfo = rankInfo;
    }

    public List<UpgradeConditionBean> getUpgradeCondition() {
        return upgradeCondition;
    }

    public void setUpgradeCondition(List<UpgradeConditionBean> upgradeCondition) {
        this.upgradeCondition = upgradeCondition;
    }

    public List<UpgradeUserBean> getUpgradeUser() {
        return upgradeUser;
    }

    public void setUpgradeUser(List<UpgradeUserBean> upgradeUser) {
        this.upgradeUser = upgradeUser;
    }

    public static class UpgradeConditionBean {
        /**
         * rank : 有效订单
         * remain : 0
         * total : 1
         * unit : 笔
         */

        private String rank;
        private String remain;
        private String total;
        private String unit;

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getRemain() {
            return remain;
        }

        public void setRemain(String remain) {
            this.remain = remain;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
    }

    public static class UpgradeUserBean {
        /**
         * avatar : http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83erVKRKEhGNJ70FHFMNECwbpiaGvgeXIjRfVfyicyibnlRgoPD5bgPf8IaJ32R6KRSAJfKUpWp3PKScJQ/132
         * lastMonthEarn : 0.29
         * nickname : 超超
         */

        private String avatar;
        private String lastMonthEarn;
        private String nickname;

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getLastMonthEarn() {
            return lastMonthEarn;
        }

        public void setLastMonthEarn(String lastMonthEarn) {
            this.lastMonthEarn = lastMonthEarn;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
