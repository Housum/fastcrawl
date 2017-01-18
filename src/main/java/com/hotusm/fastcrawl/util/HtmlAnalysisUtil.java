package com.hotusm.fastcrawl.util;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 解析网页的工具类
 */
public class HtmlAnalysisUtil {

    private static final Logger LOGGER= LogManager.getLogger(HtmlAnalysisUtil.class);

    private HtmlAnalysisUtil(){}

    public static List<String> selectAttr(String queryString, String attr, String html){
        List<String> results=new ArrayList<String>();
        Document doc = Jsoup.parse(html);
        Elements elements=doc.select(queryString);
        if(elements==null|| CollectionUtils.isEmpty(elements)){
            LOGGER.error("query  result is empty");
            return results;
        }
        for(Element element:elements){
            if(StringUtils.isNoneBlank(element.attr(attr))){
                results.add(element.attr(attr));
            }
        }
        return results;
    }

    /**
     *
     * @param queryString
     * @param attr
     * @param html
     * @return  key =attr  value=text
     */
    public static Map<String,String> selectAttrAndText(String queryString, String attr, String html){
        Map<String,String> results=new HashMap();
        Document doc = Jsoup.parse(html);
        Elements elements=doc.select(queryString);
        if(elements==null|| CollectionUtils.isEmpty(elements)){
            LOGGER.error(String.format("query  result is empty,queryString is %s,attr is %s",queryString,attr));
            return null;
        }
        for(Element element:elements){
            if(StringUtils.isNoneBlank(element.attr(attr))){
                results.put(element.attr(attr),element.text());
            }
        }
        return results;
    }

    public static List<String> selectText(String queryString,String html){
        List<String> results=new ArrayList<String>();
        Document doc = Jsoup.parse(html);
        Elements elements=doc.select(queryString);
        if(elements==null|| CollectionUtils.isEmpty(elements)){
            LOGGER.error("query  result is empty");
            return results;
        }
        for(Element element:elements){
            if(StringUtils.isNoneBlank(element.html())){
                results.add(element.attr(element.text()));
            }
        }
        return results;
    }

}
