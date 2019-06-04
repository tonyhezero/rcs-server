package com.geo.rcs.common.schedule;


import com.geo.rcs.common.exception.GeoException;
import org.quartz.*;


/**
 * 定时任务工具类
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/6/13
 */

public class ScheduleUtils {
    private final static String JOB_NAME = "TASK_";


    /**
     * 获取触发器key
     */

    public static TriggerKey getTriggerKey(Long jobId) {
        return TriggerKey.triggerKey(JOB_NAME + jobId);
    }


    /**
     * 获取jobKey
     */

    public static JobKey getJobKey(Long jobId) {
        return JobKey.jobKey(JOB_NAME + jobId);
    }



    /**
     * 获取表达式触发器
     */

    public static CronTrigger getCronTrigger(Scheduler scheduler, Long jobId) {
        try {
            return (CronTrigger) scheduler.getTrigger(getTriggerKey(jobId));
        } catch (SchedulerException e) {
            throw new GeoException("获取定时任务CronTrigger出现异常", e);
        }
    }





    /**
     * 暂停任务
     */

    public static void pauseJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.pauseJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new GeoException("暂停定时任务失败", e);
        }
    }


    /**
     * 恢复任务
     */

    public static void resumeJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.resumeJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new GeoException("暂停定时任务失败", e);
        }
    }


    /**
     * 删除定时任务
     */

    public static void deleteScheduleJob(Scheduler scheduler, Long jobId) {
        try {
            scheduler.deleteJob(getJobKey(jobId));
        } catch (SchedulerException e) {
            throw new GeoException("删除定时任务失败", e);
        }
    }
}
