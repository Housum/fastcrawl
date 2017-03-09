package com.hotusm.fastcrawl.parser;

/**
 * @author luqibao
 * @date 2017/3/9
 * html 页面的环境
 */
public class HtmlContext {

    private String html;
    private String url;

    public HtmlContext(String html, String url) {
        this.html = html;
        this.url = url;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "HtmlContext{" +
                "html='" + html + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
