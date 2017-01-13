package com.hotusm.fastcrawl.fetch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 使用Queue实现
 */
public class QueueLinkBucket<T> implements LinkBucket<T>{

    private static final Logger LOGGER= LogManager.getLogger(QueueLinkBucket.class);

    private LinkedBlockingQueue<T> queue=new LinkedBlockingQueue<T>(200);

    public T pop() throws InterruptedException{
        T tag= queue.take();
        LOGGER.info(" taking a tag,"+tag);
        return tag;
    }

    public void push(T tag) throws InterruptedException {
        LOGGER.info(" putting a tag,"+tag);
        queue.put(tag);
    }
}
