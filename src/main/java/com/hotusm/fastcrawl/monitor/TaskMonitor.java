package com.hotusm.fastcrawl.monitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * 监控工具
 * 例子：
 *<code>
 *    public class MyMonitorJob implements MonitorJob{
 *        void monitor(Job job){
 *            ....
 *        }
 *    }
 *
 *   new TaskMonitor("this is my monitor",new MyMonitorJob());
 *<code/>
 */
public class TaskMonitor extends Thread implements Job{

    private static final Logger LOGGER= LogManager.getLogger(TaskMonitor.class);

    private MonitorJob monitorJob;

    public TaskMonitor(String monitorName,MonitorJob monitorJob){
        this.setName(monitorName);
        this.setDaemon(true);
        this.monitorJob=monitorJob;
        this.start();
        LOGGER.info(String.format("启动监控:%s ",monitorName));
    }

    @Override
    public void run() {

        while (true){
            this.monitorJob.monitor(this);
            try{
                TimeUnit.SECONDS.sleep(10L);
            }catch (Exception e){
                Thread.currentThread().interrupt();
            }
        }
    }
}
