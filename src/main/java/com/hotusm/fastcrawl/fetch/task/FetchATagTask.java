package com.hotusm.fastcrawl.fetch.task;

import com.hotusm.fastcrawl.common.container.DiscardData;
import com.hotusm.fastcrawl.common.container.PageData;
import com.hotusm.fastcrawl.fetch.ATag;
import com.hotusm.fastcrawl.fetch.DownLoadPage;
import com.hotusm.fastcrawl.fetch.DownLoadPageImpl;
import com.hotusm.fastcrawl.common.container.LinkBucket;
import com.hotusm.fastcrawl.util.HttpReturnMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author luqibao
 * 获取网页信息的工作的单元
 */
public class FetchATagTask implements Runnable{

    private static final Logger LOGGER= LogManager.getLogger(FetchATagTask.class);

    private static final DownLoadPage DOWN_LOAD_PAGE=new DownLoadPageImpl();
    //允许失败的次数
    private static final int FAIL_COUNT=3;

    //里面都是待解析的a标签
    private volatile LinkBucket<ATag> linkBucket;
    //记录临时失败的次数,当超过了FAIL_COUNT 那么认为这个网页是有问题的 记录到真实区域
    private volatile ConcurrentHashMap<ATag,Integer> failCount;
    //解析完成的
    private volatile PageData pageData;
    //失败的 不需要再解析了
    private volatile DiscardData discardData;

    //传进来的a标签（元数据）
    private final ATag aTag;

    public FetchATagTask(LinkBucket<ATag> linkBucket,ConcurrentHashMap<ATag,Integer> failCount,PageData pageData,
                         DiscardData discardData,ATag aTag){
        this.linkBucket=linkBucket;
        this.failCount=failCount;
        this.pageData=pageData;
        this.discardData=discardData;
        this.aTag=aTag;
    }

    public void run() {
        try{
            HttpReturnMessage message= DOWN_LOAD_PAGE.fetchPage(aTag.getHref());
            if(message!=null&&message.isSuccess()){
                pageData.save(message);
                return;
            }
            //如果获取为空  那么就抛出运行时异常  后来进行处理
            throw new RuntimeException();
        }catch (Exception e){
            //如果是其他的异常的话 那么就重试
            int count=incrementAndGet(aTag);
            LOGGER.error(String.format("fetch aTag fail,retry,fail-count is %d,url is %s",count,aTag.getHref()));
            //失败的策略
            doAfterThrowException(count,aTag);

        }

    }

    private Integer incrementAndGet(ATag tag){
        Integer count=0;
        synchronized (tag.getHref()){
            count=failCount.get(tag);
            if(count!=null){
                failCount.put(tag,failCount.get(tag)+1);
            }else {
                failCount.put(tag,1);
                return 1;
            }
        }
        return count+1;
    }

    /**
     * 对于异常情况的处理
     * @param count
     * @param aTag
     */
    private void doAfterThrowException(int count,ATag aTag){
        if(count>=FAIL_COUNT){
            discardData.save(aTag);
        }else {
            try{
                linkBucket.push(aTag);
            }catch (Exception e1){
                Thread.currentThread().interrupt();
            }
        }
    }
}
