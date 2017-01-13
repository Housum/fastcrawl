package com.hotusm.fastcrawl.fetch;

/**
 * 这里全部是待解析的超链接
 */
public interface LinkBucket<T> {

    /**
     * 插入
     */
    void push(T tag) throws InterruptedException;

    /**
     * 弹出
     * @return
     */
    T pop() throws InterruptedException;
}
