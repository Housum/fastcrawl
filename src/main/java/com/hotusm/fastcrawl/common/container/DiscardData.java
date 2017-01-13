package com.hotusm.fastcrawl.common.container;

import com.hotusm.fastcrawl.fetch.ATag;

import java.util.Collection;

/**
 * @author luqibao
 * 被认为已经丢失不需要再重新解析的数据
 */
public interface DiscardData {

    /**
     * 保存
     * @param aTag
     */
    void save(ATag aTag);


    /**
     * 获取所有的数据
     * @return
     */
    Collection<ATag> getAll();
}
