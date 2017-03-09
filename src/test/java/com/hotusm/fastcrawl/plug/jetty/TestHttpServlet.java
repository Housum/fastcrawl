package com.hotusm.fastcrawl.plug.jetty;

import com.hotusm.fastcrawl.common.container.DiscardData;
import com.hotusm.fastcrawl.fetch.ATag;
import com.hotusm.fastcrawl.parser.ParsedData;
import com.hotusm.fastcrawl.parser.SuccessPage;
import org.apache.commons.collections4.CollectionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by luqibao on 2017/1/19.
 */
@WebServlet(urlPatterns = "/test-httpservlet")
public class TestHttpServlet extends HttpServlet {

    private volatile DiscardData discardData;
    private volatile ParsedData parsedData;
    private volatile SuccessPage successPage;

    public void setSuccessPage(SuccessPage successPage) {
        this.successPage = successPage;
    }

    public TestHttpServlet(DiscardData discardData, ParsedData parsedData,SuccessPage successPage) {

        this.discardData = discardData;
        this.parsedData = parsedData;
        this.successPage=successPage;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setCharacterEncoding("UTF-8");
        Writer writer = resp.getWriter();

        StringBuilder sb = new StringBuilder();
        sb.append("统计界面:");
        sb.append("\r\n");

        Collection<ATag> collection=null;

        synchronized (discardData){
            collection=new ArrayList(discardData.getAll());
        }
        if(CollectionUtils.isNotEmpty(collection)){
                sb.append(String.format("失败总数%d 详细信息:\r\n",collection.size()));
                for (ATag aTag : collection) {
                    sb.append(String.format("名称: %s,url: %s \r\n", aTag.getName(), aTag.getHref()));
                }
        }

        sb.append("\r\n");
        sb.append("\r\n");
        Collection<Object> parsedObj=null;

        synchronized (parsedData) {
            parsedObj = new ArrayList<Object>(parsedData.getAll());
        }
        if (CollectionUtils.isNotEmpty(parsedObj)) {
            sb.append(String.format("解析a标签数量 %d\r\n",parsedObj.size()));
//            for (Object obj : parsedObj) {
//                sb.append(String.format("url: %s  \r\n", obj));
//            }
        }


        sb.append("\r\n");
        sb.append("\r\n");
        Map<String,String> successPage=null;

        synchronized (this.successPage){
            successPage=new HashMap(this.successPage.getAll());
        }
        if (successPage!=null&&successPage.size()>0) {
            sb.append(String.format("数量文章：%d",successPage.size()));
            sb.append("\r\n");
            sb.append("\r\n");
            for (Map.Entry<String,String> entry:successPage.entrySet()) {
                sb.append(String.format("标题: %s  url: %s \r\n", entry.getKey(),"<a href='>"+entry.getValue()+"</a>"));
            }
        }
        writer.write(sb.toString());
        writer.close();
    }

}

