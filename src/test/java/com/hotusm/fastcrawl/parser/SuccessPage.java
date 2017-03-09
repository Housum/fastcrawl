package com.hotusm.fastcrawl.parser;

import java.util.Map;

/**
 * @author luqibao
 * @date 2017/3/9
 * 保存满足条件的
 */
public interface SuccessPage {

    Map getAll();

    void add(String url,String name);
}
