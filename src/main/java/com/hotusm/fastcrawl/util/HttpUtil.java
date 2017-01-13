package com.hotusm.fastcrawl.util;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Map.Entry;

/**
 * httpClient工具类<br>
 * 该工具类目前仅支持Http请求，Https请求请走其他工具类
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

        // 构建Url
        URI uri=new URI(httpUrl,"UTF-8");
        GetMethod method=new GetMethod();
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

        String content=resp.getResponseBodyAsString();
        // 关闭流

        if (log.isDebugEnabled()) {
            String msg = String.format(" Get Http Response , The Http Status Code is %s , Return Message = [ %s ] , ",
                    code, content);
            log.debug(msg);
        }

        hrm.setResult(content);
        hrm.setUrl(resp.getURI().toString());
        return hrm;
    }

    public static String buildCookie(){
        return "bid=9y1bUpDz-2k; gr_user_id=b4c4582f-75c7-449b-bccc-461762a143da; ll='108288'; _ga=GA1.2.177459142.1479353158; ct=y; viewed='10750155_1767741_4941558_26871656_3118650_1881032_5325731_1217156_1833287_1043008'; _pk_ref.100001.3ac3=%5B%22%22%2C%22%22%2C1479447406%2C%22https%3A%2F%2Fwww.douban.com%2F%22%5D; ap=1; __utmt=1; gr_session_id_22c937bbd8ebd703f2d8e9445f7dfd03=e003e2cb-8f2c-4a8a-b98e-8be75672ad0d; gr_cs1_e003e2cb-8f2c-4a8a-b98e-8be75672ad0d=user_id%3A0; _pk_id.100001.3ac3=bc0cbf569ff6215a.1479353158.16.1479452671.1479444186.; _pk_ses.100001.3ac3=*; __utmt_douban=1; __utma=30149280.177459142.1479353158.1479447406.1479452668.17; __utmb=30149280.2.10.1479452668; __utmc=30149280; __utmz=30149280.1479452668.17.5.utmcsr=baidu|utmccn=(organic)|utmcmd=organic; __utma=81379588.893501403.1479353158.1479444186.1479447406.16; __utmb=81379588.21.10.1479447406; __utmc=81379588; __utmz=81379588.1479436562.14.5.utmcsr=douban.com|utmccn=(referral)|utmcmd=referral|utmcct=/; _vwo_uuid_v2=FCFCADC78AA1CB7A484DFC18CB40AC1E|5e669b2d269df13d5ffc743101311287";
    }

/*    private static List<NameValuePair> getNameValuePairList(Map<String, String> params) {
        List<NameValuePair> listParam = new ArrayList<NameValuePair>();
        for (String key : params.keySet()) {
            listParam.add(new BasicNameValuePair(key, params.get(key)));
        }
        return listParam;
    }*/
}