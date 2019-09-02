package com.lianliantao.yuetuan.bean;

import java.util.List;

public class AppMsgBean extends BaseBean{

    /**
     * noticeInfo : [{"content":"用户违规","createTime":"2019-07-22 16:52:27","noticeType":"0","title":"违规推广处罚规则"}]
     * totalResults : 1
     */

    private String totalResults;
    private List<NoticeInfoBean> noticeInfo;

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
    }

    public List<NoticeInfoBean> getNoticeInfo() {
        return noticeInfo;
    }

    public void setNoticeInfo(List<NoticeInfoBean> noticeInfo) {
        this.noticeInfo = noticeInfo;
    }

    public static class NoticeInfoBean {
        /**
         * content : 用户违规
         * createTime : 2019-07-22 16:52:27
         * noticeType : 0
         * title : 违规推广处罚规则
         */

        private String content;
        private String createTime;
        private String noticeType;
        private String title;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getNoticeType() {
            return noticeType;
        }

        public void setNoticeType(String noticeType) {
            this.noticeType = noticeType;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
