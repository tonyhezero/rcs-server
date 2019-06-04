package com.geo.rcs.modules.event.task;

import com.geo.rcs.modules.event.service.BatchEventService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wp
 * @date Created in 13:53 2019/4/26
 */
@Component
public class BatchDispatchCenter {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private ExecutorService dispatchThreadPool = new ThreadPoolExecutor(20, 50,
            30L, TimeUnit.MINUTES,
            new LinkedBlockingQueue<>(), new ThreadFactoryBuilder().setNameFormat("source-pool-%d").build(), new ThreadPoolExecutor.AbortPolicy());
    @Autowired
    BatchEventService batchEventService;

}
