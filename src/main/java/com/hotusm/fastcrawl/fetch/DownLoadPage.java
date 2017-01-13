package com.hotusm.fastcrawl.fetch;

import com.hotusm.fastcrawl.util.HttpReturnMessage;

import java.util.Map;

/**
 * 下载网页的类
 */
public interface DownLoadPage {

    HttpReturnMessage fetchPage(String url);
    HttpReturnMessage fetchPage(String url,Map<String, String> params, Map<String, String> headers);
    HttpReturnMessage fetchPage(String url,Map<String, String> params);
    HttpReturnMessage fetchPage(String url, Map<String, String> params, Map<String, String> headers,
                                int timeout);

}
