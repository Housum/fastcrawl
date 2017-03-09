package com.hotusm.fastcrawl.parser;

import com.hotusm.fastcrawl.util.StreamUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by luqibao on 2017/1/13.
 */
public class TestParserWork implements ParserWork{

    private static final Logger LOGGER= LogManager.getLogger(TestParserWork.class);

    private AtomicInteger atomicInteger=new AtomicInteger(0);

    private SuccessPage successPage;

    public TestParserWork(SuccessPage successPage){
        this.successPage=successPage;
    }


    public Map<Object, Object> doParser(HtmlContext context) {
       // LOGGER.info(atomicInteger.incrementAndGet());
        //writeHtml(html);
        // writeHtml(html, UUID.randomUUID().toString(),"C://tomcat//");
        printlnTitle(context);
        return null;
    }

    private void printlnTitle(HtmlContext context){
        Document document= Jsoup.parse(context.getHtml());
        Elements elements=document.select(".vcard-names > .vcard-fullname");

        if(elements!=null&&elements.size()>0){
            synchronized (successPage){
                if(elements.get(0)!=null){
                    successPage.add(context.getUrl(),elements.get(0).text());
                }
            }
        }


//        try{
//            FileWriter channel=new FileWriter("c://title_yly.txt",true);
//            BufferedWriter bw=new BufferedWriter(channel);
//            bw.write(elements.get(0).text()+"\r\n");
//            bw.close();
//            channel.close();
//        }catch (Exception e){
//            //e.printStackTrace();
//        }
    }

    /**
     *
     * @param html
     */
    //将整个的html页面保留下来
    private void writeHtml(String html){

        Document document=Jsoup.parse(html);
        Elements elements= document.select(".link_title");
        if(elements!=null&&elements.size()>0){
            try{
                StreamUtil.writeFile(elements.get(0).text(),"C://article_details//"+UUID.randomUUID().toString()+".txt");
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }
}
