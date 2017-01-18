package com.hotusm.fastcrawl.fetch;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Created by luqibao on 2017/1/12.
 */
public class CommonTest {

    @Test
    public void testInteger(){

        Integer integer=0;
        System.out.println(integer);
    }

    @Test
    public  void testBoolean(){

        System.out.println(String.format("%s",true));
    }

    @Test
    public void testWhile(){

        while (true){
            System.out.println(System.currentTimeMillis());
        }
    }

    @Test
    public void testThread(){
        final List<String> list=new ArrayList<String>();
        final CountDownLatch latch=new CountDownLatch(2);
        new Thread(){
            @Override
            public void run() {
                latch.countDown();
                try{
                    latch.await();
                }catch (Exception e){}
                for (int i=0;i<10000;i++){
                    synchronized (CommonTest.class){
                        if(!list.contains(i+"")){
                            list.add(i+"");
                        }
                    }
                }
            }
        }.start();

        new Thread(){
            @Override
            public void run() {
                latch.countDown();
                try{
                    latch.await();
                }catch (Exception e){}
                for (int i=0;i<10000;i++){
                    synchronized (CommonTest.class){
                        if(!list.contains(i+"")){
                            list.add(i+"");
                        }
                    }
                }
            }
        }.start();

        try{
            TimeUnit.SECONDS.sleep(10);
        }catch (Exception e){
        }

        for (String s:list){
            System.out.println(s);
        }

    }
}
