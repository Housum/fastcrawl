package com.hotusm.fastcrawl.common.container;

import com.hotusm.fastcrawl.util.HttpReturnMessage;

/**
 * 保存下载好的网页信息
 * 其中可能包括403 404等网页
 */
public interface PageData {


    /**
     * 保存网页数据
     * @param message
     */
    void save(HttpReturnMessage message);

    /**
     * 获取网页数据
     * 可以是公平或者非公平的方式
     * @return
     */
    HttpReturnMessage get();
}
