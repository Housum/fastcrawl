package com.hotusm.fastcrawl.fetch;

import com.hotusm.fastcrawl.util.ConfigUtil;
import com.hotusm.fastcrawl.util.HttpReturnMessage;
import com.hotusm.fastcrawl.util.HttpUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by luqibao on 2017/1/12.
 * 解析网页的服务类
 */
public class DownLoadPageImpl implements DownLoadPage {

    private static final Logger LOG= LogManager.getLogger(DownLoadPageImpl.class);

    private static final String DOMAIN= ConfigUtil.getValue("domain");
    private static final String HOST=ConfigUtil.getValue("host");


    public HttpReturnMessage fetchPage(String url) {

        return fetchPage(url,null,buildHeader(),-1);
    }

    public HttpReturnMessage fetchPage(String url, Map<String, String> params) {

        return fetchPage(url,params,buildHeader(),-1);
    }

    public HttpReturnMessage fetchPage(String url, Map<String, String> params, Map<String, String> headers, int timeout) {

        if(StringUtils.isBlank(url)){
            LOG.error("param url is require!");
            return null;
        }
        LOG.info(String.format("start fetch page ,url is %s",url));
        HttpReturnMessage returnMessage=null;
        try{
            returnMessage=HttpUtil.doGet(url,params,headers,timeout);
        }catch (Exception e){
            LOG.error(String.format("fetch page error,url is %s  ",url),e);
            throw new RuntimeException(e);
        }
        LOG.info(String.format("fetch page success,url is %s", url));
        return returnMessage;
    }

    public HttpReturnMessage fetchPage(String url, Map<String, String> params, Map<String, String> headers) {

        return fetchPage(url,params,headers,-1);
    }

    //构建header
    private Map<String,String> buildHeader(){

        //TODO 优化
        Map<String,String> header=new HashMap<String,String>();
        header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        header.put("Cache-Control", "max-age=0");
        header.put("Accept-Language", "zh-CN,zh;q=0.8");
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.124 Safari/537.36");
        header.put("Cache-Control", "max-age=0");
        header.put("Connection","keep-alive");

        //这几部分是动态的
        header.put("Cookie", HttpUtil.buildCookie());
        header.put("Referer", DOMAIN);
        header.put("Host",HOST);

        return header;
    }
}
