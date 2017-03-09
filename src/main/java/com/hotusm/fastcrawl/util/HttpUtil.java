package com.hotusm.fastcrawl.util;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.SSLProtocolSocketFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Map.Entry;

/**
 * httpClient工具类<br>
 * 该工具类目前仅支持Http请求
 */
public class HttpUtil {

    private final static Logger log = LogManager.getLogger(HttpUtil.class);

    private static final int CONNECTION_TIMEOUT = ConfigUtil.getIntValue("http.connection.timeout");
    private static final int SOCKET_TIMEOUT = ConfigUtil.getIntValue("http.socket.timeout");
    private static final int REQUEST_TIMEOUT = ConfigUtil.getIntValue("http.request.timeout");

    private static final int MAX_CONNECTION = ConfigUtil.getIntValue("http.connection.maxnum");
    private static final int MAX_ROUTE_CONNECTIONS = ConfigUtil.getIntValue("http.route.maxnum");

    private static final Object lock = new Object();

    private static HttpConnectionManager clientBuilder;

    private static HttpConnectionManager getHttpConnectionManager() {
        if (clientBuilder != null) {
            return clientBuilder;
        }

        synchronized (lock) {
            if (clientBuilder != null) {
                return clientBuilder;
            }
            HttpConnectionManagerParams params=new HttpConnectionManagerParams();
            params.setMaxTotalConnections(MAX_CONNECTION);
            MultiThreadedHttpConnectionManager  clientBuilder=new MultiThreadedHttpConnectionManager();
            clientBuilder.setParams(params);
            HttpUtil.clientBuilder = clientBuilder;
        }
        return clientBuilder;
    }


    /**
     * 从pool中获取一个HttpClient对象来执行Get/Post/Delete/Put等方法
     *
     * @return
     * @throws Exception
     */
    private static HttpClient getClient() throws Exception {

        // 实例化客户端
        return new HttpClient(getHttpConnectionManager());
    }



    /**
     * 发送Get请求，参数默认
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static HttpReturnMessage doGet(String url) throws Exception {
        return doGet(url, null, null, -1);
    }

    /**
     * 发送Get请求，带有超时时间<br>
     * 其他默认
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static HttpReturnMessage doGet(String url, int timeout) throws Exception {
        return doGet(url, null, null, timeout);
    }

    /**
     * 放Get请求，带有参数<br>
     * 其他默认
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     */
    public static HttpReturnMessage doGet(String url, Map<String, String> params) throws Exception {
        return doGet(url, params, null, -1);
    }

    /**
     * 发用Get请求，带有超时时间和请求参数
     *
     * @param url
     * @param params
     * @param timeout
     * @return
     * @throws Exception
     */
    public static HttpReturnMessage doGet(String url, Map<String, String> params, int timeout) throws Exception {
        return doGet(url, params, null, timeout);
    }

    /**
     * 发送Get请求，自定义所有参数<br>
     *
     * @param httpUrl
     * @param params
     * @param headers
     * @param timeout
     * @return
     * @throws Exception
     */
    public static HttpReturnMessage doGet(String httpUrl, Map<String, String> params, Map<String, String> headers,
                                          int timeout) throws Exception {

        if (log.isDebugEnabled()) {
            int t = timeout;
            if (t < 0) {
                t = REQUEST_TIMEOUT;
            }
            String msg = String
                    .format("Inovke Http Interface By Get Method , The URL is %s , params  = [ %s ] , headers = [ %s ] , timeout = %d",
                            httpUrl, params, headers, t);
            log.debug(msg);
        }

        GetMethod method=null;
        // 构建Url
        URI uri=new URI(httpUrl,"UTF-8");
        if(httpUrl.startsWith("https")){
            Protocol myhttps = new Protocol("https", new SSLProtocolSocketFactory(), 443);
            Protocol.registerProtocol("https", myhttps);
        }
        method=new GetMethod();
        method.setURI(uri);

        // 设置请求参数

        HttpMethodParams httpParams=null;
        if (params != null) {

            httpParams=new HttpMethodParams();
            for (Entry<String, String> entry : params.entrySet()) {
                httpParams.setParameter(entry.getKey(), entry.getValue());
            }

        }
        if(timeout>0){
            if(params==null){
                httpParams.setSoTimeout(timeout);
            }
        }
        if(params!=null){
            method.setParams(httpParams);
        }
        // 设置header
        if (headers != null) {
            for (Entry<String, String> entry : headers.entrySet()) {
                method.addRequestHeader(entry.getKey(), entry.getValue());
            }
        }

        // 执行请求
        getClient().executeMethod(method);
        return getHttpBody(method);
    }

