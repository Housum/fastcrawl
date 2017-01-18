package com.hotusm.fastcrawl.parser;

/**
 * 解析过的数据
 * @author hotusm
 */
public interface ParsedData {

    /**
     * 是否解析过
     * @param obj
     * @return
     */
    boolean isParsed(Object obj);

    /**
     * 增加解析过的数据
     * @param obj
     */
    void addData(Object obj);

}
