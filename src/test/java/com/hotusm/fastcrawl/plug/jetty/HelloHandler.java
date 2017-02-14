package com.hotusm.fastcrawl.plug.jetty;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 *
 */
public class HelloHandler extends AbstractHandler{

    private StatisticsHandler statisticsHandler;


    public void setStatisticsHandler(StatisticsHandler statisticsHandler) {
        this.statisticsHandler = statisticsHandler;
    }

    /**
     *
     * @param s   可以是一个URI或者是一个转发到这的处理器的名字
     * @param request  原始请求
     * @param httpServletRequest  被包装过的请求
     * @param httpServletResponse  响应
     * @throws IOException
     * @throws ServletException
     */
    public void handle(String s, Request request, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws IOException, ServletException {

        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.getWriter().write(this.statisticsHandler.toStatsHTML());
        request.setHandled(true);
    }

    public static void main(String[] args) throws Exception{

        Server server=new Server(8080);

        /**
         * 以集合的方式设置Handler
         * 会一级一级的往下处理
         */
        HandlerCollection hc=new HandlerCollection();


        HelloHandler helloHandler=new HelloHandler();
        StatisticsHandler statisticsHandler=new StatisticsHandler();
        DefaultHandler defaultHandler=new DefaultHandler();

        helloHandler.setStatisticsHandler(statisticsHandler);
        hc.addHandler(statisticsHandler);
        hc.addHandler(helloHandler);
        hc.addHandler(defaultHandler);

        /*
            只会处理一个 只要在处理的时候遇到了request.isHandled()返回true
            那么后面就不会处理了
            HandlerList handlerList=new HandlerList();
            handlerList.setHandlers(new Handler[]{statisticsHandler,helloHandler,defaultHandler});
            server.setHandler(handlerList);
        */


        server.setHandler(hc);
        server.start();
        server.join();
    }



}
