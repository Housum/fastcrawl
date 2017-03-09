package com.hotusm.fastcrawl.parser;

import com.hotusm.fastcrawl.common.container.PageData;
import com.hotusm.fastcrawl.common.container.PageDataImpl;
import com.hotusm.fastcrawl.util.HttpReturnMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author hotusm
 */
public abstract class AbstractParser implements Parser{

    private static final Logger LOGGER=LogManager.getLogger(AbstractParser.class);

    private static final int NCPU=Runtime.getRuntime().availableProcessors();

    //解析下载好网页中实体的工厂
    private ThreadPoolExecutor parserPool=new ThreadPoolExecutor(NCPU,NCPU+1,30L,
            TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());

    //解析网页中A标签的工厂
    private ThreadPoolExecutor tagPool=new ThreadPoolExecutor(NCPU,NCPU+1,30L,
            TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>());

    //下载好的网页
    private PageData pageData;

    //a标签的解析器 @see ATagParserWork
    private volatile ParserWork aTagParserWork;

    //实体的解析器 需要注入 这个是用户实现的
    private ParserWork pageParserWork;

    public AbstractParser() {

    }

    /**
     *
     * @param pageData   存放下载的页面
     * @param pageParserWork 解析下载好网页
     * @param aTagParserWork 解析每个网页中的a标签
     *                       @see ATagParserWork
     */
    public AbstractParser(PageData pageData,ParserWork pageParserWork,ParserWork aTagParserWork){
        this.pageData=pageData;
        this.pageParserWork=pageParserWork;
        this.aTagParserWork=aTagParserWork;
    }

    public void load() {
        checkEnvironment();
        long startTime=System.currentTimeMillis();
        LOGGER.info("解析网页的工作正在开始");
        for (int i=0;i<NCPU;i++){
            parserPool.execute(new SteadilyWork());
        }
        long endTime=System.currentTimeMillis()-startTime;
        LOGGER.info(String.format("解析工作启动完成,耗时 %d",endTime));
    }
    // 检查参数是否全部设置了
    private void checkEnvironment(){

        if(pageParserWork==null){
            LOGGER.error("pageParserWork is null!!!");
            throw new RuntimeException();
        }
        if(aTagParserWork==null){
            LOGGER.error("aTagParserWork is null!!!");
            throw new RuntimeException();
        }
        if(pageData==null){
            LOGGER.warn(" pageDate is null,use default implement:PageDataImpl");
            pageData=new PageDataImpl();
        }

    }

    /**
     * 是否是满足爬虫的条件 如果满足的话 那么就解析出需要的实体
     * 如果没有的话 那么就直接单独解析a标签可以了
     * @param html
     * @return
     */
    public abstract boolean isMatch(String html);

    public PageData getPageData() {
        return pageData;
    }

    public void setPageData(PageData pageData) {
        this.pageData = pageData;
    }

    public ParserWork getaTagParserWork() {
        return aTagParserWork;
    }

    public void setaTagParserWork(ParserWork aTagParserWork) {
        this.aTagParserWork = aTagParserWork;
    }

    public ParserWork getPageParserWork() {
        return pageParserWork;
    }

    public void setPageParserWork(ParserWork pageParserWork) {
        this.pageParserWork = pageParserWork;
    }

    //TODO 这里需要修改
    //工作队列
    private class SteadilyWork implements Runnable{

        public void run() {
           while (true){
                try{
                    HttpReturnMessage message= pageData.get();
                    //继续解析网页中的a标签
                    tagPool.execute(new ParserNode(aTagParserWork,new HtmlContext(message.getResult(),message.getUrl())));
                    if(isMatch(message.getResult())){
                        if(LOGGER.isDebugEnabled()){
                            LOGGER.debug(String.format("满足条件,开始抓取数据,url :%s",message.getUrl()));
                        }
                        pageParserWork.doParser(new HtmlContext(message.getResult(),message.getUrl()));
                    }
                    if(LOGGER.isDebugEnabled()){
                        LOGGER.debug(String.format("开始解析,url :%s",message.getUrl()));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Thread.interrupted();
                }
            }
        }
    }

    //解析下载网页的工作单元
    private static class ParserNode implements Runnable{

        private volatile ParserWork parserWork;

        private final HtmlContext context;

        public ParserNode(ParserWork parserWork, HtmlContext context) {
            this.parserWork = parserWork;
            this.context=context;
        }

        public void run() {
            parserWork.doParser(context);
        }
    }
}
