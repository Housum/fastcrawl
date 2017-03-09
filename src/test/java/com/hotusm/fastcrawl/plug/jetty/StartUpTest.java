package com.hotusm.fastcrawl.plug.jetty;

import com.hotusm.fastcrawl.common.container.*;
import com.hotusm.fastcrawl.fetch.ATag;
import com.hotusm.fastcrawl.fetch.DelayAndRetryLoad;
import com.hotusm.fastcrawl.fetch.DelayAndRetryLoadImpl;
import com.hotusm.fastcrawl.parser.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Created by luqibao on 2017/1/19.
 */
public class StartUpTest {

    private final static int PORT=8080;

    public static void main(String[] args) throws Exception{
        startUp("https://book.douban.com/","读书");
    }

    public static  void startUp(String url,String name) throws Exception{
        //这部分启动爬虫
        LinkBucket linkBucket= new QueueLinkBucket<ATag>();
        PageData pageData=new PageDataImpl();

        ParsedData parsedData=new ATagParsedData();
        DiscardData discardData=new DiscardDataImpl();
        ValidateTag validateTag=new ValidateATag();
        SuccessPage successPage=new SuccessPageImpl();
        ParserWork parserWork=new TestParserWork(successPage);

        ParserWork aTagParserWork=new ATagParserWork(linkBucket,parsedData,validateTag);

        linkBucket.push(new ATag(name,url));
        DelayAndRetryLoad load=new DelayAndRetryLoadImpl(linkBucket,pageData,discardData);
        load.delayLoad();

        TestAbstractParser parser= new TestAbstractParser();

        parser.setaTagParserWork(aTagParserWork);
        parser.setPageData(pageData);
        parser.setPageParserWork(parserWork);

        parser.load();

        //这部分启动jetty
        Server server=new Server();

        ServerConnector http=new ServerConnector(server);

        http.setIdleTimeout(30000);
        http.setPort(PORT);
        http.setHost("localhost");

        server.addConnector(http);

        ServletContextHandler servlets=new ServletContextHandler();
        ServletHolder holder=new ServletHolder(new TestHttpServlet(discardData,parsedData,successPage));
        servlets.addServlet(holder,"/show");
        server.setHandler(servlets);

        server.start();
        server.join();
    }
}