  /*  *//**
     * 发送post请求，使用默认超时时间
     *
     * @param url
     * @param params
     * @return
     * @throws Exception
     *//*
    public static HttpReturnMessage doPost(String url, Map<String, String> params) throws Exception {
        return doPost(url, params, null, -1);
    }

    *//**
     * 发送post请求，带有请求参数和超时时间
     *
     * @param url
     * @param params
     * @param timeout
     * @return
     * @throws Exception
     *//*
    public static HttpReturnMessage doPost(String url, Map<String, String> params, int timeout) throws Exception {
        return doPost(url, params, null, timeout);
    }


    *//**
     * 上传文件<br>
     * 使用Http-mine包上传<br>
     * 默认超时时间 @see{REQUEST_TIMEOUT}
     *
     * @param httpUrl
     * @param files
     * @param params
     * @return
     * @throws Exception
     *//*
    public static HttpReturnMessage upload(String httpUrl, Map<String, byte[]> files, Map<String, String> params)
            throws Exception {
        return upload(httpUrl, files, params, -1);
    }

    *//**
     * * 上传文件<br>
     * 使用Http-mine包上传<br>
     *
     * @param httpUrl
     * @param files
     * @param params
     * @param timeout
     * @return
     * @throws Exception
     *//*
    public static HttpReturnMessage upload(String httpUrl, Map<String, byte[]> files, Map<String, String> params,
                                           int timeout) throws Exception {

        if (log.isDebugEnabled()) {
            int t = timeout;
            if (t < 0) {
                t = REQUEST_TIMEOUT;
            }
            String msg = String.format(
                    "Upload File To Http Interface , Thre Url is %s , params = [ %s ] , timeout = { %d }", httpUrl,
                    params, t);
            log.debug(msg);
        }

        //
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.RFC6532);// 设置游览器兼容模式
        builder.setBoundary(UUID.randomUUID().toString());
        builder.setCharset(Charset.forName("UTF-8"));
        // 加入普通参数
        Set<Entry<String, String>> pes = params.entrySet();
        ContentType ctype = ContentType.create("text/plain", Charset.forName("UTF-8"));
        for (Entry<String, String> ps : pes) {
            StringBody stringBody = new StringBody(ps.getValue(), ctype);
            builder.addPart(ps.getKey(), stringBody);
        }
        // 加入文件内容
        Set<Entry<String, byte[]>> es = files.entrySet();
        for (Entry<String, byte[]> bs : es) {
            builder.addBinaryBody(bs.getKey(), bs.getValue(), ContentType.MULTIPART_FORM_DATA, bs.getKey());
        }
        // 实例化客户端
        HttpPost post = new HttpPost(httpUrl);
        if (timeout > 0) {// 超时配置
            Builder reqCfg = RequestConfig.copy(defalutRequectCfg);
            reqCfg.setConnectionRequestTimeout(timeout);
            post.setConfig(reqCfg.build());
        }
        // 发送
        post.setEntity(builder.build());

        return getHttpBody(getClient().execute(post));
    }*/

    /**
     * 获取HttpBody
     *
     * @param resp
     * @return
     * @throws Exception
     */
    private static HttpReturnMessage getHttpBody(HttpMethod resp) throws Exception {

        int code = resp.getStatusLine().getStatusCode();
        HttpReturnMessage hrm = new HttpReturnMessage(code);

        byte[] content=resp.getResponseBody();
        // 关闭流

        if (log.isDebugEnabled()) {
            String msg = String.format(" Get Http Response , The Http Status Code is %s , Return Message = [ %s ] , ",
                    code, content);
            log.debug(msg);
        }

        hrm.setResult(new String(content,"UTF-8"));
        hrm.setUrl(resp.getURI().toString());
        return hrm;
    }

