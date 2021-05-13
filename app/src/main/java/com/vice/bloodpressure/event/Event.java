package com.vice.bloodpressure.event;


/**
 * 类描述：
 * 类传参：
 *
 * @author android.yys
 * @date 2021/1/16
 */
public class Event {

    /**
     * 设置adapter刷新
     */
    public static class SetAdapterRefresh {
        private boolean isRefresh;

        public SetAdapterRefresh(boolean isRefresh) {
            this.isRefresh = isRefresh;
        }

        public boolean isRefresh() {
            return isRefresh;
        }

        public void setRefresh(boolean refresh) {
            isRefresh = refresh;
        }
    }


}