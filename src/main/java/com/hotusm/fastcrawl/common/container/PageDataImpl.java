package com.hotusm.fastcrawl.common.container;

import com.hotusm.fastcrawl.monitor.Job;
import com.hotusm.fastcrawl.monitor.MonitorJob;
import com.hotusm.fastcrawl.monitor.TaskMonitor;
import com.hotusm.fastcrawl.util.HttpReturnMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 储存成功爬出来的网页
 * //TODO 重复的情况怎么解决？这里肯定不能保存在map或者list中  内存会奔溃 最好储存在redis或者mogodb 或者数据库中
 */
public class PageDataImpl implements PageData{

    private static final Logger LOGGER=LogManager.getLogger(PageDataImpl.class);

    //保存信息
    private BlockingQueue<HttpReturnMessage> messages=new LinkedBlockingQueue<HttpReturnMessage>();

    public PageDataImpl(){
       new TaskMonitor("监控结果",new PageDataMonitor(messages)) ;
    }

    public void save(HttpReturnMessage message) {
        messages.add(message);
    }

    public HttpReturnMessage get() {
        try{
            return messages.take();
        }catch (InterruptedException e){
            LOGGER.error("get PageData error,",e);
            Thread.currentThread().interrupt();
        }
        return null;
    }

    // 监控
    private static class PageDataMonitor implements MonitorJob {

        private static final Logger LOGGER= LogManager.getLogger(PageDataMonitor.class);

        private BlockingQueue<HttpReturnMessage> pageData;

        public PageDataMonitor(BlockingQueue<HttpReturnMessage> pageData){

            this.pageData=pageData;
        }

        public void monitor(Job job) {
            LOGGER.info(String.format("任务名称：%s,容器中网页数量 %d",job.getName(),pageData.size()));
        }
    }
}
