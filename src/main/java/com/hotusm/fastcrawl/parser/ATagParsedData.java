
package com.hotusm.fastcrawl.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * 保存已经解析过的a标签
 * @author luqibao
 */
public class ATagParsedData implements ParsedData{

    private volatile List<Object> list=new ArrayList<Object>();

    public  boolean isParsed(Object tag) {
            return list.contains(tag);
    }

    public  void addData(Object tag) {
            list.add(tag);
    }
}
