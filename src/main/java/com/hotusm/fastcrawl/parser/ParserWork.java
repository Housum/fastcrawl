package com.hotusm.fastcrawl.parser;

import java.util.Map;

/**
 * @author hotusm
 * 除了核心部分是用户自定义,其他部分如解析遇到问题的时候的
 * 处理
 */
public interface ParserWork {

    /**
     * 解析工作
     * 使用key-value的方式储存
     * 如果是用户自定义储存的话 可以返回null
     * @return
     */
    Map<Object,Object> doParser(String html);
}
