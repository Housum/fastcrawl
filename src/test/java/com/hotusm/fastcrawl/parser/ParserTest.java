package com.hotusm.fastcrawl.parser;

import com.hotusm.fastcrawl.util.ConfigUtil;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 */
public class ParserTest {

    @Test
    public void testParser() throws Exception{
        FileInputStream fileInputStream=new FileInputStream("C:\\html\\httpsbookdoubancom.html");
        StringBuilder sb=new StringBuilder();
        byte[] b=new byte[2014];
        while (fileInputStream.read(b)!=-1){
            sb.append(new String(b,"UTF-8"));
        }
        Document doc = Jsoup.parse(sb.toString());
        Elements elements=doc.select("a");
        if(elements!=null&&elements.size()>0){
          //Element element= elements.get(0);
          //System.out.println(element.text());
            for(Element element:elements){
               //System.out.println("href:"+element.attr("href")+" name:"+element.text());
              String url=additionTag(element.attr("href"));
              if(StringUtils.isNoneBlank(url)){
                  System.out.println(url);
              }
            }
        }
    }

    //如果url是以/开头的 需要增加完整的名称 domain+url  如果不是以http或者https 开头又不是/开头 那么
    //认为是相对路径  old-url+'/'+url
    private String additionTag(String url) throws Exception{
        if(StringUtils.isBlank(url)){
            return "";
        }

        List<String> suffix=new ArrayList<String>(Arrays.asList(ConfigUtil.getValue("suffix.exclude").split(",")));
        if(validate(suffix,url)){
            return "";
        }
        String domain=ConfigUtil.getValue("domain");
        String host=ConfigUtil.getValue("host");
        if(url.startsWith("https")){
            url= url.replaceFirst("https","http");
        }
        if(url.startsWith("/")){
            url= domain+url.substring(1,url.length());
        }
        if(url.startsWith("www")){
            url= "http://"+url;
        }
        if(url.startsWith(host)){
            url= url.replaceFirst(host,domain);
        }
        if(!url.startsWith("http")){
            url= ConfigUtil.getValue("domain")+url;
        }

        return url;//encodeUrlCh(url);
    }

    private boolean validate(List<String> suffix,String url){

        for (String s:suffix){
            if(url.endsWith(s)){
                return true;
            }
        }
        return false;
    }
    public static String encodeUrlCh (String url){
        URI uri;
        try {
            uri = new URI(url,false);
            return uri.toString();
        } catch (URIException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void testChinses(){

        System.out.println("11".matches("[\\u4e00-\\u9fa5]"));
        System.out.println("7868768你".matches("[\\u4e00-\\u9fa5]+"));
        Pattern pattern=Pattern.compile("[\\u4e00-\\u9fa5]+");
        System.out.println(pattern.matcher("7868768你").matches());

    }

}