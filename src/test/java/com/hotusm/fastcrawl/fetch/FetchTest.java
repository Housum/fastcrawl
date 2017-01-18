package com.hotusm.fastcrawl.fetch;

import com.hotusm.fastcrawl.common.container.*;
import com.hotusm.fastcrawl.util.HttpReturnMessage;

import java.io.FileOutputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by luqibao on 2017/1/12.
 */
public class FetchTest {

    public static void main(String[] args) throws Exception{

        long startTime=System.currentTimeMillis();
        final LinkBucket linkBucket= new QueueLinkBucket<ATag>();
        final PageData pageData=new PageDataImpl();
        final DiscardData discardData=new DiscardDataImpl();

        linkBucket.push(new ATag("百度","http://www.baidu.com"));
        linkBucket.push(new ATag("豆瓣读书","http://book.douban.com/"));
        linkBucket.push(new ATag("博客园","http://www.cnblogs.com/"));
        linkBucket.push(new ATag("CSDN","http://www.csdn.net/"));
        linkBucket.push(new ATag("Linux中top和free命令详解(转)","http://www.cnblogs.com/zr520/p/6266085.html"));
        linkBucket.push(new ATag("is not in the sudoers file的解决方法","http://www.cnblogs.com/zr520/p/6071557.html"));
        linkBucket.push(new ATag("error","www.baidu.com"));
        linkBucket.push(new ATag("金融","http://www.book.douban.com/tag/金融"));




        DelayAndRetryLoad load=new DelayAndRetryLoadImpl(linkBucket,pageData,discardData);

        load.delayLoad();

      /*  new Thread(){
            @Override
            public void run() {
                while (true){
                    try{
                        TimeUnit.SECONDS.sleep(20);
                    }catch (Exception e){}
                    Collection<ATag> tags=discardData.getAll();
                    System.out.println("失败的个数： "+discardData.getAll().size()+"  内容："+tags);
                }
            }
        }.start();*/

       writeHtml(pageData);

       /* synchronized (FetchTest.class){

            try{
                FetchTest.class.wait();
            }catch (Exception e){

            }
        }*/
    }

    //测试类  写html
    private static void writeHtml(final PageData pageData){

        int cpuNum=2*Runtime.getRuntime().availableProcessors();
        Executor executor= Executors.newFixedThreadPool(cpuNum);

       for(int i=0;i<cpuNum;i++){
           executor.execute(  new Thread(){
               @Override
               public void run() {
                   while(true){
                       try{
                           TimeUnit.SECONDS.sleep(5);
                           HttpReturnMessage message= pageData.get();
                           FileOutputStream channel=new FileOutputStream("C://html//"+message.getUrl().replace(":","")
                                   .replace(".","").replace("/","")
                                   +".html");
                           channel.write(message.getResult().getBytes("UTF-8"));
                           channel.close();
                           //遇到多次的写入的情况  必须将缓存区的清理干净 不然的话会将前一次的也写入
                       }catch (InterruptedException e){
                           Thread.currentThread().interrupt();
                       }catch (Exception e){
                           e.printStackTrace();
                       }

                   }
               }
           });
       }
    }
}
