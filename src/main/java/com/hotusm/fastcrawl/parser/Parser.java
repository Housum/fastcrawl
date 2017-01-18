package com.hotusm.fastcrawl.parser;

/**
 * 解析下载下来的网页的
 * 实现类需要从中央池中获取下载的网页 进行解析
 * 最后加载到中央池中
 * @author hotusm
 */
public interface Parser {

    void load();
}
