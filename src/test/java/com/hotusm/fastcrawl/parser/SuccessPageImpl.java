package com.hotusm.fastcrawl.parser;

import java.util.*;

/**
 * @author luqibao
 * @date 2017/3/9
 */
public class SuccessPageImpl implements SuccessPage{

    private Map<String,String> map=new HashMap<String, String>();
    public Map getAll() {
        return map;
    }

    public void add(String url,String name) {

        map.put(name,url);
    }
}
