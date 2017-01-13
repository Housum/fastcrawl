package com.hotusm.fastcrawl.util;

import com.hotusm.fastcrawl.fetch.DownLoadPage;
import com.hotusm.fastcrawl.fetch.DownLoadPageImpl;
import org.junit.Test;

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

       HttpReturnMessage message= page.fetchPage("http://www.baidu.com");

       System.out.println(message.getResult()+" "+message.getStatusCode());

    }
}
