package com.hotusm.fastcrawl.fetch;

import com.hotusm.fastcrawl.common.container.DiscardData;
import com.hotusm.fastcrawl.common.container.LinkBucket;
import com.hotusm.fastcrawl.common.container.PageData;
import com.hotusm.fastcrawl.fetch.task.FetchATagTask;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by luqibao on 2017/1/12.
 */
public class DelayAndRetryLoadImpl implements DelayAndRetryLoad{


    private static final Logger LOGGER= LogManager.getLogger(DelayAndRetryLoadImpl.class);
    private static final int NCPU=Runtime.getRuntime().availableProcessors();

    private ThreadPoolExecutor threadPoolExecutor=new ThreadPoolExecutor(2*NCPU,2*NCPU+1,20L, TimeUnit.DAYS.SECONDS,new LinkedBlockingQueue<Runnable>());
    //储存解析的标签的
    private LinkBucket<ATag> linkBucket;

    //记录某一个标签失败的次数 如果超过一定的次数的话  那么就做其他的处理
    private ConcurrentHashMap<ATag,Integer> failCount=new ConcurrentHashMap<ATag, Integer>();

    //这是下载好的网页
    private volatile PageData pageData;

    //保存有问题的URL  留作后期处理
    private volatile DiscardData discardData;

    public DelayAndRetryLoadImpl(LinkBucket<ATag> linkBucket,PageData pageData,DiscardData discardData){

        this.linkBucket=linkBucket;
        this.pageData=pageData;
        this.discardData=discardData;
    }

    public void setLinkBucket(LinkBucket<ATag> linkBucket) {
        this.linkBucket = linkBucket;
    }

    public void delayLoad() {
        LOGGER.info(String.format("启动主加载成功,cpu 个数：%d,共启线程 %d",NCPU,2*NCPU));
        long startTime=System.currentTimeMillis();
        //启动抓取网页的线程
        new Thread(new SteadilyWork()).start();
        long endTime=System.currentTimeMillis()-startTime;
        LOGGER.info(String.format("线程启动完成,共耗时 %d 毫秒",endTime));
    }

    //TODO 这里需要修改
    //工作队列
    private class SteadilyWork implements Runnable{

        public void run() {
            while (true){
                try{
                    ATag aTag= linkBucket.pop();
                    threadPoolExecutor.execute(new FetchATagTask(linkBucket,failCount,pageData,discardData,aTag));
                }catch (Exception e){
                    Thread.interrupted();
                }
            }
        }
    }

}
