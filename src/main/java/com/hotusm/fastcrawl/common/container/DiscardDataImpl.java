package com.hotusm.fastcrawl.common.container;

import com.hotusm.fastcrawl.fetch.ATag;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author luqibao
 *  最简答的方式  使用list的方式
 */
public class DiscardDataImpl implements DiscardData {

    private volatile List<ATag> counts=new CopyOnWriteArrayList<ATag>();

    public void save(ATag aTag) {

        counts.add(aTag);
    }


    public Collection<ATag> getAll() {

        return counts;
    }
}
