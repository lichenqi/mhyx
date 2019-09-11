package com.lianliantao.yuetuan.bean;

public class UserLevelBean extends BaseBean {

    /**
     * level : 2
     */

    private String level;
    private String userRank;

    public String getUserRank() {
        return userRank;
    }

    public void setUserRank(String userRank) {
        this.userRank = userRank;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
