package com.hotusm.fastcrawl.fetch;

/**
 * @author hotsum工作类
 * 实现类必须满足失败重试
 * 主要的
 */
public interface DelayAndRetryLoad {

    void delayLoad();
}
