package com.hotusm.fastcrawl.common.container;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 使用Queue实现
 */
public class QueueLinkBucket<T> implements LinkBucket<T>{


    private LinkedBlockingQueue<T> queue=new LinkedBlockingQueue<T>(200);

    public T pop() throws InterruptedException{
        return queue.take();
    }

    public void push(T tag) throws InterruptedException {
        queue.put(tag);
    }
}
