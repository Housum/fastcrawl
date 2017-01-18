package com.hotusm.fastcrawl.util;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Created by luqibao on 2017/1/18.
 */
public class StreamUtil {

    private StreamUtil(){}

    public static void writeFile(String content,String fileName) throws Exception {
        writeFile(content,fileName,false);
    }

    public static void writeFile(byte[] bytes,String fileName){


    }

    public static void writeFile(String content,String fileName,boolean append)throws Exception{
        FileWriter channel=new FileWriter(fileName,append);
        BufferedWriter bw=new BufferedWriter(channel);
        bw.write(content);
        bw.close();
        channel.close();
    }

    public static void writeFile(byte[] bytes,String fileName,boolean append){

    }
}
