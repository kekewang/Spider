package com.spider.service;

import com.spider.common.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.Lifecycle;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@Component("refreshManager")
public class RefreshManager implements Lifecycle {
    private static Logger logger = LoggerFactory.getLogger(RefreshManager.class);
    private static volatile RefreshManager refreshManager;
    private static int DEFAULT_DELAY = 30;
    private Map<Refreshable, RefreshTask> taskMap = new HashMap();
    private Timer timer = new Timer("RefreshManager-Thread");

    private RefreshManager() {
    }

    private RefreshManager.RefreshTask createRefreshTask(Refreshable refreshable) {
        return new RefreshManager.RefreshTask(refreshable);
    }

    public static RefreshManager getInstance() {
        if(refreshManager == null) {
            refreshManager = (RefreshManager) SpringContextHolder.getApplicationContext().getBean(RefreshManager.class);
        }

        return refreshManager;
    }

    public void addRefreshable(Refreshable refreshable) {
        this.addRefreshable(refreshable, 0);
    }

    public boolean isRunning() {
        return this.timer != null;
    }

    public void start() {
    }

    public void stop() {
        this.taskMap.clear();
        this.timer.cancel();
        this.timer = null;
    }

    public synchronized void addRefreshable(Refreshable refreshable, int delay) {
        if(refreshable.getIntervalSeconds() > 0) {
            RefreshManager.RefreshTask task;
            if(this.taskMap.containsKey(refreshable)) {
                task = (RefreshManager.RefreshTask)this.taskMap.get(refreshable);
                if(task.refreshInterval == refreshable.getIntervalSeconds()) {
                    return;
                }

                this.taskMap.remove(refreshable);
                task.cancel();
            }

            task = this.createRefreshTask(refreshable);
            this.taskMap.put(refreshable, task);
            int firstScheduleInterval;
            if(delay < 0) {
                firstScheduleInterval = refreshable.getIntervalSeconds() < DEFAULT_DELAY?DEFAULT_DELAY:refreshable.getIntervalSeconds();
            } else {
                firstScheduleInterval = delay;
            }

            this.timer.schedule(task, (long)(firstScheduleInterval * 1000), (long)(refreshable.getIntervalSeconds() * 1000));
        }
    }

    public synchronized void removeRefreshable(Refreshable refreshable) {
        TimerTask task = (TimerTask)this.taskMap.remove(refreshable);
        if(task != null) {
            task.cancel();
        }

    }

    private class RefreshTask extends TimerTask {
        private Refreshable refreshable;
        private int refreshInterval;

        public RefreshTask(Refreshable refreshable) {
            this.refreshable = refreshable;
            this.refreshInterval = refreshable.getIntervalSeconds();
        }

        public void run() {
            try {
                try {
                    RefreshManager.logger.debug("执行刷新任务[" + this.refreshable.getName() + "]定时检测!");
                    if(this.refreshable.isNeedRefresh()) {
                        RefreshManager.logger.info("刷新任务[" + this.refreshable.getName() + "]检测到需刷新!");
                        this.refreshable.refresh();
                        RefreshManager.logger.info("刷新任务[" + this.refreshable.getName() + "]刷新完成!");
                        int e = this.refreshable.getIntervalSeconds();
                        if(e != this.refreshInterval) {
                            RefreshManager.logger.info("刷新任务[" + this.refreshable.getName() + "]刷新间隔时间发生变化，重新注册!");
                            RefreshManager.getInstance().removeRefreshable(this.refreshable);
                            RefreshManager.getInstance().addRefreshable(this.refreshable, e);
                        }
                    }
                } catch (Exception var5) {
                    RefreshManager.logger.error(this.refreshable + "刷新异常:" + var5.getMessage(), var5);
                }

            } finally {
                ;
            }
        }
    }
}