    public static String buildCookie(){
        //return "bid=fQO07KBnyQc; gr_user_id=7f52d6de-7877-4f9a-a85e-1195e4033283; viewed=\"4251875_1239558_3376642_26176870\"; _pk_ref.100001.3ac3=%5B%22%22%2C%22%22%2C1489029665%2C%22https%3A%2F%2Fwww.google.at%2F%22%5D; ps=y; gr_cs1_8e89f4ee-f60a-48f3-b735-a0b7706f6a22=user_id%3A0; ll=\"108288\"; dbcl2=\"158824779:90KjAnKNXic\"; ck=bFs4; __utmt_douban=1; gr_session_id_22c937bbd8ebd703f2d8e9445f7dfd03=bd6f5ed4-1f32-4353-8eaa-17b368d0737d; gr_cs1_bd6f5ed4-1f32-4353-8eaa-17b368d0737d=user_id%3A1; _pk_id.100001.3ac3=ce7775d14e4b6109.1487674425.5.1489033164.1488958978.; _pk_ses.100001.3ac3=*; __utma=30149280.169797740.1487303477.1488958979.1489029668.10; __utmb=30149280.18.5.1489031278519; __utmc=30149280; __utmz=30149280.1488958979.9.8.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided); __utmt=1; __utma=81379588.1031180968.1487674426.1488958979.1489029668.5; __utmb=81379588.10.10.1489029668; __utmc=81379588; __utmz=81379588.1488958979.4.3.utmcsr=google|utmccn=(organic)|utmcmd=organic|utmctr=(not%20provided); _vwo_uuid_v2=401FAE66B5B2E3F754BC2EA7BFE323C9|147cfe1cbddbf5c0c48f8c25e2082cf4; push_noty_num=0; push_doumail_num=0";

        return "user-key=41b72461-7a09-49c7-9ada-1a298af13a26; o2-webp=true; ipLoc-djd=21-1827-3504-51731.138175816; cn=7; userInfo2016=1; dmpjs=dmp-d110769bd1929d9d4efa5b7e46d3166ebdd4fb7; TrackID=15M6KpVvebHpyN_y9IM20FGpRoUyKo94tJNNbmrgOKTk196RP03xSofFjY9UX5fl94PxSt2BMAiVNKyUYAhjQFEG4fBlJtWVctiRPmSkh2e0; pinId=2mbmgePZ7CvAwpbPLiM22g; pin=Hotsumadobeunm; unick=Hotsumadobeunm; _tp=Ic1N1sLjstOzcmYrnPPu%2Fw%3D%3D; _pst=Hotsumadobeunm; ceshi3.com=000; unpl=V2_ZzNtbRAHFBclD0MALhwMAWJTFVpKVBcWdVhGV3gZWlFvABQPclRCFXMUR1ZnGFQUZwEZWEdcQBRFCEdkex1dAWMzIm1BV3McRQwWB30YCVEzB0ZVS1MRQCZcQgR9HV4BYAcSXxEFQBV1OHZTSxlcDWIAFlRHZ5qVwt7kxqKH1tDvlCJaR1BFEnwLQWR6KV01NW3K6%2fSP5qRp3OzbrpHLSGNTQVtDAhdBcVxOXX9LCVYzB0JbRlVHEnEIRAcpGlwFVwIiXg%3d%3d; __jdv=122270672|book.douban.com|t_15055_|tuiguang|caf3a64ed4a54a6692e21a12217e927c|1489038265910; ipLocation=%u6C5F%u897F; areaId=21; __jda=122270672.5ab70dee5e985cdbe5a75356513bc211.1487209292.1488508409.1489038266.19; __jdb=122270672.22.5ab70dee5e985cdbe5a75356513bc211|19.1489038266; __jdc=122270672; __jdu=5ab70dee5e985cdbe5a75356513bc211";
    }

/*    private static List<NameValuePair> getNameValuePairList(Map<String, String> params) {
        List<NameValuePair> listParam = new ArrayList<NameValuePair>();
        for (String key : params.keySet()) {
            listParam.add(new BasicNameValuePair(key, params.get(key)));
        }
        return listParam;
    }*/
}