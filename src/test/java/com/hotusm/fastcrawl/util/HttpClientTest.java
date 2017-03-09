package com.hotusm.fastcrawl.util;

import com.hotusm.fastcrawl.fetch.DownLoadPage;
import com.hotusm.fastcrawl.fetch.DownLoadPageImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luqibao on 2017/1/12.
 */
public class HttpClientTest {


    @Test
    public void testGetPage() throws Exception{


        Map<String,String> header=new HashMap<String,String>();

        header.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        header.put("Cache-Control", "max-age=0");
        header.put("Accept-Language", "zh-CN,zh;q=0.8");
        header.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/43.0.2357.124 Safari/537.36");
        header.put("Cache-Control", "max-age=0");
        //这里也是动态的
        //header.put("Host","book.douban.com");

        header.put("Connection","keep-alive");

        //这两部分可以是动态的
        header.put("Cookie", HttpUtil.buildCookie());
        header.put("Referer", "https://book.douban.com11/");

       HttpReturnMessage returnMessage= HttpUtil.doGet("http://book.douban.com/111",null,header,0);

       System.out.println(returnMessage.getResult()+"   "+ returnMessage.getUrl()+ returnMessage.getStatusCode());
    }

    @Test
    public void testDownLoadPage(){

        DownLoadPage page=new DownLoadPageImpl();

       HttpReturnMessage message= page.fetchPage("http://www.cnblogs.com/zr520/p/4876162.html");

       System.out.println(message.getResult()+" "+message.getStatusCode());
    }

    @Test
    public void testGet() throws Exception{
        DownLoadPage page=new DownLoadPageImpl();

        HttpReturnMessage message= page.fetchPage("http://blog.csdn.net/yinwenjie/article/details/52757457");

       // System.out.println(sb.toString());
        //List<String> list= HtmlAnalysisUtil.selectText("#dale_book_subject_top_icon",sb.toString());

        Document document= Jsoup.parse(message.getResult());

       // Element element=document.getElementById("dale_book_subject_top_icon");

        Elements elements= document.select("h1");

        System.out.println(elements.get(0).text());

       // System.out.println(list);
    }

    @Test
    public void decode(){

        String s=URLDecoder.decode("https://book.douban.com/javascript:%20void%200;");
        System.out.print(s);
    }

    @Test
    public void testSSLClient(){
        try{
           HttpReturnMessage message=HttpUtil.doGet("https://www.douban.com/group/topic/79358744/?cid=984437273");
           System.out.println(message.getResult());
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
