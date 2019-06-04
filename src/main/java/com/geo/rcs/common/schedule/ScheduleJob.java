package com.geo.rcs.common.schedule;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * 定时任务
 *
 * @author guoyujie
 * @email guoyujie@geotmt.com
 * @date 2018/6/13
 */
public class ScheduleJob extends QuartzJobBean {

	private Logger logger = LoggerFactory.getLogger(getClass());

	private ExecutorService service = Executors.newSingleThreadExecutor();
	//预定更新时间
	private static Date reserveTime;
	
    @Override
    public void executeInternal(JobExecutionContext context) throws JobExecutionException {

    }

	public ScheduleJob() {
	}
}
