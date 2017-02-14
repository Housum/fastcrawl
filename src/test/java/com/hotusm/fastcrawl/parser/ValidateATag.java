package com.hotusm.fastcrawl.parser;

import com.hotusm.fastcrawl.util.ConfigUtil;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class ValidateATag implements ValidateTag{

    private static final Logger LOGGER= LogManager.getLogger(ValidateATag.class);

    private static final String FIRST_DOMAIN=ConfigUtil.getValue("first.domain");
    private static final String SECOND_DOMAIN=ConfigUtil.getValue("second.domain");


    private static final List<String>  SUFFIX=new ArrayList<String>(Arrays.asList(ConfigUtil.getValue("suffix.exclude").split(",")));

    public boolean validate(String url) {
/*        if(!url.contains(SECOND_DOMAIN)){
            return false;
        }*/

        if(!url.contains(FIRST_DOMAIN)){
            return false;
        }
        return true;
    }

    public String encodeHref(String href) {
        return additionTag(href);
    }
    private String additionTag(String url){
        if(StringUtils.isBlank(url)){
            return "";
        }

        if(validate(SUFFIX,url)){
            return "";
        }
        String host= ConfigUtil.getValue("host");
        if(url.startsWith("https")){
            /*url= url.replaceFirst("https","http");*/
            return "";
        }
        if(url.startsWith("//")){
            url=url.replaceFirst("//","");
        }
        if(url.startsWith("/")){
            url= FIRST_DOMAIN+url.substring(1,url.length());
        }
        if(url.startsWith("www")){
            url= "http://"+url;
        }
        if(url.startsWith(host)){
            url= url.replaceFirst(host,SECOND_DOMAIN);
        }
        if(!url.startsWith("http")){
            url= FIRST_DOMAIN+url;
        }
        if(!url.contains(FIRST_DOMAIN)){
            return "";
        }
        return encodeUrlCh(url);
    }

    //检查是够满足其中的
    private boolean validate(List<String> suffix, String url){

        for (String s:suffix){
            if(url.endsWith(s)){
                return true;
            }
        }
        return false;
    }

    //对路径进行编码
    private static String encodeUrlCh (String url){
        URI uri;
        try {
            uri = new URI(url,false);
            return uri.toString();
        } catch (URIException e) {
            LOGGER.error(String.format("URI encode error,url is %s",url));
            e.printStackTrace();
        }
        return null;
    }
}
