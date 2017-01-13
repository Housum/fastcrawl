package com.hotusm.fastcrawl.monitor;

/**
 * 公共类 用于后期实现  在Monitor中来执行自定义监控
 *
 */
public interface MonitorJob{

    void monitor(Job job);
}
